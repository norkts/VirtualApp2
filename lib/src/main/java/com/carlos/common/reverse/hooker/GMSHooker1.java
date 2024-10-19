package com.carlos.common.reverse.hooker;

import android.content.Context;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookReflectClass;
import com.swift.sandhook.annotation.MethodParams;

@HookReflectClass("com.google.android.gms.common.GooglePlayServicesUtilLight")
public class GMSHooker1 {
   @HookMethod("isGooglePlayServicesAvailable")
   @MethodParams({Context.class})
   public static boolean isGooglePlayServicesAvailable(Context v1) throws Throwable {
      Log.d("vatest", "isGooglePlayServicesAvailable1");
      return true;
   }

   @HookMethod("isGooglePlayServicesAvailable")
   @MethodParams({Context.class, int.class})
   public static boolean isGooglePlayServicesAvailable2(Context v1, int v2) throws Throwable {
      Log.d("vatest", "isGooglePlayServicesAvailable2");
      return true;
   }

   @HookMethod("isPlayServicesPossiblyUpdating")
   @MethodParams({Context.class, int.class})
   public static boolean isGooglePlayServicesUid(Context v1, int v2) throws Throwable {
      Log.d("vatest", "isPlayServicesPossiblyUpdating");
      return true;
   }

   @HookMethod("isPlayStorePossiblyUpdating")
   @MethodParams({Context.class, int.class})
   public static boolean isPlayStorePossiblyUpdating(Context v1, int v2) throws Throwable {
      Log.d("vatest", "isPlayStorePossiblyUpdating");
      return true;
   }
}
