package com.lody.virtual.client.hook.proxies.telephony;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.helper.compat.BuildCompat;
import java.lang.reflect.Method;
import mirror.com.android.internal.telephony.ITelephony;

public class TelephonyStub extends BinderInvocationProxy {
   public TelephonyStub() {
      super(ITelephony.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhhfD2ojNFo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIFAiZiDSAMLAdXOGkgRQhlJApKIxc2DmAaPAY="))));
      this.addMethodProxy(new MethodProxies.GetCellLocation());
      this.addMethodProxy(new MethodProxies.GetAllCellInfoUsingSubId());
      this.addMethodProxy(new MethodProxies.GetAllCellInfo());
      this.addMethodProxy(new MethodProxies.GetNeighboringCellInfo());
      this.addMethodProxy(new MethodProxies.GetDeviceId());
      this.addMethodProxy(new MethodProxies.GetImeiForSlot());
      this.addMethodProxy(new MethodProxies.GetMeidForSlot());
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+DmoFSFo="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2A2UVEkxjDlkVKj0iOG8zGiw="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzMCN9DDAqKQUcP28FMAllNywgLwhSVg=="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzMCN9DDAqKQUcP28FMAllNywgLwYiKWExNDBpNywsLBg2JmsKFlo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzMCN9DDAqKQUcP28FMA1lJywg"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzMCN9DDAqKQUcP28FMA1lJywgJi4ACGkjLCVlJCw7Ki5fJ2wzSFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzMCN9DDAqKQY2PWgwBlo="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzMCN9DDAqKQY2PWgwBghlJAoQKhgMD30jMCxpNAo7"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjNAZmJB4qKSw2IWozGghlJAoQKhgMD30jMCxpNAo7"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFJAZ9DFk/LBg6DWoVJFFuDjwg"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFJAZ9DFk/LBg6DWoVJFFuDjwgJi4ACGkjLCVlJCw7Ki5fJ2wzSFo="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjGi99JDAMKAg2I28KRSFnHh47LhYiKWExNDBpNywsLBg2JmsKFlo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIKMCtoJFkfKBdXOWcjNCxrAVRF"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIKMCtoJFkfKBdXOWcjNCxrDyQcKSs2CX0zNCZlNBorLy1fVg=="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzJCR9JzAoLwg2PWk2TQRrASQgKS0MJ2IYRSBqETweLBg+XW4KID8="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcFLAN9JDwRKBc2KGkgAgM="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIFAiZiDSARKhgmMm4mBjdrJSQcKSwqI2EjODdpARpF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIVNARiJDAwOy4MOGoFAgRqAQogKSwYIGEjSFo="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcjJCxjDh4RLy0qPWoKAghoAQ4aLAcYVg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2AGUVMCtgIig7KhdbMW8VEhVlNzgpLAguIA=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFND5jDig/Oy1fPGUwEjdsNygTLhcMD2MKAilmNFk7JAgiKW8VSFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGczNARmNAY5KAYqLm4gBitjNwY5IT0uJmEgNDVvDiguLBhSVg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjAgNmDiAoJD1fMW4FGiNoAR4dIQg+JWMgPCJuDB4qKS4AVg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcP2sjHituNAYpLAciCH0VNC9oJygeLRgYKGkgQTZmNBobIz4ACA=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgYKWsVFiRiDzwzIy4MOW82HiVqATAgLBg+I2AbNChlIjgiKT0cJ2wzSFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjAgNmDiAoJD1fMW4FGiNoAR4dIT4ID2owGjdqHgo7JAgACm8VAgRoNDBF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgIOC9hJzA7KhY+DWwjAitlATgaLAU2L2EmICplNSw+KBcYJWwzAjZoHgpF"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjGi99JDARLy42MWUVQQZqAQYbIT0qO2YaLFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFJAZ9DCA5LBccLG4gBi9lJxoQKgg+CmIFSFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjGi99JDANLwccCGYjOAJqETgVLRgmBGAjMBBqDig8KAdfI2gzNCY="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgLMC99DlE/IzwqKmkjAi9oAQIALD4qJw=="))));
      if (BuildCompat.isOreo()) {
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQjGi99JDA3LwccCH0VLCpsNzg/IxgAKmoKRSRpNF0uLz5SVg=="))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQjGi99JDA3LwccCGIVLCZrJCwcLC4uXGEwGlo="))));
      }

      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2UmgjOCBgJB4x"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2UmgjOCBgJB4xID1fKGIKGipsJzA5IxgMJ2EzSFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2DGUVBi1jDlk9"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2DGUVBi1jDlk9ID1fKGIKGipsJzA5IxgMJ2EzSFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2XGgFHis="))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2XGgFHitqNB4qOy4MOGoFAgRqAQogKS5SVg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2DGsVMC9gIh42"))));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2DGsVMC9gIh42ID1fKGIKGipsJzA5IxgMJ2EzSFo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzHi9iDlkgOz0MKWUjGgNvHDA/LRcqDw=="))));
      if (!VirtualCore.get().isSystemApp()) {
         this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjAgNmDiAoJD1fMW4FGiNoAR4dIT4uCmYaGiluJyxF")), (Object)null));
         this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFJAZ9DDA2LwcuCGkjBlo=")), 0));
         this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFJAZ9DDA2LwcuCGkjBlo=")), false));
      }

      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFND5jDig/IQc2BWwgBiBjNygsKgcuCGIFSFo="))) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            try {
               return super.call(who, method, args);
            } catch (SecurityException var5) {
               return null;
            }
         }
      });
   }
}
