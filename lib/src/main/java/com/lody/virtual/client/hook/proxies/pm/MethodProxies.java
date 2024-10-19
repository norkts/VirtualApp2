package com.lody.virtual.client.hook.proxies.pm;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageDeleteObserver2;
import android.content.pm.IPackageInstallerCallback;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IInterface;
import android.os.RemoteException;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.fixer.ComponentFixer;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.helper.compat.ParceledListSliceCompat;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.server.IPackageInstaller;
import com.lody.virtual.server.pm.installer.SessionInfo;
import com.lody.virtual.server.pm.installer.SessionParams;
import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import mirror.android.content.pm.ParceledListSlice;

class MethodProxies {
   private static final int MATCH_FACTORY_ONLY = 2097152;
   private static final int MATCH_ANY_USER = 4194304;
   private static String TAG = "MethodProxies";

   static class GetApplicationBlockedSettingAsUser extends MethodProxy {
      public String getMethodName() {
         return "getApplicationBlockedSettingAsUser";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         replaceLastUserId(args);
         return method.invoke(who, args);
      }
   }

   @TargetApi(19)
   static class QueryIntentContentProviders extends MethodProxy {
      public String getMethodName() {
         return "queryIntentContentProviders";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         boolean slice = ParceledListSliceCompat.isReturnParceledListSlice(method);
         int userId = VUserHandle.myUserId();
         int flags = (int)this.getIntOrLongValue(args[2]);
         List<ResolveInfo> appResult = VPackageManager.get().queryIntentContentProviders((Intent)args[0], (String)args[1], flags, userId);
         replaceLastUserId(args);
         Object _hostResult = method.invoke(who, args);
         List<ResolveInfo> hostResult = (List)(slice ? ParceledListSlice.getList.call(_hostResult) : _hostResult);
         if (hostResult != null) {
            Iterator<ResolveInfo> iterator = hostResult.iterator();

            while(true) {
               while(iterator.hasNext()) {
                  ResolveInfo info = (ResolveInfo)iterator.next();
                  if (info != null && info.providerInfo != null && isOutsidePackage(info.providerInfo.packageName)) {
                     ComponentFixer.fixOutsideComponentInfo(info.providerInfo);
                  } else {
                     iterator.remove();
                  }
               }

               appResult.addAll(hostResult);
               break;
            }
         }

         return ParceledListSliceCompat.isReturnParceledListSlice(method) ? ParceledListSliceCompat.create(appResult) : appResult;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class SetPackageStoppedState extends MethodProxy {
      public String getMethodName() {
         return "setPackageStoppedState";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         replaceLastUserId(args);
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   @TargetApi(17)
   static class GetPermissionFlags extends MethodProxy {
      public String getMethodName() {
         return "getPermissionFlags";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String name = (String)args[0];
         String packageName = (String)args[1];
         int userId = (Integer)args[2];
         PermissionInfo info = VPackageManager.get().getPermissionInfo(name, 0);
         if (info != null) {
            return 0;
         } else {
            args[2] = getRealUserId();
            return method.invoke(who, args);
         }
      }
   }

   static class GetReceiverInfo extends MethodProxy {
      public String getMethodName() {
         return "getReceiverInfo";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         ComponentName componentName = (ComponentName)args[0];
         if (getHostPkg().equals(componentName.getPackageName())) {
            return method.invoke(who, args);
         } else {
            int flags = (int)this.getIntOrLongValue(args[1]);
            ActivityInfo info = VPackageManager.get().getReceiverInfo(componentName, flags, 0);
            if (info == null) {
               replaceLastUserId(args);
               info = (ActivityInfo)method.invoke(who, args);
               if (info == null || !isOutsidePackage(info.packageName)) {
                  return null;
               }

               ComponentFixer.fixOutsideComponentInfo(info);
            }

            return info;
         }
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class QueryIntentReceivers extends MethodProxy {
      public String getMethodName() {
         return "queryIntentReceivers";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         boolean slice = ParceledListSliceCompat.isReturnParceledListSlice(method);
         int userId = VUserHandle.myUserId();
         int flags = (int)this.getIntOrLongValue(args[2]);
         List<ResolveInfo> appResult = VPackageManager.get().queryIntentReceivers((Intent)args[0], (String)args[1], flags, userId);
         Object _hostResult = method.invoke(who, args);
         List<ResolveInfo> hostResult = (List)(slice ? ParceledListSlice.getList.call(_hostResult) : _hostResult);
         if (hostResult != null) {
            Iterator<ResolveInfo> iterator = hostResult.iterator();

            while(true) {
               while(iterator.hasNext()) {
                  ResolveInfo info = (ResolveInfo)iterator.next();
                  if (info != null && info.activityInfo != null && !this.isAppPkg(info.activityInfo.packageName) && isOutsidePackage(info.activityInfo.packageName)) {
                     ComponentFixer.fixOutsideComponentInfo(info.activityInfo);
                  } else {
                     iterator.remove();
                  }
               }

               appResult.addAll(hostResult);
               break;
            }
         }

         return slice ? ParceledListSliceCompat.create(appResult) : appResult;
      }
   }

   static class GetInstalledPackages extends MethodProxy {
      public String getMethodName() {
         return "getInstalledPackages";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         boolean slice = ParceledListSliceCompat.isReturnParceledListSlice(method);
         int flags = (int)this.getIntOrLongValue(args[0]);
         int userId = VUserHandle.myUserId();
         List<PackageInfo> packageInfos = VPackageManager.get().getInstalledPackages(flags, userId);
         replaceLastUserId(args);
         Object _hostResult = method.invoke(who, args);
         List<PackageInfo> hostResult = (List)(slice ? ParceledListSlice.getList.call(_hostResult) : _hostResult);

         PackageInfo info;
         for(Iterator<PackageInfo> it = hostResult.iterator(); it.hasNext(); ComponentFixer.fixOutsideApplicationInfo(info.applicationInfo)) {
            info = (PackageInfo)it.next();
            if (VirtualCore.get().isAppInstalled(info.packageName) || !isOutsidePackage(info.packageName)) {
               it.remove();
            }
         }

         packageInfos.addAll(hostResult);
         if (ParceledListSliceCompat.isReturnParceledListSlice(method)) {
            return ParceledListSliceCompat.create(packageInfos);
         } else {
            return packageInfos;
         }
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetInstalledApplications extends MethodProxy {
      public String getMethodName() {
         return "getInstalledApplications";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         boolean slice = ParceledListSliceCompat.isReturnParceledListSlice(method);
         int flags = (int)this.getIntOrLongValue(args[0]);
         int userId = VUserHandle.myUserId();
         List<ApplicationInfo> appInfos = VPackageManager.get().getInstalledApplications(flags, userId);
         Object _hostResult = method.invoke(who, args);
         List<ApplicationInfo> hostResult = (List)(slice ? ParceledListSlice.getList.call(_hostResult) : _hostResult);

         ApplicationInfo info;
         for(Iterator<ApplicationInfo> it = hostResult.iterator(); it.hasNext(); ComponentFixer.fixOutsideApplicationInfo(info)) {
            info = (ApplicationInfo)it.next();
            if (VirtualCore.get().isAppInstalled(info.packageName) || !isOutsidePackage(info.packageName)) {
               it.remove();
            }
         }

         appInfos.addAll(hostResult);
         if (slice) {
            return ParceledListSliceCompat.create(appInfos);
         } else {
            return appInfos;
         }
      }
   }

   static class SetComponentEnabledSetting extends MethodProxy {
      public String getMethodName() {
         return "setComponentEnabledSetting";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         ComponentName componentName = (ComponentName)args[0];
         int newState = (Integer)args[1];
         int flags = (Integer)args[2];
         VPackageManager.get().setComponentEnabledSetting(componentName, newState, flags, getAppUserId());
         return 0;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetProviderInfo extends MethodProxy {
      public String getMethodName() {
         return "getProviderInfo";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         ComponentName componentName = (ComponentName)args[0];
         int flags = (int)this.getIntOrLongValue(args[1]);
         if (getHostPkg().equals(componentName.getPackageName())) {
            replaceLastUserId(args);
            return method.invoke(who, args);
         } else {
            int userId = VUserHandle.myUserId();
            ProviderInfo info = VPackageManager.get().getProviderInfo(componentName, flags, userId);
            if (info == null) {
               replaceLastUserId(args);
               info = (ProviderInfo)method.invoke(who, args);
               if (info == null || !isOutsidePackage(info.packageName)) {
                  return null;
               }

               ComponentFixer.fixOutsideComponentInfo(info);
            }

            return info;
         }
      }
   }

   static class GetApplicationInfo extends MethodProxy {
      public String getMethodName() {
         return "getApplicationInfo";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String pkg = (String)args[0];
         int flags = (int)this.getIntOrLongValue(args[1]);
         int userId = VUserHandle.myUserId();
         if (pkg.equals("com.android.defcontainer")) {
            return VPackageManager.get().getApplicationInfo("com.android.providers.downloads", flags, userId);
         } else if (getHostPkg().equals(pkg)) {
            replaceLastUserId(args);
            return method.invoke(who, args);
         } else {
            ApplicationInfo info = VPackageManager.get().getApplicationInfo(pkg, flags, userId);
            if (info != null) {
               return info;
            } else {
               replaceLastUserId(args);
               info = (ApplicationInfo)method.invoke(who, args);
               if (info != null && isOutsidePackage(info.packageName)) {
                  ComponentFixer.fixOutsideApplicationInfo(info);
                  return info;
               } else {
                  return null;
               }
            }
         }
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class ResolveIntent extends MethodProxy {
      public String getMethodName() {
         return "resolveIntent";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Intent intent = (Intent)args[0];
         String resolvedType = (String)args[1];
         int flags = (int)this.getIntOrLongValue(args[2]);
         int userId = VUserHandle.myUserId();
         ResolveInfo resolveInfo = VPackageManager.get().resolveIntent(intent, resolvedType, flags, userId);
         if (resolveInfo == null) {
            replaceLastUserId(args);
            ResolveInfo info = (ResolveInfo)method.invoke(who, args);
            if (info != null && isOutsidePackage(info.activityInfo.packageName)) {
               ComponentFixer.fixOutsideComponentInfo(info.activityInfo);
               return info;
            }
         }

         return resolveInfo;
      }
   }

   static class ActivitySupportsIntent extends MethodProxy {
      public String getMethodName() {
         return "activitySupportsIntent";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         ComponentName component = (ComponentName)args[0];
         Intent intent = (Intent)args[1];
         String resolvedType = (String)args[2];
         return VPackageManager.get().activitySupportsIntent(component, intent, resolvedType);
      }
   }

   static class DeletePackage extends MethodProxy {
      public String getMethodName() {
         return "deletePackage";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String pkgName = (String)args[0];

         try {
            VirtualCore.get().uninstallPackage(pkgName);
            IPackageDeleteObserver2 observer = (IPackageDeleteObserver2)args[1];
            if (observer != null) {
               observer.onPackageDeleted(pkgName, 0, "done.");
            }
         } catch (Throwable var6) {
         }

         return 0;
      }
   }

   static class getNameForUid extends MethodProxy {
      public String getMethodName() {
         return "getNameForUid";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         int uid = (Integer)args[0];
         if (uid == 9000) {
            uid = getVUid();
         }

         return VPackageManager.get().getNameForUid(uid);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class checkUidSignatures extends MethodProxy {
      public String getMethodName() {
         return "checkUidSignatures";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         int uid1 = (Integer)args[0];
         int uid2 = (Integer)args[1];
         if (uid1 != uid2 && uid1 != VEnvironment.OUTSIDE_APP_UID && uid2 != VEnvironment.OUTSIDE_APP_UID) {
            String[] pkgs1 = VirtualCore.getPM().getPackagesForUid(uid1);
            String[] pkgs2 = VirtualCore.getPM().getPackagesForUid(uid2);
            if (pkgs1 != null && pkgs1.length != 0) {
               return pkgs2 != null && pkgs2.length != 0 ? VPackageManager.get().checkSignatures(pkgs1[0], pkgs2[0]) : -4;
            } else {
               return -4;
            }
         } else {
            return 0;
         }
      }
   }

   @SuppressLint({"PackageManagerGetSignatures"})
   static class CheckSignatures extends MethodProxy {
      public String getMethodName() {
         return "checkSignatures";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (args.length == 2 && args[0] instanceof String && args[1] instanceof String) {
            String pkgNameOne = (String)args[0];
            String pkgNameTwo = (String)args[1];
            return TextUtils.equals(pkgNameOne, pkgNameTwo) ? 0 : VPackageManager.get().checkSignatures(pkgNameOne, pkgNameTwo);
         } else {
            return method.invoke(who, args);
         }
      }
   }

   static class SetApplicationEnabledSetting extends MethodProxy {
      public String getMethodName() {
         return "setApplicationEnabledSetting";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         replaceLastUserId(args);
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class QueryContentProviders extends MethodProxy {
      public String getMethodName() {
         return "queryContentProviders";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         boolean slice = ParceledListSliceCompat.isReturnParceledListSlice(method);
         String processName = (String)args[0];
         int vuid = (Integer)args[1];
         int flags = (int)this.getIntOrLongValue(args[2]);
         List<ProviderInfo> infos = VPackageManager.get().queryContentProviders(processName, vuid, 0);
         Object _hostResult = method.invoke(who, args);
         if (_hostResult != null) {
            List<ProviderInfo> hostResult = (List)(slice ? ParceledListSlice.getList.call(_hostResult) : _hostResult);

            ProviderInfo info;
            for(Iterator<ProviderInfo> it = hostResult.iterator(); it.hasNext(); ComponentFixer.fixOutsideComponentInfo(info)) {
               info = (ProviderInfo)it.next();
               if (this.isAppPkg(info.packageName) || !isOutsidePackage(info.packageName)) {
                  it.remove();
               }
            }

            infos.addAll(hostResult);
         }

         return slice ? ParceledListSliceCompat.create(infos) : infos;
      }
   }

   static class GetPersistentApplications extends MethodProxy {
      public String getMethodName() {
         return "getPersistentApplications";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return ParceledListSliceCompat.isReturnParceledListSlice(method) ? ParceledListSliceCompat.create(new ArrayList(0)) : new ArrayList(0);
      }
   }

   static class QuerySliceContentProviders extends QueryContentProviders {
      public String getMethodName() {
         return "querySliceContentProviders";
      }
   }

   static class GetPackagesForUid extends MethodProxy {
      public String getMethodName() {
         return "getPackagesForUid";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (VClient.get().getClientConfig() == null) {
            return method.invoke(who, args);
         } else {
            int uid = (Integer)args[0];
            if (uid == 1000) {
               return method.invoke(who, args);
            } else {
               if (uid == getRealUid()) {
                  uid = VClient.get().getVUid();
               }

               String[] pkgs = VPackageManager.get().getPackagesForUid(uid);
               return pkgs == null ? null : pkgs;
            }
         }
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
         String permName = (String)args[0];
         String pkgName = (String)args[1];
         int userId = VUserHandle.myUserId();
         return VPackageManager.get().checkPermission(permName, pkgName, userId);
      }

      public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
         return super.afterCall(who, method, args, result);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class AddPackageToPreferred extends MethodProxy {
      public String getMethodName() {
         return "addPackageToPreferred";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return 0;
      }
   }

   static class CanRequestPackageInstalls extends MethodProxy {
      public String getMethodName() {
         return "canRequestPackageInstalls";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (VirtualCore.get().getAppCallback() != null) {
            return true;
         } else {
            MethodParameterUtils.replaceFirstAppPkg(args);
            replaceLastUserId(args);
            return super.call(who, method, args);
         }
      }
   }

   static class GetApplicationEnabledSetting extends MethodProxy {
      public String getMethodName() {
         return "getApplicationEnabledSetting";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String pkg = (String)args[0];
         if (this.isAppPkg(pkg)) {
            return 1;
         } else if (isOutsidePackage(pkg)) {
            args[1] = 0;
            return method.invoke(who, args);
         } else {
            return 2;
         }
      }
   }

   static class SetApplicationBlockedSettingAsUser extends MethodProxy {
      public String getMethodName() {
         return "setApplicationBlockedSettingAsUser";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         replaceLastUserId(args);
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class DeleteApplicationCacheFiles extends MethodProxy {
      public String getMethodName() {
         return "deleteApplicationCacheFiles";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String pkg = (String)args[0];
         IPackageDataObserver observer = (IPackageDataObserver)args[1];
         if (pkg.equals(getAppPkg())) {
            ApplicationInfo info = VPackageManager.get().getApplicationInfo(pkg, 0, getAppUserId());
            if (info != null) {
               File dir = new File(info.dataDir);
               FileUtils.deleteDir(dir);
               dir.mkdirs();
               if (VERSION.SDK_INT >= 24) {
                  dir = new File(info.deviceProtectedDataDir);
                  FileUtils.deleteDir(dir);
                  dir.mkdirs();
               }

               if (observer != null) {
                  observer.onRemoveCompleted(pkg, true);
               }

               return 0;
            }
         }

         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static final class GetPackageInfo extends MethodProxy {
      public String getMethodName() {
         return "getPackageInfo";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String pkg = (String)args[0];
         int flags = (int)this.getIntOrLongValue(args[1]);
         int userId = VUserHandle.myUserId();
         if ((flags & 4194304) != 0) {
            flags &= -4194305;
            args[1] = flags;
         }

         if ((flags & 2097152) != 0) {
            replaceLastUserId(args);
            return method.invoke(who, args);
         } else {
            PackageInfo packageInfo = VPackageManager.get().getPackageInfo(pkg, flags, userId);
            if (packageInfo != null) {
               return packageInfo;
            } else {
               replaceLastUserId(args);
               packageInfo = (PackageInfo)method.invoke(who, args);
               if (packageInfo != null && isOutsidePackage(packageInfo.packageName)) {
                  ComponentFixer.fixOutsideApplicationInfo(packageInfo.applicationInfo);
                  return packageInfo;
               } else {
                  return null;
               }
            }
         }
      }
   }

   static class GetPermissionInfo extends MethodProxy {
      public String getMethodName() {
         return "getPermissionInfo";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String name = (String)args[0];
         int flags = (Integer)args[args.length - 1];
         PermissionInfo info = VPackageManager.get().getPermissionInfo(name, flags);
         return info != null ? info : super.call(who, method, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetPermissionGroupInfo extends MethodProxy {
      public String getMethodName() {
         return "getPermissionGroupInfo";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String name = (String)args[0];
         int flags = (int)this.getIntOrLongValue(args[1]);
         PermissionGroupInfo info = VPackageManager.get().getPermissionGroupInfo(name, flags);
         return info != null ? info : super.call(who, method, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class ClearPackagePersistentPreferredActivities extends MethodProxy {
      public String getMethodName() {
         return "clearPackagePersistentPreferredActivities";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         replaceLastUserId(args);
         return method.invoke(who, args);
      }
   }

   static class ResolveService extends MethodProxy {
      public String getMethodName() {
         return "resolveService";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Intent intent = (Intent)args[0];
         String resolvedType = (String)args[1];
         int flags = (int)this.getIntOrLongValue(args[2]);
         int userId = VUserHandle.myUserId();
         ResolveInfo resolveInfo = VPackageManager.get().resolveService(intent, resolvedType, flags, userId);
         if (resolveInfo != null) {
            return resolveInfo;
         } else {
            replaceLastUserId(args);
            ResolveInfo info = (ResolveInfo)method.invoke(who, args);
            if (info != null && isOutsidePackage(info.serviceInfo.packageName)) {
               ComponentFixer.fixOutsideComponentInfo(info.serviceInfo);
               return info;
            } else {
               return null;
            }
         }
      }
   }

   static class QueryIntentActivities extends MethodProxy {
      public String getMethodName() {
         return "queryIntentActivities";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         boolean slice = ParceledListSliceCompat.isReturnParceledListSlice(method);
         int userId = VUserHandle.myUserId();
         List<ResolveInfo> appResult = VPackageManager.get().queryIntentActivities((Intent)args[0], (String)args[1], (int)this.getIntOrLongValue(args[2]), userId);
         replaceLastUserId(args);
         Object _hostResult = method.invoke(who, args);
         if (_hostResult != null) {
            List<ResolveInfo> hostResult = (List)(slice ? ParceledListSlice.getList.call(_hostResult) : _hostResult);
            if (hostResult != null) {
               Iterator<ResolveInfo> iterator = hostResult.iterator();

               while(true) {
                  while(iterator.hasNext()) {
                     ResolveInfo info = (ResolveInfo)iterator.next();
                     if (info != null && info.activityInfo != null && isOutsidePackage(info.activityInfo.packageName)) {
                        ComponentFixer.fixOutsideComponentInfo(info.activityInfo);
                     } else {
                        iterator.remove();
                     }
                  }

                  appResult.addAll(hostResult);
                  break;
               }
            }
         }

         return slice ? ParceledListSliceCompat.create(appResult) : appResult;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetPackageGidsEtc extends GetPackageGids {
      public String getMethodName() {
         return super.getMethodName() + "Etc";
      }
   }

   static class IsPackageForzen extends MethodProxy {
      public String getMethodName() {
         return "isPackageForzen";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return false;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetPermissions extends MethodProxy {
      public String getMethodName() {
         return "getPermissions";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         replaceLastUserId(args);
         return method.invoke(who, args);
      }
   }

   static class QueryIntentServices extends MethodProxy {
      public String getMethodName() {
         return "queryIntentServices";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         boolean slice = ParceledListSliceCompat.isReturnParceledListSlice(method);
         int userId = VUserHandle.myUserId();
         Intent intent = (Intent)args[0];
         List<ResolveInfo> appResult = VPackageManager.get().queryIntentServices(intent, (String)args[1], (int)this.getIntOrLongValue(args[2]), userId);
         replaceLastUserId(args);
         Object _hostResult = method.invoke(who, args);
         if (_hostResult != null) {
            Object obj;
            if (slice) {
               obj = ParceledListSlice.getList.call(_hostResult);
            } else {
               obj = _hostResult;
            }

            List<ResolveInfo> hostResult = (List)obj;
            if (hostResult != null) {
               Iterator<ResolveInfo> iterator = hostResult.iterator();

               label38:
               while(true) {
                  ResolveInfo info;
                  do {
                     if (!iterator.hasNext()) {
                        break label38;
                     }

                     info = (ResolveInfo)iterator.next();
                     if (isHostIntent(intent)) {
                        break label38;
                     }
                  } while(info != null && info.serviceInfo != null && isOutsidePackage(info.serviceInfo.packageName));

                  iterator.remove();
               }

               appResult.addAll(hostResult);
            }
         }

         return slice ? ParceledListSliceCompat.create(appResult) : appResult;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class ResolveContentProvider extends MethodProxy {
      public String getMethodName() {
         return "resolveContentProvider";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String name = (String)args[0];
         int flags = (int)this.getIntOrLongValue(args[1]);
         int userId = VUserHandle.myUserId();
         ProviderInfo info = VPackageManager.get().resolveContentProvider(name, flags, userId);
         if (info == null) {
            replaceLastUserId(args);
            info = (ProviderInfo)method.invoke(who, args);
            if (info != null && isOutsidePackage(info.packageName)) {
               return info;
            }
         }

         return info;
      }
   }

   static class ClearPackagePreferredActivities extends MethodProxy {
      public String getMethodName() {
         return "clearPackagePreferredActivities";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         return method.invoke(who, args);
      }
   }

   static class RevokeRuntimePermission extends MethodProxy {
      public String getMethodName() {
         return "revokeRuntimePermission";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         replaceLastUserId(args);
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetPackageGids extends MethodProxy {
      public String getMethodName() {
         return "getPackageGids";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         replaceLastUserId(args);
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetPackageInstaller extends MethodProxy {
      public boolean isEnable() {
         return isAppProcess();
      }

      public String getMethodName() {
         return "getPackageInstaller";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IInterface installer = (IInterface)method.invoke(who, args);
         final IPackageInstaller vInstaller = VPackageManager.get().getPackageInstaller();
         return Proxy.newProxyInstance(installer.getClass().getClassLoader(), installer.getClass().getInterfaces(), new InvocationHandler() {
            @TargetApi(21)
            private Object createSession(Object proxy, Method method, Object[] args) throws RemoteException {
               SessionParams params = SessionParams.create((PackageInstaller.SessionParams)args[0]);
               String installerPackageName = (String)args[1];
               return vInstaller.createSession(params, installerPackageName, VUserHandle.myUserId());
            }

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
               VLog.e("PackageInstaller", "call " + method.getName() + " -> " + Arrays.toString(args));
               String var4 = method.getName();
               byte var5 = -1;
               switch (var4.hashCode()) {
                  case -1776922004:
                     if (var4.equals("toString")) {
                        var5 = 12;
                     }
                     break;
                  case -663066834:
                     if (var4.equals("getSessionInfo")) {
                        var5 = 6;
                     }
                     break;
                  case -652885011:
                     if (var4.equals("updateSessionAppIcon")) {
                        var5 = 2;
                     }
                     break;
                  case -403218424:
                     if (var4.equals("registerCallback")) {
                        var5 = 9;
                     }
                     break;
                  case -298116903:
                     if (var4.equals("getStagedSessions")) {
                        var5 = 0;
                     }
                     break;
                  case -93516191:
                     if (var4.equals("abandonSession")) {
                        var5 = 4;
                     }
                     break;
                  case -63461894:
                     if (var4.equals("createSession")) {
                        var5 = 1;
                     }
                     break;
                  case 938656808:
                     if (var4.equals("getAllSessions")) {
                        var5 = 7;
                     }
                     break;
                  case 1170196863:
                     if (var4.equals("setPermissionsResult")) {
                        var5 = 11;
                     }
                     break;
                  case 1238099456:
                     if (var4.equals("updateSessionAppLabel")) {
                        var5 = 3;
                     }
                     break;
                  case 1568181855:
                     if (var4.equals("getMySessions")) {
                        var5 = 8;
                     }
                     break;
                  case 1738611873:
                     if (var4.equals("unregisterCallback")) {
                        var5 = 10;
                     }
                     break;
                  case 1788161260:
                     if (var4.equals("openSession")) {
                        var5 = 5;
                     }
               }

               int sessionId;
               IPackageInstallerCallback callback;
               switch (var5) {
                  case 0:
                     return ParceledListSliceCompat.create(Collections.EMPTY_LIST);
                  case 1:
                     return this.createSession(proxy, method, args);
                  case 2:
                     sessionId = (Integer)args[0];
                     Bitmap appIcon = (Bitmap)args[1];
                     vInstaller.updateSessionAppIcon(sessionId, appIcon);
                     return 0;
                  case 3:
                     sessionId = (Integer)args[0];
                     String appLabel = (String)args[1];
                     vInstaller.updateSessionAppLabel(sessionId, appLabel);
                     return 0;
                  case 4:
                     vInstaller.abandonSession((Integer)args[0]);
                     return 0;
                  case 5:
                     return vInstaller.openSession((Integer)args[0]);
                  case 6:
                     SessionInfo infox = vInstaller.getSessionInfo((Integer)args[0]);
                     if (infox != null) {
                        return infox.alloc();
                     }

                     return null;
                  case 7:
                     sessionId = (Integer)args[0];
                     List<SessionInfo> infos = vInstaller.getAllSessions(sessionId).getList();
                     List<PackageInstaller.SessionInfo> sysInfos = new ArrayList(infos.size());
                     Iterator var20 = infos.iterator();

                     while(var20.hasNext()) {
                        SessionInfo infoxx = (SessionInfo)var20.next();
                        sysInfos.add(infoxx.alloc());
                     }

                     return ParceledListSliceCompat.create(sysInfos);
                  case 8:
                     String installerPackageName = (String)args[0];
                     int userId = (Integer)args[1];
                     List<SessionInfo> infosx = vInstaller.getMySessions(installerPackageName, userId).getList();
                     List<PackageInstaller.SessionInfo> sysInfosx = new ArrayList(infosx.size());
                     Iterator var10 = infosx.iterator();

                     while(var10.hasNext()) {
                        SessionInfo info = (SessionInfo)var10.next();
                        sysInfosx.add(info.alloc());
                     }

                     return ParceledListSliceCompat.create(sysInfosx);
                  case 9:
                     callback = (IPackageInstallerCallback)args[0];
                     vInstaller.registerCallback(callback, VUserHandle.myUserId());
                     return 0;
                  case 10:
                     callback = (IPackageInstallerCallback)args[0];
                     vInstaller.unregisterCallback(callback);
                     return 0;
                  case 11:
                     sessionId = (Integer)args[0];
                     boolean accepted = (Boolean)args[1];
                     vInstaller.setPermissionsResult(sessionId, accepted);
                     return 0;
                  case 12:
                     return "PackageInstaller";
                  default:
                     VLog.printStackTrace("PackageInstaller");
                     throw new RuntimeException("Not support PackageInstaller method : " + method.getName());
               }
            }
         });
      }
   }

   static class GetPackageUidEtc extends GetPackageUid {
      public String getMethodName() {
         return super.getMethodName() + "Etc";
      }
   }

   static class GetActivityInfo extends MethodProxy {
      public String getMethodName() {
         return "getActivityInfo";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         ComponentName componentName = (ComponentName)args[0];
         if (getHostPkg().equals(componentName.getPackageName())) {
            return method.invoke(who, args);
         } else {
            int userId = VUserHandle.myUserId();
            int flags = (int)this.getIntOrLongValue(args[1]);
            ActivityInfo info = VPackageManager.get().getActivityInfo(componentName, flags, userId);
            if (info == null) {
               replaceLastUserId(args);
               info = (ActivityInfo)method.invoke(who, args);
               if (info == null || !isOutsidePackage(info.packageName)) {
                  return null;
               }

               ComponentFixer.fixOutsideComponentInfo(info);
            }

            return info;
         }
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetPackageUid extends MethodProxy {
      public String getMethodName() {
         return "getPackageUid";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String pkgName = (String)args[0];
         MethodParameterUtils.replaceFirstAppPkg(args);
         replaceLastUserId(args);
         return !this.isAppPkg(pkgName) && !isOutsidePackage(pkgName) ? -1 : method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetServiceInfo extends MethodProxy {
      public String getMethodName() {
         return "getServiceInfo";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         ComponentName componentName = (ComponentName)args[0];
         int flags = (int)this.getIntOrLongValue(args[1]);
         int userId = VUserHandle.myUserId();
         ServiceInfo info = VPackageManager.get().getServiceInfo(componentName, flags, userId);
         if (info != null) {
            return info;
         } else {
            replaceLastUserId(args);
            info = (ServiceInfo)method.invoke(who, args);
            if (info != null && isOutsidePackage(info.packageName)) {
               ComponentFixer.fixOutsideComponentInfo(info);
               return info;
            } else {
               return null;
            }
         }
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class RemovePackageFromPreferred extends MethodProxy {
      public String getMethodName() {
         return "removePackageFromPreferred";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         return method.invoke(who, args);
      }
   }

   static class GetComponentEnabledSetting extends MethodProxy {
      public String getMethodName() {
         return "getComponentEnabledSetting";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         ComponentName component = (ComponentName)args[0];
         return VPackageManager.get().getComponentEnabledSetting(component, getAppUserId());
      }
   }

   static class GetPreferredActivities extends MethodProxy {
      public String getMethodName() {
         return "getPreferredActivities";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceLastAppPkg(args);
         return method.invoke(who, args);
      }
   }

   static class GetInstallerPackageName extends MethodProxy {
      public String getMethodName() {
         return "getInstallerPackageName";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return "com.android.vending";
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class IsPackageAvailable extends MethodProxy {
      public String getMethodName() {
         return "isPackageAvailable";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String pkgName = (String)args[0];
         if (this.isAppPkg(pkgName)) {
            return true;
         } else {
            replaceLastUserId(args);
            return method.invoke(who, args);
         }
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class CanForwardTo extends MethodProxy {
      public String getMethodName() {
         return "canForwardTo";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         int sourceUserId = (Integer)args[2];
         int targetUserId = (Integer)args[3];
         return sourceUserId == targetUserId;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetUidForSharedUser extends MethodProxy {
      public String getMethodName() {
         return "getUidForSharedUser";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String sharedUserName = (String)args[0];
         return VirtualCore.get().getUidForSharedUser(sharedUserName);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class GetSharedLibraries extends MethodProxy {
      public String getMethodName() {
         return "getSharedLibraries";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         int flags = (int)this.getIntOrLongValue(args[1]);
         if ((flags & 4194304) != 0) {
            flags &= -4194305;
            args[1] = flags;
         }

         args[0] = getHostPkg();
         return method.invoke(who, args);
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class CheckPackageStartable extends MethodProxy {
      public String getMethodName() {
         return "checkPackageStartable";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String pkg = (String)args[0];
         if (this.isAppPkg(pkg)) {
            return 0;
         } else {
            replaceLastUserId(args);
            return method.invoke(who, args);
         }
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class FreeStorageAndNotify extends MethodProxy {
      public String getMethodName() {
         return "freeStorageAndNotify";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IPackageDataObserver observer = (IPackageDataObserver)args[args.length - 1];
         if (observer != null) {
            observer.onRemoveCompleted(getAppPkg(), true);
         }

         return 0;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }

   static class FreeStorage extends MethodProxy {
      public String getMethodName() {
         return "freeStorage";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IntentSender sender = (IntentSender)ArrayUtils.getFirst(args, IntentSender.class);
         if (sender != null) {
            sender.sendIntent(getHostContext(), 0, (Intent)null, (IntentSender.OnFinished)null, (Handler)null);
         }

         return 0;
      }

      public boolean isEnable() {
         return isAppProcess();
      }
   }
}
