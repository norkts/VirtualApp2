package com.lody.virtual.client.hook.instruments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Instrumentation;
import android.app.UiAutomation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.MultiAvoidRecursive;
import com.lody.virtual.helper.utils.VLog;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class InstrumentationProxy extends Instrumentation {
   protected Instrumentation base;
   protected Instrumentation root;
   private MultiAvoidRecursive avoidRecursive = new MultiAvoidRecursive(20);

   public InstrumentationProxy(Instrumentation base) {
      this.base = base;
      this.root = base;
   }

   public void onCreate(Bundle arguments) {
      this.base.onCreate(arguments);
   }

   public void start() {
      this.base.start();
   }

   public void onStart() {
      this.base.onStart();
   }

   public boolean onException(Object obj, Throwable e) {
      return this.base.onException(obj, e);
   }

   public void sendStatus(int resultCode, Bundle results) {
      this.base.sendStatus(resultCode, results);
   }

   public void finish(int resultCode, Bundle results) {
      this.base.finish(resultCode, results);
   }

   public void setAutomaticPerformanceSnapshots() {
      this.base.setAutomaticPerformanceSnapshots();
   }

   public void startPerformanceSnapshot() {
      this.base.startPerformanceSnapshot();
   }

   public void endPerformanceSnapshot() {
      this.base.endPerformanceSnapshot();
   }

   public void onDestroy() {
      this.base.onDestroy();
   }

   public Context getContext() {
      return this.base.getContext();
   }

   public ComponentName getComponentName() {
      return this.base.getComponentName();
   }

   public Context getTargetContext() {
      return this.base.getTargetContext();
   }

   public boolean isProfiling() {
      return this.base.isProfiling();
   }

   public void startProfiling() {
      this.base.startProfiling();
   }

   public void stopProfiling() {
      this.base.stopProfiling();
   }

   public void setInTouchMode(boolean inTouch) {
      this.base.setInTouchMode(inTouch);
   }

   public void waitForIdle(Runnable recipient) {
      this.base.waitForIdle(recipient);
   }

   public void waitForIdleSync() {
      this.base.waitForIdleSync();
   }

   public void runOnMainSync(Runnable runner) {
      this.base.runOnMainSync(runner);
   }

   public Activity startActivitySync(Intent intent) {
      return this.base.startActivitySync(intent);
   }

   public void addMonitor(Instrumentation.ActivityMonitor monitor) {
      this.base.addMonitor(monitor);
   }

   public Instrumentation.ActivityMonitor addMonitor(IntentFilter filter, Instrumentation.ActivityResult result, boolean block) {
      return this.base.addMonitor(filter, result, block);
   }

   public Instrumentation.ActivityMonitor addMonitor(String cls, Instrumentation.ActivityResult result, boolean block) {
      return this.base.addMonitor(cls, result, block);
   }

   public boolean checkMonitorHit(Instrumentation.ActivityMonitor monitor, int minHits) {
      return this.base.checkMonitorHit(monitor, minHits);
   }

   public Activity waitForMonitor(Instrumentation.ActivityMonitor monitor) {
      return this.base.waitForMonitor(monitor);
   }

   public Activity waitForMonitorWithTimeout(Instrumentation.ActivityMonitor monitor, long timeOut) {
      return this.base.waitForMonitorWithTimeout(monitor, timeOut);
   }

   public void removeMonitor(Instrumentation.ActivityMonitor monitor) {
      this.base.removeMonitor(monitor);
   }

   public boolean invokeMenuActionSync(Activity targetActivity, int id, int flag) {
      return this.base.invokeMenuActionSync(targetActivity, id, flag);
   }

   public boolean invokeContextMenuAction(Activity targetActivity, int id, int flag) {
      return this.base.invokeContextMenuAction(targetActivity, id, flag);
   }

   public void sendStringSync(String text) {
      this.base.sendStringSync(text);
   }

   public void sendKeySync(KeyEvent event) {
      this.base.sendKeySync(event);
   }

   public void sendKeyDownUpSync(int key) {
      this.base.sendKeyDownUpSync(key);
   }

   public void sendCharacterSync(int keyCode) {
      this.base.sendCharacterSync(keyCode);
   }

   public void sendPointerSync(MotionEvent event) {
      this.base.sendPointerSync(event);
   }

   public void sendTrackballEventSync(MotionEvent event) {
      this.base.sendTrackballEventSync(event);
   }

   public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      Application var4;
      try {
         if (!this.avoidRecursive.beginCall(0)) {
            var4 = this.root.newApplication(cl, className, context);
            return var4;
         }

         var4 = this.base.newApplication(cl, className, context);
      } finally {
         this.avoidRecursive.finishCall(0);
      }

      return var4;
   }

   public void callApplicationOnCreate(Application app) {
      try {
         if (this.avoidRecursive.beginCall(1)) {
            this.base.callApplicationOnCreate(app);
         } else {
            this.root.callApplicationOnCreate(app);
         }
      } catch (Throwable var6) {
         Throwable e = var6;
         VLog.e("VA-", e);
      } finally {
         this.avoidRecursive.finishCall(1);
      }

   }

   public Activity newActivity(Class<?> clazz, Context context, IBinder token, Application application, Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id, Object lastNonConfigurationInstance) throws InstantiationException, IllegalAccessException {
      Activity var11;
      try {
         if (!this.avoidRecursive.beginCall(2)) {
            var11 = this.root.newActivity(clazz, context, token, application, intent, info, title, parent, id, lastNonConfigurationInstance);
            return var11;
         }

         var11 = this.base.newActivity(clazz, context, token, application, intent, info, title, parent, id, lastNonConfigurationInstance);
      } finally {
         this.avoidRecursive.finishCall(2);
      }

      return var11;
   }

   public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      Activity var4;
      try {
         if (this.avoidRecursive.beginCall(3)) {
            var4 = this.base.newActivity(cl, className, intent);
            return var4;
         }

         var4 = this.root.newActivity(cl, className, intent);
      } finally {
         this.avoidRecursive.finishCall(3);
      }

      return var4;
   }

   public void callActivityOnCreate(Activity activity, Bundle icicle) {
      try {
         if (this.avoidRecursive.beginCall(4)) {
            this.base.callActivityOnCreate(activity, icicle);
         } else {
            this.root.callActivityOnCreate(activity, icicle);
         }
      } finally {
         this.avoidRecursive.finishCall(4);
      }

   }

   @TargetApi(21)
   public void callActivityOnCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
      try {
         if (this.avoidRecursive.beginCall(5)) {
            this.base.callActivityOnCreate(activity, icicle, persistentState);
         } else {
            this.root.callActivityOnCreate(activity, icicle, persistentState);
         }
      } finally {
         this.avoidRecursive.finishCall(5);
      }

   }

   public void callActivityOnDestroy(Activity activity) {
      try {
         if (this.avoidRecursive.beginCall(6)) {
            this.base.callActivityOnDestroy(activity);
         } else {
            this.root.callActivityOnDestroy(activity);
         }
      } finally {
         this.avoidRecursive.finishCall(6);
      }

   }

   public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState) {
      try {
         if (this.avoidRecursive.beginCall(7)) {
            this.base.callActivityOnRestoreInstanceState(activity, savedInstanceState);
         } else {
            this.root.callActivityOnRestoreInstanceState(activity, savedInstanceState);
         }
      } finally {
         this.avoidRecursive.finishCall(7);
      }

   }

   @TargetApi(21)
   public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState, PersistableBundle persistentState) {
      try {
         if (this.avoidRecursive.beginCall(8)) {
            this.base.callActivityOnRestoreInstanceState(activity, savedInstanceState, persistentState);
         } else {
            this.root.callActivityOnRestoreInstanceState(activity, savedInstanceState, persistentState);
         }
      } finally {
         this.avoidRecursive.finishCall(8);
      }

   }

   public void callActivityOnPostCreate(Activity activity, Bundle icicle) {
      try {
         if (this.avoidRecursive.beginCall(9)) {
            this.base.callActivityOnPostCreate(activity, icicle);
         } else {
            this.root.callActivityOnPostCreate(activity, icicle);
         }
      } finally {
         this.avoidRecursive.finishCall(9);
      }

   }

   @TargetApi(21)
   public void callActivityOnPostCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
      try {
         if (this.avoidRecursive.beginCall(10)) {
            this.base.callActivityOnPostCreate(activity, icicle, persistentState);
         } else {
            this.root.callActivityOnPostCreate(activity, icicle, persistentState);
         }
      } finally {
         this.avoidRecursive.finishCall(10);
      }

   }

   public void callActivityOnNewIntent(Activity activity, Intent intent) {
      try {
         if (this.avoidRecursive.beginCall(11)) {
            this.base.callActivityOnNewIntent(activity, intent);
         } else {
            this.root.callActivityOnNewIntent(activity, intent);
         }
      } finally {
         this.avoidRecursive.finishCall(11);
      }

   }

   public void callActivityOnStart(Activity activity) {
      try {
         if (this.avoidRecursive.beginCall(12)) {
            this.base.callActivityOnStart(activity);
         } else {
            this.root.callActivityOnStart(activity);
         }
      } finally {
         this.avoidRecursive.finishCall(12);
      }

   }

   public void callActivityOnRestart(Activity activity) {
      try {
         if (this.avoidRecursive.beginCall(13)) {
            this.base.callActivityOnRestart(activity);
         } else {
            this.root.callActivityOnRestart(activity);
         }
      } finally {
         this.avoidRecursive.finishCall(13);
      }

   }

   public void callActivityOnResume(Activity activity) {
      try {
         if (this.avoidRecursive.beginCall(14)) {
            this.base.callActivityOnResume(activity);
         } else {
            this.root.callActivityOnResume(activity);
         }
      } finally {
         this.avoidRecursive.finishCall(14);
      }

   }

   public void callActivityOnStop(Activity activity) {
      try {
         if (this.avoidRecursive.beginCall(15)) {
            this.base.callActivityOnStop(activity);
         } else {
            this.root.callActivityOnStop(activity);
         }
      } finally {
         this.avoidRecursive.finishCall(15);
      }

   }

   public void callActivityOnSaveInstanceState(Activity activity, Bundle outState) {
      try {
         if (this.avoidRecursive.beginCall(16)) {
            this.base.callActivityOnSaveInstanceState(activity, outState);
         } else {
            this.root.callActivityOnSaveInstanceState(activity, outState);
         }
      } finally {
         this.avoidRecursive.finishCall(16);
      }

   }

   @TargetApi(21)
   public void callActivityOnSaveInstanceState(Activity activity, Bundle outState, PersistableBundle outPersistentState) {
      try {
         if (this.avoidRecursive.beginCall(17)) {
            this.base.callActivityOnSaveInstanceState(activity, outState, outPersistentState);
         } else {
            this.root.callActivityOnSaveInstanceState(activity, outState, outPersistentState);
         }
      } finally {
         this.avoidRecursive.finishCall(17);
      }

   }

   public void callActivityOnPause(Activity activity) {
      try {
         if (this.avoidRecursive.beginCall(18)) {
            this.base.callActivityOnPause(activity);
         } else {
            this.root.callActivityOnPause(activity);
         }
      } finally {
         this.avoidRecursive.finishCall(18);
      }

   }

   public void callActivityOnUserLeaving(Activity activity) {
      try {
         if (this.avoidRecursive.beginCall(19)) {
            this.base.callActivityOnUserLeaving(activity);
         } else {
            this.root.callActivityOnUserLeaving(activity);
         }
      } finally {
         this.avoidRecursive.finishCall(19);
      }

   }

   public Bundle getAllocCounts() {
      return this.base.getAllocCounts();
   }

   public Bundle getBinderCounts() {
      return this.base.getBinderCounts();
   }

   @TargetApi(30)
   public void callActivityOnPictureInPictureRequested(Activity activity) {
      this.base.callActivityOnPictureInPictureRequested(activity);
   }

   @TargetApi(18)
   public UiAutomation getUiAutomation() {
      return this.base.getUiAutomation();
   }

   public Instrumentation.ActivityResult execStartActivity(Context context, IBinder iBinder, IBinder iBinder2, Activity activity, Intent intent, int i, Bundle bundle) throws Throwable {
      Instrumentation.ActivityResult var18;
      try {
         Object var9;
         try {
            if (this.avoidRecursive.beginCall(20)) {
               var18 = (Instrumentation.ActivityResult)findDeclaredMethod(this.base, "execStartActivity", Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, Integer.TYPE, Bundle.class).invoke(this.base, context, iBinder, iBinder2, activity, intent, i, bundle);
               return var18;
            }

            var18 = (Instrumentation.ActivityResult)findDeclaredMethod(this.root, "execStartActivity", Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, Integer.TYPE, Bundle.class).invoke(this.root, context, iBinder, iBinder2, activity, intent, i, bundle);
         } catch (InvocationTargetException var14) {
            InvocationTargetException e = var14;
            if (e.getCause() != null) {
               throw e.getCause();
            }

            var9 = null;
            return (Instrumentation.ActivityResult)var9;
         } catch (Exception var15) {
            Exception e = var15;
            e.printStackTrace();
            var9 = null;
            return (Instrumentation.ActivityResult)var9;
         }
      } finally {
         this.avoidRecursive.finishCall(20);
      }

      return var18;
   }

   public Instrumentation.ActivityResult execStartActivity(Context context, IBinder iBinder, IBinder iBinder2, String str, Intent intent, int i, Bundle bundle) throws Throwable {
      Instrumentation.ActivityResult var18;
      try {
         Object var9;
         try {
            if (this.avoidRecursive.beginCall(21)) {
               var18 = (Instrumentation.ActivityResult)findDeclaredMethod(this.base, "execStartActivity", Context.class, IBinder.class, IBinder.class, String.class, Intent.class, Integer.TYPE, Bundle.class).invoke(this.base, context, iBinder, iBinder2, str, intent, i, bundle);
               return var18;
            }

            var18 = (Instrumentation.ActivityResult)findDeclaredMethod(this.root, "execStartActivity", Context.class, IBinder.class, IBinder.class, String.class, Intent.class, Integer.TYPE, Bundle.class).invoke(this.root, context, iBinder, iBinder2, str, intent, i, bundle);
         } catch (InvocationTargetException var14) {
            InvocationTargetException e = var14;
            if (e.getCause() != null) {
               throw e.getCause();
            }

            var9 = null;
            return (Instrumentation.ActivityResult)var9;
         } catch (Exception var15) {
            Exception e = var15;
            e.printStackTrace();
            var9 = null;
            return (Instrumentation.ActivityResult)var9;
         }
      } finally {
         this.avoidRecursive.finishCall(21);
      }

      return var18;
   }

   public Instrumentation.ActivityResult execStartActivity(Context context, IBinder iBinder, IBinder iBinder2, Fragment fragment, Intent intent, int i) throws Throwable {
      Object var8;
      try {
         Instrumentation.ActivityResult var17;
         if (this.avoidRecursive.beginCall(22)) {
            var17 = (Instrumentation.ActivityResult)findDeclaredMethod(this.base, "execStartActivity", Context.class, IBinder.class, IBinder.class, Fragment.class, Intent.class, Integer.TYPE).invoke(this.base, context, iBinder, iBinder2, fragment, intent, i);
            return var17;
         }

         var17 = (Instrumentation.ActivityResult)findDeclaredMethod(this.root, "execStartActivity", Context.class, IBinder.class, IBinder.class, Fragment.class, Intent.class, Integer.TYPE).invoke(this.root, context, iBinder, iBinder2, fragment, intent, i);
         return var17;
      } catch (InvocationTargetException var13) {
         InvocationTargetException e = var13;
         if (e.getCause() != null) {
            throw e.getCause();
         }

         var8 = null;
         return (Instrumentation.ActivityResult)var8;
      } catch (Exception var14) {
         Exception e = var14;
         e.printStackTrace();
         var8 = null;
      } finally {
         this.avoidRecursive.finishCall(22);
      }

      return (Instrumentation.ActivityResult)var8;
   }

   public Instrumentation.ActivityResult execStartActivity(Context context, IBinder iBinder, IBinder iBinder2, Activity activity, Intent intent, int i) throws Throwable {
      Object var8;
      try {
         Instrumentation.ActivityResult var17;
         if (this.avoidRecursive.beginCall(23)) {
            var17 = (Instrumentation.ActivityResult)findDeclaredMethod(this.base, "execStartActivity", Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, Integer.TYPE).invoke(this.base, context, iBinder, iBinder2, activity, intent, i);
            return var17;
         }

         var17 = (Instrumentation.ActivityResult)findDeclaredMethod(this.root, "execStartActivity", Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, Integer.TYPE).invoke(this.root, context, iBinder, iBinder2, activity, intent, i);
         return var17;
      } catch (InvocationTargetException var13) {
         InvocationTargetException e = var13;
         if (e.getCause() != null) {
            throw e.getCause();
         }

         var8 = null;
      } catch (Exception var14) {
         Exception e = var14;
         e.printStackTrace();
         var8 = null;
         return (Instrumentation.ActivityResult)var8;
      } finally {
         this.avoidRecursive.finishCall(23);
      }

      return (Instrumentation.ActivityResult)var8;
   }

   public Instrumentation.ActivityResult execStartActivity(Context context, IBinder iBinder, IBinder iBinder2, Fragment fragment, Intent intent, int i, Bundle bundle) throws Throwable {
      Instrumentation.ActivityResult var18;
      try {
         Object var9;
         try {
            if (!this.avoidRecursive.beginCall(24)) {
               var18 = (Instrumentation.ActivityResult)findDeclaredMethod(this.root, "execStartActivity", Context.class, IBinder.class, IBinder.class, Fragment.class, Intent.class, Integer.TYPE, Bundle.class).invoke(this.root, context, iBinder, iBinder2, fragment, intent, i, bundle);
               return var18;
            }

            var18 = (Instrumentation.ActivityResult)findDeclaredMethod(this.base, "execStartActivity", Context.class, IBinder.class, IBinder.class, Fragment.class, Intent.class, Integer.TYPE, Bundle.class).invoke(this.base, context, iBinder, iBinder2, fragment, intent, i, bundle);
         } catch (InvocationTargetException var14) {
            InvocationTargetException e = var14;
            if (e.getCause() != null) {
               throw e.getCause();
            }

            var9 = null;
            return (Instrumentation.ActivityResult)var9;
         } catch (Exception var15) {
            Exception e = var15;
            e.printStackTrace();
            var9 = null;
            return (Instrumentation.ActivityResult)var9;
         }
      } finally {
         this.avoidRecursive.finishCall(24);
      }

      return var18;
   }

   @TargetApi(17)
   public Instrumentation.ActivityResult execStartActivity(Context context, IBinder iBinder, IBinder iBinder2, Activity activity, Intent intent, int i, Bundle bundle, UserHandle userHandle) throws Throwable {
      Object var10;
      try {
         Instrumentation.ActivityResult var19;
         if (!this.avoidRecursive.beginCall(25)) {
            var19 = (Instrumentation.ActivityResult)findDeclaredMethod(this.root, "execStartActivity", Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, Integer.TYPE, Bundle.class, UserHandle.class).invoke(this.root, context, iBinder, iBinder2, activity, intent, i, bundle, userHandle);
            return var19;
         }

         var19 = (Instrumentation.ActivityResult)findDeclaredMethod(this.base, "execStartActivity", Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, Integer.TYPE, Bundle.class, UserHandle.class).invoke(this.base, context, iBinder, iBinder2, activity, intent, i, bundle, userHandle);
         return var19;
      } catch (InvocationTargetException var15) {
         InvocationTargetException e = var15;
         if (e.getCause() != null) {
            throw e.getCause();
         }

         var10 = null;
      } catch (Exception var16) {
         Exception e = var16;
         e.printStackTrace();
         var10 = null;
         return (Instrumentation.ActivityResult)var10;
      } finally {
         this.avoidRecursive.finishCall(25);
      }

      return (Instrumentation.ActivityResult)var10;
   }

   private static Method findDeclaredMethod(Object obj, String name, Class<?>... args) throws NoSuchMethodException {
      Class<?> cls = obj.getClass();

      while(cls != null) {
         try {
            Method method = cls.getDeclaredMethod(name, args);
            if (!method.isAccessible()) {
               method.setAccessible(true);
            }

            return method;
         } catch (NoSuchMethodException var5) {
            cls = cls.getSuperclass();
         }
      }

      throw new NoSuchMethodException("Method " + name + " with parameters " + Arrays.asList(args) + " not found in " + obj.getClass());
   }
}
