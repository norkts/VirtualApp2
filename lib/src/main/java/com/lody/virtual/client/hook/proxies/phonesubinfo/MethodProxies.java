package com.lody.virtual.client.hook.proxies.phonesubinfo;

import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.remote.VDeviceConfig;
import java.lang.reflect.Method;

class MethodProxies {
   static class GetIccSerialNumberForSubscriber extends GetIccSerialNumber {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLH0VLClpJDAqKQciCGcaGiNoNyg5Ji4ACGkjLCVlJCw7Ki5fJ2wzSFo="));
      }
   }

   static class GetIccSerialNumber extends ReplaceLastPkgMethodProxy {
      public GetIccSerialNumber() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLH0VLClpJDAqKQciCGcaGiNoNyg5")));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         VDeviceConfig config = getDeviceConfig();
         if (config.enable) {
            String iccId = getDeviceConfig().iccId;
            if (!TextUtils.isEmpty(iccId)) {
               return iccId;
            }
         }

         try {
            return super.call(who, method, args);
         } catch (Throwable var6) {
            return null;
         }
      }
   }

   static class GetImeiForSubscriber extends GetDeviceId {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLH0VEitjDDw1IzwqLW4aAilsNx4pLhcMVg=="));
      }
   }

   static class GetDeviceIdForSubscriber extends GetDeviceId {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFND5jDig/IQc2WW8KRV5vAQo6LT0MI30wLDU="));
      }
   }

   static class GetDeviceIdForPhone extends GetDeviceId {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFND5jDig/IQc2WW8KRUxqEQYbLhhSVg=="));
      }
   }

   static class GetDeviceId extends ReplaceLastPkgMethodProxy {
      public GetDeviceId() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFND5jDig/IQc2Vg==")));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         VDeviceConfig config = getDeviceConfig();
         if (config.enable) {
            String deviceId = config.deviceId;
            if (!TextUtils.isEmpty(deviceId)) {
               return deviceId;
            }
         }

         try {
            return super.call(who, method, args);
         } catch (Throwable var6) {
            return null;
         }
      }
   }
}
