package com.lody.virtual.client.hook.proxies.location;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

class GPSStatusListenerThread extends TimerTask {
   private static GPSStatusListenerThread INSTANCE = new GPSStatusListenerThread();
   private boolean isRunning = false;
   private Map<Object, Long> listeners = new HashMap();
   private Timer timer = new Timer();

   public void addListenerTransport(Object transport) {
      if (!this.isRunning) {
         synchronized(this) {
            if (!this.isRunning) {
               this.isRunning = true;
               this.timer.schedule(this, 100L, 800L);
            }
         }
      }

      this.listeners.put(transport, System.currentTimeMillis());
   }

   public void removeListenerTransport(Object obj) {
      if (obj != null) {
         this.listeners.remove(obj);
      }

   }

   public void run() {
      if (!this.listeners.isEmpty()) {
         Iterator var1 = this.listeners.entrySet().iterator();

         while(var1.hasNext()) {
            Map.Entry entry = (Map.Entry)var1.next();

            try {
               Object transport = entry.getKey();
               MockLocationHelper.invokeSvStatusChanged(transport);
               MockLocationHelper.invokeNmeaReceived(transport);
            } catch (Throwable var4) {
               Throwable e = var4;
               e.printStackTrace();
            }
         }
      }

   }

   public void stop() {
      this.timer.cancel();
   }

   public static GPSStatusListenerThread get() {
      return INSTANCE;
   }

   private GPSStatusListenerThread() {
   }
}
