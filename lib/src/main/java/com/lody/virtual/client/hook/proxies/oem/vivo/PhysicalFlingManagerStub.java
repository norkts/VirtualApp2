package com.lody.virtual.client.hook.proxies.oem.vivo;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import mirror.oem.vivo.IPhysicalFlingManagerStub;

public class PhysicalFlingManagerStub extends BinderInvocationProxy {
   private static final String SERVER_NAME = "physical_fling_service";

   public PhysicalFlingManagerStub() {
      super(IPhysicalFlingManagerStub.Stub.TYPE, "physical_fling_service");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy("isSupportPhysicalFling"));
   }
}
