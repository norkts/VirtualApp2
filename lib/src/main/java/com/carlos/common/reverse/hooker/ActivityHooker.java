package com.carlos.common.reverse.hooker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.MethodParams;
import com.swift.sandhook.annotation.ThisObject;
import com.swift.sandhook.wrapper.HookWrapper;
import java.lang.reflect.Method;

@HookClass(Activity.class)
public class ActivityHooker {
   @HookMethodBackup("onCreate")
   @MethodParams({Bundle.class})
   static Method onCreateBackup;
   @HookMethodBackup("onPause")
   static HookWrapper.HookEntity onPauseBackup;

   @HookMethod("onCreate")
   @MethodParams({Bundle.class})
   public static void onCreate(Activity thiz, Bundle bundle) throws Throwable {
      Log.e("Hooker", "hooked onCreate success " + thiz);
      SandHook.callOriginByBackup(onCreateBackup, thiz, bundle);
   }

   @HookMethod("onPause")
   public static void onPause(@ThisObject Activity thiz) throws Throwable {
      Log.e("Hooker", "hooked onPause success " + thiz);
      onPauseBackup.callOrigin(thiz);
   }
}
