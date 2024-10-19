package com.carlos.common.persistent;

import android.os.Parcel;
import android.os.Parcelable;
import com.carlos.common.network.StringFog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VPersistent implements Parcelable {
   public static final int VERSION = 3;
   public static final String PRODUCT_ENV_SIT = "sit";
   public static final String PRODUCT_ENV_PROD = "prod";
   public static String PRODUCT_ENV_KEY = "product_ent_key";
   public int requestCount = 0;
   public long currentTimeMillis = 0L;
   public static final String upgradeEnforce = "upgradeEnforce";
   public static final String upgradeVersion = "upgradeVersion";
   public static String fileName = "fileName";
   public static final String fileMd5 = "fileMd5";
   public static final String urlHost = "url_host";
   public static final String uploadAppUrl = "upload_app_url";
   public static final String downloadAppUrl = "download_app_url";
   public static final String uploadDevicesUrl = "upload_devices_url";
   public static final String downloadDevicesUrl = "download_devices_url";
   public static final String requestTime = "requestTime";
   public static final String heartbeatCount = "heartbeatCount";
   public final Map<String, String> buildAllConfig = new HashMap();
   public static final String adbHook = "adbHook";
   public static final String backupRecovery = "backupRecovery";
   public static final String dingtalk = "dingtalk";
   public static final String dingtalkPic = "dingtalkPic";
   public static final String hookXposed = "hookXposed";
   public static final String injectSo = "injectSo";
   public static final String mockDevice = "mockDevice";
   public static final String mockphone = "mockphone";
   public static final String mockwifi = "mockwifi";
   public static final String staticIp = "staticIp";
   public static final String virtualbox = "virtualbox";
   public static final String virtuallocation = "virtuallocation";
   public static final String channelLimit = "channelLimit";
   public static final String channelStatus = "channelStatus";
   public static final Parcelable.Creator<VPersistent> CREATOR = new Parcelable.Creator<VPersistent>() {
      public VPersistent createFromParcel(Parcel source) {
         return new VPersistent(source);
      }

      public VPersistent[] newArray(int size) {
         return new VPersistent[size];
      }
   };

   public int describeContents() {
      long currentTimeMillis = System.currentTimeMillis();
      return 0;
   }

   public VPersistent() {
   }

   public String getBuildConfig(String key) {
      return (String)this.buildAllConfig.get(key);
   }

   public void setBuildConfig(String key, String value) {
      this.buildAllConfig.put(key, value);
   }

   public void readToParcel(Parcel in) {
      this.requestCount = in.readInt();
      this.currentTimeMillis = in.readLong();
      int buildAppConfigSize = in.readInt();

      for(int i = 0; i < buildAppConfigSize; ++i) {
         String key = in.readString();
         String value = in.readString();
         this.buildAllConfig.put(key, value);
      }

   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.requestCount);
      dest.writeLong(this.currentTimeMillis);
      dest.writeInt(this.buildAllConfig.size());
      Iterator var3 = this.buildAllConfig.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry<String, String> entry = (Map.Entry)var3.next();
         dest.writeString((String)entry.getKey());
         dest.writeString((String)entry.getValue());
      }

   }

   public VPersistent(Parcel in) {
      this.readToParcel(in);
   }
}
