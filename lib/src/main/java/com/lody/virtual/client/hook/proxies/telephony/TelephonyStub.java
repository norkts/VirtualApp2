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
      super(ITelephony.Stub.asInterface, "phone");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getLine1NumberForDisplay"));
      this.addMethodProxy(new MethodProxies.GetCellLocation());
      this.addMethodProxy(new MethodProxies.GetAllCellInfoUsingSubId());
      this.addMethodProxy(new MethodProxies.GetAllCellInfo());
      this.addMethodProxy(new MethodProxies.GetNeighboringCellInfo());
      this.addMethodProxy(new MethodProxies.GetDeviceId());
      this.addMethodProxy(new MethodProxies.GetImeiForSlot());
      this.addMethodProxy(new MethodProxies.GetMeidForSlot());
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("call"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("isSimPinEnabled"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getCdmaEriIconIndex"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getCdmaEriIconIndexForSubscriber"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getCdmaEriIconMode"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getCdmaEriIconModeForSubscriber"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getCdmaEriText"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getCdmaEriTextForSubscriber"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getNetworkTypeForSubscriber"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getDataNetworkType"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getDataNetworkTypeForSubscriber"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getVoiceNetworkTypeForSubscriber"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getLteOnCdmaMode"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getLteOnCdmaModeForSubscriber"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getCalculatedPreferredNetworkType"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getPcscfAddress"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getLine1AlphaTagForDisplay"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getMergedSubscriberIds"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getRadioAccessFamily"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isVideoCallingEnabled"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getDeviceSoftwareVersionForSlot"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getServiceStateForSubscriber"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getVisualVoicemailPackageName"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("enableVisualVoicemailSmsFilter"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("disableVisualVoicemailSmsFilter"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getVisualVoicemailSmsFilterSettings"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("sendVisualVoicemailSmsForSubscriber"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getVoiceActivationState"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getDataActivationState"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getVoiceMailAlphaTagForSubscriber"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("sendDialerSpecialCode"));
      if (BuildCompat.isOreo()) {
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("setVoicemailVibrationEnabled"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("setVoicemailRingtoneUri"));
      }

      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isOffhook"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("isOffhookForSubscriber"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isRinging"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("isRingingForSubscriber"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isIdle"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("isIdleForSubscriber"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isRadioOn"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("isRadioOnForSubscriber"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getClientRequestStats"));
      if (!VirtualCore.get().isSystemApp()) {
         this.addMethodProxy(new ResultStaticMethodProxy("getVisualVoicemailSettings", (Object)null));
         this.addMethodProxy(new ResultStaticMethodProxy("setDataEnabled", 0));
         this.addMethodProxy(new ResultStaticMethodProxy("getDataEnabled", false));
      }

      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getDeviceIdWithFeature") {
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
