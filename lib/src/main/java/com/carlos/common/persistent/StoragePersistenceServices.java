package com.carlos.common.persistent;

import android.content.Context;
import com.carlos.common.network.StringFog;

public class StoragePersistenceServices {
   public static String SERVICE_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC0cKmo3AiFoIgEcKhccJ2wKAgVpDSQ6Li5SVg=="));
   static StoragePersistenceServices mStoragePersistenceServices = new StoragePersistenceServices();
   Context mContext;
   private StoragePersistenceLayer mPersistenceLayer = new StoragePersistenceLayer(this);
   public VPersistent mVPersistent = new VPersistent();

   public static StoragePersistenceServices get() {
      return mStoragePersistenceServices;
   }

   public StoragePersistenceServices() {
      this.mPersistenceLayer.read();
   }

   public void updatePersistent(VPersistent config) {
      synchronized(this.mVPersistent) {
         if (config != null) {
            this.mVPersistent = config;
            this.mPersistenceLayer.save();
         }

      }
   }

   public VPersistent getVPersistent() {
      synchronized(this.mPersistenceLayer) {
         this.mPersistenceLayer.read();
      }

      return this.mVPersistent;
   }
}
