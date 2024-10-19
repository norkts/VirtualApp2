package com.lody.virtual.client.hook.proxies.vibrator;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.helper.compat.BuildCompat;
import java.lang.reflect.Method;
import mirror.com.android.internal.os.IVibratorManagerService;
import mirror.com.android.internal.os.IVibratorService;

public class VibratorStub extends BinderInvocationProxy {
   public VibratorStub() {
      super(BuildCompat.isS() ? IVibratorManagerService.Stub.asInterface : IVibratorService.Stub.asInterface, BuildCompat.isS() ? "vibrator_manager" : "vibrator");
   }

   protected void onBindMethods() {
      this.addMethodProxy(new VibrateMethodProxy("vibrateMagnitude"));
      this.addMethodProxy(new VibrateMethodProxy("vibratePatternMagnitude"));
      this.addMethodProxy(new VibrateMethodProxy("vibrate"));
      this.addMethodProxy(new VibrateMethodProxy("vibratePattern"));
   }

   private static final class VibrateMethodProxy extends ReplaceCallingPkgMethodProxy {
      private VibrateMethodProxy(String name) {
         super(name);
      }

      public boolean beforeCall(Object who, Method method, Object... args) {
         if (args[0] instanceof Integer) {
            args[0] = getRealUid();
         }

         return super.beforeCall(who, method, args);
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return super.call(who, method, args);
      }

      // $FF: synthetic method
      VibrateMethodProxy(String x0, Object x1) {
         this(x0);
      }
   }
}
