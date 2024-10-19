package com.lody.virtual.helper.utils;

import com.lody.virtual.helper.compat.ObjectsCompat;
import java.util.Arrays;

public class ArrayUtils {
   public static Object[] push(Object[] array, Object item) {
      Object[] longer = new Object[array.length + 1];
      System.arraycopy(array, 0, longer, 0, array.length);
      longer[array.length] = item;
      return longer;
   }

   public static <T> boolean contains(T[] array, T value) {
      return indexOf(array, value) != -1;
   }

   public static boolean contains(int[] array, int value) {
      if (array == null) {
         return false;
      } else {
         int[] var2 = array;
         int var3 = array.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            int element = var2[var4];
            if (element == value) {
               return true;
            }
         }

         return false;
      }
   }

   public static <T> int indexOf(T[] array, T value) {
      if (array == null) {
         return -1;
      } else {
         for(int i = 0; i < array.length; ++i) {
            if (ObjectsCompat.equals(array[i], value)) {
               return i;
            }
         }

         return -1;
      }
   }

   public static int protoIndexOf(Class<?>[] array, Class<?> type) {
      if (array == null) {
         return -1;
      } else {
         for(int i = 0; i < array.length; ++i) {
            if (array[i] == type) {
               return i;
            }
         }

         return -1;
      }
   }

   public static int indexOfFirst(Object[] array, Class<?> type) {
      if (!isEmpty(array)) {
         int N = -1;
         Object[] var3 = array;
         int var4 = array.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Object one = var3[var5];
            ++N;
            if (one != null && type == one.getClass()) {
               return N;
            }
         }
      }

      return -1;
   }

   public static int protoIndexOf(Class<?>[] array, Class<?> type, int sequence) {
      if (array == null) {
         return -1;
      } else {
         while(sequence < array.length) {
            if (type == array[sequence]) {
               return sequence;
            }

            ++sequence;
         }

         return -1;
      }
   }

   public static int indexOfObject(Object[] array, Class<?> type, int sequence) {
      if (array == null) {
         return -1;
      } else {
         while(sequence < array.length) {
            if (type.isInstance(array[sequence])) {
               return sequence;
            }

            ++sequence;
         }

         return -1;
      }
   }

   public static int indexOf(Object[] array, Class<?> type, int sequence) {
      if (!isEmpty(array)) {
         int N = -1;
         Object[] var4 = array;
         int var5 = array.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Object one = var4[var6];
            ++N;
            if (one != null && one.getClass() == type) {
               --sequence;
               if (sequence <= 0) {
                  return N;
               }
            }
         }
      }

      return -1;
   }

   public static int indexOfLast(Object[] array, Class<?> type) {
      if (!isEmpty(array)) {
         for(int N = array.length; N > 0; --N) {
            Object one = array[N - 1];
            if (one != null && one.getClass() == type) {
               return N - 1;
            }
         }
      }

      return -1;
   }

   public static <T> boolean isEmpty(T[] array) {
      return array == null || array.length == 0;
   }

   public static <T> T getFirst(Object[] args, Class<?> clazz) {
      int index = indexOfFirst(args, clazz);
      return index != -1 ? args[index] : null;
   }

   public static void checkOffsetAndCount(int arrayLength, int offset, int count) throws ArrayIndexOutOfBoundsException {
      if ((offset | count) < 0 || offset > arrayLength || arrayLength - offset < count) {
         throw new ArrayIndexOutOfBoundsException(offset);
      }
   }

   public static <T> T[] trimToSize(T[] array, int size) {
      if (array != null && size != 0) {
         return array.length == size ? array : Arrays.copyOf(array, size);
      } else {
         return null;
      }
   }
}
