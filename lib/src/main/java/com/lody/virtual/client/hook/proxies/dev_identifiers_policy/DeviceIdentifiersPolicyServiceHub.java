package com.lody.virtual.client.hook.proxies.dev_identifiers_policy;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.os.IDeviceIdentifiersPolicyService;

@TargetApi(29)
public class DeviceIdentifiersPolicyServiceHub extends BinderInvocationProxy {
   public DeviceIdentifiersPolicyServiceHub() {
      super(IDeviceIdentifiersPolicyService.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguLmUVLCtsJAYwKAcYLmwjHi9rDgo6")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGczNARjDiAoID1fKGIzQSlqJzguLhhSVg=="))));
   }
}
