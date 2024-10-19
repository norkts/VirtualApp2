package com.lody.virtual.client.hook.proxies.input;

import android.annotation.TargetApi;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceLastUserIdMethodProxy;
import mirror.com.android.internal.view.inputmethod.InputMethodManager;

@Inject(MethodProxies.class)
@TargetApi(16)
public class InputMethodManagerStub extends BinderInvocationProxy {
   public InputMethodManagerStub() {
      super((IInterface)InputMethodManager.mService.get(VirtualCore.get().getContext().getSystemService("input_method")), "input_method");
   }

   public void inject() throws Throwable {
      Object inputMethodManager = this.getContext().getSystemService("input_method");
      InputMethodManager.mService.set(inputMethodManager, (IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
      ((BinderInvocationStub)this.getInvocationStub()).replaceService("input_method");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("getInputMethodList"));
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("getEnabledInputMethodList"));
   }

   public boolean isEnvBad() {
      Object inputMethodManager = this.getContext().getSystemService("input_method");
      return InputMethodManager.mService.get(inputMethodManager) != ((BinderInvocationStub)this.getInvocationStub()).getBaseInterface();
   }
}
