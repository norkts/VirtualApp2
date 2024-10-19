package com.lody.virtual.client.hook.proxies.phonesubinfo;

import android.content.Context;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.helper.compat.BuildCompat;
import java.lang.reflect.Method;
import mirror.com.android.internal.telephony.IPhoneSubInfo;

@Inject(MethodProxies.class)
public class PhoneSubInfoStub extends BinderInvocationProxy {
   public PhoneSubInfoStub() {
      super(IPhoneSubInfo.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc6CmozBithJzA6KQcYPG8FSFo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjJC9qNB4qOy4MOGoFAgRqAQogKS5SVg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFND5jDig/Oy4+Dg=="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFND5jDig/Oy4+Dn0gAi9lNyAQKhgMBWIVSFo="))));
      this.addMethodProxy(new GetSubscriberId());
      this.addMethodProxy(new GetSubscriberIdForSubscriber());
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAwFiVmASQJKBVbPWUVGiR8AVRF"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAwFiVmASQJKBVbPWUVGiR8DyQcKSs2CX0zNCZlNBorLy1fVg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIFAiZiDSARKhgmMm4mBjdrJ1RF"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIFAiZiDSARKhgmMm4mBjdrJSQcKSs2CX0zNCZlNBorLy1fVg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIaLC9hJAo2"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIaLC9hJAo2ID1fKGIKGipsJzA5IxgMJ2EzSFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjGi99JDANLwccCGcaGiNoNyg5"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjGi99JDANLwccCGcaGiNoNyg5Ji4ACGkjLCVlJCw7Ki5fJ2wzSFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjGi99JDANLwccCGYjOAJqETgVLRgmVg=="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjGi99JDANLwccCGYjOAJqETgVLRgmBGAjMBBqDig8KAdfI2gzNCY="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIFAiZiDSAMLAdXOGkgRVo="))));
      this.addMethodProxy(new GetLine1NumberForSubscriber());
   }

   class GetLine1NumberForSubscriber extends ReplaceCallingPkgMethodProxy {
      public GetLine1NumberForSubscriber() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIFAiZiDSAMLAdXOGkgRQhlJAoQKhgMD30jMCxpNAo7")));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         try {
            Context context = VirtualCore.get().getContext();
            if (context != null) {
               int p1 = context.checkCallingPermission(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsMGWUIFl99HAZXIRYAE302NEhjJShALy5SVg==")));
               int p2 = context.checkCallingPermission(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsMGWUIFl99IlES")));
               int p3 = context.checkCallingPermission(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsMGWUIFl99HAZXIRYAE2QmMB1kDyhF")));
               if (p1 == -1 && p2 == -1 && p3 == -1) {
                  return null;
               }
            }

            return super.call(who, method, args);
         } catch (Throwable var8) {
            Throwable th = var8;
            th.printStackTrace();
            return null;
         }
      }
   }

   class GetSubscriberIdForSubscriber extends ReplaceCallingPkgMethodProxy {
      public GetSubscriberIdForSubscriber() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcwNCphJCgqKQcuPWobLCxjNwY5IT0uJmEgNDVvDiguLBhSVg==")));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         try {
            return BuildCompat.isQ() ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcMWojGj1gN1RF")) : super.call(who, method, args);
         } catch (Throwable var5) {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcMWojGj1gN1RF"));
         }
      }
   }

   class GetSubscriberId extends ReplaceCallingPkgMethodProxy {
      public GetSubscriberId() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcwNCphJCgqKQcuPWobLCw=")));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         try {
            return BuildCompat.isQ() ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcMWojGj1gN1RF")) : super.call(who, method, args);
         } catch (Throwable var5) {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcMWojGj1gN1RF"));
         }
      }
   }
}
