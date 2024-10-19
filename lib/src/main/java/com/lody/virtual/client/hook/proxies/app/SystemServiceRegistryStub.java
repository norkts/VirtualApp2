package com.lody.virtual.client.hook.proxies.app;

import android.content.Context;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import mirror.android.app.SystemServiceRegistry;

public class SystemServiceRegistryStub extends MethodInvocationProxy<MethodInvocationStub<Object>> {
   public SystemServiceRegistryStub(Context context, String name) {
      super(new MethodInvocationStub(SystemServiceRegistry.getSystemService.call(context, name)));
   }

   public void inject() throws Throwable {
   }

   protected void onBindMethods() {
      super.onBindMethods();
   }

   public boolean isEnvBad() {
      return false;
   }
}
