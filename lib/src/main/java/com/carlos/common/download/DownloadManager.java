package com.carlos.common.download;

import android.content.Context;
import android.text.TextUtils;
import com.carlos.libcommon.StringFog;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DownloadManager {
   private String DEFAULT_FILE_DIR;
   private Map<String, DownloadTask> mDownloadTasks = new HashMap();
   private static DownloadManager mInstance;
   private static final String TAG = "DownloadManager";

   public void download(String... urls) {
      int i = 0;

      for(int length = urls.length; i < length; ++i) {
         String url = urls[i];
         if (this.mDownloadTasks.containsKey(url)) {
            ((DownloadTask)this.mDownloadTasks.get(url)).start();
         }
      }

   }

   public void downloadSingle(String... urls) {
      int i = 0;

      for(int length = urls.length; i < length; ++i) {
         String url = urls[i];
         if (this.mDownloadTasks.containsKey(url)) {
            ((DownloadTask)this.mDownloadTasks.get(url)).startSingle();
         }
      }

   }

   public String getFileName(String url) {
      return url.substring(url.lastIndexOf("/") + 1);
   }

   public void pause(String... urls) {
      int i = 0;

      for(int length = urls.length; i < length; ++i) {
         String url = urls[i];
         if (this.mDownloadTasks.containsKey(url)) {
            ((DownloadTask)this.mDownloadTasks.get(url)).pause();
         }
      }

   }

   public void cancel(String... urls) {
      int i = 0;

      for(int length = urls.length; i < length; ++i) {
         String url = urls[i];
         if (this.mDownloadTasks.containsKey(url)) {
            ((DownloadTask)this.mDownloadTasks.get(url)).cancel();
         }
      }

   }

   public void add(Context context, String url, DownloadListner l) {
      this.add(context, url, (String)null, (String)null, l);
   }

   public void add(Context context, String url, String filePath, DownloadListner l) {
      this.add(context, url, filePath, (String)null, l);
   }

   public void add(Context context, String url, String filePath, String fileName, DownloadListner l) {
      if (TextUtils.isEmpty(filePath)) {
         filePath = this.getDefaultDirectory(context);
      }

      if (TextUtils.isEmpty(fileName)) {
         fileName = this.getFileName(url);
      }

      this.mDownloadTasks.put(url, new DownloadTask(new FilePoint(url, filePath, fileName), l));
   }

   private String getDefaultDirectory(Context context) {
      if (TextUtils.isEmpty(this.DEFAULT_FILE_DIR)) {
         File dataDir = context.getDataDir();
         this.DEFAULT_FILE_DIR = dataDir.getAbsolutePath();
      }

      return this.DEFAULT_FILE_DIR;
   }

   public static DownloadManager getInstance() {
      if (mInstance == null) {
         Class var0 = DownloadManager.class;
         synchronized(DownloadManager.class) {
            if (mInstance == null) {
               mInstance = new DownloadManager();
            }
         }
      }

      return mInstance;
   }

   public boolean isDownloading(String... urls) {
      boolean result = false;
      int i = 0;

      for(int length = urls.length; i < length; ++i) {
         String url = urls[i];
         if (this.mDownloadTasks.containsKey(url)) {
            result = ((DownloadTask)this.mDownloadTasks.get(url)).isDownloading();
         }
      }

      return result;
   }
}
