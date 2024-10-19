package com.lody.virtual.client.hook.proxies.am;

import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgAndLastUserIdMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.compat.BuildCompat;
import java.lang.reflect.Method;
import java.util.Map;
import mirror.android.app.ActivityManagerNative;
import mirror.android.app.ActivityManagerOreo;
import mirror.android.app.IActivityManager;
import mirror.android.os.ServiceManager;
import mirror.android.util.Singleton;

@Inject(MethodProxies.class)
public class ActivityManagerStub extends MethodInvocationProxy<MethodInvocationStub<IInterface>> {
   public ActivityManagerStub() {
      super(new MethodInvocationStub((IInterface)ActivityManagerNative.getDefault.call()));
   }

   public void inject() {
      Object gDefault;
      if (BuildCompat.isOreo()) {
         gDefault = ActivityManagerOreo.IActivityManagerSingleton.get();
         Singleton.mInstance.set(gDefault, this.getInvocationStub().getProxyInterface());
      } else if (ActivityManagerNative.gDefault.type() == IActivityManager.TYPE) {
         ActivityManagerNative.gDefault.set(this.getInvocationStub().getProxyInterface());
      } else if (ActivityManagerNative.gDefault.type() == Singleton.TYPE) {
         gDefault = ActivityManagerNative.gDefault.get();
         Singleton.mInstance.set(gDefault, this.getInvocationStub().getProxyInterface());
      }

      BinderInvocationStub hookAMBinder = new BinderInvocationStub((IInterface)this.getInvocationStub().getBaseInterface());
      hookAMBinder.copyMethodProxies(this.getInvocationStub());
      ((Map)ServiceManager.sCache.get()).put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQZF")), hookAMBinder);
   }

   protected void onBindMethods() {
      super.onBindMethods();
      if (VirtualCore.get().isVAppProcess()) {
         this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGcjNAFmDjApLBcMPmcKRS9rARo/LRcqI2AgRVo="))) {
            public Object call(Object who, Method method, Object... args) throws Throwable {
               try {
                  return super.call(who, method, args);
               } catch (Throwable var5) {
                  Throwable e = var5;
                  e.printStackTrace();
                  return 0;
               }
            }
         });
         this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLH0FAgNmHh4qKQcqOW82TQRlJzAgKT02GWcaGj99NAoqLAguKmwjSFo="))));
         this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uPWUaLAZiASxKKQc2RG4aAitsNCQgKS5SVg==")), 0));
         this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcKmgVPC9hJwo/IzwMMWkxNCpsJyg5Ki4uCA==")), 0));
         this.addMethodProxy(new ReplaceLastPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMaIAJpJwo7Iz42Um8FBis="))));
         this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4YCGgKNAJiHiAgKAUqDW8VHi9rJCg5LRcqI2AgRVo=")), 0));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMaIAJoHh45KS0MPn0VGgRqASQ0IxgcIQ=="))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKGowFgZrNzA2KSs+KG8FPBFsHjxF"))));
         this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2W2owFiliDgoJKgdXPWoaAi9vNyhIKhgEKGkgNDVuDgod"))));
         this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQYAKAgqLW8jGiw="))) {
            public Object call(Object who, Method method, Object... args) throws Throwable {
               IBinder token = (IBinder)args[0];
               VActivityManager.get().onActivityResumed(token);
               return super.call(who, method, args);
            }
         });
         this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQYWKAgqLmoVND9rASxF"))) {
            public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
               IBinder token = (IBinder)args[0];
               VActivityManager.get().onActivityDestroy(token);
               return super.afterCall(who, method, args, result);
            }
         });
         this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fM2szQVBhNAYCKAguD2wgAgNqAQYb"))) {
            public Object call(Object who, Method method, Object... args) throws Throwable {
               return args[0] instanceof Uri && args[0].toString().equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmVgU1Oi42PW8zGgJqEQYbL18AJX0FMDVvDgo7LAQuDmwzNDJoHgooJxdfVg=="))) ? VirtualCore.get().checkSelfPermission(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Owg+CGUVOCthJw02IxcMKG8jLANsJx4cLCocXmk2GlRmD1kAJDwqE2QhNApkDx4fLCwuVg==")), VirtualCore.get().isExtPackage()) ? 0 : -1 : 0;
            }
         });
         this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YCGUaLCBlDiggKQg+MWUwLFo="))) {
            public Object call(Object who, Method method, Object... args) throws Throwable {
               IBinder token = (IBinder)args[0];
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
      }

      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLH0VBgZiDlkgOy0MDmkzGgRnJx4/IwYiJ30FFjBlNApF"))));
   }

   public boolean isEnvBad() {
      return ActivityManagerNative.getDefault.call() != this.getInvocationStub().getProxyInterface();
   }

   static final class BroadcastIntentWithFeature extends MethodProxies.BroadcastIntent {
      public final String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj0MD2sVMCl9ASggIQcYLmkjMAZnJx4/IwYiJ30FFjBlNApF"));
      }

      public final Object call(Object who, Method method, Object[] args) throws Throwable {
         Intent v1 = (Intent)args[2];
         v1.setDataAndType(v1.getData(), (String)args[3]);
         Intent v1_1 = this.handleIntent(v1);
         if (v1_1 == null) {
            return 0;
         } else {
            args[2] = v1_1;
            if (args[8] instanceof String || args[8] instanceof String[]) {
               args[8] = null;
            }

            return method.invoke(who, args);
         }
      }

      public final boolean isEnable() {
         return isAppProcess();
      }
   }
}
