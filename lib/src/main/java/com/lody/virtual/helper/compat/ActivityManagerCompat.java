package com.lody.virtual.helper.compat;

import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;
import android.os.Build.VERSION;
import mirror.android.app.ActivityManager;
import mirror.android.app.ActivityManagerNative;
import mirror.android.app.IActivityManager;
import mirror.android.app.IActivityManagerICS;
import mirror.android.app.IActivityManagerL;
import mirror.android.app.IActivityManagerN;

public class ActivityManagerCompat {
   public static final int SERVICE_DONE_EXECUTING_ANON = 0;
   public static final int SERVICE_DONE_EXECUTING_START = 1;
   public static final int SERVICE_DONE_EXECUTING_STOP = 2;
   public static final int START_INTENT_NOT_RESOLVED;
   public static final int START_NOT_CURRENT_USER_ACTIVITY;
   public static final int START_TASK_TO_FRONT;
   public static final int INTENT_SENDER_BROADCAST = 1;
   public static final int INTENT_SENDER_ACTIVITY = 2;
   public static final int INTENT_SENDER_ACTIVITY_RESULT = 3;
   public static final int INTENT_SENDER_SERVICE = 4;
   public static final int USER_OP_SUCCESS = 0;

   public static boolean finishActivity(IBinder token, int code, Intent data) {
      if (VERSION.SDK_INT >= 24) {
         return (Boolean)IActivityManagerN.finishActivity.call(ActivityManagerNative.getDefault.call(), token, code, data, 0);
      } else if (VERSION.SDK_INT >= 21) {
         return (Boolean)IActivityManagerL.finishActivity.call(ActivityManagerNative.getDefault.call(), token, code, data, false);
      } else {
         IActivityManagerICS.finishActivity.call(ActivityManagerNative.getDefault.call(), token, code, data);
         return false;
      }
   }

   public static void setActivityOrientation(Activity activity, int orientation) {
      try {
         activity.setRequestedOrientation(orientation);
      } catch (Throwable var7) {
         Throwable e = var7;
         e.printStackTrace();

         Activity parent;
         for(parent = (Activity)mirror.android.app.Activity.mParent.get(activity); parent != null; parent = (Activity)mirror.android.app.Activity.mParent.get(parent)) {
         }

         IBinder token = (IBinder)mirror.android.app.Activity.mToken.get(parent);

         try {
            IActivityManager.setRequestedOrientation.call(ActivityManagerNative.getDefault.call(), token, orientation);
         } catch (Throwable var6) {
            Throwable ex = var6;
            ex.printStackTrace();
         }
      }

   }

   static {
      START_INTENT_NOT_RESOLVED = ActivityManager.START_INTENT_NOT_RESOLVED == null ? -1 : ActivityManager.START_INTENT_NOT_RESOLVED.get();
      START_NOT_CURRENT_USER_ACTIVITY = ActivityManager.START_NOT_CURRENT_USER_ACTIVITY == null ? -8 : ActivityManager.START_NOT_CURRENT_USER_ACTIVITY.get();
      START_TASK_TO_FRONT = ActivityManager.START_TASK_TO_FRONT == null ? 2 : ActivityManager.START_TASK_TO_FRONT.get();
   }
}
