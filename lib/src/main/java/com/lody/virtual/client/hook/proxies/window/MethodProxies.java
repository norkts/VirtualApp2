package com.lody.virtual.client.hook.proxies.window;

import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.proxies.window.session.WindowSessionPatch;
import java.lang.reflect.Method;

class MethodProxies {
   abstract static class BasePatchSession extends MethodProxy {
      public Object call(Object who, Method method, Object... args) throws Throwable {
         Object session = method.invoke(who, args);
         return session instanceof IInterface ? this.proxySession((IInterface)session) : session;
      }

      private Object proxySession(IInterface session) {
         WindowSessionPatch windowSessionPatch = new WindowSessionPatch(session);
         return windowSessionPatch.getInvocationStub().getProxyInterface();
      }
   }

   static class SetAppStartingWindow extends BasePatchSession {
      public String getMethodName() {
         return "setAppStartingWindow";
      }
   }

   static class OverridePendingAppTransitionInPlace extends MethodProxy {
      public String getMethodName() {
         return "overridePendingAppTransitionInPlace";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (args[0] instanceof String) {
            args[0] = getHostPkg();
         }

         return method.invoke(who, args);
      }
   }

   static class OverridePendingAppTransition extends BasePatchSession {
      public String getMethodName() {
         return "overridePendingAppTransition";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (args[0] instanceof String) {
            args[0] = getHostPkg();
         }

         return super.call(who, method, args);
      }
   }

   static class OpenSession extends BasePatchSession {
      public String getMethodName() {
         return "openSession";
      }
   }
}
