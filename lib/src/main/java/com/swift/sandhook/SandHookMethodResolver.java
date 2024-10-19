package com.swift.sandhook;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public class SandHookMethodResolver {
   public static Field resolvedMethodsField;
   public static Field dexCacheField;
   public static Field dexMethodIndexField;
   public static Field artMethodField;
   public static Field fieldEntryPointFromCompiledCode;
   public static Field fieldEntryPointFromInterpreter;
   public static boolean canResolvedInJava = false;
   public static boolean isArtMethod = false;
   public static long resolvedMethodsAddress = 0L;
   public static int dexMethodIndex = 0;
   public static long entryPointFromCompiledCode = 0L;
   public static long entryPointFromInterpreter = 0L;
   public static Method testMethod;
   public static Object testArtMethod;

   public static void init() {
      testMethod = SandHook.testOffsetMethod1;
      checkSupport();
   }

   private static void checkSupport() {
      try {
         artMethodField = SandHook.getField(Method.class, "artMethod");
         testArtMethod = artMethodField.get(testMethod);
         if (SandHook.hasJavaArtMethod() && testArtMethod.getClass() == SandHook.artMethodClass) {
            checkSupportForArtMethod();
            isArtMethod = true;
         } else if (testArtMethod instanceof Long) {
            checkSupportForArtMethodId();
            isArtMethod = false;
         } else {
            canResolvedInJava = false;
         }
      } catch (Exception var1) {
      }

   }

   public static long getArtMethod(Member member) {
      if (artMethodField == null) {
         return 0L;
      } else {
         try {
            return (Long)artMethodField.get(member);
         } catch (IllegalAccessException var2) {
            return 0L;
         }
      }
   }

   private static void checkSupportForArtMethod() throws Exception {
      try {
         dexMethodIndexField = SandHook.getField(SandHook.artMethodClass, "dexMethodIndex");
      } catch (NoSuchFieldException var5) {
         dexMethodIndexField = SandHook.getField(SandHook.artMethodClass, "methodDexIndex");
      }

      dexCacheField = SandHook.getField(Class.class, "dexCache");
      Object dexCache = dexCacheField.get(testMethod.getDeclaringClass());
      resolvedMethodsField = SandHook.getField(dexCache.getClass(), "resolvedMethods");
      if (resolvedMethodsField.get(dexCache) instanceof Object[]) {
         canResolvedInJava = true;
      }

      try {
         try {
            dexMethodIndex = (Integer)dexMethodIndexField.get(testArtMethod);
         } catch (Throwable var3) {
         }

         try {
            fieldEntryPointFromCompiledCode = SandHook.getField(SandHook.artMethodClass, "entryPointFromQuickCompiledCode");
         } catch (Throwable var2) {
            fieldEntryPointFromCompiledCode = SandHook.getField(SandHook.artMethodClass, "entryPointFromCompiledCode");
         }

         if (fieldEntryPointFromCompiledCode.getType() == Integer.TYPE) {
            entryPointFromCompiledCode = (long)fieldEntryPointFromCompiledCode.getInt(testArtMethod);
         } else if (fieldEntryPointFromCompiledCode.getType() == Long.TYPE) {
            entryPointFromCompiledCode = fieldEntryPointFromCompiledCode.getLong(testArtMethod);
         }

         fieldEntryPointFromInterpreter = SandHook.getField(SandHook.artMethodClass, "entryPointFromInterpreter");
         if (fieldEntryPointFromInterpreter.getType() == Integer.TYPE) {
            entryPointFromInterpreter = (long)fieldEntryPointFromInterpreter.getInt(testArtMethod);
         } else if (fieldEntryPointFromCompiledCode.getType() == Long.TYPE) {
            entryPointFromInterpreter = fieldEntryPointFromInterpreter.getLong(testArtMethod);
         }
      } catch (Throwable var4) {
      }

   }

   private static void checkSupportForArtMethodId() throws Exception {
      dexMethodIndexField = SandHook.getField(Method.class, "dexMethodIndex");
      dexMethodIndex = (Integer)dexMethodIndexField.get(testMethod);
      dexCacheField = SandHook.getField(Class.class, "dexCache");
      Object dexCache = dexCacheField.get(testMethod.getDeclaringClass());
      resolvedMethodsField = SandHook.getField(dexCache.getClass(), "resolvedMethods");
      Object resolvedMethods = resolvedMethodsField.get(dexCache);
      if (resolvedMethods instanceof Long) {
         canResolvedInJava = false;
         resolvedMethodsAddress = (Long)resolvedMethods;
      } else if (resolvedMethods instanceof long[]) {
         canResolvedInJava = true;
      } else if (resolvedMethods instanceof int[]) {
         canResolvedInJava = true;
      }

   }

   public static void resolveMethod(Method hook, Method backup) {
      if (canResolvedInJava && artMethodField != null) {
         try {
            resolveInJava(hook, backup);
         } catch (Exception var3) {
            resolveInNative(hook, backup);
         }
      } else {
         resolveInNative(hook, backup);
      }

   }

   private static void resolveInJava(Method hook, Method backup) throws Exception {
      Object dexCache = dexCacheField.get(hook.getDeclaringClass());
      if (isArtMethod) {
         Object artMethod = artMethodField.get(backup);
         int dexMethodIndex = (Integer)dexMethodIndexField.get(artMethod);
         Object resolvedMethods = resolvedMethodsField.get(dexCache);
         ((Object[])resolvedMethods)[dexMethodIndex] = artMethod;
      } else {
         int dexMethodIndex = (Integer)dexMethodIndexField.get(backup);
         Object resolvedMethods = resolvedMethodsField.get(dexCache);
         if (resolvedMethods instanceof long[]) {
            long artMethod = (Long)artMethodField.get(backup);
            ((long[])resolvedMethods)[dexMethodIndex] = artMethod;
         } else {
            if (!(resolvedMethods instanceof int[])) {
               throw new UnsupportedOperationException("un support");
            }

            int artMethod = Long.valueOf((Long)artMethodField.get(backup)).intValue();
            ((int[])resolvedMethods)[dexMethodIndex] = artMethod;
         }
      }

   }

   private static void resolveInNative(Method hook, Method backup) {
      SandHook.ensureMethodCached(hook, backup);
   }
}
