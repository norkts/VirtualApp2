package com.carlos.common.syncversion;

import android.content.Context;
import com.carlos.common.download.DownloadListner;
import com.carlos.common.download.DownloadManager;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;

public class AppUpgradeManager {
   public static AppUpgradeManager mAppUpgradeManager = new AppUpgradeManager();
   private boolean SYNC_STATUS = false;
   private String APPLICATION_SERVER_URL = "https://github.com/ServenScorpion/virtualapp_version_release_config/blob/master/va_config.xml";

   public static AppUpgradeManager getInstance() {
      return mAppUpgradeManager;
   }

   public boolean syncVersion(Context context, final SyncCallback syncCallback) {
      this.SYNC_STATUS = false;
      DownloadManager downloadManager = DownloadManager.getInstance();
      downloadManager.add(context, this.APPLICATION_SERVER_URL, new DownloadListner() {
         public void onFinished() {
            AppUpgradeManager.this.SYNC_STATUS = true;
            syncCallback.finishedListener();
            HVLog.d("va_config 下载完成");
         }

         public void onProgress(float progress) {
            HVLog.d("va_config 下载 progress ：" + progress);
         }

         public void onPause() {
            HVLog.d("va_config 暂停下载");
         }

         public void onCancel() {
            HVLog.d("va_config 取消下载");
         }
      });
      downloadManager.downloadSingle(this.APPLICATION_SERVER_URL);
      return true;
   }

   public interface SyncCallback {
      void finishedListener();
   }
}
