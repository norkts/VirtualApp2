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
   public static final String PACKAGE = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iAVRF"));
   public static final String ACTIVITY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQZF"));
   public static final String USER = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28jSFo="));
   public static final String APP = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KA=="));
   public static final String ACCOUNT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmEVRF"));
   public static final String CONTENT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmEVRF"));
   public static final String JOB = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4AOg=="));
   public static final String NOTIFICATION = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOC99JCAgKQdfDg=="));
   public static final String VS = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT02Vg=="));
   public static final String DEVICE = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguLmUVLCs="));
   public static final String VIRTUAL_LOC = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV10oKi0qVg=="));
   public static final String FILE_TRANSFER = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YDmhSEgZhNCA2Iy0+PWoVSFo="));
   public static final String PERMISSION = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguKmoVAgNhJAY1Kj5SVg=="));
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
            Bundle response = (new ProviderCall.Builder(context, getAuthority())).methodName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JhhSVg=="))).callSafely();
            if (response != null) {
               IBinder binder = BundleCompat.getBinder(response, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh99NAY2KBcMKGMFSFo=")));
               linkBinderDied(binder);
               sFetcher = IServiceFetcher.Stub.asInterface(binder);
            }
         }
      }

      return sFetcher;
   }

   public static void ensureServerStarted() {
      (new ProviderCall.Builder(VirtualCore.get().getContext(), getAuthority())).methodName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcKWwaFitsJCgqKAciLmkjBlo="))).callSafely();
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

         VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JS4uLGczNARmNAY5KAMfPWoJLyhsNyg/KhcMKksaRTBsHlwd")), name);
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
