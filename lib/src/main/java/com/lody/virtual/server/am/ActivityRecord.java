package com.lody.virtual.server.am;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

class ActivityRecord extends Binder {
   public TaskRecord task;
   public ActivityInfo info;
   public ComponentName component;
   public Intent intent;
   public IBinder token;
   public IBinder resultTo;
   String resultWho;
   int requestCode;
   Bundle options;
   public int userId;
   public ProcessRecord process;
   public boolean marked;
   public boolean started;
   public ClearTaskAction pendingClearAction;
   public PendingNewIntent pendingNewIntent;

   public ActivityRecord(int userId, Intent intent, ActivityInfo info, IBinder resultTo) {
      this.pendingClearAction = ClearTaskAction.NONE;
      this.userId = userId;
      this.intent = intent;
      this.info = info;
      if (info.targetActivity != null) {
         this.component = new ComponentName(info.packageName, info.targetActivity);
      } else {
         this.component = new ComponentName(info.packageName, info.name);
      }

      this.resultTo = resultTo;
   }

   public void init(TaskRecord task, ProcessRecord process, IBinder token) {
      this.userId = task.userId;
      this.task = task;
      this.process = process;
      this.token = token;
      this.started = true;
   }
}
