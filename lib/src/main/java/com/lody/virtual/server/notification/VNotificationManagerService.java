package com.lody.virtual.server.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.Singleton;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.server.interfaces.INotificationManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class VNotificationManagerService extends INotificationManager.Stub {
   private static final Singleton<VNotificationManagerService> gService = new Singleton<VNotificationManagerService>() {
      protected VNotificationManagerService create() {
         return new VNotificationManagerService();
      }
   };
   private NotificationManager mNotificationManager;
   static final String TAG = NotificationCompat.class.getSimpleName();
   private final List<String> mDisables = new ArrayList();
   private final HashMap<String, List<NotificationInfo>> mNotifications = new HashMap();
   private Context mContext;

   private void init(Context context) {
      this.mContext = context;
      this.mNotificationManager = (NotificationManager)context.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOC99JCAgKQdfDg==")));
   }

   public static void systemReady(Context context) {
      get().init(context);
   }

   public static VNotificationManagerService get() {
      return (VNotificationManagerService)gService.get();
   }

   public int dealNotificationId(int id, String packageName, String tag, int userId) {
      return id;
   }

   public String dealNotificationTag(int id, String packageName, String tag, int userId) {
      if (TextUtils.equals(this.mContext.getPackageName(), packageName)) {
         return tag;
      } else {
         return tag == null ? packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JhhSVg==")) + userId : packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OD5SVg==")) + tag + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JhhSVg==")) + userId;
      }
   }

   public boolean areNotificationsEnabledForPackage(String packageName, int userId) {
      return !this.mDisables.contains(packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OD5SVg==")) + userId);
   }

   public void setNotificationsEnabledForPackage(String packageName, boolean enable, int userId) {
      String key = packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OD5SVg==")) + userId;
      if (enable) {
         if (this.mDisables.contains(key)) {
            this.mDisables.remove(key);
         }
      } else if (!this.mDisables.contains(key)) {
         this.mDisables.add(key);
      }

   }

   public void addNotification(int id, String tag, String packageName, int userId) {
      NotificationInfo notificationInfo = new NotificationInfo(id, tag, packageName, userId);
      synchronized(this.mNotifications) {
         List<NotificationInfo> list = (List)this.mNotifications.get(packageName);
         if (list == null) {
            list = new ArrayList();
            this.mNotifications.put(packageName, list);
         }

         if (!((List)list).contains(notificationInfo)) {
            ((List)list).add(notificationInfo);
         }

      }
   }

   public void cancelAllNotification(String packageName, int userId) {
      List<NotificationInfo> infos = new ArrayList();
      synchronized(this.mNotifications) {
         List<NotificationInfo> list = (List)this.mNotifications.get(packageName);
         if (list != null) {
            int count = list.size();

            for(int i = count - 1; i >= 0; --i) {
               NotificationInfo info = (NotificationInfo)list.get(i);
               if (info.userId == userId) {
                  infos.add(info);
                  list.remove(i);
               }
            }
         }
      }

      Iterator var4 = infos.iterator();

      while(var4.hasNext()) {
         NotificationInfo info = (NotificationInfo)var4.next();
         VLog.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+CGszNCRLEVRF")) + info.tag + " " + info.id);
         this.mNotificationManager.cancel(info.tag, info.id);
      }

   }

   private static class NotificationInfo {
      int id;
      String tag;
      String packageName;
      int userId;

      NotificationInfo(int id, String tag, String packageName, int userId) {
         this.id = id;
         this.tag = tag;
         this.packageName = packageName;
         this.userId = userId;
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof NotificationInfo)) {
            return super.equals(obj);
         } else {
            NotificationInfo that = (NotificationInfo)obj;
            return that.id == this.id && TextUtils.equals(that.tag, this.tag) && TextUtils.equals(this.packageName, that.packageName) && that.userId == this.userId;
         }
      }
   }
}
