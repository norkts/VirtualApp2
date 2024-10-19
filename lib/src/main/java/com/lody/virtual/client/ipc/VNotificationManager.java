package com.lody.virtual.client.ipc;

import android.app.Notification;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.server.interfaces.INotificationManager;
import com.lody.virtual.server.notification.NotificationCompat;

public class VNotificationManager {
   private static final VNotificationManager sInstance = new VNotificationManager();
   private final NotificationCompat mNotificationCompat = NotificationCompat.create();
   private INotificationManager mService;

   public INotificationManager getService() {
      if (this.mService == null || !IInterfaceUtils.isAlive(this.mService)) {
         Class var1 = VNotificationManager.class;
         synchronized(VNotificationManager.class) {
            Object pmBinder = this.getRemoteInterface();
            this.mService = (INotificationManager)LocalProxyUtils.genProxy(INotificationManager.class, pmBinder);
         }
      }

      return this.mService;
   }

   private Object getRemoteInterface() {
      return INotificationManager.Stub.asInterface(ServiceManagerNative.getService("notification"));
   }

   private VNotificationManager() {
   }

   public static VNotificationManager get() {
      return sInstance;
   }

   public boolean dealNotification(int id, Notification notification, String packageName) {
      if (notification == null) {
         return false;
      } else {
         return VirtualCore.get().getHostPkg().equals(packageName) || this.mNotificationCompat.dealNotification(id, notification, packageName);
      }
   }

   public int dealNotificationId(int id, String packageName, String tag, int userId) {
      try {
         return this.getService().dealNotificationId(id, packageName, tag, userId);
      } catch (RemoteException var6) {
         RemoteException e = var6;
         e.printStackTrace();
         return id;
      }
   }

   public String dealNotificationTag(int id, String packageName, String tag, int userId) {
      try {
         return this.getService().dealNotificationTag(id, packageName, tag, userId);
      } catch (RemoteException var6) {
         RemoteException e = var6;
         e.printStackTrace();
         return tag;
      }
   }

   public boolean areNotificationsEnabledForPackage(String packageName, int userId) {
      try {
         return this.getService().areNotificationsEnabledForPackage(packageName, userId);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         e.printStackTrace();
         return true;
      }
   }

   public void setNotificationsEnabledForPackage(String packageName, boolean enable, int userId) {
      try {
         this.getService().setNotificationsEnabledForPackage(packageName, enable, userId);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         e.printStackTrace();
      }

   }

   public void addNotification(int id, String tag, String packageName, int userId) {
      try {
         this.getService().addNotification(id, tag, packageName, userId);
      } catch (RemoteException var6) {
         RemoteException e = var6;
         e.printStackTrace();
      }

   }

   public void cancelAllNotification(String packageName, int userId) {
      try {
         this.getService().cancelAllNotification(packageName, userId);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         e.printStackTrace();
      }

   }
}
