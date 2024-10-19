package com.carlos.common.ui.delegate;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import com.carlos.libcommon.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.VAppInstallerParams;
import com.lody.virtual.remote.VAppInstallerResult;
import java.io.File;

public class MyAppRequestListener implements VirtualCore.AppRequestListener {
   private final Context mContext;

   public MyAppRequestListener(Context context) {
      this.mContext = context;
   }

   public void onRequestInstall(String path) {
      info(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii0qP28gMyhjDlkpLBciCG8zLCZrIFAr")) + path);
      VAppInstallerParams params = new VAppInstallerParams();
      VAppInstallerResult res = VirtualCore.get().installPackage(Uri.fromFile(new File(path)), params);
      if (res.status == 0) {
         info(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcKWwFJCRgVyRF")) + res.packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phc2I2szLCthJys2")));
         boolean success = VActivityManager.get().launchApp(0, res.packageName);
         info(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+I2ojLCBLHiAsI14mVg==")) + (success ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0uOWszNANhIFlF")) : StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4+CWoJBlo="))));
      } else {
         info(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcKWwFJCRgVyRF")) + res.packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgiP2UVGSRLHjAqIz1fKH4zAiVrESsxPQhSVg==")) + res.status);
      }

   }

   private static void info(String msg) {
      VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgc6KH0VBgNmHiAoKhcMKA==")), msg);
   }

   public void onRequestUninstall(String pkg) {
      Toast.makeText(this.mContext, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcLGgaFiliASQgPxgMDmwjMANvETgdLAQ6CGIFPDBuASw9Pl9XVg==")) + pkg, 0).show();
   }
}
