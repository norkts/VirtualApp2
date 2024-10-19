package com.lody.virtual.client.hook.proxies.restriction;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.content.IRestrictionsManager;

@TargetApi(21)
public class RestrictionStub extends BinderInvocationProxy {
   public RestrictionStub() {
      super(IRestrictionsManager.Stub.asInterface, "restrictions");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getApplicationRestrictions"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("notifyPermissionResponse"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("requestPermission"));
   }
}
