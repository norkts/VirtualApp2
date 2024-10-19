package com.carlos.common.reverse.hooker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.carlos.libcommon.StringFog;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.MethodParams;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

@HookClass(Toast.class)
public class ToastHooker {
   @HookMethodBackup("show")
   static Method method_m1;
   @HookMethodBackup("makeText")
   @MethodParams({Context.class, CharSequence.class, int.class})
   static Method method_m2;

   public static void e() {
      Thread.currentThread();
      Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
      if (map != null && map.size() != 0) {
         Iterator keyIterator = map.keySet().iterator();

         while(true) {
            while(keyIterator.hasNext()) {
               Thread eachThread = (Thread)keyIterator.next();
               StackTraceElement[] array = (StackTraceElement[])map.get(eachThread);
               System.out.println(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MwQHDXpSHSNYAhMLAlcdCkcWLTVBK1pNAAlAGlgXDz1EA14bAQsrO0EHIQM=")));
               System.out.println(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PlsjIlQ7OSAcLAsCXx83Mx87MTQbN1RF")) + eachThread.getName());
               System.out.println(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhU2LGsVLCFuESw7Ly0MWm8zGiNrARo/JzsHKmAaLCluJzAhPS5SVg==")) + array.length);
               System.out.println(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PlsjIlQ7OSAcLD1NXVo3XR5WGxIVCRs3XC5SVg==")) + eachThread.getState());
               if (array.length != 0) {
                  System.out.println(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PltcJhg7MRcYNSggLwcqCX0wRTdoJyhLLAguL2IKRT9jJUIvRCFEIVQtMj8VXyIzXxosIx0sOjAbKyo6EUBXVg==")));

                  for(int i = 0; i < array.length; ++i) {
                     StackTraceElement eachElement = array[i];
                     System.out.println(" " + eachElement.getClassName() + " " + eachElement.getMethodName() + " " + eachElement.getFileName() + " " + eachElement.getLineNumber());
                  }
               } else {
                  System.out.println(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pls/KhxWJR4cDyggLwcqCX0wRTdoJyhLLAguL2IKRT9jJUItWhxZIhURBANUEwQ0EyEgIxxaHD8dXyoyWURbVg==")) + eachThread.getName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcDUYEWhZhJwo7Ly0EAGoVQSlrDygdLhgIJ2AzFlNgCh4bLy4qIW8VRDd+EVRF")) + array.length);
               }
            }

            return;
         }
      }
   }

   @HookMethod("makeText")
   @MethodParams({Context.class, CharSequence.class, int.class})
   public static Toast makeText(Context context, CharSequence cs, int d) throws Throwable {
      Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRgAP28wMApgJB4xKAguVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+MWgYMCtnEQ0i")) + cs.toString() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phc6P2szQTdiJDAMLwdXPXgVSFo=")) + context.getPackageName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRhfKmgVJCx3N1RF")) + Thread.currentThread().getClass().getName());
      e();
      return (Toast)SandHook.callOriginByBackup(method_m2, (Object)null, context, cs, d);
   }

   @HookMethod("show")
   public static void show(Toast thiz) throws Throwable {
      try {
         Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRgAP28wMApgJB4xKAguVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phc2CmowPFo=")));
      } catch (Exception var2) {
         Exception e = var2;
         e.printStackTrace();
      }

      SandHook.callOriginByBackup(method_m1, thiz);
   }
}
