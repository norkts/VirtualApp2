package com.carlos.science.client;

import android.annotation.SuppressLint;
import android.os.IBinder;
import com.carlos.libcommon.StringFog;
import com.carlos.science.client.hyxd.HYXDControllerImpl;
import com.carlos.science.client.normal.LearnControllerImpl;
import java.util.HashMap;
import java.util.Map;

public class ClientControlerServiceCache {
   @SuppressLint({"NewApi"})
   private static final Map<String, IBinder> sCache = new HashMap(5);

   public static void addService(String name, IBinder service) {
      sCache.put(name, service);
   }

   public static IBinder removeService(String name) {
      return (IBinder)sCache.remove(name);
   }

   public static IBinder getService(String name) {
      return sCache.containsKey(name) ? (IBinder)sCache.get(name) : (IBinder)sCache.get("def");
   }

   static {
      addService("def", LearnControllerImpl.get());
      addService("com.netease.hyxd.ewan", HYXDControllerImpl.get());
   }
}
