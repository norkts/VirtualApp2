package com.lody.virtual.client.hook.proxies.appops;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.oem.IFlymePermissionService;

public class FlymePermissionServiceStub extends BinderInvocationProxy {
   public FlymePermissionServiceStub() {
      super(IFlymePermissionService.Stub.TYPE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4EJ2oVNB9hHjAqKgccL2oFLCVlN1RF")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGgbAiZmHjA2LBVfKmkgRTdvER4cLC5SVg=="))));
   }
}
