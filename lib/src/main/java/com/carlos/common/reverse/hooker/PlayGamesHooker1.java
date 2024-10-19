package com.carlos.common.reverse.hooker;

import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.HookReflectClass;
import com.swift.sandhook.annotation.MethodParams;
import java.lang.reflect.Method;
import mirror.android.app.Activity;

@HookReflectClass("defpackage.fos")
public class PlayGamesHooker1 {
   @HookMethodBackup("g")
   @MethodParams({Activity.class})
   static Method method_m1;

   @HookMethod("g")
   public static Activity getCallingActivity(Object thiz, Activity v1) throws Throwable {
      Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzJCRgHgY2KCsiP2UzLD5qDiw0")));
      return (Activity)SandHook.callOriginByBackup(method_m1, thiz, v1);
   }
}
