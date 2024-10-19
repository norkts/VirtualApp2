package com.lody.virtual.client.hook.proxies.system;

import android.os.IInterface;
import com.android.internal.widget.ILockSettings;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import java.util.Collections;
import mirror.android.os.ServiceManager;

public class LockSettingsStub extends BinderInvocationProxy {
   private static final String SERVICE_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgAOWU2GgNiAQogKQcYM2oFSFo="));

   public LockSettingsStub() {
      super((IInterface)(new EmptyLockSettings()), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgAOWU2GgNiAQogKQcYM2oFSFo=")));
   }

   public void inject() throws Throwable {
      if (ServiceManager.checkService.call(SERVICE_NAME) == null) {
         super.inject();
      }

   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcjNClgJzw/Iz4cAWUzQQZvDjBF")), Collections.emptyMap()));
   }

   static class EmptyLockSettings extends ILockSettings.Stub {
      public void setRecoverySecretTypes(int[] secretTypes) {
      }

      public int[] getRecoverySecretTypes() {
         return new int[0];
      }
   }
}
