package mirror.android.app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.ProviderInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import mirror.MethodParams;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefMethod;
import mirror.RefObject;
import mirror.RefStaticInt;
import mirror.RefStaticMethod;
import mirror.RefStaticObject;

public class ActivityThread {
   public static Class<?> TYPE = RefClass.load(ActivityThread.class, "android.app.ActivityThread");
   public static RefStaticObject<Boolean> USE_CACHE;
   public static RefStaticMethod<Object> currentActivityThread;
   public static RefMethod<String> getProcessName;
   public static RefStaticMethod<String> currentPackageName;
   public static RefStaticMethod<Application> currentApplication;
   public static RefMethod<Handler> getHandler;
   public static RefMethod<Object> installProvider;
   public static RefObject<Object> mBoundApplication;
   public static RefObject<Handler> mH;
   public static RefObject<Application> mInitialApplication;
   public static RefObject<android.app.Instrumentation> mInstrumentation;
   public static RefObject<Map<String, WeakReference<?>>> mPackages;
   public static RefObject<Map<IBinder, Object>> mActivities;
   public static RefObject<Map> mProviderMap;
   @MethodParams({IBinder.class, List.class})
   public static RefMethod<Void> performNewIntents;
   public static RefStaticObject<IInterface> sPackageManager;
   public static RefStaticObject<IInterface> sPermissionManager;
   @MethodParams({IBinder.class, String.class, int.class, int.class, Intent.class})
   public static RefMethod<Void> sendActivityResult;
   public static RefMethod<IBinder> getApplicationThread;
   public static RefStaticMethod<IPackageManager> getPackageManager;
   public static RefMethod<Object> getLaunchingActivity;
   public static RefMethod<Object> getPackageInfoNoCheck;
   public static RefStaticMethod<IInterface> getPermissionManager;

   public static Object installProvider(Object mainThread, Context context, ProviderInfo providerInfo, Object holder) throws Throwable {
      return VERSION.SDK_INT <= 15 ? installProvider.callWithException(mainThread, context, holder, providerInfo, false, true) : installProvider.callWithException(mainThread, context, holder, providerInfo, false, true, true);
   }

   public static void handleNewIntent(Object obj, List list) {
      try {
         Object currentActivityThread = ActivityThread.currentActivityThread.call();
         Method declaredMethod;
         if (currentActivityThread != null && (declaredMethod = currentActivityThread.getClass().getDeclaredMethod("handleNewIntent", obj.getClass(), List.class)) != null) {
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(currentActivityThread, obj, list);
         }
      } catch (Exception var4) {
         Exception e = var4;
         e.printStackTrace();
      }

   }

   public static void USE_CACHE(boolean useCache) {
      RefStaticObject<Boolean> obj = USE_CACHE;
      if (obj != null) {
         obj.set(useCache);
      }

   }

   public static class H {
      public static Class<?> TYPE = RefClass.load(H.class, "android.app.ActivityThread$H");
      public static RefStaticInt LAUNCH_ACTIVITY;
      public static RefStaticInt EXECUTE_TRANSACTION;
      public static RefStaticInt SCHEDULE_CRASH;
   }

   public static class AppBindData {
      public static Class<?> TYPE = RefClass.load(AppBindData.class, "android.app.ActivityThread$AppBindData");
      public static RefObject<ApplicationInfo> appInfo;
      public static RefObject<Object> info;
      public static RefObject<String> processName;
      public static RefObject<ComponentName> instrumentationName;
      public static RefObject<List<ProviderInfo>> providers;
   }

   public static class ProviderKeyJBMR1 {
      public static Class<?> TYPE = RefClass.load(ProviderKeyJBMR1.class, "android.app.ActivityThread$ProviderKey");
      @MethodParams({String.class, int.class})
      public static RefConstructor<?> ctor;
   }

   public static class ProviderClientRecordJB {
      public static Class<?> TYPE = RefClass.load(ProviderClientRecordJB.class, "android.app.ActivityThread$ProviderClientRecord");
      public static RefObject<Object> mHolder;
      public static RefObject<IInterface> mProvider;
   }

   public static class ProviderClientRecord {
      public static Class<?> TYPE = RefClass.load(ProviderClientRecord.class, "android.app.ActivityThread$ProviderClientRecord");
      @MethodReflectParams({"android.app.ActivityThread", "java.lang.String", "android.content.IContentProvider", "android.content.ContentProvider"})
      public static RefConstructor<?> ctor;
      public static RefObject<String> mName;
      public static RefObject<IInterface> mProvider;
   }

   public static class ActivityClientRecord {
      public static Class<?> TYPE = RefClass.load(ActivityClientRecord.class, "android.app.ActivityThread$ActivityClientRecord");
      public static RefObject<android.app.Activity> activity;
      public static RefObject<ActivityInfo> activityInfo;
      public static RefObject<Intent> intent;
      public static RefObject<IBinder> token;
      public static RefObject<Boolean> isTopResumedActivity;
      public static RefObject<Object> packageInfo;
      public static RefObject<Object> compatInfo;
   }
}
