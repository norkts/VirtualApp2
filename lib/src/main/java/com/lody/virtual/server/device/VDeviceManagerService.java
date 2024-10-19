package com.lody.virtual.server.device;

import com.lody.virtual.helper.collection.SparseArray;
import com.lody.virtual.remote.VDeviceConfig;
import com.lody.virtual.server.interfaces.IDeviceManager;

public class VDeviceManagerService extends IDeviceManager.Stub {
   private static final VDeviceManagerService sInstance = new VDeviceManagerService();
   final SparseArray<VDeviceConfig> mDeviceConfigs = new SparseArray();
   private DeviceInfoPersistenceLayer mPersistenceLayer = new DeviceInfoPersistenceLayer(this);

   public static VDeviceManagerService get() {
      return sInstance;
   }

   private VDeviceManagerService() {
      this.mPersistenceLayer.read();

      for(int i = 0; i < this.mDeviceConfigs.size(); ++i) {
         VDeviceConfig info = (VDeviceConfig)this.mDeviceConfigs.valueAt(i);
         VDeviceConfig.addToPool(info);
      }

   }

   public VDeviceConfig getDeviceConfig(int userId) {
      synchronized(this.mDeviceConfigs) {
         VDeviceConfig info = (VDeviceConfig)this.mDeviceConfigs.get(userId);
         if (info == null) {
            info = VDeviceConfig.random();
            this.mDeviceConfigs.put(userId, info);
            this.mPersistenceLayer.save();
         }

         return info;
      }
   }

   public void updateDeviceConfig(int userId, VDeviceConfig config) {
      synchronized(this.mDeviceConfigs) {
         if (config != null) {
            this.mDeviceConfigs.put(userId, config);
            this.mPersistenceLayer.save();
         }

      }
   }

   public boolean isEnable(int userId) {
      return this.getDeviceConfig(userId).enable;
   }

   public void setEnable(int userId, boolean enable) {
      synchronized(this.mDeviceConfigs) {
         VDeviceConfig info = (VDeviceConfig)this.mDeviceConfigs.get(userId);
         if (info == null) {
            info = VDeviceConfig.random();
            this.mDeviceConfigs.put(userId, info);
         }

         info.enable = enable;
         this.mPersistenceLayer.save();
      }
   }
}
