package com.lody.virtual.client.hook.secondary;

import android.app.IServiceConnection;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.collection.ArrayMap;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.server.IBinderProxyService;
import mirror.android.app.ActivityThread;
import mirror.android.app.ContextImpl;
import mirror.android.app.IServiceConnectionO;
import mirror.android.app.LoadedApk;

public class ServiceConnectionProxy extends IServiceConnection.Stub {
   private static final ArrayMap<IBinder, ServiceConnectionProxy> sProxyMap = new ArrayMap();
   private IServiceConnection mConn;

   private ServiceConnectionProxy(IServiceConnection mConn) {
      this.mConn = mConn;
   }

   public IServiceConnection getBase() {
      return this.mConn;
   }

   public static IServiceConnection getDispatcher(Context context, ServiceConnection connection, int flags) {
      IServiceConnection sd = null;
      if (connection == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGojNClmHgY1KjkmMWoJTSZvAQId")));
      } else {
         try {
            Object activityThread = ActivityThread.currentActivityThread.call();
            Object loadApk = ContextImpl.mPackageInfo.get(VirtualCore.get().getContext());
            Handler handler = (Handler)ActivityThread.getHandler.call(activityThread);
            sd = (IServiceConnection)LoadedApk.getServiceDispatcher.call(loadApk, connection, context, handler, flags);
         } catch (Exception var7) {
            Exception e = var7;
            Log.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4ACGojNClmHgY1Kjs2PW8zGi1oDiwg")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGczNARmNAY5KAU2MWoKTTdvETAZLhcMVg==")), e);
         }

         if (sd == null) {
            throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oz4ALHsKLAVhESQ1Iz42PWk3TS9lMzw6Lxc2CmIKQCNpJFkdIz4APG8VSFo=")));
         } else {
            return sd;
         }
      }
   }

   public static IServiceConnection removeDispatcher(Context context, ServiceConnection conn) {
      IServiceConnection connection = null;

      try {
         Object loadApk = ContextImpl.mPackageInfo.get(VirtualCore.get().getContext());
         connection = (IServiceConnection)LoadedApk.forgetServiceDispatcher.call(loadApk, context, conn);
      } catch (Exception var4) {
         Exception e = var4;
         Log.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4ACGojNClmHgY1Kjs2PW8zGi1oDiwg")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4AKmgzNAZpJDAqLD0cP2khBi9sJDwsKgg2LGIFMFo=")), e);
      }

      return connection;
   }

   public static ServiceConnectionProxy getOrCreateProxy(IServiceConnection conn) {
      if (conn instanceof ServiceConnectionProxy) {
         return (ServiceConnectionProxy)conn;
      } else {
         IBinder binder = conn.asBinder();
         synchronized(sProxyMap) {
            ServiceConnectionProxy proxy = (ServiceConnectionProxy)sProxyMap.get(binder);
            if (proxy == null) {
               proxy = new ServiceConnectionProxy(conn);
               sProxyMap.put(binder, proxy);
            }

            return proxy;
         }
      }
   }

   public static ServiceConnectionProxy removeProxy(IServiceConnection conn) {
      if (conn == null) {
         return null;
      } else {
         synchronized(sProxyMap) {
            return (ServiceConnectionProxy)sProxyMap.remove(conn.asBinder());
         }
      }
   }

   public void connected(ComponentName name, IBinder service) throws RemoteException {
      this.connected(name, service, false);
   }

   public void connected(ComponentName name, IBinder service, boolean dead) throws RemoteException {
      if (service != null) {
         IBinderProxyService proxy = IBinderProxyService.Stub.asInterface(service);
         if (proxy != null) {
            name = proxy.getComponent();
            service = proxy.getService();
            if (VirtualCore.get().isVAppProcess()) {
               IBinder proxyService = ProxyServiceFactory.getProxyService(VClient.get().getCurrentApplication(), name, service);
               if (proxyService != null) {
                  service = proxyService;
               }
            }
         }

         if (BuildCompat.isOreo()) {
            IServiceConnectionO.connected.call(this.mConn, name, service, dead);
         } else {
            this.mConn.connected(name, service);
         }

      }
   }
}
