package com.lody.virtual.client.hook.proxies.clipboard;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import mirror.android.sec.clipboard.IClipboardService;

public class SemClipBoardStub extends BinderInvocationProxy {
   public SemClipBoardStub() {
      super(IClipboardService.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uDWszHi9hHiw1LwguPg==")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzHi9hHAo7LBciVg=="))));
   }

   public void inject() throws Throwable {
      super.inject();
   }
}
