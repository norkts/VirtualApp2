package com.lody.virtual.client.hook.proxies.network;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.helper.utils.Reflect;
import java.lang.reflect.Method;
import mirror.android.net.ITetheringConnector;

public class TetheringConnectorStub extends BinderInvocationProxy {
   private static final String SERVER_NAME = "tethering";

   public TetheringConnectorStub() {
      super(ITetheringConnector.Stub.asInterface, "tethering");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new StaticMethodProxy("isTetheringSupported") {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            try {
               Object iIntResultListener = args[2];
               Reflect.on(iIntResultListener).call("onResult", 3);
               return null;
            } catch (Exception var5) {
               return super.call(who, method, args);
            }
         }
      });
   }
}
