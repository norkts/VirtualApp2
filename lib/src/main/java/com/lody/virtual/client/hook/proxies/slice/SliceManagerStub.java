package com.lody.virtual.client.hook.proxies.slice;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import java.util.Collections;
import mirror.com.android.internal.app.ISliceManager;

public class SliceManagerStub extends BinderInvocationProxy {
   public SliceManagerStub() {
      super(ISliceManager.Stub.TYPE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4ECWszNFo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgYCGczHi99JDBF")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcKGUVBl5gHgY5KAhSVg==")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBg+KWczHi99JDARLy0qPWoKAlo=")), false));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS0MP2ogMF5gHgY5KAYmPWoVPC9sJDAaLD4cVg==")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uLmozQStpJFEzLy0MDGkgRSNqDjA6IxgAKg==")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fM2szQV5gHgY5KAYmPWoVPC9sJDAaLD4cVg==")), 0));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS0MP2ogMExiASw3KQgqL2wjNCZjNAocLBUuD2IFMFo=")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcFAiZgNDAwOy4mPW4KAlo=")), Collections.EMPTY_LIST.toArray()));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcFAiZgNDAwOy1bMW4FGgM=")), Collections.EMPTY_LIST.toArray()));
   }
}
