package com.kook.deviceinfo.data;

import android.os.Build.VERSION;
import com.kook.deviceinfo.DeviceApplication;
import com.kook.deviceinfo.impClasses.BuildInfo;
import com.kook.deviceinfo.util.GeneralUtils;
import com.kook.deviceinfo.util.LanguageUtils;
import com.kook.deviceinfo.util.NetWorkUtils;
import com.kook.deviceinfo.util.OtherUtils;

public class GeneralData {
   public String and_id = GeneralUtils.getAndroidID();
   public String gaid;
   public String network_operator_name;
   public String network_operator;
   public String network_type;
   public String phone_type;
   public String mcc;
   public String bluetooth_mac;
   public String bluetooth_name;
   public String mnc;
   public String locale_iso_3_language;
   public String locale_iso_3_country;
   public String time_zone_id;
   public String locale_display_language;
   public String cid;
   public String dns;
   public String uuid;
   public int slot_count;
   public String meid;
   public String imei1;
   public String imei2;
   public String imsi;
   public String mac;
   public String language;
   public String ui_mode_type;
   public int screenHeight;
   public int screenWidth;
   public String security_patch;

   public GeneralData() {
      this.gaid = GeneralUtils.gaid;
      this.network_operator_name = GeneralUtils.getNetworkOperatorName();
      this.network_operator = GeneralUtils.getNetworkOperator();
      this.network_type = GeneralUtils.getNetworkType();
      this.phone_type = GeneralUtils.getPhoneType();
      this.mcc = GeneralUtils.getMcc();
      this.mnc = GeneralUtils.getMnc();
      this.cid = GeneralUtils.getCidNumbers();
      this.dns = GeneralUtils.getLocalDNS();
      this.uuid = GeneralUtils.getMyUUID();
      this.slot_count = OtherUtils.getPhoneSimCount();
      this.meid = GeneralUtils.getMeid();
      this.locale_iso_3_country = LanguageUtils.getSystemLanguage().getISO3Country();
      this.locale_iso_3_language = LanguageUtils.getSystemLanguage().getISO3Language();
      this.locale_display_language = LanguageUtils.getSystemLanguage().getDisplayLanguage();
      this.language = LanguageUtils.getSystemLanguage().getLanguage();
      this.imei1 = GeneralUtils.getIMEI(0);
      this.imei2 = GeneralUtils.getIMEI(1);
      this.imsi = GeneralUtils.getIMSI();
      this.ui_mode_type = GeneralUtils.getUiModeType();
      this.time_zone_id = LanguageUtils.getCurrentTimeZone();
      this.mac = NetWorkUtils.getMacAddress();
      this.bluetooth_mac = NetWorkUtils.getBluetoothMac();
      this.bluetooth_name = NetWorkUtils.getBluetoothName(DeviceApplication.getApp());
      this.screenHeight = BuildInfo.getScreenHeight(DeviceApplication.getApp());
      this.screenWidth = BuildInfo.getScreenWidth(DeviceApplication.getApp());
      if (VERSION.SDK_INT >= 23) {
         this.security_patch = VERSION.SECURITY_PATCH;
      }

   }
}
