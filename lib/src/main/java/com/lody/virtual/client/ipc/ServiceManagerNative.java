package com.lody.virtual.client.ipc;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.compat.BundleCompat;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.server.ServiceCache;
import com.lody.virtual.server.interfaces.IServiceFetcher;

public class ServiceManagerNative {
   public static final String PACKAGE = "package";
   public static final String ACTIVITY = "activity";
   public static final String USER = "user";
   public static final String APP = "app";
   public static final String ACCOUNT = "account";
   public static final String CONTENT = "content";
   public static final String JOB = "job";
   public static final String NOTIFICATION = "notification";
   public static final String VS = "vs";
   public static final String DEVICE = "device";
   public static final String VIRTUAL_LOC = "virtual-loc";
   public static final String FILE_TRANSFER = "file-transfer";
   public static final String PERMISSION = "permission";
   private static final String TAG = ServiceManagerNative.class.getSimpleName();
   private static IServiceFetcher sFetcher;

   private static String getAuthority() {
      return VirtualCore.getConfig().getBinderProviderAuthority();
   }

   private static IServiceFetcher getServiceFetcher() {
      if (sFetcher == null || !sFetcher.asBinder().isBinderAlive()) {
         Class var0 = ServiceManagerNative.class;
         synchronized(ServiceManagerNative.class) {
            Context context = VirtualCore.get().getContext();
            Bundle response = (new ProviderCall.Builder(context, getAuthority())).methodName("@").callSafely();
            if (response != null) {
               IBinder binder = BundleCompat.getBinder(response, "_VA_|_binder_");
               linkBinderDied(binder);
               sFetcher = IServiceFetcher.Stub.asInterface(binder);
            }
         }
      }

      return sFetcher;
   }

   public static void ensureServerStarted() {
      (new ProviderCall.Builder(VirtualCore.get().getContext(), getAuthority())).methodName("ensure_created").callSafely();
   }

   public static void clearServerFetcher() {
      sFetcher = null;
   }

   private static void linkBinderDied(final IBinder binder) {
      IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
         public void binderDied() {
            binder.unlinkToDeath(this, 0);
         }
      };

      try {
         binder.linkToDeath(deathRecipient, 0);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         e.printStackTrace();
      }

   }

   public static IBinder getService(String name) {
      if (VirtualCore.get().isServerProcess()) {
         return ServiceCache.getService(name);
      } else {
         IServiceFetcher fetcher = getServiceFetcher();
         if (fetcher != null) {
            try {
               return fetcher.getService(name);
            } catch (RemoteException var3) {
               RemoteException e = var3;
               e.printStackTrace();
            }
         }

         VLog.e(TAG, "GetService(%s) return null.", name);
         return null;
      }
   }

   public static void addService(String name, IBinder service) {
      IServiceFetcher fetcher = getServiceFetcher();
      if (fetcher != null) {
         try {
            fetcher.addService(name, service);
         } catch (RemoteException var4) {
            RemoteException e = var4;
            e.printStackTrace();
         }
      }

   }

   public static void removeService(String name) {
      IServiceFetcher fetcher = getServiceFetcher();
      if (fetcher != null) {
         try {
            fetcher.removeService(name);
         } catch (RemoteException var3) {
            RemoteException e = var3;
            e.printStackTrace();
         }
      }

   }

   public static void linkToDeath(IBinder.DeathRecipient deathRecipient) {
      try {
         getServiceFetcher().asBinder().linkToDeath(deathRecipient, 0);
      } catch (RemoteException var2) {
         RemoteException e = var2;
         e.printStackTrace();
      }

   }
}
