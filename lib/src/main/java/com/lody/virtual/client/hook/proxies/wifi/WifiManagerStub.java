package com.lody.virtual.client.hook.proxies.wifi;

import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.WorkSource;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.remote.VDeviceConfig;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import mirror.android.net.wifi.IWifiManager;
import mirror.android.net.wifi.WifiSsid;

public class WifiManagerStub extends BinderInvocationProxy {
   public void inject() throws Throwable {
      super.inject();
      WifiManager wifiManager = (WifiManager)VirtualCore.get().getContext().getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4YPmUVSFo=")));
      Exception e;
      if (mirror.android.net.wifi.WifiManager.mService != null) {
         try {
            mirror.android.net.wifi.WifiManager.mService.set(wifiManager, (IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
         } catch (Exception var4) {
            e = var4;
            e.printStackTrace();
         }
      } else if (mirror.android.net.wifi.WifiManager.sService != null) {
         try {
            mirror.android.net.wifi.WifiManager.sService.set((IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
         } catch (Exception var3) {
            e = var3;
            e.printStackTrace();
         }
      }

   }

   public WifiManagerStub() {
      super(IWifiManager.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4YPmUVSFo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new MethodProxy() {
         public String getMethodName() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2B2UVOC9qDlk7Lz1bPWkzSFo="));
         }

         public Object call(Object who, Method method, Object... args) throws Throwable {
            SettingConfig.FakeWifiStatus fakeWifiStatus = getConfig().getFakeWifiStatus(getAppPkg(), getAppUserId());
            return fakeWifiStatus != null ? true : super.call(who, method, args);
         }
      });
      this.addMethodProxy(new MethodProxy() {
         public String getMethodName() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQzAi5jDDA2LwcuCGkjBl5vETg/LhhSVg=="));
         }

         public Object call(Object who, Method method, Object... args) throws Throwable {
            SettingConfig.FakeWifiStatus fakeWifiStatus = getConfig().getFakeWifiStatus(getAppPkg(), getAppUserId());
            return fakeWifiStatus != null ? 3 : super.call(who, method, args);
         }
      });
      this.addMethodProxy(new MethodProxy() {
         public String getMethodName() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0MM2saMCtqHho5IxUcDmkVNFo="));
         }

         public Object call(Object who, Method method, Object... args) throws Throwable {
            SettingConfig.FakeWifiStatus fakeWifiStatus = getConfig().getFakeWifiStatus(getAppPkg(), getAppUserId());
            if (fakeWifiStatus != null) {
               IPInfo ipInfo = WifiManagerStub.getIPInfo();
               if (ipInfo != null) {
                  return WifiManagerStub.this.createDhcpInfo(ipInfo);
               }
            }

            return super.call(who, method, args);
         }
      });
      this.addMethodProxy(new GetConnectionInfo());
      this.addMethodProxy(new GetScanResults());
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMjJAZ9JBo/KBYqP24jMF9rDjAwLAcqDw=="))));
      this.addMethodProxy(new RemoveWorkSourceMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2L2wVAgRiDzgzKD0cU28FAiE="))));
      this.addMethodProxy(new RemoveWorkSourceMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc6PGsaMCtuJAY+KQVbDW4FJFJlJAoiIT4ACWEwNCA="))));
      if (VERSION.SDK_INT > 21) {
         this.addMethodProxy(new RemoveWorkSourceMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMA5gJCg7LBccDW8YRStsJCw5Ixg2CmIKFhBpJCQd"))));
      }

      if (VERSION.SDK_INT >= 19) {
         this.addMethodProxy(new RemoveWorkSourceMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uL2wVNANmHCw7LBcqMmkjBl5oJzgb"))));
      }

      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQzAi5jDDA2LwcuCGkjBlo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzGiZiNAY9LAguPWkxMCtvHiAcKS5bDw=="))));
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQzAi5jDCAsJy1fDmkVLC1vDgosKggYKWAzSFo="))) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            List<WifiConfiguration> configurations = ((WifiManager)WifiManagerStub.this.getContext().getApplicationContext().getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4YPmUVSFo=")))).getConfiguredNetworks();
            if (!configurations.isEmpty()) {
               return configurations.get(0);
            } else {
               WifiConfiguration wifiConfiguration = new WifiConfiguration();
               wifiConfiguration.SSID = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JggcPG8jGi9iHCACJi5SVg==")) + (new Random()).nextInt(9000) + 1000;
               wifiConfiguration.allowedKeyManagement.set(4);
               String uuid = UUID.randomUUID().toString();
               wifiConfiguration.preSharedKey = uuid.substring(0, 8) + uuid.substring(9, 13);
               return wifiConfiguration;
            }
         }
      });
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQzAi5jDCAsJy1fDmkVLC1vDgosKggYKWAzSFo=")), 0));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMA5gJCg7KhVfDm8wLAplJCw6KQgACg=="))));
      if (BuildCompat.isOreo()) {
         this.addMethodProxy(new RemoveWorkSourceMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMF59JCA2"))) {
            public Object call(Object who, Method method, Object... args) throws Throwable {
               MethodParameterUtils.replaceFirstAppPkg(args);
               return super.call(who, method, args);
            }
         });
      } else if (VERSION.SDK_INT >= 19) {
         this.addMethodProxy(new RemoveWorkSourceMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMF59JCA2"))));
      }

   }

   private static ScanResult cloneScanResult(Parcelable scanResult) {
      Parcel p = Parcel.obtain();
      scanResult.writeToParcel(p, 0);
      p.setDataPosition(0);
      ScanResult newScanResult = (ScanResult)Reflect.on((Object)scanResult).field(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JisMWGMYMA9pN1RF"))).call(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0MM2saMCtqNyw1KgYmOWoVAitlEVRF")), p).get();
      p.recycle();
      return newScanResult;
   }

   private static IPInfo getIPInfo() {
      try {
         List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
         Iterator var1 = interfaces.iterator();

         while(var1.hasNext()) {
            NetworkInterface intf = (NetworkInterface)var1.next();
            List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
            Iterator var4 = addrs.iterator();

            while(var4.hasNext()) {
               InetAddress addr = (InetAddress)var4.next();
               if (!addr.isLoopbackAddress()) {
                  String sAddr = addr.getHostAddress().toUpperCase();
                  boolean isIPv4 = isIPv4Address(sAddr);
                  if (isIPv4) {
                     IPInfo info = new IPInfo();
                     info.addr = addr;
                     info.intf = intf;
                     info.ip = sAddr;
                     info.ip_hex = InetAddress_to_hex(addr);
                     info.netmask_hex = netmask_to_hex(((InterfaceAddress)intf.getInterfaceAddresses().get(0)).getNetworkPrefixLength());
                     return info;
                  }
               }
            }
         }
      } catch (SocketException var9) {
         SocketException e = var9;
         e.printStackTrace();
      }

      return null;
   }

   private static boolean isIPv4Address(String input) {
      Pattern IPV4_PATTERN = Pattern.compile(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JzpeKnwYQAJODTBTLl8uGXo3PwZhDAIvKAVaDk4JPEx4JV0tPQUiIHoJRRJ6I1gbCgYHP3wJFhNrMFlJOl8HLGYYHixkHwEsOgQiH3sIOCx5IgIvM18YPU8jQC8=")));
      return IPV4_PATTERN.matcher(input).matches();
   }

   private static int netmask_to_hex(int netmask_slash) {
      int r = 0;
      int b = 1;

      for(int i = 0; i < netmask_slash; b <<= 1) {
         r |= b;
         ++i;
      }

      return r;
   }

   private static int InetAddress_to_hex(InetAddress a) {
      int result = 0;
      byte[] b = a.getAddress();

      for(int i = 0; i < 4; ++i) {
         result |= (b[i] & 255) << 8 * i;
      }

      return result;
   }

   private DhcpInfo createDhcpInfo(IPInfo ip) {
      DhcpInfo i = new DhcpInfo();
      i.ipAddress = ip.ip_hex;
      i.netmask = ip.netmask_hex;
      i.dns1 = 67372036;
      i.dns2 = 134744072;
      return i;
   }

   private static WifiInfo createWifiInfo(SettingConfig.FakeWifiStatus status) {
      WifiInfo info = (WifiInfo)mirror.android.net.wifi.WifiInfo.ctor.newInstance();
      IPInfo ip = getIPInfo();
      InetAddress address = ip != null ? ip.addr : null;
      mirror.android.net.wifi.WifiInfo.mNetworkId.set(info, 1);
      mirror.android.net.wifi.WifiInfo.mSupplicantState.set(info, SupplicantState.COMPLETED);
      mirror.android.net.wifi.WifiInfo.mBSSID.set(info, status.getBSSID());
      mirror.android.net.wifi.WifiInfo.mMacAddress.set(info, status.getMAC());
      mirror.android.net.wifi.WifiInfo.mIpAddress.set(info, address);
      mirror.android.net.wifi.WifiInfo.mLinkSpeed.set(info, 65);
      if (VERSION.SDK_INT >= 21) {
         mirror.android.net.wifi.WifiInfo.mFrequency.set(info, 5000);
      }

      mirror.android.net.wifi.WifiInfo.mRssi.set(info, 200);
      if (mirror.android.net.wifi.WifiInfo.mWifiSsid != null) {
         mirror.android.net.wifi.WifiInfo.mWifiSsid.set(info, WifiSsid.createFromAsciiEncoded.call(status.getSSID()));
      } else {
         mirror.android.net.wifi.WifiInfo.mSSID.set(info, status.getSSID());
      }

      return info;
   }

   public static class IPInfo {
      NetworkInterface intf;
      InetAddress addr;
      String ip;
      int ip_hex;
      int netmask_hex;
   }

   private final class GetScanResults extends ReplaceCallingPkgMethodProxy {
      public GetScanResults() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGczLDdgNSw/Iy4MCGUwAlo=")));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return isFakeLocationEnable() ? new ArrayList() : super.call(who, method, args);
      }
   }

   private final class GetConnectionInfo extends MethodProxy {
      private GetConnectionInfo() {
      }

      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzGiZgNDA5LBccDW8bLCZrNwZF"));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         MethodParameterUtils.replaceFirstAppPkg(args);
         SettingConfig.FakeWifiStatus status = getConfig().getFakeWifiStatus(getAppPkg(), getAppUserId());
         if (status != null) {
            return WifiManagerStub.createWifiInfo(status);
         } else {
            WifiInfo wifiInfo = (WifiInfo)method.invoke(who, args);
            if (wifiInfo != null) {
               if (isFakeLocationEnable()) {
                  mirror.android.net.wifi.WifiInfo.mBSSID.set(wifiInfo, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ol45In8OIzJPViciM18lIHo0TDJ8VzxF")));
                  mirror.android.net.wifi.WifiInfo.mMacAddress.set(wifiInfo, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ol45In8OIzJPViciM18lIHo0TDJ8VzxF")));
               } else {
                  VDeviceConfig config = getDeviceConfig();
                  if (config.enable) {
                     String mac = getDeviceConfig().wifiMac;
                     if (!TextUtils.isEmpty(mac)) {
                        mirror.android.net.wifi.WifiInfo.mMacAddress.set(wifiInfo, mac);
                     }
                  }
               }
            }

            return wifiInfo;
         }
      }

      // $FF: synthetic method
      GetConnectionInfo(Object x1) {
         this();
      }
   }

   private class RemoveWorkSourceMethodProxy extends StaticMethodProxy {
      RemoveWorkSourceMethodProxy(String name) {
         super(name);
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         int index = ArrayUtils.indexOfFirst(args, WorkSource.class);
         if (index >= 0) {
            args[index] = null;
         }

         return super.call(who, method, args);
      }
   }
}
