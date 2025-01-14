package com.lody.virtual.server.pm;

import android.net.Uri;
import android.os.Parcel;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.PersistenceLayer;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.VAppInstallerParams;
import com.lody.virtual.remote.VAppInstallerResult;
import com.lody.virtual.server.pm.legacy.PackageSettingV1;
import com.lody.virtual.server.pm.legacy.PackageSettingV5;
import com.lody.virtual.server.pm.parser.VPackage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

class PackagePersistenceLayer extends PersistenceLayer {
   private static final char[] MAGIC = new char[]{'v', 'p', 'k', 'g'};
   private static final int CURRENT_VERSION = 6;
   public boolean changed = false;
   private VAppManagerService mService;

   PackagePersistenceLayer(VAppManagerService service) {
      super(VEnvironment.getPackageListFile());
      this.mService = service;
   }

   public int getCurrentVersion() {
      return 6;
   }

   public void writeMagic(Parcel p) {
      p.writeCharArray(MAGIC);
   }

   public boolean verifyMagic(Parcel p) {
      char[] magic = p.createCharArray();
      return Arrays.equals(magic, MAGIC);
   }

   public void writePersistenceData(Parcel p) {
      synchronized(PackageCacheManager.PACKAGE_CACHE) {
         p.writeInt(PackageCacheManager.PACKAGE_CACHE.size());
         Iterator var3 = PackageCacheManager.PACKAGE_CACHE.values().iterator();

         while(var3.hasNext()) {
            VPackage pkg = (VPackage)var3.next();
            PackageSetting ps = (PackageSetting)pkg.mExtras;
            ps.writeToParcel(p, 0);
         }

      }
   }

   public void readPersistenceData(Parcel p, int version) {
      int count;
      if (version == 6) {
         count = p.readInt();

         while(count-- > 0) {
            PackageSetting setting = new PackageSetting(version, p);
            if (!this.mService.loadPackage(setting)) {
               this.changed = true;
            }
         }

      } else {
         if (version <= 5) {
            count = p.readInt();

            ArrayList list = new ArrayList(count);
            PackageSettingV5 settingV5;
            PackageSettingV5 uri;
            for(; count-- > 0;) {
               if (version < 5) {
                  this.changed = true;
                  PackageSettingV1 v1 = new PackageSettingV1();
                  v1.readFromParcel(p, version);
                  uri = new PackageSettingV5();
                  uri.packageName = v1.packageName;
                  uri.appMode = v1.notCopyApk ? 1 : 0;
                  uri.appId = v1.appId;
                  uri.flag = v1.flag;
                  uri.userState = v1.userState;
                  uri.firstInstallTime = System.currentTimeMillis();
                  uri.lastUpdateTime = uri.firstInstallTime;
                  settingV5 = uri;
               } else {
                  settingV5 = new PackageSettingV5(version, p);
               }
               list.add(settingV5);
            }

            Iterator var12 = list.iterator();

            while(var12.hasNext()) {
               PackageSettingV5 settingV5_1 = (PackageSettingV5)var12.next();
               Uri uri_1;
               if (settingV5_1.appMode == 1) {
                  uri_1 = Uri.parse("package:" + settingV5_1.packageName);
               } else {
                  File apkFile = VEnvironment.getPackageFile(settingV5_1.packageName);
                  if (!apkFile.exists()) {
                     apkFile = VEnvironment.getPackageFileExt(settingV5_1.packageName);
                  }

                  if (apkFile.exists()) {
                     uri_1 = Uri.fromFile(apkFile);
                  } else {
                     uri_1 = Uri.parse("package:" + settingV5_1.packageName);
                  }
               }

               if (uri_1 != null) {
                  VAppInstallerParams params = new VAppInstallerParams(26, 1);
                  VAppInstallerResult installerResult = VirtualCore.get().installPackage(uri_1, params);
                  if (installerResult.status == 0) {
                     PackageSetting ps = PackageCacheManager.getSetting(settingV5_1.packageName);
                     ps.userState = settingV5_1.userState;
                  } else {
                     VLog.e("PackagePersistenceLayer", "update package info failed : install %s failed", settingV5_1.packageName);
                  }
               }
            }

            this.save();
            this.changed = true;
         } else {
            this.onPersistenceFileDamage();
         }

      }
   }

   public void onPersistenceFileDamage() {
   }
}
