package com.lody.virtual.client.stub;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.service.VServiceRuntime;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.ClientConfig;
import com.lody.virtual.remote.ServiceData;
import com.lody.virtual.server.IBinderProxyService;
import com.lody.virtual.server.secondary.FakeIdentityBinder;
import java.util.HashMap;
import java.util.Map;

public class ShadowServiceImpl extends Service {
   private static final String TAG = ShadowServiceImpl.class.getSimpleName();
   private final VServiceRuntime mRuntime = VServiceRuntime.getInstance();
   private static final Map<String, IBindServiceProxy> sBinderServiceProxies = new HashMap();

   public void onCreate() {
      this.mRuntime.setShadowService(this);
   }

   public int onStartCommand(Intent intent, int flags, int startId) {
      if (intent == null) {
         return 2;
      } else {
         String event = intent.getAction();
         if (event == null) {
            return 2;
         } else if (event.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMB9hJDAqLD0cP2kjSFo=")))) {
            ServiceData.ServiceStartData data = new ServiceData.ServiceStartData(intent);
            if (data.intent == null) {
               VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLmsVHi9iVyQpLBciKGU3TQNrDgo9Ixg2J0saGilqHgodIzk5JA==")) + intent);
               return 2;
            } else {
               ClientConfig config = VClient.get().getClientConfig();
               if (config == null) {
                  VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKWwFJARmVyQpKAguLGwjAit4Hjw5LD42J2EjNz15EVRF")) + data.info.processName);
                  return 2;
               } else if (!data.info.processName.equals(config.processName)) {
                  return 2;
               } else if (data.userId != VUserHandle.myUserId()) {
                  return 2;
               } else {
                  VServiceRuntime.ServiceRecord record = this.mRuntime.getServiceRecord(data.component, true);
                  if (record.service == null) {
                     record.service = VClient.get().createService(data.info, record);
                  }

                  record.lastActivityTime = SystemClock.uptimeMillis();
                  record.started = true;
                  ++record.startId;
                  data.intent.setExtrasClassLoader(record.service.getClassLoader());
                  ComponentUtils.unpackFillIn(data.intent, record.service.getClassLoader());
                  int result = record.service.onStartCommand(data.intent, flags, record.startId);
                  if (result == 1) {
                     result = 3;
                  }

                  return result;
               }
            }
         } else if (event.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qD28IGgNiASwuKQcqPQ==")))) {
            ServiceData.ServiceStopData data = new ServiceData.ServiceStopData(intent);
            VServiceRuntime.ServiceRecord record = null;
            if (data.token instanceof VServiceRuntime.ServiceRecord) {
               record = (VServiceRuntime.ServiceRecord)data.token;
            }

            if (record == null) {
               record = this.mRuntime.getServiceRecord(data.component, false);
            }

            if (record == null) {
               return 2;
            } else {
               record.stopServiceIfNecessary(data.startId, true);
               return 2;
            }
         } else {
            throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcMWojGj1gMCQ7Ly42MW8FMzJ4EVRF")) + event);
         }
      }
   }

   public IBinder onBind(Intent intent) {
      ServiceData.ServiceBindData data = new ServiceData.ServiceBindData(intent);
      ClientConfig config = VClient.get().getClientConfig();
      if (config == null) {
         VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKWwFJARmVyQpKAguLGwjAit4Hjw5LD42J2EjNz15EVRF")) + data.info.processName);
         return null;
      } else if (!data.info.processName.equals(config.processName)) {
         return null;
      } else if (data.intent == null) {
         return null;
      } else if (data.userId != VUserHandle.myUserId()) {
         return null;
      } else if (data.connection == null) {
         return null;
      } else {
         VServiceRuntime.ServiceRecord record = this.mRuntime.getServiceRecord(data.component, true);
         if (record.service == null) {
            if ((data.flags & 1) == 0) {
               return null;
            }

            record.service = VClient.get().createService(data.info, record);
         }

         data.intent.setExtrasClassLoader(record.service.getClassLoader());
         IBinder binder = record.onBind(data.connection, data.intent);
         if (binder instanceof Binder) {
            try {
               String descriptor = ((IBinder)binder).getInterfaceDescriptor();
               IBindServiceProxy proxy = (IBindServiceProxy)sBinderServiceProxies.get(descriptor);
               if (proxy != null) {
                  binder = proxy.createProxy((Binder)binder);
                  VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uKmwjAiliDywvKj42MW8jGlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhcMD2kKDSh9NAY2KBcMKH4zSFo=")) + descriptor + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgiD28nIANiASwuKQcqPXhSTVo=")) + data.component);
               }
            } catch (RemoteException var8) {
               RemoteException e = var8;
               e.printStackTrace();
            }
         }

         return new BinderProxy(data.component, (IBinder)binder);
      }
   }

   public boolean onUnbind(Intent intent) {
      ServiceData.ServiceBindData data = new ServiceData.ServiceBindData(intent);
      if (data.intent == null) {
         return false;
      } else if (data.userId != VUserHandle.myUserId()) {
         return false;
      } else if (data.connection == null) {
         return false;
      } else {
         VServiceRuntime.ServiceRecord record = this.mRuntime.getServiceRecord(data.component, false);
         if (record != null && record.service != null) {
            data.intent.setExtrasClassLoader(record.service.getClassLoader());
            record.onUnbind(data.connection, data.intent);
            return false;
         } else {
            return false;
         }
      }
   }

   public void onDestroy() {
      this.mRuntime.setShadowService((Service)null);
   }

   static {
      sBinderServiceProxies.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7Ly0qDWUjMAZsIxpPJRg2JWAjLClqHCQ+Iz4MJ2UwMAVrNzgZJi4qVg==")), new IBindServiceProxy() {
         public Binder createProxy(Binder binder) {
            return new FakeIdentityBinder(binder, 1000, Process.myPid());
         }
      });
   }

   static class BinderProxy extends IBinderProxyService.Stub {
      private ComponentName component;
      private IBinder binder;

      public BinderProxy(ComponentName component, IBinder binder) {
         this.component = component;
         this.binder = binder;
      }

      public ComponentName getComponent() {
         return this.component;
      }

      public IBinder getService() {
         return this.binder;
      }
   }

   interface IBindServiceProxy {
      Binder createProxy(Binder var1);
   }
}
