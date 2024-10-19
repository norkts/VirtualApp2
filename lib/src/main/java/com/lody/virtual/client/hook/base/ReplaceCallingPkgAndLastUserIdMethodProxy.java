package com.lody.virtual.client.hook.base;

import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import java.lang.reflect.Method;

public class ReplaceCallingPkgAndLastUserIdMethodProxy extends StaticMethodProxy {
   public ReplaceCallingPkgAndLastUserIdMethodProxy(String name) {
      super(name);
   }

   public boolean beforeCall(Object who, Method method, Object... args) {
      replaceLastUserId(args);
      MethodParameterUtils.replaceFirstAppPkg(args);
      return super.beforeCall(who, method, args);
   }
}
