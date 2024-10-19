package com.lody.virtual.client.hook.proxies.display;

import android.annotation.TargetApi;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.hardware.display.DisplayManagerGlobal;

@TargetApi(17)
public class DisplayStub extends MethodInvocationProxy<MethodInvocationStub<IInterface>> {
   public DisplayStub() {
      super(new MethodInvocationStub((IInterface)DisplayManagerGlobal.mDm.get(DisplayManagerGlobal.getInstance.call())));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0MM2saMCtuNAYqLBgMOW8xBi9sJDwdLRcYVg=="))));
   }

   public void inject() throws Throwable {
      Object dmg = DisplayManagerGlobal.getInstance.call();
      DisplayManagerGlobal.mDm.set(dmg, (IInterface)this.getInvocationStub().getProxyInterface());
   }

   public boolean isEnvBad() {
      Object dmg = DisplayManagerGlobal.getInstance.call();
      IInterface mDm = (IInterface)DisplayManagerGlobal.mDm.get(dmg);
      return mDm != this.getInvocationStub().getProxyInterface();
   }
}
