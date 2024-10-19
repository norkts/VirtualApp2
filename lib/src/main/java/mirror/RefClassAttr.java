package mirror;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class RefClassAttr {
   public static List<String> getClassField(Class clz) {
      Field[] declaredFields = clz.getDeclaredFields();
      List<String> fieldList = new ArrayList();
      Field[] var3 = declaredFields;
      int var4 = declaredFields.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         String fieldName = field.getName();
         fieldList.add(fieldName);
      }

      return fieldList;
   }

   public static String getFieldStringValueByObject(String fieldName, Object object) {
      Class<?> clz = object.getClass();

      try {
         Field field = clz.getField(fieldName);
         Class type = field.getType();
         Object value = field.get(object);
         if (value instanceof String) {
            return (String)value;
         } else {
            return !(value instanceof Integer) && !(value instanceof Double) && !(value instanceof Float) ? String.valueOf(value) : String.valueOf(value);
         }
      } catch (IllegalAccessException | NoSuchFieldException var6) {
         ReflectiveOperationException e = var6;
         throw new RuntimeException(e);
      }
   }
}
