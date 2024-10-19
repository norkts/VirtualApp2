package com.lody.virtual.server.vs;

import android.os.Parcel;
import android.util.SparseArray;
import com.lody.virtual.helper.PersistenceLayer;
import com.lody.virtual.os.VEnvironment;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class VSPersistenceLayer extends PersistenceLayer {
   private static final char[] MAGIC = new char[]{'v', 's', 'a'};
   private static final int CURRENT_VERSION = 1;
   private final VirtualStorageService mService;

   VSPersistenceLayer(VirtualStorageService service) {
      super(VEnvironment.getVSConfigFile());
      this.mService = service;
   }

   public int getCurrentVersion() {
      return 1;
   }

   public void writeMagic(Parcel p) {
      p.writeCharArray(MAGIC);
   }

   public boolean verifyMagic(Parcel p) {
      char[] magic = p.createCharArray();
      return Arrays.equals(magic, MAGIC);
   }

   public void writePersistenceData(Parcel p) {
      SparseArray<HashMap<String, VSConfig>> configs = this.mService.getConfigs();
      int N = configs.size();
      p.writeInt(N);

      while(N-- > 0) {
         int userId = configs.keyAt(N);
         Map<String, VSConfig> userMap = (Map)configs.valueAt(N);
         p.writeInt(userId);
         p.writeMap(userMap);
      }

   }

   public void readPersistenceData(Parcel p, int version) {
      SparseArray<HashMap<String, VSConfig>> configs = this.mService.getConfigs();
      int N = p.readInt();

      while(N-- > 0) {
         int userId = p.readInt();
         HashMap<String, VSConfig> userMap = p.readHashMap(VSConfig.class.getClassLoader());
         configs.put(userId, userMap);
      }

   }

   public void onPersistenceFileDamage() {
   }
}
