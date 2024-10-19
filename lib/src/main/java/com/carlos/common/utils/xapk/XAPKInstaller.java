package com.carlos.common.utils.xapk;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import java.io.File;
import java.util.ArrayList;
import org.zeroturnaround.zip.NameMapper;
import org.zeroturnaround.zip.ZipException;
import org.zeroturnaround.zip.ZipUtil;

public class XAPKInstaller {
   public static void doInstallApk(Context context, String xapkFilePath) {
      if (!xapkFilePath.isEmpty()) {
         File xapkFile = new File(xapkFilePath);
         String unzipOutputDirPath = getUnzipOutputDirPath(xapkFile);
         if (!unzipOutputDirPath.isEmpty()) {
            File unzipOutputDir = new File(unzipOutputDirPath);
            ZipUtil.unpack(xapkFile, unzipOutputDir, new NameMapper() {
               public String map(String name) {
                  return name.endsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4+KGUzSFo="))) ? name : null;
               }
            });
            File[] files = unzipOutputDir.listFiles();
            int apkSize = 0;
            File[] var7 = files;
            int var8 = files.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               File file = var7[var9];
               if (file.isFile() && file.getName().endsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4+KGUzSFo=")))) {
                  ++apkSize;
               }
            }

            unzipObbToAndroidObbDir(xapkFile, new File(getMobileAndroidObbDir()));
            HVLog.i(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KAdXCg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6MWczAjJiDQU8PxhSVg==")) + apkSize);
            if (apkSize > 0) {
               doInstallApk(context, xapkFilePath, unzipOutputDir);
            }

         }
      }
   }

   private static String getUnzipOutputDirPath(File file) {
      String filePathPex = file.getParent() + File.separator;
      String unzipOutputDir = filePathPex + XAPKUtils.getFileNameNoExtension(file);
      HVLog.d("");
      boolean result = XAPKUtils.createOrExistsDir(unzipOutputDir);
      return result ? unzipOutputDir : null;
   }

   private static boolean unzipObbToAndroidObbDir(File xapkFile, File unzipOutputDir) {
      final String prefix = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JggcPG8jGi9iVx41Lz0uVg=="));
      ZipUtil.unpack(xapkFile, unzipOutputDir, new NameMapper() {
         public String map(String name) {
            return name.startsWith(prefix) ? name.substring(prefix.length()) : null;
         }
      });
      return true;
   }

   public static String getMobileAndroidObbDir() {
      String path;
      if (isSDCardEnableByEnvironment()) {
         path = Environment.getExternalStorageDirectory().getPath() + File.separator + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JggcPG8jGi9iEVRF")) + File.separator + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4MOg=="));
      } else {
         path = Environment.getDataDirectory().getParent().toString() + File.separator + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JggcPG8jGi9iEVRF")) + File.separator + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4MOg=="));
      }

      XAPKUtils.createOrExistsDir(path);
      return path;
   }

   private static boolean isSDCardEnableByEnvironment() {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwgAI2ogMCtiEVRF")) == Environment.getExternalStorageState();
   }

   private static void doInstallApk(Context context, String xapkPath, File xapkUnzipOutputDir) {
      try {
         File[] files = xapkUnzipOutputDir.listFiles();
         if (files == null || files.length < 1) {
            return;
         }

         ArrayList<String> apkFilePaths = new ArrayList();
         File[] var5 = files;
         int var6 = files.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            File file = var5[var7];
            if (file != null && file.isFile() && file.getName().endsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4+KGUzSFo=")))) {
               apkFilePaths.add(file.getAbsolutePath());
            }
         }

         Intent intent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42GillJzAqKT4iHWggMAVsJx4ZIQhSVg==")));
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KBg+KGU2GgJ9AQo0")), xapkPath);
         intent.putStringArrayListExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6MWYwIDdmHhpF")), apkFilePaths);
         intent.addFlags(268435456);
         context.startActivity(intent);
      } catch (ZipException var9) {
         ZipException e = var9;
         e.printStackTrace();
      }

   }
}
