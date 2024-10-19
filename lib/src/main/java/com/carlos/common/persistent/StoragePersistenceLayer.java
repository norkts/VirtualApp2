package com.carlos.common.persistent;

import android.os.Parcel;
import com.carlos.common.network.StringFog;
import java.io.File;

public class StoragePersistenceLayer extends PersistenceLayer {
   private StoragePersistenceServices mService;

   StoragePersistenceLayer(StoragePersistenceServices service) {
      super(new File(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OS4cJGVSDSloNzMiKV82CW8jPyVuIx4pLD4HDn4jJClvHho/KANfOA==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KAdXDX8JQSlqES89KjoMOW8zLFo="))));
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
