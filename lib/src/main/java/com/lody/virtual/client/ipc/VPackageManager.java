package com.lody.virtual.client.ipc;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.remote.ReceiverInfo;
import com.lody.virtual.server.IPackageInstaller;
import com.lody.virtual.server.interfaces.IPackageManager;
import java.util.List;

public class VPackageManager {
   private static final VPackageManager sMgr = new VPackageManager();
   private IPackageManager mService;

   public IPackageManager getService() {
      if (!IInterfaceUtils.isAlive(this.mService)) {
         Class var1 = VPackageManager.class;
         synchronized(VPackageManager.class) {
            Object remote = this.getRemoteInterface();
            this.mService = (IPackageManager)LocalProxyUtils.genProxy(IPackageManager.class, remote);
         }
      }

      return this.mService;
   }

   private Object getRemoteInterface() {
      return IPackageManager.Stub.asInterface(ServiceManagerNative.getService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iAVRF"))));
   }

   public static VPackageManager get() {
      return sMgr;
   }

   public int checkPermission(String permission, String pkgName, int userId) {
      try {
         return this.getService().checkPermission(VirtualCore.get().isExtPackage(), permission, pkgName, userId);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (Integer)VirtualRuntime.crash(e);
      }
   }

   public ResolveInfo resolveService(Intent intent, String resolvedType, int flags, int userId) {
      try {
         return this.getService().resolveService(intent, resolvedType, flags, userId);
      } catch (RemoteException var6) {
         RemoteException e = var6;
         return (ResolveInfo)VirtualRuntime.crash(e);
      }
   }

   public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) {
      try {
         return this.getService().getPermissionGroupInfo(name, flags);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (PermissionGroupInfo)VirtualRuntime.crash(e);
      }
   }

   public List<ApplicationInfo> getInstalledApplications(int flags, int userId) {
      try {
         return this.getService().getInstalledApplications(flags, userId).getList();
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (List)VirtualRuntime.crash(e);
      }
   }

   public PackageInfo getPackageInfo(String packageName, int flags, int userId) {
      try {
         return this.getService().getPackageInfo(packageName, flags, userId);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (PackageInfo)VirtualRuntime.crash(e);
      }
   }

   public ResolveInfo resolveIntent(Intent intent, String resolvedType, int flags, int userId) {
      try {
         return this.getService().resolveIntent(intent, resolvedType, flags, userId);
      } catch (RemoteException var6) {
         RemoteException e = var6;
         return (ResolveInfo)VirtualRuntime.crash(e);
      }
   }

   public List<ResolveInfo> queryIntentContentProviders(Intent intent, String resolvedType, int flags, int userId) {
      try {
         return this.getService().queryIntentContentProviders(intent, resolvedType, flags, userId);
      } catch (RemoteException var6) {
         RemoteException e = var6;
         return (List)VirtualRuntime.crash(e);
      }
   }

   public ActivityInfo getReceiverInfo(ComponentName componentName, int flags, int userId) {
      try {
         return this.getService().getReceiverInfo(componentName, flags, userId);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (ActivityInfo)VirtualRuntime.crash(e);
      }
   }

   public List<PackageInfo> getInstalledPackages(int flags, int userId) {
      try {
         return this.getService().getInstalledPackages(flags, userId).getList();
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (List)VirtualRuntime.crash(e);
      }
   }

   public List<PermissionInfo> queryPermissionsByGroup(String group, int flags) {
      try {
         return this.getService().queryPermissionsByGroup(group, flags);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (List)VirtualRuntime.crash(e);
      }
   }

   public PermissionInfo getPermissionInfo(String name, int flags) {
      try {
         return this.getService().getPermissionInfo(name, flags);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (PermissionInfo)VirtualRuntime.crash(e);
      }
   }

   public ActivityInfo getActivityInfo(ComponentName componentName, int flags, int userId) {
      try {
         return this.getService().getActivityInfo(componentName, flags, userId);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (ActivityInfo)VirtualRuntime.crash(e);
      }
   }

   public List<ResolveInfo> queryIntentReceivers(Intent intent, String resolvedType, int flags, int userId) {
      try {
         return this.getService().queryIntentReceivers(intent, resolvedType, flags, userId);
      } catch (RemoteException var6) {
         RemoteException e = var6;
         return (List)VirtualRuntime.crash(e);
      }
   }

