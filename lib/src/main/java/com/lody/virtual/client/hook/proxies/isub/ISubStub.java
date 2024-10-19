package com.lody.virtual.client.hook.proxies.isub;

import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import mirror.com.android.internal.telephony.ISub;

public class ISubStub extends BinderInvocationProxy {
   public ISubStub() {
      super(ISub.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2I2sjSFo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVLAZjATw/Oy4MOGQjMC5lJTAcKhgcCg=="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcwNCphJCgqKQgmLmwjNCZkHgocKQguCGYVGlo="))));
      this.addMethodProxy(new StaticMethodProxy(VERSION.SDK_INT >= 24 ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGczAiNpJwo7LBcMWW8KRV5lEQY/OxgqPA==")) : StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGczAiNpJwo7LBcMWW8KRV5vAQo6LT0MI30wLDU="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVLAZjATw/Oy4MOGoFAgRqDjw/IxgAKmsKRSFsJ1RF"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVLAZjATw/Oy4MOGoFAgRqDjw/IxgAKmsKRSFsIjgeLBY2JWghAjA="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVLAZjATw/Oy4MOGoFAgRqDjw/IxgAKmsKRSFsIjgeLBUYI2UILAJqNCwWJj0MLGsVSFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVHiRpJzA6IQcYPG8LOC9sJCxF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVHiRpJzA6IQcYPG8LAiVvARo/"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVLAZjATw/Oy4MOGoFAgRqDjw/IxgAKmsKRSFsIl0iLAccVg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMaODdjDlE7Lz1bPWIKGipsJzA5Ixc6CmMKAilnDh4vKQYiI2wgMFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVLCliASgpKQcuCGkmAgVoNDAqKS4YDmYaGipsMhodLxguAmoKLCA="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2EWswMC9mNDAPLAcuXmkzSFo="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIwIAJgJywgLAcYMWoKBi9oIjAwLS02JWEwGjNqHhoeKRcYVg=="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0MM2saMCtpJzA6Iy0qKGwgTQZqAQYbJj0MKWYFOFo="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uDWowOCtpJzA6Iy0qKGwgTQZqAQYbKTwiCGAgQQxlNFk+LD5SVg=="))));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVLAZjATw/Oy4MOGQjBg5qDjA/")), new int[0]));
   }
}
