package com.lody.virtual.helper.compat;

import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.os.Build.VERSION;
import java.util.ArrayList;
import java.util.List;
import mirror.android.app.IApplicationThread;
import mirror.android.app.IApplicationThreadICSMR1;
import mirror.android.app.IApplicationThreadKitkat;
import mirror.android.app.IApplicationThreadOreo;
import mirror.android.app.ServiceStartArgs;
import mirror.android.content.res.CompatibilityInfo;

public class IApplicationThreadCompat {
   public static void scheduleCreateService(IInterface appThread, IBinder token, ServiceInfo info) throws RemoteException {
      if (VERSION.SDK_INT >= 19) {
         IApplicationThreadKitkat.scheduleCreateService.call(appThread, token, info, CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO.get(), 0);
      } else if (VERSION.SDK_INT >= 15) {
         IApplicationThreadICSMR1.scheduleCreateService.call(appThread, token, info, CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO.get());
      } else {
         IApplicationThread.scheduleCreateService.call(appThread, token, info);
      }

   }

   public static void scheduleBindService(IInterface appThread, IBinder token, Intent intent, boolean rebind) throws RemoteException {
      if (VERSION.SDK_INT >= 19) {
         IApplicationThreadKitkat.scheduleBindService.call(appThread, token, intent, rebind, 0);
      } else {
         IApplicationThread.scheduleBindService.call(appThread, token, intent, rebind);
      }

   }

   public static void scheduleUnbindService(IInterface appThread, IBinder token, Intent intent) throws RemoteException {
      IApplicationThread.scheduleUnbindService.call(appThread, token, intent);
   }

   public static void scheduleServiceArgs(IInterface appThread, IBinder token, int startId, Intent args) throws RemoteException {
      if (BuildCompat.isOreo()) {
         List<Object> list = new ArrayList(1);
         Object serviceStartArg = ServiceStartArgs.ctor.newInstance(false, startId, 0, args);
         list.add(serviceStartArg);
         IApplicationThreadOreo.scheduleServiceArgs.call(appThread, token, ParceledListSliceCompat.create(list));
      } else if (VERSION.SDK_INT >= 15) {
         IApplicationThreadICSMR1.scheduleServiceArgs.call(appThread, token, false, startId, 0, args);
      } else {
         IApplicationThread.scheduleServiceArgs.call(appThread, token, startId, 0, args);
      }

   }

   public static void scheduleStopService(IInterface appThread, IBinder token) throws RemoteException {
      IApplicationThread.scheduleStopService.call(appThread, token);
   }
}
