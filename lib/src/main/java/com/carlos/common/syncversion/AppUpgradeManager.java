package com.carlos.common.syncversion;

import android.content.Context;
import com.carlos.common.download.DownloadListner;
import com.carlos.common.download.DownloadManager;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;

public class AppUpgradeManager {
   public static AppUpgradeManager mAppUpgradeManager = new AppUpgradeManager();
   private boolean SYNC_STATUS = false;
   private String APPLICATION_SERVER_URL = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB49KQg2MmUjRCZoJwYePDs2J2EzICBsNSwsKQdfDmoFGgR6NCQwJz4MPGkKQTVqNFEAKT4uKm8zAiVgNR4qKAdbPW4gAithJzAcLC4iI2IkAiVsHlkrOQgmO2wgMD9vIwYhOwYYKmwgAjBsJwE5KBgIDg=="));

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
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+H2szGiZiNAY9P1oNIhkNEzNXCS0bQAA/BlcVSFo=")));
         }

         public void onProgress(float progress) {
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+H2szGiZiNAY9P1oNIhkNEzNXDTw7KS4AIWEwLDZlICIeWgk6Vg==")) + progress);
         }

         public void onPause() {
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+H2szGiZiNAY9P1o7GhtWBxEaUiEzRDZcPxkFSFo=")));
         }

         public void onCancel() {
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+H2szGiZiNAY9P1oJRAJWGz4GUiEzRDZcPxkFSFo=")));
         }
      });
      downloadManager.downloadSingle(this.APPLICATION_SERVER_URL);
      return true;
   }

   public interface SyncCallback {
      void finishedListener();
   }
}
