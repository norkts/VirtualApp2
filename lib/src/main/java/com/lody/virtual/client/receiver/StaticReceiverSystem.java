package com.lody.virtual.client.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.env.SpecialComponentList;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.BroadcastIntentData;
import com.lody.virtual.remote.ReceiverInfo;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StaticReceiverSystem {
   private static final String TAG = "StaticReceiverSystem";
   private static final StaticReceiverSystem mSystem = new StaticReceiverSystem();
   private static final int BROADCAST_TIME_OUT = 8500;
   private Context mContext;
   private ApplicationInfo mApplicationInfo;
   private int mUserId;
   private StaticScheduler mScheduler;
   private TimeoutHandler mTimeoutHandler;
   private final Map<IBinder, BroadcastRecord> mBroadcastRecords = new HashMap();

   public void attach(String processName, Context context, ApplicationInfo appInfo, int userId) {
      if (this.mApplicationInfo != null) {
         throw new IllegalStateException("attached");
      } else {
         this.mContext = context;
         this.mApplicationInfo = appInfo;
         this.mUserId = userId;
         HandlerThread broadcastThread = new HandlerThread("BroadcastThread");
         HandlerThread anrThread = new HandlerThread("BroadcastAnrThread");
         broadcastThread.start();
         anrThread.start();
         this.mScheduler = new StaticScheduler(broadcastThread.getLooper());
         this.mTimeoutHandler = new TimeoutHandler(anrThread.getLooper());
         List<ReceiverInfo> receiverList = VPackageManager.get().getReceiverInfos(appInfo.packageName, processName, userId);
         Iterator var8 = receiverList.iterator();

         while(var8.hasNext()) {
            ReceiverInfo receiverInfo = (ReceiverInfo)var8.next();
            String componentAction = ComponentUtils.getComponentAction(receiverInfo.info);
            IntentFilter componentFilter = new IntentFilter(componentAction);
            componentFilter.addCategory("__VA__|_static_receiver_");
            this.mContext.registerReceiver(new StaticReceiver(receiverInfo.info), componentFilter, (String)null, this.mScheduler);
            Iterator var12 = receiverInfo.filters.iterator();

            while(var12.hasNext()) {
               IntentFilter filter = (IntentFilter)var12.next();
               SpecialComponentList.protectIntentFilter(filter);
               filter.addCategory("__VA__|_static_receiver_");
               this.mContext.registerReceiver(new StaticReceiver(receiverInfo.info), filter, (String)null, this.mScheduler);
            }
         }

      }
   }

   public static StaticReceiverSystem get() {
      return mSystem;
   }

   public boolean broadcastFinish(IBinder token) {
      BroadcastRecord record;
      synchronized(this.mBroadcastRecords) {
         record = (BroadcastRecord)this.mBroadcastRecords.remove(token);
      }

      if (record == null) {
         return false;
      } else {
         this.mTimeoutHandler.removeMessages(0, token);
         record.pendingResult.finish();
         return true;
      }
   }

   private boolean handleStaticBroadcast(BroadcastIntentData data, ActivityInfo info, BroadcastReceiver.PendingResult result) {
      if (data.targetPackage != null && !data.targetPackage.equals(info.packageName)) {
         return false;
      } else if (data.userId != -1 && data.userId != this.mUserId) {
         return false;
      } else {
         ComponentName componentName = ComponentUtils.toComponentName(info);
         BroadcastRecord record = new BroadcastRecord(info, result);
         IBinder token = (IBinder)mirror.android.content.BroadcastReceiver.PendingResult.mToken.get(result);
         synchronized(this.mBroadcastRecords) {
            this.mBroadcastRecords.put(token, record);
         }

         Message msg = new Message();
         msg.obj = token;
         this.mTimeoutHandler.sendMessageDelayed(msg, 8500L);
         VClient.get().scheduleReceiver(info.processName, componentName, data.intent, result);
         return true;
      }
   }

   private class StaticReceiver extends BroadcastReceiver {
      private ActivityInfo info;

      public StaticReceiver(ActivityInfo info) {
         this.info = info;
      }

      public void onReceive(Context context, Intent intent) {
         if ((intent.getFlags() & 1073741824) == 0 && !this.isInitialStickyBroadcast() && VClient.get() != null && VClient.get().getCurrentApplication() != null) {
            intent.setExtrasClassLoader(VClient.get().getCurrentApplication().getClassLoader());
            BroadcastIntentData data = new BroadcastIntentData(intent);
            if (data.intent == null) {
               data.intent = intent;
               data.targetPackage = intent.getPackage();
               intent.setPackage((String)null);
            }

            BroadcastReceiver.PendingResult result = this.goAsync();
            if (result != null && !StaticReceiverSystem.this.handleStaticBroadcast(data, this.info, result)) {
               result.finish();
            }

         }
      }
   }

   private final class TimeoutHandler extends Handler {
      TimeoutHandler(Looper looper) {
         super(looper);
      }

      public void handleMessage(Message msg) {
         IBinder token = (IBinder)msg.obj;
         BroadcastRecord r = (BroadcastRecord)StaticReceiverSystem.this.mBroadcastRecords.remove(token);
         if (r != null) {
            VLog.w("StaticReceiverSystem", "Broadcast timeout, cancel to dispatch it.");
            r.pendingResult.finish();
         }

      }
   }

   private static final class BroadcastRecord {
      ActivityInfo receiverInfo;
      BroadcastReceiver.PendingResult pendingResult;

      BroadcastRecord(ActivityInfo receiverInfo, BroadcastReceiver.PendingResult pendingResult) {
         this.receiverInfo = receiverInfo;
         this.pendingResult = pendingResult;
      }
   }

   private static final class StaticScheduler extends Handler {
      StaticScheduler(Looper looper) {
         super(looper);
      }
   }
}
