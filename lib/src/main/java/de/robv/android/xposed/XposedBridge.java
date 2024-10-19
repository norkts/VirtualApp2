package de.robv.android.xposed;

import android.annotation.SuppressLint;
import android.util.Log;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.xposedcompat.methodgen.DynamicBridge;
import com.swift.sandhook.xposedcompat.utils.DexLog;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class XposedBridge {
   public static final ClassLoader BOOTCLASSLOADER = XposedBridge.class.getClassLoader();
   public static final String TAG = "SandXposed";
   /** @deprecated */
   @Deprecated
   public static int XPOSED_BRIDGE_VERSION;
   static boolean isZygote = true;
   private static int runtime = 2;
   private static final int RUNTIME_DALVIK = 1;
   private static final int RUNTIME_ART = 2;
   public static boolean disableHooks = false;
   static long BOOT_START_TIME;
   private static final Object[] EMPTY_ARRAY = new Object[0];
   public static final Map<Member, CopyOnWriteSortedSet<XC_MethodHook>> sHookedMethodCallbacks = new HashMap();
   public static final CopyOnWriteSortedSet<XC_LoadPackage> sLoadedPackageCallbacks = new CopyOnWriteSortedSet();
   static final CopyOnWriteSortedSet<XC_InitPackageResources> sInitPackageResourcesCallbacks = new CopyOnWriteSortedSet();

   private XposedBridge() {
   }

   public static void main(String[] args) {
   }

   private static void initXResources() throws IOException {
   }

   @SuppressLint({"SetWorldReadable"})
   private static File ensureSuperDexFile(String clz, Class<?> realSuperClz, Class<?> topClz) throws IOException {
      return null;
   }

   public static int getXposedVersion() {
      return 90;
   }

   public static synchronized void log(String text) {
      if (DexLog.DEBUG) {
         Log.i("SandXposed", text);
      }

   }

   public static synchronized void log(Throwable t) {
      if (DexLog.DEBUG) {
         Log.e("SandXposed", Log.getStackTraceString(t));
      }

   }

   public static XC_MethodHook.Unhook hookMethod(Member hookMethod, XC_MethodHook callback) {
      if (!(hookMethod instanceof Method) && !(hookMethod instanceof Constructor)) {
         throw new IllegalArgumentException("Only methods and constructors can be hooked: " + hookMethod.toString());
      } else if (hookMethod.getDeclaringClass().isInterface()) {
         throw new IllegalArgumentException("Cannot hook interfaces: " + hookMethod.toString());
      } else if (Modifier.isAbstract(hookMethod.getModifiers())) {
         throw new IllegalArgumentException("Cannot hook abstract methods: " + hookMethod.toString());
      } else if (callback == null) {
         throw new IllegalArgumentException("callback should not be null!");
      } else {
         boolean newMethod = false;
         CopyOnWriteSortedSet callbacks;
         synchronized(sHookedMethodCallbacks) {
            callbacks = (CopyOnWriteSortedSet)sHookedMethodCallbacks.get(hookMethod);
            if (callbacks == null) {
               callbacks = new CopyOnWriteSortedSet();
               sHookedMethodCallbacks.put(hookMethod, callbacks);
               newMethod = true;
            }
         }

         callbacks.add(callback);
         if (newMethod) {
            Class<?> declaringClass = hookMethod.getDeclaringClass();
            int slot;
            Class[] parameterTypes;
            Class returnType;
            if (runtime == 2) {
               slot = 0;
               parameterTypes = null;
               returnType = null;
            } else if (hookMethod instanceof Method) {
               slot = XposedHelpers.getIntField(hookMethod, "slot");
               parameterTypes = ((Method)hookMethod).getParameterTypes();
               returnType = ((Method)hookMethod).getReturnType();
            } else {
               slot = XposedHelpers.getIntField(hookMethod, "slot");
               parameterTypes = ((Constructor)hookMethod).getParameterTypes();
               returnType = null;
            }

            AdditionalHookInfo additionalInfo = new AdditionalHookInfo(callbacks, parameterTypes, returnType);
            hookMethodNative(hookMethod, declaringClass, slot, additionalInfo);
         }

         Objects.requireNonNull(callback);
         return callback.new Unhook(hookMethod);
      }
   }

   /** @deprecated */
   @Deprecated
   public static void unhookMethod(Member hookMethod, XC_MethodHook callback) {
      CopyOnWriteSortedSet callbacks;
      synchronized(sHookedMethodCallbacks) {
         callbacks = (CopyOnWriteSortedSet)sHookedMethodCallbacks.get(hookMethod);
         if (callbacks == null) {
            return;
         }
      }

      callbacks.remove(callback);
   }

   public static Set<XC_MethodHook.Unhook> hookAllMethods(Class<?> hookClass, String methodName, XC_MethodHook callback) {
      Set<XC_MethodHook.Unhook> unhooks = new HashSet();
      Method[] var4 = hookClass.getDeclaredMethods();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Member method = var4[var6];
         if (method.getName().equals(methodName)) {
            unhooks.add(hookMethod(method, callback));
         }
      }

      return unhooks;
   }

   public static Set<XC_MethodHook.Unhook> hookAllConstructors(Class<?> hookClass, XC_MethodHook callback) {
      Set<XC_MethodHook.Unhook> unhooks = new HashSet();
      Constructor[] var3 = hookClass.getDeclaredConstructors();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Member constructor = var3[var5];
         unhooks.add(hookMethod(constructor, callback));
      }

      return unhooks;
   }

   public static void hookLoadPackage(XC_LoadPackage callback) {
      synchronized(sLoadedPackageCallbacks) {
         sLoadedPackageCallbacks.add(callback);
      }
   }

   public static void hookInitPackageResources(XC_InitPackageResources callback) {
   }

   private static synchronized void hookMethodNative(Member method, Class<?> declaringClass, int slot, Object additionalInfoObj) {
      DynamicBridge.hookMethod(method, (AdditionalHookInfo)additionalInfoObj);
   }

   public static Object invokeOriginalMethod(Member method, Object thisObject, Object[] args) throws NullPointerException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      try {
         return SandHook.callOriginMethod(method, thisObject, args);
      } catch (NullPointerException var4) {
         NullPointerException e = var4;
         throw e;
      } catch (IllegalAccessException var5) {
         IllegalAccessException e = var5;
         throw e;
      } catch (IllegalArgumentException var6) {
         IllegalArgumentException e = var6;
         throw e;
      } catch (InvocationTargetException var7) {
         InvocationTargetException e = var7;
         throw e;
      } catch (Throwable var8) {
         Throwable throwable = var8;
         throw new InvocationTargetException(throwable);
      }
   }

   public static class AdditionalHookInfo {
      public final CopyOnWriteSortedSet<XC_MethodHook> callbacks;
      public final Class<?>[] parameterTypes;
      public final Class<?> returnType;

      private AdditionalHookInfo(CopyOnWriteSortedSet<XC_MethodHook> callbacks, Class<?>[] parameterTypes, Class<?> returnType) {
         this.callbacks = callbacks;
         this.parameterTypes = parameterTypes;
         this.returnType = returnType;
      }

      // $FF: synthetic method
      AdditionalHookInfo(CopyOnWriteSortedSet x0, Class[] x1, Class x2, Object x3) {
         this(x0, x1, x2);
      }
   }

   public static final class CopyOnWriteSortedSet<E> {
      private transient volatile Object[] elements;

      public CopyOnWriteSortedSet() {
         this.elements = XposedBridge.EMPTY_ARRAY;
      }

      public synchronized boolean add(E e) {
         int index = this.indexOf(e);
         if (index >= 0) {
            return false;
         } else {
            Object[] newElements = new Object[this.elements.length + 1];
            System.arraycopy(this.elements, 0, newElements, 0, this.elements.length);
            newElements[this.elements.length] = e;
            Arrays.sort(newElements);
            this.elements = newElements;
            return true;
         }
      }

      public synchronized boolean remove(E e) {
         int index = this.indexOf(e);
         if (index == -1) {
            return false;
         } else {
            Object[] newElements = new Object[this.elements.length - 1];
            System.arraycopy(this.elements, 0, newElements, 0, index);
            System.arraycopy(this.elements, index + 1, newElements, index, this.elements.length - index - 1);
            this.elements = newElements;
            return true;
         }
      }

      private int indexOf(Object o) {
         for(int i = 0; i < this.elements.length; ++i) {
            if (o.equals(this.elements[i])) {
               return i;
            }
         }

         return -1;
      }

      public Object[] getSnapshot() {
         return this.elements;
      }
   }
}
