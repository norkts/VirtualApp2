package external.org.apache.commons.lang3.builder;

import external.org.apache.commons.lang3.ArrayUtils;
import external.org.apache.commons.lang3.tuple.Pair;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EqualsBuilder implements Builder<Boolean> {
   private static final ThreadLocal<Set<Pair<IDKey, IDKey>>> REGISTRY = new ThreadLocal();
   private boolean isEquals = true;

   static Set<Pair<IDKey, IDKey>> getRegistry() {
      return (Set)REGISTRY.get();
   }

   static Pair<IDKey, IDKey> getRegisterPair(Object lhs, Object rhs) {
      IDKey left = new IDKey(lhs);
      IDKey right = new IDKey(rhs);
      return Pair.of(left, right);
   }

   static boolean isRegistered(Object lhs, Object rhs) {
      Set<Pair<IDKey, IDKey>> registry = getRegistry();
      Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
      Pair<IDKey, IDKey> swappedPair = Pair.of((IDKey)pair.getLeft(), (IDKey)pair.getRight());
      return registry != null && (registry.contains(pair) || registry.contains(swappedPair));
   }

   static void register(Object lhs, Object rhs) {
      Class var2 = EqualsBuilder.class;
      synchronized(EqualsBuilder.class) {
         if (getRegistry() == null) {
            REGISTRY.set(new HashSet());
         }
      }

      Set<Pair<IDKey, IDKey>> registry = getRegistry();
      Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
      registry.add(pair);
   }

   static void unregister(Object lhs, Object rhs) {
      Set<Pair<IDKey, IDKey>> registry = getRegistry();
      if (registry != null) {
         Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
         registry.remove(pair);
         Class var4 = EqualsBuilder.class;
         synchronized(EqualsBuilder.class) {
            registry = getRegistry();
            if (registry != null && registry.isEmpty()) {
               REGISTRY.remove();
            }
         }
      }

   }

   public static boolean reflectionEquals(Object lhs, Object rhs, Collection<String> excludeFields) {
      return reflectionEquals(lhs, rhs, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
   }

   public static boolean reflectionEquals(Object lhs, Object rhs, String... excludeFields) {
      return reflectionEquals(lhs, rhs, false, (Class)null, excludeFields);
   }

   public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients) {
      return reflectionEquals(lhs, rhs, testTransients, (Class)null);
   }

   public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class<?> reflectUpToClass, String... excludeFields) {
      if (lhs == rhs) {
         return true;
      } else if (lhs != null && rhs != null) {
         Class<?> lhsClass = lhs.getClass();
         Class<?> rhsClass = rhs.getClass();
         Class testClass;
         if (lhsClass.isInstance(rhs)) {
            testClass = lhsClass;
            if (!rhsClass.isInstance(lhs)) {
               testClass = rhsClass;
            }
         } else {
            if (!rhsClass.isInstance(lhs)) {
               return false;
            }

            testClass = rhsClass;
            if (!lhsClass.isInstance(rhs)) {
               testClass = lhsClass;
            }
         }

         EqualsBuilder equalsBuilder = new EqualsBuilder();

         try {
            reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);

            while(testClass.getSuperclass() != null && testClass != reflectUpToClass) {
               testClass = testClass.getSuperclass();
               reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
            }
         } catch (IllegalArgumentException var10) {
            return false;
         }

         return equalsBuilder.isEquals();
      } else {
         return false;
      }
   }

   private static void reflectionAppend(Object lhs, Object rhs, Class<?> clazz, EqualsBuilder builder, boolean useTransients, String[] excludeFields) {
      if (!isRegistered(lhs, rhs)) {
         try {
            register(lhs, rhs);
            Field[] fields = clazz.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);

            for(int i = 0; i < fields.length && builder.isEquals; ++i) {
               Field f = fields[i];
               if (!ArrayUtils.contains(excludeFields, f.getName()) && f.getName().indexOf(36) == -1 && (useTransients || !Modifier.isTransient(f.getModifiers())) && !Modifier.isStatic(f.getModifiers())) {
                  try {
                     builder.append(f.get(lhs), f.get(rhs));
                  } catch (IllegalAccessException var13) {
                     throw new InternalError("Unexpected IllegalAccessException");
                  }
               }
            }
         } finally {
            unregister(lhs, rhs);
         }

      }
   }

   public EqualsBuilder appendSuper(boolean superEquals) {
      if (!this.isEquals) {
         return this;
      } else {
         this.isEquals = superEquals;
         return this;
      }
   }

   public EqualsBuilder append(Object lhs, Object rhs) {
      if (!this.isEquals) {
         return this;
      } else if (lhs == rhs) {
         return this;
      } else if (lhs != null && rhs != null) {
         Class<?> lhsClass = lhs.getClass();
         if (!lhsClass.isArray()) {
            this.isEquals = lhs.equals(rhs);
         } else if (lhs.getClass() != rhs.getClass()) {
            this.setEquals(false);
         } else if (lhs instanceof long[]) {
            this.append((long[])lhs, (long[])rhs);
         } else if (lhs instanceof int[]) {
            this.append((int[])lhs, (int[])rhs);
         } else if (lhs instanceof short[]) {
            this.append((short[])lhs, (short[])rhs);
         } else if (lhs instanceof char[]) {
            this.append((char[])lhs, (char[])rhs);
         } else if (lhs instanceof byte[]) {
            this.append((byte[])lhs, (byte[])rhs);
         } else if (lhs instanceof double[]) {
            this.append((double[])lhs, (double[])rhs);
         } else if (lhs instanceof float[]) {
            this.append((float[])lhs, (float[])rhs);
         } else if (lhs instanceof boolean[]) {
            this.append((boolean[])lhs, (boolean[])rhs);
         } else {
            this.append((Object[])lhs, (Object[])rhs);
         }

         return this;
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(long lhs, long rhs) {
      if (!this.isEquals) {
         return this;
      } else {
         this.isEquals = lhs == rhs;
         return this;
      }
   }

   public EqualsBuilder append(int lhs, int rhs) {
      if (!this.isEquals) {
         return this;
      } else {
         this.isEquals = lhs == rhs;
         return this;
      }
   }

   public EqualsBuilder append(short lhs, short rhs) {
      if (!this.isEquals) {
         return this;
      } else {
         this.isEquals = lhs == rhs;
         return this;
      }
   }

   public EqualsBuilder append(char lhs, char rhs) {
      if (!this.isEquals) {
         return this;
      } else {
         this.isEquals = lhs == rhs;
         return this;
      }
   }

   public EqualsBuilder append(byte lhs, byte rhs) {
      if (!this.isEquals) {
         return this;
      } else {
         this.isEquals = lhs == rhs;
         return this;
      }
   }

   public EqualsBuilder append(double lhs, double rhs) {
      return !this.isEquals ? this : this.append(Double.doubleToLongBits(lhs), Double.doubleToLongBits(rhs));
   }

   public EqualsBuilder append(float lhs, float rhs) {
      return !this.isEquals ? this : this.append(Float.floatToIntBits(lhs), Float.floatToIntBits(rhs));
   }

   public EqualsBuilder append(boolean lhs, boolean rhs) {
      if (!this.isEquals) {
         return this;
      } else {
         this.isEquals = lhs == rhs;
         return this;
      }
   }

   public EqualsBuilder append(Object[] lhs, Object[] rhs) {
      if (!this.isEquals) {
         return this;
      } else if (lhs == rhs) {
         return this;
      } else if (lhs != null && rhs != null) {
         if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int i = 0; i < lhs.length && this.isEquals; ++i) {
               this.append(lhs[i], rhs[i]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(long[] lhs, long[] rhs) {
      if (!this.isEquals) {
         return this;
      } else if (lhs == rhs) {
         return this;
      } else if (lhs != null && rhs != null) {
         if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int i = 0; i < lhs.length && this.isEquals; ++i) {
               this.append(lhs[i], rhs[i]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(int[] lhs, int[] rhs) {
      if (!this.isEquals) {
         return this;
      } else if (lhs == rhs) {
         return this;
      } else if (lhs != null && rhs != null) {
         if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int i = 0; i < lhs.length && this.isEquals; ++i) {
               this.append(lhs[i], rhs[i]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(short[] lhs, short[] rhs) {
      if (!this.isEquals) {
         return this;
      } else if (lhs == rhs) {
         return this;
      } else if (lhs != null && rhs != null) {
         if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int i = 0; i < lhs.length && this.isEquals; ++i) {
               this.append(lhs[i], rhs[i]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(char[] lhs, char[] rhs) {
      if (!this.isEquals) {
         return this;
      } else if (lhs == rhs) {
         return this;
      } else if (lhs != null && rhs != null) {
         if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int i = 0; i < lhs.length && this.isEquals; ++i) {
               this.append(lhs[i], rhs[i]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(byte[] lhs, byte[] rhs) {
      if (!this.isEquals) {
         return this;
      } else if (lhs == rhs) {
         return this;
      } else if (lhs != null && rhs != null) {
         if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int i = 0; i < lhs.length && this.isEquals; ++i) {
               this.append(lhs[i], rhs[i]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(double[] lhs, double[] rhs) {
      if (!this.isEquals) {
         return this;
      } else if (lhs == rhs) {
         return this;
      } else if (lhs != null && rhs != null) {
         if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int i = 0; i < lhs.length && this.isEquals; ++i) {
               this.append(lhs[i], rhs[i]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(float[] lhs, float[] rhs) {
      if (!this.isEquals) {
         return this;
      } else if (lhs == rhs) {
         return this;
      } else if (lhs != null && rhs != null) {
         if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int i = 0; i < lhs.length && this.isEquals; ++i) {
               this.append(lhs[i], rhs[i]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(boolean[] lhs, boolean[] rhs) {
      if (!this.isEquals) {
         return this;
      } else if (lhs == rhs) {
         return this;
      } else if (lhs != null && rhs != null) {
         if (lhs.length != rhs.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int i = 0; i < lhs.length && this.isEquals; ++i) {
               this.append(lhs[i], rhs[i]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public boolean isEquals() {
      return this.isEquals;
   }

   public Boolean build() {
      return this.isEquals();
   }

   protected void setEquals(boolean isEquals) {
      this.isEquals = isEquals;
   }

   public void reset() {
      this.isEquals = true;
   }
}
