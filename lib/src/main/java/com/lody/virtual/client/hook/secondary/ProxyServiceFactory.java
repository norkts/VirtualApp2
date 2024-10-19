package com.lody.virtual.client.hook.secondary;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import java.util.HashMap;
import java.util.Map;

public class ProxyServiceFactory {
   private static final String TAG = ProxyServiceFactory.class.getSimpleName();
   private static Map<String, ServiceFetcher> sHookSecondaryServiceMap = new HashMap();

   public static IBinder getProxyService(Context context, ComponentName component, IBinder binder) {
      if (context != null && binder != null) {
         try {
            String description = binder.getInterfaceDescriptor();
            ServiceFetcher fetcher = (ServiceFetcher)sHookSecondaryServiceMap.get(description);
            if (fetcher != null) {
               IBinder res = fetcher.getService(context, context.getClassLoader(), binder);
               if (res != null) {
                  return res;
               }
            }
         } catch (Throwable var6) {
            Throwable e = var6;
            e.printStackTrace();
         }

         return null;
      } else {
         return null;
      }
   }

   private interface ServiceFetcher {
      IBinder getService(Context var1, ClassLoader var2, IBinder var3);
   }
}
