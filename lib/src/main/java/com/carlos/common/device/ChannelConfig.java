package com.carlos.common.device;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import com.carlos.common.utils.IniFile;
import com.carlos.libcommon.StringFog;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.helper.InstalledInfoCache;

public class ChannelConfig {
   public static String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji5fP2ojBitgHCg1Kj0+MWkFSFo="));
   DeviceInfo deviceInfo;
   private static ChannelConfig channelConfig;
   IniFile mIniFile;

   private ChannelConfig() {
   }

   public static ChannelConfig getInstance() {
      Class var0 = ChannelConfig.class;
      synchronized(ChannelConfig.class) {
         if (channelConfig == null) {
            channelConfig = new ChannelConfig();
         }
      }

      return channelConfig;
   }

   public IniFile getIniFile() {
      if (this.mIniFile == null) {
         this.mIniFile = IniFile.getInstance();
      }

      return this.mIniFile;
   }

   public boolean needActivation(Context context) {
      return false;
   }

   public void getChannelConfig() {
   }

   public void getHidePkg() {
   }

   public void getDevicesAction(Context context) {
   }

   public ApplicationInfo getApplicationInfo(String pkgName, int userId) {
      return VPackageManager.get().getApplicationInfo(pkgName, 0, userId);
   }

   public String getApplicationName(Context context, String pkgName, int userId) {
      ApplicationInfo appInfo = this.getApplicationInfo(pkgName, userId);
      if (appInfo == null) {
         return null;
      } else {
         PackageManager pm = context.getPackageManager();

         try {
            InstalledInfoCache.CacheItem appInfoCache = InstalledInfoCache.get(appInfo.packageName);
            String name;
            if (appInfoCache == null) {
               name = appInfo.loadLabel(pm).toString();
            } else {
               name = appInfoCache.getLabel();
            }

            return name;
         } catch (Throwable var8) {
            Throwable e = var8;
            e.printStackTrace();
            return null;
         }
      }
   }

   public void dayActivation(Context context, String metaDataFromApp) {
   }
}
