package com.lody.virtual.client.core;

import android.content.Intent;
import android.os.Build;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.SpecialComponentList;

public abstract class SettingConfig {
   public abstract String getMainPackageName();

   public abstract String getExtPackageName();

   public String getBinderProviderAuthority() {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcMmMFMD9qDiQbORcYJ2wwOAVrNys5Iz0AJW4aFiRiNFk6KT4YPGgaFlo="));
   }

   public String getExtPackageHelperAuthority() {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcMmMFMD9qDiQbORcYJ2wwOAVrNys5JAgcM2AgGjFvNFE0Kj5SVg=="));
   }

   public boolean isEnableIORedirect() {
      return true;
   }

   public boolean isUseRealApkPath(String packageName) {
      return false;
   }

   public boolean isAllowCreateShortcut() {
      return true;
   }

   public boolean isUseRealDataDir(String packageName) {
      return false;
   }

   public boolean isUseRealLibDir(String packageName) {
      return false;
   }

   public boolean isEnableVirtualSdcardAndroidData() {
      return false;
   }

   public String getVirtualSdcardAndroidDataName() {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JggcPG8jGi9iHx4uLwhSVg=="));
   }

   public boolean isDisableTinker(String packageName) {
      return false;
   }

   public abstract boolean isOutsidePackage(String var1);

   public boolean isOutsideAction(String action) {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k3KAc2MW4nMDdoJCwaLD4bKmsIQQ5mIgoOIAZbQGcYNABgEVRF")).equals(action) || StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k3KAc2MW4nMDdoJCwaLD4bKm42GglmDFkOIAZbQGcYNABgEVRF")).equals(action) || StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xOBZhIgJF")).equals(action);
   }

   public boolean isUseRealPath(String packageName) {
      return isUseNativeEngine2(packageName);
   }

   public boolean isUnProtectAction(String action) {
      return SpecialComponentList.SYSTEM_BROADCAST_ACTION.contains(action);
   }

   public Intent onHandleLauncherIntent(Intent originIntent) {
      return null;
   }

   public Intent onHandleCameraIntent(Intent originIntent) {
      return null;
   }

   public boolean isHideForegroundNotification() {
      return false;
   }

   public FakeWifiStatus getFakeWifiStatus() {
      return null;
   }

   public FakeWifiStatus getFakeWifiStatus(String packageName, int userId) {
      return null;
   }

   public boolean isHostIntent(Intent intent) {
      return false;
   }

   public boolean isDisableDrawOverlays(String packageName) {
      return false;
   }

   public boolean disableSetScreenOrientation(String packageName) {
      return false;
   }

   public boolean resumeInstrumentationInMakeApplication(String packageName) {
      return Build.BRAND.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUuEWQxNAk="))) && Build.BOARD.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JSwEXg==")));
   }

   public static boolean isUseNativeEngine2(String pkg) {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojNDB9Dl0sKhcLDmUVQQZrDjA/OS5SVg==")).equals(pkg) || StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4bCGszGiNONyw/KS42PW4JMDBlAQYpIxgEJ04wHiZrAVRF")).equals(pkg);
   }

   public static class FakeWifiStatus {
      public static String DEFAULT_BSSID = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OTkhInxTNzJMVg0iMyopIHpTRDJ8CjhF"));
      public static String DEFAULT_MAC = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OgM9In8kETJPIysiPF81IHUkBTJ/MCRF"));
      public static String DEFAULT_SSID = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITw+H2c2LAlqEVRF"));

      public String getSSID() {
         return DEFAULT_SSID;
      }

      public String getBSSID() {
         return DEFAULT_BSSID;
      }

      public String getMAC() {
         return DEFAULT_MAC;
      }

      public void setSSID(String ssid) {
         DEFAULT_SSID = ssid;
      }

      public void setBSSID(String bssid) {
         DEFAULT_BSSID = bssid;
      }

      public void setDefaultMac(String mac) {
         DEFAULT_MAC = mac;
      }
   }
}
