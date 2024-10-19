package com.lody.virtual.server.notification;

import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;

class NotificationCompatCompatV14 extends NotificationCompat {
   private final RemoteViewsFixer mRemoteViewsFixer = new RemoteViewsFixer(this);

   private RemoteViewsFixer getRemoteViewsFixer() {
      return this.mRemoteViewsFixer;
   }

   public boolean dealNotification(int id, Notification notification, String packageName) {
      Context appContext = this.getAppContext(packageName);
      if (appContext == null) {
         return false;
      } else if (VClient.get().isDynamicApp() && VirtualCore.get().isOutsideInstalled(packageName)) {
         if (notification.icon != 0) {
            this.getNotificationFixer().fixIconImage(appContext.getResources(), notification.contentView, false, notification);
            if (VERSION.SDK_INT >= 16) {
               this.getNotificationFixer().fixIconImage(appContext.getResources(), notification.bigContentView, false, notification);
            }

            notification.icon = this.getHostContext().getApplicationInfo().icon;
         }

         return true;
      } else {
         this.remakeRemoteViews(id, notification, appContext);
         if (notification.icon != 0) {
            notification.icon = this.getHostContext().getApplicationInfo().icon;
         }

         return true;
      }
   }

   protected void remakeRemoteViews(int id, Notification notification, Context appContext) {
      if (notification.tickerView != null) {
         if (this.isSystemLayout(notification.tickerView)) {
            this.getNotificationFixer().fixRemoteViewActions(appContext, false, notification.tickerView);
         } else {
            notification.tickerView = this.getRemoteViewsFixer().makeRemoteViews(id + ":tickerView", appContext, notification.tickerView, false, false);
         }
      }

      boolean hasIconBitmap;
      if (notification.contentView != null) {
         if (this.isSystemLayout(notification.contentView)) {
            hasIconBitmap = this.getNotificationFixer().fixRemoteViewActions(appContext, false, notification.contentView);
            this.getNotificationFixer().fixIconImage(appContext.getResources(), notification.contentView, hasIconBitmap, notification);
         } else {
            notification.contentView = this.getRemoteViewsFixer().makeRemoteViews(id + ":contentView", appContext, notification.contentView, false, true);
         }
      }

      if (VERSION.SDK_INT >= 16 && notification.bigContentView != null) {
         if (this.isSystemLayout(notification.bigContentView)) {
            this.getNotificationFixer().fixRemoteViewActions(appContext, false, notification.bigContentView);
         } else {
            notification.bigContentView = this.getRemoteViewsFixer().makeRemoteViews(id + ":bigContentView", appContext, notification.bigContentView, true, true);
         }
      }

      if (VERSION.SDK_INT >= 21 && notification.headsUpContentView != null) {
         if (this.isSystemLayout(notification.headsUpContentView)) {
            hasIconBitmap = this.getNotificationFixer().fixRemoteViewActions(appContext, false, notification.headsUpContentView);
            this.getNotificationFixer().fixIconImage(appContext.getResources(), notification.contentView, hasIconBitmap, notification);
         } else {
            notification.headsUpContentView = this.getRemoteViewsFixer().makeRemoteViews(id + ":headsUpContentView", appContext, notification.headsUpContentView, false, false);
         }
      }

   }

   Context getAppContext(String packageName) {
      Context context = null;

      try {
         context = this.getHostContext().createPackageContext(packageName, 3);
      } catch (PackageManager.NameNotFoundException var4) {
         PackageManager.NameNotFoundException e = var4;
         e.printStackTrace();
      }

      return context;
   }
}
