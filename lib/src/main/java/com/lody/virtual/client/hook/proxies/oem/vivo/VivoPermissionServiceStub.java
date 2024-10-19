package com.lody.virtual.client.hook.proxies.oem.vivo;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgAndLastUserIdMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastUidMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.Method;
import mirror.oem.vivo.IVivoPermissonService;

public class VivoPermissionServiceStub extends BinderInvocationProxy {
   private static final String SERVER_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YLmo2GgJiASw3KQgqL2wjNCZhJDAgKS0iI30gLFo="));

   public VivoPermissionServiceStub() {
      super(IVivoPermissonService.Stub.TYPE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YLmo2GgJiASw3KQgqL2wjNCZhJDAgKS0iI30gLFo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceLastUidMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fM2szQUxiASw3KQgqL2wjNCY="))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMaIAJpHjAqKgccL2oFLCVlN1RF"))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMaIAJpHjAqKgccL2oFLCVlN1RF"))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQzRS9mHjAOKQgqLmYgTQI="))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMjHjd9JA4OKQgqLmYgTQI="))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGgYLAZ9ASwgJwcqLmwgHi9vHh4RKS4AJWIFNDY="))));
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2HGwVAiRiHAY2JBcAMWoVBkxoDgo/JRc6Dg=="))));
      this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGIzBitpHjAqKgccL2oFLCVlN1RF"))));
      this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGIzBitpHjAqKgccL2oFLCVlNSgzKghSVg=="))));
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fM2szQRZiDlE/LBcMVg=="))) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            if (args[1] instanceof String) {
               args[1] = getHostPkg();
            }

            replaceLastUserId(args);
            return super.call(who, method, args);
         }
      });
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2AGUaOCVrDl0/KQYmCWkFSFo="))));
   }
}
