package de.robv.android.xposed;

import de.robv.android.xposed.services.BaseService;
import de.robv.android.xposed.services.DirectAccessService;

public final class SELinuxHelper {
   private static boolean sIsSELinuxEnabled = false;
   private static BaseService sServiceAppDataFile = new DirectAccessService();

   private SELinuxHelper() {
   }

   public static boolean isSELinuxEnabled() {
      return sIsSELinuxEnabled;
   }

   public static boolean isSELinuxEnforced() {
      return sIsSELinuxEnabled;
   }

   public static String getContext() {
      return null;
   }

   public static BaseService getAppDataFileService() {
      return (BaseService)(sServiceAppDataFile != null ? sServiceAppDataFile : new DirectAccessService());
   }
}
