package com.swift.sandhook.utils;

import android.util.Log;
import com.swift.sandhook.HookLog;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class Unsafe {
   private static final String TAG = "Unsafe";
   private static Object unsafe;
   private static Class unsafeClass;
   private static Method arrayBaseOffsetMethod;
   private static Method arrayIndexScaleMethod;
   private static Method getIntMethod;
   private static Method getLongMethod;
   private static volatile boolean supported = false;
   private static Class objectArrayClass = Object[].class;

   public static boolean support() {
      return supported;
   }

   private Unsafe() {
   }

   public static int arrayBaseOffset(Class cls) {
      try {
         return (Integer)arrayBaseOffsetMethod.invoke(unsafe, cls);
      } catch (Exception var2) {
         return 0;
      }
   }

   public static int arrayIndexScale(Class cls) {
      try {
         return (Integer)arrayIndexScaleMethod.invoke(unsafe, cls);
      } catch (Exception var2) {
         return 0;
      }
   }

   public static int getInt(Object array, long offset) {
      try {
         return (Integer)getIntMethod.invoke(unsafe, array, offset);
      } catch (Exception var4) {
         return 0;
      }
   }

   public static long getLong(Object array, long offset) {
      try {
         return (Long)getLongMethod.invoke(unsafe, array, offset);
      } catch (Exception var4) {
         return 0L;
      }
   }

   public static long getObjectAddress(Object obj) {
      try {
         Object[] array = new Object[]{obj};
         return arrayIndexScale(objectArrayClass) == 8 ? getLong(array, (long)arrayBaseOffset(objectArrayClass)) : 4294967295L & (long)getInt(array, (long)arrayBaseOffset(objectArrayClass));
      } catch (Exception var2) {
         Exception e = var2;
         HookLog.e("get object address error", e);
         return -1L;
      }
   }

   static {
      try {
         unsafeClass = Class.forName("sun.misc.Unsafe");
         Field theUnsafe = unsafeClass.getDeclaredField("theUnsafe");
         theUnsafe.setAccessible(true);
         unsafe = theUnsafe.get((Object)null);
      } catch (Exception var4) {
         try {
            Field theUnsafe = unsafeClass.getDeclaredField("THE_ONE");
            theUnsafe.setAccessible(true);
            unsafe = theUnsafe.get((Object)null);
         } catch (Exception var3) {
            Log.w("Unsafe", "Unsafe not found o.O");
         }
      }

      if (unsafe != null) {
         try {
            arrayBaseOffsetMethod = unsafeClass.getDeclaredMethod("arrayBaseOffset", Class.class);
            arrayIndexScaleMethod = unsafeClass.getDeclaredMethod("arrayIndexScale", Class.class);
            getIntMethod = unsafeClass.getDeclaredMethod("getInt", Object.class, Long.TYPE);
            getLongMethod = unsafeClass.getDeclaredMethod("getLong", Object.class, Long.TYPE);
            supported = true;
         } catch (Exception var2) {
         }
      }

   }
}
