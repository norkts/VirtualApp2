package com.lody.virtual.os;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.Base64;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.EncodeUtils;
import com.lody.virtual.helper.utils.FileUtils;
import java.io.File;
import java.io.IOException;

public class VEnvironment {
   private static final String TAG = VEnvironment.class.getSimpleName();
   private static final File ROOT;
   private static final File DATA_DIRECTORY;
   private static final File DATA_USER_DIRECTORY;
   private static final File DATA_USER_DE_DIRECTORY;
   private static final File DATA_APP_DIRECTORY;
   private static final File DATA_APP_SYSTEM_DIRECTORY;
   private static final File FRAMEWORK_DIRECTORY;
   private static final File ROOT_EXT;
   private static final File DATA_DIRECTORY_EXT;
   private static final File DATA_APP_DIRECTORY_EXT;
   private static final File DATA_USER_DIRECTORY_EXT;
   private static final File DATA_USER_DE_DIRECTORY_EXT;
   private static final File FRAMEWORK_DIRECTORY_EXT;
   public static int OUTSIDE_APP_UID = 9000;
   public static int UNKNOWN_APP_UID = 9001;
   public static int SYSTEM_UID = 1000;

   public static File buildPath(File base, String... segments) {
      File cur = base;
      String[] var3 = segments;
      int var4 = segments.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String segment = var3[var5];
         if (cur == null) {
            cur = new File(segment);
         } else {
            cur = new File(cur, segment);
         }
      }

