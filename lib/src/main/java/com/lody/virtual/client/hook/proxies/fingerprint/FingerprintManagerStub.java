package com.lody.virtual.client.hook.proxies.fingerprint;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import mirror.android.hardware.fingerprint.IFingerprintService;

@TargetApi(23)
public class FingerprintManagerStub extends BinderInvocationProxy {
   public FingerprintManagerStub() {
      super(IFingerprintService.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YCGgzNARhESwzKj42Vg==")));
   }

   protected void onBindMethods() {
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2XWsaFixmJCAqKAU2PWUzGilvESgv"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBg+KWAVBgRgJFEoKAc2WWwjMC1rDgo7KS4YKmYVNFo="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUFNCZmHgY5Lwg2PQ=="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+CGszNCRlATAgKRcMDmUzLCloDiwaLD4cVg=="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAVBgRgJFEoKAc2WWwjMC1rDgo7KS4YKmYVNFo="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMaNAZjHjA2LBccP24gBiVsNR4v"))));
   }
}
