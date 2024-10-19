package com.lody.virtual.client.hook.proxies.battery_stats;

import android.annotation.TargetApi;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceLastUidMethodProxy;
import java.lang.reflect.Method;
import mirror.android.os.health.SystemHealthManager;
import mirror.com.android.internal.app.IBatteryStats;

@TargetApi(24)
public class BatteryStatsHub extends BinderInvocationProxy {
   private static final String SERVICE_NAME = "batterystats";

   public BatteryStatsHub() {
      super(IBatteryStats.Stub.asInterface, "batterystats");
   }

   public void inject() throws Throwable {
      super.inject();
      if (SystemHealthManager.mBatteryStats != null) {
         android.os.health.SystemHealthManager manager = (android.os.health.SystemHealthManager)VirtualCore.get().getContext().getSystemService("systemhealth");
         SystemHealthManager.mBatteryStats.set(manager, (IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
      }

   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceLastUidMethodProxy("takeUidSnapshot") {
         public Object call(Object who, Method method, Object... args) {
            try {
               return super.call(who, method, args);
            } catch (Throwable var5) {
               return null;
            }
         }
      });
   }
}
