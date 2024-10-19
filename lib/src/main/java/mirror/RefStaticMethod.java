package mirror;

import com.lody.virtual.StringFog;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RefStaticMethod<T> {
   private Method method;
   private String parent;
   private String name;

   public RefStaticMethod(Class<?> cls, Field field) throws NoSuchMethodException {
      this.name = field.getName();
      this.parent = cls.getName();
      int i;
      if (field.isAnnotationPresent(MethodParams.class)) {
         Class<?>[] types = ((MethodParams)field.getAnnotation(MethodParams.class)).value();

         for(i = 0; i < types.length; ++i) {
            Class<?> clazz = types[i];
            if (clazz.getClassLoader() == this.getClass().getClassLoader()) {
               try {
                  Class.forName(clazz.getName());
                  Class<?> realClass = (Class)clazz.getField("TYPE").get((Object)null);
                  types[i] = realClass;
               } catch (Throwable var13) {
                  Throwable e = var13;
                  throw new RuntimeException(e);
               }
            }
         }

         this.method = cls.getDeclaredMethod(field.getName(), types);
         this.method.setAccessible(true);
      } else if (field.isAnnotationPresent(MethodReflectParams.class)) {
         boolean arrayset = false;
         String[] typeNames = ((MethodReflectParams)field.getAnnotation(MethodReflectParams.class)).value();
         Class<?>[] types = new Class[typeNames.length];
         Class<?>[] types2 = new Class[typeNames.length];

         for(i = 0; i < typeNames.length; ++i) {
            Class<?> type = getProtoType(typeNames[i]);
            if (type == null) {
               try {
                  type = Class.forName(typeNames[i]);
               } catch (ClassNotFoundException var12) {
                  ClassNotFoundException e = var12;
                  e.printStackTrace();
               }
            }

            types[i] = type;
            if ("java.util.HashSet".equals(typeNames[i])) {
               arrayset = true;
               Class<?> type2 = type;

               try {
                  type2 = Class.forName("android.util.ArraySet");
               } catch (ClassNotFoundException var11) {
               }

               if (type2 != null) {
                  types2[i] = type2;
               } else {
                  types2[i] = type;
               }
            } else {
               types2[i] = type;
            }
         }

         try {
            this.method = cls.getDeclaredMethod(field.getName(), types);
         } catch (Exception var14) {
            var14.printStackTrace();
            if (arrayset) {
               this.method = cls.getDeclaredMethod(field.getName(), types2);
            }
         }

         this.method.setAccessible(true);
      } else {
         Method[] var16 = cls.getDeclaredMethods();
         i = var16.length;

         for(int var19 = 0; var19 < i; ++var19) {
            Method method = var16[var19];
            if (method.getName().equals(field.getName())) {
               this.method = method;
               this.method.setAccessible(true);
               break;
            }
         }
      }

      if (this.method == null) {
         throw new NoSuchMethodException(field.getName());
      }
   }

   static Class<?> getProtoType(String typeName) {
      if (typeName.equals("int")) {
         return Integer.TYPE;
      } else if (typeName.equals("long")) {
         return Long.TYPE;
      } else if (typeName.equals("boolean")) {
         return Boolean.TYPE;
      } else if (typeName.equals("byte")) {
         return Byte.TYPE;
      } else if (typeName.equals("short")) {
         return Short.TYPE;
      } else if (typeName.equals("char")) {
         return Character.TYPE;
      } else if (typeName.equals("float")) {
         return Float.TYPE;
      } else if (typeName.equals("double")) {
         return Double.TYPE;
      } else {
         return typeName.equals("void") ? Void.TYPE : null;
      }
   }

   public T call(Object... params) {
      T obj = null;

      try {
         obj = (T) this.method.invoke((Object)null, params);
      } catch (Exception var4) {
         Exception e = var4;
         e.printStackTrace();
      }

      return obj;
   }

   public T callWithException(Object... params) throws Throwable {
      try {
         return (T)this.method.invoke(null, params);
      } catch (InvocationTargetException var3) {
         InvocationTargetException e = var3;
         if (e.getCause() != null) {
            throw e.getCause();
         } else {
            throw e;
         }
      }
   }

   public String toString() {
      return "RefStaticMethod{" + this.parent + "@" + this.name + " find=" + (this.method != null) + '}';
   }
}
