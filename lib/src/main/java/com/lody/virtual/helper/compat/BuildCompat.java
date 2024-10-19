package com.lody.virtual.helper.compat;

import android.os.Build;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;

public class BuildCompat {
   private static ROMType sRomType;

   public static int getPreviewSDKInt() {
      if (VERSION.SDK_INT >= 23) {
         try {
            return VERSION.PREVIEW_SDK_INT;
         } catch (Throwable var1) {
         }
      }

      return 0;
   }

   public static boolean isOreo() {
      return VERSION.SDK_INT > 25 || VERSION.SDK_INT == 25 && getPreviewSDKInt() > 0;
   }

   public static boolean isPie() {
      return VERSION.SDK_INT > 27 || VERSION.SDK_INT == 27 && getPreviewSDKInt() > 0;
   }

   public static boolean isQ() {
      return VERSION.SDK_INT > 28 || VERSION.SDK_INT == 28 && getPreviewSDKInt() > 0;
   }

   public static boolean isR() {
      return VERSION.SDK_INT > 29 || VERSION.SDK_INT == 29 && getPreviewSDKInt() > 0;
   }

   public static boolean isS() {
      return VERSION.SDK_INT > 30 || VERSION.SDK_INT == 30 && getPreviewSDKInt() > 0;
   }

   public static boolean isT() {
      return VERSION.SDK_INT > 31 || VERSION.SDK_INT == 31 && getPreviewSDKInt() > 0;
   }

   public static boolean isSL() {
      return VERSION.SDK_INT > 31 || VERSION.SDK_INT == 31 && getPreviewSDKInt() > 0;
   }

   public static boolean isTiramisu() {
      return VERSION.SDK_INT > 32 || VERSION.SDK_INT == 32 && getPreviewSDKInt() > 0;
   }

   public static boolean isUpsideDownCake() {
      return VERSION.SDK_INT > 33 || VERSION.SDK_INT == 33 && getPreviewSDKInt() > 0;
   }

   public static boolean isSamsung() {
      return "samsung".equalsIgnoreCase(Build.BRAND) || "samsung".equalsIgnoreCase(Build.MANUFACTURER);
   }

   public static boolean isEMUI() {
      if (Build.DISPLAY.toUpperCase().startsWith("EMUI")) {
         return true;
      } else {
         String property = SystemPropertiesCompat.get("ro.build.version.emui");
         return property != null && property.contains("EmotionUI");
      }
   }

   public static boolean isMIUI() {
      return SystemPropertiesCompat.getInt("ro.miui.ui.version.code", 0) > 0;
   }

   public static boolean isFlyme() {
      return Build.DISPLAY.toLowerCase().contains("flyme");
   }

   public static boolean isColorOS() {
      return SystemPropertiesCompat.isExist("ro.build.version.opporom") || SystemPropertiesCompat.isExist("ro.rom.different.version");
   }

   public static boolean is360UI() {
      String property = SystemPropertiesCompat.get("ro.build.uiversion");
      return property != null && property.toUpperCase().contains("360UI");
   }

   public static boolean isLetv() {
      return Build.MANUFACTURER.equalsIgnoreCase("Letv");
   }

   public static boolean isVivo() {
      return SystemPropertiesCompat.isExist("ro.vivo.os.build.display.id");
   }

   public static ROMType getROMType() {
      if (sRomType == null) {
         if (isEMUI()) {
            sRomType = BuildCompat.ROMType.EMUI;
         } else if (isMIUI()) {
            sRomType = BuildCompat.ROMType.MIUI;
         } else if (isFlyme()) {
            sRomType = BuildCompat.ROMType.FLYME;
         } else if (isColorOS()) {
            sRomType = BuildCompat.ROMType.COLOR_OS;
         } else if (is360UI()) {
            sRomType = BuildCompat.ROMType._360;
         } else if (isLetv()) {
            sRomType = BuildCompat.ROMType.LETV;
         } else if (isVivo()) {
            sRomType = BuildCompat.ROMType.VIVO;
         } else if (isSamsung()) {
            sRomType = BuildCompat.ROMType.SAMSUNG;
         } else {
            sRomType = BuildCompat.ROMType.OTHER;
         }
      }

      return sRomType;
   }

   public static enum ROMType {
      EMUI,
      MIUI,
      FLYME,
      COLOR_OS,
      LETV,
      VIVO,
      _360,
      SAMSUNG,
      OTHER;
   }
}
