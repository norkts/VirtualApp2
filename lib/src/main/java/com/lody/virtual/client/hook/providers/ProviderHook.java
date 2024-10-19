package com.lody.virtual.client.hook.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.MethodBox;
import com.lody.virtual.client.hook.secondary.ProxyBinder;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.VLog;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import mirror.android.content.IContentProvider;

public class ProviderHook implements InvocationHandler {
   private static final Map<String, HookFetcher> PROVIDER_MAP = new HashMap();
   public static final String QUERY_ARG_SQL_SELECTION = "android:query-arg-sql-selection";
   public static final String QUERY_ARG_SQL_SELECTION_ARGS = "android:query-arg-sql-selection-args";
   public static final String QUERY_ARG_SQL_SORT_ORDER = "android:query-arg-sql-sort-order";
   protected final IInterface mBase;
   protected IInterface mProxy;
   protected ProxyBinder mProxyBinder;

   public ProviderHook(IInterface base) {
      this.mBase = base;
      this.mProxy = (IInterface)Proxy.newProxyInstance(IContentProvider.TYPE.getClassLoader(), new Class[]{IContentProvider.TYPE}, this);
      this.mProxyBinder = new ProxyBinder(this.mBase.asBinder(), this.mProxy);
   }

   public IInterface getProxyInterface() {
      return this.mProxy;
   }

   private static HookFetcher fetchHook(String authority) {
      VLog.d("HV-", "fetchHook authority:" + authority);
      HookFetcher fetcher = (HookFetcher)PROVIDER_MAP.get(authority);
      if (fetcher == null) {
         fetcher = new HookFetcher() {
            public ProviderHook fetch(boolean external, IInterface provider) {
               return (ProviderHook)(external ? new ExternalProviderHook(provider) : new InternalProviderHook(provider));
            }
         };
      }

      return fetcher;
   }

   public static IInterface createProxy(boolean external, String authority, IInterface provider) {
      if (provider instanceof Proxy && Proxy.getInvocationHandler(provider) instanceof ProviderHook) {
         return provider;
      } else {
         HookFetcher fetcher = fetchHook(authority);
         if (fetcher != null) {
            ProviderHook hook = fetcher.fetch(external, provider);
            IInterface proxyProvider = hook.getProxyInterface();
            if (proxyProvider != null) {
               provider = proxyProvider;
            }
         }

         return provider;
      }
   }

   public Bundle call(MethodBox methodBox, String method, String arg, Bundle extras) throws InvocationTargetException {
      Object[] args = methodBox.args;
      int start = this.getCallIndex(methodBox);
      args[start] = method;
      args[start + 1] = arg;
      args[start + 2] = extras;
      return (Bundle)methodBox.call();
   }

   public int getCallIndex(MethodBox methodBox) {
      return methodBox.args.length - 3;
   }

   public Uri insert(MethodBox methodBox, Uri url, ContentValues initialValues) throws InvocationTargetException {
      Object[] args = methodBox.args;
      int start = MethodParameterUtils.getIndex(args, Uri.class);
      args[start] = url;
      args[start + 1] = initialValues;
      return (Uri)methodBox.call();
   }

   public Cursor query(MethodBox methodBox, Uri url, String[] projection, String selection, String[] selectionArgs, String sortOrder, Bundle originQueryArgs) throws InvocationTargetException {
      Object[] args = methodBox.args;
      int start = MethodParameterUtils.getIndex(args, Uri.class);
      args[start] = url;
      args[start + 1] = projection;
      if (BuildCompat.isOreo()) {
         if (originQueryArgs != null) {
            originQueryArgs.putString(QUERY_ARG_SQL_SELECTION, selection);
            originQueryArgs.putStringArray(QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs);
            originQueryArgs.putString(QUERY_ARG_SQL_SORT_ORDER, sortOrder);
         }
      } else {
         args[start + 2] = selection;
         args[start + 3] = selectionArgs;
         args[start + 4] = sortOrder;
      }

      return (Cursor)methodBox.call();
   }

   public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
      try {
         this.processArgs(method, args);
      } catch (Throwable var13) {
         Throwable e = var13;
         e.printStackTrace();
      }

      MethodBox methodBox = new MethodBox(method, this.mBase, args);

      try {
         String name = method.getName();
         int start;
         if ("call".equals(name)) {
            start = this.getCallIndex(methodBox);
            String methodName = (String)args[start];
            String arg = (String)args[start + 1];
            Bundle extras = (Bundle)args[start + 2];
            return this.call(methodBox, methodName, arg, extras);
         } else {
            Uri url;
            if ("insert".equals(name)) {
               start = MethodParameterUtils.getIndex(args, Uri.class);
               url = (Uri)args[start];
               ContentValues initialValues = (ContentValues)args[start + 1];
               return this.insert(methodBox, url, initialValues);
            } else if ("query".equals(name)) {
               start = MethodParameterUtils.getIndex(args, Uri.class);
               url = (Uri)args[start];
               String[] projection = (String[])args[start + 1];
               String selection = null;
               String[] selectionArgs = null;
               String sortOrder = null;
               Bundle queryArgs = null;
               if (BuildCompat.isOreo()) {
                  queryArgs = (Bundle)args[start + 2];
                  if (queryArgs != null) {
                     selection = queryArgs.getString(QUERY_ARG_SQL_SELECTION);
                     selectionArgs = queryArgs.getStringArray(QUERY_ARG_SQL_SELECTION_ARGS);
                     sortOrder = queryArgs.getString(QUERY_ARG_SQL_SORT_ORDER);
                  }
               } else {
                  selection = (String)args[start + 2];
                  selectionArgs = (String[])args[start + 3];
                  sortOrder = (String)args[start + 4];
               }

               return this.query(methodBox, url, projection, selection, selectionArgs, sortOrder, queryArgs);
            } else {
               return "asBinder".equals(name) ? this.mProxyBinder : methodBox.call();
            }
         }
      } catch (Throwable var14) {
         Throwable e = var14;
         VLog.w("ProviderHook", "call: %s (%s) with error", method.getName(), Arrays.toString(args));
         if (e instanceof InvocationTargetException) {
            throw e.getCause();
         } else {
            throw e;
         }
      }
   }

   protected void processArgs(Method method, Object... args) {
   }

   static {
      PROVIDER_MAP.put("settings", new HookFetcher() {
         public ProviderHook fetch(boolean external, IInterface provider) {
            return new SettingsProviderHook(provider);
         }
      });
      PROVIDER_MAP.put("downloads", new HookFetcher() {
         public ProviderHook fetch(boolean external, IInterface provider) {
            return new DownloadProviderHook(provider);
         }
      });
      PROVIDER_MAP.put("com.android.badge", new HookFetcher() {
         public ProviderHook fetch(boolean external, IInterface provider) {
            return new BadgeProviderHook(provider);
         }
      });
      PROVIDER_MAP.put("com.huawei.android.launcher.settings", new HookFetcher() {
         public ProviderHook fetch(boolean external, IInterface provider) {
            return new BadgeProviderHook(provider);
         }
      });
   }

   public interface HookFetcher {
      ProviderHook fetch(boolean var1, IInterface var2);
   }
}
