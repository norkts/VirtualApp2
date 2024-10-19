package com.lody.virtual.client.hook.proxies.oem.vivo;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import mirror.oem.vivo.IPhysicalFlingManagerStub;

public class PhysicalFlingManagerStub extends BinderInvocationProxy {
   private static final String SERVER_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhhfJ28zAil9DlFAKD1bMW8VEh9sJyg5Ki4YJWIFSFo="));

   public PhysicalFlingManagerStub() {
      super(IPhysicalFlingManagerStub.Stub.TYPE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhhfJ28zAil9DlFAKD1bMW8VEh9sJyg5Ki4YJWIFSFo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2A2waIAJgJywgOxcAIWoFLCloAQJILAgYKmIjSFo="))));
   }
}
