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
      if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fP2ojPCtsJCw7KBc6PQ==")).equals(method)) {
         info = new BadgerInfo();
         info.userId = VUserHandle.myUserId();
         info.packageName = extras.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iAVRF")));
         info.className = extras.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4EP28wLFo=")));
         info.badgerCount = extras.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+PGgzNCZmDl06KAguVg==")));
         VActivityManager.get().notifyBadgerChange(info);
         out = new Bundle();
         out.putBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0uOWszNANhJ1RF")), true);
         return out;
      } else {
         if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMaIAJlNCAwKC0MEW8KGiZvEVRF")).equals(method)) {
            info = new BadgerInfo();
            info.userId = VUserHandle.myUserId();
            info.packageName = VClient.get().getCurrentPackage();
            info.badgerCount = extras.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGYzFjdiHjg/Ji0qDWUjMAY=")));
            VActivityManager.get().notifyBadgerChange(info);
            out = new Bundle();
            out.putBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0uOWszNANhJ1RF")), true);
         }

         return super.call(methodBox, method, arg, extras);
      }
   }
}
