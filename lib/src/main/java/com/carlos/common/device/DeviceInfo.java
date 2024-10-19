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
   private static String SOFT_CHANNEL_NO = "SOFT_CHANNEL_NO";
   private static String SOFT_ID = "softId";
   static DeviceInfo deviceInfo;
   JSONObject mElement = new JSONObject();
   HashMap<String, String> mHashMap = new LinkedHashMap(16);
   private String terminalDevicesSoftID = "terminalDevicesSoftID";
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
         HVLog.d("getMetaDataFromApp " + e.toString());
      }

      return value;
   }

   public String getSoftId(Context context) {
      String metaDataFromApp = this.getMetaDataFromApp(context, SOFT_ID);
      if (TextUtils.isEmpty(metaDataFromApp)) {
         throw new NullPointerException("请在Androidmainfest中添加 softid");
      } else {
         return metaDataFromApp.replace("softId-", "");
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
      return Secure.getString(context.getContentResolver(), "android_id");
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
      ZoneId beijingZoneId = ZoneId.of("Asia/Shanghai");
      ZonedDateTime beijingDateTime = ZonedDateTime.now(beijingZoneId);
      long timestampInSeconds = beijingDateTime.toEpochSecond();
      return timestampInSeconds;
   }

   public HashMap<String, String> toMap() {
      this.mHashMap.put("deviceWidth", String.valueOf(this.deviceWidth));
      this.mHashMap.put("deviceHeight", String.valueOf(this.deviceHeight));
      this.mHashMap.put("imei", this.checkString(this.imei));
      this.mHashMap.put("board", this.checkString(this.board));
      this.mHashMap.put("bootloader", this.checkString(this.bootloader));
      this.mHashMap.put("brand", this.checkString(this.brand));
      this.mHashMap.put("cpuAbi", this.checkString(this.cpuAbi));
      this.mHashMap.put("cpuAbi2", this.checkString(this.cpuAbi2));
      this.mHashMap.put("device", this.checkString(this.device));
      this.mHashMap.put("display", this.checkString(this.display));
      this.mHashMap.put("fingerprint", this.checkString(this.fingerprint));
      this.mHashMap.put("hardware", this.checkString(this.hardware));
      this.mHashMap.put("host", this.checkString(this.host));
      this.mHashMap.put("device_id", this.checkString(this.device_id));
      this.mHashMap.put("model", this.checkString(this.model));
      this.mHashMap.put("manufacturer", this.checkString(this.manufacturer));
      this.mHashMap.put("product", this.product);
      this.mHashMap.put("radio", this.checkString(this.radio));
      this.mHashMap.put("tags", this.checkString(this.tags));
      this.mHashMap.put("type", this.type);
      this.mHashMap.put("user", this.user);
      this.mHashMap.put("sdkInt", String.valueOf(this.sdkInt));
      this.mHashMap.put("serial", this.serial);
      this.mHashMap.put("android_id", this.androidId);
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
            throw new NullPointerException(property + " :   " + object + "不能转成json 格式数据");
         }

         this.mElement.put(property, (Long)object);
      }

   }

   public String checkString(String atrr) {
      return atrr == null ? "" : atrr;
   }
}