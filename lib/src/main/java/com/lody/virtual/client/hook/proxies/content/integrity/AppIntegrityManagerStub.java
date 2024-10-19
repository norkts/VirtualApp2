package com.lody.virtual.client.hook.proxies.content.integrity;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.helper.compat.ParceledListSliceCompat;
import java.util.Collections;
import mirror.android.content.integrity.IAppIntegrityManager;

public class AppIntegrityManagerStub extends BinderInvocationProxy {
   private static final String SERVER_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGYzAiZmHjA9Iz0cLmgjSFo="));

   public AppIntegrityManagerStub() {
      super(IAppIntegrityManager.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGYzAiZmHjA9Iz0cLmgjSFo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc6PGsaMCtpNzAoKAYqPWUzSFo=")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMwNARhNDA2LBYuLW8zGl5rDiwTLhcMD2MKAik=")), ""));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMwNARhNDA2LBYuLW8zGl5rDiwRKS4AMmMKFiBlN1RF")), ""));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMwNARhNDA2LBYuLW8zGgM=")), ParceledListSliceCompat.create(Collections.emptyList())));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQzRS9mHjAoKQgqLmkjBl9vAQIgIQcMKWYwGi9uASg8")), Collections.emptyList()));
   }
}
