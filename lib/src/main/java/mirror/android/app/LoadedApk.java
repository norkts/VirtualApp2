package mirror.android.app;

import android.app.Application;
import android.app.IServiceConnection;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IIntentReceiver;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import java.lang.ref.WeakReference;
import mirror.MethodParams;
import mirror.RefBoolean;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;

public class LoadedApk {
   public static Class Class = RefClass.load(LoadedApk.class, StringFog.decrypt("EgsWBAoHO10CHwJeJQAPFwAWNxUF"));
   public static RefObject<ApplicationInfo> mApplicationInfo;
   @MethodParams({boolean.class, android.app.Instrumentation.class})
   public static RefMethod<Application> makeApplication;
   @MethodParams({ServiceConnection.class, Context.class, Handler.class, int.class})
   public static RefMethod<IServiceConnection> getServiceDispatcher;
   @MethodParams({Context.class, ServiceConnection.class})
   public static RefMethod<IServiceConnection> forgetServiceDispatcher;
   public static RefMethod<ClassLoader> getClassLoader;
   public static RefBoolean mSecurityViolation;
   public static RefObject<ClassLoader> mClassLoader;
   public static RefObject<Application> mApplication;

   public static class ServiceDispatcher {
      public static Class Class = RefClass.load(ServiceDispatcher.class, StringFog.decrypt("EgsWBAoHO10CHwJeJQAPFwAWNxUFeyAGHQQZCgoqGhYCFxENNxYR"));
      public static RefObject<ServiceConnection> mConnection;
      public static RefObject<Context> mContext;

      public static class InnerConnection {
         public static Class Class = RefClass.load(InnerConnection.class, StringFog.decrypt("EgsWBAoHO10CHwJeJQAPFwAWNxUFeyAGHQQZCgoqGhYCFxENNxYRSzseBwocMAocGAANKxoMAQ=="));
         public static RefObject<WeakReference> mDispatcher;
      }
   }

   public static class ReceiverDispatcher {
      public static Class Class = RefClass.load(ReceiverDispatcher.class, StringFog.decrypt("EgsWBAoHO10CHwJeJQAPFwAWNxUFeyEGDBcZHwocNwwBBgQaPBsGHQ=="));
      public static RefMethod<IInterface> getIIntentReceiver;
      public static RefObject<BroadcastReceiver> mReceiver;
      public static RefObject<IIntentReceiver> mIIntentReceiver;

      public static class InnerReceiver {
         public static Class Class = RefClass.load(InnerReceiver.class, StringFog.decrypt("EgsWBAoHO10CHwJeJQAPFwAWNxUFeyEGDBcZHwocNwwBBgQaPBsGHVY5BwELATcXFQAHKRYR"));
         public static RefObject<WeakReference> mDispatcher;
      }
   }
}
