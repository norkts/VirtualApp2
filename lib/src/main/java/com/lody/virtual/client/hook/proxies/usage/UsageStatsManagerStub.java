package com.lody.virtual.client.hook.proxies.usage;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.client.ipc.VActivityManager;
import java.lang.reflect.Method;
import mirror.android.app.IUsageStatsManager;

@TargetApi(22)
public class UsageStatsManagerStub extends BinderInvocationProxy {
   public UsageStatsManagerStub() {
      super(IUsageStatsManager.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2P2gzNANmHiAgIy5SVg==")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMaIAJpJwo7Kj02OGghRQVoJ10gKghSVg=="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KgcuM28gAlBhJCA9KAYqLm4gBgM="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KgcuM28gAhNgJFk+KQc6LWoVQQZqAQYbKT5SVg=="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KgcuM28gAhVmNDA2LBgqVg=="))));
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMaIAJrDlk7Ly42MWUVGlo="))) {
         public Object call(Object who, Method method, Object... args) {
            int userId = args.length > 2 ? (Integer)args[2] : 0;
            VActivityManager.get().setAppInactive((String)args[0], (Boolean)args[1], userId);
            return 0;
         }
      });
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2EW8KIAlgNCA5LBccLGkjSFo="))) {
         public Object call(Object who, Method method, Object... args) {
            int userId = args.length > 1 ? (Integer)args[1] : 0;
            return VActivityManager.get().isAppInactive((String)args[0], userId);
         }
      });
      this.addMethodProxy(new ReplacePkgAndUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS5fCWwFNCRjASggJwgmKn0zGiNsEQY5LRcMI2AVGlo="))));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMaIAJpJwo7Kj02OGghRQVoJ10gKghSVg==")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMaIAJpJwo7Kj02OGghRQVoJ10gKgc2Vg==")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uPWUaLAZiASwRIxgmB2oFQS1rDwYpKT4uCGYwLDU=")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcKmgVPC9hJwo/IzsiKmo2GgNoASAgJD4MD2IFMDFuAShF")), (Object)null));
   }

   private class ReplacePkgAndUserIdMethodProxy extends ReplaceLastPkgMethodProxy {
      public ReplacePkgAndUserIdMethodProxy(String name) {
         super(name);
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (args[args.length - 1] instanceof Integer) {
            args[args.length - 1] = 0;
         }

         return super.call(who, method, args);
      }
   }
}
