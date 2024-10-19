package com.lody.virtual.helper;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.lody.virtual.remote.InstalledAppInfo;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PackageCleaner {
   public static void cleanUsers(File usersDir) {
      List<VUserInfo> users = VUserManager.get().getUsers();
      Set<Integer> userIds = new HashSet(users.size());
      Iterator var3 = users.iterator();

      while(var3.hasNext()) {
         VUserInfo user = (VUserInfo)var3.next();
         userIds.add(user.id);
      }

      File[] userDirs = usersDir.listFiles();
      if (userDirs != null) {
         File[] var12 = userDirs;
         int var5 = userDirs.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File userDir = var12[var6];
            boolean isExist = false;

            try {
               Integer id = Integer.parseInt(userDir.getName());
               if (userIds.contains(id)) {
                  isExist = true;
               }
            } catch (NumberFormatException var10) {
               NumberFormatException e = var10;
               e.printStackTrace();
            }

            if (!isExist) {
               FileUtils.deleteDir(userDir);
            }
         }

      }
   }

   public static void cleanUninstalledPackages() {
      List<InstalledAppInfo> appInfos = VirtualCore.get().getInstalledApps(0);
      Set<String> installedPackageNames = new HashSet(appInfos.size());
      Iterator var2 = appInfos.iterator();

      while(var2.hasNext()) {
         InstalledAppInfo info = (InstalledAppInfo)var2.next();
         installedPackageNames.add(info.packageName);
      }

      File dataAppDirectory = VEnvironment.getDataAppDirectoryExt();
      File[] appDirs = dataAppDirectory.listFiles();
      if (appDirs != null) {
         File[] var4 = appDirs;
         int var5 = appDirs.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File appDir = var4[var6];
            String packageName = appDir.getName();
            if (!packageName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YKWwFNCM="))) && !installedPackageNames.contains(packageName)) {
               cleanAllUserPackage(VEnvironment.getDataUserDirectoryExt(), packageName);
               cleanAllUserPackage(VEnvironment.getDeDataUserDirectoryExt(), packageName);
               FileUtils.deleteDir(appDir);
            }
         }
      }

   }

   public static void cleanAllUserPackage(File usersDir, String packageName) {
      File[] userDirs = usersDir.listFiles();
      if (userDirs != null) {
         File[] var3 = userDirs;
         int var4 = userDirs.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File userDir = var3[var5];
            File userPackageDir = new File(userDir, packageName);
            if (userPackageDir.exists()) {
               FileUtils.deleteDir(userPackageDir);
            }
         }

      }
   }
}
