package com.lody.virtual.client.hook.proxies.role;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.role.IRoleManager;

public class RoleManagerStub extends BinderInvocationProxy {
   public RoleManagerStub() {
      super(IRoleManager.Stub.TYPE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4ADmgVSFo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2DGozHitrHjAoKBhSVg=="))));
   }
}
