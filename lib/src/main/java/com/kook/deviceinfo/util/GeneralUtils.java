package com.kook.deviceinfo.util;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import androidx.core.app.ActivityCompat;
import com.kook.deviceinfo.DeviceApplication;
import com.kook.deviceinfo.data.SimCardData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class GeneralUtils {
   public static String gaid = "";

   public static boolean isChekSelfPermission(String permission) {
      return ActivityCompat.checkSelfPermission(DeviceApplication.getApp(), permission) != 0;
   }

   @SuppressLint({"HardwareIds"})
   public static String getAndroidID() {
      String id = Secure.getString(DeviceApplication.getApp().getContentResolver(), "android_id");
      if ("9774d56d682e549c".equals(id)) {
         return "";
      } else {
         return id == null ? "" : id;
      }
   }

   public static String getNetworkOperatorName() {
      return getTelephonyManager().getNetworkOperatorName();
   }

   public static String getNetworkOperator() {
      return getTelephonyManager().getNetworkOperator();
   }

   public static String getSimOperatorByMnc() {
      String operator = getTelephonyManager().getSimOperator();
      if (operator == null) {
         return "";
      } else {
         switch (operator) {
            case "46000":
            case "46002":
            case "46007":
            case "46020":
               return "中国移动";
            case "46001":
            case "46006":
            case "46009":
               return "中国联通";
            case "46003":
            case "46005":
            case "46011":
               return "中国电信";
            default:
               return operator;
         }
      }
   }

   public static String getMcc() {
      String networkOperator = getTelephonyManager().getNetworkOperator();
      return !TextUtils.isEmpty(networkOperator) ? networkOperator.substring(0, 3) : "";
   }

   public static String getMnc() {
      String networkOperator = getTelephonyManager().getNetworkOperator();
      return !TextUtils.isEmpty(networkOperator) ? networkOperator.substring(3) : "";
   }

   public static String getNetworkType() {
      if (isChekSelfPermission("android.permission.ACCESS_NETWORK_STATE")) {
         return "NETWORK_NO";
      } else if (isEthernet()) {
         return "NETWORK_ETHERNET";
      } else {
         ConnectivityManager cm = (ConnectivityManager)DeviceApplication.getApp().getSystemService("connectivity");
         if (cm == null) {
            return null;
         } else {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
               if (info.getType() == 1) {
                  return "NETWORK_WIFI";
               } else if (info.getType() == 0) {
                  switch (info.getSubtype()) {
                     case 1:
                     case 2:
                     case 4:
                     case 7:
                     case 11:
                     case 16:
                        return "NETWORK_2G";
                     case 3:
                     case 5:
                     case 6:
                     case 8:
                     case 9:
                     case 10:
                     case 12:
                     case 14:
                     case 15:
                     case 17:
                        return "NETWORK_3G";
                     case 13:
                     case 18:
                        return "NETWORK_4G";
                     case 19:
                     default:
                        String subtypeName = info.getSubtypeName();
                        if (!subtypeName.equalsIgnoreCase("TD-SCDMA") && !subtypeName.equalsIgnoreCase("WCDMA") && !subtypeName.equalsIgnoreCase("CDMA2000")) {
                           return "NETWORK_UNKNOWN";
                        }

                        return "NETWORK_3G";
                     case 20:
                        return "NETWORK_5G";
                  }
               } else {
                  return "NETWORK_UNKNOWN";
               }
            } else {
               return "NETWORK_NO";
            }
         }
      }
   }

   public static String getPhoneType() {
      int phoneType = getTelephonyManager().getPhoneType();
      switch (phoneType) {
         case 0:
            return "PHONE_TYPE_NONE";
         case 1:
            return "PHONE_TYPE_GSM";
         case 2:
            return "PHONE_TYPE_CDMA";
         case 3:
            return "PHONE_TYPE_SIP";
         default:
            return "";
      }
   }

   private static boolean isEthernet() {
      ConnectivityManager cm = (ConnectivityManager)DeviceApplication.getApp().getSystemService("connectivity");
      if (cm == null) {
         return false;
      } else {
         NetworkInfo info = cm.getNetworkInfo(9);
         if (info == null) {
            return false;
         } else {
            NetworkInfo.State state = info.getState();
            if (null == state) {
               return false;
            } else {
               return state == State.CONNECTED || state == State.CONNECTING;
            }
         }
      }
   }

   @SuppressLint({"HardwareIds"})
   public static String getIMSI() {
      if (isChekSelfPermission("android.permission.READ_PHONE_STATE")) {
         return "";
      } else {
         if (VERSION.SDK_INT >= 29) {
            try {
               getTelephonyManager().getSubscriberId();
            } catch (SecurityException var1) {
               SecurityException e = var1;
               e.printStackTrace();
               return "";
            }
         }

         return getTelephonyManager().getSubscriberId();
      }
   }

   public static String getIMSI(int subId) {
      TelephonyManager telephonyManager = getTelephonyManager();
      Class<?> telephonyManagerClass = null;
      String imsi = "";

      try {
         telephonyManagerClass = Class.forName("android.telephony.TelephonyManager");
         Method method;
         if (VERSION.SDK_INT > 21) {
            method = telephonyManagerClass.getMethod("getSubscriberId", Integer.TYPE);
            imsi = (String)method.invoke(telephonyManager, subId);
         } else if (VERSION.SDK_INT == 21) {
            method = telephonyManagerClass.getMethod("getSubscriberId", Long.TYPE);
            imsi = (String)method.invoke(telephonyManager, (long)subId);
         }
      } catch (Exception var5) {
         Exception e = var5;
         e.printStackTrace();
      }

      if (imsi == null) {
         imsi = "";
      }

      return imsi;
   }

   @SuppressLint({"MissingPermission"})
   public static SimCardData getSimCardInfo() {
      SimCardData simCardData = new SimCardData();
      simCardData.sim_count = OtherUtils.getJudgeSIMCount();
      if (VERSION.SDK_INT >= 22) {
         SubscriptionManager mSubscriptionManager = (SubscriptionManager)DeviceApplication.getApp().getSystemService("telephony_subscription_service");
         List<SubscriptionInfo> activeSubscriptionInfoList = null;
         if (mSubscriptionManager != null) {
            try {
               activeSubscriptionInfoList = mSubscriptionManager.getActiveSubscriptionInfoList();
            } catch (Exception var5) {
            }
         }

         if (activeSubscriptionInfoList != null && activeSubscriptionInfoList.size() > 0) {
            for(int i = 0; i < activeSubscriptionInfoList.size(); ++i) {
               SubscriptionInfo subscriptionInfo = (SubscriptionInfo)activeSubscriptionInfoList.get(i);
               if (i == 0) {
                  simCardData.imsi1 = getIMSI(subscriptionInfo.getSimSlotIndex());
                  simCardData.sim_country_iso1 = subscriptionInfo.getCountryIso();
                  simCardData.sim_serial_number1 = subscriptionInfo.getIccId();
                  simCardData.number1 = subscriptionInfo.getNumber();
               } else {
                  simCardData.imsi2 = getIMSI(subscriptionInfo.getSimSlotIndex());
                  simCardData.sim_country_iso2 = subscriptionInfo.getCountryIso();
                  simCardData.sim_serial_number2 = subscriptionInfo.getIccId();
                  simCardData.number2 = subscriptionInfo.getNumber();
               }
            }
         }
      }

      return simCardData;
   }

   public static String getImei() {
      return getImeiOrMeid(true);
   }

   public static String getMeid() {
      return getImeiOrMeid(false);
   }

   public static String getImeiOrMeid(boolean isImei) {
      if (isChekSelfPermission("android.permission.READ_PHONE_STATE")) {
         return "";
      } else if (VERSION.SDK_INT >= 29) {
         return "";
      } else {
         TelephonyManager tm = getTelephonyManager();
         if (VERSION.SDK_INT >= 26) {
            return isImei ? getMinOne(tm.getImei(0), tm.getImei(1)) : getMinOne(tm.getMeid(0), tm.getMeid(1));
         } else {
            String deviceId;
            if (VERSION.SDK_INT >= 21) {
               deviceId = getSystemPropertyByReflect(isImei ? "ril.gsm.imei" : "ril.cdma.meid");
               if (!TextUtils.isEmpty(deviceId)) {
                  String[] idArr = deviceId.split(",");
                  return idArr.length == 2 ? getMinOne(idArr[0], idArr[1]) : idArr[0];
               } else {
                  String id0 = tm.getDeviceId();
                  String id1 = "";

                  try {
                     Method method = tm.getClass().getMethod("getDeviceId", Integer.TYPE);
                     id1 = (String)method.invoke(tm, isImei ? 1 : 2);
                  } catch (NoSuchMethodException var6) {
                     NoSuchMethodException e = var6;
                     e.printStackTrace();
                  } catch (IllegalAccessException var7) {
                     IllegalAccessException e = var7;
                     e.printStackTrace();
                  } catch (InvocationTargetException var8) {
                     InvocationTargetException e = var8;
                     e.printStackTrace();
                  }

                  if (isImei) {
                     if (id0 != null && id0.length() < 15) {
                        id0 = "";
                     }

                     if (id1 != null && id1.length() < 15) {
                        id1 = "";
                     }
                  } else {
                     if (id0 != null && id0.length() == 14) {
                        id0 = "";
                     }

                     if (id1 != null && id1.length() == 14) {
                        id1 = "";
                     }
                  }

                  return getMinOne(id0, id1);
               }
            } else {
               deviceId = tm.getDeviceId();
               if (isImei) {
                  if (deviceId != null && deviceId.length() >= 15) {
                     return deviceId;
                  }
               } else if (deviceId != null && deviceId.length() == 14) {
                  return deviceId;
               }

               return "";
            }
         }
      }
   }

   private static String getMinOne(String s0, String s1) {
      boolean empty0 = TextUtils.isEmpty(s0);
      boolean empty1 = TextUtils.isEmpty(s1);
      if (empty0 && empty1) {
         return "";
      } else if (!empty0 && !empty1) {
         return s0.compareTo(s1) <= 0 ? s0 : s1;
      } else {
         return !empty0 ? s0 : s1;
      }
   }

   private static String getSystemPropertyByReflect(String key) {
      try {
         Class<?> clz = Class.forName("android.os.SystemProperties");
         Method getMethod = clz.getMethod("get", String.class, String.class);
         return (String)getMethod.invoke(clz, key, "");
      } catch (Exception var3) {
         return "";
      }
   }

   public static String getCidNumbers() {
      if (getTelephonyManager().getPhoneType() == 1) {
         if (ActivityCompat.checkSelfPermission(DeviceApplication.getApp(), "android.permission.ACCESS_FINE_LOCATION") != 0) {
            return "";
         }

         GsmCellLocation location = (GsmCellLocation)getTelephonyManager().getCellLocation();
         if (location != null) {
            return String.valueOf(location.getCid());
         }
      }

      return "";
   }

   public static String getLocalDNS() {
      Process cmdProcess = null;
      BufferedReader reader = null;
      String dnsIP = "";

      Object var4;
      try {
         cmdProcess = Runtime.getRuntime().exec("getprop net.dns1");
         reader = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));
         dnsIP = reader.readLine();
         String var3 = dnsIP;
         return var3;
      } catch (IOException var14) {
         var4 = null;
      } finally {
         try {
            reader.close();
         } catch (IOException var13) {
         }

         cmdProcess.destroy();
      }

      return (String)var4;
   }

   public static String getUUids() {
      return UUID.randomUUID().toString();
   }

   public static String getMyUUID() {
      if (isChekSelfPermission("android.permission.READ_PHONE_STATE")) {
         return "";
      } else {
         TelephonyManager tm = getTelephonyManager();
         String tmDevice;
         String tmSerial;
         if (VERSION.SDK_INT >= 29) {
            tmDevice = "";
            tmSerial = "";
         } else {
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
         }

         String androidId = "" + Secure.getString(DeviceApplication.getApp().getContentResolver(), "android_id");
         UUID deviceUuid = new UUID((long)androidId.hashCode(), (long)tmDevice.hashCode() << 32 | (long)tmSerial.hashCode());
         String uniqueId = deviceUuid.toString();
         return uniqueId;
      }
   }

   public static String getIMEI(int slotId) {
      try {
         TelephonyManager manager = getTelephonyManager();
         Method method = manager.getClass().getMethod("getImei", Integer.TYPE);
         String imei = (String)method.invoke(manager, slotId);
         return imei;
      } catch (Exception var4) {
         return "";
      }
   }

   public static void getGaid() {
      Executors.newSingleThreadExecutor().execute(new Runnable() {
         public void run() {
            try {
               GeneralUtils.gaid = AdvertisingIdClient.getGoogleAdId(DeviceApplication.getApp());
            } catch (Exception var2) {
               Exception e = var2;
               e.printStackTrace();
            }

         }
      });
   }

   public static String getUiModeType() {
      UiModeManager mUiModeManager = (UiModeManager)DeviceApplication.getApp().getSystemService("uimode");
      int uiMode = mUiModeManager.getCurrentModeType();
      switch (uiMode) {
         case 0:
            return "UI_MODE_TYPE_UNDEFINED";
         case 1:
            return "UI_MODE_TYPE_NORMAL";
         case 2:
            return "UI_MODE_TYPE_DESK";
         case 3:
            return "UI_MODE_TYPE_CAR";
         case 4:
            return "UI_MODE_TYPE_TELEVISION";
         case 5:
            return "UI_MODE_TYPE_APPLIANCE";
         case 6:
            return "UI_MODE_TYPE_WATCH";
         case 7:
            return "UI_MODE_TYPE_VR_HEADSET";
         default:
            return Integer.toString(uiMode);
      }
   }

   private static TelephonyManager getTelephonyManager() {
      return (TelephonyManager)DeviceApplication.getApp().getSystemService("phone");
   }

   public static enum NetworkType {
      NETWORK_ETHERNET,
      NETWORK_WIFI,
      NETWORK_5G,
      NETWORK_4G,
      NETWORK_3G,
      NETWORK_2G,
      NETWORK_UNKNOWN,
      NETWORK_NO;
   }
}
