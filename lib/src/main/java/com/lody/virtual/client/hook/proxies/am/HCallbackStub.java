package com.lody.virtual.client.hook.proxies.am;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.proxies.app.ActivityClientControllerStub;
import com.lody.virtual.client.interfaces.IInjector;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.AvoidRecursive;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.InstalledAppInfo;
import com.lody.virtual.remote.ShadowActivityInfo;
import java.util.List;
import mirror.android.app.ActivityClient;
import mirror.android.app.ActivityManagerNative;
import mirror.android.app.ActivityThread;
import mirror.android.app.ClientTransactionHandler;
import mirror.android.app.IActivityManager;
import mirror.android.app.servertransaction.ClientTransaction;
import mirror.android.app.servertransaction.ClientTransactionItem;
import mirror.android.app.servertransaction.LaunchActivityItem;
import mirror.android.app.servertransaction.TopResumedActivityChangeItem;

public class HCallbackStub implements Handler.Callback, IInjector {
   private static final int LAUNCH_ACTIVITY;
   private static final int EXECUTE_TRANSACTION;
   private static final int SCHEDULE_CRASH;
   private static final String TAG;
   private static final HCallbackStub sCallback;
   private final AvoidRecursive mAvoidRecurisve = new AvoidRecursive();
   private Handler.Callback otherCallback;

   private HCallbackStub() {
   }

   public static HCallbackStub getDefault() {
      return sCallback;
   }

   private static Handler getH() {
      return (Handler)ActivityThread.mH.get(VirtualCore.mainThread());
   }

   private static Handler.Callback getHCallback() {
      try {
         Handler handler = getH();
         return (Handler.Callback)mirror.android.os.Handler.mCallback.get(handler);
      } catch (Throwable var1) {
         Throwable e = var1;
         e.printStackTrace();
         return null;
      }
   }

   public boolean handleMessage(Message msg) {
      if (this.mAvoidRecurisve.beginCall()) {
         boolean var7;
         try {
            if (LAUNCH_ACTIVITY == msg.what) {
               if (!this.handleLaunchActivity(msg, msg.obj)) {
                  var7 = true;
                  return var7;
               }
            } else if (BuildCompat.isPie() && EXECUTE_TRANSACTION == msg.what) {
               if (!this.handleExecuteTransaction(msg)) {
                  var7 = true;
                  return var7;
               }
            } else if (SCHEDULE_CRASH == msg.what) {
               String crashReason = (String)msg.obj;
               (new RemoteException(crashReason)).printStackTrace();
               boolean var3 = false;
               return var3;
            }

            if (this.otherCallback == null) {
               return false;
            }

            var7 = this.otherCallback.handleMessage(msg);
         } finally {
            this.mAvoidRecurisve.finishCall();
         }

         return var7;
      } else {
         return false;
      }
   }

