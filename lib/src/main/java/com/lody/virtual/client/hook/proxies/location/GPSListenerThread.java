package com.lody.virtual.client.hook.proxies.location;

import android.location.Location;
import android.os.Handler;
import android.os.Build.VERSION;
import android.util.ArrayMap;
import com.lody.virtual.client.ipc.VirtualLocationManager;
import com.lody.virtual.remote.vloc.VLocation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import mirror.android.location.LocationManager;

class GPSListenerThread extends TimerTask {
   private static GPSListenerThread INSTANCE = new GPSListenerThread();
   private Handler handler = new Handler();
   private boolean isRunning = false;
   private HashMap<Object, Long> listeners = new HashMap();
   private Timer timer = new Timer();

   private void notifyGPSStatus(Map listeners) {
      if (listeners != null && !listeners.isEmpty()) {
         Set<Map.Entry> entries = listeners.entrySet();
         Iterator var3 = entries.iterator();

         while(var3.hasNext()) {
            Map.Entry entry = (Map.Entry)var3.next();

            try {
               Object value = entry.getValue();
               if (value != null) {
                  MockLocationHelper.invokeSvStatusChanged(value);
               }
            } catch (Throwable var6) {
               Throwable e = var6;
               e.printStackTrace();
            }
         }
      }

   }

   private void notifyLocation(Map listeners) {
      if (listeners != null) {
         try {
            if (!listeners.isEmpty()) {
               VLocation vLocation = VirtualLocationManager.get().getLocation();
               if (vLocation != null) {
                  Location location = vLocation.toSysLocation();
                  Set<Map.Entry> entries = listeners.entrySet();
                  Iterator var5 = entries.iterator();

                  while(var5.hasNext()) {
                     Map.Entry entry = (Map.Entry)var5.next();
                     Object value = entry.getValue();
                     if (value != null) {
                        try {
                           LocationManager.ListenerTransport.onLocationChanged.call(value, location);
                        } catch (Throwable var9) {
                           Throwable e = var9;
                           e.printStackTrace();
                        }
                     }
                  }
               }
            }
         } catch (Throwable var10) {
            Throwable e = var10;
            e.printStackTrace();
         }
      }

   }

   private void notifyMNmeaListener(Map listeners) {
      if (listeners != null && !listeners.isEmpty()) {
         Set<Map.Entry> entries = listeners.entrySet();
         Iterator var3 = entries.iterator();

         while(var3.hasNext()) {
            Map.Entry entry = (Map.Entry)var3.next();

            try {
               Object value = entry.getValue();
               if (value != null) {
                  MockLocationHelper.invokeNmeaReceived(value);
               }
            } catch (Exception var6) {
               Exception e = var6;
               e.printStackTrace();
            }
         }
      }

   }

   public void addListenerTransport(Object transport) {
      this.listeners.put(transport, System.currentTimeMillis());
      if (!this.isRunning) {
         synchronized(this) {
            if (!this.isRunning) {
               this.isRunning = true;
               this.timer.schedule(this, 1000L, 1000L);
            }
         }
      }

   }

   public void removeListenerTransport(Object transport) {
      if (transport != null) {
         this.listeners.remove(transport);
      }

   }

   public void run() {
      if (!this.listeners.isEmpty()) {
         if (VirtualLocationManager.get().getMode() == 0) {
            this.listeners.clear();
            return;
         }

         Iterator var1 = this.listeners.entrySet().iterator();

         while(var1.hasNext()) {
            Map.Entry entry = (Map.Entry)var1.next();

            try {
               Object transport = entry.getKey();
               Map gpsStatusListeners;
               final Map listeners;
               if (VERSION.SDK_INT >= 24) {
                  listeners = (Map)LocationManager.mGnssNmeaListeners.get(transport);
                  this.notifyGPSStatus((Map)LocationManager.mGnssStatusListeners.get(transport));
                  this.notifyMNmeaListener(listeners);
                  gpsStatusListeners = (Map)LocationManager.mGpsStatusListeners.get(transport);
                  if (gpsStatusListeners instanceof HashMap) {
                     this.notifyGPSStatus((HashMap)gpsStatusListeners);
                  } else if (gpsStatusListeners instanceof ArrayMap) {
                     this.notifyGPSStatus((ArrayMap)gpsStatusListeners);
                  }

                  this.notifyMNmeaListener((Map)LocationManager.mGpsNmeaListeners.get(transport));
               } else {
                  gpsStatusListeners = (Map)LocationManager.mGpsStatusListeners.get(transport);
                  if (gpsStatusListeners instanceof HashMap) {
                     this.notifyGPSStatus((HashMap)gpsStatusListeners);
                  } else if (gpsStatusListeners instanceof ArrayMap) {
                     this.notifyGPSStatus((ArrayMap)gpsStatusListeners);
                  }

                  this.notifyMNmeaListener((Map)LocationManager.mNmeaListeners.get(transport));
               }

               listeners = (Map)LocationManager.mListeners.get(transport);
               if (gpsStatusListeners != null && !gpsStatusListeners.isEmpty()) {
                  if (listeners != null && !listeners.isEmpty()) {
                     this.notifyLocation(listeners);
                  } else {
                     this.handler.postDelayed(new Runnable() {
                        public void run() {
                           GPSListenerThread.this.notifyLocation(listeners);
                        }
                     }, 100L);
                  }
               }
            } catch (Exception var6) {
               Exception e = var6;
               e.printStackTrace();
            }
         }
      }

   }

   public void stop() {
      this.timer.cancel();
   }

   public static GPSListenerThread get() {
      return INSTANCE;
   }

   private GPSListenerThread() {
   }
}
