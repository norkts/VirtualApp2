package com.lody.virtual.sandxposed;

import android.os.Build;
import com.lody.virtual.StringFog;

public class DeviceManufactureCompat {
   public static boolean isMIUI() {
      String manufacturer = Build.MANUFACTURER;
      return "xiaomi".equalsIgnoreCase(manufacturer);
   }

   public static boolean isEMUI() {
      String manufacturer = Build.MANUFACTURER;
      return "HUAWEI".equalsIgnoreCase(manufacturer);
   }

   public static boolean isOPPO() {
      String manufacturer = Build.MANUFACTURER;
      return "OPPO".equalsIgnoreCase(manufacturer);
   }

   public static boolean isVIVO() {
      String manufacturer = Build.MANUFACTURER;
      return "vivo".equalsIgnoreCase(manufacturer);
   }
}
