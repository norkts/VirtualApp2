package com.carlos.common.reverse.hooker;

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

@HookClass(Thread.class)
public class ThreadHooker {
   static List<String> bypassList = new ArrayList();
   @HookMethodBackup("start")
   static Method methodStart;

   @HookMethod("start")
   public static void start(Thread thiz) throws Throwable {
      if (VirtualCore.get().isMainProcess()) {
         SandHook.callOriginByBackup(methodStart, thiz);
      } else {
         String packageName = VClient.get().getCurrentPackage();
         String clzName = thiz.getClass().getName();
         Log.e("ThreadHooker", "hooked Thread start packageName:" + packageName + "  thiz:" + thiz + "  clzName:" + clzName);
         Thread.dumpStack();
         if (bypassList.contains(clzName)) {
            thiz.interrupt();
            Log.e("ThreadHooker", clzName + " hooked pass");
         } else {
            SandHook.callOriginByBackup(methodStart, thiz);
         }
      }
   }

   static {
      bypassList.add("com.inca.security.iiiIiiiIiI");
      bypassList.add("com.inca.security.iIiIiiiIii");
      bypassList.add("com.inca.security.iIIiiiiIiI");
      bypassList.add("com.inca.security.IIiIiiiIiI");
      bypassList.add("com.inca.security.iiiiiiiIIi");
      bypassList.add("com.inca.security.iIiIiiiiiI");
      bypassList.add("com.inca.security.IiIiiiIiiI");
      bypassList.add("com.inca.security.iiiIiiiiII");
      bypassList.add("com.inca.security.iiiIiiiIii");
      bypassList.add("com.inca.security.iIIIiiiiiI");
      bypassList.add("com.inca.security.iiIIiiiiiI");
      bypassList.add("com.inca.security.IIiiIiIiiI");
      bypassList.add("com.inca.security.IIiiiiiIiI");
      bypassList.add("com.inca.security.IiIIiiiiIi");
      bypassList.add("com.inca.security.IiIIiiiiii");
      bypassList.add("com.inca.security.iIIiiiiiII");
      bypassList.add("com.inca.security.IiiiIiiIii");
      bypassList.add("com.inca.security.IIIIIiiIiI");
      bypassList.add("com.inca.security.IIiIiiiIii");
      bypassList.add("com.inca.security.IIiIiiiIii");
      bypassList.add("com.inca.security.wa");
      bypassList.add("com.inca.security.wk");
      bypassList.add("com.inca.security.rb");
      bypassList.add("com.inca.security.fb");
      bypassList.add("com.inca.security.ll");
      bypassList.add("com.inca.security.ii");
      bypassList.add("com.inca.security.ib");
      bypassList.add("com.inca.security.jb");
      bypassList.add("com.inca.security.wb");
      bypassList.add("com.inca.security.i");
      bypassList.add("com.inca.security.Proxy.AppGuardProxyManager$1");
   }
}
