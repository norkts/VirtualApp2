package com.lody.virtual.client.hook.proxies.connectivity;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import mirror.android.net.IConnectivityManager;

public class ConnectivityStub extends BinderInvocationProxy {
   public ConnectivityStub() {
      super(IConnectivityManager.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGojNClmHgYuKQg2IQ==")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2BmgaMCBiASwzKj06AWUgTQJlJAo/LhgqVg==")), true));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uL2wVNANmHFk/LBg6DWoVJFo="))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjNAZmJB4qKSsqOWozQSpqAQIaKggYJ2EjSFo="))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgYKWwFNCZqNB4qIj0MLmUFNARqJ1RF"))));
   }
}
