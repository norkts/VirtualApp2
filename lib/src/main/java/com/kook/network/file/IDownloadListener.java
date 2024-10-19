package com.kook.network.file;

public interface IDownloadListener {
   void onDownloadSuccess();

   void onDownloadFail(Exception var1);

   void onProgress(int var1);
}
