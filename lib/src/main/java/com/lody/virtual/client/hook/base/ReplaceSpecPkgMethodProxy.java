package com.lody.virtual.client.hook.base;

import java.lang.reflect.Method;

public class ReplaceSpecPkgMethodProxy extends StaticMethodProxy {
   private int index;

   public ReplaceSpecPkgMethodProxy(String name, int index) {
      super(name);
      this.index = index;
   }

   public boolean beforeCall(Object who, Method method, Object... args) {
      if (args != null) {
         int i = this.index;
         if (i < 0) {
            i += args.length;
         }

         if (i >= 0 && i < args.length && args[i] instanceof String) {
            args[i] = getHostPkg();
         }
      }

      return super.beforeCall(who, method, args);
   }
}
