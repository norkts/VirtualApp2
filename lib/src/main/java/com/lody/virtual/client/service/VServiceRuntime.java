package com.lody.virtual.client.service;

import android.app.ActivityManager;
import android.app.IServiceConnection;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.SystemClock;
import com.lody.virtual.client.VClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VServiceRuntime {
   private static final VServiceRuntime sInstance = new VServiceRuntime();
   private final Map<ComponentName, ServiceRecord> mComponentToServiceRecords = new HashMap();
   private RemoteCallbackList<IServiceConnection> mConnectionCallbackList = new RemoteCallbackList<IServiceConnection>() {
      public void onCallbackDied(final IServiceConnection callback) {
         VServiceRuntime.this.mHandler.post(new Runnable() {
            public void run() {
               VServiceRuntime.this.handleConnectionDied(callback);
            }
         });
      }
   };
   private Service mShadowService;
   private Handler mHandler = new Handler(Looper.getMainLooper());

   private VServiceRuntime() {
   }

   public void setShadowService(Service service) {
      this.mShadowService = service;
   }

   public ServiceRecord getServiceRecord(ComponentName component, boolean create) {
      synchronized(this.mComponentToServiceRecords) {
         ServiceRecord record = (ServiceRecord)this.mComponentToServiceRecords.get(component);
         if (record == null && create) {
            record = new ServiceRecord();
            record.component = component;
            record.lastActivityTime = SystemClock.uptimeMillis();
            record.activeSince = SystemClock.elapsedRealtime();
            this.mComponentToServiceRecords.put(component, record);
         }

         return record;
      }
   }

   public static VServiceRuntime getInstance() {
      return sInstance;
   }

   private void handleConnectionDied(IServiceConnection conn) {
      synchronized(this.mComponentToServiceRecords) {
         Iterator var3 = this.mComponentToServiceRecords.values().iterator();

         while(var3.hasNext()) {
            ServiceRecord serviceRecord = (ServiceRecord)var3.next();
            Iterator var5 = serviceRecord.bindings.iterator();

            while(var5.hasNext()) {
               ServiceBindRecord bindRecord = (ServiceBindRecord)var5.next();
               bindRecord.connections.remove(conn.asBinder());
            }
         }

         this.trimService();
      }
   }

   private void trimService() {
      synchronized(this.mComponentToServiceRecords) {
         Iterator var2 = this.mComponentToServiceRecords.values().iterator();

         while(var2.hasNext()) {
            ServiceRecord serviceRecord = (ServiceRecord)var2.next();
            if (serviceRecord.service != null && !serviceRecord.started && serviceRecord.getClientCount() <= 0 && serviceRecord.getConnectionCount() <= 0) {
               serviceRecord.service.onDestroy();
               serviceRecord.service = null;
               this.mComponentToServiceRecords.remove(serviceRecord.component);
            }
         }

      }
   }

   public List<ActivityManager.RunningServiceInfo> getServices() {
      List<ActivityManager.RunningServiceInfo> infos = new ArrayList(this.mComponentToServiceRecords.size());
      synchronized(this.mComponentToServiceRecords) {
         Iterator var3 = this.mComponentToServiceRecords.values().iterator();

         while(var3.hasNext()) {
            ServiceRecord serviceRecord = (ServiceRecord)var3.next();
            ActivityManager.RunningServiceInfo info = new ActivityManager.RunningServiceInfo();
            info.pid = Process.myPid();
            info.uid = VClient.get().getVUid();
            info.activeSince = serviceRecord.activeSince;
            info.lastActivityTime = serviceRecord.lastActivityTime;
            info.clientCount = serviceRecord.getClientCount();
            info.service = serviceRecord.component;
            info.started = serviceRecord.started;
            info.process = VClient.get().getClientConfig().processName;
            infos.add(info);
         }

         return infos;
      }
   }

   public class ServiceRecord extends Binder {
      public ComponentName component;
      public long activeSince;
      public boolean foreground;
      public long lastActivityTime;
      public boolean started;
      public Service service;
      public int startId;
      public final List<ServiceBindRecord> bindings = new ArrayList();

      public int getClientCount() {
         return this.bindings.size();
      }

      int getConnectionCount() {
         int count = 0;

         ServiceBindRecord record;
         for(Iterator var2 = this.bindings.iterator(); var2.hasNext(); count += record.getConnectionCount()) {
            record = (ServiceBindRecord)var2.next();
         }

         return count;
      }

      public void stopServiceIfNecessary(int requestStartId, boolean stopService) {
         if (stopService) {
            if (requestStartId != -1 && requestStartId != this.startId) {
               return;
            }

            this.started = false;
         }

         if (this.service != null && !this.started && this.getConnectionCount() <= 0) {
            this.service.onDestroy();
            this.service = null;
            synchronized(VServiceRuntime.this.mComponentToServiceRecords) {
               VServiceRuntime.this.mComponentToServiceRecords.remove(this.component);
            }

            if (VServiceRuntime.this.mComponentToServiceRecords.isEmpty()) {
               VServiceRuntime.this.mShadowService.stopSelf();
            }
         }

      }

      public IBinder onBind(IServiceConnection conn, Intent intent) {
         this.lastActivityTime = SystemClock.uptimeMillis();
         VServiceRuntime.this.mConnectionCallbackList.register(conn);
         synchronized(VServiceRuntime.this.mComponentToServiceRecords) {
            Iterator var4 = this.bindings.iterator();

            ServiceBindRecord binding;
            do {
               if (!var4.hasNext()) {
                  ServiceBindRecord bindRecord = new ServiceBindRecord();
                  bindRecord.intent = intent;
                  bindRecord.connections.add(conn.asBinder());
                  bindRecord.binder = this.service.onBind(intent);
                  this.bindings.add(bindRecord);
                  return bindRecord.binder;
               }

               binding = (ServiceBindRecord)var4.next();
            } while(!binding.intent.filterEquals(intent));

            if (binding.connections.isEmpty() && binding.rebindStatus == VServiceRuntime.RebindStatus.Rebind) {
               this.service.onRebind(intent);
            }

            binding.connections.add(conn.asBinder());
            return binding.binder;
         }
      }

      public void onUnbind(IServiceConnection conn, Intent intent) {
         synchronized(VServiceRuntime.this.mComponentToServiceRecords) {
            Iterator var4 = this.bindings.iterator();

            while(var4.hasNext()) {
               ServiceBindRecord binding = (ServiceBindRecord)var4.next();
               if (binding.intent.filterEquals(intent)) {
                  if (binding.connections.remove(conn.asBinder())) {
                     if (binding.connections.isEmpty() && binding.rebindStatus != VServiceRuntime.RebindStatus.NotRebind) {
                        binding.rebindStatus = this.service.onUnbind(intent) ? VServiceRuntime.RebindStatus.Rebind : VServiceRuntime.RebindStatus.NotRebind;
                     }

                     this.stopServiceIfNecessary(-1, false);
                  }
                  break;
               }
            }

         }
      }
   }

   public static class ServiceBindRecord {
      public Intent intent;
      public final Set<IBinder> connections = new HashSet();
      public RebindStatus rebindStatus;
      public IBinder binder;

      public int getConnectionCount() {
         return this.connections.size();
      }
   }

   public static enum RebindStatus {
      NotYetBound,
      Rebind,
      NotRebind;
   }
}
