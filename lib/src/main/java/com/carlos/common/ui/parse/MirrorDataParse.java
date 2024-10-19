package com.carlos.common.ui.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carlos.common.App;
import com.carlos.common.ui.activity.base.BaseActivity;
import com.carlos.common.utils.FileTools;
import com.carlos.common.utils.SPTools;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VDeviceManager;
import com.lody.virtual.client.ipc.VLocationManager;
import com.lody.virtual.client.ipc.VirtualLocationManager;
import com.lody.virtual.remote.VDeviceConfig;
import com.lody.virtual.remote.vloc.VLocation;

public class MirrorDataParse {
   JSONObject mElement = new JSONObject();

   public String getBackupData(String packageName, int userId) {
      this.mElement.clear();
      this.elementAddProperty("packageName", packageName);
      this.elementAddProperty("userId", userId);
      SettingConfig.FakeWifiStatus fakeWifiStatus = App.getApp().mConfig.getFakeWifiStatus(packageName, userId);
      this.elementAddProperty("ssid", fakeWifiStatus == null ? "" : fakeWifiStatus.getSSID());
      this.elementAddProperty("mac", fakeWifiStatus == null ? "" : fakeWifiStatus.getMAC());
      this.elementAddProperty("bssid", fakeWifiStatus == null ? "" : fakeWifiStatus.getBSSID());
      VLocation location = VLocationManager.get().getLocation(packageName, userId);
      if (location != null) {
         this.elementAddProperty("latitude", location.latitude);
         this.elementAddProperty("longitude", location.longitude);
         this.elementAddProperty("altitude", location.altitude);
         this.elementAddProperty("accuracy", location.accuracy);
         this.elementAddProperty("speed", location.speed);
         this.elementAddProperty("bearing", location.bearing);
      }

      int deviceId = BaseActivity.getDeviceId(packageName, userId);
      VDeviceConfig deviceConfig = VDeviceManager.get().getDeviceConfig(deviceId);
      boolean enable = VDeviceManager.get().isEnable(deviceId);
      if (enable) {
         this.elementAddProperty("BRAND", deviceConfig.getProp("BRAND"));
         this.elementAddProperty("MODEL", deviceConfig.getProp("MODEL"));
         this.elementAddProperty("PRODUCT", deviceConfig.getProp("PRODUCT"));
         this.elementAddProperty("DEVICE", deviceConfig.getProp("DEVICE"));
         this.elementAddProperty("BOARD", deviceConfig.getProp("BOARD"));
         this.elementAddProperty("DISPLAY", deviceConfig.getProp("DISPLAY"));
         this.elementAddProperty("ID", deviceConfig.getProp("ID"));
         this.elementAddProperty("MANUFACTURER", deviceConfig.getProp("MANUFACTURER"));
         this.elementAddProperty("FINGERPRINT", deviceConfig.getProp("FINGERPRINT"));
         this.elementAddProperty("serial", deviceConfig.serial);
         this.elementAddProperty("deviceId", deviceConfig.deviceId);
         this.elementAddProperty("iccId", deviceConfig.iccId);
         this.elementAddProperty("wifiMac", deviceConfig.wifiMac);
         this.elementAddProperty("androidId", deviceConfig.androidId);
      } else {
         this.elementAddProperty("deviceIdEnable", "false");
      }

      return this.mElement.toString();
   }

