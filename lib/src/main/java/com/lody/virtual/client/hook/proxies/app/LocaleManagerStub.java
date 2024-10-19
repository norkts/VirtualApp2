package com.lody.virtual.client.hook.proxies.app;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.app.ILocaleManager;

public class LocaleManagerStub extends BinderInvocationProxy {
   public LocaleManagerStub() {
      super(ILocaleManager.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgAOWsVHis=")));
   }

   public void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMaIAJgHgY5Lwg2MW8FMA5lJzAsLAguDw=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMaIAJgHgY5Lwg2MW8FMA5lJzAsLAguDw=="))));
   }
}
