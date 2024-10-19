package com.lody.virtual.client.hook.proxies.window.session;

import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.helper.compat.BuildCompat;
import java.lang.reflect.Method;
import mirror.android.view.WindowManagerGlobal;

public class WindowSessionPatch extends MethodInvocationProxy<MethodInvocationStub<IInterface>> {
   private static final int ADD_PERMISSION_DENIED;

   public WindowSessionPatch(IInterface session) {
      super(new MethodInvocationStub(session));
   }

   public void onBindMethods() {
      this.addMethodProxy(new BaseMethodProxy("add"));
      this.addMethodProxy(new AddToDisplay("addToDisplayAsUser"));
      this.addMethodProxy(new AddToDisplay("addToDisplay"));
      this.addMethodProxy(new BaseMethodProxy("addToDisplayWithoutInputChannel"));
      this.addMethodProxy(new BaseMethodProxy("addWithoutInputChannel"));
      this.addMethodProxy(new BaseMethodProxy("relayout"));
      if (BuildCompat.isQ()) {
         this.addMethodProxy(new BaseMethodProxy("addToDisplayAsUser"));
         this.addMethodProxy(new BaseMethodProxy("grantInputChannel"));
      }

   }

   public void inject() throws Throwable {
   }

   public boolean isEnvBad() {
      return this.getInvocationStub().getProxyInterface() != null;
   }

   static {
      ADD_PERMISSION_DENIED = WindowManagerGlobal.ADD_PERMISSION_DENIED != null ? WindowManagerGlobal.ADD_PERMISSION_DENIED.get() : -8;
   }

   class AddToDisplay extends BaseMethodProxy {
      public AddToDisplay(String name) {
         super(name);
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return this.isDrawOverlays() && VirtualCore.getConfig().isDisableDrawOverlays(getAppPkg()) ? WindowSessionPatch.ADD_PERMISSION_DENIED : super.call(who, method, args);
      }
   }
}
