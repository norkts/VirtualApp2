package com.lody.virtual.client.hook.proxies.permissionmgr;

import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.Reflect;
import java.lang.reflect.Method;
import mirror.android.app.ActivityThread;

public class PermissionManagerStub extends MethodInvocationProxy<MethodInvocationStub<IInterface>> {
   public PermissionManagerStub() {
      super(new MethodInvocationStub((IInterface)ActivityThread.sPermissionManager.get()));
   }

   public void inject() throws Throwable {
      try {
         VirtualCore.get().getPackageManager().getAllPermissionGroups(0);
      } catch (Throwable var5) {
         Throwable e = var5;
         e.printStackTrace();
      }

      IInterface hooked = (IInterface)this.getInvocationStub().getProxyInterface();
      ActivityThread.sPermissionManager.set(hooked);

      try {
         Object mPermissionManager = Reflect.on((Object)VirtualCore.getPM()).field("mPermissionManager").get();
         Object packagemanager = VirtualCore.getPM();
         if (BuildCompat.isS()) {
            packagemanager = mPermissionManager;
            mPermissionManager = Reflect.on(mPermissionManager).field("mPermissionManager").get();
         }

         if (mPermissionManager != hooked) {
            Reflect.on(packagemanager).set("mPermissionManager", hooked);
         }
      } catch (Throwable var4) {
         Throwable e2 = var4;
         e2.printStackTrace();
      }

      BinderInvocationStub hookBinder = new BinderInvocationStub((IInterface)this.getInvocationStub().getBaseInterface());
      hookBinder.copyMethodProxies(this.getInvocationStub());
      hookBinder.replaceService("permissionmgr");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new StaticMethodProxy("addOnPermissionsChangeListener") {
         public Object call(Object who, Method method, Object... args) {
            return 0;
         }
      });
      this.addMethodProxy(new StaticMethodProxy("removeOnPermissionsChangeListener") {
         public Object call(Object who, Method method, Object... args) {
            return 0;
         }
      });
      this.addMethodProxy(new StaticMethodProxy("addPermission") {
         public Object call(Object who, Method method, Object... args) {
            return true;
         }
      });
      this.addMethodProxy(new StaticMethodProxy("checkPermission") {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            return VPackageManager.get().checkPermission((String)args[0], (String)args[1], Integer.valueOf((Integer)args[2]));
         }
      });
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy("shouldShowRequestPermissionRationale"));
   }

   public boolean isEnvBad() {
      return false;
   }
}
