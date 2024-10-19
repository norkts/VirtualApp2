package com.lody.virtual.client.hook.proxies.backup;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.helper.compat.BuildCompat;
import mirror.android.app.backup.IBackupManager;

public class BackupManagerStub extends BinderInvocationProxy {
   public BackupManagerStub() {
      super(IBackupManager.Stub.asInterface, "backup");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ResultStaticMethodProxy("dataChanged", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("clearBackupData", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("agentConnected", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("agentDisconnected", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("restoreAtInstall", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("setBackupEnabled", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("setBackupProvisioned", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("backupNow", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("fullBackup", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("fullTransportBackup", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("fullRestore", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("acknowledgeFullBackupOrRestore", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("getCurrentTransport", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("listAllTransports", new String[0]));
      this.addMethodProxy(new ResultStaticMethodProxy("selectBackupTransport", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("isBackupEnabled", false));
      this.addMethodProxy(new ResultStaticMethodProxy("setBackupPassword", true));
      this.addMethodProxy(new ResultStaticMethodProxy("hasBackupPassword", false));
      this.addMethodProxy(new ResultStaticMethodProxy("beginRestoreSession", (Object)null));
      if (BuildCompat.isOreo()) {
         this.addMethodProxy(new ResultStaticMethodProxy("selectBackupTransportAsync", (Object)null));
      }

      if (BuildCompat.isPie()) {
         this.addMethodProxy(new ResultStaticMethodProxy("updateTransportAttributes", (Object)null));
         this.addMethodProxy(new ResultStaticMethodProxy("isBackupServiceActive", false));
      }

   }
}
