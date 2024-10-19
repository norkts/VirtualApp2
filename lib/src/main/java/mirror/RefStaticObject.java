package mirror;

import java.lang.reflect.Field;

public class RefStaticObject<T> {
   private Field field;

   public RefStaticObject(Class<?> cls, Field field) throws NoSuchFieldException {
      this.field = cls.getDeclaredField(field.getName());
      this.field.setAccessible(true);
   }

   public Class<?> type() {
      return this.field.getType();
   }

   public T get() {
      T obj = null;

      try {
         obj = this.field.get((Object)null);
      } catch (Exception var3) {
      }

      return obj;
   }

   public void set(T obj) {
      try {
         this.field.set((Object)null, obj);
      } catch (Exception var3) {
      }

   }
}
