package com.lody.virtual.client.hook.proxies.network;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceUidMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.Method;
import mirror.android.os.INetworkManagementService;

@TargetApi(23)
public class NetworkManagementStub extends BinderInvocationProxy {
   public NetworkManagementStub() {
      super(INetworkManagementService.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4uLGwzGgRjJR43LwcYOWkFGiNrARo/")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceUidMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQVAixlJFE/LwguLmkgFgZ9Nyg/Kj4ACGMhOCpsHhosLi5SVg==")), 0));
      this.addMethodProxy(new ReplaceUidMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQVAixoDjAgKAguPWkxMCtvHiAcKS5bGGAaPCZvJF0iLAccVg==")), 0));
      this.addMethodProxy(new ReplaceUidMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQVAixoDjAgKAguPWkxMCtvHiAcKS5bXmMaGj9uDl0iLAccVg==")), 0));
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjNAZmJB4qKSwqLm4gBgNnAR4vJgguCn0KGjc="))) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            int uid = (Integer)args[0];
            if (uid == getVUid()) {
               args[0] = getRealUid();
            }

            return super.call(who, method, args);
         }
      });
   }
}
