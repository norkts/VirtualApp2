package com.lody.virtual.client.hook.proxies.clipboard;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import mirror.android.sec.clipboard.IClipboardService;

public class SemClipBoardStub extends BinderInvocationProxy {
   public SemClipBoardStub() {
      super(IClipboardService.Stub.asInterface, "semclipboard");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getClipData"));
   }

   public void inject() throws Throwable {
      super.inject();
   }
}
