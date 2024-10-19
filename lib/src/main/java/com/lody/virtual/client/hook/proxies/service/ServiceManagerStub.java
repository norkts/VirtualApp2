package com.lody.virtual.client.hook.proxies.service;

import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.ServiceLocalManager;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.helper.utils.VLog;
import java.lang.reflect.Method;
import mirror.android.os.ServiceManager;

public class ServiceManagerStub extends MethodInvocationProxy<MethodInvocationStub<IInterface>> {
   public ServiceManagerStub() {
      super(new MethodInvocationStub((IInterface)ServiceManager.getIServiceManager.call()));
   }

   public void inject() {
      ServiceManager.sServiceManager.set((IInterface)this.getInvocationStub().getProxyInterface());
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new StaticMethodProxy("getService") {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            String name = (String)args[0];
            BinderInvocationStub proxy = ServiceLocalManager.getService(name);
            if (proxy != null) {
               VLog.d("HV-", "ServiceLocalManager.getService:%s->%s", name, proxy);
               return proxy;
            } else {
               VLog.d("HV-", "ServiceLocalManager.getService:%s no find", name);
               return super.call(who, method, args);
            }
         }
      });
      this.addMethodProxy(new StaticMethodProxy("checkService") {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            String name = (String)args[0];
            BinderInvocationStub proxy = ServiceLocalManager.getService(name);
            if (proxy != null) {
               VLog.d("HV-", "ServiceLocalManager.checkService:%s->%s", name, proxy);
               return proxy;
            } else {
               VLog.d("HV-", "ServiceLocalManager.checkService:%s no find", name);
               return super.call(who, method, args);
            }
         }
      });
   }

   public boolean isEnvBad() {
      return ServiceManager.sServiceManager.get() != this.getInvocationStub().getProxyInterface();
   }
}
