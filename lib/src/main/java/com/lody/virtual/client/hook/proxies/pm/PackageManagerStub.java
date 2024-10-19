package com.lody.virtual.client.hook.proxies.pm;

import android.content.Context;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.fixer.ContextFixer;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.Reflect;
import mirror.android.app.ActivityThread;
import mirror.huawei.android.app.HwApiCacheManagerEx;

@Inject(MethodProxies.class)
public final class PackageManagerStub extends MethodInvocationProxy<MethodInvocationStub<IInterface>> {
   public PackageManagerStub() {
      super(new MethodInvocationStub((IInterface)ActivityThread.sPackageManager.get()));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPGcFNARgDgYpIy0cDW8bQQNuARoq")), true));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPGcFNARgDgYpIy0cDW8VSFo=")), true));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguKmgjGgRgDAo/LRVfKmUzSFo=")), true));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguKmgjGgRgDAo/LRVfKmUxLC59NyggLgguIA==")), false));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguKmgjGgRgDAo/LRVfKmU2AitoJwYbLgg+CGcFSFo=")), true));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPGIzBkxiASw3KQgqL2wjNCZsJTAZLRgcIWIITSxlJzAuKRgACA==")), 0));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uDWowOCtoJFkCKAguD2wgAgNqAQYbKTw2LH0KRSJuDF0iLAccJ2UzNCY=")), 0));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki5fD2wVHixpJBo1LCwuPWogGitsJCwRLhcML2MFNDZvDlkdJBhbCmoFGgRrEQI0"))));
      if (BuildCompat.isOreo()) {
         this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOD9qHjAaIhdfOWkzSFo=")), 0));
         this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOD9pHiA5KS0iM2kmGgNrAVRF")), 0));
         this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLH0VBgNmHiA2LBUiKmoxAiVlJ10aLhhSVg==")), false));
         this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2XGogLAZ9DlkgJwgmKg==")), false));
      }

      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2AmsVLCF9Djg/Oy4ML2ozGiZrESgvJi4ACG4FNCBlN1RF"))));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fM2szQUx9DigxLwc6PWIKBjdsNCwsLS4EJw==")), 0));
   }

   public void inject() throws Throwable {
      IInterface hookedPM = (IInterface)this.getInvocationStub().getProxyInterface();
      ActivityThread.sPackageManager.set(hookedPM);
      BinderInvocationStub pmHookBinder = new BinderInvocationStub((IInterface)this.getInvocationStub().getBaseInterface());
      pmHookBinder.copyMethodProxies(this.getInvocationStub());
      pmHookBinder.replaceService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iAVRF")));

      try {
         Context systemContext = (Context)Reflect.on(VirtualCore.mainThread()).call(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcwAgNmHjA3Jy1fDmUzGjBvEVRF"))).get();
         Object systemContextPm = Reflect.on((Object)systemContext).field(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwU6P2szQTdiJDANLwcYOWkFGgQ="))).get();
         if (systemContextPm != null) {
            Reflect.on((Object)systemContext).field(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwU6P2szQTdiJDANLwcYOWkFGgQ="))).set(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwU6RA==")), hookedPM);
         }
      } catch (Throwable var5) {
         Throwable e = var5;
         e.printStackTrace();
      }

      ContextFixer.fixContext(VirtualCore.get().getContext(), (String)null);
      if (HwApiCacheManagerEx.mPkg != null) {
         HwApiCacheManagerEx.mPkg.set(HwApiCacheManagerEx.getDefault.call(), VirtualCore.getPM());
      }

   }

   public boolean isEnvBad() {
      return this.getInvocationStub().getProxyInterface() != ActivityThread.sPackageManager.get();
   }
}
