package com.lody.virtual.server;

import android.os.IBinder;
import com.lody.virtual.helper.collection.ArrayMap;
import java.util.Map;

public class ServiceCache {
   private static final Map<String, IBinder> sCache = new ArrayMap(5);

   public static void addService(String name, IBinder service) {
      sCache.put(name, service);
   }

   public static IBinder removeService(String name) {
      return (IBinder)sCache.remove(name);
   }

   public static IBinder getService(String name) {
      return (IBinder)sCache.get(name);
   }
}
