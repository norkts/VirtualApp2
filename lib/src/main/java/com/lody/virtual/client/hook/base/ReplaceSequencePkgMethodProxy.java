package com.lody.virtual.client.hook.base;

import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import java.lang.reflect.Method;

public class ReplaceSequencePkgMethodProxy extends StaticMethodProxy {
   private int sequence;

   public ReplaceSequencePkgMethodProxy(String name, int sequence) {
      super(name);
      this.sequence = sequence;
   }

   public boolean beforeCall(Object who, Method method, Object... args) {
      MethodParameterUtils.replaceSequenceAppPkg(args, this.sequence);
      return super.beforeCall(who, method, args);
   }
}
