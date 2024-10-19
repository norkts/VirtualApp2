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
      super(IDevicePolicyManager.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguLmUVLCtsJyQ1KhccP2gjSFo=")));
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
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2BW8zFhZ9AQo7Oy0cM28VQSRqARouJhgcO30wTSBuEVRF"))));
   }

   private static class IsDeviceProvisioned extends MethodProxy {
      private IsDeviceProvisioned() {
      }

      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAc2WWgaOC99JDACIz1fLGwgAi9lJxogLghSVg=="));
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
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAjJClmHh4qLQYuPWoFGgZkHgocKgguJWYaGipsNSAeKT42JW4FSFo="));
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
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uLGcFJANhJzg1Iz02A2UjQSRqDiw0"));
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
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcKFiViNAYoKAVfI28VGgR9NzgeLhhSVg=="));
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
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFND5jDig/Ii46DmkgRQBoAQ4g"));
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
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOD9pHjA2KBccDmkIAj9sJCwgLBUuDmIaPD9uAVRF"));
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
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcwMCVhNCA9KAUMDm4KRT9sHiwaLD4cUmYaPD9qASxF"));
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
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFND5jDig/Ii46DmkgRRNlJw47LD4cJ2AzFlo="));
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
