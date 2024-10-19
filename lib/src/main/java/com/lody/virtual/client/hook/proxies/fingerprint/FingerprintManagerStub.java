package com.lody.virtual.client.hook.proxies.fingerprint;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import mirror.android.hardware.fingerprint.IFingerprintService;

@TargetApi(23)
public class FingerprintManagerStub extends BinderInvocationProxy {
   public FingerprintManagerStub() {
      super(IFingerprintService.Stub.asInterface, "fingerprint");
   }

   protected void onBindMethods() {
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("isHardwareDetected"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("hasEnrolledFingerprints"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("authenticate"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("cancelAuthentication"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getEnrolledFingerprints"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getAuthenticatorId"));
   }
}
