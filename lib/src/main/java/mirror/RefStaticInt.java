package mirror;

import java.lang.reflect.Field;

public class RefStaticInt {
   private Field field;

   public RefStaticInt(Class<?> cls, Field field) throws NoSuchFieldException {
      this.field = cls.getDeclaredField(field.getName());
      this.field.setAccessible(true);
   }

   public int get() {
      try {
         return this.field.getInt((Object)null);
      } catch (Exception var2) {
         return 0;
      }
   }

   public void set(int value) {
      try {
         this.field.setInt((Object)null, value);
      } catch (Exception var3) {
      }

   }
}
