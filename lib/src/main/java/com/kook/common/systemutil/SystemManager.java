package com.kook.common.systemutil;

import android.content.Context;
import android.os.Binder;

public class SystemManager {
   public static boolean isSystemSign(Context context) {
      return context.getPackageManager().checkSignatures(Binder.getCallingUid(), 1000) == 0;
   }
}
