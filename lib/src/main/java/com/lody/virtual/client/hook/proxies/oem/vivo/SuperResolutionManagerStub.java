package com.lody.virtual.client.hook.proxies.oem.vivo;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import mirror.oem.vivo.ISuperResolutionManager;

public class SuperResolutionManagerStub extends BinderInvocationProxy {
   private static final String SERVER_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii0uKGgaFl9iASg1KhgMLmwjNCZ9ATgbLRgmJ2EzSFo="));

   public SuperResolutionManagerStub() {
      super(ISuperResolutionManager.Stub.TYPE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii0uKGgaFl9iASg1KhgMLmwjNCZ9ATgbLRgmJ2EzSFo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uPWUaLAZiASwCLwcqCW4jEitkJyg/KggYKmIhND9pATAuIAgMO2UzPD9iAR4cIBc2JW4FNFo="))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcDGgVPC9hJwo/IzwmOW4FJDdrJygQLhcqCmMKRSJ9JzAqIz4AH2oVJARoNygRJQguM24KAjFqEVRF"))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uPWUaLAZiASwPLAgmPWoYRStsJwYdKhcqI2AgRRBqHiQ9LywYLGgFBjFoEVRF"))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcDGgVPC9hJwo/IzwqLWozGgRkNyg6LD4ECWYaGipsNSw9KC0cJ2AjRTNqJyA0"))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcFJCljJCA9KAYqPWUwBi9lNyAQKgg+CmIFSFo="))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhcuLGcFJCljJCA9KAYqPWUwBi9lNyAQKgg+CmIFSFo="))));
   }
}
