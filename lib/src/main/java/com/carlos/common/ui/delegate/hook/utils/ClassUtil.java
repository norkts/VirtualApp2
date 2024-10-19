package com.carlos.common.ui.delegate.hook.utils;

import com.carlos.libcommon.StringFog;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassUtil {
   private static final String TAG = "ClassUtil";

   public static void printMethodsInClass(String printTag, Class mClazz) {
      Method[] var2 = mClazz.getDeclaredMethods();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method method = var2[var4];
         String typeName = method.getReturnType().getSimpleName();
         String canonicalName = method.getReturnType().getCanonicalName();
         String methodName = method.getName();
         Class<?>[] methodParameterTypes = method.getParameterTypes();
         String types = "";
         Class[] var11 = methodParameterTypes;
         int var12 = methodParameterTypes.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            Class clazz = var11[var13];
            types = types + clazz.getName() + ",";
         }

         LogUtil.d(TAG, printTag + " methodName=" + methodName + "，typeName=" + typeName + ",canonicalName=" + canonicalName + "，返回type=(" + types + ")");
         method.setAccessible(true);
      }

   }

   private static void printFieldsInClass(String printTag, Class mClazz) {
      Field[] var2 = mClazz.getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];
         String fieldName = field.getName();
         field.setAccessible(true);

         try {
            LogUtil.d(TAG, printTag + " fieldName=" + fieldName);
         } catch (Throwable var8) {
            Throwable e = var8;
            e.printStackTrace();
         }
      }

   }

   public static void printFieldsInClassAndObject(String printTag, Class mClazz, Object object) {
      Field[] var3 = mClazz.getDeclaredFields();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         String fieldName = field.getName();
         field.setAccessible(true);

         try {
            LogUtil.d(TAG, printTag + " fieldName=" + fieldName + ",值是" + field.get(object));
         } catch (Throwable var9) {
            Throwable e = var9;
            e.printStackTrace();
         }
      }

   }
}
