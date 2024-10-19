package com.lody.virtual.client.hook.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodBox {
   public final Method method;
   public final Object who;
   public final Object[] args;

   public MethodBox(Method method, Object who, Object[] args) {
      this.method = method;
      this.who = who;
      this.args = args;
   }

   public <T> T call() throws InvocationTargetException {
      try {
         return this.method.invoke(this.who, this.args);
      } catch (IllegalAccessException var2) {
         IllegalAccessException e = var2;
         throw new RuntimeException(e);
      }
   }

   public <T> T callSafe() {
      try {
         return this.method.invoke(this.who, this.args);
      } catch (IllegalAccessException var2) {
         IllegalAccessException e = var2;
         e.printStackTrace();
      } catch (InvocationTargetException var3) {
         InvocationTargetException e = var3;
         e.printStackTrace();
      }

      return null;
   }
}
