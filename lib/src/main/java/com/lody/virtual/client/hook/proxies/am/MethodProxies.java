package com.lody.virtual.client.hook.proxies.am;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.IServiceConnection;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.NativeEngine;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.badger.BadgerManager;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.Constants;
import com.lody.virtual.client.env.SpecialComponentList;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.delegate.TaskDescriptionDelegate;
import com.lody.virtual.client.hook.providers.ProviderHook;
import com.lody.virtual.client.hook.secondary.ServiceConnectionProxy;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.client.stub.ChooserActivity;
import com.lody.virtual.client.stub.IntentBuilder;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.compat.ActivityManagerCompat;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.compat.BundleCompat;
import com.lody.virtual.helper.compat.ParceledListSliceCompat;
import com.lody.virtual.helper.compat.PendingIntentCompat;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.BitmapUtils;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.DrawableUtils;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.lody.virtual.remote.AppTaskInfo;
import com.lody.virtual.remote.ClientConfig;
import com.lody.virtual.remote.IntentSenderData;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;
import mirror.android.app.IActivityManager;
import mirror.android.app.LoadedApk;
import mirror.android.content.ContentProviderHolderOreo;
import mirror.android.content.IIntentReceiverJB;
import mirror.android.content.pm.ParceledListSlice;
import mirror.android.content.pm.UserInfo;

public class MethodProxies {
   public static class SetPictureInPictureParams extends MethodProxy {
      public String getMethodName() {
         return "SetPictureInPictureParams";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return null;
      }
   }

   public static class OverridePendingTransition extends MethodProxy {
      public String getMethodName() {
         return "overridePendingTransition";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return !VClient.get().isDynamicApp() ? 0 : super.call(who, method, args);
      }
   }

