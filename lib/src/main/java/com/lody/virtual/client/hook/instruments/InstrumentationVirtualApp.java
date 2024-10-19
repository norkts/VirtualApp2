package com.lody.virtual.client.hook.instruments;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.AppCallback;
import com.lody.virtual.client.core.InvocationStubManager;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.fixer.ActivityFixer;
import com.lody.virtual.client.fixer.ContextFixer;
import com.lody.virtual.client.hook.proxies.am.HCallbackStub;
import com.lody.virtual.client.interfaces.IInjector;
import com.lody.virtual.helper.utils.VLog;
import java.lang.reflect.Field;
import mirror.android.app.ActivityThread;

public class InstrumentationVirtualApp extends InstrumentationProxy implements IInjector {
   private static final String TAG = InstrumentationVirtualApp.class.getSimpleName();
   private static InstrumentationVirtualApp gDefault;

   public static InstrumentationVirtualApp getDefault() {
      if (gDefault == null) {
         Class var0 = InstrumentationVirtualApp.class;
         synchronized(InstrumentationVirtualApp.class) {
            if (gDefault == null) {
               gDefault = create();
            }
         }
      }

      return gDefault;
   }

   private static InstrumentationVirtualApp create() {
      Instrumentation instrumentation = (Instrumentation)ActivityThread.mInstrumentation.get(VirtualCore.mainThread());
      return instrumentation instanceof InstrumentationVirtualApp ? (InstrumentationVirtualApp)instrumentation : new InstrumentationVirtualApp(instrumentation);
   }

   public InstrumentationVirtualApp(Instrumentation base) {
      super(base);
   }

   private void dynamicResolveConflict() {
      try {
         Field[] fields = this.base.getClass().getDeclaredFields();
         Field[] var2 = fields;
         int var3 = fields.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            if (field.getType().isAssignableFrom(Instrumentation.class)) {
               VLog.e(TAG, "resolve conflict instrumentation: %s->%s", this.base.getClass().getName(), field.getName());
               field.setAccessible(true);
               field.set(this.base, this.root);
            }
         }
      } catch (Throwable var6) {
         Throwable e = var6;
         e.printStackTrace();
      }

   }

   public Instrumentation getBaseInstrumentation() {
      return this.base;
   }

   public void inject() throws Throwable {
      Instrumentation baseNew = (Instrumentation)ActivityThread.mInstrumentation.get(VirtualCore.mainThread());
      if (this.base == null) {
         this.base = baseNew;
      }

      if (baseNew != this.base) {
         this.root = this.base;
         this.base = baseNew;
         this.dynamicResolveConflict();
      }

      ActivityThread.mInstrumentation.set(VirtualCore.mainThread(), this);
   }

   public boolean isEnvBad() {
      Instrumentation current = (Instrumentation)ActivityThread.mInstrumentation.get(VirtualCore.mainThread());
      return !(current instanceof InstrumentationVirtualApp);
   }

   private void checkActivityCallback() {
      InvocationStubManager.getInstance().checkEnv(HCallbackStub.class);
      InvocationStubManager.getInstance().checkEnv(InstrumentationVirtualApp.class);
   }

   public void callApplicationOnCreate(Application app) {
      this.checkActivityCallback();
      super.callApplicationOnCreate(app);
   }

   private AppCallback getAppCallback() {
      return VirtualCore.get().getAppCallback();
   }

   public void callActivityOnCreate(Activity activity, Bundle icicle) {
      this.checkActivityCallback();
      ActivityInfo info = (ActivityInfo)mirror.android.app.Activity.mActivityInfo.get(activity);
      String packageName = info != null ? info.packageName : null;
      ContextFixer.fixContext(activity, packageName);
      ActivityFixer.fixActivity(activity);
      AppCallback callback = this.getAppCallback();
      callback.beforeActivityOnCreate(activity);
      super.callActivityOnCreate(activity, icicle);
      callback.afterActivityOnCreate(activity);
   }

   public void callActivityOnCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
      this.checkActivityCallback();
      ActivityInfo info = (ActivityInfo)mirror.android.app.Activity.mActivityInfo.get(activity);
      String packageName = info != null ? info.packageName : null;
      ContextFixer.fixContext(activity, packageName);
      ActivityFixer.fixActivity(activity);
      AppCallback callback = this.getAppCallback();
      callback.beforeActivityOnCreate(activity);
      super.callActivityOnCreate(activity, icicle, persistentState);
      callback.afterActivityOnCreate(activity);
   }

   public void callActivityOnStart(Activity activity) {
      AppCallback callback = this.getAppCallback();
      callback.beforeActivityOnStart(activity);
      super.callActivityOnStart(activity);
      if (!VirtualCore.getConfig().disableSetScreenOrientation(activity.getPackageName())) {
         ActivityInfo info = (ActivityInfo)mirror.android.app.Activity.mActivityInfo.get(activity);
         if (info != null && info.screenOrientation != -1 && activity.getRequestedOrientation() == -1) {
            activity.setRequestedOrientation(info.screenOrientation);
         }
      }

      callback.afterActivityOnStart(activity);
   }

   public void callActivityOnResume(Activity activity) {
      AppCallback callback = this.getAppCallback();
      callback.beforeActivityOnResume(activity);
      super.callActivityOnResume(activity);
      callback.afterActivityOnResume(activity);
   }

   public void callActivityOnStop(Activity activity) {
      AppCallback callback = this.getAppCallback();
      callback.beforeActivityOnStop(activity);
      super.callActivityOnStop(activity);
      callback.afterActivityOnStop(activity);
   }

   public void callActivityOnDestroy(Activity activity) {
      AppCallback callback = this.getAppCallback();
      callback.beforeActivityOnDestroy(activity);
      super.callActivityOnDestroy(activity);
      callback.afterActivityOnDestroy(activity);
   }

   private boolean checkIsEnvOk(Instrumentation instrumentation) {
      if (instrumentation instanceof InstrumentationVirtualApp) {
         return true;
      } else {
         Class cls = instrumentation.getClass();
         if (Instrumentation.class.equals(cls)) {
            return false;
         } else if (TextUtils.equals(VClient.get().getCurrentPackage(), "com.zhiliaoapp.musically") && VERSION.SDK_INT == 26) {
            return false;
         } else {
            do {
               Field[] declaredFields = cls.getDeclaredFields();
               if (declaredFields != null) {
                  Field[] var4 = declaredFields;
                  int var5 = declaredFields.length;

                  for(int var6 = 0; var6 < var5; ++var6) {
                     Field field = var4[var6];
                     if (Instrumentation.class.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);

                        try {
                           if (field.get(instrumentation) instanceof InstrumentationVirtualApp) {
                              return true;
                           }
                        } catch (IllegalAccessException var9) {
                           return false;
                        }
                     }
                  }
               }

               cls = cls.getSuperclass();
            } while(!Instrumentation.class.equals(cls));

            return false;
         }
      }
   }
}
