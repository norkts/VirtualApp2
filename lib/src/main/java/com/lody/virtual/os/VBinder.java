package com.lody.virtual.os;

import android.os.Binder;
import com.lody.virtual.client.ipc.VActivityManager;

public class VBinder {
   public static int getCallingUid() {
      return VActivityManager.get().getUidByPid(Binder.getCallingPid());
   }

   public static int getBaseCallingUid() {
      return VUserHandle.getAppId(getCallingUid());
   }

   public static int getCallingPid() {
      return Binder.getCallingPid();
   }

   /** @deprecated */
   public static VUserHandle getCallingUserHandle() {
      return VUserHandle.getCallingUserHandle();
   }
}
