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
         super("registerLocationListener", 2);
      }

      public Object call(Object obj, Method method, Object... args) throws Throwable {
         VLog.d("VA-", "   registerLocationListener  ");
         if (isFakeLocationEnable()) {
            VLocationManager.get().requestLocationUpdates(args);
            return 0;
         } else {
            if ("passive".equals(args[0])) {
               args[0] = "gps";
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
         return "locationCallbackFinished";
      }
   }

   static class getProviderProperties extends MethodProxy {
      public String getMethodName() {
         return "getProviderProperties";
      }

      public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
         if (isFakeLocationEnable()) {
            try {
               Reflect.on(result).set("mRequiresNetwork", false);
               Reflect.on(result).set("mRequiresCell", false);
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
         return "sendExtraCommand";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return isFakeLocationEnable() ? true : super.call(who, method, args);
      }
   }

   @SkipInject
   static class RegisterGnssStatusCallback extends AddGpsStatusListener {
      public RegisterGnssStatusCallback() {
         super("registerGnssStatusCallback");
      }
   }

   @SkipInject
   static class UnregisterGnssStatusCallback extends RemoveGpsStatusListener {
      public UnregisterGnssStatusCallback() {
         super("unregisterGnssStatusCallback");
      }
   }

   @SkipInject
   static class RemoveUpdatesPI extends RemoveUpdates {
      public RemoveUpdatesPI() {
         super("removeUpdatesPI");
      }
   }

   @SkipInject
   static class RequestLocationUpdatesPI extends RequestLocationUpdates {
      public RequestLocationUpdatesPI() {
         super("requestLocationUpdatesPI");
      }
   }

   @SkipInject
   static class RemoveGpsStatusListener extends ReplaceLastPkgMethodProxy {
      public RemoveGpsStatusListener() {
         super("removeGpsStatusListener");
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
         return "getBestProvider";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return isFakeLocationEnable() ? "gps" : super.call(who, method, args);
      }
   }

   private static class getAllProviders extends MethodProxy {
      public String getMethodName() {
         return "getAllProviders";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return isFakeLocationEnable() ? Arrays.asList("gps", "network") : super.call(who, method, args);
      }
   }

   @SkipInject
   static class IsProviderEnabled extends MethodProxy {
      public String getMethodName() {
         return "isProviderEnabled";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return isFakeLocationEnable() && args[0] instanceof String ? VLocationManager.get().isProviderEnabled((String)args[0]) : super.call(who, method, args);
      }
   }

   @SkipInject
   static class GetLastKnownLocation extends GetLastLocation {
      public String getMethodName() {
         return "getLastKnownLocation";
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
         super("getLastLocation");
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
         super("removeUpdates");
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
         super("requestLocationUpdates");
      }

      public RequestLocationUpdates(String name) {
         super(name);
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         VLog.d("VA-", "   requestLocationUpdates  ");
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
         super("addGpsStatusListener");
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
