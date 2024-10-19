package com.lody.virtual.client.hook.base;

import com.lody.virtual.helper.utils.Reflect;
import java.lang.reflect.Method;

public class AutoResultStaticMethodProxy extends StaticMethodProxy {
   public AutoResultStaticMethodProxy(String name) {
      super(name);
   }

   public Object call(Object who, Method method, Object... args) throws Throwable {
      return this.getDefaultValue(who, method, args);
   }

   public Object getDefaultValue(Object who, Method method, Object... args) {
      Class<?> type = Reflect.wrapper(method.getReturnType());
      if (type == null) {
         return 0;
      } else {
         if (type.isPrimitive()) {
            if (Boolean.class == type) {
               return false;
            }

            if (Integer.class == type) {
               return 0;
            }

            if (Long.class == type) {
               return 0L;
            }

            if (Short.class == type) {
               return Short.valueOf((short)0);
            }

            if (Byte.class == type) {
               return 0;
            }

            if (Double.class == type) {
               return 0.0;
            }

            if (Float.class == type) {
               return 0.0F;
            }

            if (Character.class == type) {
               return '\u0000';
            }
         }

         return null;
      }
   }
}
