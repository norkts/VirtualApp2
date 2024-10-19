package com.lody.virtual.client.hook.base;

import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import java.lang.reflect.Method;

public class ReplaceLastPkgMethodProxy extends StaticMethodProxy {
   public ReplaceLastPkgMethodProxy(String name) {
      super(name);
   }

   public boolean beforeCall(Object who, Method method, Object... args) {
      MethodParameterUtils.replaceLastAppPkg(args);
      return super.beforeCall(who, method, args);
   }
}
