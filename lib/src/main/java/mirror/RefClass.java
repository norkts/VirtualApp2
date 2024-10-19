package mirror;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public final class RefClass {
   private static HashMap<Class<?>, Constructor<?>> REF_TYPES = new HashMap();

   public static Class<?> load(Class<?> mappingClass, String className) {
      try {
         return load(mappingClass, Class.forName(className));
      } catch (Exception var3) {
         return null;
      }
   }

   public static Class load(Class mappingClass, Class<?> realClass) {
      Field[] fields = mappingClass.getDeclaredFields();
      Field[] var3 = fields;
      int var4 = fields.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];

         try {
            if (Modifier.isStatic(field.getModifiers())) {
               Constructor<?> constructor = (Constructor)REF_TYPES.get(field.getType());
               if (constructor != null) {
                  field.set((Object)null, constructor.newInstance(realClass, field));
               }
            }
         } catch (Exception var8) {
         }
      }

      return realClass;
   }

   static {
      try {
         REF_TYPES.put(RefObject.class, RefObject.class.getConstructor(Class.class, Field.class));
         REF_TYPES.put(RefMethod.class, RefMethod.class.getConstructor(Class.class, Field.class));
         REF_TYPES.put(RefInt.class, RefInt.class.getConstructor(Class.class, Field.class));
         REF_TYPES.put(RefLong.class, RefLong.class.getConstructor(Class.class, Field.class));
         REF_TYPES.put(RefFloat.class, RefFloat.class.getConstructor(Class.class, Field.class));
         REF_TYPES.put(RefDouble.class, RefDouble.class.getConstructor(Class.class, Field.class));
         REF_TYPES.put(RefBoolean.class, RefBoolean.class.getConstructor(Class.class, Field.class));
         REF_TYPES.put(RefStaticObject.class, RefStaticObject.class.getConstructor(Class.class, Field.class));
         REF_TYPES.put(RefStaticInt.class, RefStaticInt.class.getConstructor(Class.class, Field.class));
         REF_TYPES.put(RefStaticMethod.class, RefStaticMethod.class.getConstructor(Class.class, Field.class));
         REF_TYPES.put(RefConstructor.class, RefConstructor.class.getConstructor(Class.class, Field.class));
      } catch (Exception var1) {
         Exception e = var1;
         e.printStackTrace();
      }

   }
}
