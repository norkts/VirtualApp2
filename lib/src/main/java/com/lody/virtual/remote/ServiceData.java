package com.lody.virtual.remote;

import android.app.IServiceConnection;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.compat.BundleCompat;

public class ServiceData {
   public static class ServiceBindData {
      public ComponentName component;
      public ServiceInfo info;
      public Intent intent;
      public int flags;
      public int userId;
      public IServiceConnection connection;

      public ServiceBindData(ComponentName component, ServiceInfo info, Intent intent, int flags, int userId, IServiceConnection connection) {
         this.component = component;
         this.info = info;
         this.intent = intent;
         this.flags = flags;
         this.userId = userId;
         this.connection = connection;
      }

      public ServiceBindData(Intent proxyIntent) {
         this.info = (ServiceInfo)proxyIntent.getParcelableExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcPmozSFo=")));
         if (this.info != null) {
            this.component = new ComponentName(this.info.packageName, this.info.name);
         }

         this.intent = (Intent)proxyIntent.getParcelableExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLGgVBgY=")));
         this.flags = proxyIntent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4EP2gwLFo=")), 0);
         this.userId = proxyIntent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28mGi9iEVRF")), 0);
         IBinder connBinder = BundleCompat.getBinder(proxyIntent, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGojSFo=")));
         if (connBinder != null) {
            this.connection = IServiceConnection.Stub.asInterface(connBinder);
         }

      }

      public void saveToIntent(Intent proxyIntent) {
         proxyIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcPmozSFo=")), this.info);
         proxyIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLGgVBgY=")), this.intent);
         proxyIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4EP2gwLFo=")), this.flags);
         proxyIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28mGi9iEVRF")), this.userId);
         if (this.connection != null) {
            BundleCompat.putBinder(proxyIntent, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGojSFo=")), this.connection.asBinder());
         }

      }
   }

   public static class ServiceStartData {
      public ComponentName component;
      public ServiceInfo info;
      public Intent intent;
      public int userId;

      public ServiceStartData(ComponentName component, ServiceInfo info, Intent intent, int userId) {
         this.component = component;
         this.info = info;
         this.intent = intent;
         this.userId = userId;
      }

      public ServiceStartData(Intent proxyIntent) {
         String type = proxyIntent.getType();
         if (type != null) {
            this.component = ComponentName.unflattenFromString(type);
         }

         this.info = (ServiceInfo)proxyIntent.getParcelableExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcPmozSFo=")));
         this.intent = (Intent)proxyIntent.getParcelableExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLGgVBgY=")));
         this.userId = proxyIntent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28mGi9iEVRF")), 0);
         if (this.info != null && this.intent != null && this.component != null && this.intent.getComponent() == null) {
            this.intent.setComponent(this.component);
         }

      }

      public void saveToIntent(Intent proxyIntent) {
         proxyIntent.setAction(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMB9hJDAqLD0cP2kjSFo=")));
         proxyIntent.setType(this.component.flattenToString());
         proxyIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcPmozSFo=")), this.info);
         proxyIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLGgVBgY=")), this.intent);
         proxyIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28mGi9iEVRF")), this.userId);
      }
   }

   public static class ServiceStopData {
      public int userId;
      public ComponentName component;
      public int startId;
      public IBinder token;

      public ServiceStopData(int userId, ComponentName component, int startId, IBinder token) {
         this.userId = userId;
         this.component = component;
         this.startId = startId;
         this.token = token;
      }

      public ServiceStopData(Intent proxyIntent) {
         String type = proxyIntent.getType();
         if (type != null) {
            this.component = ComponentName.unflattenFromString(type);
         }

         this.userId = proxyIntent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28mGi9iEVRF")), 0);
         this.startId = proxyIntent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMB9jDgpF")), 0);
         this.token = BundleCompat.getBinder(proxyIntent, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgAMWgVBlo=")));
      }

      public void saveToIntent(Intent proxyIntent) {
         proxyIntent.setAction(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qD28IGgNiASwuKQcqPQ==")));
         proxyIntent.setType(this.component.flattenToString());
         proxyIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28mGi9iEVRF")), this.userId);
         proxyIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMB9jDgpF")), this.startId);
         BundleCompat.putBinder(proxyIntent, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgAMWgVBlo=")), this.token);
      }
   }
}
