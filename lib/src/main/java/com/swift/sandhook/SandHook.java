package com.swift.sandhook;

import com.swift.sandhook.annotation.HookMode;
import com.swift.sandhook.blacklist.HookBlackList;
import com.swift.sandhook.utils.ClassStatusUtils;
import com.swift.sandhook.utils.FileUtils;
import com.swift.sandhook.utils.ReflectionUtils;
import com.swift.sandhook.utils.Unsafe;
import com.swift.sandhook.wrapper.HookErrorException;
import com.swift.sandhook.wrapper.HookWrapper;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SandHook {
   static Map<Member, HookWrapper.HookEntity> globalHookEntityMap = new ConcurrentHashMap();
   static Map<Method, HookWrapper.HookEntity> globalBackupMap = new ConcurrentHashMap();
   private static HookModeCallBack hookModeCallBack;
   private static HookResultCallBack hookResultCallBack;
   public static Class artMethodClass;
   public static Field nativePeerField;
   public static Method testOffsetMethod1;
   public static Method testOffsetMethod2;
   public static Object testOffsetArtMethod1;
   public static Object testOffsetArtMethod2;
   public static int testAccessFlag;

   public static void setHookModeCallBack(HookModeCallBack hookModeCallBack) {
      SandHook.hookModeCallBack = hookModeCallBack;
   }

   public static void setHookResultCallBack(HookResultCallBack hookResultCallBack) {
      SandHook.hookResultCallBack = hookResultCallBack;
   }

   private static boolean init() {
      initTestOffset();
      initThreadPeer();
      SandHookMethodResolver.init();
      return initNative(SandHookConfig.SDK_INT, SandHookConfig.DEBUG);
   }

   private static void initThreadPeer() {
      try {
         nativePeerField = getField(Thread.class, "nativePeer");
      } catch (NoSuchFieldException var1) {
      }

   }

   public static void addHookClass(Class... hookWrapperClass) throws HookErrorException {
      HookWrapper.addHookClass(hookWrapperClass);
   }

   public static void addHookClass(ClassLoader classLoader, Class... hookWrapperClass) throws HookErrorException {
      HookWrapper.addHookClass(classLoader, hookWrapperClass);
   }

   public static synchronized void hook(HookWrapper.HookEntity entity) throws HookErrorException {
      if (entity == null) {
         throw new HookErrorException("null hook entity");
      } else {
         Member target = entity.target;
         Method hook = entity.hook;
         Method backup = entity.backup;
         if (target != null && hook != null) {
            if (globalHookEntityMap.containsKey(entity.target)) {
               throw new HookErrorException("method <" + entity.target.toString() + "> has been hooked!");
            } else if (HookBlackList.canNotHook(target)) {
               throw new HookErrorException("method <" + entity.target.toString() + "> can not hook, because of in blacklist!");
            } else if (SandHookConfig.delayHook && PendingHookHandler.canWork() && ClassStatusUtils.isStaticAndNoInited(entity.target)) {
               PendingHookHandler.addPendingHook(entity);
            } else {
               if (entity.initClass) {
                  resolveStaticMethod(target);
                  MakeInitializedClassVisibilyInitialized(getThreadId());
               }

               resolveStaticMethod(backup);
               if (backup != null && entity.resolveDexCache) {
                  SandHookMethodResolver.resolveMethod(hook, backup);
               }

               if (target instanceof Method) {
                  ((Method)target).setAccessible(true);
               }

               int mode = 0;
               if (hookModeCallBack != null) {
                  mode = hookModeCallBack.hookMode(target);
               }

               globalHookEntityMap.put(entity.target, entity);
               int res;
               if (mode != 0) {
                  res = hookMethod(target, hook, backup, mode);
               } else {
                  HookMode hookMode = (HookMode)hook.getAnnotation(HookMode.class);
                  res = hookMethod(target, hook, backup, hookMode == null ? 0 : hookMode.value());
               }

               if (res > 0 && backup != null) {
                  backup.setAccessible(true);
               }

               entity.hookMode = res;
               if (hookResultCallBack != null) {
                  hookResultCallBack.hookResult(res > 0, entity);
               }

               if (res < 0) {
                  globalHookEntityMap.remove(entity.target);
                  throw new HookErrorException("hook method <" + entity.target.toString() + "> error in native!");
               } else {
                  if (entity.backup != null) {
                     globalBackupMap.put(entity.backup, entity);
                  }

                  HookLog.d("method <" + entity.target.toString() + "> hook <" + (res == 1 ? "inline" : "replacement") + "> success!");
               }
            }
         } else {
            throw new HookErrorException("null input");
         }
      }
   }

   public static final Object callOriginMethod(Member originMethod, Object thiz, Object... args) throws Throwable {
      HookWrapper.HookEntity hookEntity = (HookWrapper.HookEntity)globalHookEntityMap.get(originMethod);
      return hookEntity != null && hookEntity.backup != null ? callOriginMethod(hookEntity.backupIsStub, originMethod, hookEntity.backup, thiz, args) : null;
   }

   public static final Object callOriginByBackup(Method backupMethod, Object thiz, Object... args) throws Throwable {
      HookWrapper.HookEntity hookEntity = (HookWrapper.HookEntity)globalBackupMap.get(backupMethod);
      return hookEntity == null ? null : callOriginMethod(hookEntity.backupIsStub, hookEntity.target, backupMethod, thiz, args);
   }

   public static final Object callOriginMethod(Member originMethod, Method backupMethod, Object thiz, Object[] args) throws Throwable {
      return callOriginMethod(true, originMethod, backupMethod, thiz, args);
   }

   public static final Object callOriginMethod(boolean backupIsStub, Member originMethod, Method backupMethod, Object thiz, Object[] args) throws Throwable {
      if (!backupIsStub && SandHookConfig.SDK_INT >= 24) {
         Class originClassHolder = originMethod.getDeclaringClass();
         ensureDeclareClass(originMethod, backupMethod);
      }

      InvocationTargetException throwable;
      if (Modifier.isStatic(originMethod.getModifiers())) {
         try {
            return backupMethod.invoke((Object)null, args);
         } catch (InvocationTargetException var7) {
            throwable = var7;
            if (throwable.getCause() != null) {
               throw throwable.getCause();
            } else {
               throw throwable;
            }
         }
      } else {
         try {
            return backupMethod.invoke(thiz, args);
         } catch (InvocationTargetException var6) {
            throwable = var6;
            if (throwable.getCause() != null) {
               throw throwable.getCause();
            } else {
               throw throwable;
            }
         }
      }
   }

   public static final void ensureBackupMethod(Method backupMethod) {
      if (SandHookConfig.SDK_INT >= 24) {
         HookWrapper.HookEntity entity = (HookWrapper.HookEntity)globalBackupMap.get(backupMethod);
         if (entity != null) {
            ensureDeclareClass(entity.target, backupMethod);
         }

      }
   }

   public static boolean resolveStaticMethod(Member method) {
      if (method == null) {
         return true;
      } else {
         try {
            if (method instanceof Method && Modifier.isStatic(method.getModifiers())) {
               ((Method)method).setAccessible(true);
               ((Method)method).invoke(new Object(), getFakeArgs((Method)method));
            }
         } catch (ExceptionInInitializerError var2) {
            return false;
         } catch (Throwable var3) {
         }

         return true;
      }
   }

   private static Object[] getFakeArgs(Method method) {
      Class[] pars = method.getParameterTypes();
      return pars != null && pars.length != 0 ? null : new Object[]{new Object()};
   }

   public static Object getObject(long address) {
      if (address == 0L) {
         return null;
      } else {
         long threadSelf = getThreadId();
         return getObjectNative(threadSelf, address);
      }
   }

   public static boolean canGetObjectAddress() {
      return Unsafe.support();
   }

   public static long getObjectAddress(Object object) {
      return Unsafe.getObjectAddress(object);
   }

   private static void initTestOffset() {
      ArtMethodSizeTest.method1();
      ArtMethodSizeTest.method2();

      try {
         testOffsetMethod1 = ArtMethodSizeTest.class.getDeclaredMethod("method1");
         testOffsetMethod2 = ArtMethodSizeTest.class.getDeclaredMethod("method2");
      } catch (NoSuchMethodException var1) {
         NoSuchMethodException e = var1;
         throw new RuntimeException("SandHook init error", e);
      }

      initTestAccessFlag();
   }

   private static void initTestAccessFlag() {
      Field fieldAccessFlags;
      if (hasJavaArtMethod()) {
         try {
            loadArtMethod();
            fieldAccessFlags = getField(artMethodClass, "accessFlags");
            testAccessFlag = (Integer)fieldAccessFlags.get(testOffsetArtMethod1);
         } catch (Exception var2) {
         }
      } else {
         try {
            fieldAccessFlags = getField(Method.class, "accessFlags");
            testAccessFlag = (Integer)fieldAccessFlags.get(testOffsetMethod1);
         } catch (Exception var1) {
         }
      }

   }

   private static void loadArtMethod() {
      try {
         Field fieldArtMethod = getField(Method.class, "artMethod");
         testOffsetArtMethod1 = fieldArtMethod.get(testOffsetMethod1);
         testOffsetArtMethod2 = fieldArtMethod.get(testOffsetMethod2);
      } catch (IllegalAccessException var1) {
         IllegalAccessException e = var1;
         e.printStackTrace();
      } catch (NoSuchFieldException var2) {
         NoSuchFieldException e = var2;
         e.printStackTrace();
      }

   }

   public static boolean hasJavaArtMethod() {
      if (SandHookConfig.SDK_INT >= 26) {
         return false;
      } else if (artMethodClass != null) {
         return true;
      } else {
         try {
            if (SandHookConfig.initClassLoader == null) {
               artMethodClass = Class.forName("java.lang.reflect.ArtMethod");
            } else {
               artMethodClass = Class.forName("java.lang.reflect.ArtMethod", true, SandHookConfig.initClassLoader);
            }

            return true;
         } catch (ClassNotFoundException var1) {
            return false;
         }
      }
   }

   public static Field getField(Class topClass, String fieldName) throws NoSuchFieldException {
      while(topClass != null && topClass != Object.class) {
         try {
            Field field = topClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
         } catch (Exception var3) {
            topClass = topClass.getSuperclass();
         }
      }

      throw new NoSuchFieldException(fieldName);
   }

   public static long getThreadId() {
      if (nativePeerField == null) {
         return 0L;
      } else {
         try {
            return nativePeerField.getType() == Integer.TYPE ? (long)nativePeerField.getInt(Thread.currentThread()) : nativePeerField.getLong(Thread.currentThread());
         } catch (IllegalAccessException var1) {
            return 0L;
         }
      }
   }

   public static Object getJavaMethod(String className, String methodName) {
      if (className == null) {
         return null;
      } else {
         Class clazz = null;

         try {
            clazz = Class.forName(className);
         } catch (ClassNotFoundException var5) {
            return null;
         }

         try {
            return clazz.getDeclaredMethod(methodName);
         } catch (NoSuchMethodException var4) {
            return null;
         }
      }
   }

   public static long getArtMethod(Member member) {
      return SandHookMethodResolver.getArtMethod(member);
   }

   public static boolean passApiCheck() {
      return ReflectionUtils.passApiCheck();
   }

   public static boolean tryDisableProfile(String selfPackageName) {
      if (SandHookConfig.SDK_INT < 24) {
         return false;
      } else {
         try {
            File profile = new File("/data/misc/profiles/cur/" + SandHookConfig.curUser + "/" + selfPackageName + "/primary.prof");
            if (!profile.getParentFile().exists()) {
               return false;
            } else {
               try {
                  profile.delete();
                  profile.createNewFile();
               } catch (Throwable var3) {
               }

               FileUtils.chmod(profile.getAbsolutePath(), 256);
               return true;
            }
         } catch (Throwable var4) {
            return false;
         }
      }
   }

   private static native boolean initNative(int var0, boolean var1);

   public static native void setHookMode(int var0);

   public static native void setInlineSafeCheck(boolean var0);

   public static native void skipAllSafeCheck(boolean var0);

   private static native int hookMethod(Member var0, Method var1, Method var2, int var3);

   public static native void ensureMethodCached(Method var0, Method var1);

   public static native void ensureDeclareClass(Member var0, Method var1);

   public static native boolean compileMethod(Member var0);

   public static native boolean deCompileMethod(Member var0, boolean var1);

   public static native boolean canGetObject();

   public static native Object getObjectNative(long var0, long var2);

   public static native boolean is64Bit();

   public static native boolean disableVMInline();

   public static native boolean disableDex2oatInline(boolean var0);

   public static native boolean setNativeEntry(Member var0, Member var1, long var2);

   public static native boolean initForPendingHook();

   public static native void MakeInitializedClassVisibilyInitialized(long var0);

   public static native void addPendingHookNative(Member var0);

   static {
      init();
   }

   @FunctionalInterface
   public interface HookResultCallBack {
      void hookResult(boolean var1, HookWrapper.HookEntity var2);
   }

   @FunctionalInterface
   public interface HookModeCallBack {
      int hookMode(Member var1);
   }
}
