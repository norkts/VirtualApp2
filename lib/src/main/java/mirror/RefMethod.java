package mirror;

import com.lody.virtual.StringFog;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RefMethod<T> {
   private Method method;

   public RefMethod(Class<?> cls, Field field) throws NoSuchMethodException {
      int i;
      Class type;
      if (field.isAnnotationPresent(MethodParams.class)) {
         Class<?>[] types = ((MethodParams)field.getAnnotation(MethodParams.class)).value();

         for(i = 0; i < types.length; ++i) {
            Class<?> clazz = types[i];
            if (clazz.getClassLoader() == this.getClass().getClassLoader()) {
               try {
                  Class.forName(clazz.getName());
                  type = (Class)clazz.getField(StringFog.decrypt("JzwiMw==")).get((Object)null);
                  types[i] = type;
               } catch (Throwable var9) {
                  Throwable e = var9;
                  throw new RuntimeException(e);
               }
            }
         }

         this.method = cls.getDeclaredMethod(field.getName(), types);
         this.method.setAccessible(true);
      } else {
         if (field.isAnnotationPresent(MethodReflectParams.class)) {
            String[] typeNames = ((MethodReflectParams)field.getAnnotation(MethodReflectParams.class)).value();
            Class<?>[] types = new Class[typeNames.length];

            for(i = 0; i < typeNames.length; ++i) {
               type = RefStaticMethod.getProtoType(typeNames[i]);
               if (type == null) {
                  try {
                     type = Class.forName(typeNames[i]);
                  } catch (ClassNotFoundException var8) {
                     ClassNotFoundException e = var8;
                     e.printStackTrace();
                  }
               }

               types[i] = type;
            }

            this.method = cls.getDeclaredMethod(field.getName(), types);
            this.method.setAccessible(true);
         } else {
            Method[] var11 = cls.getDeclaredMethods();
            i = var11.length;

            for(i = 0; i < i; ++i) {
               Method method = var11[i];
               if (method.getName().equals(field.getName())) {
                  this.method = method;
                  this.method.setAccessible(true);
                  break;
               }
            }
         }
      }

      if (this.method == null) {
         throw new NoSuchMethodException(field.getName());
      }
   }

   public T call(Object receiver, Object... args) {
      try {
         return (T)this.method.invoke(receiver, args);
      } catch (InvocationTargetException var4) {
         InvocationTargetException e = var4;
         if (e.getCause() != null) {
            e.getCause().printStackTrace();
         } else {
            e.printStackTrace();
         }
      } catch (Throwable var5) {
         Throwable e = var5;
         e.printStackTrace();
      }

      return null;
   }

   public T callWithException(Object receiver, Object... args) throws Throwable {
      try {
         return (T)this.method.invoke(receiver, args);
      } catch (InvocationTargetException var4) {
         InvocationTargetException e = var4;
         if (e.getCause() != null) {
            throw e.getCause();
         } else {
            throw e;
         }
      }
   }

   public Class<?>[] paramList() {
      return this.method.getParameterTypes();
   }
}
