package com.lody.virtual.client.hook.proxies.uri_grants;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.app.IUriGrantsManager;

@TargetApi(29)
public class UriGrantsManagerStub extends BinderInvocationProxy {
   public UriGrantsManagerStub() {
      super(IUriGrantsManager.Stub.asInterface, "uri_grants");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getUriPermissions"));
   }
}
