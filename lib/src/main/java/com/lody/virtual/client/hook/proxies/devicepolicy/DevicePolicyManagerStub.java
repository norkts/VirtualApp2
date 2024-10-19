package com.lody.virtual.client.hook.proxies.devicepolicy;

import android.content.ComponentName;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import java.lang.reflect.Method;
import mirror.android.app.admin.IDevicePolicyManager;

public class DevicePolicyManagerStub extends BinderInvocationProxy {
   public DevicePolicyManagerStub() {
      super(IDevicePolicyManager.Stub.asInterface, "device_policy");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new GetStorageEncryptionStatus());
      this.addMethodProxy(new GetDeviceOwnerComponent());
      this.addMethodProxy(new NotifyPendingSystemUpdate());
      this.addMethodProxy(new GetDeviceOwnerName());
      this.addMethodProxy(new GetProfileOwnerName());
      this.addMethodProxy(new SetPasswordQuality());
      this.addMethodProxy(new GetFactoryResetProtectionPolicy());
      this.addMethodProxy(new IsDeviceProvisioned());
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy("isUsbDataSignalingEnabled"));
   }

   private static class IsDeviceProvisioned extends MethodProxy {
      private IsDeviceProvisioned() {
      }

      public String getMethodName() {
         return "IsDeviceProvisioned";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return true;
      }

      // $FF: synthetic method
      IsDeviceProvisioned(Object x0) {
         this();
      }
   }

   private static class GetFactoryResetProtectionPolicy extends MethodProxy {
      private GetFactoryResetProtectionPolicy() {
      }

      public String getMethodName() {
         return "getFactoryResetProtectionPolicy";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return null;
      }

      // $FF: synthetic method
      GetFactoryResetProtectionPolicy(Object x0) {
         this();
      }
   }

   private static class SetPasswordQuality extends MethodProxy {
      private SetPasswordQuality() {
      }

      public String getMethodName() {
         return "SetPasswordQuality";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return null;
      }

      // $FF: synthetic method
      SetPasswordQuality(Object x0) {
         this();
      }
   }

   private static class GetProfileOwnerName extends MethodProxy {
      private GetProfileOwnerName() {
      }

      public String getMethodName() {
         return "getProfileOwnerName";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return null;
      }

      // $FF: synthetic method
      GetProfileOwnerName(Object x0) {
         this();
      }
   }

   private static class GetDeviceOwnerName extends MethodProxy {
      private GetDeviceOwnerName() {
      }

      public String getMethodName() {
         return "getDeviceOwnerName";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return null;
      }

      // $FF: synthetic method
      GetDeviceOwnerName(Object x0) {
         this();
      }
   }

   private static class NotifyPendingSystemUpdate extends MethodProxy {
      private NotifyPendingSystemUpdate() {
      }

      public String getMethodName() {
         return "notifyPendingSystemUpdate";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return 0;
      }

      // $FF: synthetic method
      NotifyPendingSystemUpdate(Object x0) {
         this();
      }
   }

   private static class GetStorageEncryptionStatus extends MethodProxy {
      private GetStorageEncryptionStatus() {
      }

      public String getMethodName() {
         return "getStorageEncryptionStatus";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         args[0] = VirtualCore.get().getHostPkg();
         replaceLastUserId(args);
         return method.invoke(who, args);
      }

      // $FF: synthetic method
      GetStorageEncryptionStatus(Object x0) {
         this();
      }
   }

   private static class GetDeviceOwnerComponent extends MethodProxy {
      private GetDeviceOwnerComponent() {
      }

      public String getMethodName() {
         return "getDeviceOwnerComponent";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return new ComponentName(getAppPkg(), "");
      }

      // $FF: synthetic method
      GetDeviceOwnerComponent(Object x0) {
         this();
      }
   }
}
