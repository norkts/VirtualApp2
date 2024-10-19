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
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGczNARmNAY5KAhSVg=="))) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            String name = (String)args[0];
            BinderInvocationStub proxy = ServiceLocalManager.getService(name);
            if (proxy != null) {
               VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uKmwjAiliDFE1Ly0iCGcjQSZoASAgKSocIWIFFhBuASg/Ki4YJ343NCV6Vx00Jy5SVg==")), name, proxy);
               return proxy;
            } else {
               VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uKmwjAiliDFE1Ly0iCGcjQSZoASAgKSocIWIFFhBuASg/Ki4YJ343NCV7ARo6DRc6IGwwLFo=")), name);
               return super.call(who, method, args);
            }
         }
      });
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fM2szQV5iASwuKQcqPQ=="))) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            String name = (String)args[0];
            BinderInvocationStub proxy = ServiceLocalManager.getService(name);
            if (proxy != null) {
               VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uKmwjAiliDFE1Ly0iCGcjQSZoASAgKSocJWMaLCZvJSwuLBcEI2gjNy54HjM8Mjk2Og==")), name, proxy);
               return proxy;
            } else {
               VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uKmwjAiliDFE1Ly0iCGcjQSZoASAgKSocJWMaLCZvJSwuLBcEI2gjNy54HjM3Jj0XL24wBgJpN1RF")), name);
               return super.call(who, method, args);
            }
         }
      });
   }

   public boolean isEnvBad() {
      return ServiceManager.sServiceManager.get() != this.getInvocationStub().getProxyInterface();
   }
}
