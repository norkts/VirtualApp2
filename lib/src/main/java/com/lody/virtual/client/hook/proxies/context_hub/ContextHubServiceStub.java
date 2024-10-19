package com.lody.virtual.client.hook.proxies.context_hub;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.helper.compat.BuildCompat;
import mirror.android.hardware.location.IContextHubService;

public class ContextHubServiceStub extends BinderInvocationProxy {
   public ContextHubServiceStub() {
      super(IContextHubService.Stub.asInterface, getServiceName());
   }

   private static String getServiceName() {
      return BuildCompat.isOreo() ? "contexthub" : "contexthub_service";
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ResultStaticMethodProxy("registerCallback", 0));
      this.addMethodProxy(new ResultStaticMethodProxy("getContextHubInfo", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("getContextHubHandles", new int[0]));
   }
}
