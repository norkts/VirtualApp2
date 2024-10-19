package com.carlos.science;

import com.carlos.libcommon.StringFog;
import com.carlos.science.exception.AgencyException;
import com.carlos.science.utils.ReflectUtil;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ServiceAgency {
   private final HashMap<Class, Object> cacheMap = new LinkedHashMap();
   private boolean isServiceConfigExists;

   public static <T> T getService(Class<T> tClass) {
      return ServiceAgency.InstanceHolder.instance.getServiceFromMap(tClass);
   }

   public static void clear() {
      ServiceAgency.InstanceHolder.instance.clearMap();
   }

   public <T> T getServiceFromMap(Class<T> tClass) {
      if (!this.isServiceConfigExists) {
         try {
            Class.forName(StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsIMxwCGxEfBxscHAkeExdADBYRGRsTDCwBHQMbEQ=="));
            this.isServiceConfigExists = true;
         } catch (ClassNotFoundException var8) {
            throw new AgencyException(StringFog.decrypt("PQpSFQkPLABDDhweBhsPBwBSAQwaN1MwCgAGAAwLMgIXGBFA"));
         }
      }

      T service = this.cacheMap.get(tClass);
      if (service == null) {
         ServiceConfig[] var3 = ServiceConfig.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ServiceConfig serviceEnum = var3[var5];

            try {
               Class serviceClass = Class.forName(serviceEnum.className);
               if (tClass.isAssignableFrom(serviceClass)) {
                  service = ReflectUtil.newInstance(serviceClass);
                  this.cacheMap.put(tClass, service);
                  return service;
               }
            } catch (ClassNotFoundException var9) {
               ClassNotFoundException e = var9;
               throw new AgencyException(e);
            }
         }
      }

      if (service == null) {
         throw new AgencyException(StringFog.decrypt("PQpSFQkPLABDBh8ABQoDFgsGBUU=") + tClass.getName() + StringFog.decrypt("UwQcEkUPMR0MGxMEDAtOBAwGHkU9OgEVBhEVKAgLHRFc"));
      } else {
         return service;
      }
   }

   public void clearMap() {
      this.cacheMap.clear();
   }

   private static class InstanceHolder {
      private static final ServiceAgency instance = new ServiceAgency();
   }
}
