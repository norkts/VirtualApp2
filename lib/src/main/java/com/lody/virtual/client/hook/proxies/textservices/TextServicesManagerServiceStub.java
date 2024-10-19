package com.lody.virtual.client.hook.proxies.textservices;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstUserIdMethodProxy;
import mirror.com.android.internal.textservice.ITextServicesManager;

public class TextServicesManagerServiceStub extends BinderInvocationProxy {
   public TextServicesManagerServiceStub() {
      super(ITextServicesManager.Stub.asInterface, "textservices");
   }

   protected void onBindMethods() {
      access$001(this);
      this.addMethodProxy(new ReplaceFirstUserIdMethodProxy("getCurrentSpellChecker"));
      this.addMethodProxy(new ReplaceFirstUserIdMethodProxy("getCurrentSpellCheckerSubtype"));
      this.addMethodProxy(new ReplaceFirstUserIdMethodProxy("getSpellCheckerService"));
      this.addMethodProxy(new ReplaceFirstUserIdMethodProxy("finishSpellCheckerService"));
      this.addMethodProxy(new ReplaceFirstUserIdMethodProxy("isSpellCheckerEnabled"));
      this.addMethodProxy(new ReplaceFirstUserIdMethodProxy("getEnabledSpellCheckers"));
   }

   // $FF: synthetic method
   static void access$001(TextServicesManagerServiceStub x0) {
      x0.onBindMethods();
   }
}
