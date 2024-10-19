package com.carlos.common.ui.delegate;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import com.carlos.libcommon.StringFog;
import com.lody.virtual.client.hook.delegate.TaskDescriptionDelegate;
import com.lody.virtual.os.VUserManager;

@TargetApi(21)
public class MyTaskDescDelegate implements TaskDescriptionDelegate {
   public ActivityManager.TaskDescription getTaskDescription(ActivityManager.TaskDescription oldTaskDescription) {
      if (oldTaskDescription == null) {
         return null;
      } else {
         int userId = VUserManager.get().getUserHandle();
         String suffix = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl9fVg==")) + (userId + 1) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PAhSVg=="));
         String oldLabel = oldTaskDescription.getLabel() != null ? oldTaskDescription.getLabel() : "";
         return !oldLabel.endsWith(suffix) ? new ActivityManager.TaskDescription(oldTaskDescription.getLabel() + suffix, oldTaskDescription.getIcon(), oldTaskDescription.getPrimaryColor()) : oldTaskDescription;
      }
   }
}
