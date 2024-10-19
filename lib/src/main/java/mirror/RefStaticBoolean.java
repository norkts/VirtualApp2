package mirror;

import java.lang.reflect.Field;

public class RefStaticBoolean {
   private Field field;

   public RefStaticBoolean(Class<?> cls, Field field) throws NoSuchFieldException {
      this.field = cls.getDeclaredField(field.getName());
      this.field.setAccessible(true);
   }

   public boolean get() {
      try {
         return this.field.getBoolean((Object)null);
      } catch (Exception var2) {
         return false;
      }
   }

   public void set(boolean value) {
      try {
         this.field.setBoolean((Object)null, value);
      } catch (Exception var3) {
      }

   }
}
