package com.lody.virtual.client.hook.proxies.graphics;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.view.IGraphicsStats;

public class GraphicsStatsStub extends BinderInvocationProxy {
   public GraphicsStatsStub() {
      super(IGraphicsStats.Stub.asInterface, "graphicsstats");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("requestBufferForProcess"));
   }
}
