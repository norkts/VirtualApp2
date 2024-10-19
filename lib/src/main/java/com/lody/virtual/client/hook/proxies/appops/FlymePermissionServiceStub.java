package com.lody.virtual.client.hook.proxies.appops;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.oem.IFlymePermissionService;

public class FlymePermissionServiceStub extends BinderInvocationProxy {
   public FlymePermissionServiceStub() {
      super(IFlymePermissionService.Stub.TYPE, "flyme_permission");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("noteIntentOperation"));
   }
}
