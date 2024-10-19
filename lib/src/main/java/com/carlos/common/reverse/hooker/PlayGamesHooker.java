package com.carlos.common.reverse.hooker;

import android.os.Bundle;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.HookReflectClass;
import com.swift.sandhook.annotation.MethodParams;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

@HookReflectClass("com.google.android.gms.games.ui.signin.SignInActivity")
public class PlayGamesHooker {
   @HookMethodBackup("onCreate")
   @MethodParams({Bundle.class})
   static Method method_m1;

   @HookMethod("onCreate")
   @MethodParams({Bundle.class})
   public static void m1(Object thiz, Bundle v1) throws Throwable {
      Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4YPWohAiZlDiggKQg+MWUwLFo=")));
      Set<String> keys = v1.keySet();
      Iterator var3 = keys.iterator();

      while(var3.hasNext()) {
         String key = (String)var3.next();
         Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC4uJ3kjSFo=")) + key);
         v1.get(key);
      }

      SandHook.callOriginByBackup(method_m1, thiz, v1);
   }
}
