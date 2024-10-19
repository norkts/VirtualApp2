package com.lody.virtual.client.hook.proxies.textservices;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstUserIdMethodProxy;
import mirror.com.android.internal.textservice.ITextServicesManager;

public class TextServicesManagerServiceStub extends BinderInvocationProxy {
   public TextServicesManagerServiceStub() {
      super(ITextServicesManager.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRguIGwKLCthNzwzLy0MLw==")));
   }

   protected void onBindMethods() {
      access$001(this);
      this.addMethodProxy(new ReplaceFirstUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMwNARhNDA2LBYqKmkjOCRgJ1kgLT5bJ2EzSFo="))));
      this.addMethodProxy(new ReplaceFirstUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMwNARhNDA2LBYqKmkjOCRgJ1kgLT5bJ2ExNDBpNzAyLD4AVg=="))));
      this.addMethodProxy(new ReplaceFirstUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcwICtgHlEfKRcMP2wFGgRkJyg5Ki4YJWIFSFo="))));
      this.addMethodProxy(new ReplaceFirstUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YCGUaLCBpJyQ/KhdbEWwzGilqJyg5IT4uCGYwGiZuAVRF"))));
      this.addMethodProxy(new ReplaceFirstUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2A28FNCRgHCg0KAcqCWkgRRVlNzgpLAguIA=="))));
      this.addMethodProxy(new ReplaceFirstUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAVBjd9NFE/KBYqKmkjOCRgJ1kgLT5bJ2EzNFo="))));
   }

   // $FF: synthetic method
   static void access$001(TextServicesManagerServiceStub x0) {
      x0.onBindMethods();
   }
}
