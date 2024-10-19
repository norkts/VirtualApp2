package com.lody.virtual.client.stub;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Build.VERSION;
import com.kook.librelease.R.string;
import com.lody.virtual.helper.compat.NotificationChannelCompat;

public class HiddenForeNotification extends Service {
   private static final int ID = 2781;

   public static void bindForeground(Service service) {
      Notification.Builder builder = NotificationChannelCompat.createBuilder(service.getApplicationContext(), NotificationChannelCompat.DAEMON_ID);
      builder.setSmallIcon(17301544);
      if (VERSION.SDK_INT > 24) {
         builder.setContentTitle(service.getString(string.keep_service_damon_noti_title_v24));
         builder.setContentText(service.getString(string.keep_service_damon_noti_text_v24));
      } else {
         builder.setContentTitle(service.getString(string.keep_service_damon_noti_title));
         builder.setContentText(service.getString(string.keep_service_damon_noti_text));
         builder.setContentIntent(PendingIntent.getService(service, 0, new Intent(service, HiddenForeNotification.class), 0));
      }

      builder.setSound((Uri)null);
      service.startForeground(2781, builder.getNotification());
      if (VERSION.SDK_INT <= 24) {
         service.startService(new Intent(service, HiddenForeNotification.class));
      }

   }

   public int onStartCommand(Intent intent, int flags, int startId) {
      try {
         Notification.Builder builder = NotificationChannelCompat.createBuilder(this.getBaseContext(), NotificationChannelCompat.DAEMON_ID);
         builder.setSmallIcon(17301544);
         builder.setContentTitle(this.getString(string.keep_service_noti_title));
         builder.setContentText(this.getString(string.keep_service_noti_text));
         builder.setSound((Uri)null);
         this.startForeground(2781, builder.getNotification());
         this.stopForeground(true);
         this.stopSelf();
      } catch (Exception var5) {
         Exception e = var5;
         e.printStackTrace();
      }

      return 2;
   }

   public IBinder onBind(Intent intent) {
      return null;
   }
}
