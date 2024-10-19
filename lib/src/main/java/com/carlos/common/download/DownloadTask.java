package com.carlos.common.download;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

public class DownloadTask extends Handler {
   private final int THREAD_COUNT = 1;
   private FilePoint mPoint;
   private long mFileLength;
   private volatile boolean isDownloading = false;
   private AtomicInteger childCanleCount = new AtomicInteger(0);
   private AtomicInteger childPauseCount = new AtomicInteger(0);
   private AtomicInteger childFinshCount = new AtomicInteger(0);
   private HttpUtil mHttpUtil;
   private long[] mProgress;
   private File[] mCacheFiles;
   private File mTmpFile;
   private volatile boolean pause;
   private volatile boolean cancel;
   private static final int MSG_PROGRESS = 1;
   private static final int MSG_FINISH = 2;
   private static final int MSG_PAUSE = 3;
   private static final int MSG_CANCEL = 4;
   private DownloadListner mListner;
   private static final String TAG = "DownloadTask";

   DownloadTask(FilePoint point, DownloadListner l) {
      this.mPoint = point;
      this.mListner = l;
      this.mProgress = new long[1];
      this.mCacheFiles = new File[1];
      this.mHttpUtil = HttpUtil.getInstance();
   }

   public void handleMessage(Message msg) {
      if (null != this.mListner) {
         switch (msg.what) {
            case 1:
               long progress = 0L;
               int i = 0;

               for(int length = this.mProgress.length; i < length; ++i) {
                  progress += this.mProgress[i];
               }

               this.mListner.onProgress((float)progress * 1.0F / (float)this.mFileLength);
               break;
            case 2:
               if (this.confirmStatus(this.childFinshCount)) {
                  return;
               }

               this.mTmpFile.renameTo(new File(this.mPoint.getFilePath(), this.mPoint.getFileName()));
               this.resetStutus();
               this.mListner.onFinished();
               break;
            case 3:
               if (this.confirmStatus(this.childPauseCount)) {
                  return;
               }

               this.resetStutus();
               this.mListner.onPause();
               break;
            case 4:
               if (this.confirmStatus(this.childCanleCount)) {
                  return;
               }

               this.resetStutus();
               this.mProgress = new long[1];
               this.mListner.onCancel();
         }

      }
   }

   public synchronized void startSingle() {
      if (!this.isDownloading) {
         this.isDownloading = true;

         try {
            this.mHttpUtil.getContentLength(this.mPoint.getUrl(), new okhttp3.Callback () {
               public void onFailure(Call call, IOException e) {
                  DownloadTask.this.mListner.onCancel();
               }

               public void onResponse(Call call, Response response) throws IOException {
                  if (response.isSuccessful()) {
                     InputStream is = null;
                     FileOutputStream fos = null;
                     is = response.body().byteStream();
                     File file = new File(DownloadTask.this.mPoint.getFilePath(), DownloadTask.this.mPoint.getFileName());

                     try {
                        fos = new FileOutputStream(file);
                        byte[] bytes = new byte[1024];
                        long fileSize = response.body().contentLength();
                        HVLog.d("fileSize:" + fileSize);
                        long sum = 0L;

                        int len;
                        while((len = is.read(bytes)) != -1) {
                           fos.write(bytes, 0, len);
                           sum += (long)len;
                           int porSizex = (int)((float)sum * 1.0F / (float)fileSize * 100.0F);
                           DownloadTask.this.mListner.onProgress((float)porSizex);
                        }
                     } catch (Exception var21) {
                        Exception ex = var21;
                        ex.printStackTrace();
                        HVLog.printException(ex);
                        DownloadTask.this.mListner.onCancel();
                     } finally {
                        try {
                           if (is != null) {
                              is.close();
                           }

                           if (fos != null) {
                              fos.close();
                           }

                           DownloadTask.this.mListner.onFinished();
                        } catch (IOException var20) {
                           IOException e = var20;
                           HVLog.printException(e);
                        }

                     }

                     HVLog.i("myTag", "下载成功");
                  } else {
                     DownloadTask.this.mListner.onCancel();
                  }

               }
            });
         } catch (IOException var2) {
            IOException e = var2;
            e.printStackTrace();
         }

      }
   }

