package com.carlos.common.imagepicker.util;

import android.annotation.TargetApi;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.os.Build.VERSION;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import javax.microedition.khronos.egl.EGL10;

public class EglUtils {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQgmDmQaMC9gEShF"));

   private EglUtils() {
   }

   public static int getMaxTextureSize() {
      try {
         return VERSION.SDK_INT >= 17 ? getMaxTextureEgl14() : getMaxTextureEgl10();
      } catch (Exception var1) {
         Exception e = var1;
         Log.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIVJDBuHjAaLBgMKGkmAi9uNysxPQhSVg==")), e);
         return 0;
      }
   }

   @TargetApi(17)
   private static int getMaxTextureEgl14() {
      EGLDisplay dpy = EGL14.eglGetDisplay(0);
      int[] vers = new int[2];
      EGL14.eglInitialize(dpy, vers, 0, vers, 1);
      int[] configAttr = new int[]{12351, 12430, 12329, 0, 12352, 4, 12339, 1, 12344};
      EGLConfig[] configs = new EGLConfig[1];
      int[] numConfig = new int[1];
      EGL14.eglChooseConfig(dpy, configAttr, 0, configs, 0, 1, numConfig, 0);
      if (numConfig[0] == 0) {
         return 0;
      } else {
         EGLConfig config = configs[0];
         int[] surfAttr = new int[]{12375, 64, 12374, 64, 12344};
         EGLSurface surf = EGL14.eglCreatePbufferSurface(dpy, config, surfAttr, 0);
         int[] ctxAttrib = new int[]{12440, 2, 12344};
         EGLContext ctx = EGL14.eglCreateContext(dpy, config, EGL14.EGL_NO_CONTEXT, ctxAttrib, 0);
         EGL14.eglMakeCurrent(dpy, surf, surf, ctx);
         int[] maxSize = new int[1];
         GLES20.glGetIntegerv(3379, maxSize, 0);
         EGL14.eglMakeCurrent(dpy, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
         EGL14.eglDestroySurface(dpy, surf);
         EGL14.eglDestroyContext(dpy, ctx);
         EGL14.eglTerminate(dpy);
         return maxSize[0];
      }
   }

   @TargetApi(14)
   private static int getMaxTextureEgl10() {
      EGL10 egl = (EGL10)javax.microedition.khronos.egl.EGLContext.getEGL();
      javax.microedition.khronos.egl.EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
      int[] vers = new int[2];
      egl.eglInitialize(dpy, vers);
      int[] configAttr = new int[]{12351, 12430, 12329, 0, 12339, 1, 12344};
      javax.microedition.khronos.egl.EGLConfig[] configs = new javax.microedition.khronos.egl.EGLConfig[1];
      int[] numConfig = new int[1];
      egl.eglChooseConfig(dpy, configAttr, configs, 1, numConfig);
      if (numConfig[0] == 0) {
         return 0;
      } else {
         javax.microedition.khronos.egl.EGLConfig config = configs[0];
         int[] surfAttr = new int[]{12375, 64, 12374, 64, 12344};
         javax.microedition.khronos.egl.EGLSurface surf = egl.eglCreatePbufferSurface(dpy, config, surfAttr);
//         int EGL_CONTEXT_CLIENT_VERSION = true;
         int[] ctxAttrib = new int[]{12440, 1, 12344};
         javax.microedition.khronos.egl.EGLContext ctx = egl.eglCreateContext(dpy, config, EGL10.EGL_NO_CONTEXT, ctxAttrib);
         egl.eglMakeCurrent(dpy, surf, surf, ctx);
         int[] maxSize = new int[1];
         GLES10.glGetIntegerv(3379, maxSize, 0);
         egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
         egl.eglDestroySurface(dpy, surf);
         egl.eglDestroyContext(dpy, ctx);
         egl.eglTerminate(dpy);
         return maxSize[0];
      }
   }
}
