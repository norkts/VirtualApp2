package com.lody.virtual.sandxposed;

import android.os.Build;
import com.lody.virtual.StringFog;

public class DeviceManufactureCompat {
   public static boolean isMIUI() {
      String manufacturer = Build.MANUFACTURER;
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KBgYP2ozEi8=")).equalsIgnoreCase(manufacturer);
   }

   public static boolean isEMUI() {
      String manufacturer = Build.MANUFACTURER;
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUuEWQxNAk=")).equalsIgnoreCase(manufacturer);
   }

   public static boolean isOPPO() {
      String manufacturer = Build.MANUFACTURER;
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oys6AmIzSFo=")).equalsIgnoreCase(manufacturer);
   }

   public static boolean isVIVO() {
      String manufacturer = Build.MANUFACTURER;
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YLmozSFo=")).equalsIgnoreCase(manufacturer);
   }
}
