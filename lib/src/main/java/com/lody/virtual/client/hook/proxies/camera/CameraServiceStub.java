package com.lody.virtual.client.hook.proxies.camera;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.Method;
import mirror.android.camera.ICameraService;

public class CameraServiceStub extends BinderInvocationProxy {
   private static final String SERVER_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwguPGUVJyZ9JCA3KAguOQ=="));

   public CameraServiceStub() {
      super(ICameraService.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwguPGUVJyZ9JCA3KAguOQ==")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGojNClmEVRF"))));
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGojNClmHAo/LD0cP2kjSFo="))) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            if (args[2] instanceof String) {
               args[2] = VirtualCore.get().getHostPkg();
            }

            return super.call(who, method, args);
         }
      });
      this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGojNClmHFE/KC0iP2gjSFo="))));
   }
}
