package mirror;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class RefConstructor<T> {
   private Constructor<?> ctor;

   public RefConstructor(Class<?> cls, Field field) throws NoSuchMethodException {
      if (field.isAnnotationPresent(MethodParams.class)) {
         Class<?>[] types = ((MethodParams)field.getAnnotation(MethodParams.class)).value();
         this.ctor = cls.getDeclaredConstructor(types);
      } else if (field.isAnnotationPresent(MethodReflectParams.class)) {
         String[] values = ((MethodReflectParams)field.getAnnotation(MethodReflectParams.class)).value();
         Class[] parameterTypes = new Class[values.length];
         int N = 0;

         while(N < values.length) {
            try {
               Class<?> type = RefStaticMethod.getProtoType(values[N]);
               if (type == null) {
                  type = Class.forName(values[N]);
               }

               parameterTypes[N] = type;
               ++N;
            } catch (Exception var7) {
               Exception e = var7;
               e.printStackTrace();
            }
         }

         this.ctor = cls.getDeclaredConstructor(parameterTypes);
      } else {
         this.ctor = cls.getDeclaredConstructor();
      }

      if (this.ctor != null && !this.ctor.isAccessible()) {
         this.ctor.setAccessible(true);
      }

   }

   public T newInstance() {
      try {
         return (T)this.ctor.newInstance();
      } catch (Exception var2) {
         return null;
      }
   }

   public T newInstance(Object... params) {
      try {
         return (T)this.ctor.newInstance(params);
      } catch (Exception var3) {
         return null;
      }
   }
}
