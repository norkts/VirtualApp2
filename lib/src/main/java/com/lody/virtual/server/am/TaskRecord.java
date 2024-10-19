package com.lody.virtual.server.am;

import android.content.ComponentName;
import android.content.Intent;
import com.lody.virtual.remote.AppTaskInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class TaskRecord {
   public final List<ActivityRecord> activities = new ArrayList();
   public int taskId;
   public int userId;
   public String affinity;
   public Intent taskRoot;

   TaskRecord(int taskId, int userId, String affinity, Intent intent) {
      this.taskId = taskId;
      this.userId = userId;
      this.affinity = affinity;
      this.taskRoot = intent;
   }

   ActivityRecord getRootActivityRecord() {
      synchronized(this.activities) {
         for(int i = 0; i < this.activities.size(); ++i) {
            ActivityRecord r = (ActivityRecord)this.activities.get(i);
            if (!r.started || !r.marked) {
               return r;
            }
         }

         return null;
      }
   }

   public ActivityRecord getTopActivityRecord() {
      return this.getTopActivityRecord(false);
   }

   public ActivityRecord getTopActivityRecord(boolean containFinishedActivity) {
      synchronized(this.activities) {
         if (this.activities.isEmpty()) {
            return null;
         } else {
            for(int i = this.activities.size() - 1; i >= 0; --i) {
               ActivityRecord r = (ActivityRecord)this.activities.get(i);
               if (r.started && (containFinishedActivity || !r.marked)) {
                  return r;
               }
            }

            return null;
         }
      }
   }

   AppTaskInfo getAppTaskInfo() {
      int len = this.activities.size();
      if (len <= 0) {
         return null;
      } else {
         ComponentName top = ((ActivityRecord)this.activities.get(len - 1)).component;
         return new AppTaskInfo(this.taskId, this.taskRoot, this.taskRoot.getComponent(), top);
      }
   }

   public boolean isFinishing() {
      synchronized(this.activities) {
         Iterator var2 = this.activities.iterator();

         ActivityRecord r;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            r = (ActivityRecord)var2.next();
         } while(!r.started || r.marked);

         return false;
      }
   }

   public void finish() {
      synchronized(this.activities) {
         ActivityRecord r;
         for(Iterator var2 = this.activities.iterator(); var2.hasNext(); r.marked = true) {
            r = (ActivityRecord)var2.next();
         }

      }
   }
}
