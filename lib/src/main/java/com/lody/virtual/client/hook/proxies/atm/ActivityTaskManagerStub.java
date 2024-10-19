package com.lody.virtual.client.hook.proxies.atm;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.IBinder;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.os.VUserHandle;
import java.lang.reflect.Method;
import mirror.android.app.IActivityTaskManager;
import mirror.android.util.Singleton;

@Inject(MethodProxies.class)
@TargetApi(29)
public class ActivityTaskManagerStub extends BinderInvocationProxy {
   public ActivityTaskManagerStub() {
      super(IActivityTaskManager.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQZALBciL2wFSFo=")));

      try {
         Object singleton = Reflect.on(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7IxglDmYjAgZqDiQaKgcYXX0FNC5kDiQdKC4IJ2wzSFo="))).field(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAY+OWwFAj5jAQoZJBciL2wLPDdlNzguLhcMUmMKRSJsHgo9KQgqVg=="))).get();
         Singleton.mInstance.set(singleton, ((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
      } catch (Exception var2) {
         Exception e = var2;
         e.printStackTrace();
      }

   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQYWKAgqLmoVND9rASxF"))) {
         public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
            IBinder token = (IBinder)args[0];
            VActivityManager.get().onActivityDestroy(token);
            return super.afterCall(who, method, args, result);
         }
      });
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQYAKAgqLW8jGiw="))) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            IBinder token = (IBinder)args[0];
            VActivityManager.get().onActivityResumed(token);
            return super.call(who, method, args);
         }
      });
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YCGUaLCBlDiggKQg+MWUwLFo="))) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            IBinder token = (IBinder)args[0];
            Intent intent = (Intent)args[2];
            if (intent != null) {
               args[2] = ComponentUtils.processOutsideIntent(VUserHandle.myUserId(), VirtualCore.get().isExtPackage(), intent);
            }

            VActivityManager.get().onFinishActivity(token);
            return super.call(who, method, args);
         }

         public boolean isEnable() {
            return isAppProcess();
         }
      });
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YCGUaLCBlDiggKQg+MWUwLBFrNyQaLC4YCmcFSFo="))) {
         public Object call(Object who, Method method, Object... args) {
            IBinder token = (IBinder)args[0];
            return VActivityManager.get().finishActivityAffinity(getAppUserId(), token);
         }

         public boolean isEnable() {
            return isAppProcess();
         }
      });
      if (BuildCompat.isSamsung()) {
         this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMBFhESQOKi0qCWIFGgRvNx4qLhhSVg=="))) {
            public Object call(Object who, Method method, Object... args) {
               return 0;
            }
         });
      }

   }
}
