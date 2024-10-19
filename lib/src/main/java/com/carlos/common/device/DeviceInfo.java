package com.carlos.common.device;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import androidx.annotation.RequiresApi;
import com.alibaba.fastjson.JSONObject;
import com.carlos.common.network.StringFog;
import com.kook.common.utils.HVLog;
import com.kook.network.secret.MD5Utils;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class DeviceInfo {
   private static String SOFT_CHANNEL_NO = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JCwuG31RPA99NTMSICwYW2MhMAA="));
   private static String SOFT_ID = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC4uIWVQJCA="));
   static DeviceInfo deviceInfo;
   JSONObject mElement = new JSONObject();
   HashMap<String, String> mHashMap = new LinkedHashMap(16);
   private String terminalDevicesSoftID = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgAD28JJCprJx0IKhdXOW4jGgRiIyA9KjxaGw=="));
   public int deviceWidth;
   public int deviceHeight;
   public String imei;
   public String board;
   public String bootloader;
   public String brand;
   public String cpuAbi;
   public String cpuAbi2;
   public String device;
   public String display;
   public String fingerprint;
   public String hardware;
   public String host;
   public String device_id;
   public String model;
   public String manufacturer;
   public String product;
   public String radio;
   public String tags;
   public String type;
   public String user;
   public int sdkInt;
   public String serial;
   public int versionCode;
   public String androidId;
   public String channel;
   public String softId;
   public String applicationName;

   public static DeviceInfo getInstance(Context context) {
      if (deviceInfo == null) {
         deviceInfo = new DeviceInfo(context);
      }

      return deviceInfo;
   }

   private DeviceInfo(Context context) {
      this.board = Build.BOARD;
      this.bootloader = Build.BOOTLOADER;
      this.brand = Build.BRAND;
      this.cpuAbi = Build.CPU_ABI;
      this.cpuAbi2 = Build.CPU_ABI2;
      this.device = Build.DEVICE;
      this.display = Build.DISPLAY;
      this.fingerprint = Build.FINGERPRINT;
      this.hardware = Build.HARDWARE;
      this.host = Build.HOST;
      this.device_id = Build.ID;
      this.model = Build.MODEL;
      this.manufacturer = Build.MANUFACTURER;
      this.product = Build.PRODUCT;
      this.radio = Build.RADIO;
      this.tags = Build.TAGS;
      this.type = Build.TYPE;
      this.user = Build.USER;
      this.sdkInt = VERSION.SDK_INT;
      this.serial = Build.SERIAL;
      this.deviceWidth = getDeviceWidth(context);
      this.deviceHeight = getDeviceHeight(context);
      this.imei = getIMEI(context);
      this.versionCode = this.getVersionCode(context);
      this.channel = this.getMetaDataFromApp(context, SOFT_CHANNEL_NO);
      this.softId = this.getMetaDataFromApp(context, SOFT_ID);
      this.androidId = getAndroidId(context);
      this.applicationName = this.getApplicationName(context);
   }

   private String getApplicationName(Context context) {
      PackageManager packageManager = context.getPackageManager();
      String packageName = context.getPackageName();
      ApplicationInfo appInfo = null;

      try {
         appInfo = packageManager.getApplicationInfo(packageName, 128);
      } catch (PackageManager.NameNotFoundException var6) {
         PackageManager.NameNotFoundException e = var6;
         throw new RuntimeException(e);
      }

      String appName = packageManager.getApplicationLabel(appInfo).toString();
      return appName;
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   public int getVersionCode(Context context) {
      try {
         PackageManager packageManager = context.getPackageManager();
         packageManager.getPackageInfo(context.getPackageName(), 0);
         int versionCode = 23202;
         return versionCode;
      } catch (PackageManager.NameNotFoundException var5) {
         PackageManager.NameNotFoundException e = var5;
         e.printStackTrace();
         return 0;
      }
   }

   public String getVersionName(Context context) {
      try {
         PackageManager packageManager = context.getPackageManager();
         PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
         String versionName = packInfo.versionName;
         return versionName;
      } catch (PackageManager.NameNotFoundException var5) {
         PackageManager.NameNotFoundException e = var5;
         e.printStackTrace();
         return "";
      }
   }

   private String getMetaDataFromApp(Context context, String meta) {
      String value = "";

      try {
         ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
         value = appInfo.metaData.getString(meta);
      } catch (PackageManager.NameNotFoundException var5) {
         PackageManager.NameNotFoundException e = var5;
         e.printStackTrace();
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ly4ACWcJEjBrJSc1LggIQGozNCRmClkrPT5SVg==")) + e.toString());
      }

      return value;
   }

   public String getSoftId(Context context) {
      String metaDataFromApp = this.getMetaDataFromApp(context, SOFT_ID);
      if (TextUtils.isEmpty(metaDataFromApp)) {
         throw new NullPointerException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("DFcrMkcQPSxjJwUyLS42OWkVPChsDSQ9Li09MUoWPTFHAgMtAR4JKHswJyRsJFkqLxhSVg==")));
      } else {
         return metaDataFromApp.replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC4uIWVQJCB6J1RF")), "");
      }
   }

   public static int getDeviceWidth(Context context) {
      try {
         return context.getResources().getDisplayMetrics().widthPixels;
      } catch (Exception var2) {
         Exception e = var2;
         HVLog.printException(e);
         return 0;
      }
   }

   public static int getDeviceHeight(Context context) {
      try {
         return context.getResources().getDisplayMetrics().heightPixels;
      } catch (Exception var2) {
         Exception e = var2;
         HVLog.printException(e);
         return 0;
      }
   }

   public static String getAndroidId(Context context) {
      return Secure.getString(context.getContentResolver(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KAgqJ2o3PCtoMgE9KghSVg==")));
   }

   public static String getIMEI(Context context) {
      return "";
   }

   public static String getDeviceDefaultLanguage() {
      return Locale.getDefault().getLanguage();
   }

   public String getDevicesNo() {
      HashMap<String, String> hashMap = this.toMap();
      String devicesSoftID = "";
      Iterator var3 = hashMap.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry<String, String> entry = (Map.Entry)var3.next();
         if (!((String)entry.getKey()).equals(this.terminalDevicesSoftID)) {
            devicesSoftID = devicesSoftID + (String)entry.getValue();
         }
      }

      String decrypt = MD5Utils.md5Encode(devicesSoftID);
      return decrypt;
   }

   public String getChannelNo() {
      return this.channel;
   }

   public int getVersionCode() {
      return this.versionCode;
   }

   @RequiresApi(
      api = 26
   )
   public long getCurrentTimestamp() {
      ZoneId beijingZoneId = ZoneId.of(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IAcYLG4FPB9lNzM8Kj4qMWwFSFo=")));
      ZonedDateTime beijingDateTime = ZonedDateTime.now(beijingZoneId);
      long timestampInSeconds = beijingDateTime.toEpochSecond();
      return timestampInSeconds;
   }

   public HashMap<String, String> toMap() {
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxgAMWwJQS9kAVwyLggqVg==")), String.valueOf(this.deviceWidth));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxgAMWwJQS99NyM9Kj4qJg==")), String.valueOf(this.deviceHeight));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KggmIGwFSFo=")), this.checkString(this.imei));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KD4uJGo3Hlo=")), this.checkString(this.board));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KD4uKmVSMClrJycxLS5SVg==")), this.checkString(this.bootloader));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KD1fJG83Hlo=")), this.checkString(this.brand));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KC1XCmYJTSs=")), this.checkString(this.cpuAbi));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KC1XCmYJTSt/EVRF")), this.checkString(this.cpuAbi2));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxgAMWwJQS8=")), this.checkString(this.device));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lxg2CGpSMCNpJ1RF")), this.checkString(this.display));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lz42KWknEjZvND89KC1fVg==")), this.checkString(this.fingerprint));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhhbD2lTGiNvESBF")), this.checkString(this.hardware));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguCGUVSFo=")), this.checkString(this.host));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxgAMWwJQS9mAVwy")), this.checkString(this.device_id));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQguJ2kJMFo=")), this.checkString(this.model));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQhbKWUJBiNrDichLS4YIA==")), this.checkString(this.manufacturer));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBdfKmlTEiVsN1RF")), this.product);
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD5bJ2wJPFo=")), this.checkString(this.radio));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxhbImojSFo=")), this.checkString(this.tags));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixc2DWkFSFo=")), this.type);
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwcYIGozSFo=")), this.user);
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC4cLmQJODA=")), String.valueOf(this.sdkInt));
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC4AD2wJAig=")), this.serial);
      this.mHashMap.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KAgqJ2o3PCtoMgE9KghSVg==")), this.androidId);
      return this.mHashMap;
   }

   private void elementAddProperty(String property, Object object) {
      if (this.mElement.containsKey(property)) {
         this.mElement.remove(property);
      }

      if (object instanceof String) {
         this.mElement.put(property, (String)object);
      } else if (object instanceof Integer) {
         this.mElement.put(property, (Integer)object);
      } else if (object instanceof Boolean) {
         this.mElement.put(property, (Boolean)object);
      } else {
         if (!(object instanceof Long)) {
            throw new NullPointerException(property + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OF45O34VRVo=")) + object + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("AVYJAkpUQgFDAhg6DCAnBGwwAiZvN1s9WgsZIh8XFAICEkIyXgAiVg==")));
         }

         this.mElement.put(property, (Long)object);
      }

   }

   public String checkString(String atrr) {
      return atrr == null ? "" : atrr;
   }
}
