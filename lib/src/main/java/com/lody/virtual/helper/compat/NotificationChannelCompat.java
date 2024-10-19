package com.lody.virtual.helper.compat;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.Constants;

public class NotificationChannelCompat {
   public static final String DAEMON_ID;
   public static final String DEFAULT_ID;

   public static boolean enable() {
      if (VERSION.SDK_INT > 26) {
         return VirtualCore.get().getTargetSdkVersion() >= 26;
      } else {
         return false;
      }
   }

   @TargetApi(26)
   public static void checkOrCreateChannel(Context context, String channelId, String name) {
      if (VERSION.SDK_INT >= 26) {
         NotificationManager manager = (NotificationManager)context.getSystemService("notification");
         NotificationChannel channel = manager.getNotificationChannel(channelId);
         if (channel == null) {
            channel = new NotificationChannel(channelId, name, 4);
            channel.setDescription("Compatibility of old versions");
            channel.setSound((Uri)null, (AudioAttributes)null);
            channel.setShowBadge(false);

            try {
               manager.createNotificationChannel(channel);
            } catch (Throwable var6) {
               Throwable e = var6;
               e.printStackTrace();
            }
         }
      }

   }

   public static Notification.Builder createBuilder(Context context, String channelId) {
      return VERSION.SDK_INT >= 26 && VirtualCore.get().getTargetSdkVersion() >= 26 ? new Notification.Builder(context, channelId) : new Notification.Builder(context);
   }

   static {
      DAEMON_ID = Constants.NOTIFICATION_DAEMON_CHANNEL;
      DEFAULT_ID = Constants.NOTIFICATION_CHANNEL;
   }
}
