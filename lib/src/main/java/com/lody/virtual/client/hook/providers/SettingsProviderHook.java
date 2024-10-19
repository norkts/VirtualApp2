package com.lody.virtual.client.hook.providers;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IInterface;
import android.os.Build.VERSION;
import android.provider.Settings;
import android.text.TextUtils;
import com.lody.virtual.GmsSupport;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.env.SpecialComponentList;
import com.lody.virtual.client.hook.base.MethodBox;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.VDeviceConfig;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SettingsProviderHook extends ExternalProviderHook {
   private static final String TAG = SettingsProviderHook.class.getSimpleName();
   private static final int METHOD_GET = 0;
   private static final int METHOD_PUT = 1;
   private static final int METHOD_LIST = 2;
   private static final Map<String, String> PRE_SET_VALUES = new HashMap();
   private static final Set<String> SETTINGS_DIRECT_TO_SYSTEM = new HashSet();
   private static final Set<String> sSystemTableColums = new HashSet();

   public SettingsProviderHook(IInterface base) {
      super(base);
   }

   private static int getMethodType(String method) {
      if (method.startsWith("GET_")) {
         return 0;
      } else {
         return method.startsWith("PUT_") ? 1 : -1;
      }
   }

   private static boolean isSecureMethod(String method) {
      return method.endsWith("secure");
   }

   public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
      VLog.e(TAG, "sp call " + method.getName() + " -> " + Arrays.toString(args));
      return super.invoke(proxy, method, args);
   }

   public Cursor query(MethodBox methodBox, Uri url, String[] projection, String selection, String[] selectionArgs, String sortOrder, Bundle originQueryArgs) throws InvocationTargetException {
      return url.toString().equals("content://settings/config") ? null : super.query(methodBox, url, projection, selection, selectionArgs, sortOrder, originQueryArgs);
   }

   static int getTableIndex(String str) {
      if (str.contains("secure")) {
         return 1;
      } else if (str.contains("system")) {
         return 0;
      } else if (str.contains("global")) {
         return 2;
      } else {
         return str.contains("config") ? 3 : -1;
      }
   }

   private static void initSystemTableColums() {
      try {
         Field[] declaredFields = Settings.System.class.getDeclaredFields();
         if (declaredFields != null) {
            int length = declaredFields.length;

            for(int i = 0; i < length; ++i) {
               Field field = declaredFields[i];
               if ((field.getModifiers() & 1) != 0 && (field.getModifiers() & 8) != 0 && (field.getModifiers() & 16) != 0 && field.getType() == String.class) {
                  sSystemTableColums.add((String)field.get((Object)null));
               }
            }
         }
      } catch (Exception var4) {
         Exception e = var4;
         e.printStackTrace();
      }

   }

   private static boolean isUseVSettingsProvider(String pkg) {
      return GmsSupport.isGoogleAppOrService(pkg) || SpecialComponentList.getPreInstallPackages().contains(pkg);
   }

   public static void passSettingsProviderPermissionCheck(String packageName) {
      if (isUseVSettingsProvider(packageName)) {
         try {
            XposedHelpers.findAndHookMethod("android.provider.DeviceConfig", Settings.class.getClassLoader(), "getString", String.class, String.class, String.class, new XC_MethodHook() {
               protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                  param.setResult(param.args[2]);
               }
            });
            XposedHelpers.findAndHookMethod("android.provider.DeviceConfig", Settings.class.getClassLoader(), "getBoolean", String.class, String.class, Boolean.TYPE, new XC_MethodHook() {
               protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                  param.setResult(param.args[2]);
               }
            });
            XposedHelpers.findAndHookMethod("android.provider.DeviceConfig", Settings.class.getClassLoader(), "getInt", String.class, String.class, Integer.TYPE, new XC_MethodHook() {
               protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                  param.setResult(param.args[2]);
               }
            });
            XposedHelpers.findAndHookMethod("android.provider.DeviceConfig", Settings.class.getClassLoader(), "getLong", String.class, String.class, Long.TYPE, new XC_MethodHook() {
               protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                  param.setResult(param.args[2]);
               }
            });
            XposedHelpers.findAndHookMethod("android.provider.DeviceConfig", Settings.class.getClassLoader(), "getFloat", String.class, String.class, Float.TYPE, new XC_MethodHook() {
               protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                  param.setResult(param.args[2]);
               }
            });
            if (BuildCompat.isS()) {
               XposedHelpers.findAndHookMethod("android.provider.Settings$NameValueCache", Settings.class.getClassLoader(), "isCallerExemptFromReadableRestriction", new XC_MethodHook() {
                  protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                     param.setResult(true);
                  }
               });
            }
         } catch (Throwable var2) {
            Throwable e = var2;
            e.printStackTrace();
         }
      }

   }

   public Bundle call(MethodBox methodBox, String method, String arg, Bundle extras) throws InvocationTargetException {
      if (!VClient.get().isProcessBound()) {
         return (Bundle)methodBox.call();
      } else {
         int tableIndex = getTableIndex(method);
         if (tableIndex >= 0) {
            if (BuildCompat.isR() && TextUtils.equals(method, "SET_ALL_config")) {
               Bundle bundle = new Bundle();
               bundle.putInt("config_set_all_return", 1);
               return bundle;
            }

            if (BuildCompat.isR() && TextUtils.equals(method, "LIST_config")) {
               return null;
            }

            int methodType = getMethodType(method);
            String presetValue;
            if (methodType == 0) {
               presetValue = (String)PRE_SET_VALUES.get(arg);
               VDeviceConfig config;
               if ("bluetooth_name".equals(arg)) {
                  config = VClient.get().getDeviceConfig();
                  if (config.enable && config.bluetoothName != null) {
                     return this.wrapBundle("bluetooth_name", config.bluetoothName);
                  }
               }

               VLog.d("VA-", "SettingsProviderHook call methodType  :" + methodType + "    presetValue:" + presetValue);
               if (presetValue != null) {
                  return this.wrapBundle(arg, presetValue);
               }

               if ("android_id".equals(arg)) {
                  config = VClient.get().getDeviceConfig();
                  if (config.enable && config.androidId != null) {
                     return this.wrapBundle("android_id", config.androidId);
                  }
               }

               if (SETTINGS_DIRECT_TO_SYSTEM.contains(arg)) {
                  return (Bundle)methodBox.call();
               }

               if (tableIndex != 0 && isUseVSettingsProvider(VClient.get().getCurrentPackage())) {
                  return this.wrapBundle(arg, VActivityManager.get().getSettingsProvider(tableIndex, arg));
               }
            } else {
               if (tableIndex != 0 && isUseVSettingsProvider(VClient.get().getCurrentPackage())) {
                  presetValue = extras.getString("value");
                  if (!TextUtils.isEmpty(presetValue) && !SETTINGS_DIRECT_TO_SYSTEM.contains(arg)) {
                     VActivityManager.get().setSettingsProvider(tableIndex, arg, presetValue);
                     return new Bundle();
                  }

                  return new Bundle();
               }

               if (isSecureMethod(method)) {
                  return new Bundle();
               }
            }
         }

         try {
            return (Bundle)methodBox.call();
         } catch (Exception var9) {
            Exception e = var9;
            if (e.getCause() instanceof SecurityException) {
               return new Bundle();
            } else if (e.getCause() instanceof IllegalArgumentException) {
               return new Bundle();
            } else {
               throw new RuntimeException(e);
            }
         }
      }
   }

   private Bundle wrapBundle(String name, String value) {
      Bundle bundle = new Bundle();
      if (VERSION.SDK_INT >= 24) {
         bundle.putString("name", name);
         bundle.putString("value", value);
      } else {
         bundle.putString(name, value);
      }

      return bundle;
   }

   protected void processArgs(Method method, Object... args) {
      super.processArgs(method, args);
   }

   static {
      PRE_SET_VALUES.put("user_setup_complete", "1");
      PRE_SET_VALUES.put("install_non_market_apps", "1");
      PRE_SET_VALUES.put("development_settings_enabled", "0");
      PRE_SET_VALUES.put("adb_enabled", "0");
      SETTINGS_DIRECT_TO_SYSTEM.add("device_provisioned");
      SETTINGS_DIRECT_TO_SYSTEM.add("location_providers_allowed");
   }
}