   private boolean handleExecuteTransaction(Message msg) {
      Object transaction = msg.obj;
      IBinder token = this.getTokenByClientTransaction(transaction);
      Object r = ClientTransactionHandler.getActivityClient.call(VirtualCore.mainThread(), token);
      List<Object> activityCallbacks = (List)ClientTransaction.mActivityCallbacks.get(transaction);
      if (activityCallbacks != null && !activityCallbacks.isEmpty()) {
         Object item = activityCallbacks.get(0);
         if (item == null) {
            return true;
         } else if (r == null) {
            return item.getClass() != LaunchActivityItem.TYPE ? true : this.handleLaunchActivity(msg, item);
         } else {
            if (BuildCompat.isQ() && TopResumedActivityChangeItem.TYPE != null && item.getClass() == TopResumedActivityChangeItem.TYPE) {
               try {
                  if (TopResumedActivityChangeItem.mOnTop.get(item) == ActivityThread.ActivityClientRecord.isTopResumedActivity.get(r)) {
                     Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2LGUaOC9mERk8LBdfKn4wTSVsJx4/IxgAKksaPDdlNAoqLz01JGwjNCB7Diw6DRcYJWIaRSZ7J1RF")) + TopResumedActivityChangeItem.mOnTop.get(item));
                     return false;
                  }
               } catch (Throwable var8) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return true;
      }
   }

   private IBinder getTokenByClientTransaction(Object transaction) {
      IBinder token = null;
      if (ClientTransaction.mActivityToken == null) {
         List items;
         Object mLifecycleStateRequest;
         if (ClientTransaction.getTransactionItems != null && (items = (List)ClientTransaction.getTransactionItems.call(transaction)) != null && !items.isEmpty()) {
            mLifecycleStateRequest = items.get(0);
            token = (IBinder)ClientTransactionItem.getActivityToken.call(mLifecycleStateRequest);
         }

         if (token == null) {
            mLifecycleStateRequest = ClientTransaction.mLifecycleStateRequest.get(transaction);
            if (mLifecycleStateRequest == null) {
               List<Object> activityCallbacks = (List)ClientTransaction.mActivityCallbacks.get(transaction);
               return activityCallbacks != null && !activityCallbacks.isEmpty() ? (IBinder)ClientTransactionItem.getActivityToken.call(activityCallbacks.get(0)) : token;
            } else {
               return (IBinder)ClientTransactionItem.getActivityToken.call(mLifecycleStateRequest);
            }
         } else {
            return token;
         }
      } else {
         return (IBinder)ClientTransaction.mActivityToken.get(transaction);
      }
   }

   private boolean handleLaunchActivity(Message msg, Object r) {
      Intent stubIntent;
      if (BuildCompat.isPie()) {
         stubIntent = (Intent)LaunchActivityItem.mIntent.get(r);
      } else {
         stubIntent = (Intent)ActivityThread.ActivityClientRecord.intent.get(r);
      }

      ShadowActivityInfo saveInstance = new ShadowActivityInfo(stubIntent);
      if (saveInstance.intent == null) {
         return true;
      } else {
         Intent intent = saveInstance.intent;
         IBinder token;
         if (BuildCompat.isPie()) {
            token = (IBinder)ClientTransaction.mActivityToken.get(msg.obj);
         } else {
            token = this.getTokenByClientTransaction(msg.obj);
         }

         ActivityInfo info = saveInstance.info;
         if (info == null) {
            return true;
         } else if (VClient.get().getClientConfig() == null) {
            InstalledAppInfo installedAppInfo = VirtualCore.get().getInstalledAppInfo(info.packageName, 0);
            if (installedAppInfo == null) {
               return true;
            } else {
               VActivityManager.get().processRestarted(info.packageName, info.processName, saveInstance.userId);
               getH().sendMessageAtFrontOfQueue(Message.obtain(msg));
               return false;
            }
         } else {
            VClient.get().bindApplication(info.packageName, info.processName);
            int taskId = (Integer)IActivityManager.getTaskForActivity.call(ActivityManagerNative.getDefault.call(), token, false);
            VActivityManager.get().onActivityCreate(saveInstance.virtualToken, token, taskId);
            ClassLoader appClassLoader = VClient.get().getClassLoader(info.applicationInfo);
            ComponentUtils.unpackFillIn(intent, appClassLoader);
            if (BuildCompat.isPie()) {
               if (BuildCompat.isS() && ActivityThread.getLaunchingActivity != null) {
                  Object activityClientRecord = ActivityThread.getLaunchingActivity.call(VirtualCore.mainThread(), token);
                  if (activityClientRecord != null) {
                     Object compatInfo = ActivityThread.ActivityClientRecord.compatInfo.get(activityClientRecord);
                     Object loadedApk = ActivityThread.getPackageInfoNoCheck.call(VirtualCore.mainThread(), info.applicationInfo, compatInfo);
                     ActivityThread.ActivityClientRecord.intent.set(activityClientRecord, intent);
                     ActivityThread.ActivityClientRecord.activityInfo.set(activityClientRecord, info);
                     ActivityThread.ActivityClientRecord.packageInfo.set(activityClientRecord, loadedApk);
                  }
               }

               if (BuildCompat.isS() && LaunchActivityItem.mActivityClientController != null) {
                  IInterface activityClientController = (IInterface)LaunchActivityItem.mActivityClientController.get(r);
                  if (activityClientController != null) {
                     ActivityClient.ActivityClientControllerSingleton.mKnownInstance.set(ActivityClient.INTERFACE_SINGLETON.get(), ActivityClientControllerStub.getProxyInterface());
                  }
               }

               LaunchActivityItem.mIntent.set(r, intent);
               LaunchActivityItem.mInfo.set(r, info);
            } else {
               ActivityThread.ActivityClientRecord.intent.set(r, intent);
               ActivityThread.ActivityClientRecord.activityInfo.set(r, info);
            }

            return true;
         }
      }
   }

   public void inject() {
      this.otherCallback = getHCallback();
      mirror.android.os.Handler.mCallback.set(getH(), this);
   }

   public boolean isEnvBad() {
      Handler.Callback callback = getHCallback();
      boolean envBad = callback != this;
      if (callback != null && envBad) {
         VLog.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBY2P2oFHip9DigxPxcAOWoJTSpoAS8dPQgACmMaLDV5HiwqKT4iJmgFLD17CgE3")) + callback);
      }

      return envBad;
   }

   static {
      SCHEDULE_CRASH = ActivityThread.H.SCHEDULE_CRASH.get();
      LAUNCH_ACTIVITY = BuildCompat.isPie() ? -1 : ActivityThread.H.LAUNCH_ACTIVITY.get();
      EXECUTE_TRANSACTION = BuildCompat.isPie() ? ActivityThread.H.EXECUTE_TRANSACTION.get() : -1;
      TAG = HCallbackStub.class.getSimpleName();
      sCallback = new HCallbackStub();
   }
}
