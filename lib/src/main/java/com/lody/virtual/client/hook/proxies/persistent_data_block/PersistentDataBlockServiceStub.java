package com.lody.virtual.client.hook.proxies.persistent_data_block;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import mirror.android.service.persistentdata.IPersistentDataBlockService;

public class PersistentDataBlockServiceStub extends BinderInvocationProxy {
   public PersistentDataBlockServiceStub() {
      super(IPersistentDataBlockService.Stub.TYPE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguKm8zAgNmHjA2LBZfPm4gBjdhJwodLD42LQ==")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS0MCWwFNFo=")), -1));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uP2gFSFo=")), new byte[0]));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4YKGgVSFo=")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFJAZ9DCwoKi0qCWIFLDJrAVRF")), 0));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIVJDBjDl0vKgU2OWUzQRRlEQYqIzs2I2cwLFo=")), 0));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGIzNCNuDlkoKi0qCWEjMDdoNwIgLghSVg==")), 0));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIzNCNuDlkoKi0qCWEjMDdoNwIgLghSVg==")), false));
   }
}