   public synchronized void start() {
      try {
         Log.e(TAG, "start: " + this.isDownloading + "\t" + this.mPoint.getUrl());
         if (this.isDownloading) {
            return;
         }

         this.isDownloading = true;
         this.mHttpUtil.getContentLength(this.mPoint.getUrl(), new okhttp3.Callback () {
            public void onResponse(Call call, Response response) throws IOException {
               HVLog.e("DownloadTask", "start: " + response.code() + "\t isDownloading:" + DownloadTask.this.isDownloading + "\t" + DownloadTask.this.mPoint.getUrl());
               if (response.code() != 200) {
                  DownloadTask.this.close(response.body());
                  DownloadTask.this.resetStutus();
               } else {
                  DownloadTask.this.mFileLength = response.body().contentLength();
                  Headers headers = response.headers();
                  DownloadTask.this.close(response.body());
                  HVLog.e("DownloadTask", "mPoint: " + DownloadTask.this.mPoint.getFilePath() + "    " + DownloadTask.this.mPoint.getFileName());
                  DownloadTask.this.mTmpFile = new File(DownloadTask.this.mPoint.getFilePath(), DownloadTask.this.mPoint.getFileName() + ".tmp");
                  if (!DownloadTask.this.mTmpFile.getParentFile().exists()) {
                     HVLog.e("DownloadTask", "===: ");
                     boolean mkdirs = DownloadTask.this.mTmpFile.getParentFile().mkdirs();
                     HVLog.e("DownloadTask", "mkdirs: " + mkdirs);
                  }

                  try {
                     RandomAccessFile tmpAccessFile = new RandomAccessFile(DownloadTask.this.mTmpFile, "rw");
                     HVLog.e("DownloadTask", "mFileLength: " + DownloadTask.this.mFileLength);
                     tmpAccessFile.setLength(DownloadTask.this.mFileLength);
                  } catch (FileNotFoundException var11) {
                     HVLog.printException(var11);
                  }

                  long blockSize = DownloadTask.this.mFileLength / 1L;

                  for(int threadId = 0; threadId < 1; ++threadId) {
                     long startIndex = (long)threadId * blockSize;
                     long endIndex = (long)(threadId + 1) * blockSize - 1L;
                     if (threadId == 0) {
                        endIndex = DownloadTask.this.mFileLength - 1L;
                     }

                     DownloadTask.this.download(startIndex, endIndex, threadId);
                  }

               }
            }

            public void onFailure(Call call, IOException e) {
               HVLog.e("DownloadTask", "start:Exception " + e.getMessage() + "\n" + DownloadTask.this.mPoint.getUrl());
               DownloadTask.this.resetStutus();
            }
         });
      } catch (IOException var2) {
         IOException e = var2;
         e.printStackTrace();
         HVLog.printException(e);
         this.resetStutus();
      }

   }

