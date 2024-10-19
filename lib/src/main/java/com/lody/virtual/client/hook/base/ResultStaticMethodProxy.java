package com.lody.virtual.client.hook.base;

import java.lang.reflect.Method;

public class ResultStaticMethodProxy extends StaticMethodProxy {
   Object mResult;

   public ResultStaticMethodProxy(String name, Object result) {
      super(name);
      this.mResult = result;
   }

   public Object getResult() {
      return this.mResult;
   }

   public Object call(Object who, Method method, Object... args) throws Throwable {
      return this.mResult;
   }
}
