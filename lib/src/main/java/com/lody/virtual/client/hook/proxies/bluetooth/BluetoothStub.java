package com.lody.virtual.client.hook.proxies.bluetooth;

import android.os.IInterface;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.FixAttributionSourceMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultBinderMethodProxy;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.remote.VDeviceConfig;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import mirror.android.bluetooth.IBluetooth;

public class BluetoothStub extends BinderInvocationProxy {
   private static final String SERVER_NAME;

   public BluetoothStub() {
      super(IBluetooth.Stub.asInterface, SERVER_NAME);
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new GetAddress());
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcwAgNmHjA3Jy1fDmkVLC1jARosLS4EJ2IbODVsJDgiKT4AD2MzGiZnATg2JS0mLm4FSFo="))));
      if (BuildCompat.isS()) {
         this.addMethodProxy(new FixAttributionSourceMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcP2sjHis="))));
         this.addMethodProxy(new FixAttributionSourceMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcP2sjHitoNB4RLAg2DWYFNCZlNygqKghSVg=="))));
         this.addMethodProxy(new FixAttributionSourceMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgYKWsVFiRiAVRF"))));
         this.addMethodProxy(new FixAttributionSourceMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVMCxhNDApIy5SVg=="))));
         this.addMethodProxy(new FixAttributionSourceMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjJCNiAVRF"))));
         this.addMethodProxy(new FixAttributionSourceMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cW2sVLAZgJywZOz0ML2kgBlo="))));
         this.addMethodProxy(new FixAttributionSourceMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcP2sjHitlNFE/"))));
         this.addMethodProxy(new FixAttributionSourceMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgYKWsVFiRiDCwoKAhSVg=="))));
      } else {
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcP2sjHis="))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgYKWsVFiRiAVRF"))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcP2sjHitoNB4RLAg2DWYFNCZlNygqKghSVg=="))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc6PGsaMCtlNFE/JwgmKmYFNAVlNCxF"))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcP2sjHitlNFE/"))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgYKWsVFiRiDCwoKAhSVg=="))));
      }

      if (VERSION.SDK_INT >= 17) {
         this.addMethodProxy(new ResultBinderMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uPWUaLAZiASwRKBciKmUzGgQ="))) {
            public InvocationHandler createProxy(final IInterface base) {
               return new InvocationHandler() {
                  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                     if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVMCxhNDApIy5SVg==")).equals(method.getName())) {
                        //FIXME
//                        VDeviceConfig config = null.getDeviceConfig();
//                        if (config.enable) {
//                           String mac = null.getDeviceConfig().bluetoothMac;
//                           if (!TextUtils.isEmpty(mac)) {
//                              return mac;
//                           }
//                        }
                     }

                     return method.invoke(base, args);
                  }
               };
            }
         });
      }

   }

   static {
      SERVER_NAME = VERSION.SDK_INT >= 17 ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4EI2gaMCVgJwo0Ji1XOW8VQS1rDgpF")) : StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4EI2gaMCVgJwo0"));
   }

   private static class GetAddress extends ReplaceLastPkgMethodProxy {
      public GetAddress() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVMCxhNDApIy5SVg==")));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         VDeviceConfig config = getDeviceConfig();
         if (config.enable) {
            String mac = getDeviceConfig().bluetoothMac;
            if (!TextUtils.isEmpty(mac)) {
               return mac;
            }
         }

         return super.call(who, method, args);
      }
   }
}
