package com.lody.virtual.helper.compat;

import mirror.android.os.StrictMode;

public class StrictModeCompat {
   public static int DETECT_VM_FILE_URI_EXPOSURE;
   public static int PENALTY_DEATH_ON_FILE_URI_EXPOSURE;

   public static boolean disableDeathOnFileUriExposure() {
      try {
         StrictMode.disableDeathOnFileUriExposure.call();
         return true;
      } catch (Throwable var3) {
         try {
            int sVmPolicyMask = StrictMode.sVmPolicyMask.get();
            sVmPolicyMask &= ~(DETECT_VM_FILE_URI_EXPOSURE | PENALTY_DEATH_ON_FILE_URI_EXPOSURE);
            StrictMode.sVmPolicyMask.set(sVmPolicyMask);
            return true;
         } catch (Throwable var2) {
            Throwable e2 = var2;
            e2.printStackTrace();
            return false;
         }
      }
   }

   static {
      DETECT_VM_FILE_URI_EXPOSURE = StrictMode.DETECT_VM_FILE_URI_EXPOSURE == null ? 8192 : StrictMode.DETECT_VM_FILE_URI_EXPOSURE.get();
      PENALTY_DEATH_ON_FILE_URI_EXPOSURE = StrictMode.PENALTY_DEATH_ON_FILE_URI_EXPOSURE == null ? 67108864 : StrictMode.PENALTY_DEATH_ON_FILE_URI_EXPOSURE.get();
   }
}
