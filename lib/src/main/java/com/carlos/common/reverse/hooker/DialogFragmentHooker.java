package com.carlos.common.reverse.hooker;

import android.app.DialogFragment;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@HookClass(DialogFragment.class)
public class DialogFragmentHooker {
   static List<String> hiddenDialog = new ArrayList();
   @HookMethodBackup("onStart")
   static Method onStartBackup;
   @HookMethodBackup("dismiss")
   static Method dismiss;

   @HookMethod("onStart")
   public static void onStart(DialogFragment thiz) throws Throwable {
      if (VirtualCore.get().isMainProcess()) {
         SandHook.callOriginByBackup(onStartBackup, thiz);
      } else {
         String packageName = VClient.get().getCurrentPackage();
         String clzName = thiz.getClass().getName();
         Log.e("Hooker", packageName + " DialogFragment onStart " + clzName + "  " + thiz.getDialog().hashCode());
         if (HookDialogUtils.isHiddDialog(clzName, packageName)) {
            HookDialogUtils.addHiddenDialogCode(thiz.getDialog().hashCode());
         }

         SandHook.callOriginByBackup(onStartBackup, thiz);
      }
   }

   static {
      hiddenDialog.add("kr.co.nexon.toy.android.ui.auth.accountmenu.NXPAccountMenuDialog");
   }
}
