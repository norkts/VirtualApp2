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
      this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iDFk7KgcMVg==")), packageName);
      this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28hAiw=")), userId);
      SettingConfig.FakeWifiStatus fakeWifiStatus = App.getApp().mConfig.getFakeWifiStatus(packageName, userId);
      this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki02CWgFSFo=")), fakeWifiStatus == null ? "" : fakeWifiStatus.getSSID());
      this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+OQ==")), fakeWifiStatus == null ? "" : fakeWifiStatus.getMAC());
      this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj02KWUVMFo=")), fakeWifiStatus == null ? "" : fakeWifiStatus.getBSSID());
      VLocation location = VLocationManager.get().getLocation(packageName, userId);
      if (location != null) {
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+LGUaMAViHjBF")), location.latitude);
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgACGgzAgZmDgo/")), location.longitude);
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggELGUaMAViHjBF")), location.altitude);
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWwaFjd9JwZF")), location.accuracy);
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki06M2gVMFo=")), location.speed);
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4uP28jAiZiJ1RF")), location.bearing);
      }

      int deviceId = BaseActivity.getDeviceId(packageName, userId);
      VDeviceConfig deviceConfig = VDeviceManager.get().getDeviceConfig(deviceId);
      boolean enable = VDeviceManager.get().isEnable(deviceId);
      if (enable) {
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjsMEWIhMFo=")), deviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjsMEWIhMFo="))));
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwYAWWAbHlo=")), deviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwYAWWAbHlo="))));
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IhUMUmAINBNuEVRF")), deviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IhUMUmAINBNuEVRF"))));
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYuAH0bLBU=")), deviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYuAH0bLBU="))));
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjwAEWchMFo=")), deviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjwAEWchMFo="))));
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYYA2cLHhFvAVRF")), deviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYYA2cLHhFvAVRF"))));
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAYqVg==")), deviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAYqVg=="))));
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwY+U2QbOBFlJQpKOzsMAg==")), deviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwY+U2QbOBFlJQpKOzsMAg=="))));
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwYU2AxNF9pHywJIjw2Vg==")), deviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwYU2AxNF9pHywJIjw2Vg=="))));
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uKmUVJCQ=")), deviceConfig.serial);
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguLmUVLCtrDgpF")), deviceConfig.deviceId);
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAg2OX0VMFo=")), deviceConfig.iccId);
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4YPmUbEjd9J1RF")), deviceConfig.wifiMac);
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iHAYw")), deviceConfig.androidId);
      } else {
         this.elementAddProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguLmUVLCtrDgoVKj0iOG8zGlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4+Dm8zNFo=")));
      }

      return this.mElement.toString();
   }

   public void parseBackupData(String filePath) {
      String readFile = FileTools.readFile(filePath);
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uP2gLOC9gHjMi")) + readFile + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsFOC9gHjACLwg2MngVSFo=")) + filePath);
      JSONObject jsonObject = JSON.parseObject(readFile);
      if (jsonObject == null) {
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlYdGkZJAx9YAwssAglADkcsJRRBEloz")));
      } else {
         String packageName = this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iDFk7KgcMVg==")));
         int userId = this.getPropertyInt(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28hAiw=")));
         String ssid = this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki02CWgFSFo=")));
         String mac = this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+OQ==")));
         this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj02KWUVMFo=")));
         String SSID_KEY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki02CWgIGiFiAQZF")) + packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + userId;
         String MAC_KEY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+OWYzQStnAVRF")) + packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + userId;
         SPTools.putString(VirtualCore.get().getContext(), SSID_KEY, ssid);
         SPTools.putString(VirtualCore.get().getContext(), MAC_KEY, mac);
         if (jsonObject.containsKey(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+LGUaMAViHjBF"))) || jsonObject.containsKey(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgACGgzAgZmDgo/")))) {
            VLocation mLatLng = new VLocation();
            mLatLng.latitude = (double)this.getPropertyInt(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+LGUaMAViHjBF")));
            mLatLng.longitude = (double)this.getPropertyInt(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgACGgzAgZmDgo/")));
            mLatLng.altitude = (double)this.getPropertyInt(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggELGUaMAViHjBF")));
            mLatLng.accuracy = (float)this.getPropertyInt(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWwaFjd9JwZF")));
            mLatLng.speed = (float)this.getPropertyInt(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki06M2gVMFo=")));
            mLatLng.bearing = (float)this.getPropertyInt(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4uP28jAiZiJ1RF")));
            VirtualLocationManager.get().setMode(userId, packageName, 2);
            VirtualLocationManager.get().setLocation(userId, packageName, mLatLng);
         }

         int deviceId = BaseActivity.getDeviceId(packageName, userId);
         VDeviceConfig deviceConfig = VDeviceManager.get().getDeviceConfig(deviceId);
         if (!jsonObject.containsKey(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguLmUVLCtrDgoVKj0iOG8zGlo=")))) {
            deviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjsMEWIhMFo=")), this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjsMEWIhMFo="))));
            deviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwYAWWAbHlo=")), this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwYAWWAbHlo="))));
            deviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IhUMUmAINBNuEVRF")), this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IhUMUmAINBNuEVRF"))));
            deviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYuAH0bLBU=")), this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYuAH0bLBU="))));
            deviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjwAEWchMFo=")), this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjwAEWchMFo="))));
            deviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYYA2cLHhFvAVRF")), this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYYA2cLHhFvAVRF"))));
            deviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAYqVg==")), this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAYqVg=="))));
            deviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwY+U2QbOBFlJQpKOzsMAg==")), this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwY+U2QbOBFlJQpKOzsMAg=="))));
            deviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwYU2AxNF9pHywJIjw2Vg==")), this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwYU2AxNF9pHywJIjw2Vg=="))));
            deviceConfig.serial = this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uKmUVJCQ=")));
            deviceConfig.deviceId = this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguLmUVLCtrDgpF")));
            deviceConfig.iccId = this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAg2OX0VMFo=")));
            deviceConfig.wifiMac = this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4YPmUbEjd9J1RF")));
            deviceConfig.androidId = this.getPropertyString(jsonObject, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iHAYw")));
            VDeviceManager.get().updateDeviceConfig(deviceId, deviceConfig);
         }

         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlYdGkZJAx9YAwssAglADkcvPQ5BA1oR")));
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
            throw new NullPointerException(property + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl5WOHsJIFo=")) + object + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcREMXFzNZXl4oAgkdDGwaAiVlMz4tWhsCJxlNDCEGFSIvXiIqVg==")));
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
