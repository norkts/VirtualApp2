package com.lody.virtual.client.hook.proxies.system;

import android.os.Bundle;
import android.os.IInterface;
import android.os.ISystemUpdateManager;
import android.os.PersistableBundle;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import mirror.android.os.ServiceManager;

public class SystemUpdateStub extends BinderInvocationProxy {
   private static final String SERVICE_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YKWwFNCNsJzAsKBciLmkjSFo="));

   public SystemUpdateStub() {
      super((IInterface)(new EmptySystemUpdateManagerImpl()), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YKWwFNCNsJzAsKBciLmkjSFo=")));
   }

   public void inject() throws Throwable {
      if (ServiceManager.checkService.call(SERVICE_NAME) == null) {
         super.inject();
      }

   }

   static class EmptySystemUpdateManagerImpl extends ISystemUpdateManager.Stub {
      public Bundle retrieveSystemUpdateInfo() {
         Bundle info = new Bundle();
         info.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wKNAM=")), 0);
         return info;
      }

      public void updateSystemUpdateInfo(PersistableBundle data) {
      }
   }
}
