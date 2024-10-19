package com.carlos.common.reverse.hooker;

import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookReflectClass;

@HookReflectClass("com.google.android.gms.auth.api.signin.internal.zbe")
public class GMSHooker2 {
   @HookMethod("zba")
   public static boolean providesSignIn(Object thiz) throws Throwable {
      Log.d("vatest", "providesSignIn");
      return true;
   }
}
