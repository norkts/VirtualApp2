package com.lody.virtual.client;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Process;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Pair;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.hiddenapibypass.HiddenApiBypass;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.client.natives.NativeMethods;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.InstalledAppInfo;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NativeEngine {
   private static final String TAG = NativeEngine.class.getSimpleName();
   private static final List<DexOverride> sDexOverrides = new ArrayList();
   private static boolean sFlag = false;
   private static boolean sEnabled = false;
   private static final String LIB_NAME = "vbox";
   private static boolean EnablePidInfoCache = false;
   private static HashMap<Integer, PidCacheInfo> pidCache;
   private static int MaxCachePidInfoTime;
   private static int MaxCachePidInfoZise;
   private static final List<Pair<String, String>> REDIRECT_LISTS;
   public static Field artMethodField;

   public static void startDexOverride() {
      List<InstalledAppInfo> installedApps = VirtualCore.get().getInstalledApps(0);
      Iterator var1 = installedApps.iterator();

      while(var1.hasNext()) {
         InstalledAppInfo info = (InstalledAppInfo)var1.next();
         if (!info.dynamic) {
            String originDexPath = getCanonicalPath(info.getApkPath());
            DexOverride override = new DexOverride(originDexPath, (String)null, (String)null, info.getOatPath());
            addDexOverride(override);
         }
      }

   }

   public static void addDexOverride(DexOverride dexOverride) {
      sDexOverrides.add(dexOverride);
   }

   public static String getRedirectedPath(String origPath) {
      VirtualCore.getConfig();

      try {
         return nativeGetRedirectedPath(origPath);
      } catch (Throwable var2) {
         Throwable e = var2;
         VLog.e(TAG, VLog.getStackTraceString(e));
         return origPath;
      }
   }

   public static String reverseRedirectedPath(String origPath) {
      VirtualCore.getConfig();

      try {
         return nativeReverseRedirectedPath(origPath);
      } catch (Throwable var2) {
         Throwable e = var2;
         VLog.e(TAG, VLog.getStackTraceString(e));
         return origPath;
      }
   }

   public static void redirectDirectory(String origPath, String newPath) {
      VirtualCore.getConfig();
      if (!origPath.endsWith("/")) {
         origPath = origPath + "/";
      }

      if (!newPath.endsWith("/")) {
         newPath = newPath + "/";
      }

      REDIRECT_LISTS.add(new Pair(origPath, newPath));
   }

   public static void HideSu() {
      redirectFile("/system/app/Superuser.apk", "/system/app/Superuser.apk-fake");
      redirectFile("/sbin/su", "/sbin/su-fake");
      redirectFile("/system/bin/su", "/system/bin/su-fake");
      redirectFile("/system/xbin/su", "/system/xbin/su-fake");
      redirectFile("/data/local/xbin/su", "/data/local/xbin/su-fake");
      redirectFile("/data/local/bin/su", "/data/local/bin/su-fake");
      redirectFile("/system/sd/xbin/su", "/system/sd/xbin/su-fake");
      redirectFile("/system/bin/failsafe/su", "/system/bin/failsafe/su-fake");
      redirectFile("/data/local/su", "/data/local/su-fake");
      redirectFile("/su/bin/su", "/su/bin/su-fake");
   }

   public static void redirectFile(String origPath, String newPath) {
      VirtualCore.getConfig();
      if (origPath.endsWith("/")) {
         origPath = origPath.substring(0, origPath.length() - 1);
      }

      if (newPath.endsWith("/")) {
         newPath = newPath.substring(0, newPath.length() - 1);
      }

      REDIRECT_LISTS.add(new Pair(origPath, newPath));
   }

   public static void readOnlyFile(String path) {
      VirtualCore.getConfig();
      if (!SettingConfig.isUseNativeEngine2(VClient.get().getCurrentPackage())) {
         try {
            nativeIOReadOnly(path);
         } catch (Throwable var2) {
            Throwable e = var2;
            VLog.e(TAG, VLog.getStackTraceString(e));
         }

      }
   }

   public static void readOnly(String path) {
      VirtualCore.getConfig();
      if (!SettingConfig.isUseNativeEngine2(VClient.get().getCurrentPackage())) {
         if (!path.endsWith("/")) {
            path = path + "/";
         }

         try {
            nativeIOReadOnly(path);
         } catch (Throwable var2) {
            Throwable e = var2;
            VLog.e(TAG, VLog.getStackTraceString(e));
         }

      }
   }

   public static void whitelistFile(String path) {
      VirtualCore.getConfig();

      try {
         nativeIOWhitelist(path);
      } catch (Throwable var2) {
         Throwable e = var2;
         VLog.e(TAG, VLog.getStackTraceString(e));
      }

   }

   public static void whitelist(String path) {
      VirtualCore.getConfig();
      if (!path.endsWith("/")) {
         path = path + "/";
      }

      try {
         nativeIOWhitelist(path);
      } catch (Throwable var2) {
         Throwable e = var2;
         VLog.e(TAG, VLog.getStackTraceString(e));
      }

   }

   public static void forbid(String path, boolean file) {
      VirtualCore.getConfig();
      if (!file && !path.endsWith("/")) {
         path = path + "/";
      }

      try {
         nativeIOForbid(path);
      } catch (Throwable var3) {
         Throwable e = var3;
         VLog.e(TAG, VLog.getStackTraceString(e));
      }

   }

   public static String pathCat(String path1, String path2) {
      if (!TextUtils.isEmpty(path2) && !path1.endsWith("/")) {
         path1 = path1 + "/";
      }

      path1 = path1 + path2;
      return path1;
   }

   public static void enableIORedirect(InstalledAppInfo appInfo) {
      VirtualCore.getConfig();
      if (!sEnabled) {
         ApplicationInfo coreAppInfo;
         try {
            coreAppInfo = VirtualCore.get().getHostPackageManager().getApplicationInfo(VirtualCore.getConfig().getMainPackageName(), 0L);
         } catch (PackageManager.NameNotFoundException var9) {
            PackageManager.NameNotFoundException e = var9;
            throw new RuntimeException(e);
         }

         Collections.sort(REDIRECT_LISTS, new Comparator<Pair<String, String>>() {
            public int compare(Pair<String, String> o1, Pair<String, String> o2) {
               String a = (String)o1.first;
               String b = (String)o2.first;
               return this.compare(b.length(), a.length());
            }

            private int compare(int x, int y) {
               return Integer.compare(x, y);
            }
         });
         Iterator var10 = REDIRECT_LISTS.iterator();

         while(var10.hasNext()) {
            Pair<String, String> pair = (Pair)var10.next();

            try {
               nativeIORedirect((String)pair.first, (String)pair.second);
            } catch (Throwable var8) {
               Throwable e = var8;
               VLog.e(TAG, VLog.getStackTraceString(e));
            }
         }

         try {
            String soPath = (new File(coreAppInfo.nativeLibraryDir, "libvbox.so")).getAbsolutePath();
            String extSoPath = (new File(coreAppInfo.nativeLibraryDir, "libvbox_ext.so")).getAbsolutePath();
            String soPath32 = extSoPath;
            String soPath64 = soPath;
            String nativePath = VEnvironment.getNativeCacheDir(VirtualCore.get().isExtPackage()).getPath();
            nativeEnableIORedirect(soPath32, soPath64, nativePath, VERSION.SDK_INT, appInfo.packageName, VirtualCore.get().getHostPkg());
         } catch (Throwable var7) {
            Throwable e = var7;
            VLog.e(TAG, VLog.getStackTraceString(e));
         }

         sEnabled = true;
      }
   }

   public static void launchEngine(String packageName) {
      VirtualCore.getConfig();
      if (!sFlag) {
         Object[] methods = new Object[]{NativeMethods.gNativeMask, NativeMethods.gOpenDexFileNative, NativeMethods.gCameraNativeSetup, NativeMethods.gAudioRecordNativeCheckPermission, NativeMethods.gMediaRecorderNativeSetup, NativeMethods.gAudioRecordNativeSetup, NativeMethods.gNativeLoad};

         try {
            nativeLaunchEngine(methods, VirtualCore.get().getHostPkg(), packageName, VirtualRuntime.isArt(), BuildCompat.isR() ? 30 : VERSION.SDK_INT, NativeMethods.gCameraMethodType, NativeMethods.gAudioRecordMethodType);
         } catch (Throwable var3) {
            Throwable e = var3;
            VLog.e(TAG, VLog.getStackTraceString(e));
         }

         sFlag = true;
      }
   }

   public static void bypassHiddenAPIEnforcementPolicyIfNeeded() {
      if (BuildCompat.isR()) {
         HiddenApiBypass.setHiddenApiExemptions("L");
      } else if (BuildCompat.isPie()) {
         try {
            Method forNameMethod = Class.class.getDeclaredMethod("forName", String.class);
            Class<?> clazz = (Class)forNameMethod.invoke((Object)null, "dalvik.system.VMRuntime");
            Method getMethodMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
            Method getRuntime = (Method)getMethodMethod.invoke(clazz, "getRuntime", new Class[0]);
            Method setHiddenApiExemptions = (Method)getMethodMethod.invoke(clazz, "setHiddenApiExemptions", new Class[]{String[].class});
            Object runtime = getRuntime.invoke((Object)null);
            setHiddenApiExemptions.invoke(runtime, new String[]{"L"});
         } catch (Throwable var6) {
            Throwable e = var6;
            e.printStackTrace();
         }
      }

   }

   public static boolean onKillProcess(int pid, int signal) {
      VLog.e(TAG, "killProcess: pid = %d, signal = %d.", pid, signal);
      if (pid == Process.myPid()) {
         VLog.e(TAG, VLog.getStackTraceString(new Throwable()));
      }

      return true;
   }

   public static int onGetCallingUid(int originUid) {
      try {
         return onGetCallingUid0(originUid);
      } catch (Throwable var2) {
         Throwable e = var2;
         VLog.e("VA", e);
         return originUid;
      }
   }

   public static int onGetCallingUid0(int originUid) {
      if (VClient.get().getClientConfig() == null) {
         return originUid;
      } else if (originUid != VirtualCore.get().myUid() && originUid != VirtualCore.get().remoteUid()) {
         return originUid;
      } else {
         int callingPid = Binder.getCallingPid();
         if (callingPid == 0) {
            return BuildCompat.isS() ? VClient.get().getBaseVUid() : 9001;
         } else if (callingPid == Process.myPid()) {
            return VClient.get().getBaseVUid();
         } else if (callingPid == VClient.get().getCorePid()) {
            return VEnvironment.SYSTEM_UID;
         } else {
            if (EnablePidInfoCache) {
               long curTime = System.currentTimeMillis();
               PidCacheInfo pidCacheInfo = (PidCacheInfo)pidCache.get(callingPid);
               if (pidCacheInfo != null) {
                  if (curTime - pidCacheInfo.lastTime <= (long)MaxCachePidInfoTime) {
                     if (pidCacheInfo.uid == -1) {
                        return originUid;
                     }

                     pidCacheInfo.lastTime = curTime;
                     return pidCacheInfo.uid;
                  }

                  pidCache.remove(callingPid);
               }

               pidCache.put(callingPid, new PidCacheInfo(callingPid, -1, curTime));
            }

            int uidRet = VActivityManager.get().getUidByPid(callingPid);
            if (uidRet == 9000) {
               uidRet = 1000;
            }

            if (EnablePidInfoCache) {
               long curTime2 = System.currentTimeMillis();
               if (pidCache.size() >= MaxCachePidInfoZise) {
                  Iterator<Map.Entry<Integer, PidCacheInfo>> iterator = pidCache.entrySet().iterator();

                  while(iterator.hasNext()) {
                     Map.Entry<Integer, PidCacheInfo> entry = (Map.Entry)iterator.next();
                     if (curTime2 - ((PidCacheInfo)entry.getValue()).lastTime > (long)MaxCachePidInfoTime) {
                        iterator.remove();
                     }
                  }
               }

               pidCache.put(callingPid, new PidCacheInfo(callingPid, uidRet, curTime2));
            }

            return uidRet;
         }
      }
   }

   private static Field getField(Class topClass, String fieldName) throws NoSuchFieldException {
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

   public static long getArtMethod(Member member) {
      if (artMethodField == null) {
         try {
            artMethodField = getField(Method.class, "artMethod");
         } catch (NoSuchFieldException var3) {
         }
      }

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

   private static DexOverride findDexOverride(String originDexPath) {
      Iterator var1 = sDexOverrides.iterator();

      DexOverride dexOverride;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         dexOverride = (DexOverride)var1.next();
      } while(!dexOverride.originDexPath.equals(originDexPath));

      return dexOverride;
   }

   public static void onOpenDexFileNative(String[] params) {
      String dexPath = params[0];
      if (dexPath != null) {
         String dexCanonicalPath = getCanonicalPath(dexPath);
         DexOverride override = findDexOverride(dexCanonicalPath);
         if (override != null) {
            VLog.e(TAG, "override: " + override.newOdexPath);
            if (override.newDexPath != null) {
               params[0] = override.newDexPath;
            }

            String oatPath = override.newDexPath;
            if (override.originOdexPath != null) {
               String oatCanonicalPath = getCanonicalPath(oatPath);
               if (oatCanonicalPath.equals(override.originOdexPath)) {
                  params[1] = override.newOdexPath;
               }
            } else {
               params[1] = override.newOdexPath;
            }
         }
      }

      VLog.i(TAG, "OpenDexFileNative(\"%s\", \"%s\")", params[0], params[1]);
   }

   private static String getCanonicalPath(String path) {
      File file = new File(path);

      try {
         return file.getCanonicalPath();
      } catch (IOException var3) {
         IOException e = var3;
         e.printStackTrace();
         return file.getAbsolutePath();
      }
   }

   private static native void nativeLaunchEngine(Object[] var0, String var1, String var2, boolean var3, int var4, int var5, int var6);

   private static native void nativeMark();

   private static native String nativeReverseRedirectedPath(String var0);

   private static native String nativeGetRedirectedPath(String var0);

   private static native void nativeIORedirect(String var0, String var1);

   private static native void nativeIOWhitelist(String var0);

   private static native void nativeIOForbid(String var0);

   private static native void nativeIOReadOnly(String var0);

   private static native void nativeEnableIORedirect(String var0, String var1, String var2, int var3, String var4, String var5);

   public static int onGetUid(int uid) {
      return VClient.get().getClientConfig() == null ? uid : VClient.get().getBaseVUid();
   }

   static {
      try {
         System.loadLibrary(VirtualRuntime.adjustLibName("vbox"));
      } catch (Throwable var1) {
         Throwable e = var1;
         VLog.e(TAG, VLog.getStackTraceString(e));
      }

      EnablePidInfoCache = true;
      pidCache = new HashMap();
      MaxCachePidInfoTime = 10000;
      MaxCachePidInfoZise = 64;
      REDIRECT_LISTS = new LinkedList();
   }

   static class PidCacheInfo {
      long lastTime;
      int pid;
      int uid;

      public PidCacheInfo(int pid, int uid, long lastTime) {
         this.pid = pid;
         this.uid = uid;
         this.lastTime = lastTime;
      }

      public String toString() {
         return "PidCacheInfo{pid=" + this.pid + ", uid=" + this.uid + ", lastTime=" + this.lastTime + '}';
      }
   }
}
