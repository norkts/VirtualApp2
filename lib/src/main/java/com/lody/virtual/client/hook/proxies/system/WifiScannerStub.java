package com.lody.virtual.client.hook.proxies.system;

import android.net.wifi.IWifiScanner;
import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.os.Looper;
import android.os.Messenger;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import java.util.ArrayList;
import mirror.android.net.wifi.WifiScanner;
import mirror.android.os.ServiceManager;

public class WifiScannerStub extends BinderInvocationProxy {
   private static final String SERVICE_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4YPmUaLCl9Dlk2KAguVg=="));

   public WifiScannerStub() {
      super((IInterface)(new EmptyWifiScannerImpl()), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4YPmUaLCl9Dlk2KAguVg==")));
   }

   public void inject() throws Throwable {
      if (ServiceManager.checkService.call(SERVICE_NAME) == null) {
         super.inject();
      }

   }

   static class EmptyWifiScannerImpl extends IWifiScanner.Stub {
      private final Handler mHandler = new Handler(Looper.getMainLooper());

      public Messenger getMessenger() {
         return new Messenger(this.mHandler);
      }

      public Bundle getAvailableChannels(int band) {
         Bundle bundle = new Bundle();
         bundle.putIntegerArrayList((String)WifiScanner.GET_AVAILABLE_CHANNELS_EXTRA.get(), new ArrayList(0));
         return bundle;
      }
   }
}
