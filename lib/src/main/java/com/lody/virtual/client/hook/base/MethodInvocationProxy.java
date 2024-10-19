package com.lody.virtual.client.hook.base;

import android.content.Context;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.annotations.LogInvocation;
import com.lody.virtual.client.hook.annotations.SkipInject;
import com.lody.virtual.client.interfaces.IInjector;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class MethodInvocationProxy<T extends MethodInvocationStub> implements IInjector {
   protected T mInvocationStub;

   public MethodInvocationProxy(T invocationStub) {
      this.mInvocationStub = invocationStub;
      this.onBindMethods();
      this.afterHookApply(invocationStub);
      LogInvocation loggingAnnotation = (LogInvocation)this.getClass().getAnnotation(LogInvocation.class);
      if (loggingAnnotation != null) {
         invocationStub.setInvocationLoggingCondition(loggingAnnotation.value());
      }

   }

   protected void onBindMethods() {
      if (this.mInvocationStub != null) {
         Class<? extends MethodInvocationProxy> clazz = this.getClass();
         Inject inject = (Inject)clazz.getAnnotation(Inject.class);
         if (inject != null) {
            Class<?> proxiesClass = inject.value();
            Class<?>[] innerClasses = proxiesClass.getDeclaredClasses();
            Class[] var5 = innerClasses;
            int var6 = innerClasses.length;

            int var7;
            for(var7 = 0; var7 < var6; ++var7) {
               Class<?> innerClass = var5[var7];
               if (!Modifier.isAbstract(innerClass.getModifiers()) && MethodProxy.class.isAssignableFrom(innerClass) && innerClass.getAnnotation(SkipInject.class) == null) {
                  this.addMethodProxy(innerClass);
               }
            }

            Method[] var9 = proxiesClass.getMethods();
            var6 = var9.length;

            for(var7 = 0; var7 < var6; ++var7) {
               Method method = var9[var7];
               if (Modifier.isStatic(method.getModifiers()) && method.getAnnotation(SkipInject.class) == null) {
                  this.addMethodProxy((MethodProxy)(new DirectCallMethodProxy(method)));
               }
            }
         }

      }
   }

   private void addMethodProxy(Class<?> hookType) {
      try {
         Constructor<?> constructor = hookType.getDeclaredConstructors()[0];
         if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
         }

         MethodProxy methodProxy;
         if (constructor.getParameterTypes().length == 0) {
            methodProxy = (MethodProxy)constructor.newInstance();
         } else {
            methodProxy = (MethodProxy)constructor.newInstance(this);
         }

         this.mInvocationStub.addMethodProxy(methodProxy);
      } catch (Throwable var4) {
         Throwable e = var4;
         throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcP2sjHitLEQo1PxccDmoKBjdlNzAgPQZfKWAgHSN7MCBF")) + hookType + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl5WOA==")) + e.getMessage());
      }
   }

   public MethodProxy addMethodProxy(MethodProxy methodProxy) {
      return this.mInvocationStub.addMethodProxy(methodProxy);
   }

   public void setDefaultMethodProxy(MethodProxy methodProxy) {
      this.mInvocationStub.setDefaultMethodProxy(methodProxy);
   }

   protected void afterHookApply(T delegate) {
   }

   public abstract void inject() throws Throwable;

   public Context getContext() {
      return VirtualCore.get().getContext();
   }

   public T getInvocationStub() {
      return this.mInvocationStub;
   }

   private static final class DirectCallMethodProxy extends StaticMethodProxy {
      private Method directCallMethod;

      public DirectCallMethodProxy(Method method) {
         super(method.getName());
         this.directCallMethod = method;
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return this.directCallMethod.invoke((Object)null, who, method, args);
      }
   }
}
