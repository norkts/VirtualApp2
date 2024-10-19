package com.lody.virtual.client.hook.base;

import java.lang.reflect.Method;

public class ReplaceFirstUserIdMethodProxy extends StaticMethodProxy {
   public ReplaceFirstUserIdMethodProxy(String name) {
      super(name);
   }

   public boolean beforeCall(Object who, Method method, Object... args) {
      replaceFirstUserId(args);
      return super.beforeCall(who, method, args);
   }

}
