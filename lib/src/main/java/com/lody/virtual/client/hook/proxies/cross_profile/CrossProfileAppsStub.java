package com.lody.virtual.client.hook.proxies.cross_profile;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import mirror.android.content.pm.ICrossProfileApps;

public class CrossProfileAppsStub extends BinderInvocationProxy {
   public CrossProfileAppsStub() {
      super(ICrossProfileApps.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0MD28wLAJhNB4+KQdbPW4gTQJsJ1RF")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQFJARiJDAgJAgqPWoYTQRlJyQaLAguDw=="))));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMBF9JwozLD0cLmghQQNnDjAgKS5SVg==")), (Object)null));
   }
}
