package com.lody.virtual.client.hook.proxies.graphics;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.view.IGraphicsStats;

public class GraphicsStatsStub extends BinderInvocationProxy {
   public GraphicsStatsStub() {
      super(IGraphicsStats.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS0MP28FRS99JygpLBciLmoFSFo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uL2wVNANmHCwvKD0+PWobHiVsMjw5LD42J2EjNFo="))));
   }
}
