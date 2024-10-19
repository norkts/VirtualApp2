package com.carlos.common.reverse.hooker;

import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.MethodParams;
import java.lang.reflect.Method;

@HookClass(Method.class)
public class Hook_Clazz {
   @HookMethodBackup("invoke")
   @MethodParams({Object.class, Object.class})
   static Method method_m1;

   @HookMethod("invoke")
   @MethodParams({Object.class, Object.class})
   public static Object m1(Method method, Object v1, Object[] v2) throws Throwable {
      Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBgAD2U2GhNgHiAiLTkmMW8aHiVqJyhF")));
      return SandHook.callOriginByBackup(method_m1, method, v1, v2);
   }
}