      return cur;
   }

   public static void systemReady() {
      FileUtils.ensureDirCreate(DATA_DIRECTORY);
      FileUtils.ensureDirCreate(DATA_APP_DIRECTORY);
      FileUtils.ensureDirCreate(DATA_APP_SYSTEM_DIRECTORY);
      FileUtils.ensureDirCreate(DATA_USER_DIRECTORY);
      FileUtils.ensureDirCreate(DATA_USER_DE_DIRECTORY);
      FileUtils.ensureDirCreate(FRAMEWORK_DIRECTORY);
      FileUtils.chmod(ROOT.getAbsolutePath(), 493);
      FileUtils.chmod(DATA_DIRECTORY.getAbsolutePath(), 493);
      FileUtils.chmod(DATA_APP_DIRECTORY.getAbsolutePath(), 493);
   }

   private static Context getContext() {
      return VirtualCore.get().getContext();
   }

   public static void chmodPackageDictionary(File packageFile) {
      try {
         if (VERSION.SDK_INT >= 21) {
            if (FileUtils.isSymlink(packageFile)) {
               return;
            }

            FileUtils.chmod(packageFile.getParentFile().getAbsolutePath(), 493);
            FileUtils.chmod(packageFile.getAbsolutePath(), 493);
         }
      } catch (Exception var2) {
         Exception e = var2;
         e.printStackTrace();
      }

   }

   public static File getRoot() {
      return ROOT;
   }

   public static File getRootExt() {
      return ROOT_EXT;
   }

   public static File getDataUserPackageDirectory(int userId, String packageName) {
      return buildPath(getDataUserDirectory(userId), packageName);
   }

   public static File getDataUserPackageDirectoryExt(int userId, String packageName) {
      return buildPath(getDataUserDirectoryExt(userId), packageName);
   }

   public static File getDeDataUserPackageDirectory(int userId, String packageName) {
      return buildPath(getDataUserDeDirectory(userId), packageName);
   }

   public static File getDeDataUserPackageDirectoryRoot(int userId) {
      return buildPath(getDataUserDeDirectory(userId));
   }

   public static File getDeDataUserPackageDirectoryExt(int userId, String packageName) {
      return buildPath(getDeDataUserDirectoryExt(userId), packageName);
   }

   public static File getDeDataUserPackageDirectoryExtRoot(int userId) {
      return buildPath(getDeDataUserDirectoryExt(userId));
   }

   public static File getDataAppLibDirectoryExt(String packageName) {
      return buildPath(getDataAppPackageDirectoryExt(packageName), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgYOg==")));
   }

   public static File getPackageFile(String packageName) {
      return new File(getDataAppPackageDirectory(packageName), EncodeUtils.decodeBase64(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IAgIW2kmTV5MDho5IC4pJQ=="))));
   }

   public static String getPackageFileStub(String packageName) {
      return BuildCompat.isOreo() ? String.format(EncodeUtils.decodeBase64(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("O14MDGUFMBdqATxOJRUuI2cwLFNuNQIQIi1XAk82HitpIygUIys2WWAwFlo="))), packageName, Base64.encodeToString(packageName.getBytes(), 10)) : String.format(EncodeUtils.decodeBase64(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("O14MDGUFMBdqATxOJRUuI2cwLFNuNQIVJhciWGAIID1jNS8+Kj4YG2wkElo="))), packageName);
   }

   public static String getSplitFileName(String splitName) {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki06DmUaMB8=")) + splitName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4+KGUzSFo="));
   }

   public static File getSplitPackageFile(String packageName, String splitName) {
      return new File(getDataAppPackageDirectory(packageName), getSplitFileName(splitName));
   }

   public static File getPackageFileExt(String packageName) {
      return new File(getDataAppPackageDirectoryExt(packageName), EncodeUtils.decodeBase64(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IAgIW2kmTV5MDho5IC4pJQ=="))));
   }

   public static File getSplitPackageFileExt(String packageName, String splitName) {
      return new File(getDataAppPackageDirectoryExt(packageName), getSplitFileName(splitName));
   }

   public static File getDataAppDirectory() {
      return DATA_APP_DIRECTORY;
   }

   public static File getDataAppDirectoryExt() {
      return DATA_APP_DIRECTORY_EXT;
   }

   public static File getDingConfigFile() {
      return new File(DATA_APP_SYSTEM_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgYCGg3EilgJFk+KQc5DmwjMC8=")));
   }

   public static File getUidListFile() {
      return new File(DATA_APP_SYSTEM_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgYPHoVHi9hJw02KQcYMQ==")));
   }

   public static File getBakUidListFile() {
      return new File(DATA_APP_SYSTEM_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgYPHoVHi9hJw02KQcYMX8VRTdqJ1RF")));
   }

   public static File getAccountConfigFile() {
      return new File(DATA_APP_SYSTEM_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmV10oKQgqLn8VLCZqAVRF")));
   }

   public static File getSyncDirectory() {
      return buildPath(DATA_APP_SYSTEM_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YCGszSFo=")));
   }

   public static File getAccountVisibilityConfigFile() {
      return new File(DATA_APP_SYSTEM_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmV10uKQgqMW4VLCRqDiw0PBgEI2EjESlvDh4i")));
   }

   public static File getVirtualLocationFile() {
      return new File(DATA_APP_SYSTEM_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV10oKi0pDmwjMC8=")));
   }

   public static File getDeviceInfoFile() {
      return new File(DATA_APP_SYSTEM_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguLmUVLCtODig1Kj0+MWkJMC9lNx5F")));
   }

   public static File getPackageListFile() {
      return new File(DATA_APP_SYSTEM_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iASs2KQcYMQ==")));
   }

   public static File getVSConfigFile() {
      return new File(DATA_APP_SYSTEM_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT02KXojAiZjAVRF")));
   }

   public static File getJobConfigFile() {
      return new File(DATA_APP_SYSTEM_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4AOnoVHi9hJw02KQcYMQ==")));
   }

   public static File getOatDirectory(String packageName) {
      return buildPath(getDataAppPackageDirectory(packageName), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4+LA==")));
   }

   public static File getOatDirectoryExt(String packageName) {
      return buildPath(getDataAppPackageDirectoryExt(packageName), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4+LA==")));
   }

   public static File getOatFile(String packageName, String instructionSet) {
      return buildPath(getOatDirectory(packageName), instructionSet, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+KWhSBiViHjAa")));
   }

   public static File getOatFileExt(String packageName, String instructionSet) {
      return buildPath(getOatDirectoryExt(packageName), instructionSet, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+KWhSBiViHjAa")));
   }

   public static File getDataAppPackageDirectory(String packageName) {
      return buildPath(DATA_APP_DIRECTORY, packageName);
   }

   public static File getDataAppPackageDirectoryExt(String packageName) {
      return buildPath(DATA_APP_DIRECTORY_EXT, packageName);
   }

   public static File getDataAppLibDirectory(String packageName) {
      return buildPath(getDataAppPackageDirectory(packageName), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgYOg==")));
   }

   public static File getUserAppLibDirectory(int userId, String packageName) {
      return new File(getDataUserPackageDirectory(userId, packageName), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgYOg==")));
   }

   public static File getUserAppLibDirectoryExt(int userId, String packageName) {
      return new File(getDataUserPackageDirectoryExt(userId, packageName), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgYOg==")));
   }

   public static File getPackageCacheFile(String packageName) {
      return new File(getDataAppPackageDirectory(packageName), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iClkzKj0cVg==")));
   }

   public static File getSignatureFile(String packageName) {
      return new File(getDataAppPackageDirectory(packageName), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YPWojJAZmASw/Oj0cDmwjSFo=")));
   }

   public static File getFrameworkDirectory(String name) {
      return buildPath(FRAMEWORK_DIRECTORY, name);
   }

   public static File getOptimizedFrameworkFile(String name) {
      return new File(getFrameworkDirectory(name), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4EP28wLCthIFkwKAgAVg==")));
   }

   public static File getFrameworkFile(String name) {
      return new File(getFrameworkDirectory(name), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfLG8jJClmHjAwOj0IOWoVSFo=")));
   }

   public static File getDataUserDirectory() {
      return DATA_USER_DIRECTORY;
   }

   public static File getDataUserDirectoryExt() {
      return DATA_USER_DIRECTORY_EXT;
   }

   public static File getDataUserDeDirectory() {
      return DATA_USER_DE_DIRECTORY;
   }

   public static File getDataUserDirectory(int userId) {
      return buildPath(DATA_USER_DIRECTORY, String.valueOf(userId));
   }

   public static File getDataUserDeDirectory(int userId) {
      return buildPath(DATA_USER_DE_DIRECTORY, String.valueOf(userId));
   }

   public static File getDataUserDirectoryExt(int userId) {
      return buildPath(DATA_USER_DIRECTORY_EXT, String.valueOf(userId));
   }

   public static File getDeDataUserDirectoryExt() {
      return DATA_USER_DE_DIRECTORY_EXT;
   }

   public static File getDeDataUserDirectoryExt(int userId) {
      return buildPath(DATA_USER_DE_DIRECTORY_EXT, String.valueOf(userId));
   }

   public static File getDataSystemDirectory(int userId) {
      return buildPath(getDataUserDirectory(userId), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YKWwFNCM=")));
   }

   public static File getDataSystemDirectoryExt(int userId) {
      return buildPath(getDataUserDirectoryExt(userId), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YKWwFNCM=")));
   }

   public static File getWifiMacFile(int userId, boolean isExt) {
      return isExt ? new File(getDataSystemDirectoryExt(userId), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4YPmUbEjd9IiAwKBguPWoKAlo="))) : new File(getDataSystemDirectory(userId), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4YPmUbEjd9IiAwKBguPWoKAlo=")));
   }

   public static File getDataDirectory() {
      return DATA_DIRECTORY;
   }

   public static File getPackageInstallerStageDir() {
      return buildPath(DATA_APP_SYSTEM_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz02M28wLC9gJFlAKBccKA==")));
   }

   public static File getNativeCacheDir(boolean isExt) {
      return buildPath(isExt ? ROOT_EXT : ROOT, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4cP2wFAj5iAVRF")));
   }

   public static File getFrameworkDirectory() {
      return FRAMEWORK_DIRECTORY;
   }

   public static File getFrameworkDirectoryExt() {
      return FRAMEWORK_DIRECTORY_EXT;
   }

   public static File getSystemSettingsFile(int userId) {
      return new File(getDataSystemDirectory(userId), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGwFAiZiJys2KQcYMQ==")));
   }

   public static boolean enableMediaRedirect() {
      return false;
   }

   static {
      OUTSIDE_APP_UID = VUserHandle.getUid(VUserHandle.realUserId(), OUTSIDE_APP_UID);
      UNKNOWN_APP_UID = VUserHandle.getUid(VUserHandle.realUserId(), UNKNOWN_APP_UID);
      SYSTEM_UID = VUserHandle.getUid(VUserHandle.realUserId(), SYSTEM_UID);

      File hostUserDir;
      try {
         hostUserDir = (new File(getContext().getApplicationInfo().dataDir)).getCanonicalFile().getParentFile();
      } catch (IOException var3) {
         hostUserDir = (new File(getContext().getApplicationInfo().dataDir)).getParentFile();
      }

      File host = new File(hostUserDir, StubManifest.PACKAGE_NAME);
      ROOT = buildPath(host, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgEVRF")));
      FRAMEWORK_DIRECTORY = buildPath(ROOT, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT0MP2oVND1gJywx")));
      DATA_DIRECTORY = buildPath(ROOT, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRg+LGsVSFo=")));
      DATA_USER_DIRECTORY = buildPath(DATA_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28jSFo=")));
      DATA_USER_DE_DIRECTORY = buildPath(DATA_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28mGixiAVRF")));
      DATA_APP_DIRECTORY = buildPath(DATA_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KA==")));
      DATA_APP_SYSTEM_DIRECTORY = buildPath(DATA_APP_DIRECTORY, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YKWwFNCM=")));
      File hostExt = new File(hostUserDir, StubManifest.EXT_PACKAGE_NAME);
      ROOT_EXT = buildPath(hostExt, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgEVRF")));
      DATA_DIRECTORY_EXT = buildPath(ROOT_EXT, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRg+LGsVSFo=")));
      DATA_USER_DIRECTORY_EXT = buildPath(DATA_DIRECTORY_EXT, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28jSFo=")));
      DATA_APP_DIRECTORY_EXT = buildPath(DATA_DIRECTORY_EXT, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KA==")));
      DATA_USER_DE_DIRECTORY_EXT = buildPath(DATA_DIRECTORY_EXT, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28mGixiAVRF")));
      FRAMEWORK_DIRECTORY_EXT = buildPath(ROOT_EXT, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT0MP2oVND1gJywx")));
   }
}
