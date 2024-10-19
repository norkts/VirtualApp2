package com.carlos.common.download;

import android.os.Bundle;
import android.os.Build.VERSION;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R.layout;

public class DownloadActivity extends AppCompatActivity {
   private static final int PERMISSION_REQUEST_CODE = 1;
   TextView tv_file_name1;
   TextView tv_progress1;
   TextView tv_file_name2;
   TextView tv_progress2;
   Button btn_download1;
   Button btn_download2;
   Button btn_download_all;
   ProgressBar pb_progress1;
   ProgressBar pb_progress2;
   DownloadManager mDownloadManager;
   String wechatUrl = "http://dldir1.qq.com/weixin/android/weixin703android1400.apk";
   String qqUrl = "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk";
   Button btn_cancel2;
   Button btn_cancel1;

   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.setContentView(layout.download_demo);
      this.initDownloads();
   }

   private void initDownloads() {
      this.mDownloadManager = DownloadManager.getInstance();
      this.mDownloadManager.add(this, this.wechatUrl, new DownloadListner() {
         public void onFinished() {
            Toast.makeText(DownloadActivity.this, "下载完成!", 0).show();
         }

         public void onProgress(float progress) {
            DownloadActivity.this.pb_progress1.setProgress((int)(progress * 100.0F));
            DownloadActivity.this.tv_progress1.setText(String.format("%.2f", progress * 100.0F) + "%");
         }

         public void onPause() {
            Toast.makeText(DownloadActivity.this, "暂停了!", 0).show();
         }

         public void onCancel() {
            DownloadActivity.this.tv_progress1.setText("0%");
            DownloadActivity.this.pb_progress1.setProgress(0);
            DownloadActivity.this.btn_download1.setText("下载");
            Toast.makeText(DownloadActivity.this, "下载已取消!", 0).show();
         }
      });
      this.mDownloadManager.add(this, this.qqUrl, new DownloadListner() {
         public void onFinished() {
            Toast.makeText(DownloadActivity.this, "下载完成!", 0).show();
         }

         public void onProgress(float progress) {
            DownloadActivity.this.pb_progress2.setProgress((int)(progress * 100.0F));
            DownloadActivity.this.tv_progress2.setText(String.format("%.2f", progress * 100.0F) + "%");
         }

         public void onPause() {
            Toast.makeText(DownloadActivity.this, "暂停了!", 0).show();
         }

         public void onCancel() {
            DownloadActivity.this.tv_progress2.setText("0%");
            DownloadActivity.this.pb_progress2.setProgress(0);
            DownloadActivity.this.btn_download2.setText("下载");
            Toast.makeText(DownloadActivity.this, "下载已取消!", 0).show();
         }
      });
   }

   public void downloadOrPause(View view) {
      if (view == this.btn_download1) {
         if (!this.mDownloadManager.isDownloading(this.wechatUrl)) {
            this.mDownloadManager.download(this.wechatUrl);
            this.btn_download1.setText("暂停");
         } else {
            this.btn_download1.setText("下载");
            this.mDownloadManager.pause(this.wechatUrl);
         }
      } else if (view == this.btn_download2) {
         if (!this.mDownloadManager.isDownloading(this.qqUrl)) {
            this.mDownloadManager.download(this.qqUrl);
            this.btn_download2.setText("暂停");
         } else {
            this.btn_download2.setText("下载");
            this.mDownloadManager.pause(this.qqUrl);
         }
      }

   }

   public void downloadOrPauseAll(View view) {
      if (!this.mDownloadManager.isDownloading(this.wechatUrl, this.qqUrl)) {
         this.btn_download1.setText("暂停");
         this.btn_download2.setText("暂停");
         this.btn_download_all.setText("全部暂停");
         this.mDownloadManager.download(this.wechatUrl, this.qqUrl);
      } else {
         this.mDownloadManager.pause(this.wechatUrl, this.qqUrl);
         this.btn_download1.setText("下载");
         this.btn_download2.setText("下载");
         this.btn_download_all.setText("全部下载");
      }

   }

   public void cancel(View view) {
      if (view == this.btn_cancel1) {
         this.mDownloadManager.cancel(this.wechatUrl);
      } else if (view == this.btn_cancel2) {
         this.mDownloadManager.cancel(this.qqUrl);
      }

   }

   public void cancelAll(View view) {
      this.mDownloadManager.cancel(this.wechatUrl, this.qqUrl);
      this.btn_download1.setText("下载");
      this.btn_download2.setText("下载");
      this.btn_download_all.setText("全部下载");
   }

   protected void onStart() {
      super.onStart();
      if (VERSION.SDK_INT >= 23) {
         String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
         if (!this.checkPermission(permission)) {
            if (this.shouldShowRationale(permission)) {
               this.showMessage("需要权限跑demo哦...");
            }

            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
         }

      }
   }

   protected void onDestroy() {
      super.onDestroy();
      this.cancelAll((View)null);
   }

   private void showMessage(String msg) {
      Toast.makeText(this, msg, 0).show();
   }

   protected boolean checkPermission(String permission) {
      return ContextCompat.checkSelfPermission(this, permission) == 0;
   }

   protected boolean shouldShowRationale(String permission) {
      return ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
   }
}
