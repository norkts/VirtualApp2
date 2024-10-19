package com.carlos.common.reverse.hooker;

import android.app.Dialog;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import java.lang.reflect.Method;

@HookClass(Dialog.class)
public class DialogHooker {
   @HookMethodBackup("show")
   static Method showBackup;

   @HookMethod("show")
   public static void show(Dialog thiz) throws Throwable {
      if (VirtualCore.get().isMainProcess()) {
         SandHook.callOriginByBackup(showBackup, thiz);
      } else {
         String packageName = VClient.get().getCurrentPackage();
         String clzName = thiz.getClass().getName();
         Log.e("DialogHooker", packageName + " Dialog show " + thiz);
         thiz.cancel();
      }
   }
}
