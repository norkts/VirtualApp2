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
      super(getInterface(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ECW8FFiV9ASww")));
   }

   private static IInterface getInterface() {
      if (ClipboardManager.getService != null) {
         return (IInterface)ClipboardManager.getService.call();
      } else if (ClipboardManagerOreo.mService != null) {
         android.content.ClipboardManager cm = (android.content.ClipboardManager)VirtualCore.get().getContext().getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ECW8FFiV9ASww")));
         return (IInterface)ClipboardManagerOreo.mService.get(cm);
      } else {
         return ClipboardManagerOreo.sService != null ? (IInterface)ClipboardManagerOreo.sService.get() : null;
      }
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcKFi9gDiAqLQUqCGwgTVo="))));
      if (VERSION.SDK_INT > 17) {
         this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGcKFi9gDiAqLQUqCGwgTVo="))));
         this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcKFi9gDiAqLQUqCGwgTRZrDjAqKS4YDmYaGipsN1RF"))));
         this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBg+KWcKFi9gDiAqLQUqCGwgTVo="))));
         this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPGcKFi9gDiAqLQUqCGwgTRNqETgbLj4uIGgaGjZqHgodLy1fVg=="))));
         this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uDWowOCtpESwzKgciKGghAiRqDjwAIwg+KmIgLC9kHho8Iz4AKmsKFlo="))));
         this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBg+KWMzHi9hHiw1LwguPn0zGjBvEVRF"))));
      }

   }

   public void inject() throws Throwable {
      super.inject();
      if (ClipboardManagerOreo.mService != null) {
         android.content.ClipboardManager cm = (android.content.ClipboardManager)VirtualCore.get().getContext().getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ECW8FFiV9ASww")));
         ClipboardManagerOreo.mService.set(cm, (IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
      } else if (ClipboardManagerOreo.sService != null) {
         ClipboardManagerOreo.sService.set((IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
      }

   }
}
