package com.kook.deviceinfo.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.concurrent.LinkedBlockingQueue;

public class AdvertisingIdClient {
   public static String getGoogleAdId(Context context) throws Exception {
      if (Looper.getMainLooper() == Looper.myLooper()) {
         return "Cannot call in the main thread, You must call in the other thread";
      } else {
         PackageManager pm = context.getPackageManager();
         pm.getPackageInfo("com.android.vending", 0);
         AdvertisingConnection connection = new AdvertisingConnection();
         Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
         intent.setPackage("com.google.android.gms");
         if (context.bindService(intent, connection, 1)) {
            String var5;
            try {
               AdvertisingInterface adInterface = new AdvertisingInterface(connection.getBinder());
               var5 = adInterface.getId();
            } finally {
               context.unbindService(connection);
            }

            return var5;
         } else {
            return "";
         }
      }
   }

   private static final class AdvertisingInterface implements IInterface {
      private IBinder binder;

      public AdvertisingInterface(IBinder pBinder) {
         this.binder = pBinder;
      }

      public IBinder asBinder() {
         return this.binder;
      }

      public String getId() throws RemoteException {
         Parcel data = Parcel.obtain();
         Parcel reply = Parcel.obtain();

         String id;
         try {
            data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
            this.binder.transact(1, data, reply, 0);
            reply.readException();
            id = reply.readString();
         } finally {
            reply.recycle();
            data.recycle();
         }

         return id;
      }

      public boolean isLimitAdTrackingEnabled(boolean paramBoolean) throws RemoteException {
         Parcel data = Parcel.obtain();
         Parcel reply = Parcel.obtain();

         boolean limitAdTracking;
         try {
            data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
            data.writeInt(paramBoolean ? 1 : 0);
            this.binder.transact(2, data, reply, 0);
            reply.readException();
            limitAdTracking = 0 != reply.readInt();
         } finally {
            reply.recycle();
            data.recycle();
         }

         return limitAdTracking;
      }
   }

   private static final class AdvertisingConnection implements ServiceConnection {
      boolean retrieved;
      private final LinkedBlockingQueue<IBinder> queue;

      private AdvertisingConnection() {
         this.retrieved = false;
         this.queue = new LinkedBlockingQueue(1);
      }

      public void onServiceConnected(ComponentName name, IBinder service) {
         try {
            this.queue.put(service);
         } catch (InterruptedException var4) {
         }

      }

      public void onServiceDisconnected(ComponentName name) {
      }

      public IBinder getBinder() throws InterruptedException {
         if (this.retrieved) {
            throw new IllegalStateException();
         } else {
            this.retrieved = true;
            return (IBinder)this.queue.take();
         }
      }

      // $FF: synthetic method
      AdvertisingConnection(Object x0) {
         this();
      }
   }
}
