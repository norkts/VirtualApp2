package com.lody.virtual.client.hook.providers;

import android.os.Bundle;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.hook.base.MethodBox;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.BadgerInfo;
import java.lang.reflect.InvocationTargetException;

public class BadgeProviderHook extends ExternalProviderHook {
   public BadgeProviderHook(IInterface base) {
      super(base);
   }

   public Bundle call(MethodBox methodBox, String method, String arg, Bundle extras) throws InvocationTargetException {
      BadgerInfo info;
      Bundle out;
      if ("change_badge".equals(method)) {
         info = new BadgerInfo();
         info.userId = VUserHandle.myUserId();
         info.packageName = extras.getString("package");
         info.className = extras.getString("class");
         info.badgerCount = extras.getInt("badgenumber");
         VActivityManager.get().notifyBadgerChange(info);
         out = new Bundle();
         out.putBoolean("success", true);
         return out;
      } else {
         if ("setAppBadgeCount".equals(method)) {
            info = new BadgerInfo();
            info.userId = VUserHandle.myUserId();
            info.packageName = VClient.get().getCurrentPackage();
            info.badgerCount = extras.getInt("app_badge_count");
            VActivityManager.get().notifyBadgerChange(info);
            out = new Bundle();
            out.putBoolean("success", true);
         }

         return super.call(methodBox, method, arg, extras);
      }
   }
}
