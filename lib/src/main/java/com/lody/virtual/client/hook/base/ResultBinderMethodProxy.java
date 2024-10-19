package com.lody.virtual.client.hook.base;

import android.os.IInterface;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class ResultBinderMethodProxy extends AutoResultStaticMethodProxy {
   public ResultBinderMethodProxy(String name) {
      super(name);
   }

   public Object call(Object who, Method method, Object... args) throws Throwable {
      IInterface base = (IInterface)super.call(who, method, args);
      return this.newProxyInstance(base, this.createProxy(base));
   }

   public Object newProxyInstance(IInterface iInterface, InvocationHandler proxy) {
      return Proxy.newProxyInstance(iInterface.getClass().getClassLoader(), iInterface.getClass().getInterfaces(), proxy);
   }

   public abstract InvocationHandler createProxy(IInterface var1);
}
