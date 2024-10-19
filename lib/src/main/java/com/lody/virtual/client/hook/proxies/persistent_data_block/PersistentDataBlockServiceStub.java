package com.lody.virtual.client.hook.proxies.persistent_data_block;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import mirror.android.service.persistentdata.IPersistentDataBlockService;

public class PersistentDataBlockServiceStub extends BinderInvocationProxy {
   public PersistentDataBlockServiceStub() {
      super(IPersistentDataBlockService.Stub.TYPE, "persistent_data_block");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ResultStaticMethodProxy("write", -1));
      this.addMethodProxy(new ResultStaticMethodProxy("read", new byte[0]));
      this.addMethodProxy(new ResultStaticMethodProxy("wipe", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("getDataBlockSize", 0));
      this.addMethodProxy(new ResultStaticMethodProxy("getMaximumDataBlockSize", 0));
      this.addMethodProxy(new ResultStaticMethodProxy("setOemUnlockEnabled", 0));
      this.addMethodProxy(new ResultStaticMethodProxy("getOemUnlockEnabled", false));
   }
}