   static class GetPackageProcessState extends MethodProxy {
      public String getMethodName() {
         return "getPackageProcessState";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return 4;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class isUserRunning extends MethodProxy {
      public String getMethodName() {
         return "isUserRunning";
      }

      public Object call(Object who, Method method, Object... args) {
         int userId = (Integer)args[0];
         Iterator var5 = VUserManager.get().getUsers().iterator();

         VUserInfo userInfo;
         do {
            if (!var5.hasNext()) {
               return false;
            }

            userInfo = (VUserInfo)var5.next();
         } while(userInfo.id != userId);

         return true;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class CheckGrantUriPermission extends MethodProxy {
      public String getMethodName() {
         return "checkGrantUriPermission";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GrantUriPermission extends MethodProxy {
      public String getMethodName() {
         return "grantUriPermission";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         replaceLastUserId(args);
         if (args[2] != null && args[2] instanceof Uri) {
            args[2] = ComponentUtils.processOutsideUri(getAppUserId(), VirtualCore.get().isExtPackage(), (Uri)args[2]);
         }

         try {
            method.invoke(who, args);
            return null;
         } catch (Exception var9) {
            Exception exp = var9;
            if (exp.getCause() != null && exp.getCause() instanceof SecurityException) {
               StackTraceElement[] var5 = exp.getStackTrace();
               int var6 = var5.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  StackTraceElement element = var5[var7];
                  if (TextUtils.equals(element.getClassName(), Intent.class.getName()) && TextUtils.equals(element.getMethodName(), "migrateExtraStreamToClipData")) {
                     return null;
                  }
               }
            }

            throw exp;
         }
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetActivityClassForToken extends MethodProxy {
      public String getMethodName() {
         return "getActivityClassForToken";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IBinder token = (IBinder)args[0];
         ComponentName comp = VActivityManager.get().getActivityForToken(token);
         return comp == null ? method.invoke(who, args) : comp;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class BroadcastIntent extends MethodProxy {
      public String getMethodName() {
         return "broadcastIntent";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Intent intent = new Intent((Intent)args[1]);
         String type = (String)args[2];
         intent.setDataAndType(intent.getData(), type);
         Intent newIntent = this.handleIntent(intent);
         if (newIntent == null) {
            return 0;
         } else {
            args[1] = newIntent;
            if (args[7] instanceof String || args[7] instanceof String[]) {
               args[7] = null;
            }

            int index = ArrayUtils.indexOfFirst(args, Boolean.class);
            args[index] = false;
            replaceLastUserId(args);

            try {
               return method.invoke(who, args);
            } catch (Throwable var9) {
               return 0;
            }
         }
      }

      protected Intent handleIntent(Intent intent) {
         String action = intent.getAction();
         if (!"android.intent.action.CREATE_SHORTCUT".equals(action) && !"com.android.launcher.action.INSTALL_SHORTCUT".equals(action) && !"com.aliyun.homeshell.action.INSTALL_SHORTCUT".equals(action)) {
            if (!"com.android.launcher.action.UNINSTALL_SHORTCUT".equals(action) && !"com.aliyun.homeshell.action.UNINSTALL_SHORTCUT".equals(action)) {
               if ("android.intent.action.MEDIA_SCANNER_SCAN_FILE".equals(action)) {
                  return this.handleMediaScannerIntent(intent);
               } else if (BadgerManager.handleBadger(intent)) {
                  return null;
               } else {
                  if (intent.getComponent() != null) {
                     try {
                        final ActivityInfo receiverInfo = VirtualCore.get().getPackageManager().getReceiverInfo(intent.getComponent(), 0);
                        if (receiverInfo != null && VirtualCore.get().getProccessInfo(receiverInfo.processName, VClient.get().getVUid()) == null) {
                           VirtualCore.get().getHandlerASyc().post(new Runnable() {
                              public void run() {
                                 try {
                                    ProviderInfo providerInfo = new ProviderInfo();
                                    providerInfo.applicationInfo = receiverInfo.applicationInfo;
                                    providerInfo.packageName = receiverInfo.packageName;
                                    providerInfo.processName = receiverInfo.processName;
                                    providerInfo.authority = "_VA_START_PROCESS";
                                    VActivityManager.get().acquireProviderClient(VUserHandle.myUserId(), providerInfo);
                                 } catch (Exception var2) {
                                    Exception e = var2;
                                    e.printStackTrace();
                                 }

                              }
                           });
                        }
                     } catch (Exception var4) {
                     }
                  }

                  return ComponentUtils.proxyBroadcastIntent(intent, VUserHandle.myUserId());
               }
            } else {
               this.handleUninstallShortcutIntent(intent);
               return intent;
            }
         } else {
            return getConfig().isAllowCreateShortcut() ? this.handleInstallShortcutIntent(intent) : null;
         }
      }

      private Intent handleMediaScannerIntent(Intent intent) {
         if (intent == null) {
            return null;
         } else {
            Uri data = intent.getData();
            if (data == null) {
               return intent;
            } else {
               String scheme = data.getScheme();
               if (!"file".equalsIgnoreCase(scheme)) {
                  return intent;
               } else {
                  String path = data.getPath();
                  if (path == null) {
                     return intent;
                  } else {
                     String newPath = NativeEngine.getRedirectedPath(path);
                     File newFile = new File(newPath);
                     if (!newFile.exists()) {
                        return intent;
                     } else {
                        intent.setData(Uri.fromFile(newFile));
                        return intent;
                     }
                  }
               }
            }
         }
      }

      private Intent handleInstallShortcutIntent(Intent intent) {
         Intent shortcut = (Intent)intent.getParcelableExtra("android.intent.extra.shortcut.INTENT");
         if (shortcut != null) {
            ComponentName component = shortcut.resolveActivity(VirtualCore.getPM());
            if (component != null) {
               String pkg = component.getPackageName();
               Intent newShortcutIntent = new Intent();
               newShortcutIntent.addCategory("android.intent.category.DEFAULT");
               newShortcutIntent.setAction(Constants.ACTION_SHORTCUT);
               newShortcutIntent.setPackage(getHostPkg());
               newShortcutIntent.putExtra("_VA_|_intent_", shortcut);
               newShortcutIntent.putExtra("_VA_|_uri_", shortcut.toUri(0));
               newShortcutIntent.putExtra("_VA_|_user_id_", VUserHandle.myUserId());
               intent.removeExtra("android.intent.extra.shortcut.INTENT");
               intent.putExtra("android.intent.extra.shortcut.INTENT", newShortcutIntent);
               Intent.ShortcutIconResource icon = (Intent.ShortcutIconResource)intent.getParcelableExtra("android.intent.extra.shortcut.ICON_RESOURCE");
               if (icon != null && !TextUtils.equals(icon.packageName, getHostPkg())) {
                  try {
                     Resources resources = VirtualCore.get().getResources(pkg);
                     int resId = resources.getIdentifier(icon.resourceName, "drawable", pkg);
                     if (resId > 0) {
                        Drawable iconDrawable = resources.getDrawable(resId);
                        Bitmap newIcon = BitmapUtils.drawableToBitmap(iconDrawable);
                        if (newIcon != null) {
                           intent.removeExtra("android.intent.extra.shortcut.ICON_RESOURCE");
                           intent.putExtra("android.intent.extra.shortcut.ICON", newIcon);
                        }
                     }
                  } catch (Throwable var11) {
                     Throwable e = var11;
                     e.printStackTrace();
                  }
               }
            }
         }

         return intent;
      }

      private void handleUninstallShortcutIntent(Intent intent) {
         Intent shortcut = (Intent)intent.getParcelableExtra("android.intent.extra.shortcut.INTENT");
         if (shortcut != null) {
            ComponentName componentName = shortcut.resolveActivity(this.getPM());
            if (componentName != null) {
               Intent newShortcutIntent = new Intent();
               newShortcutIntent.putExtra("_VA_|_uri_", shortcut.toUri(0));
               newShortcutIntent.setClassName(getHostPkg(), Constants.SHORTCUT_PROXY_ACTIVITY_NAME);
               newShortcutIntent.removeExtra("android.intent.extra.shortcut.INTENT");
               intent.putExtra("android.intent.extra.shortcut.INTENT", newShortcutIntent);
            }
         }

      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class BroadcastIntentWithFeature extends BroadcastIntent {
      public String getMethodName() {
         return "broadcastIntentWithFeature";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Intent intent = new Intent((Intent)args[2]);
         String type = (String)args[3];
         intent.setDataAndType(intent.getData(), type);
         Intent newIntent = this.handleIntent(intent);
         if (newIntent != null) {
            args[2] = newIntent;
            if (args[8] instanceof String[]) {
               args[8] = null;
            }

            int index = ArrayUtils.indexOfFirst(args, Boolean.class);
            args[index] = false;
            replaceLastUserId(args);

            try {
               return method.invoke(who, args);
            } catch (Throwable var9) {
               Throwable e = var9;
               VLog.e("VA", e);
               return 0;
            }
         } else {
            return 0;
         }
      }
   }

   static class StartNextMatchingActivity extends StartActivity {
      public String getMethodName() {
         return "startNextMatchingActivity";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return false;
      }
   }

   public static class StartActivityWithConfig extends StartActivity {
      public String getMethodName() {
         return "startActivityWithConfig";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return super.call(who, method, args);
      }
   }

   static class StopServiceToken extends MethodProxy {
      public String getMethodName() {
         return "stopServiceToken";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         ComponentName componentName = (ComponentName)args[0];
         IBinder token = (IBinder)args[1];
         int startId = (Integer)args[2];
         int userId = VUserHandle.myUserId();
         ServiceInfo serviceInfo = VPackageManager.get().getServiceInfo(componentName, 0, userId);
         if (serviceInfo != null) {
            ClientConfig clientConfig = VActivityManager.get().initProcess(serviceInfo.packageName, serviceInfo.processName, userId);
            if (clientConfig == null) {
               VLog.e("ActivityManager", "failed to initProcess for stopServiceToken");
               return false;
            } else {
               Intent intent = IntentBuilder.createStopProxyServiceIntent(clientConfig.vpid, clientConfig.isExt, componentName, userId, startId, token);
               getHostContext().startService(intent);
               return true;
            }
         } else {
            return isOutsidePackage(componentName.getPackageName()) ? method.invoke(who, args) : false;
         }
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   @TargetApi(21)
   public static class SetTaskDescription extends MethodProxy {
      public String getMethodName() {
         return "setTaskDescription";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         ActivityManager.TaskDescription td = (ActivityManager.TaskDescription)args[1];
         String label = td.getLabel();
         Bitmap icon = td.getIcon();
         if (label == null || icon == null) {
            Application app = VClient.get().getCurrentApplication();
            if (app != null) {
               try {
                  if (label == null) {
                     label = app.getApplicationInfo().loadLabel(app.getPackageManager()).toString();
                  }

                  if (icon == null) {
                     Drawable drawable = app.getApplicationInfo().loadIcon(app.getPackageManager());
                     if (drawable != null) {
                        icon = DrawableUtils.drawableToBitMap(drawable);
                     }
                  }

                  td = new ActivityManager.TaskDescription(label, icon, td.getPrimaryColor());
               } catch (Throwable var9) {
                  Throwable e = var9;
                  e.printStackTrace();
               }
            }
         }

         TaskDescriptionDelegate descriptionDelegate = VirtualCore.get().getTaskDescriptionDelegate();
         if (descriptionDelegate != null) {
            td = descriptionDelegate.getTaskDescription(td);
         }

         args[1] = td;
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetContentProvider extends MethodProxy {
      public String getMethodName() {
         return "getContentProvider";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         int nameIdx = this.getProviderNameIndex();
         String name = (String)args[nameIdx];
         if (!name.startsWith(StubManifest.STUB_CP_AUTHORITY) && !name.startsWith(StubManifest.EXT_STUB_CP_AUTHORITY) && !name.equals(getConfig().getExtPackageHelperAuthority()) && !name.equals(getConfig().getBinderProviderAuthority())) {
            VLog.w("VActivityManger", "getContentProvider:%s", name);
            int userId;
            if (BuildCompat.isQ()) {
               userId = nameIdx - 1;
               if (args[userId] instanceof String) {
                  args[userId] = getHostPkg();
               }
            }

            userId = VUserHandle.myUserId();
            ProviderInfo info = VPackageManager.get().resolveContentProvider(name, 0, userId);
            if (info != null && !info.enabled) {
               return null;
            } else if (info != null && this.isAppPkg(info.packageName)) {
               ClientConfig config = VActivityManager.get().initProcess(info.packageName, info.processName, userId);
               if (config == null) {
                  VLog.e("ActivityManager", "failed to initProcess for provider: " + name);
                  return null;
               } else {
                  args[nameIdx] = StubManifest.getStubAuthority(config.vpid, config.isExt);
                  replaceLastUserId(args);
                  Object holder = method.invoke(who, args);
                  if (holder == null) {
                     return null;
                  } else {
                     boolean maybeLoadingProvider = false;
                     IInterface provider;
                     if (BuildCompat.isOreo()) {
                        provider = (IInterface)ContentProviderHolderOreo.provider.get(holder);
                        if (provider != null) {
                           provider = VActivityManager.get().acquireProviderClient(userId, info);
                           if (BuildCompat.isS() && provider != null) {
                              provider = ProviderHook.createProxy(false, name, provider);
                           }
                        } else {
                           maybeLoadingProvider = true;
                        }

                        if (provider == null) {
                           if (maybeLoadingProvider) {
                              VLog.w("VActivityManager", "Loading provider: " + info.authority + "(" + info.processName + ")");
                              ContentProviderHolderOreo.info.set(holder, info);
                              return holder;
                           }

                           VLog.e("VActivityManager", "acquireProviderClient fail: " + info.authority + "(" + info.processName + ")");
                           return null;
                        }

                        ContentProviderHolderOreo.provider.set(holder, provider);
                        ContentProviderHolderOreo.info.set(holder, info);
                     } else {
                        provider = (IInterface)IActivityManager.ContentProviderHolder.provider.get(holder);
                        if (provider != null) {
                           provider = VActivityManager.get().acquireProviderClient(userId, info);
                        } else {
                           maybeLoadingProvider = true;
                        }

                        if (provider == null) {
                           if (maybeLoadingProvider) {
                              if (BuildCompat.isMIUI() && this.miuiProviderWaitingTargetProcess(holder)) {
                                 VLog.w("VActivityManager", "miui provider waiting process: " + info.authority + "(" + info.processName + ")");
                                 return null;
                              }

                              return null;
                           }

                           VLog.e("VActivityManager", "acquireProviderClient fail: " + info.authority + "(" + info.processName + ")");
                           return null;
                        }

                        IActivityManager.ContentProviderHolder.provider.set(holder, provider);
                        IActivityManager.ContentProviderHolder.info.set(holder, info);
                     }

                     return holder;
                  }
               }
            } else {
               replaceLastUserId(args);
               Object holder2 = method.invoke(who, args);
               if (holder2 != null) {
                  IInterface provider3;
                  ProviderInfo info2;
                  if (BuildCompat.isOreo()) {
                     provider3 = (IInterface)ContentProviderHolderOreo.provider.get(holder2);
                     info2 = (ProviderInfo)ContentProviderHolderOreo.info.get(holder2);
                     if (provider3 != null) {
                        provider3 = ProviderHook.createProxy(true, info2.authority, provider3);
                     }

                     ContentProviderHolderOreo.provider.set(holder2, provider3);
                  } else {
                     provider3 = (IInterface)IActivityManager.ContentProviderHolder.provider.get(holder2);
                     info2 = (ProviderInfo)IActivityManager.ContentProviderHolder.info.get(holder2);
                     if (provider3 != null) {
                        provider3 = ProviderHook.createProxy(true, info2.authority, provider3);
                     }

                     IActivityManager.ContentProviderHolder.provider.set(holder2, provider3);
                  }

                  return holder2;
               } else {
                  return null;
               }
            }
         } else {
            replaceLastUserId(args);
            return method.invoke(who, args);
         }
      }

      public int getProviderNameIndex() {
         return BuildCompat.isQ() ? 2 : 1;
      }

      public boolean isEnable() {
         return isAppProcess();
      }

      private boolean miuiProviderWaitingTargetProcess(Object providerHolder) {
         return providerHolder != null && IActivityManager.ContentProviderHolderMIUI.waitProcessStart != null ? IActivityManager.ContentProviderHolderMIUI.waitProcessStart.get(providerHolder) : false;
      }
   }

   static class RegisterReceiver extends MethodProxy {
      int IDX_IIntentReceiver = 2;
      int IDX_IntentFilter = 3;
      int IDX_RequiredPermission = 4;
      private WeakHashMap<IBinder, IIntentReceiver> mProxyIIntentReceivers = new WeakHashMap();

      public String getMethodName() {
         return "registerReceiver";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         replaceFirstUserId(args);
         args[this.IDX_RequiredPermission] = null;
         IntentFilter filter = (IntentFilter)args[this.IDX_IntentFilter];
         if (filter == null) {
            return method.invoke(who, args);
         } else {
            filter = new IntentFilter(filter);
            if (filter.hasCategory("__VA__|_static_receiver_")) {
               List<String> categories = (List)mirror.android.content.IntentFilter.mCategories.get(filter);
               categories.remove("__VA__|_static_receiver_");
               return method.invoke(who, args);
            } else {
               SpecialComponentList.protectIntentFilter(filter);
               args[this.IDX_IntentFilter] = filter;
               if (args.length > this.IDX_IIntentReceiver && args[this.IDX_IIntentReceiver] instanceof IIntentReceiver) {
                  IInterface old = (IInterface)args[this.IDX_IIntentReceiver];
                  if (!(old instanceof IIntentReceiverProxy)) {
                     final IBinder token = old.asBinder();
                     if (token != null) {
                        token.linkToDeath(new IBinder.DeathRecipient() {
                           public void binderDied() {
                              token.unlinkToDeath(this, 0);
                              RegisterReceiver.this.mProxyIIntentReceivers.remove(token);
                           }
                        }, 0);
                        IIntentReceiver proxyIIntentReceiver = (IIntentReceiver)this.mProxyIIntentReceivers.get(token);
                        if (proxyIIntentReceiver == null) {
                           proxyIIntentReceiver = new IIntentReceiverProxy(old, filter);
                           this.mProxyIIntentReceivers.put(token, proxyIIntentReceiver);
                        }

                        WeakReference mDispatcher = (WeakReference)LoadedApk.ReceiverDispatcher.InnerReceiver.mDispatcher.get(old);
                        if (mDispatcher != null) {
                           LoadedApk.ReceiverDispatcher.mIIntentReceiver.set(mDispatcher.get(), proxyIIntentReceiver);
                           args[this.IDX_IIntentReceiver] = proxyIIntentReceiver;
                        }
                     }
                  }
               }

               Intent stickyIntent = (Intent)method.invoke(who, args);
               if (stickyIntent != null) {
                  stickyIntent = SpecialComponentList.unprotectIntent(VUserHandle.myUserId(), stickyIntent);
               }

               return stickyIntent;
            }
         }
      }

      public boolean isEnable() {
         return isAppProcess();
      }

      private static class IIntentReceiverProxy extends IIntentReceiver.Stub {
         IInterface mOld;
         IntentFilter mFilter;

         IIntentReceiverProxy(IInterface old, IntentFilter filter) {
            this.mOld = old;
            this.mFilter = filter;
         }

         public void performReceive(Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
            intent = SpecialComponentList.unprotectIntent(VUserHandle.myUserId(), intent);
            IIntentReceiverJB.performReceive.call(this.mOld, intent, resultCode, data, extras, ordered, sticky, sendingUser);
         }

         public void performReceive(Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky) {
            this.performReceive(intent, resultCode, data, extras, ordered, sticky, 0);
         }
      }
   }

   static class RegisterReceiverWithFeature extends RegisterReceiver {
      public RegisterReceiverWithFeature() {
         if (BuildCompat.isS()) {
            this.IDX_IIntentReceiver = 4;
            this.IDX_IntentFilter = 5;
            this.IDX_RequiredPermission = 6;
         } else {
            this.IDX_IIntentReceiver = 3;
            this.IDX_IntentFilter = 4;
            this.IDX_RequiredPermission = 5;
         }

      }

      public String getMethodName() {
         return "registerReceiverWithFeature";
      }
   }

   static class GetPersistedUriPermissions extends MethodProxy {
      public String getMethodName() {
         return "getPersistedUriPermissions";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   public static class GetTasks extends MethodProxy {
      public String getMethodName() {
         return "getTasks";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         List<ActivityManager.RunningTaskInfo> runningTaskInfos = (List)method.invoke(who, args);
         Iterator var5 = runningTaskInfos.iterator();

         while(var5.hasNext()) {
            ActivityManager.RunningTaskInfo info = (ActivityManager.RunningTaskInfo)var5.next();
            AppTaskInfo taskInfo = VActivityManager.get().getTaskInfo(info.id);
            if (taskInfo != null) {
               info.topActivity = taskInfo.topActivity;
               info.baseActivity = taskInfo.baseActivity;
            }
         }

         return runningTaskInfos;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class HandleIncomingUser extends MethodProxy {
      public String getMethodName() {
         return "handleIncomingUser";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         int lastIndex = args.length - 1;
         if (args[lastIndex] instanceof String) {
            args[lastIndex] = getHostPkg();
         }

         replaceLastUserId(args);
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class StartActivityAsCaller extends StartActivity {
      public String getMethodName() {
         return "startActivityAsCaller";
      }
   }

   static class CheckPermissionWithToken extends MethodProxy {
      public String getMethodName() {
         return "checkPermissionWithToken";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String permission = (String)args[0];
         int pid = (Integer)args[1];
         int uid = (Integer)args[2];
         return VActivityManager.get().checkPermission(permission, pid, uid);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class CheckPermission extends MethodProxy {
      public String getMethodName() {
         return "checkPermission";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String permission = (String)args[0];
         int pid = (Integer)args[1];
         int uid = (Integer)args[2];
         return VActivityManager.get().checkPermission(permission, pid, uid);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   public static class StartActivityAsUser extends StartActivity {
      public String getMethodName() {
         return "startActivityAsUser";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         replaceLastUserId(args);
         return super.call(who, method, args);
      }
   }

   static class KillBackgroundProcesses extends MethodProxy {
      public String getMethodName() {
         return "killBackgroundProcesses";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (args[0] instanceof String) {
            String pkg = (String)args[0];
            VActivityManager.get().killAppByPkg(pkg, getAppUserId());
            return 0;
         } else {
            replaceLastUserId(args);
            return super.call(who, method, args);
         }
      }
   }

   static class KillApplicationProcess extends MethodProxy {
      public String getMethodName() {
         return "killApplicationProcess";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetCurrentUser extends MethodProxy {
      public String getMethodName() {
         return "getCurrentUser";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         try {
            return UserInfo.ctor.newInstance(0, "user", 1);
         } catch (Throwable var5) {
            Throwable e = var5;
            e.printStackTrace();
            return null;
         }
      }
   }

   static class GetCurrentUserId extends MethodProxy {
      public String getMethodName() {
         return "GetCurrentUserId";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return 0;
      }
   }

   public static class GetCallingActivity extends MethodProxy {
      public String getMethodName() {
         return "getCallingActivity";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IBinder token = (IBinder)args[0];
         return VActivityManager.get().getCallingActivity(token);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class SetPackageAskScreenCompat extends MethodProxy {
      public String getMethodName() {
         return "setPackageAskScreenCompat";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (VERSION.SDK_INT >= 15 && args.length > 0 && args[0] instanceof String) {
            args[0] = getHostPkg();
         }

         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetRunningAppProcesses extends MethodProxy {
      public String getMethodName() {
         return "getRunningAppProcesses";
      }

      public synchronized Object call(Object who, Method method, Object... args) throws Throwable {
         if (VClient.get().getClientConfig() == null) {
            return method.invoke(who, args);
         } else {
            List<ActivityManager.RunningAppProcessInfo> _infoList = (List)method.invoke(who, args);
            if (_infoList == null) {
               return null;
            } else {
               List<ActivityManager.RunningAppProcessInfo> infoList = new ArrayList(_infoList);
               Iterator<ActivityManager.RunningAppProcessInfo> it = infoList.iterator();

               while(true) {
                  while(true) {
                     ActivityManager.RunningAppProcessInfo info;
                     do {
                        if (!it.hasNext()) {
                           return infoList;
                        }

                        info = (ActivityManager.RunningAppProcessInfo)it.next();
                     } while(info.uid != getRealUid());

                     if (VActivityManager.get().isAppPid(info.pid)) {
                        int vuid = VActivityManager.get().getUidByPid(info.pid);
                        int userId = VUserHandle.getUserId(vuid);
                        if (userId != getAppUserId()) {
                           it.remove();
                        } else {
                           List<String> pkgList = VActivityManager.get().getProcessPkgList(info.pid);
                           String processName = VActivityManager.get().getAppProcessName(info.pid);
                           if (processName != null) {
                              info.importanceReasonCode = 0;
                              info.importanceReasonPid = 0;
                              info.importanceReasonComponent = null;
                              info.processName = processName;
                           }

                           info.pkgList = (String[])pkgList.toArray(new String[0]);
                           info.uid = vuid;
                        }
                     } else if (info.processName.startsWith(getConfig().getMainPackageName()) || info.processName.startsWith(getConfig().getExtPackageName())) {
                        it.remove();
                     }
                  }
               }
            }
         }
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   public static class StartActivityAndWait extends StartActivity {
      public String getMethodName() {
         return "startActivityAndWait";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return super.call(who, method, args);
      }
   }

   static class UnbindService extends MethodProxy {
      public String getMethodName() {
         return "unbindService";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IServiceConnection conn = (IServiceConnection)args[0];
         ServiceConnectionProxy proxy = ServiceConnectionProxy.removeProxy(conn);
         if (proxy != null) {
            args[0] = proxy;
         }

         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess() || isServerProcess();
      }
   }

   static class StopService extends MethodProxy {
      public String getMethodName() {
         return "stopService";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IInterface caller = (IInterface)args[0];
         Intent service = new Intent((Intent)args[1]);
         String resolvedType = (String)args[2];
         service.setDataAndType(service.getData(), resolvedType);
         ComponentName component = service.getComponent();
         if (component != null && this.isHostPkg(component.getPackageName())) {
            return method.invoke(who, args);
         } else {
            int userId = isServerProcess() ? service.getIntExtra("_VA_|_user_id_", -1) : VUserHandle.myUserId();
            if (userId == -1) {
               throw new IllegalArgumentException();
            } else {
               ServiceInfo serviceInfo = VirtualCore.get().resolveServiceInfo(service, userId);
               if (serviceInfo != null && this.isAppPkg(serviceInfo.packageName)) {
                  ClientConfig clientConfig = VActivityManager.get().initProcess(serviceInfo.packageName, serviceInfo.processName, userId);
                  if (clientConfig == null) {
                     VLog.e("ActivityManager", "failed to initProcess for stopService: " + component);
                     return 0;
                  } else {
                     if (component == null) {
                        component = new ComponentName(serviceInfo.packageName, serviceInfo.name);
                     }

                     Intent intent = IntentBuilder.createStopProxyServiceIntent(clientConfig.vpid, clientConfig.isExt, component, userId, -1, (IBinder)null);
                     getHostContext().startService(intent);
                     return 1;
                  }
               } else if (component != null && isOutsidePackage(component.getPackageName())) {
                  replaceLastUserId(args);
                  return method.invoke(who, args);
               } else {
                  return 0;
               }
            }
         }
      }

      public boolean isEnable() {
         return isAppProcess() || isServerProcess();
      }
   }

   static class BindServiceInstance extends BindIsolatedService {
      public String getMethodName() {
         return "bindServiceInstance";
      }
   }

   static class PeekService extends MethodProxy {
      public String getMethodName() {
         return "peekService";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Intent service = new Intent((Intent)args[0]);
         String resolvedType = (String)args[1];
         ComponentName component = service.getComponent();
         if (component != null && this.isHostPkg(component.getPackageName())) {
            return method.invoke(who, args);
         } else {
            int userId = isServerProcess() ? service.getIntExtra("_VA_|_user_id_", -1) : VUserHandle.myUserId();
            if (userId == -1) {
               throw new IllegalArgumentException();
            } else {
               service.setDataAndType(service.getData(), resolvedType);
               ServiceInfo serviceInfo = VirtualCore.get().resolveServiceInfo(service, userId);
               if (serviceInfo != null) {
                  ClientConfig clientConfig = VClient.get().getClientConfig();
                  args[0] = IntentBuilder.createBindProxyServiceIntent(clientConfig.vpid, clientConfig.isExt, serviceInfo, service, 0, userId, (IServiceConnection)null);
                  return method.invoke(who, args);
               } else {
                  return component != null && isOutsidePackage(component.getPackageName()) ? method.invoke(who, args) : null;
               }
            }
         }
      }

      public boolean isEnable() {
         return isAppProcess() || isServerProcess();
      }
   }

   static class BindIsolatedService extends BindService {
      public String getMethodName() {
         return "bindIsolatedService";
      }

      public boolean beforeCall(Object who, Method method, Object... args) {
         replaceLastUserId(args);
         return super.beforeCall(who, method, args);
      }

      protected boolean isIsolated() {
         return true;
      }
   }

   static class BindService extends MethodProxy {
      public String getMethodName() {
         return "bindService";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IInterface iInterface = (IInterface)args[0];
         IBinder iBinder = (IBinder)args[1];
         Intent service = new Intent((Intent)args[2]);
         String resolvedType = (String)args[3];
         IServiceConnection conn = (IServiceConnection)args[4];
         service.setDataAndType(service.getData(), resolvedType);
         ComponentName component = service.getComponent();
         if (component != null && this.isHostPkg(component.getPackageName())) {
            return method.invoke(who, args);
         } else if (isHostIntent(service)) {
            return method.invoke(who, args);
         } else {
            int callingPkgIdx = this.isIsolated() ? 7 : 6;
            if (VERSION.SDK_INT >= 23 && args.length >= 8 && args[callingPkgIdx] instanceof String) {
               args[callingPkgIdx] = getHostPkg();
            }

            long flags = this.getIntOrLongValue(args[5]);
            int userId2 = isServerProcess() ? service.getIntExtra("_VA_|_user_id_", -1) : VUserHandle.myUserId();
            if (userId2 == -1) {
               throw new IllegalArgumentException();
            } else {
               ServiceInfo serviceInfo = VirtualCore.get().resolveServiceInfo(service, userId2);
               if (serviceInfo == null) {
                  if (component != null && isOutsidePackage(component.getPackageName())) {
                     replaceLastUserId(args);
                     return method.invoke(who, args);
                  } else {
                     VLog.e("VActivityManager", "Block bindService: " + service);
                     return 0;
                  }
               } else {
                  if (this.isIsolated()) {
                     args[6] = null;
                  }

                  int userId;
                  if (VERSION.SDK_INT < 24) {
                     userId = userId2;
                  } else if ((-2147483648L & flags) == 0L) {
                     userId = userId2;
                  } else if (BuildCompat.isUpsideDownCake()) {
                     args[5] = flags & 2147483647L;
                     userId = userId2;
                  } else {
                     userId = userId2;
                     args[5] = (int)(flags & 2147483647L);
                  }

                  int userId3 = userId;
                  ClientConfig clientConfig = VActivityManager.get().initProcess(serviceInfo.packageName, serviceInfo.processName, userId3);
                  if (clientConfig == null) {
                     VLog.e("ActivityManager", "failed to initProcess for bindService: " + component);
                     return 0;
                  } else {
                     args[2] = IntentBuilder.createBindProxyServiceIntent(clientConfig.vpid, clientConfig.isExt, serviceInfo, service, (int)flags, userId3, conn);
                     args[4] = ServiceConnectionProxy.getOrCreateProxy(conn);
                     return method.invoke(who, args);
                  }
               }
            }
         }
      }

      protected boolean isIsolated() {
         return false;
      }

      public boolean isEnable() {
         return isAppProcess() || isServerProcess();
      }
   }

   static class StartService extends MethodProxy {
      public String getMethodName() {
         return "startService";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Intent service = new Intent((Intent)args[1]);
         String resolvedType = (String)args[2];
         ComponentName component = service.getComponent();
         if (component != null && this.isHostPkg(component.getPackageName())) {
            return method.invoke(who, args);
         } else {
            int userId = isServerProcess() ? service.getIntExtra("_VA_|_user_id_", -1) : VUserHandle.myUserId();
            if (userId == -1) {
               return method.invoke(who, args);
            } else {
               service.setDataAndType(service.getData(), resolvedType);
               ServiceInfo serviceInfo = VirtualCore.get().resolveServiceInfo(service, userId);
               if (serviceInfo != null) {
                  if (VERSION.SDK_INT >= 26 && args.length >= 6 && args[3] instanceof Boolean) {
                     args[3] = false;
                  }

                  ClientConfig clientConfig = VActivityManager.get().initProcess(serviceInfo.packageName, serviceInfo.processName, userId);
                  if (clientConfig == null) {
                     VLog.e("ActivityManager", "failed to initProcess for startService: " + component);
                     return null;
                  } else {
                     args[1] = IntentBuilder.createStartProxyServiceIntent(clientConfig.vpid, clientConfig.isExt, serviceInfo, service, userId);
                     replaceLastUserId(args);
                     ComponentName res = (ComponentName)method.invoke(who, args);
                     if (res != null) {
                        res = new ComponentName(serviceInfo.packageName, serviceInfo.name);
                        return res;
                     } else {
                        return null;
                     }
                  }
               } else if ((component == null || !isOutsidePackage(component.getPackageName())) && service.getPackage() != null && !isOutsidePackage(service.getPackage())) {
                  VLog.e("VActivityManager", "Block StartService: " + service);
                  return null;
               } else {
                  replaceLastUserId(args);
                  return method.invoke(who, args);
               }
            }
         }
      }

      public boolean isEnable() {
         return isAppProcess() || isServerProcess();
      }
   }

   public static class GetAppTasks extends MethodProxy {
      public String getMethodName() {
         return "getAppTasks";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         return super.call(who, method, args);
      }
   }

   public static class StartActivityIntentSender extends MethodProxy {
      public String getMethodName() {
         return "startActivityIntentSender";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         byte intentIndex;
         byte resultToIndex;
         byte resultWhoIndex;
         byte optionsIndex;
         byte requestCodeIndex;
         byte flagsMaskIndex;
         byte flagsValuesIndex;
         if (BuildCompat.isOreo()) {
            intentIndex = 3;
            resultToIndex = 5;
            resultWhoIndex = 6;
            requestCodeIndex = 7;
            flagsMaskIndex = 8;
            flagsValuesIndex = 9;
            optionsIndex = 10;
         } else {
            intentIndex = 2;
            resultToIndex = 4;
            resultWhoIndex = 5;
            requestCodeIndex = 6;
            flagsMaskIndex = 7;
            flagsValuesIndex = 8;
            optionsIndex = 9;
         }

         IInterface target = (IInterface)args[1];
         Intent fillIn = (Intent)args[intentIndex];
         IBinder resultTo = (IBinder)args[resultToIndex];
         String resultWho = (String)args[resultWhoIndex];
         int requestCode = (Integer)args[requestCodeIndex];
         Bundle options = (Bundle)args[optionsIndex];
         int flagsMask = (Integer)args[flagsMaskIndex];
         int flagsValues = (Integer)args[flagsValuesIndex];
         if (fillIn == null) {
            fillIn = new Intent();
            args[intentIndex] = fillIn;
         }

         ComponentUtils.parcelActivityIntentSender(fillIn, resultTo, options);
         return super.call(who, method, args);
      }
   }

   static class GetIntentForIntentSender extends MethodProxy {
      public String getMethodName() {
         return "getIntentForIntentSender";
      }

      public Object afterCall(Object who, Method method, Object[] args, Object result) {
         Intent intent = (Intent)result;
         if (intent != null) {
            try {
               ComponentUtils.IntentSenderInfo info = ComponentUtils.parseIntentSenderInfo(intent, false);
               if (info != null) {
                  return info.intent;
               }
            } catch (Throwable var7) {
               Throwable e = var7;
               e.printStackTrace();
            }
         }

         return intent;
      }
   }

   static class IsBackgroundRestricted extends MethodProxy {
      public String getMethodName() {
         return "isBackgroundRestricted";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         return method.invoke(who, args);
      }
   }

   static class GetUidForIntentSender extends MethodProxy {
      public String getMethodName() {
         return "getUidForIntentSender";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IInterface sender = (IInterface)args[0];
         if (sender != null) {
            IntentSenderData data = VActivityManager.get().getIntentSender(sender.asBinder());
            if (data != null) {
               return VPackageManager.get().getPackageUid(data.targetPkg, data.userId);
            }
         }

         return -1;
      }
   }

   static class UpdateDeviceOwner extends MethodProxy {
      public String getMethodName() {
         return "updateDeviceOwner";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class SetServiceForeground extends MethodProxy {
      public String getMethodName() {
         return "setServiceForeground";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return 0;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GrantUriPermissionFromOwner extends MethodProxy {
      public String getMethodName() {
         return "grantUriPermissionFromOwner";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetServices extends MethodProxy {
      public String getMethodName() {
         return "getServices";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         int maxNum = (Integer)args[0];
         int flags = (Integer)args[1];
         return VActivityManager.get().getServices(VClient.get().getCurrentPackage(), maxNum, flags).getList();
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class PublishContentProviders extends MethodProxy {
      public String getMethodName() {
         return "publishContentProviders";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetPackageForIntentSender extends MethodProxy {
      public String getMethodName() {
         return "getPackageForIntentSender";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IInterface sender = (IInterface)args[0];
         if (sender != null) {
            IntentSenderData data = VActivityManager.get().getIntentSender(sender.asBinder());
            if (data != null) {
               return data.targetPkg;
            }
         }

         return super.call(who, method, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   public static class GetCallingPackage extends MethodProxy {
      public String getMethodName() {
         return "getCallingPackage";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IBinder token = (IBinder)args[0];
         return VActivityManager.get().getCallingPackage(token);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class ShouldUpRecreateTask extends MethodProxy {
      public String getMethodName() {
         return "shouldUpRecreateTask";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return false;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   public static class StartActivities extends MethodProxy {
      public String getMethodName() {
         return "startActivities";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         (new Exception()).printStackTrace();
         Intent[] intents = (Intent[])ArrayUtils.getFirst(args, Intent[].class);
         String[] resolvedTypes = (String[])ArrayUtils.getFirst(args, String[].class);
         int tokenIndex = ArrayUtils.indexOfObject(args, IBinder.class, 2);
         IBinder token;
         if (tokenIndex == -1) {
            token = null;
         } else {
            IBinder token2 = (IBinder)args[tokenIndex];
            token = token2;
         }

         Bundle options = (Bundle)ArrayUtils.getFirst(args, Bundle.class);
         return VActivityManager.get().startActivities(intents, resolvedTypes, token, options, VClient.get().getCurrentPackage(), VUserHandle.myUserId());
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   public static class StartActivity extends MethodProxy {
      private static final String SCHEME_FILE = "file";
      private static final String SCHEME_PACKAGE = "package";
      private static final String SCHEME_CONTENT = "content";

      public String getMethodName() {
         return "startActivity";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         int intentIndex = ArrayUtils.indexOfObject(args, Intent.class, 1);
         if (intentIndex < 0) {
            return ActivityManagerCompat.START_INTENT_NOT_RESOLVED;
         } else {
            int resultToIndex = ArrayUtils.indexOfObject(args, IBinder.class, 2);
            String resolvedType = (String)args[intentIndex + 1];
            Intent intent = new Intent((Intent)args[intentIndex]);
            intent.setDataAndType(intent.getData(), resolvedType);
            IBinder resultTo = resultToIndex >= 0 ? (IBinder)args[resultToIndex] : null;
            Bundle options = (Bundle)ArrayUtils.getFirst(args, Bundle.class);
            String resultWho;
            int requestCode;
            if (resultTo == null) {
               resultWho = null;
               requestCode = 0;
            } else {
               String resultWho2 = (String)args[resultToIndex + 1];
               int requestCode2 = (Integer)args[resultToIndex + 2];
               resultWho = resultWho2;
               requestCode = requestCode2;
            }

            int userId = VUserHandle.myUserId();
            String action = intent.getAction();
            Intent intent3;
            if ("android.intent.action.MAIN".equals(action) && intent.hasCategory("android.intent.category.HOME")) {
               intent3 = getConfig().onHandleLauncherIntent(intent);
               if (intent3 != null) {
                  args[intentIndex] = intent3;
               }

               return method.invoke(who, args);
            } else if (!"android.settings.APP_NOTIFICATION_SETTINGS".equals(action) && !"android.settings.CHANNEL_NOTIFICATION_SETTINGS".equals(action)) {
               if (isHostIntent(intent)) {
                  return method.invoke(who, args);
               } else {
                  if ("android.intent.action.INSTALL_PACKAGE".equals(action) || "android.intent.action.VIEW".equals(action) && "application/vnd.android.package-archive".equals(intent.getType())) {
                     if (this.handleInstallRequest(intent)) {
                        if (resultTo != null && requestCode > 0) {
                           VActivityManager.get().sendCancelActivityResult(resultTo, resultWho, requestCode);
                        }

                        return 0;
                     }
                  } else if (("android.intent.action.UNINSTALL_PACKAGE".equals(action) || "android.intent.action.DELETE".equals(action)) && SCHEME_PACKAGE.equals(intent.getScheme()) && this.handleUninstallRequest(intent)) {
                     return 0;
                  }

                  String pkg = intent.getPackage();
                  if (pkg != null && !this.isAppPkg(pkg)) {
                     if (BuildCompat.isR() && "android.content.pm.action.REQUEST_PERMISSIONS".equals(action)) {
                        args[intentIndex - 2] = getHostPkg();
                     }

                     return method.invoke(who, args);
                  } else if (ChooserActivity.check(intent)) {
                     Intent intent2 = ComponentUtils.processOutsideIntent(userId, VirtualCore.get().isExtPackage(), new Intent(intent));
                     args[intentIndex] = intent2;
                     Bundle extras = new Bundle();
                     extras.putInt("android.intent.extra.user_handle", userId);
                     extras.putBundle("android.intent.extra.virtual.data", options);
                     extras.putString("android.intent.extra.virtual.who", resultWho);
                     extras.putInt("android.intent.extra.virtual.request_code", requestCode);
                     BundleCompat.putBinder(extras, "_va|ibinder|resultTo", resultTo);
                     intent2.setComponent(new ComponentName(StubManifest.PACKAGE_NAME, ChooserActivity.class.getName()));
                     intent2.setAction((String)null);
                     intent2.putExtras(extras);
                     return method.invoke(who, args);
                  } else {
                     args[intentIndex - 1] = getHostPkg();
                     if (intent.getScheme() != null && intent.getScheme().equals(SCHEME_PACKAGE) && intent.getData() != null && action != null && action.startsWith("android.settings.")) {
                        intent.setData(Uri.parse("package:" + getHostPkg()));
                     }

                     ActivityInfo activityInfo = VirtualCore.get().resolveActivityInfo(intent, userId);
                     if (activityInfo == null) {
                        VLog.e("VActivityManager", "Unable to resolve activityInfo : %s", intent);
                        if (intent.getPackage() != null && this.isAppPkg(intent.getPackage())) {
                           return 0;
                        } else {
                           args[intentIndex] = ComponentUtils.processOutsideIntent(userId, VirtualCore.get().isExtPackage(), intent);
                           ResolveInfo resolveInfo = VirtualCore.get().getHostPackageManager().resolveActivity(intent, 0L);
                           if ((resolveInfo == null || resolveInfo.activityInfo == null) && intent.resolveActivityInfo(VirtualCore.getPM(), 0) != null) {
                              return method.invoke(who, args);
                           } else {
                              return !"android.intent.action.VIEW".equals(action) && !getConfig().isOutsideAction(action) && (resolveInfo == null || !isOutsidePackage(resolveInfo.activityInfo.packageName)) ? ActivityManagerCompat.START_INTENT_NOT_RESOLVED : method.invoke(who, args);
                           }
                        }
                     } else {
                        int requestCode3 = requestCode;
                        String resultWho3 = resultWho;
                        int res = VActivityManager.get().startActivity(intent, activityInfo, resultTo, options, resultWho3, requestCode3, VClient.get().getCurrentPackage(), VUserHandle.myUserId());
                        if (res != 0 && resultTo != null && requestCode3 > 0) {
                           VActivityManager.get().sendCancelActivityResult(resultTo, resultWho3, requestCode3);
                        }

                        return res;
                     }
                  }
               }
            } else {
               intent3 = (Intent)args[intentIndex];
               if (BuildCompat.isOreo()) {
                  intent3.putExtra("android.provider.extra.APP_PACKAGE", VirtualCore.get().getHostPkg());
               } else {
                  intent3.putExtra("app_package", VirtualCore.get().getHostPkg());
               }

               return method.invoke(who, args);
            }
         }
      }

      private boolean handleInstallRequest(Intent intent) {
         VirtualCore.AppRequestListener listener = VirtualCore.get().getAppRequestListener();
         if (listener != null) {
            Uri packageUri = intent.getData();
            if (SCHEME_FILE.equals(packageUri.getScheme())) {
               File sourceFile = new File(packageUri.getPath());
               String path = NativeEngine.getRedirectedPath(sourceFile.getAbsolutePath());
               listener.onRequestInstall(path);
               return true;
            }

            if (SCHEME_CONTENT.equals(packageUri.getScheme())) {
               InputStream inputStream = null;
               OutputStream outputStream = null;
               File sharedFileCopy = new File(getHostContext().getCacheDir(), packageUri.getLastPathSegment());

               try {
                  try {
                     inputStream = getHostContext().getContentResolver().openInputStream(packageUri);
                     outputStream = new FileOutputStream(sharedFileCopy);
                     byte[] buffer = new byte[1024];

                     while(true) {
                        int count = inputStream.read(buffer);
                        if (count <= 0) {
                           ((OutputStream)outputStream).flush();
                           break;
                        }

                        ((OutputStream)outputStream).write(buffer, 0, count);
                     }
                  } catch (IOException var9) {
                     IOException e = var9;
                     e.printStackTrace();
                  }

                  FileUtils.closeQuietly(inputStream);
                  FileUtils.closeQuietly(outputStream);
                  listener.onRequestInstall(sharedFileCopy.getPath());
                  return true;
               } catch (Throwable var10) {
                  Throwable th = var10;
                  FileUtils.closeQuietly(inputStream);
                  FileUtils.closeQuietly(outputStream);
                  throw new RuntimeException(th);
               }
            }
         }

         return false;
      }

      private boolean handleUninstallRequest(Intent intent) {
         VirtualCore.AppRequestListener listener = VirtualCore.get().getAppRequestListener();
         if (listener != null) {
            Uri packageUri = intent.getData();
            if (SCHEME_PACKAGE.equals(packageUri.getScheme())) {
               String pkg = packageUri.getSchemeSpecificPart();
               listener.onRequestUninstall(pkg);
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetIntentSenderWithSourceToken extends GetIntentSender {
      public String getMethodName() {
         return "getIntentSenderWithSourceToken";
      }
   }

   static class GetIntentSender extends MethodProxy {
      public String getMethodName() {
         return "getIntentSender";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String creator = (String)args[1];
         args[1] = getHostPkg();
         if (args[args.length - 1] instanceof Integer) {
            args[args.length - 1] = getRealUserId();
         }

         int type = (Integer)args[0];
         int intentsIndex = ArrayUtils.indexOfFirst(args, Intent[].class);
         Intent[] intents = (Intent[])args[intentsIndex];
         String[] resolvedTypes = (String[])args[intentsIndex + 1];
         int flags = (Integer)args[intentsIndex + 2];
         int userId = VUserHandle.myUserId();
         if (intents.length > 0) {
            Intent intent = intents[intents.length - 1];
            if (resolvedTypes != null && resolvedTypes.length >= intents.length) {
               intent.setDataAndType(intent.getData(), resolvedTypes[intents.length - 1]);
            }

            Intent targetIntent = ComponentUtils.getProxyIntentSenderIntent(userId, type, creator, intent);
            if (targetIntent == null) {
               return null;
            } else {
               flags &= -9;
               if (VERSION.SDK_INT >= 16) {
                  flags &= -129;
               }

               if ((134217728 & flags) != 0) {
                  flags = flags & -671088641 | 268435456;
               }

               args[intentsIndex] = new Intent[]{targetIntent};
               args[intentsIndex + 1] = new String[]{null};
               IInterface sender;
               if ((flags & 268435456) != 0 && BuildCompat.isSamsung() && VERSION.SDK_INT >= 21) {
                  args[intentsIndex + 2] = 536870912;
                  sender = (IInterface)method.invoke(who, args);
                  if (sender != null) {
                     PendingIntent pendingIntent = PendingIntentCompat.readPendingIntent(sender.asBinder());
                     if (pendingIntent != null) {
                        AlarmManager alarmManager = (AlarmManager)getHostContext().getSystemService("alarm");
                        if (alarmManager != null) {
                           try {
                              alarmManager.cancel(pendingIntent);
                           } catch (Throwable var17) {
                              Throwable e = var17;
                              e.printStackTrace();
                           }
                        }
                     }
                  }
               }

               args[intentsIndex + 2] = flags;
               sender = (IInterface)method.invoke(who, args);
               if (sender != null) {
                  IBinder token = sender.asBinder();
                  IntentSenderData data = new IntentSenderData(creator, token, type, userId);
                  VActivityManager.get().addOrUpdateIntentSender(data);
               }

               return sender;
            }
         } else {
            return method.invoke(who, args);
         }
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetIntentSenderWithFeature extends GetIntentSender {
      public String getMethodName() {
         return "getIntentSenderWithFeature";
      }
   }

   static class GetPackageAskScreenCompat extends MethodProxy {
      public String getMethodName() {
         return "getPackageAskScreenCompat";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (VERSION.SDK_INT >= 15 && args.length > 0 && args[0] instanceof String) {
            args[0] = getHostPkg();
         }

         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class UnstableProviderDied extends MethodProxy {
      public String getMethodName() {
         return "unstableProviderDied";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return args[0] == null ? 0 : method.invoke(who, args);
      }
   }

   static class StartVoiceActivity extends StartActivity {
      public String getMethodName() {
         return "startVoiceActivity";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return super.call(who, method, args);
      }
   }

   static class GetContentProviderExternal extends GetContentProvider {
      public String getMethodName() {
         return "getContentProviderExternal";
      }

      public int getProviderNameIndex() {
         return BuildCompat.isQ() ? 1 : 0;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetPackageForToken extends MethodProxy {
      public String getMethodName() {
         return "getPackageForToken";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IBinder token = (IBinder)args[0];
         String pkg = VActivityManager.get().getPackageForToken(token);
         return pkg != null ? pkg : super.call(who, method, args);
      }
   }

   static class AddPackageDependency extends MethodProxy {
      public String getMethodName() {
         return "addPackageDependency";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class CrashApplication extends MethodProxy {
      public String getMethodName() {
         return "crashApplication";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class ForceStopPackage extends MethodProxy {
      public String getMethodName() {
         return "forceStopPackage";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String pkg = (String)args[0];
         int userId = VUserHandle.myUserId();
         VActivityManager.get().killAppByPkg(pkg, userId);
         return 0;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetRecentTasks extends MethodProxy {
      public String getMethodName() {
         return "getRecentTasks";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         replaceFirstUserId(args);
         Object _infos = method.invoke(who, args);
         List<ActivityManager.RecentTaskInfo> infos = (List)(ParceledListSliceCompat.isReturnParceledListSlice(method) ? ParceledListSlice.getList.call(_infos) : _infos);
         Iterator var6 = infos.iterator();

         while(var6.hasNext()) {
            ActivityManager.RecentTaskInfo info = (ActivityManager.RecentTaskInfo)var6.next();
            AppTaskInfo taskInfo = VActivityManager.get().getTaskInfo(info.id);
            if (taskInfo != null) {
               if (VERSION.SDK_INT >= 23) {
                  try {
                     info.topActivity = taskInfo.topActivity;
                     info.baseActivity = taskInfo.baseActivity;
                  } catch (Throwable var10) {
                  }
               }

               try {
                  info.origActivity = taskInfo.baseActivity;
                  info.baseIntent = taskInfo.baseIntent;
               } catch (Throwable var11) {
               }
            }
         }

         return _infos;
      }
   }

   static class FinishReceiver extends MethodProxy {
      public String getMethodName() {
         return "finishReceiver";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IBinder token = (IBinder)args[0];
         VActivityManager.get().broadcastFinish(token);
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }
}
