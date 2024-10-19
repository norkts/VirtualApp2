package com.lody.virtual.client.hook.proxies.restriction;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.content.IRestrictionsManager;

@TargetApi(21)
public class RestrictionStub extends BinderInvocationProxy {
   public RestrictionStub() {
      super(IRestrictionsManager.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKWwKFi99JwozKi0YLw==")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMaIAJgHgY5Lwg2MW8FMF9rDjA/KS4YJWYaGipsNyxF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOD9pHjAqKgccL2oFLCVlMgogKT06KWAzNCA="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uL2wVNANmHyQ/Iz1XMWoKAi9lJxpF"))));
   }
}
