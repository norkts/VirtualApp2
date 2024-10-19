package com.kook.deviceinfo.persistence;

import android.app.Application;
import android.os.Environment;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import java.io.File;
import mirror.android.app.ActivityThread;

public class VEnvironment {
   public static String SYSTEM_DIRECTORY = "/system/kook/";
   public static String SYSTEM_DIRECTORY_DOWLOAD_MOCK;
   public static String SYSTEM_MOCK_STORAGE;
   private static VEnvironment mVEnvironment;

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

   public String getDownloadDirectoryPath() {
      if (isExternalStorageAvailable()) {
         File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
         if (!externalStoragePublicDirectory.exists()) {
            externalStoragePublicDirectory.mkdirs();
         }

         return externalStoragePublicDirectory.getAbsolutePath();
      } else {
         return "";
      }
   }

   public String getPersisteceDataPath() {
      String packageName = (String)ActivityThread.currentPackageName.call();
      File host = new File(SYSTEM_DIRECTORY);
      if (host.exists() && host.canRead() && host.canWrite()) {
         File hostdir = new File(SYSTEM_DIRECTORY + packageName + File.separator);
         if (!hostdir.exists()) {
            hostdir.mkdirs();
         }

         return "/data/data/" + packageName + "/";
      } else {
         return "/data/data/" + packageName + "/";
      }
   }

   public String getPersisteceExternalStoragePath() {
      Application application = (Application)ActivityThread.currentApplication.call();
      if (ActivityCompat.checkSelfPermission(application, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
         return Environment.getExternalStorageDirectory() + File.separator + "mockdata/";
      } else {
         Toast.makeText(application, "请授予存储权限", 1).show();
         return null;
      }
   }

   public static VEnvironment get() {
      return mVEnvironment;
   }

   public File getControlFile() {
      return new File(this.getPersisteceDataPath(), "control-config.ini");
   }

   public File getDeviceInfoFile() {
      return new File(this.getPersisteceDataPath(), "device-config.ini");
   }

   public File getLibraryConfigFile() {
      return new File(this.getPersisteceDataPath(), "library-config.ini");
   }

   public File getSystemSettingConfigFile() {
      return new File(this.getPersisteceDataPath(), "setting-config.ini");
   }

   public File getVirtualLocationFile() {
      return new File(this.getPersisteceDataPath(), "virtual-loc.ini");
   }

   public File getTabMenuManagerFile() {
      return new File(this.getPersisteceDataPath(), "tab-menu-manager.ini");
   }

   public static boolean isExternalStorageAvailable() {
      String state = Environment.getExternalStorageState();
      return "mounted".equals(state);
   }

   static {
      SYSTEM_DIRECTORY_DOWLOAD_MOCK = "/data/data/" + (String)ActivityThread.currentPackageName.call() + "/download/";
      SYSTEM_MOCK_STORAGE = "/data/data/" + (String)ActivityThread.currentPackageName.call() + "/mock_config.ini";
      mVEnvironment = new VEnvironment();
   }
}
