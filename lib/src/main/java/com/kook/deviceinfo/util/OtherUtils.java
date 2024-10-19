package com.kook.deviceinfo.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.provider.Settings;
import android.provider.ContactsContract.Groups;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import com.kook.deviceinfo.DeviceApplication;
import com.kook.deviceinfo.constant.SystemFileConStant;
import com.kook.deviceinfo.data.SensorData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class OtherUtils {
   private static final String LINE_SEP = System.getProperty("line.separator");

   private static TelephonyManager getTelephonyManager() {
      return (TelephonyManager)DeviceApplication.getApp().getSystemService("phone");
   }

   public static String getBaseband_Ver() {
      String Version = "";

      try {
         Class cl = Class.forName("android.os.SystemProperties");
         Object invoker = cl.newInstance();
         Method m = cl.getMethod("get", String.class, String.class);
         Object result = m.invoke(invoker, "gsm.version.baseband", "no message");
         Version = (String)result;
      } catch (Exception var5) {
      }

      return Version;
   }

   @RequiresApi(
      api = 17
   )
   public static String getResolutions() {
      Point outSize = new Point();
      WindowManager wm = (WindowManager)DeviceApplication.getApp().getSystemService("window");
      wm.getDefaultDisplay().getRealSize(outSize);
      int x = outSize.x;
      int y = outSize.y;
      return x + "*" + y;
   }

   public static String getSerialNumbers() {
      String serial = "";

      try {
         if (VERSION.SDK_INT >= 28) {
            serial = Build.getSerial();
         } else if (VERSION.SDK_INT > 24) {
            serial = Build.SERIAL;
         } else {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String)get.invoke(c, "ro.serialno");
         }
      } catch (Exception var3) {
         Exception e = var3;
         e.printStackTrace();
         Log.e("e", "读取设备序列号异常：" + e.toString());
      }

      return serial;
   }

   public static String getScreenSizeOfDevice2() {
      Point point = new Point();
      WindowManager wm = (WindowManager)DeviceApplication.getApp().getSystemService("window");
      wm.getDefaultDisplay().getRealSize(point);
      DisplayMetrics dm = DeviceApplication.getApp().getResources().getDisplayMetrics();
      double x = Math.pow((double)((float)point.x / dm.xdpi), 2.0);
      double y = Math.pow((double)((float)point.y / dm.ydpi), 2.0);
      double screenInches = Math.sqrt(x + y);
      return String.valueOf(screenInches);
   }

   @SuppressLint({"MissingPermission"})
   public static int getJudgeSIMCount() {
      if (ActivityCompat.checkSelfPermission(DeviceApplication.getApp(), "android.permission.READ_PHONE_STATE") != 0) {
         return 0;
      } else {
         int count = 0;
         if (VERSION.SDK_INT >= 22) {
            count = SubscriptionManager.from(DeviceApplication.getApp()).getActiveSubscriptionInfoCount();
            return count;
         } else {
            return count;
         }
      }
   }

   public static int getPhoneSimCount() {
      return VERSION.SDK_INT >= 23 ? getTelephonyManager().getPhoneCount() : 0;
   }

   @SuppressLint({"HardwareIds"})
   public static String getSimSerialNumbers() {
      if (VERSION.SDK_INT >= 29) {
         return "";
      } else {
         String simSerialNumber = getTelephonyManager().getSimSerialNumber();
         return simSerialNumber;
      }
   }

   public static String getSimCountryIsos() {
      return getTelephonyManager().getSimCountryIso();
   }

   public static int isAppRoot() {
      int result = 0;
      if (isDeviceRooted()) {
         result = 1;
      }

      return result;
   }

   public static boolean isDeviceRooted() {
      return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
   }

   private static boolean checkRootMethod1() {
      String buildTags = Build.TAGS;
      return buildTags != null && buildTags.contains("test-keys");
   }

   private static boolean checkRootMethod2() {
      String[] paths = SystemFileConStant.ROOT_FILE;
      String[] var1 = paths;
      int var2 = paths.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String path = var1[var3];
         if ((new File(path)).exists()) {
            return true;
         }
      }

      return false;
   }

   private static boolean checkRootMethod3() {
      Process process = null;

      boolean var2;
      try {
         process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
         BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
         if (in.readLine() == null) {
            var2 = false;
            return var2;
         }

         var2 = true;
      } catch (Throwable var6) {
         var2 = false;
         return var2;
      } finally {
         if (process != null) {
            process.destroy();
         }

      }

      return var2;
   }

   public static int isEmulator() {
      boolean checkProperty = Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.toLowerCase().contains("vbox") || Build.FINGERPRINT.toLowerCase().contains("test-keys") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86") || Build.MANUFACTURER.contains("Genymotion") || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") || "google_sdk".equals(Build.PRODUCT);
      if (checkProperty) {
         return 1;
      } else {
         String operatorName = "";
         TelephonyManager tm = (TelephonyManager)DeviceApplication.getApp().getSystemService("phone");
         if (tm != null) {
            String name = tm.getNetworkOperatorName();
            if (name != null) {
               operatorName = name;
            }
         }

         boolean checkOperatorName = operatorName.toLowerCase().equals("android");
         if (checkOperatorName) {
            return 1;
         } else {
            String url = "tel:123456";
            Intent intent = new Intent();
            intent.setData(Uri.parse(url));
            intent.setAction("android.intent.action.DIAL");
            boolean checkDial = intent.resolveActivity(DeviceApplication.getApp().getPackageManager()) == null;
            return checkDial ? 1 : 0;
         }
      }
   }

   public static int isMockLocation() {
      boolean isOpen = Secure.getInt(DeviceApplication.getApp().getContentResolver(), "mock_location", 0) != 0;
      return isOpen ? 1 : 0;
   }

   public static int isAppDebug() {
      boolean b = Secure.getInt(DeviceApplication.getApp().getContentResolver(), "adb_enabled", 0) > 0;
      return b ? 1 : 0;
   }

   public static int isAirplaneModeOn() {
      boolean b = Global.getInt(DeviceApplication.getApp().getContentResolver(), "airplane_mode_on", 0) != 0;
      return b ? 1 : 0;
   }

   public static int getPhoneMode() {
      AudioManager audioManager = (AudioManager)DeviceApplication.getApp().getSystemService("audio");
      return audioManager.getRingerMode();
   }

   public static String getProxyAddress() {
      String port = "";
      if (checkVPN() == 1) {
         try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();

            while(en.hasMoreElements()) {
               NetworkInterface intf = (NetworkInterface)en.nextElement();
               Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();

               while(enumIpAddr.hasMoreElements()) {
                  InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                  if (!inetAddress.isLoopbackAddress() && intf.getName().contains("tun")) {
                     port = Formatter.formatIpAddress(inetAddress.hashCode());
                  }
               }
            }
         } catch (SocketException var5) {
            SocketException ex = var5;
            ex.printStackTrace();
         }
      }

      return port;
   }

   public static int getIsWifiProxy() {
      boolean IS_ICS_OR_LATER = VERSION.SDK_INT >= 14;
      String proxyAddress;
      int proxyPort;
      if (IS_ICS_OR_LATER) {
         proxyAddress = System.getProperty("http.proxyHost");
         String portStr = System.getProperty("http.proxyPort");
         proxyPort = Integer.parseInt(portStr != null ? portStr : "-1");
      } else {
         proxyAddress = Proxy.getHost(DeviceApplication.getApp());
         proxyPort = Proxy.getPort(DeviceApplication.getApp());
      }

      boolean b = !TextUtils.isEmpty(proxyAddress) && proxyPort != -1;
      return b ? 1 : 0;
   }

   public static String getHostAndPort() {
      boolean IS_ICS_OR_LATER = VERSION.SDK_INT >= 14;
      String proxyAddress;
      int proxyPort;
      if (IS_ICS_OR_LATER) {
         proxyAddress = System.getProperty("http.proxyHost");
         String portStr = System.getProperty("http.proxyPort");
         proxyPort = Integer.parseInt(portStr != null ? portStr : "-1");
      } else {
         proxyAddress = Proxy.getHost(DeviceApplication.getApp());
         proxyPort = Proxy.getPort(DeviceApplication.getApp());
      }

      return TextUtils.isEmpty(proxyAddress) && proxyPort == -1 ? "" : proxyAddress + ":" + proxyPort;
   }

   public static long getBootTime() {
      return System.currentTimeMillis() - SystemClock.elapsedRealtimeNanos() / 1000000L;
   }

   @SuppressLint({"NewApi"})
   public static String getMobileDbm() {
      String dbm = "";
      TelephonyManager tm = getTelephonyManager();
      if (ActivityCompat.checkSelfPermission(DeviceApplication.getApp(), "android.permission.ACCESS_FINE_LOCATION") != 0) {
         return dbm;
      } else {
         List<CellInfo> cellInfoList = tm.getAllCellInfo();
         if (VERSION.SDK_INT >= 17 && null != cellInfoList) {
            Iterator var3 = cellInfoList.iterator();

            while(var3.hasNext()) {
               CellInfo cellInfo = (CellInfo)var3.next();
               if (cellInfo instanceof CellInfoGsm) {
                  CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm)cellInfo).getCellSignalStrength();
                  dbm = String.valueOf(cellSignalStrengthGsm.getDbm());
               } else if (cellInfo instanceof CellInfoCdma) {
                  CellSignalStrengthCdma cellSignalStrengthCdma = ((CellInfoCdma)cellInfo).getCellSignalStrength();
                  dbm = String.valueOf(cellSignalStrengthCdma.getDbm());
               } else if (cellInfo instanceof CellInfoWcdma) {
                  if (VERSION.SDK_INT >= 18) {
                     CellSignalStrengthWcdma cellSignalStrengthWcdma = ((CellInfoWcdma)cellInfo).getCellSignalStrength();
                     dbm = String.valueOf(cellSignalStrengthWcdma.getDbm());
                  }
               } else if (cellInfo instanceof CellInfoLte) {
                  CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte)cellInfo).getCellSignalStrength();
                  dbm = String.valueOf(cellSignalStrengthLte.getDbm());
               }
            }
         }

         return dbm;
      }
   }

   public static int getBrightness() {
      int value = 255;
      return android.provider.Settings.System.getInt(DeviceApplication.getApp().getContentResolver(), "screen_brightness", value);
   }

   public static long getExternalTotalSize() {
      return getFsTotalSize(getSDCardPathByEnvironment());
   }

   public static long getExternalAvailableSize() {
      return getFsAvailableSize(getSDCardPathByEnvironment());
   }

   public static long getInternalTotalSize() {
      return getFsTotalSize(Environment.getDataDirectory().getAbsolutePath());
   }

   public static long getInternalAvailableSize() {
      return getFsAvailableSize(Environment.getDataDirectory().getAbsolutePath());
   }

   public static int getContactGroupCount() {
      if (ActivityCompat.checkSelfPermission(DeviceApplication.getApp(), "android.permission.READ_CONTACTS") != 0) {
         return 0;
      } else {
         Uri uri = Groups.CONTENT_URI;
         ContentResolver contentResolver = DeviceApplication.getApp().getContentResolver();
         Cursor cursor = contentResolver.query(uri, new String[]{"title", "_id"}, (String)null, (String[])null, (String)null);
         int cursorCount = cursor.getCount();
         return cursorCount;
      }
   }

   public static long getAvailMemory() {
      ActivityManager am = (ActivityManager)DeviceApplication.getApp().getSystemService("activity");
      ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
      am.getMemoryInfo(mi);
      return mi.availMem;
   }

   public static long getTotalMemory() {
      String str1 = "/proc/meminfo";
      long initial_memory = 0L;

      try {
         FileReader localFileReader = new FileReader(str1);
         BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
         String str2 = localBufferedReader.readLine();
         String[] arrayOfString = str2.split("\\s+");
         String[] var7 = arrayOfString;
         int var8 = arrayOfString.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String num = var7[var9];
            Log.i(str2, num + "\t");
         }

         int i = Integer.valueOf(arrayOfString[1]);
         initial_memory = new Long((long)i * 1024L);
         localBufferedReader.close();
      } catch (IOException var11) {
      }

      return initial_memory;
   }

   public static long getFsTotalSize(String anyPathInFs) {
      if (TextUtils.isEmpty(anyPathInFs)) {
         return 0L;
      } else {
         StatFs statFs = new StatFs(anyPathInFs);
         long blockSize;
         long totalSize;
         if (VERSION.SDK_INT >= 18) {
            blockSize = statFs.getBlockSizeLong();
            totalSize = statFs.getBlockCountLong();
         } else {
            blockSize = (long)statFs.getBlockSize();
            totalSize = (long)statFs.getBlockCount();
         }

         return blockSize * totalSize;
      }
   }

   public static long getFsAvailableSize(String anyPathInFs) {
      if (TextUtils.isEmpty(anyPathInFs)) {
         return 0L;
      } else {
         StatFs statFs = new StatFs(anyPathInFs);
         long blockSize;
         long availableSize;
         if (VERSION.SDK_INT >= 18) {
            blockSize = statFs.getBlockSizeLong();
            availableSize = statFs.getAvailableBlocksLong();
         } else {
            blockSize = (long)statFs.getBlockSize();
            availableSize = (long)statFs.getAvailableBlocks();
         }

         return blockSize * availableSize;
      }
   }

   public static String getSDCardPathByEnvironment() {
      return MediaFilesUtils.isSDCardEnableByEnvironment() ? Environment.getExternalStorageDirectory().getAbsolutePath() : "";
   }

   public static float getScreenDensity() {
      return Resources.getSystem().getDisplayMetrics().density;
   }

   public static int getScreenDensityDpi() {
      return Resources.getSystem().getDisplayMetrics().densityDpi;
   }

   public static int isTabletDevice() {
      boolean b = (DeviceApplication.getApp().getResources().getConfiguration().screenLayout & 15) >= 3;
      return b ? 1 : 0;
   }

   public static String getDeviceid() {
      StringBuffer stringBuffer = new StringBuffer();
      String meid = "";
      String imei = "";
      String serial = "";
      String mac = "";
      String manufacturer = "";
      if (getJudgeSIMCount() == 2) {
         meid = GeneralUtils.getMeid();
         if (TextUtils.isEmpty(meid)) {
            imei = GeneralUtils.getIMEI(0);
            stringBuffer.append(imei).append("/");
         } else {
            stringBuffer.append(meid).append("/");
         }
      } else {
         imei = GeneralUtils.getIMEI(0);
         stringBuffer.append(imei).append("/");
      }

      serial = getSerialNumbers();
      manufacturer = Build.MANUFACTURER;
      if (TextUtils.isEmpty(serial)) {
         mac = NetWorkUtils.getMacAddress();
         stringBuffer.append(mac).append("/");
      } else {
         stringBuffer.append(serial).append("/");
      }

      stringBuffer.append(manufacturer).append("/");
      stringBuffer.append(Build.BRAND).append("/");
      stringBuffer.append(Build.DEVICE).append("/");
      stringBuffer.append(Build.HARDWARE).append("/");
      stringBuffer.append(Build.MODEL).append("/");
      stringBuffer.append(Build.PRODUCT).append("/");
      stringBuffer.append(Build.TAGS).append("/");
      stringBuffer.append(Build.TYPE).append("/");
      stringBuffer.append(Build.USER).append("/");
      if (VERSION.SDK_INT >= 21) {
         stringBuffer.append(Build.SUPPORTED_ABIS[0]).append("/");
      }

      stringBuffer.append(getResolutions()).append("/");
      stringBuffer.append(getScreenDensity()).append("/");
      stringBuffer.append(getScreenDensityDpi());
      return Md5Utils.string2MD5(stringBuffer.toString());
   }

   public static SensorData getSensorList(SensorData sensorData) {
      SensorManager sensorManager = (SensorManager)DeviceApplication.getApp().getSystemService("sensor");
      List<Sensor> sensors = sensorManager.getSensorList(-1);
      Iterator var3 = sensors.iterator();

      while(var3.hasNext()) {
         Sensor item = (Sensor)var3.next();
         SensorData.SensorInfo storageDatas = new SensorData.SensorInfo();
         storageDatas.type = item.getType();
         storageDatas.name = item.getName();
         storageDatas.version = item.getVersion();
         storageDatas.vendor = item.getVendor();
         storageDatas.max_range = item.getMaximumRange();
         storageDatas.min_delay = item.getMinDelay();
         storageDatas.power = item.getPower();
         storageDatas.resolution = item.getResolution();
         sensorData.sensor_lists.add(storageDatas);
      }

      return sensorData;
   }

   public static String detectInputDeviceWithShell() {
      String deviceInfo = "";

      try {
         Process p = Runtime.getRuntime().exec("cat /proc/bus/input/devices");
         BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
         String line = null;
         if ((line = in.readLine()) != null) {
            deviceInfo = line.trim();
            return deviceInfo;
         }
      } catch (Exception var4) {
         Exception e = var4;
         e.printStackTrace();
      }

      return deviceInfo;
   }

   public static String detectUsbDeviceWithInputManager() {
      String name = "";
      InputManager im = (InputManager)DeviceApplication.getApp().getSystemService("input");
      int[] devices = im.getInputDeviceIds();
      int[] var3 = devices;
      int var4 = devices.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int id = var3[var5];
         InputDevice device = im.getInputDevice(id);
         name = device.getName();
      }

      return name;
   }

   public static int checkDeviceHasNavigationBar() {
      boolean hasMenuKey = ViewConfiguration.get(DeviceApplication.getApp()).hasPermanentMenuKey();
      boolean hasBackKey = KeyCharacterMap.deviceHasKey(4);
      return !hasMenuKey && !hasBackKey ? 0 : 1;
   }

   public static int checkVPN() {
      ConnectivityManager cm = (ConnectivityManager)DeviceApplication.getApp().getSystemService("connectivity");
      boolean b = cm.getNetworkInfo(17).isConnectedOrConnecting();
      return b ? 1 : 0;
   }

   public static boolean isLoactionEnabled(Context context) {
      if (VERSION.SDK_INT >= 19) {
         int locationMode;
         try {
            locationMode = Secure.getInt(context.getContentResolver(), "location_mode");
         } catch (Settings.SettingNotFoundException var4) {
            Settings.SettingNotFoundException e = var4;
            e.printStackTrace();
            return false;
         }

         return locationMode != 0;
      } else {
         String locationProviders = Secure.getString(context.getContentResolver(), "location_providers_allowed");
         return !TextUtils.isEmpty(locationProviders);
      }
   }

   public static int isNetState() {
      ConnectivityManager connectivityManager = (ConnectivityManager)DeviceApplication.getApp().getSystemService("connectivity");
      NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
      if (activeNetworkInfo == null) {
         return 0;
      } else {
         int type = activeNetworkInfo.getType();
         switch (type) {
            case 0:
               return 2;
            case 1:
               return 1;
            default:
               return 0;
         }
      }
   }

   public static String getDeviceName() {
      return Secure.getString(DeviceApplication.getApp().getContentResolver(), "bluetooth_name");
   }
}
