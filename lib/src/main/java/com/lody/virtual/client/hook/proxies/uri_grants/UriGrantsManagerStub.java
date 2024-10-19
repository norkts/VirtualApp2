package com.lody.virtual.client.hook.proxies.uri_grants;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.app.IUriGrantsManager;

@TargetApi(29)
public class UriGrantsManagerStub extends BinderInvocationProxy {
   public UriGrantsManagerStub() {
      super(IUriGrantsManager.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQcMCWYzPAR9DlkgIy5SVg==")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQaFi9pHjAqKgccL2oFLCVlNDBF"))));
   }
}
