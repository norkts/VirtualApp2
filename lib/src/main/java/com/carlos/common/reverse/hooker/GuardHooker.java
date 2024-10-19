package com.carlos.common.reverse.hooker;

import android.os.Message;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.HookReflectClass;
import com.swift.sandhook.annotation.MethodParams;
import java.lang.reflect.Method;

@HookReflectClass("com.inca.security.IiiIiiiiIi")
public class GuardHooker {
   @HookMethodBackup("handleMessage")
   @MethodParams({Message.class})
   static Method method_m1;

   @HookMethod("handleMessage")
   @MethodParams({Message.class})
   public static void m1(Message v1) throws Throwable {
      Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JS0uP28jMApgJB4xKAgtOm4gBgZoATAZJS4+D2IINCpsNzAuLj0cVg==")));
   }
}
