package com.lody.virtual.client.hiddenapibypass;

import com.lody.virtual.StringFog;
import dalvik.system.VMRuntime;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleInfo;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import sun.misc.Unsafe;

public final class HiddenApiBypass {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBgYPGgFNCZlASQzJz4cKm4gAgM="));
   private static final Unsafe unsafe;
   private static final long artOffset;
   private static final long infoOffset;
   private static final long methodsOffset;
   private static final long memberOffset;
   private static final long size;
   private static final long bias;
   private static final Set<String> signaturePrefixes = new HashSet();

   public static List<Executable> getDeclaredMethods(Class<?> clazz) {
      ArrayList<Executable> list = new ArrayList();
      if (!clazz.isPrimitive() && !clazz.isArray()) {
         MethodHandle mh;
         try {
            mh = MethodHandles.lookup().unreflect(Helper.NeverCall.class.getDeclaredMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LghSVg=="))));
         } catch (IllegalAccessException | NoSuchMethodException var12) {
            return list;
         }

         long methods = unsafe.getLong(clazz, methodsOffset);
         int numMethods = unsafe.getInt(methods);

         for(int i = 0; i < numMethods; ++i) {
            long method = methods + (long)i * size + bias;
            unsafe.putLong(mh, artOffset, method);
            unsafe.putObject(mh, infoOffset, (Object)null);

            try {
               MethodHandles.lookup().revealDirect(mh);
            } catch (Throwable var11) {
            }

            MethodHandleInfo info = (MethodHandleInfo)unsafe.getObject(mh, infoOffset);
            Executable member = (Executable)unsafe.getObject(info, memberOffset);
            list.add(member);
         }

         return list;
      } else {
         return list;
      }
   }

   public static boolean setHiddenApiExemptions(String... signaturePrefixes) {
      List<Executable> methods = getDeclaredMethods(VMRuntime.class);
      Optional<Executable> getRuntime = methods.stream().filter((it) -> {
         return it.getName().equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcgNCZmHgY3KAhSVg==")));
      }).findFirst();
      Optional<Executable> setHiddenApiExemptions = methods.stream().filter((it) -> {
         return it.getName().equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLH0FAixiHjA2JwgmMWEgFitlDjw/IxgAKmEjSFo=")));
      }).findFirst();
      if (getRuntime.isPresent() && setHiddenApiExemptions.isPresent()) {
         ((Executable)getRuntime.get()).setAccessible(true);

         try {
            Object runtime = ((Method)getRuntime.get()).invoke((Object)null);
            ((Executable)setHiddenApiExemptions.get()).setAccessible(true);
            ((Method)setHiddenApiExemptions.get()).invoke(runtime, signaturePrefixes);
            return true;
         } catch (InvocationTargetException | IllegalAccessException var5) {
         }
      }

      return false;
   }

   public static boolean addHiddenApiExemptions(String... signaturePrefixes) {
      HiddenApiBypass.signaturePrefixes.addAll(Arrays.asList(signaturePrefixes));
      String[] strings = new String[HiddenApiBypass.signaturePrefixes.size()];
      HiddenApiBypass.signaturePrefixes.toArray(strings);
      return setHiddenApiExemptions(strings);
   }

   public static boolean clearHiddenApiExemptions() {
      signaturePrefixes.clear();
      return setHiddenApiExemptions();
   }

   static {
      try {
         unsafe = (Unsafe)Unsafe.class.getDeclaredMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQVBgN9Djw/"))).invoke((Object)null);

         assert unsafe != null;

         artOffset = unsafe.objectFieldOffset(Helper.MethodHandle.class.getDeclaredField(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcMLGAjAitgHgoLIztXPWUzFiVrEVRF"))));
         infoOffset = unsafe.objectFieldOffset(Helper.MethodHandleImpl.class.getDeclaredField(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcPmozSFo="))));
         methodsOffset = unsafe.objectFieldOffset(Helper.Class.class.getDeclaredField(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwguLGUFGixhJ1RF"))));
         memberOffset = unsafe.objectFieldOffset(Helper.HandleInfo.class.getDeclaredField(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwguDWsjNAQ="))));
         MethodHandle mhA = MethodHandles.lookup().unreflect(Helper.NeverCall.class.getDeclaredMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LghSVg=="))));
         MethodHandle mhB = MethodHandles.lookup().unreflect(Helper.NeverCall.class.getDeclaredMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj5SVg=="))));
         long aAddr = unsafe.getLong(mhA, artOffset);
         long bAddr = unsafe.getLong(mhB, artOffset);
         long aMethods = unsafe.getLong(Helper.NeverCall.class, methodsOffset);
         size = bAddr - aAddr;
         bias = aAddr - aMethods - size;
      } catch (ReflectiveOperationException var8) {
         ReflectiveOperationException e = var8;
         throw new ExceptionInInitializerError(e);
      }
   }
}
