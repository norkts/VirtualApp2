package com.lody.virtual.client.hook.proxies.telephony;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import mirror.com.android.internal.telephony.IHwTelephony;

@Inject(MethodProxies.class)
public class HwTelephonyStub extends BinderInvocationProxy {
   public HwTelephonyStub() {
      super(IHwTelephony.Stub.TYPE, "phone_huawei");
   }

   protected void onBindMethods() {
      this.addMethodProxy(new GetUniqueDeviceId());
   }

   private static class GetUniqueDeviceId extends MethodProxies.GetDeviceId {
      private GetUniqueDeviceId() {
      }

      public String getMethodName() {
         return "getUniqueDeviceId";
      }

      // $FF: synthetic method
      GetUniqueDeviceId(Object x0) {
         this();
      }
   }
}
