package com.lody.virtual.client.hook.proxies.view;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ComponentName;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastUserIdMethodProxy;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.VLog;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import mirror.android.app.ActivityThread;
import mirror.android.view.IAutoFillManager;

public class AutoFillManagerStub extends BinderInvocationProxy {
   private static final String TAG = "AutoFillManagerStub";
   private static final String AUTO_FILL_NAME = "autofill";

   public AutoFillManagerStub() {
      super(IAutoFillManager.Stub.asInterface, "autofill");
   }

   @SuppressLint({"WrongConstant"})
   public void inject() throws Throwable {
      super.inject();

      try {
         Object mainThread = ActivityThread.currentActivityThread.call();
         VLog.e("HV-", " AutoFillManagerStub inject mainThread:" + mainThread);
         if (mainThread != null) {
            Application application = (Application)ActivityThread.mInitialApplication.get(mainThread);
            VLog.e("HV-", " AutoFillManagerStub inject application:" + application);
         }

         Object AutoFillManagerInstance = this.getContext().getSystemService(AUTO_FILL_NAME);
         if (AutoFillManagerInstance == null) {
            throw new NullPointerException("AutoFillManagerInstance is null.");
         }

         Object AutoFillManagerProxy = ((BinderInvocationStub)this.getInvocationStub()).getProxyInterface();
         if (AutoFillManagerProxy == null) {
            throw new NullPointerException("AutoFillManagerProxy is null.");
         }

         Field AutoFillManagerServiceField = AutoFillManagerInstance.getClass().getDeclaredField("mService");
         AutoFillManagerServiceField.setAccessible(true);
         AutoFillManagerServiceField.set(AutoFillManagerInstance, AutoFillManagerProxy);
      } catch (Throwable var5) {
         Throwable tr = var5;
         Log.e(TAG, "AutoFillManagerStub inject error.", tr);
         return;
      }

      this.addMethodProxy(new ReplacePkgAndComponentProxy("startSession") {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            replaceFirstUserId(args);
            return super.call(who, method, args);
         }
      });
      this.addMethodProxy(new ReplacePkgAndComponentProxy("updateOrRestartSession"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("isServiceEnabled"));
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("addClient"));
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("removeClient"));
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("updateSession"));
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("finishSession"));
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("cancelSession"));
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("setAuthenticationResult"));
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("setHasCallback"));
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("disableOwnedAutofillServices"));
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("isServiceSupported"));
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("isServiceEnabled") {
         public boolean beforeCall(Object who, Method method, Object... args) {
            MethodParameterUtils.replaceLastAppPkg(args);
            return super.beforeCall(who, method, args);
         }
      });
   }

   public static void disableAutoFill(Object object) {
      try {
         if (object == null) {
            throw new NullPointerException("AutoFillManagerInstance is null.");
         } else {
            Field AutoFillManagerServiceField = object.getClass().getDeclaredField("mService");
            AutoFillManagerServiceField.setAccessible(true);
            AutoFillManagerServiceField.set(object, (Object)null);
         }
      } catch (Throwable var2) {
         Throwable tr = var2;
         Log.e(TAG, "AutoFillManagerStub inject error.", tr);
      }
   }

   static class ReplacePkgAndComponentProxy extends ReplaceLastPkgMethodProxy {
      ReplacePkgAndComponentProxy(String name) {
         super(name);
      }

      public boolean beforeCall(Object who, Method method, Object... args) {
         this.replaceLastAppComponent(args, getHostPkg());
         return super.beforeCall(who, method, args);
      }

      private void replaceLastAppComponent(Object[] args, String hostPkg) {
         int index = ArrayUtils.indexOfLast(args, ComponentName.class);
         if (index != -1) {
            ComponentName orig = (ComponentName)args[index];
            ComponentName newComponent = new ComponentName(hostPkg, orig.getClassName());
            args[index] = newComponent;
         }

      }
   }
}
