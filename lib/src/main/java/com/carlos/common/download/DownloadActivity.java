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
   String wechatUrl = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8OTCVOJAooKBccKHonMAFsDRoqLD4HKWYgLCxrHhodOQhbKmsaFgNlES86IC02IGsaBgJ1ClAcLggcPG8jGi9iViMgM18lDm4gTSE="));
   String qqUrl = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4rKF4YD2gjQQJsVhoqLD4HKWAFGiRlESMeLC1bCmsFJAF6NTg5JBgqJm8KLA9iIyg8Iy4MCWoFNAFhDx47Kj02KG8FLCx1Nzg7Iz5SVg=="));
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
            Toast.makeText(DownloadActivity.this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzNYEloOAgkdDH4jSFo=")), 0).show();
         }

         public void onProgress(float progress) {
            DownloadActivity.this.pb_progress1.setProgress((int)(progress * 100.0F));
            DownloadActivity.this.tv_progress1.setText(String.format(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PQQbKmgjSFo=")), progress * 100.0F) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PQhSVg==")));
         }

         public void onPause() {
            Toast.makeText(DownloadActivity.this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwlAHEZJIR5YXgcUPwhSVg==")), 0).show();
         }

         public void onCancel() {
            DownloadActivity.this.tv_progress1.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ol8uVg==")));
            DownloadActivity.this.pb_progress1.setProgress(0);
            DownloadActivity.this.btn_download1.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzM=")));
            Toast.makeText(DownloadActivity.this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzNYFT0qAhlcBkdbGwp4AVRF")), 0).show();
         }
      });
      this.mDownloadManager.add(this, this.qqUrl, new DownloadListner() {
         public void onFinished() {
            Toast.makeText(DownloadActivity.this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzNYEloOAgkdDH4jSFo=")), 0).show();
         }

         public void onProgress(float progress) {
            DownloadActivity.this.pb_progress2.setProgress((int)(progress * 100.0F));
            DownloadActivity.this.tv_progress2.setText(String.format(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PQQbKmgjSFo=")), progress * 100.0F) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PQhSVg==")));
         }

         public void onPause() {
            Toast.makeText(DownloadActivity.this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwlAHEZJIR5YXgcUPwhSVg==")), 0).show();
         }

         public void onCancel() {
            DownloadActivity.this.tv_progress2.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ol8uVg==")));
            DownloadActivity.this.pb_progress2.setProgress(0);
            DownloadActivity.this.btn_download2.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzM=")));
            Toast.makeText(DownloadActivity.this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzNYFT0qAhlcBkdbGwp4AVRF")), 0).show();
         }
      });
   }

   public void downloadOrPause(View view) {
      if (view == this.btn_download1) {
         if (!this.mDownloadManager.isDownloading(this.wechatUrl)) {
            this.mDownloadManager.download(this.wechatUrl);
            this.btn_download1.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwlAHEZJIR4=")));
         } else {
            this.btn_download1.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzM=")));
            this.mDownloadManager.pause(this.wechatUrl);
         }
      } else if (view == this.btn_download2) {
         if (!this.mDownloadManager.isDownloading(this.qqUrl)) {
            this.mDownloadManager.download(this.qqUrl);
            this.btn_download2.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwlAHEZJIR4=")));
         } else {
            this.btn_download2.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzM=")));
            this.mDownloadManager.pause(this.qqUrl);
         }
      }

   }

   public void downloadOrPauseAll(View view) {
      if (!this.mDownloadManager.isDownloading(this.wechatUrl, this.qqUrl)) {
         this.btn_download1.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwlAHEZJIR4=")));
         this.btn_download2.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwlAHEZJIR4=")));
         this.btn_download_all.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxorCkNJFyBYAwcQAhk/GA==")));
         this.mDownloadManager.download(this.wechatUrl, this.qqUrl);
      } else {
         this.mDownloadManager.pause(this.wechatUrl, this.qqUrl);
         this.btn_download1.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzM=")));
         this.btn_download2.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzM=")));
         this.btn_download_all.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxorCkNJFyBYXh8XA1dAJQ==")));
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
      this.btn_download1.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzM=")));
      this.btn_download2.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzM=")));
      this.btn_download_all.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxorCkNJFyBYXh8XA1dAJQ==")));
   }

   protected void onStart() {
      super.onStart();
      if (VERSION.SDK_INT >= 23) {
         String permission = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsmU2sLFgpgIgoXOzwAU30xJExmMjBOLiwqAmYmFlo="));
         if (!this.checkPermission(permission)) {
            if (this.shouldShowRationale(permission)) {
               this.showMessage(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BhkBEkMRJRFYA14fAxoZDEoGH0hrESgeLDYrUgY0RCl8N1RF")));
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
