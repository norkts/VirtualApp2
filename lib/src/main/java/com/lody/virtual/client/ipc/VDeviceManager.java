package com.lody.virtual.client.ipc;

import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.ReflectException;
import com.lody.virtual.remote.VDeviceConfig;
import com.lody.virtual.server.interfaces.IDeviceManager;
import java.util.Iterator;
import java.util.Map;
import mirror.android.os.Build;

public class VDeviceManager {
   private static final VDeviceManager sInstance = new VDeviceManager();
   private IDeviceManager mService;

   public static VDeviceManager get() {
      return sInstance;
   }

   public IDeviceManager getService() {
      if (!IInterfaceUtils.isAlive(this.mService)) {
         synchronized(this) {
            Object binder = this.getRemoteInterface();
            this.mService = (IDeviceManager)LocalProxyUtils.genProxy(IDeviceManager.class, binder);
         }
      }

      return this.mService;
   }

   private Object getRemoteInterface() {
      return IDeviceManager.Stub.asInterface(ServiceManagerNative.getService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguLmUVLCs="))));
   }

   public VDeviceConfig getDeviceConfig(int userId) {
      try {
         return this.getService().getDeviceConfig(userId);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (VDeviceConfig)VirtualRuntime.crash(e);
      }
   }

   public void updateDeviceConfig(int userId, VDeviceConfig config) {
      try {
         this.getService().updateDeviceConfig(userId, config);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         VirtualRuntime.crash(e);
      }

   }

   public boolean isEnable(int userId) {
      try {
         return this.getService().isEnable(userId);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public void setEnable(int userId, boolean enable) {
      try {
         this.getService().setEnable(userId, enable);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         VirtualRuntime.crash(e);
      }

   }

   public void applyBuildProp(VDeviceConfig config) {
      Iterator var2 = config.buildProp.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry<String, String> entry = (Map.Entry)var2.next();

         try {
            Reflect.on(Build.TYPE).set((String)entry.getKey(), entry.getValue());
         } catch (ReflectException var5) {
            ReflectException e = var5;
            e.printStackTrace();
         }
      }

      if (config.serial != null) {
         Reflect.on(Build.TYPE).set(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IiwuDH0bJA4=")), config.serial);
      }

   }
}
