package com.lody.virtual.client.hook.proxies.telephony;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import mirror.com.android.internal.telephony.IHwTelephony;

@Inject(MethodProxies.class)
public class HwTelephonyStub extends BinderInvocationProxy {
   public HwTelephonyStub() {
      super(IHwTelephony.Stub.TYPE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhhfD2ojNB9jETA7LC0MMQ==")));
   }

   protected void onBindMethods() {
      this.addMethodProxy(new GetUniqueDeviceId());
   }

   private static class GetUniqueDeviceId extends MethodProxies.GetDeviceId {
      private GetUniqueDeviceId() {
      }

      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQVBi9hATA/IBcMLGwjAitiASxF"));
      }

      // $FF: synthetic method
      GetUniqueDeviceId(Object x0) {
         this();
      }
   }
}
