package com.lody.virtual.helper.compat;

import android.annotation.TargetApi;
import android.app.job.JobWorkItem;
import android.content.Intent;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.ComponentUtils;

@TargetApi(26)
public class JobWorkItemCompat {
   public static JobWorkItem redirect(int userId, JobWorkItem item, String pkg) {
      if (item != null) {
         Intent target = (Intent)mirror.android.app.job.JobWorkItem.getIntent.call(item);
         if (target.hasExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9jDlkgKAcYLmMFSFo=")))) {
            return item;
         } else {
            Intent intent = ComponentUtils.getProxyIntentSenderIntent(userId, 4, pkg, target);
            JobWorkItem workItem = (JobWorkItem)mirror.android.app.job.JobWorkItem.ctor.newInstance(intent);
            int wordId = mirror.android.app.job.JobWorkItem.mWorkId.get(item);
            mirror.android.app.job.JobWorkItem.mWorkId.set(workItem, wordId);
            Object obj = mirror.android.app.job.JobWorkItem.mGrants.get(item);
            mirror.android.app.job.JobWorkItem.mGrants.set(workItem, obj);
            int deliveryCount = mirror.android.app.job.JobWorkItem.mDeliveryCount.get(item);
            mirror.android.app.job.JobWorkItem.mDeliveryCount.set(workItem, deliveryCount);
            return workItem;
         }
      } else {
         return null;
      }
   }

   public static JobWorkItem parse(JobWorkItem item) {
      if (item != null) {
         Intent target = (Intent)mirror.android.app.job.JobWorkItem.getIntent.call(item);
         if (target.hasExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9jDlkgKAcYLmMFSFo=")))) {
            return item;
         } else {
            Intent intent = ComponentUtils.parseIntentSenderInfo(item.getIntent(), false).intent;
            ComponentUtils.unpackFillIn(intent, JobWorkItemCompat.class.getClassLoader());
            JobWorkItem workItem = (JobWorkItem)mirror.android.app.job.JobWorkItem.ctor.newInstance(intent);
            int wordId = mirror.android.app.job.JobWorkItem.mWorkId.get(item);
            mirror.android.app.job.JobWorkItem.mWorkId.set(workItem, wordId);
            Object obj = mirror.android.app.job.JobWorkItem.mGrants.get(item);
            mirror.android.app.job.JobWorkItem.mGrants.set(workItem, obj);
            int deliveryCount = mirror.android.app.job.JobWorkItem.mDeliveryCount.get(item);
            mirror.android.app.job.JobWorkItem.mDeliveryCount.set(workItem, deliveryCount);
            return workItem;
         }
      } else {
         return null;
      }
   }
}
