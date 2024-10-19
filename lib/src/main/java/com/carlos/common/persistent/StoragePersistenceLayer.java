package com.carlos.common.persistent;

import android.os.Parcel;
import com.carlos.common.network.StringFog;
import java.io.File;

public class StoragePersistenceLayer extends PersistenceLayer {
   private StoragePersistenceServices mService;

   StoragePersistenceLayer(StoragePersistenceServices service) {
      super(new File("/data/data/com.carlos.multiapp", "app-config.ini"));
      this.mService = service;
   }

   public int getCurrentVersion() {
      return 3;
   }

   public void writeMagic(Parcel p) {
   }

   public boolean verifyMagic(Parcel p) {
      return true;
   }

   public void writePersistenceData(Parcel p) {
      VPersistent infos = this.mService.mVPersistent;
      infos.writeToParcel(p, 0);
   }

   public void readPersistenceData(Parcel p, int version) {
      this.mService.mVPersistent.readToParcel(p);
   }

   public void onPersistenceFileDamage() {
      this.getPersistenceFile().delete();
   }
}
