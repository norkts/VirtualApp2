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
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("enqueueToast"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("enqueueToastForLog"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("enqueueToastEx"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("cancelToast"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("finishToken"));
      if (VERSION.SDK_INT >= 24) {
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("removeAutomaticZenRules"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getImportance"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("areNotificationsEnabled"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("setNotificationPolicy"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getNotificationPolicy"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("setNotificationPolicyAccessGranted"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isNotificationPolicyAccessGranted"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isNotificationPolicyAccessGrantedForPackage"));
      }

      if ("samsung".equalsIgnoreCase(Build.BRAND) || "samsung".equalsIgnoreCase(Build.MANUFACTURER)) {
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("removeEdgeNotification"));
      }

      if (BuildCompat.isOreo()) {
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("createNotificationChannelGroups"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getNotificationChannelGroups"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("deleteNotificationChannelGroup"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("createNotificationChannels"));
         if (BuildCompat.isQ()) {
            this.addMethodProxy(new MethodProxy() {
               public String getMethodName() {
                  return "getNotificationChannels";
               }

               public Object call(Object who, Method method, Object... args) throws Throwable {
                  args[0] = VirtualCore.get().getHostPkg();
                  args[1] = VirtualCore.get().getHostPkg();
                  replaceLastUserId(args);
                  return super.call(who, method, args);
               }
            });
         } else {
            this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("getNotificationChannels"));
         }

         if (BuildCompat.isQ()) {
            this.addMethodProxy(new MethodProxy() {
               public String getMethodName() {
                  return "getNotificationChannel";
               }

               public Object call(Object who, Method method, Object... args) throws Throwable {
                  args[0] = VirtualCore.get().getHostPkg();
                  args[2] = VirtualCore.get().getHostPkg();
                  replaceLastUserId(args);
                  return super.call(who, method, args);
               }
            });
            this.addMethodProxy(new ResultStaticMethodProxy("setNotificationDelegate", (Object)null));
            this.addMethodProxy(new ResultStaticMethodProxy("getNotificationDelegate", (Object)null));
            this.addMethodProxy(new ResultStaticMethodProxy("canNotifyAsPackage", false));
         } else {
            this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("getNotificationChannel"));
         }

         this.addMethodProxy(new MethodProxy() {
            public String getMethodName() {
               return "deleteNotificationChannel";
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
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getNotificationChannelGroup"));
      }

      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("setInterruptionFilter"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getPackageImportance"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("shouldGroupPkg"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getBubblePreferenceForPackage"));
      this.addMethodProxy(new StaticMethodProxy("getConversationNotificationChannel") {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            args[0] = VirtualCore.get().getHostPkg();
            args[2] = VirtualCore.get().getHostPkg();
            return super.call(who, method, args);
         }
      });
      this.addMethodProxy(new StaticMethodProxy("getConversationNotificationChannel") {
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
