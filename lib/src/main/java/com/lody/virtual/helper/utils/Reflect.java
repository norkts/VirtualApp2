package com.lody.virtual.helper.utils;

import com.lody.virtual.StringFog;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Reflect {
   private final Object object;
   private final boolean isClass;

   private Reflect(Class<?> type) {
      this.object = type;
      this.isClass = true;
   }

   private Reflect(Object object) {
      this.object = object;
      this.isClass = false;
   }

   public static Reflect on(String name) throws ReflectException {
      return on(forName(name));
   }

   public static Reflect on(String name, ClassLoader classLoader) throws ReflectException {
      return on(forName(name, classLoader));
   }

   public static Reflect on(Class<?> clazz) {
      return new Reflect(clazz);
   }

   public static Reflect on(Object object) {
      return new Reflect(object);
   }

   public static <T extends AccessibleObject> T accessible(T accessible) {
      if (accessible == null) {
         return null;
      } else {
         if (accessible instanceof Member) {
            Member member = (Member)accessible;
            if (Modifier.isPublic(member.getModifiers()) && Modifier.isPublic(member.getDeclaringClass().getModifiers())) {
               return accessible;
            }
         }

         if (!accessible.isAccessible()) {
            accessible.setAccessible(true);
         }

         return accessible;
      }
   }

   private static String property(String string) {
      int length = string.length();
      if (length == 0) {
         return "";
      } else {
         return length == 1 ? string.toLowerCase() : string.substring(0, 1).toLowerCase() + string.substring(1);
      }
   }

   private static Reflect on(Constructor<?> constructor, Object... args) throws ReflectException {
      try {
         return on(((Constructor)accessible(constructor)).newInstance(args));
      } catch (Exception var3) {
         Exception e = var3;
         throw new ReflectException(e);
      }
   }

   private static Reflect on(Method method, Object object, Object... args) throws ReflectException {
      try {
         accessible(method);
         if (method.getReturnType() == Void.TYPE) {
            method.invoke(object, args);
            return on(object);
         } else {
            return on(method.invoke(object, args));
         }
      } catch (Exception var4) {
         Exception e = var4;
         throw new ReflectException(e);
      }
   }

   private static Object unwrap(Object object) {
      return object instanceof Reflect ? ((Reflect)object).get() : object;
   }

   private static Class<?>[] types(Object... values) {
      if (values == null) {
         return new Class[0];
      } else {
         Class<?>[] result = new Class[values.length];

         for(int i = 0; i < values.length; ++i) {
            Object value = values[i];
            result[i] = value == null ? NULL.class : value.getClass();
         }

         return result;
      }
   }

   private static Class<?> forName(String name) throws ReflectException {
      try {
         return Class.forName(name);
      } catch (Exception var2) {
         Exception e = var2;
         throw new ReflectException(e);
      }
   }

   private static Class<?> forName(String name, ClassLoader classLoader) throws ReflectException {
      try {
         return Class.forName(name, true, classLoader);
      } catch (Exception var3) {
         Exception e = var3;
         throw new ReflectException(e);
      }
   }

   public static Class<?> wrapper(Class<?> type) {
      if (type == null) {
         return null;
      } else {
         if (type.isPrimitive()) {
            if (Boolean.TYPE == type) {
               return Boolean.class;
            }

            if (Integer.TYPE == type) {
               return Integer.class;
            }

            if (Long.TYPE == type) {
               return Long.class;
            }

            if (Short.TYPE == type) {
               return Short.class;
            }

            if (Byte.TYPE == type) {
               return Byte.class;
            }

            if (Double.TYPE == type) {
               return Double.class;
            }

            if (Float.TYPE == type) {
               return Float.class;
            }

            if (Character.TYPE == type) {
               return Character.class;
            }

            if (Void.TYPE == type) {
               return Void.class;
            }
         }

         return type;
      }
   }

   public static Object defaultValue(Class<?> _type) {
      Class<?> type = wrapper(_type);
      if (type == null) {
         return null;
      } else {
         if (type.isPrimitive()) {
            if (Boolean.class == type) {
               return false;
            }

            if (Number.class.isAssignableFrom(type)) {
               return 0;
            }

            if (Character.class == type) {
               return '\u0000';
            }

            if (Void.class == type) {
               return null;
            }
         }

         return null;
      }
   }

   public <T> T get() {
      return this.object;
   }

   public Reflect set(String name, Object value) throws ReflectException {
      try {
         Field field = this.field0(name);
         field.setAccessible(true);
         field.set(this.object, unwrap(value));
         return this;
      } catch (Exception var4) {
         Exception e = var4;
         throw new ReflectException(e);
      }
   }

   public <T> T opt(String name) {
      try {
         return this.field(name).get();
      } catch (Exception var3) {
         Exception e = var3;
         e.printStackTrace();
         return null;
      }
   }

   public <T> T get(String name) throws ReflectException {
      return this.field(name).get();
   }

   public Reflect field(String name) throws ReflectException {
      try {
         Field field = this.field0(name);
         return on(field.get(this.object));
      } catch (Exception var3) {
         Exception e = var3;
         throw new ReflectException(this.object.getClass().getName(), e);
      }
   }

   private Field field0(String name) throws ReflectException {
      Class<?> type = this.type();

      try {
         return type.getField(name);
      } catch (NoSuchFieldException var6) {
         NoSuchFieldException e = var6;

         while(true) {
            try {
               return (Field)accessible(type.getDeclaredField(name));
            } catch (NoSuchFieldException var5) {
               type = type.getSuperclass();
               if (type == null) {
                  throw new ReflectException(e);
               }
            }
         }
      }
   }

   public Map<String, Reflect> fields() {
      Map<String, Reflect> result = new LinkedHashMap();
      Class<?> type = this.type();

      do {
         Field[] var3 = type.getDeclaredFields();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Field field = var3[var5];
            if (!this.isClass ^ Modifier.isStatic(field.getModifiers())) {
               String name = field.getName();
               if (!result.containsKey(name)) {
                  result.put(name, this.field(name));
               }
            }
         }

         type = type.getSuperclass();
      } while(type != null);

      return result;
   }

   public Reflect call(String name) throws ReflectException {
      return this.call(name);
   }

   public Reflect call(String name, Object... args) throws ReflectException {
      Class<?>[] types = types(args);

      try {
         Method method = this.exactMethod(name, types);
         return on(method, this.object, args);
      } catch (NoSuchMethodException var7) {
         try {
            Method method = this.similarMethod(name, types);
            return on(method, this.object, args);
         } catch (NoSuchMethodException var6) {
            NoSuchMethodException e1 = var6;
            throw new ReflectException(e1);
         }
      }
   }

   public Method exactMethod(String name, Class<?>[] types) throws NoSuchMethodException {
      Class<?> type = this.type();

      try {
         return type.getMethod(name, types);
      } catch (NoSuchMethodException var7) {
         while(true) {
            try {
               return type.getDeclaredMethod(name, types);
            } catch (NoSuchMethodException var6) {
               type = type.getSuperclass();
               if (type == null) {
                  throw new NoSuchMethodException();
               }
            }
         }
      }
   }

   private Method similarMethod(String name, Class<?>[] types) throws NoSuchMethodException {
      Class<?> type = this.type();
      Method[] var4 = type.getMethods();
      int var5 = var4.length;

      int var6;
      Method method;
      for(var6 = 0; var6 < var5; ++var6) {
         method = var4[var6];
         if (this.isSimilarSignature(method, name, types)) {
            return method;
         }
      }

      do {
         var4 = type.getDeclaredMethods();
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            method = var4[var6];
            if (this.isSimilarSignature(method, name, types)) {
               return method;
            }
         }

         type = type.getSuperclass();
      } while(type != null);

      throw new NoSuchMethodException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oz4fOG8zAiNjDlE7IzkmD2kgBiBlJy8r")) + name + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhcmCWwFRChhHiAqLwdXL34zSFo=")) + Arrays.toString(types) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg2D2wVHixLHiw/Pxc+DWUjMCx4EQYbPQcqM2EaLyM=")) + this.type() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz5SVg==")));
   }

   private boolean isSimilarSignature(Method possiblyMatchingMethod, String desiredMethodName, Class<?>[] desiredParamTypes) {
      return possiblyMatchingMethod.getName().equals(desiredMethodName) && this.match(possiblyMatchingMethod.getParameterTypes(), desiredParamTypes);
   }

   public Reflect create() throws ReflectException {
      return this.create();
   }

   public Reflect create(Object... args) throws ReflectException {
      Class<?>[] types = types(args);

      try {
         Constructor<?> constructor = this.type().getDeclaredConstructor(types);
         return on(constructor, args);
      } catch (NoSuchMethodException var8) {
         NoSuchMethodException e = var8;
         Constructor[] var4 = this.type().getDeclaredConstructors();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Constructor<?> constructor = var4[var6];
            if (this.match(constructor.getParameterTypes(), types)) {
               return on(constructor, args);
            }
         }

         throw new ReflectException(e);
      }
   }

   public <P> P as(Class<P> proxyType) {
      final boolean isMap = this.object instanceof Map;
      InvocationHandler handler = new InvocationHandler() {
         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName();

            try {
               return Reflect.on(Reflect.this.object).call(name, args).get();
            } catch (ReflectException var8) {
               ReflectException e = var8;
               if (isMap) {
                  Map<String, Object> map = (Map)Reflect.this.object;
                  int length = args == null ? 0 : args.length;
                  if (length == 0 && name.startsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLA==")))) {
                     return map.get(Reflect.property(name.substring(3)));
                  }

                  if (length == 0 && name.startsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2Vg==")))) {
                     return map.get(Reflect.property(name.substring(2)));
                  }

                  if (length == 1 && name.startsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLA==")))) {
                     map.put(Reflect.property(name.substring(3)), args[0]);
                     return null;
                  }
               }

               throw e;
            }
         }
      };
      return Proxy.newProxyInstance(proxyType.getClassLoader(), new Class[]{proxyType}, handler);
   }

   private boolean match(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
      if (declaredTypes.length == actualTypes.length) {
         for(int i = 0; i < actualTypes.length; ++i) {
            if (actualTypes[i] != NULL.class && !wrapper(declaredTypes[i]).isAssignableFrom(wrapper(actualTypes[i]))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.object.hashCode();
   }

   public boolean equals(Object obj) {
      return obj instanceof Reflect && this.object.equals(((Reflect)obj).get());
   }

   public String toString() {
      return this.object.toString();
   }

   public Class<?> type() {
      return this.isClass ? (Class)this.object : this.object.getClass();
   }

   public static String getMethodDetails(Method method) {
      StringBuilder sb = new StringBuilder(40);
      sb.append(Modifier.toString(method.getModifiers())).append(" ").append(method.getReturnType().getName()).append(" ").append(method.getName()).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PBhSVg==")));
      Class<?>[] parameters = method.getParameterTypes();
      Class[] var3 = parameters;
      int var4 = parameters.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Class<?> parameter = var3[var5];
         sb.append(parameter.getName()).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186Vg==")));
      }

      if (parameters.length > 0) {
         sb.delete(sb.length() - 2, sb.length());
      }

      sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PAhSVg==")));
      return sb.toString();
   }

   public Reflect callBest(String name, Object... args) throws ReflectException {
      Class<?>[] types = types(args);
      Class<?> type = this.type();
      Method bestMethod = null;
      int level = 0;
      Method[] var7 = type.getDeclaredMethods();
      int var8 = var7.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         Method method = var7[var9];
         if (this.isSimilarSignature(method, name, types)) {
            bestMethod = method;
            level = 2;
            break;
         }

         if (this.matchObjectMethod(method, name, types)) {
            bestMethod = method;
            level = 1;
         } else if (method.getName().equals(name) && method.getParameterTypes().length == 0 && level == 0) {
            bestMethod = method;
         }
      }

      if (bestMethod != null) {
         if (level == 0) {
            args = new Object[0];
         }

         if (level == 1) {
            Object[] args2 = new Object[]{args};
            args = args2;
         }

         return on(bestMethod, this.object, args);
      } else {
         throw new ReflectException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4fOGoVNAZjHh4wPxc+DWUjMCx4ESQcKSo6Vg==")) + name, new NoSuchMethodException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oz4fOGsjNANmVyQ3KAg2Mm8FASg=")) + name + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhcmCWwFRChhHiAqLwdXL34zSFo=")) + Arrays.toString(types) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg2D2wVHixLHiw/Pxc+DWUjMCx4EQYbPQcqM2EaLyM=")) + this.type() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz5SVg=="))));
      }
   }

   public void printFields() {
      if (this.object != null) {
         Map<String, Reflect> fields = this.fields();
         if (fields != null) {
            StringBuilder out = new StringBuilder();
            Iterator var3 = fields.entrySet().iterator();

            while(var3.hasNext()) {
               Map.Entry<String, Reflect> entry = (Map.Entry)var3.next();
               String name = (String)entry.getKey();
               Reflect reflect = (Reflect)entry.getValue();
               String value = reflect.object == null ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDmoFSFo=")) : reflect.object.toString();
               out.append(name + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl4HOA==")) + value);
               out.append('\n');
            }

            VLog.e(this.isClass ? ((Class)this.object).getSimpleName() : this.object.getClass().getSimpleName(), out.toString());
         }
      }
   }

   private boolean matchObjectMethod(Method possiblyMatchingMethod, String desiredMethodName, Class<?>[] desiredParamTypes) {
      return possiblyMatchingMethod.getName().equals(desiredMethodName) && this.matchObject(possiblyMatchingMethod.getParameterTypes());
   }

   private boolean matchObject(Class<?>[] parameterTypes) {
      Class<Object[]> c = Object[].class;
      return parameterTypes.length > 0 && parameterTypes[0].isAssignableFrom(c);
   }

   private static class NULL {
   }
}
