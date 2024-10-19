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
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getSystemConfigEnabledProfilesForPackage"));
      if (BuildCompat.isS()) {
         this.addMethodProxy(new FixAttributionSourceMethodProxy("enable"));
         this.addMethodProxy(new FixAttributionSourceMethodProxy("enableNoAutoConnect"));
         this.addMethodProxy(new FixAttributionSourceMethodProxy("disable"));
         this.addMethodProxy(new FixAttributionSourceMethodProxy("getAddress"));
         this.addMethodProxy(new FixAttributionSourceMethodProxy("getName"));
         this.addMethodProxy(new FixAttributionSourceMethodProxy("onFactoryReset"));
         this.addMethodProxy(new FixAttributionSourceMethodProxy("enableBle"));
         this.addMethodProxy(new FixAttributionSourceMethodProxy("disableBle"));
      } else {
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("enable"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("disable"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("enableNoAutoConnect"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("updateBleAppCount"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("enableBle"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("disableBle"));
      }

      if (VERSION.SDK_INT >= 17) {
         this.addMethodProxy(new ResultBinderMethodProxy("registerAdapter") {
            public InvocationHandler createProxy(final IInterface base) {
               return new InvocationHandler() {
                  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                     if ("getAddress".equals(method.getName())) {
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
      SERVER_NAME = VERSION.SDK_INT >= 17 ? "bluetooth_manager" : "bluetooth";
   }

   private static class GetAddress extends ReplaceLastPkgMethodProxy {
      public GetAddress() {
         super("getAddress");
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
