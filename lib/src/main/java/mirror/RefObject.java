package mirror;

import java.lang.reflect.Field;

public class RefObject<T> {
   private Field field;

   public RefObject(Class<?> cls, Field field) throws NoSuchFieldException {
      this.field = cls.getDeclaredField(field.getName());
      this.field.setAccessible(true);
   }

   public T get(Object object) {
      try {
         return this.field.get(object);
      } catch (Exception var3) {
         Exception e = var3;
         e.printStackTrace();
         return null;
      }
   }

   public boolean set(Object obj, T value) {
      try {
         this.field.set(obj, value);
         return true;
      } catch (Exception var4) {
         Exception e = var4;
         e.printStackTrace();
         return false;
      }
   }
}
