package com.carlos.common.download;

public interface DownloadListner {
   void onFinished();

   void onProgress(float var1);

   void onPause();

   void onCancel();
}
