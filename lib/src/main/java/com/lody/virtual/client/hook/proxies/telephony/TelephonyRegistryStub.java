package com.lody.virtual.client.hook.proxies.telephony;

import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceSequencePkgMethodProxy;
import java.lang.reflect.Method;
import mirror.com.android.internal.telephony.ITelephonyRegistry;

public class TelephonyRegistryStub extends BinderInvocationProxy {
   public TelephonyRegistryStub() {
      super(ITelephonyRegistry.Stub.asInterface, "telephony.registry");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("addOnSubscriptionsChangedListener"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("addOnOpportunisticSubscriptionsChangedListener"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("removeOnSubscriptionsChangedListener"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("listen"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("listenWithEventList"));
      this.addMethodProxy(new ReplaceSequencePkgMethodProxy("listenForSubscriber", 1) {
         public boolean beforeCall(Object who, Method method, Object... args) {
            if (VERSION.SDK_INT >= 17 && isFakeLocationEnable()) {
               for(int i = args.length - 1; i > 0; --i) {
                  if (args[i] instanceof Integer) {
                     int events = (Integer)args[i];
                     events ^= 1024;
                     events ^= 16;
                     args[i] = events;
                     break;
                  }
               }
            }

            return super.beforeCall(who, method, args);
         }
      });
   }
}
