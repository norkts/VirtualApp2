package com.lody.virtual.client.hook.proxies.wallpaper;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.app.IWallpaperManager;

public class WallpaperManagerStub extends BinderInvocationProxy {
   public WallpaperManagerStub() {
      super(IWallpaperManager.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4+DmoKIDdhHjAq")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQzJCRgESQ7IxcMKA=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQzJCRgESQ7IxcMKA=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFAiNiDlkpKQdfDmQzLCZvHjBF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFAgNhHlE7LQYmOWkzBi9lNyBF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4EM2saFlJ9DlEoIxciKmkgRVo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQzJCRgESQ7IxcMKGYFNCNsEQYbLhgcCmUgBiBpJAIuLz5SVg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2B2sVHiRhHiAsKAguAWUgTQJlJAo/LhgqVg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2A2gaMFJ9DlEoIxciKmkgRRFlEQIcKj4uIA=="))));
   }

   public void inject() throws Throwable {
      super.inject();
   }
}
