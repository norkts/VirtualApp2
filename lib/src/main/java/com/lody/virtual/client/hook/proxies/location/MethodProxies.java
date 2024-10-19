package com.lody.virtual.client.hook.proxies.location;

import android.location.LocationRequest;
import android.os.WorkSource;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.annotations.SkipInject;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceSequencePkgMethodProxy;
import com.lody.virtual.client.ipc.VLocationManager;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.vloc.VLocation;
import java.lang.reflect.Method;
import java.util.Arrays;
import mirror.android.location.LocationRequestL;

class MethodProxies {
   private static void fixLocationRequest(LocationRequest locationRequest) {
      if (locationRequest != null) {
         if (LocationRequestL.mHideFromAppOps != null) {
            LocationRequestL.mHideFromAppOps.set(locationRequest, false);
         }

         if (BuildCompat.isS() && LocationRequestL.mWorkSource != null) {
            WorkSource workSource = (WorkSource)LocationRequestL.mWorkSource.get(locationRequest);
            if (workSource != null) {
               workSource.clear();
            }

         } else if (LocationRequestL.mWorkSource != null) {
            LocationRequestL.mWorkSource.set(locationRequest, (Object)null);
         }
      }
   }

   private static class RegisterLocationListener extends ReplaceSequencePkgMethodProxy {
      public RegisterLocationListener() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uPWUaLAZiASwOKi0qOWUzLCVlNQIaKT0qJ2AwLDU=")), 2);
      }

