package com.carlos.common.reverse.hooker;

import android.content.Context;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookReflectClass;
import com.swift.sandhook.annotation.MethodParams;

@HookReflectClass("com.google.android.gms.common.GooglePlayServicesUtilLight")
public class GoogleServiceHooker {
   @MethodParams({Context.class})
   @HookMethod("isGooglePlayServicesAvailable")
   public static boolean m531a(Context context) throws Throwable {
      return true;
   }

   @MethodParams({Context.class, int.class})
   @HookMethod("isGooglePlayServicesAvailable")
   public static boolean m530a(Context context, int i) throws Throwable {
      return true;
   }

   @MethodParams({Context.class, int.class})
   @HookMethod("isPlayServicesPossiblyUpdating")
   public static boolean m529b(Context context, int i) throws Throwable {
      return true;
   }

   @MethodParams({Context.class, int.class})
   @HookMethod("isPlayStorePossiblyUpdating")
   public static boolean m528c(Context context, int i) throws Throwable {
      return true;
   }
}
