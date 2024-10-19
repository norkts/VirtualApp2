package com.lody.virtual.client.hook.proxies.audio;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import java.lang.reflect.Method;
import mirror.android.media.IAudioService;

public class AudioManagerStub extends BinderInvocationProxy {
   public AudioManagerStub() {
      super(IAudioService.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuPGUVGlo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqMmwaLAZuNB4oLAdXPQ=="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqMmwaLAZoHh45LwdbRGoYRStlAQY/LhU2CmEwLCRsDzgeKT0AL2sFSFo="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqMmwaLAZpJzA9KC0ML2UzGixkJCw5Lhg+L24wAjdqDlEu"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqMmwaLAZpJwoqKAciD30VNCRvAQ4g"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqMmwaLAZoDiApLBcMKH0VNCRvAQ4g"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGcwMARiDiA3JD1fCGUjPCs="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGIVJANmHjAqJD1fCGUjPCs="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGIVAilhNB4sKRdfDmkhPAVvEShF"))) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            replaceLastUserId(args);
            return super.call(who, method, args);
         }
      });
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGcjAiZiJDAqIgdfPmkhGjBvESg5LC4+KA=="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGcjAiZiJDAqIgdfPmkhLCZvESg5LC4+KA=="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGIVGixiAVRF"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgciKmswIF5mASQsKi4uLmoLQSpsJwYdKhcqJ24wAjdqDlEu"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggMP2ojMCVgMiAvKBccDWEVNClvDjBF"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uL2wVNANmHCAvKBccDWEVNClvDjBF"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQzAgRiDgoWKAg+MW4FGhNlJxobLhg2CmMKAil9JzAqIz4AVg=="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGcwICt9Dg4/Iz4mMm8FMCt9JxpF"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMjHgViAQo1Ki42MmIFAiV9JxpF"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qD28LFiRmDjAgKi1fLmw2AillJ1RF"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMBRgETA/LBdfDWUzFl5oJwZF"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgYKWsVFiRiDyg7KD0MUmkjBi9oDCQcLAcuL2IFSFo="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uPWUaLAZiASwAKAdXDWUzGhNlJxo/KS4AKGUgTSxuDh49"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcKmgVPC9hJwo/IzsiLWkzLCVjNwYqKhc2H2AaGiBsNzBF"))));
   }
}
