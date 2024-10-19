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
      return BuildCompat.isOreo() ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNDBmHhovLz5SVg==")) : StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNDBmHhovLzxfL2kgRT5qATAg"));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uPWUaLAZiASwfLwdbCG4VQSlqJ1RF")), 0));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzGiZmHjAaLBUALW4bLCZrNwZF")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzGiZmHjAaLBUALW4bFjdlNywdLhc2Vg==")), new int[0]));
   }
}
