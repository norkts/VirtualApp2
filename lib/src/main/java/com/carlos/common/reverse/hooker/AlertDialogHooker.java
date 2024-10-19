package com.carlos.common.reverse.hooker;

import android.app.AlertDialog;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.MethodParams;
import java.lang.reflect.Method;

@HookClass(AlertDialog.class)
public class AlertDialogHooker {
   @HookMethodBackup("setMessage")
   static Method setMessageBackup;

   @HookMethod("setMessage")
   @MethodParams({CharSequence.class})
   public static void setMessage(AlertDialog thiz, CharSequence msg) throws Throwable {
      if (VirtualCore.get().isMainProcess()) {
         SandHook.callOriginByBackup(setMessageBackup, thiz, msg);
      } else {
         if (msg != null) {
            Log.d("AlertDialogHooker", msg.toString());
            if (msg.toString().contains("維護")) {
               thiz.cancel();
            }
         }

         SandHook.callOriginByBackup(setMessageBackup, thiz, msg);
      }
   }
}
