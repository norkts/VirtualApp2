package com.lody.virtual.client.hook.proxies.power;

import android.os.Build;
import android.os.WorkSource;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceSequencePkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import mirror.android.os.IPowerManager;

public class PowerManagerStub extends BinderInvocationProxy {
   public PowerManagerStub() {
      super(IPowerManager.Stub.asInterface, "power");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("wakeUp"));
      this.addMethodProxy(new ReplaceSequencePkgMethodProxy("acquireWakeLock", 2) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            PowerManagerStub.this.replaceWorkSource(args);

            try {
               return super.call(who, method, args);
            } catch (InvocationTargetException var5) {
               InvocationTargetException e = var5;
               return PowerManagerStub.this.onHandleError(e);
            }
         }
      });
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("acquireWakeLockWithUid") {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            PowerManagerStub.this.replaceWorkSource(args);

            try {
               return super.call(who, method, args);
            } catch (InvocationTargetException var5) {
               InvocationTargetException e = var5;
               return PowerManagerStub.this.onHandleError(e);
            }
         }
      });
      this.addMethodProxy(new ResultStaticMethodProxy("updateWakeLockWorkSource", 0) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            PowerManagerStub.this.replaceWorkSource(args);
            return super.call(who, method, args);
         }
      });
      if (Build.MANUFACTURER.equalsIgnoreCase("FUJITSU")) {
         this.addMethodProxy(new StaticMethodProxy("acquireWakeLockWithLogging") {
            public Object call(Object who, Method method, Object... args) throws Throwable {
               if (args[3] instanceof String && this.isAppPkg((String)args[3])) {
                  args[3] = getHostPkg();
               }

               PowerManagerStub.this.replaceWorkSource(args);
               return super.call(who, method, args);
            }
         });
      }

   }

   private void replaceWorkSource(Object[] args) {
      for(int i = 0; i < args.length; ++i) {
         if (args[i] instanceof WorkSource) {
            args[i] = null;
            break;
         }
      }

   }

   private Object onHandleError(InvocationTargetException e) throws Throwable {
      if (e.getCause() instanceof SecurityException) {
         return 0;
      } else {
         throw e.getCause();
      }
   }
}
