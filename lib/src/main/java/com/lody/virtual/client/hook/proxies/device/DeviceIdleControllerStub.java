package com.lody.virtual.client.hook.proxies.device;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.os.IDeviceIdleController;

public class DeviceIdleControllerStub extends BinderInvocationProxy {
   public DeviceIdleControllerStub() {
      super(IDeviceIdleController.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguLmUVLCtjDgooKAhSVg==")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPGcFGj1iASwPLwg+PX0FFi9vESgdIxc2CmUFODM="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uDWowOCtpHh4tKAguAW4gHitnJ1kaKgguKGMFND9hASA5"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uDWowOCtpJwYpLBcMD2IzND1rDgoUIwgYCmIKTSxlJzAALD1XVg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKWwFGgRiDygZIy42PW8mTSVvJyg5Ij5fI2YaLDdvASw9IC1XDg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2AmowPCthNSg7LD0MBWwzLAZrAQIaKT0qGWcaNCBlETBNLz4iJ2AKICQ="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2AmowPCthNSg7LD0MBWwzLAZrAQIaKT0qHWEVOFo="))));
   }
}
