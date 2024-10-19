package com.swift.sandhook.utils;

import com.swift.sandhook.SandHook;

public class ParamWrapper {
   private static boolean is64Bit = SandHook.is64Bit();

   public static boolean support(Class objectType) {
      if (is64Bit) {
         return objectType != Float.TYPE && objectType != Double.TYPE;
      } else {
         return objectType != Float.TYPE && objectType != Double.TYPE && objectType != Long.TYPE;
      }
   }

   public static Object addressToObject(Class objectType, long address) {
      return is64Bit ? addressToObject64(objectType, address) : addressToObject32(objectType, (int)address);
   }

   public static Object addressToObject64(Class objectType, long address) {
      if (objectType == null) {
         return null;
      } else if (objectType.isPrimitive()) {
         if (objectType == Integer.TYPE) {
            return (int)address;
         } else if (objectType == Long.TYPE) {
            return address;
         } else if (objectType == Short.TYPE) {
            return (short)((int)address);
         } else if (objectType == Byte.TYPE) {
            return (byte)((int)address);
         } else if (objectType == Character.TYPE) {
            return (char)((int)address);
         } else if (objectType == Boolean.TYPE) {
            return address != 0L;
         } else {
            throw new RuntimeException("unknown type: " + objectType.toString());
         }
      } else {
         return SandHook.getObject(address);
      }
   }

   public static Object addressToObject32(Class objectType, int address) {
      if (objectType == null) {
         return null;
      } else if (objectType.isPrimitive()) {
         if (objectType == Integer.TYPE) {
            return address;
         } else if (objectType == Short.TYPE) {
            return (short)address;
         } else if (objectType == Byte.TYPE) {
            return (byte)address;
         } else if (objectType == Character.TYPE) {
            return (char)address;
         } else if (objectType == Boolean.TYPE) {
            return address != 0;
         } else {
            throw new RuntimeException("unknown type: " + objectType.toString());
         }
      } else {
         return SandHook.getObject((long)address);
      }
   }

   public static long objectToAddress(Class objectType, Object object) {
      return is64Bit ? objectToAddress64(objectType, object) : (long)objectToAddress32(objectType, object);
   }

   public static int objectToAddress32(Class objectType, Object object) {
      if (object == null) {
         return 0;
      } else if (objectType.isPrimitive()) {
         if (objectType == Integer.TYPE) {
            return (Integer)object;
         } else if (objectType == Short.TYPE) {
            return (Short)object;
         } else if (objectType == Byte.TYPE) {
            return (Byte)object;
         } else if (objectType == Character.TYPE) {
            return (Character)object;
         } else if (objectType == Boolean.TYPE) {
            return Boolean.TRUE.equals(object) ? 1 : 0;
         } else {
            throw new RuntimeException("unknown type: " + objectType.toString());
         }
      } else {
         return (int)SandHook.getObjectAddress(object);
      }
   }

   public static long objectToAddress64(Class objectType, Object object) {
      if (object == null) {
         return 0L;
      } else if (objectType.isPrimitive()) {
         if (objectType == Integer.TYPE) {
            return (long)(Integer)object;
         } else if (objectType == Long.TYPE) {
            return (Long)object;
         } else if (objectType == Short.TYPE) {
            return (long)(Short)object;
         } else if (objectType == Byte.TYPE) {
            return (long)(Byte)object;
         } else if (objectType == Character.TYPE) {
            return (long)(Character)object;
         } else if (objectType == Boolean.TYPE) {
            return Boolean.TRUE.equals(object) ? 1L : 0L;
         } else {
            throw new RuntimeException("unknown type: " + objectType.toString());
         }
      } else {
         return SandHook.getObjectAddress(object);
      }
   }
}
