package mirror;

import java.lang.reflect.Field;

public class RefLong {
   private Field field;

   public RefLong(Class cls, Field field) throws NoSuchFieldException {
      this.field = cls.getDeclaredField(field.getName());
      this.field.setAccessible(true);
   }

   public long get(Object object) {
      try {
         return this.field.getLong(object);
      } catch (Exception var3) {
         return 0L;
      }
   }

   public void set(Object obj, long value) {
      try {
         this.field.setLong(obj, value);
      } catch (Exception var5) {
      }

   }
}
