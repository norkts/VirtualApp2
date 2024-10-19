package com.lody.virtual.client.hook.proxies.clipboard;

import android.os.IInterface;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgAndLastUserIdMethodProxy;
import mirror.android.content.ClipboardManager;
import mirror.android.content.ClipboardManagerOreo;

public class ClipBoardStub extends BinderInvocationProxy {
   public ClipBoardStub() {
      super(getInterface(), "clipboard");
   }

   private static IInterface getInterface() {
      if (ClipboardManager.getService != null) {
         return (IInterface)ClipboardManager.getService.call();
      } else if (ClipboardManagerOreo.mService != null) {
         android.content.ClipboardManager cm = (android.content.ClipboardManager)VirtualCore.get().getContext().getSystemService("clipboard");
         return (IInterface)ClipboardManagerOreo.mService.get(cm);
      } else {
         return ClipboardManagerOreo.sService != null ? (IInterface)ClipboardManagerOreo.sService.get() : null;
      }
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("getPrimaryClip"));
      if (VERSION.SDK_INT > 17) {
         this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("setPrimaryClip"));
         this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("getPrimaryClipDescription"));
         this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("hasPrimaryClip"));
         this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("addPrimaryClipChangedListener"));
         this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("removePrimaryClipChangedListener"));
         this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("hasClipboardText"));
      }

   }

   public void inject() throws Throwable {
      super.inject();
      if (ClipboardManagerOreo.mService != null) {
         android.content.ClipboardManager cm = (android.content.ClipboardManager)VirtualCore.get().getContext().getSystemService("clipboard");
         ClipboardManagerOreo.mService.set(cm, (IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
      } else if (ClipboardManagerOreo.sService != null) {
         ClipboardManagerOreo.sService.set((IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
      }

   }
}
