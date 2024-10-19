package com.lody.virtual.client.hook.base;

import android.text.TextUtils;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.annotations.LogInvocation;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.helper.utils.VLog;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MethodInvocationStub<T> {
   private static final String TAG = MethodInvocationStub.class.getSimpleName();
   private Map<String, MethodProxy> mInternalMethodProxies;
   private T mBaseInterface;
   private T mProxyInterface;
   private MethodProxy mDefaultProxy;
   private LogInvocation.Condition mInvocationLoggingCondition;

   public Map<String, MethodProxy> getAllHooks() {
      return this.mInternalMethodProxies;
   }

   public MethodInvocationStub(T baseInterface, Class<?>... proxyInterfaces) {
      this.mInternalMethodProxies = new HashMap();
      this.mInvocationLoggingCondition = LogInvocation.Condition.NEVER;
      this.mBaseInterface = baseInterface;
      if (baseInterface != null) {
         if (proxyInterfaces == null) {
            proxyInterfaces = MethodParameterUtils.getAllInterface(baseInterface.getClass());
         }

         this.mProxyInterface = Proxy.newProxyInstance(baseInterface.getClass().getClassLoader(), proxyInterfaces, new HookInvocationHandler());
      }

   }

   public LogInvocation.Condition getInvocationLoggingCondition() {
      return this.mInvocationLoggingCondition;
   }

   public void setInvocationLoggingCondition(LogInvocation.Condition invocationLoggingCondition) {
      this.mInvocationLoggingCondition = invocationLoggingCondition;
   }

   public MethodInvocationStub(T baseInterface) {
      this(baseInterface, (Class[])null);
   }

   public void copyMethodProxies(MethodInvocationStub from) {
      this.mInternalMethodProxies.putAll(from.getAllHooks());
   }

   public MethodProxy addMethodProxy(MethodProxy methodProxy) {
      if (methodProxy != null && !TextUtils.isEmpty(methodProxy.getMethodName())) {
         if (this.mInternalMethodProxies.containsKey(methodProxy.getMethodName())) {
            VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRhfM3sLRSVgJAE0OAgpCH43GgN6DTw0LD0tJH0KFi9uDjMpKj5bD3gVFj9oER03JQdeL24FGj1qDho0Iz42M3ojSFo=")), methodProxy.getMethodName(), methodProxy.getClass().getName());
            return methodProxy;
         }

         this.mInternalMethodProxies.put(methodProxy.getMethodName(), methodProxy);
      }

      return methodProxy;
   }

   public MethodProxy removeMethodProxy(String hookName) {
      return (MethodProxy)this.mInternalMethodProxies.remove(hookName);
   }

   public void removeMethodProxy(MethodProxy methodProxy) {
      if (methodProxy != null) {
         this.removeMethodProxy(methodProxy.getMethodName());
      }

   }

   public void removeAllMethodProxies() {
      this.mInternalMethodProxies.clear();
   }

   public <H extends MethodProxy> H getMethodProxy(String name) {
      H proxy = (MethodProxy)this.mInternalMethodProxies.get(name);
      return proxy == null ? this.mDefaultProxy : proxy;
   }

   public void setDefaultMethodProxy(MethodProxy proxy) {
      this.mDefaultProxy = proxy;
   }

   public T getProxyInterface() {
      return this.mProxyInterface;
   }

   public T getBaseInterface() {
      return this.mBaseInterface;
   }

   public int getMethodProxiesCount() {
      return this.mInternalMethodProxies.size();
   }

   public static String argToString(Object obj) {
      if (obj != null && obj.getClass().isArray()) {
         StringBuilder b = new StringBuilder();
         Object[] array = (Object[])obj;

         for(int j = 0; j < array.length; ++j) {
            Object e = array[j];
            b.append(e);
            if (j != array.length - 1) {
               b.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186Vg==")));
            }
         }

         return b.toString();
      } else {
         return String.valueOf(obj);
      }
   }

   public static String argsToString(Object[] a) {
      if (a == null) {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDmoFSFo="));
      } else {
         int iMax = a.length - 1;
         if (iMax == -1) {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("P14cVg=="));
         } else {
            StringBuilder b = new StringBuilder();
            b.append('<');
            int i = 0;

            while(true) {
               Object obj = a[i];
               b.append(argToString(obj));
               if (i == iMax) {
                  return b.append('>').toString();
               }

               b.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186Vg==")));
               ++i;
            }
         }
      }
   }

   private void dumpMethodProxies() {
      StringBuilder sb = new StringBuilder(50);
      sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PDpWMnUnTCJJMAUyOTkHCnxSIyJ6M1AhMypWLg==")));
      Iterator var2 = this.mInternalMethodProxies.values().iterator();

      while(var2.hasNext()) {
         MethodProxy proxy = (MethodProxy)var2.next();
         sb.append(proxy.getMethodName()).append("\n");
      }

      sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PDpWMnUnTCJJMAUyOTkHCnxSIyJ6M1AhMypWLg==")));
      VLog.e(TAG, sb.toString());
   }

   private class HookInvocationHandler implements InvocationHandler {
      private HookInvocationHandler() {
      }

      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         MethodProxy methodProxy = MethodInvocationStub.this.getMethodProxy(method.getName());
         boolean useProxy = VirtualCore.get().isStartup() && methodProxy != null && methodProxy.isEnable();
         boolean mightLog = MethodInvocationStub.this.mInvocationLoggingCondition != LogInvocation.Condition.NEVER || methodProxy != null && methodProxy.getInvocationLoggingCondition() != LogInvocation.Condition.NEVER;
         if (!VirtualCore.get().isVAppProcess()) {
            mightLog = false;
         }

         String argStr = null;
         Object res = null;
         Throwable exception = null;
         Throwable t;
         if (mightLog) {
            try {
               argStr = MethodInvocationStub.argsToString(args);
               argStr = argStr.substring(1, argStr.length() - 1);
            } catch (Throwable var20) {
               t = var20;
               argStr = "" + t.getMessage();
            }
         }

         boolean var19 = false;

         Object var23;
         try {
            var19 = true;
            if (useProxy && methodProxy.beforeCall(MethodInvocationStub.this.mBaseInterface, method, args)) {
               res = methodProxy.call(MethodInvocationStub.this.mBaseInterface, method, args);
               res = methodProxy.afterCall(MethodInvocationStub.this.mBaseInterface, method, args, res);
            } else {
               res = method.invoke(MethodInvocationStub.this.mBaseInterface, args);
            }

            var23 = res;
            var19 = false;
         } catch (Throwable var21) {
            t = var21;
            exception = t;
            if (exception instanceof InvocationTargetException && ((InvocationTargetException)exception).getTargetException() != null) {
               exception = ((InvocationTargetException)exception).getTargetException();
            }

            throw exception;
         } finally {
            if (var19) {
               if (mightLog) {
                  int logPriorityx = MethodInvocationStub.this.mInvocationLoggingCondition.getLogLevel(useProxy, exception != null);
                  if (methodProxy != null) {
                     logPriorityx = Math.max(logPriorityx, methodProxy.getInvocationLoggingCondition().getLogLevel(useProxy, exception != null));
                  }

                  if (logPriorityx >= 0) {
                     String retStringx;
                     if (exception != null) {
                        retStringx = exception.toString();
                     } else if (method.getReturnType().equals(Void.TYPE)) {
                        retStringx = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4ACWgFSFo="));
                     } else {
                        retStringx = MethodInvocationStub.argToString(res);
                     }

                     Log.println(logPriorityx, MethodInvocationStub.TAG, method.getDeclaringClass().getSimpleName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz5SVg==")) + method.getName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PBhSVg==")) + argStr + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PAQ5O34nIFo=")) + retStringx);
                  }
               }

            }
         }

         if (mightLog) {
            int logPriority = MethodInvocationStub.this.mInvocationLoggingCondition.getLogLevel(useProxy, exception != null);
            if (methodProxy != null) {
               logPriority = Math.max(logPriority, methodProxy.getInvocationLoggingCondition().getLogLevel(useProxy, exception != null));
            }

            if (logPriority >= 0) {
               String retString;
               if (exception != null) {
                  retString = exception.toString();
               } else if (method.getReturnType().equals(Void.TYPE)) {
                  retString = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4ACWgFSFo="));
               } else {
                  retString = MethodInvocationStub.argToString(res);
               }

               Log.println(logPriority, MethodInvocationStub.TAG, method.getDeclaringClass().getSimpleName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz5SVg==")) + method.getName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PBhSVg==")) + argStr + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PAQ5O34nIFo=")) + retString);
            }
         }

         return var23;
      }

      // $FF: synthetic method
      HookInvocationHandler(Object x1) {
         this();
      }
   }
}