      public Object call(Object obj, Method method, Object... args) throws Throwable {
         VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITw9DQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OG8jNC1jASggKAguU28FAjdvER4cLCwEI2EjFiBsNAo7ODpXVg==")));
         if (isFakeLocationEnable()) {
            VLocationManager.get().requestLocationUpdates(args);
            return 0;
         } else {
            if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+KW8zAj5iAVRF")).equals(args[0])) {
               args[0] = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS06KQ=="));
            }

            LocationRequest locationRequest = (LocationRequest)args[1];
            MethodProxies.fixLocationRequest(locationRequest);
            return super.call(obj, method, args);
         }
      }
   }

   static class locationCallbackFinished extends MethodProxy {
      public Object call(Object who, Method method, Object... args) throws Throwable {
         return super.call(who, method, args);
      }

      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgAOWsaMC9gJFkfLwdbCG4VQSlqJSQaLC4YD2MaLC8="));
      }
   }

   static class getProviderProperties extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcKFiVmNAYwKAguDGoVNAJrDgo/IxguDw=="));
      }

      public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
         if (isFakeLocationEnable()) {
            try {
               Reflect.on(result).set(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwUMM28aNC9hNDApIj0MLmUFNARqJ1RF")), false);
               Reflect.on(result).set(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwUMM28aNC9hNDApJy0MCG8zSFo=")), false);
            } catch (Throwable var6) {
               Throwable e = var6;
               e.printStackTrace();
            }

            return result;
         } else {
            return super.afterCall(who, method, args, result);
         }
      }
   }

   static class sendExtraCommand extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgLNDBmESw7Jy1fD28jQSZrEVRF"));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return isFakeLocationEnable() ? true : super.call(who, method, args);
      }
   }

   @SkipInject
   static class RegisterGnssStatusCallback extends AddGpsStatusListener {
      public RegisterGnssStatusCallback() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uPWUaLAZiASwTKj4qL2IKBjdvHig6JT4+KGAaMCRpJAJF")));
      }
   }

   @SkipInject
   static class UnregisterGnssStatusCallback extends RemoveGpsStatusListener {
      public UnregisterGnssStatusCallback() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcKmgVPC9hJwo/Izs6DmoKAl5vETg/Khc2H30KTTdpNCQsKghSVg==")));
      }
   }

   @SkipInject
   static class RemoveUpdatesPI extends RemoveUpdates {
      public RemoveUpdatesPI() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uDWowOCtuASQwLwg2PWoITQk=")));
      }
   }

   @SkipInject
   static class RequestLocationUpdatesPI extends RequestLocationUpdates {
      public RequestLocationUpdatesPI() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uL2wVNANmHFE1Ly0iLmwjNCZnDjwvLRcqJ2EhOBY=")));
      }
   }

   @SkipInject
   static class RemoveGpsStatusListener extends ReplaceLastPkgMethodProxy {
      public RemoveGpsStatusListener() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uDWowOCtqJyQpOy42OWUwGgN9ER46KgguKmIFMFo=")));
      }

      public RemoveGpsStatusListener(String name) {
         super(name);
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (isFakeLocationEnable()) {
            VLocationManager.get().removeGpsStatusListener(args);
            return 0;
         } else {
            return super.call(who, method, args);
         }
      }
   }

   @SkipInject
   static class GetBestProvider extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMjNANmHyQqKi4+MWkzGgQ="));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return isFakeLocationEnable() ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS06KQ==")) : super.call(who, method, args);
      }
   }

   private static class getAllProviders extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVHiRpESw1LD0cPmkgRQM="));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return isFakeLocationEnable() ? Arrays.asList(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS06KQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4uLGwzGgRjJ1RF"))) : super.call(who, method, args);
      }
   }

   @SkipInject
   static class IsProviderEnabled extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2Am8jGj5jDgo/IzsMDm4jRSRrASxF"));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return isFakeLocationEnable() && args[0] instanceof String ? VLocationManager.get().isProviderEnabled((String)args[0]) : super.call(who, method, args);
      }
   }

   @SkipInject
   static class GetLastKnownLocation extends GetLastLocation {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIFJANmHA42Ki46DmczNCloDiwaLD4cVg=="));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (isFakeLocationEnable()) {
            VLocation loc = VLocationManager.get().getLocation(getAppPkg(), getAppUserId());
            return loc != null ? loc.toSysLocation() : null;
         } else {
            return super.call(who, method, args);
         }
      }
   }

   @SkipInject
   static class GetLastLocation extends ReplaceLastPkgMethodProxy {
      public GetLastLocation() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIFJANmHFE1Ly0iLmwjNCY=")));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (!(args[0] instanceof String)) {
            LocationRequest request = (LocationRequest)args[0];
            MethodProxies.fixLocationRequest(request);
         }

         if (isFakeLocationEnable()) {
            VLocation loc = VLocationManager.get().getLocation(getAppPkg(), getAppUserId());
            return loc != null ? loc.toSysLocation() : null;
         } else {
            return super.call(who, method, args);
         }
      }
   }

   static class RemoveUpdates extends ReplaceLastPkgMethodProxy {
      public RemoveUpdates() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uDWowOCtuASQwLwg2PWoFSFo=")));
      }

      public RemoveUpdates(String name) {
         super(name);
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (isFakeLocationEnable()) {
            VLocationManager.get().removeUpdates(args);
            return 0;
         } else {
            return super.call(who, method, args);
         }
      }
   }

   @SkipInject
   static class RequestLocationUpdates extends ReplaceFirstPkgMethodProxy {
      public RequestLocationUpdates() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uL2wVNANmHFE1Ly0iLmwjNCZnDjwvLRcqJ2EjSFo=")));
      }

      public RequestLocationUpdates(String name) {
         super(name);
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITw9DQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OG8jNAFmDjApLBVbDW4FQQZqAQYbIhc6IH0FFiBlICMp")));
         if (isFakeLocationEnable()) {
            VLocationManager.get().requestLocationUpdates(args);
            return 0;
         } else {
            if (VERSION.SDK_INT > 16) {
               LocationRequest request = (LocationRequest)args[0];
               MethodProxies.fixLocationRequest(request);
            }

            return super.call(who, method, args);
         }
      }
   }

   @SkipInject
   static class AddGpsStatusListener extends ReplaceLastPkgMethodProxy {
      public AddGpsStatusListener() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPGAwIANpJwo7LBgML2czLANvESgbLhcMVg==")));
      }

      public AddGpsStatusListener(String name) {
         super(name);
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (isFakeLocationEnable()) {
            VLocationManager.get().addGpsStatusListener(args);
            return true;
         } else {
            return super.call(who, method, args);
         }
      }
   }
}
