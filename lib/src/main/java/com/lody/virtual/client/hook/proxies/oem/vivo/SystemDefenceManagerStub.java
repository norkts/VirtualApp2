package com.lody.virtual.client.hook.proxies.oem.vivo;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import mirror.oem.vivo.ISystemDefenceManager;

public class SystemDefenceManagerStub extends BinderInvocationProxy {
   private static final String SERVER_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YKWwFNCNsJAo/KD0MDm4FGh9sJyg5Ki4YJWIFSFo="));

   public SystemDefenceManagerStub() {
      super(ISystemDefenceManager.Stub.TYPE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YKWwFNCNsJAo/KD0MDm4FGh9sJyg5Ki4YJWIFSFo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fM2szQVFhNCA2Iy0cLmwjNCZnER4eLD0uCmoFMDVsJyhILy4EJ2UzLD8="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fM2szQV5jJAYsIS0cCG8zGixgNB4fLhgIKWYwLFRpASwa"))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fM2szQV5gDiAoKhUcP28FMABnDwJTIQg+JWMgPCJuAVRF"))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fM2szQRZiDlE7LQYMKmkzQQZrAVRF"))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cA2gaMBF9JwozLD0cLmgmRStsJCgeLhgqVg=="))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fM2szQV9iDgY2Iy42OW8zOExoATAsIz4mJw=="))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKGowFgZqNDgfIz0iL2wxBjdvEThF"))));
   }
}
