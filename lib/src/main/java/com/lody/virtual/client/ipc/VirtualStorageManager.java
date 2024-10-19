package com.lody.virtual.client.ipc;

import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.server.interfaces.IVirtualStorageService;

public class VirtualStorageManager {
   private static final VirtualStorageManager sInstance = new VirtualStorageManager();
   private IVirtualStorageService mService;

   public static VirtualStorageManager get() {
      return sInstance;
   }

   public IVirtualStorageService getService() {
      if (this.mService == null || !IInterfaceUtils.isAlive(this.mService)) {
         synchronized(this) {
            Object binder = this.getRemoteInterface();
            this.mService = (IVirtualStorageService)LocalProxyUtils.genProxy(IVirtualStorageService.class, binder);
         }
      }

      return this.mService;
   }

   private Object getRemoteInterface() {
      return IVirtualStorageService.Stub.asInterface(ServiceManagerNative.getService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT02Vg=="))));
   }

   public String getVirtualStorage(String packageName, int userId) {
      try {
         return this.getService().getVirtualStorage(packageName, userId);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (String)VirtualRuntime.crash(e);
      }
   }

   public void setVirtualStorageState(String packageName, int userId, boolean enable) {
      try {
         this.getService().setVirtualStorageState(packageName, userId, enable);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         VirtualRuntime.crash(e);
      }

   }

   public boolean isVirtualStorageEnable(String packageName, int userId) {
      try {
         return this.getService().isVirtualStorageEnable(packageName, userId);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public void setVirtualStorage(String packageName, int userId, String vsPath) {
      try {
         this.getService().setVirtualStorage(packageName, userId, vsPath);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         VirtualRuntime.crash(e);
      }

   }
}
