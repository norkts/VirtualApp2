package external.org.apache.commons.lang3.builder;

import external.org.apache.commons.lang3.ArrayUtils;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilder implements Builder<Integer> {
   private static final ThreadLocal<Set<IDKey>> REGISTRY = new ThreadLocal();
   private final int iConstant;
   private int iTotal = 0;

   static Set<IDKey> getRegistry() {
      return (Set)REGISTRY.get();
   }

   static boolean isRegistered(Object value) {
      Set<IDKey> registry = getRegistry();
      return registry != null && registry.contains(new IDKey(value));
   }

   private static void reflectionAppend(Object object, Class<?> clazz, HashCodeBuilder builder, boolean useTransients, String[] excludeFields) {
      if (!isRegistered(object)) {
         try {
            register(object);
            Field[] fields = clazz.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
            Field[] var6 = fields;
            int var7 = fields.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Field field = var6[var8];
               if (!ArrayUtils.contains(excludeFields, field.getName()) && field.getName().indexOf(36) == -1 && (useTransients || !Modifier.isTransient(field.getModifiers())) && !Modifier.isStatic(field.getModifiers())) {
                  try {
                     Object fieldValue = field.get(object);
                     builder.append(fieldValue);
                  } catch (IllegalAccessException var14) {
                     throw new InternalError("Unexpected IllegalAccessException");
                  }
               }
            }
         } finally {
            unregister(object);
         }

      }
   }

   public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object) {
      return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, false, (Class)null);
   }

   public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object, boolean testTransients) {
      return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, (Class)null);
   }

   public static <T> int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, T object, boolean testTransients, Class<? super T> reflectUpToClass, String... excludeFields) {
      if (object == null) {
         throw new IllegalArgumentException("The object to build a hash code for must not be null");
      } else {
         HashCodeBuilder builder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
         Class<?> clazz = object.getClass();
         reflectionAppend(object, clazz, builder, testTransients, excludeFields);

         while(clazz.getSuperclass() != null && clazz != reflectUpToClass) {
            clazz = clazz.getSuperclass();
            reflectionAppend(object, clazz, builder, testTransients, excludeFields);
         }

         return builder.toHashCode();
      }
   }

   public static int reflectionHashCode(Object object, boolean testTransients) {
      return reflectionHashCode(17, 37, object, testTransients, (Class)null);
   }

   public static int reflectionHashCode(Object object, Collection<String> excludeFields) {
      return reflectionHashCode(object, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
   }

   public static int reflectionHashCode(Object object, String... excludeFields) {
      return reflectionHashCode(17, 37, object, false, (Class)null, excludeFields);
   }

   static void register(Object value) {
      Class var1 = HashCodeBuilder.class;
      synchronized(HashCodeBuilder.class) {
         if (getRegistry() == null) {
            REGISTRY.set(new HashSet());
         }
      }

      getRegistry().add(new IDKey(value));
   }

   static void unregister(Object value) {
      Set<IDKey> registry = getRegistry();
      if (registry != null) {
         registry.remove(new IDKey(value));
         Class var2 = HashCodeBuilder.class;
         synchronized(HashCodeBuilder.class) {
            registry = getRegistry();
            if (registry != null && registry.isEmpty()) {
               REGISTRY.remove();
            }
         }
      }

   }

   public HashCodeBuilder() {
      this.iConstant = 37;
      this.iTotal = 17;
   }

   public HashCodeBuilder(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber) {
      if (initialNonZeroOddNumber == 0) {
         throw new IllegalArgumentException("HashCodeBuilder requires a non zero initial value");
      } else if (initialNonZeroOddNumber % 2 == 0) {
         throw new IllegalArgumentException("HashCodeBuilder requires an odd initial value");
      } else if (multiplierNonZeroOddNumber == 0) {
         throw new IllegalArgumentException("HashCodeBuilder requires a non zero multiplier");
      } else if (multiplierNonZeroOddNumber % 2 == 0) {
         throw new IllegalArgumentException("HashCodeBuilder requires an odd multiplier");
      } else {
         this.iConstant = multiplierNonZeroOddNumber;
         this.iTotal = initialNonZeroOddNumber;
      }
   }

   public HashCodeBuilder append(boolean value) {
      this.iTotal = this.iTotal * this.iConstant + (value ? 0 : 1);
      return this;
   }

   public HashCodeBuilder append(boolean[] array) {
      if (array == null) {
         this.iTotal *= this.iConstant;
      } else {
         boolean[] var2 = array;
         int var3 = array.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            boolean element = var2[var4];
            this.append(element);
         }
      }

      return this;
   }

   public HashCodeBuilder append(byte value) {
      this.iTotal = this.iTotal * this.iConstant + value;
      return this;
   }

   public HashCodeBuilder append(byte[] array) {
      if (array == null) {
         this.iTotal *= this.iConstant;
      } else {
         byte[] var2 = array;
         int var3 = array.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            byte element = var2[var4];
            this.append(element);
         }
      }

      return this;
   }

   public HashCodeBuilder append(char value) {
      this.iTotal = this.iTotal * this.iConstant + value;
      return this;
   }

   public HashCodeBuilder append(char[] array) {
      if (array == null) {
         this.iTotal *= this.iConstant;
      } else {
         char[] var2 = array;
         int var3 = array.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            char element = var2[var4];
            this.append(element);
         }
      }

      return this;
   }

   public HashCodeBuilder append(double value) {
      return this.append(Double.doubleToLongBits(value));
   }

   public HashCodeBuilder append(double[] array) {
      if (array == null) {
         this.iTotal *= this.iConstant;
      } else {
         double[] var2 = array;
         int var3 = array.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            double element = var2[var4];
            this.append(element);
         }
      }

      return this;
   }

   public HashCodeBuilder append(float value) {
      this.iTotal = this.iTotal * this.iConstant + Float.floatToIntBits(value);
      return this;
   }

   public HashCodeBuilder append(float[] array) {
      if (array == null) {
         this.iTotal *= this.iConstant;
      } else {
         float[] var2 = array;
         int var3 = array.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            float element = var2[var4];
            this.append(element);
         }
      }

      return this;
   }

   public HashCodeBuilder append(int value) {
      this.iTotal = this.iTotal * this.iConstant + value;
      return this;
   }

   public HashCodeBuilder append(int[] array) {
      if (array == null) {
         this.iTotal *= this.iConstant;
      } else {
         int[] var2 = array;
         int var3 = array.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            int element = var2[var4];
            this.append(element);
         }
      }

      return this;
   }

   public HashCodeBuilder append(long value) {
      this.iTotal = this.iTotal * this.iConstant + (int)(value ^ value >> 32);
      return this;
   }

   public HashCodeBuilder append(long[] array) {
      if (array == null) {
         this.iTotal *= this.iConstant;
      } else {
         long[] var2 = array;
         int var3 = array.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            long element = var2[var4];
            this.append(element);
         }
      }

      return this;
   }

   public HashCodeBuilder append(Object object) {
      if (object == null) {
         this.iTotal *= this.iConstant;
      } else if (object.getClass().isArray()) {
         if (object instanceof long[]) {
            this.append((long[])object);
         } else if (object instanceof int[]) {
            this.append((int[])object);
         } else if (object instanceof short[]) {
            this.append((short[])object);
         } else if (object instanceof char[]) {
            this.append((char[])object);
         } else if (object instanceof byte[]) {
            this.append((byte[])object);
         } else if (object instanceof double[]) {
            this.append((double[])object);
         } else if (object instanceof float[]) {
            this.append((float[])object);
         } else if (object instanceof boolean[]) {
            this.append((boolean[])object);
         } else {
            this.append((Object[])object);
         }
      } else {
         this.iTotal = this.iTotal * this.iConstant + object.hashCode();
      }

      return this;
   }

   public HashCodeBuilder append(Object[] array) {
      if (array == null) {
         this.iTotal *= this.iConstant;
      } else {
         Object[] var2 = array;
         int var3 = array.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Object element = var2[var4];
            this.append(element);
         }
      }

      return this;
   }

   public HashCodeBuilder append(short value) {
      this.iTotal = this.iTotal * this.iConstant + value;
      return this;
   }

   public HashCodeBuilder append(short[] array) {
      if (array == null) {
         this.iTotal *= this.iConstant;
      } else {
         short[] var2 = array;
         int var3 = array.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            short element = var2[var4];
            this.append(element);
         }
      }

      return this;
   }

   public HashCodeBuilder appendSuper(int superHashCode) {
      this.iTotal = this.iTotal * this.iConstant + superHashCode;
      return this;
   }

   public int toHashCode() {
      return this.iTotal;
   }

   public Integer build() {
      return this.toHashCode();
   }

   public int hashCode() {
      return this.toHashCode();
   }
}