   private void download(final long startIndex, long endIndex, final int threadId) throws IOException {
      long newStartIndex = startIndex;
      final File cacheFile = new File(this.mPoint.getFilePath(), "thread" + threadId + "_" + this.mPoint.getFileName() + ".cache");
      this.mCacheFiles[threadId] = cacheFile;
      final RandomAccessFile cacheAccessFile = new RandomAccessFile(cacheFile, "rwd");
      if (cacheFile.exists()) {
         String startIndexStr = cacheAccessFile.readLine();

         try {
            newStartIndex = (long)Integer.parseInt(startIndexStr);
         } catch (NumberFormatException var12) {
            NumberFormatException e = var12;
            HVLog.printException(e);
         }
      }

      final long finalStartIndex = newStartIndex;
      this.mHttpUtil.downloadFileByRange(this.mPoint.getUrl(), finalStartIndex, endIndex, new okhttp3.Callback () {
         public void onResponse(Call call, Response response) throws IOException {
            Log.e("DownloadTask", "download: " + response.code() + "\t isDownloading:" + DownloadTask.this.isDownloading + "\t" + DownloadTask.this.mPoint.getUrl());
            if (response.code() != 206) {
               DownloadTask.this.resetStutus();
            } else {
               HVLog.d("状态开始 ");
               InputStream is = response.body().byteStream();
               RandomAccessFile tmpAccessFile = new RandomAccessFile(DownloadTask.this.mTmpFile, "rw");
               tmpAccessFile.seek(finalStartIndex);
               byte[] buffer = new byte[4096];
               int total = 0;
               long progress = 0L;

               int lengthx;
               while((lengthx = is.read(buffer)) > 0) {
                  HVLog.d("   ===----===  ");
                  if (DownloadTask.this.cancel) {
                     DownloadTask.this.close(cacheAccessFile, is, response.body());
                     DownloadTask.this.cleanFile(cacheFile);
                     DownloadTask.this.sendEmptyMessage(4);
                     return;
                  }

                  if (DownloadTask.this.pause) {
                     DownloadTask.this.close(cacheAccessFile, is, response.body());
                     DownloadTask.this.sendEmptyMessage(3);
                     return;
                  }

                  tmpAccessFile.write(buffer, 0, lengthx);
                  total += lengthx;
                  progress = finalStartIndex + (long)total;
                  cacheAccessFile.seek(0L);
                  cacheAccessFile.write((progress + "").getBytes("UTF-8"));
                  DownloadTask.this.mProgress[threadId] = progress - startIndex;
                  DownloadTask.this.sendEmptyMessage(1);
               }

               DownloadTask.this.close(cacheAccessFile, is, response.body());
               DownloadTask.this.cleanFile(cacheFile);
               DownloadTask.this.sendEmptyMessage(2);
            }
         }

         public void onFailure(Call call, IOException e) {
            DownloadTask.this.isDownloading = false;
            HVLog.printException(e);
         }
      });
   }

   private void close(Closeable... closeables) {
      int length = closeables.length;
      boolean var9 = false;

      int i;
      label131: {
         try {
            var9 = true;

            for(i = 0; i < length; ++i) {
               Closeable closeable = closeables[i];
               if (null != closeable) {
                  closeables[i].close();
               }
            }

            var9 = false;
            break label131;
         } catch (IOException var10) {
            IOException e = var10;
            e.printStackTrace();
            HVLog.printException(e);
            var9 = false;
         } finally {
            if (var9) {
               for(i = 0; i < length; ++i) {
                  closeables[i] = null;
               }

            }
         }

         for(i = 0; i < length; ++i) {
            closeables[i] = null;
         }

         return;
      }

      for(i = 0; i < length; ++i) {
         closeables[i] = null;
      }

   }

   private void cleanFile(File... files) {
      int i = 0;

      for(int length = files.length; i < length; ++i) {
         if (null != files[i]) {
            files[i].delete();
         }
      }

   }

   public void pause() {
      this.pause = true;
   }

   public void cancel() {
      this.cancel = true;
      this.cleanFile(this.mTmpFile);
      if (!this.isDownloading && null != this.mListner) {
         this.cleanFile(this.mCacheFiles);
         this.resetStutus();
         this.mListner.onCancel();
      }

   }

   private void resetStutus() {
      this.pause = false;
      this.cancel = false;
      this.isDownloading = false;
   }

   private boolean confirmStatus(AtomicInteger count) {
      return count.incrementAndGet() % 1 != 0;
   }

   public boolean isDownloading() {
      return this.isDownloading;
   }
}
