package com.carlos.science.utils;

import com.carlos.libcommon.StringFog;
import com.carlos.science.exception.AgencyException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtil {
   public static <T> T newInstance(Class<T> cls) {
      T instance = null;

      try {
         Constructor constructor = cls.getDeclaredConstructor();
         constructor.setAccessible(true);
         instance = (T)constructor.newInstance();
      } catch (NoSuchMethodException var3) {
         NoSuchMethodException e = var3;
         e.printStackTrace();
      } catch (IllegalAccessException var4) {
         IllegalAccessException e = var4;
         e.printStackTrace();
      } catch (InstantiationException var5) {
         InstantiationException e = var5;
         e.printStackTrace();
      } catch (InvocationTargetException var6) {
         throw new AgencyException(StringFog.decrypt("NwpSGAoafxcMTwEEGw4AFABSGRULLRIXBh0eSQYAUxEaE0UNMB0QGwAFChsBAUs="));
      }

      return instance;
   }

   public static Object invokeMethod(Object targetObject, String methodName, Object[] params, Class[] paramTypes) {
      Object returnObj = null;
      if (targetObject != null && methodName != null && !methodName.isEmpty()) {
         Method method = null;
         Class cls = targetObject.getClass();

         while(cls != Object.class) {
            try {
               method = cls.getDeclaredMethod(methodName, paramTypes);
               break;
            } catch (Exception var9) {
               cls = cls.getSuperclass();
            }
         }

         if (method != null) {
            method.setAccessible(true);

            try {
               returnObj = method.invoke(targetObject, params);
            } catch (Exception var8) {
               Exception e = var8;
               e.printStackTrace();
            }
         }

         return returnObj;
      } else {
         return null;
      }
   }
}
