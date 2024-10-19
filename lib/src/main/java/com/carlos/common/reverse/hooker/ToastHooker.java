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
               System.out.println("------每个线程的基本信息");
               System.out.println(" 线程名称：" + eachThread.getName());
               System.out.println(" StackTraceElement[].length=" + array.length);
               System.out.println(" 线程的状态：" + eachThread.getState());
               if (array.length != 0) {
                  System.out.println(" 输出StackTraceElement[]数组具体信息：");

                  for(int i = 0; i < array.length; ++i) {
                     StackTraceElement eachElement = array[i];
                     System.out.println(" " + eachElement.getClassName() + " " + eachElement.getMethodName() + " " + eachElement.getFileName() + " " + eachElement.getLineNumber());
                  }
               } else {
                  System.out.println(" 没有StackTraceElement[]信息，因为线程" + eachThread.getName() + "中的stackTraceElement[].length==" + array.length);
               }
            }

            return;
         }
      }
   }

   @HookMethod("makeText")
   @MethodParams({Context.class, CharSequence.class, int.class})
   public static Toast makeText(Context context, CharSequence cs, int d) throws Throwable {
      Log.d("ToastHooker", "makeText:" + cs.toString() + " packageName:" + context.getPackageName() + "Thread:" + Thread.currentThread().getClass().getName());
      e();
      return (Toast)SandHook.callOriginByBackup(method_m2, (Object)null, context, cs, d);
   }

   @HookMethod("show")
   public static void show(Toast thiz) throws Throwable {
      try {
         Log.d("ToastHooker", " show");
      } catch (Exception var2) {
         Exception e = var2;
         e.printStackTrace();
      }

      SandHook.callOriginByBackup(method_m1, thiz);
   }
}
