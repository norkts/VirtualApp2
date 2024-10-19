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
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMaIAJpJwo7Iz42MW8VElJqARovLD0mVg=="));
      }
   }

   static class OverridePendingAppTransitionInPlace extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy0iM28gFi9iHjACKAcYPmwjMC1gDjw7IgcMO2AzNCxqHhoeKRY2KmQVHjNrNyhF"));
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
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy0iM28gFi9iHjACKAcYPmwjMC1gDjw7IgcMO2AzNCxqHhoeKRhSVg=="));
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
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy06M2omLCthJygzKi0YVg=="));
      }
   }
}
