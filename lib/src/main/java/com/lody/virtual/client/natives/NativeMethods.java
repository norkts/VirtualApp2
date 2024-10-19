package com.lody.virtual.client.natives;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Parcelable;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.NativeEngine;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.helper.compat.BuildCompat;
import dalvik.system.DexFile;
import java.lang.reflect.Method;

public class NativeMethods {
   public static int gCameraMethodType;
   public static Method gNativeMask;
   public static Method gCameraNativeSetup;
   public static Method gOpenDexFileNative;
   public static Method gAudioRecordNativeCheckPermission;
   public static Method gMediaRecorderNativeSetup;
   public static Method gAudioRecordNativeSetup;
   public static Method gNativeLoad;
   public static int gAudioRecordMethodType;

   @SuppressLint({"PrivateApi"})
   private static void init() {
      NoSuchMethodException e;
      try {
         gNativeMask = NativeEngine.class.getDeclaredMethod("nativeMark");
      } catch (NoSuchMethodException var7) {
         e = var7;
         e.printStackTrace();
      }

      if (BuildCompat.isR()) {
         try {
            gNativeLoad = Runtime.class.getDeclaredMethod("nativeLoad", String.class, ClassLoader.class, Class.class);
         } catch (NoSuchMethodException var6) {
            e = var6;
            e.printStackTrace();
         }
      }

      gMediaRecorderNativeSetup = getMediaRecorderNativeSetup();
      gAudioRecordNativeSetup = getAudioRecordNativeSetup();
      if (gAudioRecordNativeSetup != null && gAudioRecordNativeSetup.getParameterTypes().length == 10) {
         gAudioRecordMethodType = 2;
      } else {
         gAudioRecordMethodType = 1;
      }

      String methodName = VERSION.SDK_INT >= 19 ? "openDexFileNative" : "openDexFile";
      Method[] var1 = DexFile.class.getDeclaredMethods();
      int index = var1.length;

      int var3;
      for(var3 = 0; var3 < index; ++var3) {
         Method method = var1[var3];
         if (method.getName().equals(methodName)) {
            gOpenDexFileNative = method;
            break;
         }
      }

      if (gOpenDexFileNative == null) {
         throw new RuntimeException("Unable to find method : " + methodName);
      } else {
         gOpenDexFileNative.setAccessible(true);
         gCameraMethodType = -1;
         Method method = getCameraNativeSetup();
         if (method != null) {
            index = MethodParameterUtils.getParamsIndex(method.getParameterTypes(), String.class);
            gCameraNativeSetup = method;
            gCameraMethodType = 16 + index;
         }

         Method[] var10 = AudioRecord.class.getDeclaredMethods();
         var3 = var10.length;

         for(int var11 = 0; var11 < var3; ++var11) {
            Method mth = var10[var11];
            if (mth.getName().equals("native_check_permission") && mth.getParameterTypes().length == 1 && mth.getParameterTypes()[0] == String.class) {
               gAudioRecordNativeCheckPermission = mth;
               mth.setAccessible(true);
               break;
            }
         }

      }
   }

   private static Method getCameraNativeSetup() {
      Method[] methods = Camera.class.getDeclaredMethods();
      if (methods != null) {
         Method[] var1 = methods;
         int var2 = methods.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Method method = var1[var3];
            if ("native_setup".equals(method.getName())) {
               return method;
            }
         }
      }

      return null;
   }

   @SuppressLint({"PrivateApi"})
   private static Method getMediaRecorderNativeSetup() {
      Method native_setup = null;

      try {
         if (BuildCompat.isS()) {
            native_setup = MediaRecorder.class.getDeclaredMethod("native_setup", Object.class, String.class, Parcelable.class);
         } else {
            native_setup = MediaRecorder.class.getDeclaredMethod("native_setup", Object.class, String.class, String.class);
         }
      } catch (NoSuchMethodException var3) {
      }

      if (native_setup == null) {
         try {
            native_setup = MediaRecorder.class.getDeclaredMethod("native_setup", Object.class, String.class);
         } catch (NoSuchMethodException var2) {
         }
      }

      return native_setup;
   }

   @SuppressLint({"PrivateApi"})
   private static Method getAudioRecordNativeSetup() {
      Method native_setup = null;

      try {
         native_setup = AudioRecord.class.getDeclaredMethod("native_setup", Object.class, Object.class, int[].class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, int[].class, String.class, Long.TYPE);
      } catch (NoSuchMethodException var3) {
      }

      if (native_setup == null) {
         try {
            native_setup = AudioRecord.class.getDeclaredMethod("native_setup", Object.class, Object.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, int[].class, String.class);
         } catch (NoSuchMethodException var2) {
         }
      }

      return native_setup;
   }

   static {
      try {
         init();
      } catch (Throwable var1) {
         Throwable e = var1;
         e.printStackTrace();
      }

   }
}
