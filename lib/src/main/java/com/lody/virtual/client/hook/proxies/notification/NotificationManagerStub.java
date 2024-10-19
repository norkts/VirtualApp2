package com.lody.virtual.client.hook.proxies.notification;

import android.os.Build;
import android.os.IInterface;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.Constants;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgAndLastUserIdMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.helper.compat.BuildCompat;
import java.lang.reflect.Method;
import mirror.android.app.NotificationManager;
import mirror.android.widget.Toast;

@Inject(MethodProxies.class)
public class NotificationManagerStub extends MethodInvocationProxy<MethodInvocationStub<IInterface>> {
   public NotificationManagerStub() {
      super(new MethodInvocationStub((IInterface)NotificationManager.getService.call()));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcL2wVNAViDwo1LwgqLg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcL2wVNAViDwo1LwgqLmEVNAR9EQYu"))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcL2wVNAViDwo1LwgqLmEgFlo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+CGszNCRuHh47Iy42Vg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YCGUaLCBuHh4xKAcYVg=="))));
      if (VERSION.SDK_INT >= 24) {
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uDWowOCtlATAgKi1XOWUzLClmNygbIS0uKGIFNFo="))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLH0VEgJgJywgLwcYP2kjSFo="))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcMM2IjGgZjDjwzLy0iLmwjNCZsJSgbLRgMKGIKFlo="))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGIjGgZjDjwzLy0iLmwjNCZkEQYdIxg2Mw=="))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjGgZjDjwzLy0iLmwjNCZkEQYdIxg2Mw=="))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGIjGgZjDjwzLy0iLmwjNCZkEQYdIxg2M2UKNCZuASw8JwdfO2UwMD9oAVRF"))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2U2owMC9iNAY5Lwg2MW8FMExlJwIaLT0YHX0gNCBlJyxLLBhbKm8VNDA="))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2U2owMC9iNAY5Lwg2MW8FMExlJwIaLT0YHX0gNCBlJyxLLBhbKm8VNDBgJwYbLxcmKm8gOD9pJ1RF"))));
      }

      if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+DW8wNCZiJ1RF")).equalsIgnoreCase(Build.BRAND) || StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+DW8wNCZiJ1RF")).equalsIgnoreCase(Build.MANUFACTURER)) {
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uDWowOCtqDgo9KAUYDWUzLC5qATAsKggYKWAzSFo="))));
      }

      if (BuildCompat.isOreo()) {
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0MM2saMCtoNB4gKQc+MW4FQQZqAQYbJT5fO2AwRSBsHDw7KQcADmwjSFo="))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjGgZjDjwzLy0iLmwjNCZgJ1ksLC4cJ2AYJDVsJwo5LAhSVg=="))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguDmgaMCtoNB4gKQc+MW4FQQZqAQYbJT5fO2AwRSBsHDw7KQcADg=="))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0MM2saMCtoNB4gKQc+MW4FQQZqAQYbJT5fO2AwRSBsESxF"))));
         if (BuildCompat.isQ()) {
            this.addMethodProxy(new MethodProxy() {
               public String getMethodName() {
                  return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjGgZjDjwzLy0iLmwjNCZgJ1ksLC4cJ2AVNFo="));
               }

               public Object call(Object who, Method method, Object... args) throws Throwable {
                  args[0] = VirtualCore.get().getHostPkg();
                  args[1] = VirtualCore.get().getHostPkg();
                  replaceLastUserId(args);
                  return super.call(who, method, args);
               }
            });
         } else {
            this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjGgZjDjwzLy0iLmwjNCZgJ1ksLC4cJ2AVNFo="))));
         }

         if (BuildCompat.isQ()) {
            this.addMethodProxy(new MethodProxy() {
               public String getMethodName() {
                  return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjGgZjDjwzLy0iLmwjNCZgJ1ksLC4cJ2AVSFo="));
               }

               public Object call(Object who, Method method, Object... args) throws Throwable {
                  args[0] = VirtualCore.get().getHostPkg();
                  args[2] = VirtualCore.get().getHostPkg();
                  replaceLastUserId(args);
                  return super.call(who, method, args);
               }
            });
            this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGIjGgZjDjwzLy0iLmwjNCZjESgdLhgmO2YaLFo=")), (Object)null));
            this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjGgZjDjwzLy0iLmwjNCZjESgdLhgmO2YaLFo=")), (Object)null));
            this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+CGIjGgZjDjwZJwgqDG4jAiFoASAg")), false));
         } else {
            this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjGgZjDjwzLy0iLmwjNCZgJ1ksLC4cJ2AVSFo="))));
         }

         this.addMethodProxy(new MethodProxy() {
            public String getMethodName() {
               return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguDmgaMCtoNB4gKQc+MW4FQQZqAQYbJT5fO2AwRSBsEVRF"));
            }

            public Object call(Object who, Method method, Object... args) throws Throwable {
               MethodParameterUtils.replaceFirstAppPkg(args);
               if (args != null && args.length >= 2 && args[1] instanceof String && Constants.NOTIFICATION_DAEMON_CHANNEL.equals(args[1])) {
                  return null;
               } else {
                  try {
                     return super.call(who, method, args);
                  } catch (Exception var5) {
                     Exception e = var5;
                     e.printStackTrace();
                     return null;
                  }
               }
            }
         });
      }

      if (BuildCompat.isPie()) {
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjGgZjDjwzLy0iLmwjNCZgJ1ksLC4cJ2AYJDVsJwo5"))));
      }

      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLH0VBgZiASwqLAgmLmwjNCZjNx4dKgguCA=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcFJCljJCA9KAUcD2ozNARvETgbLT4uVg=="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki5fD2wVHixqJyw1LAgmDGwFElo="))));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMgNCp9NFE/OxguPWkVGgRrARoqLhYiKWExOCRpJAIqLwgAVg=="))));
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzGiZmNDAqIy0iLmwjNCZ9NwY/IxgiI30gPD9vDlkdIAgMO2UzBj9qAVRF"))) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            args[0] = VirtualCore.get().getHostPkg();
            args[2] = VirtualCore.get().getHostPkg();
            return super.call(who, method, args);
         }
      });
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzGiZmNDAqIy0iLmwjNCZ9NwY/IxgiI30gPD9vDlkdIAgMO2UzBj9qAVRF"))) {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            args[0] = VirtualCore.get().getHostPkg();
            args[2] = VirtualCore.get().getHostPkg();
            return super.call(who, method, args);
         }
      });
   }

   public void inject() throws Throwable {
      NotificationManager.sService.set((IInterface)this.getInvocationStub().getProxyInterface());
      Toast.sService.set((IInterface)this.getInvocationStub().getProxyInterface());
   }

   public boolean isEnvBad() {
      return NotificationManager.getService.call() != this.getInvocationStub().getProxyInterface();
   }
}