   public void parseBackupData(String filePath) {
      String readFile = FileTools.readFile(filePath);
      HVLog.d("readFile:" + readFile + "    filePath:" + filePath);
      JSONObject jsonObject = JSON.parseObject(readFile);
      if (jsonObject == null) {
         HVLog.d("还原数据异常");
      } else {
         String packageName = this.getPropertyString(jsonObject, "packageName");
         int userId = this.getPropertyInt(jsonObject, "userId");
         String ssid = this.getPropertyString(jsonObject, "ssid");
         String mac = this.getPropertyString(jsonObject, "mac");
         this.getPropertyString(jsonObject, "bssid");
         String SSID_KEY = "ssid_key" + packageName + "_" + userId;
         String MAC_KEY = "mac_key" + packageName + "_" + userId;
         SPTools.putString(VirtualCore.get().getContext(), SSID_KEY, ssid);
         SPTools.putString(VirtualCore.get().getContext(), MAC_KEY, mac);
         if (jsonObject.containsKey("latitude") || jsonObject.containsKey("longitude")) {
            VLocation mLatLng = new VLocation();
            mLatLng.latitude = (double)this.getPropertyInt(jsonObject, "latitude");
            mLatLng.longitude = (double)this.getPropertyInt(jsonObject, "longitude");
            mLatLng.altitude = (double)this.getPropertyInt(jsonObject, "altitude");
            mLatLng.accuracy = (float)this.getPropertyInt(jsonObject, "accuracy");
            mLatLng.speed = (float)this.getPropertyInt(jsonObject, "speed");
            mLatLng.bearing = (float)this.getPropertyInt(jsonObject, "bearing");
            VirtualLocationManager.get().setMode(userId, packageName, 2);
            VirtualLocationManager.get().setLocation(userId, packageName, mLatLng);
         }

         int deviceId = BaseActivity.getDeviceId(packageName, userId);
         VDeviceConfig deviceConfig = VDeviceManager.get().getDeviceConfig(deviceId);
         if (!jsonObject.containsKey("deviceIdEnable")) {
            deviceConfig.setProp("BRAND", this.getPropertyString(jsonObject, "BRAND"));
            deviceConfig.setProp("MODEL", this.getPropertyString(jsonObject, "MODEL"));
            deviceConfig.setProp("PRODUCT", this.getPropertyString(jsonObject, "PRODUCT"));
            deviceConfig.setProp("DEVICE", this.getPropertyString(jsonObject, "DEVICE"));
            deviceConfig.setProp("BOARD", this.getPropertyString(jsonObject, "BOARD"));
            deviceConfig.setProp("DISPLAY", this.getPropertyString(jsonObject, "DISPLAY"));
            deviceConfig.setProp("ID", this.getPropertyString(jsonObject, "ID"));
            deviceConfig.setProp("MANUFACTURER", this.getPropertyString(jsonObject, "MANUFACTURER"));
            deviceConfig.setProp("FINGERPRINT", this.getPropertyString(jsonObject, "FINGERPRINT"));
            deviceConfig.serial = this.getPropertyString(jsonObject, "serial");
            deviceConfig.deviceId = this.getPropertyString(jsonObject, "deviceId");
            deviceConfig.iccId = this.getPropertyString(jsonObject, "iccId");
            deviceConfig.wifiMac = this.getPropertyString(jsonObject, "wifiMac");
            deviceConfig.androidId = this.getPropertyString(jsonObject, "androidId");
            VDeviceManager.get().updateDeviceConfig(deviceId, deviceConfig);
         }

         HVLog.d("还原数据完成");
      }
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
      } else if (object instanceof Long) {
         this.mElement.put(property, (Long)object);
      } else if (object instanceof Double) {
         this.mElement.put(property, (Double)object);
      } else {
         if (!(object instanceof Float)) {
            throw new NullPointerException(property + " :   " + object + "不能转成json 格式数据");
         }

         this.mElement.put(property, (Float)object);
      }

   }

   private String getPropertyString(JSONObject jsonObject, String key) {
      return jsonObject.containsKey(key) ? jsonObject.getString(key) : "";
   }

   private int getPropertyInt(JSONObject jsonObject, String key) {
      return jsonObject.containsKey(key) ? jsonObject.getIntValue(key) : -1;
   }

   private long getPropertyLong(JSONObject jsonObject, String key) {
      return jsonObject.containsKey(key) ? jsonObject.getLongValue(key) : -1L;
   }

   private boolean getPropertyBoolean(JSONObject jsonObject, String key) {
      return jsonObject.containsKey(key) ? jsonObject.getBooleanValue(key) : false;
   }
}
