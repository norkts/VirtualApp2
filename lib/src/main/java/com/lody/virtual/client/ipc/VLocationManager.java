package com.lody.virtual.client.ipc;

import android.app.PendingIntent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Build.VERSION;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.proxies.location.MockLocationHelper;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.vloc.VLocation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VLocationManager {
   private Handler mWorkHandler;
   private HandlerThread mHandlerThread;
   private final List<Object> mGpsListeners = new ArrayList();
   private static VLocationManager sVLocationManager = new VLocationManager();
   private Runnable mUpdateGpsStatusTask = new Runnable() {
      public void run() {
         synchronized(VLocationManager.this.mGpsListeners) {
            Iterator var2 = VLocationManager.this.mGpsListeners.iterator();

            while(true) {
               if (!var2.hasNext()) {
                  break;
               }

               Object listener = var2.next();
               VLocationManager.this.notifyGpsStatus(listener);
            }
         }

         VLocationManager.this.mWorkHandler.postDelayed(VLocationManager.this.mUpdateGpsStatusTask, 8000L);
      }
   };
   private final Map<Object, UpdateLocationTask> mLocationTaskMap = new HashMap();

   private VLocationManager() {
      LocationManager locationManager = (LocationManager)VirtualCore.get().getContext().getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgAOWsaMC9gJFlF")));
      MockLocationHelper.fakeGpsStatus(locationManager);
   }

   public static VLocationManager get() {
      return sVLocationManager;
   }

   private void checkWork() {
      if (this.mHandlerThread == null) {
         synchronized(this) {
            if (this.mHandlerThread == null) {
               this.mHandlerThread = new HandlerThread(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgAOWYwMCBhNDA7KBhSVg==")));
               this.mHandlerThread.start();
            }
         }
      }

      if (this.mWorkHandler == null) {
         synchronized(this) {
            if (this.mWorkHandler == null) {
               this.mWorkHandler = new Handler(this.mHandlerThread.getLooper());
            }
         }
      }

   }

   private void stopGpsTask() {
      if (this.mWorkHandler != null) {
         this.mWorkHandler.removeCallbacks(this.mUpdateGpsStatusTask);
      }

   }

   private void startGpsTask() {
      this.checkWork();
      this.stopGpsTask();
      this.mWorkHandler.postDelayed(this.mUpdateGpsStatusTask, 5000L);
   }

   public boolean hasVirtualLocation(String packageName, int userId) {
      try {
         return VirtualLocationManager.get().getMode(userId, packageName) != 0;
      } catch (Exception var4) {
         Exception e = var4;
         e.printStackTrace();
         return false;
      }
   }

   public boolean isProviderEnabled(String provider) {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS06KQ==")).equals(provider);
   }

   public VLocation getLocation(String packageName, int userId) {
      return this.getVirtualLocation(packageName, (Location)null, userId);
   }

   public VLocation getCurAppLocation() {
      return this.getVirtualLocation(VClient.get().getCurrentPackage(), (Location)null, VUserHandle.myUserId());
   }

   public VLocation getVirtualLocation(String packageName, Location loc, int userId) {
      try {
         return VirtualLocationManager.get().getMode(userId, packageName) == 1 ? VirtualLocationManager.get().getGlobalLocation() : VirtualLocationManager.get().getLocation(userId, packageName);
      } catch (Exception var5) {
         Exception e = var5;
         e.printStackTrace();
         return null;
      }
   }

   public String getPackageName() {
      return VClient.get().getCurrentPackage();
   }

   public void removeGpsStatusListener(Object[] args) {
      if (!(args[0] instanceof PendingIntent)) {
         boolean needStop;
         synchronized(this.mGpsListeners) {
            this.mGpsListeners.remove(args[0]);
            needStop = this.mGpsListeners.size() == 0;
         }

         if (needStop) {
            this.stopGpsTask();
         }

      }
   }

   public void addGpsStatusListener(Object[] args) {
      Object GpsStatusListenerTransport = args[0];
      MockLocationHelper.invokeSvStatusChanged(GpsStatusListenerTransport);
      if (GpsStatusListenerTransport != null) {
         synchronized(this.mGpsListeners) {
            this.mGpsListeners.add(GpsStatusListenerTransport);
         }
      }

      this.checkWork();
      this.notifyGpsStatus(GpsStatusListenerTransport);
      this.startGpsTask();
   }

   private void notifyGpsStatus(final Object transport) {
      if (transport != null) {
         this.mWorkHandler.post(new Runnable() {
            public void run() {
               MockLocationHelper.invokeSvStatusChanged(transport);
               MockLocationHelper.invokeNmeaReceived(transport);
            }
         });
      }
   }

   public void removeUpdates(Object[] args) {
      if (args[0] != null) {
         UpdateLocationTask task = this.getTask(args[0]);
         if (task != null) {
            task.stop();
         }
      }

   }

   public void requestLocationUpdates(Object[] args) {
      int index;
      if (VERSION.SDK_INT >= 17) {
         index = 1;
      } else {
         index = args.length - 1;
      }

      Object listenerTransport = args[index];
      if (listenerTransport == null) {
         Log.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITwED2szSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OxgYKWwFNCZiASxLIz0iDmoKTSVsNC8xLC0uKGAVSFo=")));
      } else {
         long mInterval;
         if (VERSION.SDK_INT >= 17) {
            try {
               mInterval = (Long)Reflect.on(args[0]).get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwYYCGwFNARmNCAo")));
            } catch (Throwable var11) {
               mInterval = 60000L;
            }
         } else {
            mInterval = (Long)MethodParameterUtils.getFirstParam(args, Long.class);
         }

         VLocation location = this.getCurAppLocation();
         this.checkWork();
         this.notifyLocation(listenerTransport, location.toSysLocation(), true);
         UpdateLocationTask task = this.getTask(listenerTransport);
         if (task == null) {
            synchronized(this.mLocationTaskMap) {
               task = new UpdateLocationTask(listenerTransport, mInterval);
               this.mLocationTaskMap.put(listenerTransport, task);
            }
         }

         task.start();
      }

   }

   private boolean notifyLocation(final Object ListenerTransport, final Location location, boolean post) {
      if (ListenerTransport == null) {
         return false;
      } else if (!post) {
         try {
            mirror.android.location.LocationManager.ListenerTransport.onLocationChanged.call(ListenerTransport, location);
            return true;
         } catch (Throwable var5) {
            Throwable e = var5;
            e.printStackTrace();
            return false;
         }
      } else {
         this.mWorkHandler.post(new Runnable() {
            public void run() {
               try {
                  mirror.android.location.LocationManager.ListenerTransport.onLocationChanged.call(ListenerTransport, location);
               } catch (Throwable var2) {
                  Throwable e = var2;
                  e.printStackTrace();
               }

            }
         });
         return true;
      }
   }

   private UpdateLocationTask getTask(Object locationListener) {
      synchronized(this.mLocationTaskMap) {
         UpdateLocationTask task = (UpdateLocationTask)this.mLocationTaskMap.get(locationListener);
         return task;
      }
   }

   private class UpdateLocationTask implements Runnable {
      private Object mListenerTransport;
      private long mTime;
      private volatile boolean mRunning;

      private UpdateLocationTask(Object ListenerTransport, long time) {
         this.mListenerTransport = ListenerTransport;
         this.mTime = time;
      }

      public void run() {
         if (this.mRunning) {
            VLocation location = VLocationManager.this.getCurAppLocation();
            if (location != null && VLocationManager.this.notifyLocation(this.mListenerTransport, location.toSysLocation(), false)) {
               this.start();
            }
         }

      }

      public void start() {
         this.mRunning = true;
         VLocationManager.this.mWorkHandler.removeCallbacks(this);
         if (this.mTime > 0L) {
            VLocationManager.this.mWorkHandler.postDelayed(this, this.mTime);
         } else {
            VLocationManager.this.mWorkHandler.post(this);
         }

      }

      public void stop() {
         this.mRunning = false;
         VLocationManager.this.mWorkHandler.removeCallbacks(this);
      }

      // $FF: synthetic method
      UpdateLocationTask(Object x1, long x2, Object x3) {
         this(x1, x2);
      }
   }
}
