package com.lody.virtual.server.am;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.server.pm.parser.VPackage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UidSystem {
   private static final String TAG = UidSystem.class.getSimpleName();
   private final HashMap<String, Integer> mSharedUserIdMap = new HashMap();
   private int mFreeUid = 10000;

   public void initUidList() {
      this.mSharedUserIdMap.clear();
      File uidFile = VEnvironment.getUidListFile();
      if (!this.loadUidList(uidFile)) {
         File bakUidFile = VEnvironment.getBakUidListFile();
         this.loadUidList(bakUidFile);
      }

   }

   private boolean loadUidList(File uidFile) {
      if (!uidFile.exists()) {
         return false;
      } else {
         try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(uidFile));
            this.mFreeUid = is.readInt();
            Map<String, Integer> map = (HashMap)is.readObject();
            this.mSharedUserIdMap.putAll(map);
            is.close();
            return true;
         } catch (Throwable var4) {
            return false;
         }
      }
   }

   private void save() {
      File uidFile = VEnvironment.getUidListFile();
      File bakUidFile = VEnvironment.getBakUidListFile();
      IOException e;
      if (uidFile.exists()) {
         if (bakUidFile.exists() && !bakUidFile.delete()) {
            VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IS4+KmojAiZiIwU8JAcYOW4VOCt4HiwcPQgqJ2AaLD9uCiA9Kj4fJGsKRSRlHgo0JF4iLW8KQTF+MyM8Djo6Vg==")) + bakUidFile.getPath());
         }

         try {
            FileUtils.copyFile(uidFile, bakUidFile);
         } catch (IOException var5) {
            e = var5;
            e.printStackTrace();
         }
      }

      try {
         ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(uidFile));
         os.writeInt(this.mFreeUid);
         os.writeObject(this.mSharedUserIdMap);
         os.close();
      } catch (IOException var4) {
         e = var4;
         e.printStackTrace();
      }

   }

   public int getOrCreateUid(VPackage pkg) {
      synchronized(this.mSharedUserIdMap) {
         String sharedUserId = pkg.mSharedUserId;
         if (sharedUserId == null) {
            sharedUserId = pkg.packageName;
         }

         Integer uid = (Integer)this.mSharedUserIdMap.get(sharedUserId);
         if (uid != null) {
            return uid;
         } else {
            int newUid = ++this.mFreeUid;
            if (newUid == VirtualCore.get().myUid()) {
               newUid = ++this.mFreeUid;
            }

            this.mSharedUserIdMap.put(sharedUserId, newUid);
            this.save();
            return newUid;
         }
      }
   }

   public int getUid(String sharedUserName) {
      synchronized(this.mSharedUserIdMap) {
         Integer uid = (Integer)this.mSharedUserIdMap.get(sharedUserName);
         return uid != null ? uid : -1;
      }
   }
}
