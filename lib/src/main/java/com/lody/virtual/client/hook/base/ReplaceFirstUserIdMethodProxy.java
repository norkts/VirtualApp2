package com.lody.virtual.client.hook.base;

import java.lang.reflect.Method;

public class ReplaceFirstUserIdMethodProxy extends StaticMethodProxy {
   public ReplaceFirstUserIdMethodProxy(String name) {
      super(name);
   }

   public boolean beforeCall(Object who, Method method, Object... args) {
      replaceFirstUserId(args);
      return access$001(this, who, method, args);
   }

   // $FF: synthetic method
   static boolean access$001(ReplaceFirstUserIdMethodProxy x0, Object x1, Method x2, Object[] x3) {
      return x0.beforeCall(x1, x2, x3);
   }
}