   public List<PermissionGroupInfo> getAllPermissionGroups(int flags) {
      try {
         return this.getService().getAllPermissionGroups(flags);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (List)VirtualRuntime.crash(e);
      }
   }

   public List<ResolveInfo> queryIntentActivities(Intent intent, String resolvedType, int flags, int userId) {
      try {
         return this.getService().queryIntentActivities(intent, resolvedType, flags, userId);
      } catch (RemoteException var6) {
         RemoteException e = var6;
         return (List)VirtualRuntime.crash(e);
      }
   }

   public List<ResolveInfo> queryIntentServices(Intent intent, String resolvedType, int flags, int userId) {
      try {
         return this.getService().queryIntentServices(intent, resolvedType, flags, userId);
      } catch (RemoteException var6) {
         RemoteException e = var6;
         return (List)VirtualRuntime.crash(e);
      }
   }

   public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) {
      try {
         return this.getService().getApplicationInfo(packageName, flags, userId);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (ApplicationInfo)VirtualRuntime.crash(e);
      }
   }

   public ProviderInfo resolveContentProvider(String name, int flags, int userId) {
      try {
         return this.getService().resolveContentProvider(name, flags, userId);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (ProviderInfo)VirtualRuntime.crash(e);
      }
   }

   public ServiceInfo getServiceInfo(ComponentName componentName, int flags, int userId) {
      try {
         return this.getService().getServiceInfo(componentName, flags, userId);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (ServiceInfo)VirtualRuntime.crash(e);
      }
   }

   public ProviderInfo getProviderInfo(ComponentName componentName, int flags, int userId) {
      try {
         return this.getService().getProviderInfo(componentName, flags, userId);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (ProviderInfo)VirtualRuntime.crash(e);
      }
   }

   public boolean activitySupportsIntent(ComponentName component, Intent intent, String resolvedType) {
      try {
         return this.getService().activitySupportsIntent(component, intent, resolvedType);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public List<ProviderInfo> queryContentProviders(String processName, int uid, int flags) {
      try {
         return this.getService().queryContentProviders(processName, uid, flags).getList();
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (List)VirtualRuntime.crash(e);
      }
   }

   public List<String> querySharedPackages(String packageName) {
      try {
         return this.getService().querySharedPackages(packageName);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (List)VirtualRuntime.crash(e);
      }
   }

   public String[] getPackagesForUid(int uid) {
      try {
         return this.getService().getPackagesForUid(uid);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (String[])VirtualRuntime.crash(e);
      }
   }

   public int getPackageUid(String packageName, int userId) {
      try {
         return this.getService().getPackageUid(packageName, userId);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (Integer)VirtualRuntime.crash(e);
      }
   }

   public String getNameForUid(int uid) {
      try {
         return this.getService().getNameForUid(uid);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (String)VirtualRuntime.crash(e);
      }
   }

   public IPackageInstaller getPackageInstaller() {
      try {
         return IPackageInstaller.Stub.asInterface(this.getService().getPackageInstaller());
      } catch (RemoteException var2) {
         RemoteException e = var2;
         return (IPackageInstaller)VirtualRuntime.crash(e);
      }
   }

   public int checkSignatures(String pkg1, String pkg2) {
      try {
         return this.getService().checkSignatures(pkg1, pkg2);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (Integer)VirtualRuntime.crash(e);
      }
   }

   public String[] getDangerousPermissions(String packageName) {
      try {
         return this.getService().getDangerousPermissions(packageName);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (String[])VirtualRuntime.crash(e);
      }
   }

   public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags, int userId) {
      try {
         this.getService().setComponentEnabledSetting(componentName, newState, flags, userId);
      } catch (RemoteException var6) {
         RemoteException e = var6;
         VirtualRuntime.crash(e);
      }

   }

   public int getComponentEnabledSetting(ComponentName component, int userId) {
      try {
         return this.getService().getComponentEnabledSetting(component, userId);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (Integer)VirtualRuntime.crash(e);
      }
   }

   public List<ReceiverInfo> getReceiverInfos(String packageName, String processName, int userId) {
      try {
         return this.getService().getReceiverInfos(packageName, processName, userId);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (List)VirtualRuntime.crash(e);
      }
   }
}
