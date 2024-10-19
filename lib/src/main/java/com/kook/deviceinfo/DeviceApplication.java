package com.kook.deviceinfo;

import android.annotation.SuppressLint;
import android.app.Application;
import com.kook.deviceinfo.util.GeneralUtils;

public class DeviceApplication {
   @SuppressLint({"StaticFieldLeak"})
   private static Application sApp;
   public static String deviceId = "";
   public static boolean mRegisterTag = false;
   private Application mApplication;
   @SuppressLint({"StaticFieldLeak"})
   private static DeviceApplication gCore = new DeviceApplication();

   public static DeviceApplication get() {
      return gCore;
   }

   public void startup(Application application) {
      sApp = application;
      GeneralUtils.getGaid();
      initBoadcast();
   }

   public static Application getApp() {
      if (sApp != null) {
         return sApp;
      } else if (sApp == null) {
         throw new NullPointerException("reflect failed.");
      } else {
         return sApp;
      }
   }

   public static void initBoadcast() {
      mRegisterTag = true;
   }

   public static void removeBoadcast() {
      if (mRegisterTag) {
         mRegisterTag = false;
      }

   }
}
