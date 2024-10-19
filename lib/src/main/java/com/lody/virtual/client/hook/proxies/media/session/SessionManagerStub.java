package com.lody.virtual.client.hook.proxies.media.session;

import android.annotation.TargetApi;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import mirror.android.media.session.ISessionManager;

@TargetApi(21)
public class SessionManagerStub extends BinderInvocationProxy {
   public SessionManagerStub() {
      super(ISessionManager.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwguPGUVJB9hJDApIy0cDW8VSFo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0MM2saMCtpJDApIy0cDW8VSFo="))) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            replaceLastUserId(args);
            final IInterface ISession = (IInterface)super.call(who, method, args);
            return SessionManagerStub.CreateProxy(ISession, new InvocationHandler() {
               public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                  if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzGiZmESw1KhdbPWoVSFo=")).equals(method.getName())) {
                     final IInterface controller = (IInterface)method.invoke(ISession, args);
                     return SessionManagerStub.CreateProxy(controller, new InvocationHandler() {
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                           if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQjGiRmDl0/JBdfVg==")).equals(method.getName())) {
                              MethodParameterUtils.replaceFirstAppPkg(args);
                              return method.invoke(controller, args);
                           } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqMmwaLAZuNB4oLAdXPQ==")).equals(method.getName())) {
                              MethodParameterUtils.replaceFirstAppPkg(args);
                              return method.invoke(controller, args);
                           } else {
                              if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0MM2saMCtpJDApIy0cDW8VSFo=")).equals(method.getName()) || StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGczNANhJAY1Kj4qVg==")).equals(method.getName()) || StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGczNANhJAY1KjouAG8FJCtlNDBF")).equals(method.getName()) || StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPGczNANhJAY1Kj4qU2wgAgZrARogKS5SVg==")).equals(method.getName()) || StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPGczNANhJAY1KjouAG8FJCtlNDBTIxc2CmIKRSBlN1RF")).equals(method.getName())) {
                                 MethodProxy.replaceLastUserId(args);
                              }

                              return method.invoke(controller, args);
                           }
                        }
                     });
                  } else {
                     return method.invoke(ISession, args);
                  }
               }
            });
         }
      });
   }

   private static Object CreateProxy(IInterface iInterface, InvocationHandler proxy) {
      return Proxy.newProxyInstance(iInterface.getClass().getClassLoader(), iInterface.getClass().getInterfaces(), proxy);
   }
}
