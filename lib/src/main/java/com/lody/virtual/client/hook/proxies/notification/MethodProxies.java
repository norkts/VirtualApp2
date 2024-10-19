package com.lody.virtual.client.hook.proxies.notification;

import android.app.Notification;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.client.ipc.VNotificationManager;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.ArrayUtils;
import java.lang.reflect.Method;

class MethodProxies {
   static class GetAppActiveNotifications extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMaIAJlDiggKQg+PWcVNAZqASQaLT4+CmMKAillJ1RF"));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         args[0] = getHostPkg();
         replaceLastUserId(args);
         return method.invoke(who, args);
      }
   }

   static class SetNotificationsEnabledForPackage extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGIjGgZjDjwzLy0iLmwjNCZsJSgbLRgMKGIKFgtsJygfKC4YLWgFPD8="));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String pkg = (String)args[0];
         if (getHostPkg().equals(pkg)) {
            return method.invoke(who, args);
         } else {
            int enableIndex = ArrayUtils.indexOfFirst(args, Boolean.class);
            boolean enable = (Boolean)args[enableIndex];
            VNotificationManager.get().setNotificationsEnabledForPackage(pkg, enable, getAppUserId());
            return 0;
         }
      }
   }

   static class AreNotificationsEnabledForPackage extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcMM2IjGgZjDjwzLy0iLmwjNCZsJSgbLRgMKGIKFgtsJygfKC4YLWgFPD8="));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String pkg = (String)args[0];
         return getHostPkg().equals(pkg) ? method.invoke(who, args) : VNotificationManager.get().areNotificationsEnabledForPackage(pkg, getAppUserId());
      }
   }

   static class CancelAllNotifications extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+CGszNCRlDlEoIj1fLmwjHi9oJzg/IxgAKmEjSFo="));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String pkg = MethodParameterUtils.replaceFirstAppPkg(args);
         if (VirtualCore.get().isAppInstalled(pkg)) {
            VNotificationManager.get().cancelAllNotification(pkg, getAppUserId());
            return 0;
         } else {
            replaceLastUserId(args);
            return method.invoke(who, args);
         }
      }
   }

   static class CancelNotificationWithTag extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+CGszNCRoNB4gKQc+MW4FQQZqAQYbIj4YCmMbFiRuJ1RF"));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         int id_index = BuildCompat.isR() ? 3 : 2;
         int tag_index = BuildCompat.isR() ? 2 : 1;
         String pkg = MethodParameterUtils.replaceFirstAppPkg(args);
         replaceLastUserId(args);
         if (getHostPkg().equals(pkg)) {
            return method.invoke(who, args);
         } else {
            String tag = (String)args[tag_index];
            int id = (Integer)args[id_index];
            id = VNotificationManager.get().dealNotificationId(id, pkg, tag, getAppUserId());
            tag = VNotificationManager.get().dealNotificationTag(id, pkg, tag, getAppUserId());
            args[tag_index] = tag;
            args[id_index] = id;
            return method.invoke(who, args);
         }
      }
   }

   static class EnqueueNotificationWithTagPriority extends EnqueueNotificationWithTag {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcL2wVNAViDFk1LBccPGwjAjdvER4cLCsmI2YaBlRpDjwfLBg2KWwzAiBpEVRF"));
      }
   }

   static class EnqueueNotificationWithTag extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcL2wVNAViDFk1LBccPGwjAjdvER4cLCsmI2YaBlRpDjxF"));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String pkg = (String)args[0];
         replaceLastUserId(args);
         if (getHostPkg().equals(pkg)) {
            return method.invoke(who, args);
         } else {
            int notificationIndex = ArrayUtils.indexOfFirst(args, Notification.class);
            int idIndex = ArrayUtils.indexOfFirst(args, Integer.class);
            int tagIndex = VERSION.SDK_INT >= 18 ? 2 : 1;
            int id = (Integer)args[idIndex];
            String tag = (String)args[tagIndex];
            id = VNotificationManager.get().dealNotificationId(id, pkg, tag, getAppUserId());
            tag = VNotificationManager.get().dealNotificationTag(id, pkg, tag, getAppUserId());
            args[idIndex] = id;
            args[tagIndex] = tag;
            Notification notification = (Notification)args[notificationIndex];
            if (!VNotificationManager.get().dealNotification(id, notification, pkg)) {
               return 0;
            } else {
               VNotificationManager.get().addNotification(id, tag, pkg, getAppUserId());
               args[0] = getHostPkg();
               if (VERSION.SDK_INT >= 18 && args[1] instanceof String) {
                  args[1] = getHostPkg();
               }

               return method.invoke(who, args);
            }
         }
      }
   }

   static class EnqueueNotification extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcL2wVNAViDFk1LBccPGwjAjdvER4cLC5SVg=="));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String pkg = (String)args[0];
         replaceLastUserId(args);
         if (getHostPkg().equals(pkg)) {
            return method.invoke(who, args);
         } else {
            int notificationIndex = ArrayUtils.indexOfFirst(args, Notification.class);
            int idIndex = ArrayUtils.indexOfFirst(args, Integer.class);
            int id = (Integer)args[idIndex];
            id = VNotificationManager.get().dealNotificationId(id, pkg, (String)null, getAppUserId());
            args[idIndex] = id;
            Notification notification = (Notification)args[notificationIndex];
            if (!VNotificationManager.get().dealNotification(id, notification, pkg)) {
               return 0;
            } else {
               VNotificationManager.get().addNotification(id, (String)null, pkg, getAppUserId());
               args[0] = getHostPkg();
               return method.invoke(who, args);
            }
         }
      }
   }
}
