package com.lody.virtual.client.hook.proxies.content;

import android.content.pm.ProviderInfo;
import android.database.IContentObserver;
import android.net.Uri;
import android.os.Build.VERSION;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.ipc.VContentManager;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.helper.Keep;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.os.VUserHandle;
import java.lang.reflect.Method;

@Keep
public class MethodProxies {
   private static boolean isAppUri(Uri uri) {
      ProviderInfo info = VPackageManager.get().resolveContentProvider(uri.getAuthority(), 0, VUserHandle.myUserId());
      return info != null && info.enabled;
   }

   public static Object registerContentObserver(Object who, Method method, Object[] args) throws Throwable {
      if (VERSION.SDK_INT >= 24 && args.length >= 5) {
         args[4] = 22;
      }

      Uri uri = (Uri)args[0];
      boolean notifyForDescendents = (Boolean)args[1];
      IContentObserver observer = (IContentObserver)args[2];
      if (isAppUri(uri)) {
         VContentManager.get().registerContentObserver(uri, notifyForDescendents, observer, VUserHandle.myUserId());
         return 0;
      } else {
         MethodProxy.replaceFirstUserId(args);
         return method.invoke(who, args);
      }
   }

   public static Object unregisterContentObserver(Object who, Method method, Object[] args) throws Throwable {
      IContentObserver observer = (IContentObserver)args[0];
      VContentManager.get().unregisterContentObserver(observer);
      return method.invoke(who, args);
   }

   public static Object notifyChange(Object who, Method method, Object[] args) throws Throwable {
      if (VERSION.SDK_INT >= 24 && args.length >= 6) {
         args[5] = 22;
      }

      IContentObserver observer = (IContentObserver)args[1];
      boolean observerWantsSelfNotifications = (Boolean)args[2];
      boolean syncToNetwork;
      if (args[3] instanceof Integer) {
         int flags = (Integer)args[3];
         syncToNetwork = (flags & 1) != 0;
      } else {
         syncToNetwork = (Boolean)args[3];
      }

      if (BuildCompat.isR()) {
         MethodProxy.replaceLastUserId(args);
         Uri[] uris = (Uri[])args[0];
         Uri[] var7 = uris;
         int var8 = uris.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            Uri uri = var7[var9];
            if (isAppUri(uri)) {
               VContentManager.get().notifyChange(uri, observer, observerWantsSelfNotifications, syncToNetwork, VUserHandle.myUserId());
            } else {
               method.invoke(who, args);
            }
         }

         return 0;
      } else {
         Uri uri = (Uri)args[0];
         if (isAppUri(uri)) {
            VContentManager.get().notifyChange(uri, observer, observerWantsSelfNotifications, syncToNetwork, VUserHandle.myUserId());
            return 0;
         } else {
            MethodProxy.replaceLastUserId(args);
            return method.invoke(who, args);
         }
      }
   }
}
