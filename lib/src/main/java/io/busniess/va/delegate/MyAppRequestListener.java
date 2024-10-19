package io.busniess.va.delegate;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
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
      info("Start installing: " + path);
      VAppInstallerParams params = new VAppInstallerParams();
      VAppInstallerResult res = VirtualCore.get().installPackage(Uri.fromFile(new File(path)), params);
      if (res.status == 0) {
         info("Install " + res.packageName + " success.");
         boolean success = VActivityManager.get().launchApp(0, res.packageName);
         info("launch app " + (success ? "success." : "fail."));
      } else {
         info("Install " + res.packageName + " fail, error code: " + res.status);
      }

   }

   private static void info(String msg) {
      VLog.e("AppInstaller", msg);
   }

   public void onRequestUninstall(String pkg) {
      Toast.makeText(this.mContext, "Intercept uninstall request: " + pkg, 0).show();
   }
}
