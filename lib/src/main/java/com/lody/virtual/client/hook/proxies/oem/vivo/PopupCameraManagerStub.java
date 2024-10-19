package com.lody.virtual.client.hook.proxies.oem.vivo;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import mirror.oem.vivo.IPopupCameraManager;

public class PopupCameraManagerStub extends BinderInvocationProxy {
   private static final String SERVER_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAKGwaIB99JCA3KAguOWMKAitsNCQaLT4uVg=="));

   public PopupCameraManagerStub() {
      super(IPopupCameraManager.Stub.TYPE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAKGwaIB99JCA3KAguOWMKAitsNCQaLT4uVg==")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOD9lJCA3KAguOWIKBjdvHig6"))));
   }
}
