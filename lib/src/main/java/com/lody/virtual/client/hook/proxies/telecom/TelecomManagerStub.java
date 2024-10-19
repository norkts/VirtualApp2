package com.lody.virtual.client.hook.proxies.telecom;

import android.annotation.TargetApi;
import android.content.Context;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.Method;
import mirror.com.android.internal.telecom.ITelecomService;

@TargetApi(21)
public class TelecomManagerStub extends BinderInvocationProxy {
   public TelecomManagerStub() {
      super(ITelecomService.Stub.TYPE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRguDmgVLCVgAVRF")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uPWUaLAZiASwCKRdfDmkhQSloJwYwLC0qVg=="))) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            return 0;
         }
      });
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki5fD2wxAiZlJCAoKhYqP2oVGitlN1RF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFNC59ATAoLBVfLWUzEiVqARouIQhfKWAwLA5pJCweIy4qCg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQaLCthNSg/KhcMP2UzGix9JCg/Lj4AI2AwJB1vHlkdLyxbJWgjGi9qJCxF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzJCRgHCg7IxciOG8zGkxqEQYbLhY+JX0gAjBsNzA8"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGczNCRiMl07Kj0iM2kjBkxqEQYbLhY+JX0gAjBsNzA8"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIwPCZpJDAoKDtXOW8VQS1rASwRIwgAKmIIPCZpJFk+KRccDw=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcFRSVgNDARLy0qDWUjMAZsIjAwKQc6KWEzFixsNDwSKAgMJ2UFNFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcFRSVgNDARLy0qDWUjMAZsJSQcKSs6O30gEiRuJApF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcFRSVgNDARLy0qDWUjMAY="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4EM2saFhF9JCg1LAcYLmoFSFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2AGozAiliDF07KQdbQGUjPCprDgpF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjGi99JDANLwccCGcaGiNoNyg5"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIFAiZiDSAMLAdXOGkgRVo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YDmgVBiliDywzKj06PWoVSFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2XGohLDdgHlFF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2XGohEjdgNCA9KAc2EW4jOCQ="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2DGUVBi1jDlk9"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWgaIAZpNAY2KC0cDmkLAjdlEQJF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWgaIAZpNAY2KC0cDmkLAjdlEQIUIxcqLG4wGi9uDlkSIz5bCmsJRVo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+CGszNCRoDgYpIy0MPmYFQSRlHjBALD0qI2IwGiZpATAiKQgqVg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBg+CGgFHitpHgY2IgdXMQ=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBg+CGgFHitpHgY2IgdXMWEVNARkEVkcLC4uHX0gNCpqDh49"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVMCZuASwzID1fKGIzFiVlNygCLT42KWYKRT8="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2BmwKAl5mASQsKi4uLmkjBlo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMwNARhNDA2LBY2LmghPCVrEShF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgEP2szNBN9DlEo"))));
      this.addMethodProxy(new GetCallStateUsingPackage());
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcPGMzJCRgEVRF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMBNgJFk+KAguPW8VAis="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBU2MW4jOCtsN1RF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2XGojLCVgDgY2KCsqOW8zOExrDgoeIxcqCmIKFlo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2UmwaMC1gJAY2KCsqOW8zOExrDgoeIxcqCmIKFlo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2XGomLCtgHjwNLwcYOWkFGixgJzgdLAhSVg=="))));
   }

   class GetCallStateUsingPackage extends ReplaceCallingPkgMethodProxy {
      public GetCallStateUsingPackage() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzJCRgHyggLwg2PX0gAi9lNyARLRg2LX0KJCA=")));
      }

      public Object call(Object obj, Method method, Object... args) throws Throwable {
         Context context = VirtualCore.get().getContext();
         return context != null && context.checkCallingPermission(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsMGWUIFl99HAZXIRYAE2QmMB1kDyhF"))) == 0 ? 0 : super.call(obj, method, args);
      }
   }
}
