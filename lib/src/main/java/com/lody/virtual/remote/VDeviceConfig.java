package com.lody.virtual.remote;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VDeviceConfig implements Parcelable {
   private static final UsedDeviceInfoPool mPool = new UsedDeviceInfoPool();
   public static final int VERSION = 3;
   public boolean enable;
   public String deviceId;
   public String androidId;
   public String wifiMac;
   public String wifiName;
   public String bluetoothMac;
   public String iccId;
   public String serial;
   public String gmsAdId;
   public String bluetoothName;
   public final Map<String, String> buildProp = new HashMap();
   public static final Parcelable.Creator<VDeviceConfig> CREATOR = new Parcelable.Creator<VDeviceConfig>() {
      public VDeviceConfig createFromParcel(Parcel source) {
         return new VDeviceConfig(source);
      }

      public VDeviceConfig[] newArray(int size) {
         return new VDeviceConfig[size];
      }
   };

   public VDeviceConfig() {
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeByte((byte)(this.enable ? 1 : 0));
      dest.writeString(this.deviceId);
      dest.writeString(this.androidId);
      dest.writeString(this.wifiMac);
      dest.writeString(this.wifiName);
      dest.writeString(this.bluetoothMac);
      dest.writeString(this.iccId);
      dest.writeString(this.serial);
      dest.writeString(this.gmsAdId);
      dest.writeInt(this.buildProp.size());
      Iterator var3 = this.buildProp.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry<String, String> entry = (Map.Entry)var3.next();
         dest.writeString((String)entry.getKey());
         dest.writeString((String)entry.getValue());
      }

      dest.writeString(this.bluetoothName);
   }

   public VDeviceConfig(Parcel in) {
      this.enable = in.readByte() != 0;
      this.deviceId = in.readString();
      this.androidId = in.readString();
      this.wifiMac = in.readString();
      this.wifiName = in.readString();
      this.bluetoothMac = in.readString();
      this.iccId = in.readString();
      this.serial = in.readString();
      this.gmsAdId = in.readString();
      int buildPropSize = in.readInt();

      for(int i = 0; i < buildPropSize; ++i) {
         String key = in.readString();
         String value = in.readString();
         this.buildProp.put(key, value);
      }

      this.bluetoothName = in.readString();
   }

   public String getProp(String key) {
      return (String)this.buildProp.get(key);
   }

   public void setProp(String key, String value) {
      this.buildProp.put(key, value);
   }

   public void clear() {
      this.deviceId = null;
      this.androidId = null;
      this.wifiMac = null;
      this.wifiName = null;
      this.bluetoothMac = null;
      this.iccId = null;
      this.serial = null;
      this.gmsAdId = null;
   }

   public static VDeviceConfig random() {
      VDeviceConfig info = new VDeviceConfig();

      String value;
      do {
         value = generateDeviceId();
         info.deviceId = value;
      } while(mPool.deviceIds.contains(value));

      do {
         value = generateHex(System.currentTimeMillis(), 16);
         info.androidId = value;
      } while(mPool.androidIds.contains(value));

      do {
         value = generateMac();
         info.wifiMac = value;
      } while(mPool.wifiMacs.contains(value));

      do {
         value = generate10(System.currentTimeMillis(), 10);
         info.wifiName = value;
      } while(mPool.wifiName.contains(value));

      do {
         value = generateMac();
         info.bluetoothMac = value;
      } while(mPool.bluetoothMacs.contains(value));

      do {
         value = generate10(System.currentTimeMillis(), 20);
         info.iccId = value;
      } while(mPool.iccIds.contains(value));

      info.serial = generateSerial();
      addToPool(info);
      return info;
   }

   public static String generateDeviceId() {
      return generate10(System.currentTimeMillis(), 15);
   }

   public static String generate10(long seed, int length) {
      Random random = new Random(seed);
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < length; ++i) {
         sb.append(random.nextInt(10));
      }

      return sb.toString();
   }

   public static String generateHex(long seed, int length) {
      Random random = new Random(seed);
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < length; ++i) {
         int nextInt = random.nextInt(16);
         if (nextInt < 10) {
            sb.append(nextInt);
         } else {
            sb.append((char)(nextInt - 10 + 97));
         }
      }

      return sb.toString();
   }

   private static String generateMac() {
      Random random = new Random();
      StringBuilder sb = new StringBuilder();
      int next = 1;

      for(int cur = 0; cur < 12; ++cur) {
         int val = random.nextInt(16);
         if (val < 10) {
            sb.append(val);
         } else {
            sb.append((char)(val + 87));
         }

         if (cur == next && cur != 11) {
            sb.append(":");
            next += 2;
         }
      }

      return sb.toString();
   }

   @SuppressLint({"HardwareIds"})
   private static String generateSerial() {
      String serial;
      if (Build.SERIAL != null && Build.SERIAL.length() > 0) {
         serial = Build.SERIAL;
      } else {
         serial = "0123456789ABCDEF";
      }

      List<Character> list = new ArrayList();
      VLog.e("VA-", "serial:" + serial);
      char[] var2 = serial.toCharArray();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char c = var2[var4];
         list.add(c);
      }

      Collections.shuffle(list);
      StringBuilder sb = new StringBuilder();
      Iterator var7 = list.iterator();

      while(var7.hasNext()) {
         Character c = (Character)var7.next();
         sb.append(c);
      }

      VLog.e("VA-", "serial StringBuilder:" + sb.toString());
      return sb.toString();
   }

   public File getWifiFile(int userId, boolean isExt) {
      if (TextUtils.isEmpty(this.wifiMac)) {
         return null;
      } else {
         File wifiMacFie = VEnvironment.getWifiMacFile(userId, isExt);
         if (!wifiMacFie.exists()) {
            try {
               RandomAccessFile file = new RandomAccessFile(wifiMacFie, "rws");
               file.write((this.wifiMac + "\n").getBytes());
               file.close();
            } catch (IOException var5) {
               IOException e = var5;
               e.printStackTrace();
            }
         }

         return wifiMacFie;
      }
   }

   public static void addToPool(VDeviceConfig info) {
      mPool.deviceIds.add(info.deviceId);
      mPool.androidIds.add(info.androidId);
      mPool.wifiMacs.add(info.wifiMac);
      mPool.wifiName.add(info.wifiName);
      mPool.bluetoothMacs.add(info.bluetoothMac);
      mPool.iccIds.add(info.iccId);
   }

   private static final class UsedDeviceInfoPool {
      final List<String> deviceIds;
      final List<String> androidIds;
      final List<String> wifiMacs;
      final List<String> wifiName;
      final List<String> bluetoothMacs;
      final List<String> iccIds;
      final List<String> serials;

      private UsedDeviceInfoPool() {
         this.deviceIds = new ArrayList();
         this.androidIds = new ArrayList();
         this.wifiMacs = new ArrayList();
         this.wifiName = new ArrayList();
         this.bluetoothMacs = new ArrayList();
         this.iccIds = new ArrayList();
         this.serials = new ArrayList();
      }

      // $FF: synthetic method
      UsedDeviceInfoPool(Object x0) {
         this();
      }
   }
}
