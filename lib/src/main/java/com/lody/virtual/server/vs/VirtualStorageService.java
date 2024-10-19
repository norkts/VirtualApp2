package com.lody.virtual.server.vs;

import android.util.SparseArray;
import com.lody.virtual.StringFog;
import com.lody.virtual.server.interfaces.IVirtualStorageService;
import com.lody.virtual.server.pm.VUserManagerService;
import java.io.File;
import java.util.HashMap;

public class VirtualStorageService extends IVirtualStorageService.Stub {
   private static final VirtualStorageService sService = new VirtualStorageService();
   private static final String[] sPublicDirs = new String[]{"DCIM", "Music", "Pictures"};
   private final VSPersistenceLayer mLayer = new VSPersistenceLayer(this);
   private final SparseArray<HashMap<String, VSConfig>> mConfigs = new SparseArray();

   public static VirtualStorageService get() {
      return sService;
   }

   private VirtualStorageService() {
      this.mLayer.read();
   }

   SparseArray<HashMap<String, VSConfig>> getConfigs() {
      return this.mConfigs;
   }

   public void setVirtualStorage(String packageName, int userId, String vsPath) {
      this.checkUserId(userId);
      synchronized(this.mConfigs) {
         VSConfig config = this.getOrCreateVSConfigLocked(packageName, userId);
         config.vsPath = vsPath;
         this.mLayer.save();
      }

      this.preInitPublicPath(vsPath);
   }

   private void preInitPublicPath(String vsPath) {
      new File(vsPath, "DCIM");
      String[] var2 = sPublicDirs;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String dir = var2[var4];
         File file = new File(vsPath, dir);
         if (!file.exists()) {
            file.mkdirs();
         }
      }

   }

   private VSConfig getOrCreateVSConfigLocked(String packageName, int userId) {
      HashMap<String, VSConfig> userMap = (HashMap)this.mConfigs.get(userId);
      if (userMap == null) {
         userMap = new HashMap();
         this.mConfigs.put(userId, userMap);
      }

      VSConfig config = (VSConfig)userMap.get(packageName);
      if (config == null) {
         config = new VSConfig();
         config.enable = true;
         userMap.put(packageName, config);
      }

      return config;
   }

   public String getVirtualStorage(String packageName, int userId) {
      this.checkUserId(userId);
      synchronized(this.mConfigs) {
         VSConfig config = this.getOrCreateVSConfigLocked(packageName, userId);
         return config.vsPath;
      }
   }

   public void setVirtualStorageState(String packageName, int userId, boolean enable) {
      this.checkUserId(userId);
      synchronized(this.mConfigs) {
         VSConfig config = this.getOrCreateVSConfigLocked(packageName, userId);
         config.enable = enable;
         this.mLayer.save();
      }
   }

   public boolean isVirtualStorageEnable(String packageName, int userId) {
      this.checkUserId(userId);
      synchronized(this.mConfigs) {
         VSConfig config = this.getOrCreateVSConfigLocked(packageName, userId);
         return config.enable;
      }
   }

   private void checkUserId(int userId) {
      if (!VUserManagerService.get().exists(userId)) {
         throw new IllegalStateException("Invalid userId " + userId);
      }
   }
}
