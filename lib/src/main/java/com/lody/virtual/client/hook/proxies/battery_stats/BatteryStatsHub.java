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
   private static final String SERVICE_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+LGwFNARnASggLwg2Lw=="));

   public BatteryStatsHub() {
      super(IBatteryStats.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+LGwFNARnASggLwg2Lw==")));
   }

   public void inject() throws Throwable {
      super.inject();
      if (SystemHealthManager.mBatteryStats != null) {
         android.os.health.SystemHealthManager manager = (android.os.health.SystemHealthManager)VirtualCore.get().getContext().getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YKWwFNCNjHjA7Khg2Mg==")));
         SystemHealthManager.mBatteryStats.set(manager, (IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
      }

   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceLastUidMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRg+MWgYNC9iHyg2LwgmL2wzNAY="))) {
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
