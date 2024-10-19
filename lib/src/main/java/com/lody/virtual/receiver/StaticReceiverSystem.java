package com.lody.virtual.receiver;

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
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.env.SpecialComponentList;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.BroadcastIntentData;
import com.lody.virtual.remote.ReceiverInfo;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StaticReceiverSystem {
   private static final int BROADCAST_TIME_OUT = 8500;
   private static final String TAG = "StaticReceiverSystem";
   private static StaticReceiverSystem mSystem = new StaticReceiverSystem();
   private ApplicationInfo mApplicationInfo;
   private Map<IBinder, BroadcastRecord> mBroadcastRecords = new HashMap();
   private Context mContext;
   private StaticScheduler mScheduler;
   private TimeoutHandler mTimeoutHandler;
   private int mUserId;

   public StaticReceiverSystem() {
      this.mBroadcastRecords = new HashMap();
   }

   public static StaticReceiverSystem get() {
      return mSystem;
   }

   public Map BroadcastRecords(StaticReceiverSystem staticReceiverSystem) {
      return staticReceiverSystem.mBroadcastRecords;
   }

   public boolean handleStaticBroadcast(StaticReceiverSystem staticReceiverSystem, BroadcastIntentData broadcastIntentData, ActivityInfo activityInfo, BroadcastReceiver.PendingResult pendingResult) {
      if (broadcastIntentData.targetPackage != null && !broadcastIntentData.targetPackage.equals(activityInfo.packageName)) {
         return false;
      } else if (broadcastIntentData.userId != -1 && broadcastIntentData.userId != this.mUserId) {
         return false;
      } else {
         ComponentName componentName = ComponentUtils.toComponentName(activityInfo);
         BroadcastRecord broadcastRecord = new BroadcastRecord(pendingResult, activityInfo);
         IBinder iBinder = (IBinder)mirror.android.content.BroadcastReceiver.PendingResult.mToken.get(pendingResult);
         synchronized(this.mBroadcastRecords) {
            this.mBroadcastRecords.put(iBinder, broadcastRecord);
         }

         Message message = new Message();
         message.obj = iBinder;
         this.mTimeoutHandler.sendMessageDelayed(message, 8500L);
         VClient.get().scheduleReceiver(activityInfo.processName, componentName, broadcastIntentData.intent, pendingResult);
         return true;
      }
   }

   @RequiresApi(
      api = 26
   )
   public void attach(String processName, Context context, ApplicationInfo appInfo, int userId) {
      StaticReceiverSystem staticReceiverSystem = this;
      if (staticReceiverSystem.mApplicationInfo != null) {
         throw new IllegalStateException("attached");
      } else {
         staticReceiverSystem.mContext = context;
         staticReceiverSystem.mApplicationInfo = appInfo;
         staticReceiverSystem.mUserId = userId;
         HandlerThread broadcastThread = new HandlerThread("BroadcastThread");
         HandlerThread anrThread = new HandlerThread("BroadcastAnrThread");
         broadcastThread.start();
         anrThread.start();
         staticReceiverSystem.mScheduler = new StaticScheduler(broadcastThread.getLooper());
         staticReceiverSystem.mTimeoutHandler = new TimeoutHandler(staticReceiverSystem, anrThread.getLooper());
         List<ReceiverInfo> receiverList = VPackageManager.get().getReceiverInfos(appInfo.packageName, processName, userId);

         for(Iterator var11 = receiverList.iterator(); var11.hasNext(); staticReceiverSystem = this) {
            ReceiverInfo receiverInfo = (ReceiverInfo)var11.next();
            String componentAction = ComponentUtils.getComponentAction(receiverInfo.info);
            IntentFilter componentFilter = new IntentFilter(componentAction);
            componentFilter.addCategory("__VA__|_static_receiver_");
            String str;
            if (BuildCompat.isUpsideDownCake()) {
               str = "__VA__|_static_receiver_";
               staticReceiverSystem.mContext.registerReceiver(new StaticReceiver(staticReceiverSystem, receiverInfo.info), componentFilter, (String)null, staticReceiverSystem.mScheduler, 2);
            } else {
               str = "__VA__|_static_receiver_";
               staticReceiverSystem.mContext.registerReceiver(new StaticReceiver(staticReceiverSystem, receiverInfo.info), componentFilter, (String)null, staticReceiverSystem.mScheduler);
            }

            for(Iterator var15 = receiverInfo.filters.iterator(); var15.hasNext(); staticReceiverSystem = this) {
               IntentFilter filter = (IntentFilter)var15.next();
               SpecialComponentList.protectIntentFilter(filter);
               String str2 = str;
               filter.addCategory(str2);
               Object obj;
               if (BuildCompat.isUpsideDownCake()) {
                  staticReceiverSystem.mContext.registerReceiver(new StaticReceiver(staticReceiverSystem, receiverInfo.info), filter, (String)null, staticReceiverSystem.mScheduler, 2);
                  obj = null;
               } else {
                  Context context2 = staticReceiverSystem.mContext;
                  StaticReceiver staticReceiver = new StaticReceiver(staticReceiverSystem, receiverInfo.info);
                  StaticScheduler staticScheduler = staticReceiverSystem.mScheduler;
                  obj = null;
                  context2.registerReceiver(staticReceiver, filter, (String)null, staticScheduler);
               }

               str = str2;
            }
         }

      }
   }

   public boolean broadcastFinish(IBinder iBinder) {
      BroadcastRecord remove;
      synchronized(this.mBroadcastRecords) {
         remove = (BroadcastRecord)this.mBroadcastRecords.remove(iBinder);
      }

      if (remove == null) {
         return false;
      } else {
         this.mTimeoutHandler.removeMessages(0, iBinder);
         remove.pendingResult.finish();
         return true;
      }
   }

   static {
      mSystem = new StaticReceiverSystem();
   }

   final class TimeoutHandler extends Handler {
      final StaticReceiverSystem staticReceiverSystem;

      public TimeoutHandler(@NonNull StaticReceiverSystem s, Looper looper) {
         super(looper);
         this.staticReceiverSystem = s;
      }

      public void handleMessage(@NonNull Message msg) {
         BroadcastRecord broadcastRecord = (BroadcastRecord)StaticReceiverSystem.get().BroadcastRecords(this.staticReceiverSystem).remove((IBinder)msg.obj);
         if (broadcastRecord != null) {
            VLog.w("StaticReceiverSystem", "Broadcast timeout, cancel to dispatch it.");
            broadcastRecord.pendingResult.finish();
         }

      }
   }

   final class StaticScheduler extends Handler {
      public StaticScheduler(@NonNull Looper looper) {
         super(looper);
      }
   }

   class StaticReceiver extends BroadcastReceiver {
      private ActivityInfo info;
      final StaticReceiverSystem staticReceiverSystem;

      public StaticReceiver(StaticReceiverSystem staticReceiverSystem, ActivityInfo info) {
         this.info = info;
         this.staticReceiverSystem = staticReceiverSystem;
      }

      public void onReceive(Context context, Intent intent) {
         if ((intent.getFlags() & 1073741824) == 0 && !this.isInitialStickyBroadcast() && VClient.get() != null && VClient.get().getCurrentApplication() != null) {
            if (intent.getAction() == null || !intent.getAction().startsWith("_VA_protected_")) {
               intent.setExtrasClassLoader(VClient.get().getCurrentApplication().getClassLoader());
               BroadcastIntentData data = new BroadcastIntentData(intent);
               if (data.intent == null) {
                  data.intent = intent;
                  data.targetPackage = intent.getPackage();
                  intent.setPackage((String)null);
               }

               BroadcastReceiver.PendingResult result = this.goAsync();
               if (result != null && !StaticReceiverSystem.get().handleStaticBroadcast(this.staticReceiverSystem, data, this.info, result)) {
                  result.finish();
               }

            }
         }
      }
   }

   final class BroadcastRecord {
      BroadcastReceiver.PendingResult pendingResult;
      ActivityInfo receiverInfo;

      public BroadcastRecord(BroadcastReceiver.PendingResult pendingResult, ActivityInfo receiverInfo) {
         this.pendingResult = pendingResult;
         this.receiverInfo = receiverInfo;
      }
   }
}
