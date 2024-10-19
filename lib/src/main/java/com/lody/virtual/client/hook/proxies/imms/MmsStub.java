package com.lody.virtual.client.hook.proxies.imms;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceSpecPkgMethodProxy;
import mirror.com.android.internal.telephony.IMms;

public class MmsStub extends BinderInvocationProxy {
   public MmsStub() {
      super(IMms.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgIDW8zSFo=")));
   }

   protected void onBindMethods() {
      this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgLEithJyg7KC0MVg==")), 1));
      this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgALWojHiV9DgoNKAgqL24jEis=")), 1));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgIKGowFgZuHjAaLBVXPWoKAjdrJyhF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgIKGowFgZoATAoLBccD2kjBi9oDw4gKT02O2IgLFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguDmgaMCtpJwo1Iz0MPmcjGgNsJzguLhhSVg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguDmgaMCtpJwo1Iz0MPmYFNCZvNyg5KT4+CmMKAik="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc6PGsaMCtpJwo1Iz0MPmcjGgNsJzguLhU2Cn0FFjBlJ1RF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcMOWUFAj5iDyggKi4uPWkxAiVlNCQgKS02O2YaGipsN1RF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPGQFNDBmHF0/Iy4qOWkFGhZsNzgtKghSVg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPGIaNCRmHgY3KAc2MW4hPCtsJDAsLj4uGmEwPCFqEVRF"))));
      this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgILAZgJyw/KBVXPWoKAjdrJyhF")), 1));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMaNAZgJSQ/Iz4qMWoKBi9lNyBF"))));
   }
}
