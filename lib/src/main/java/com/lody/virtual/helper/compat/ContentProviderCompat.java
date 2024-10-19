package com.lody.virtual.helper.compat;

import android.content.ContentProviderClient;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Build.VERSION;

public class ContentProviderCompat {
   public static Bundle call(Context context, Uri uri, String method, String arg, Bundle extras, int retryCount) throws IllegalAccessException {
      if (VERSION.SDK_INT < 17) {
         return context.getContentResolver().call(uri, method, arg, extras);
      } else {
         ContentProviderClient client = acquireContentProviderClientRetry(context, uri, retryCount);

         Bundle var13;
         try {
            if (client == null) {
               throw new IllegalAccessException();
            }

            var13 = client.call(method, arg, extras);
         } catch (RemoteException var11) {
            RemoteException e = var11;
            throw new IllegalAccessException(e.getMessage());
         } finally {
            releaseQuietly(client);
         }

         return var13;
      }
   }

   private static ContentProviderClient acquireContentProviderClient(Context context, Uri uri) {
      try {
         return VERSION.SDK_INT >= 16 ? context.getContentResolver().acquireUnstableContentProviderClient(uri) : context.getContentResolver().acquireContentProviderClient(uri);
      } catch (SecurityException var3) {
         SecurityException e = var3;
         e.printStackTrace();
         return null;
      }
   }

   public static ContentProviderClient acquireContentProviderClientRetry(Context context, Uri uri, int retryCount) {
      ContentProviderClient client = acquireContentProviderClient(context, uri);
      if (client == null) {
         for(int retry = 0; retry < retryCount && client == null; client = acquireContentProviderClient(context, uri)) {
            SystemClock.sleep(100L);
            ++retry;
         }
      }

      return client;
   }

   public static ContentProviderClient acquireContentProviderClientRetry(Context context, String name, int retryCount) {
      ContentProviderClient client = acquireContentProviderClient(context, name);
      if (client == null) {
         for(int retry = 0; retry < retryCount && client == null; client = acquireContentProviderClient(context, name)) {
            SystemClock.sleep(100L);
            ++retry;
         }
      }

      return client;
   }

   private static ContentProviderClient acquireContentProviderClient(Context context, String name) {
      return VERSION.SDK_INT >= 16 ? context.getContentResolver().acquireUnstableContentProviderClient(name) : context.getContentResolver().acquireContentProviderClient(name);
   }

   private static void releaseQuietly(ContentProviderClient client) {
      if (client != null) {
         try {
            if (VERSION.SDK_INT >= 24) {
               client.close();
            } else {
               client.release();
            }
         } catch (Exception var2) {
         }
      }

   }
}
