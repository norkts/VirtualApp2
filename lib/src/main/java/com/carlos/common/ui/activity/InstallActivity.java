package com.carlos.common.ui.activity;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.carlos.common.utils.xapk.XAPKUtils;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R.layout;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InstallActivity extends AppCompatActivity {
   private static final String TAG = InstallActivity.class.getSimpleName();
   private static final String PACKAGE_INSTALLED_ACTION = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xBg59HAIOJDxbH2IhJFZgHAYWLjwuWGEIQV9hJRpF"));
   public static final String KEY_XAPK_PATH = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KBg+KGU2GgJ9AQo0"));
   public static final String KEY_APK_PATHS = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6MWYwIDdmHhpF"));
   private String xapkPath;
   private List<String> apkPaths;
   private ExecutorService mExecutorService;
   private PackageInstaller.Session mSession;

   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.setContentView(layout.activity_xapk_install);
      this.xapkPath = this.getIntent().getStringExtra(KEY_XAPK_PATH);
      this.apkPaths = this.getIntent().getStringArrayListExtra(KEY_APK_PATHS);
      this.installXapk();
   }

   private void installXapk() {
      if (VERSION.SDK_INT < 21) {
         Toast.makeText(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwlAHEYyOT5YXh8NAgoNDUdJJRFBFR9PBRwNGU5XAyofLD0KWDYBX1pWMVFVDzg5JBgqJm8KLzZ1Iy8dBxodX0YWQitYXh8IAj8ZX0dNJSQ=")), 0).show();
         this.finish();
      }

      this.mExecutorService = Executors.newSingleThreadExecutor();
      this.mExecutorService.execute(new Runnable() {
         public void run() {
            try {
               InstallActivity.this.mSession = InstallActivity.this.initSession();
               Iterator var4 = InstallActivity.this.apkPaths.iterator();

               while(var4.hasNext()) {
                  String apkPath = (String)var4.next();
                  InstallActivity.this.addApkToInstallSession(apkPath, InstallActivity.this.mSession);
               }

               InstallActivity.this.commitSession(InstallActivity.this.mSession);
            } catch (IOException var3) {
               IOException e = var3;
               e.printStackTrace();
               InstallActivity.this.abandonSession();
            }

         }
      });
   }

   @TargetApi(21)
   private PackageInstaller.Session initSession() throws IOException {
      PackageInstaller.Session session = null;
      PackageInstaller packageInstaller = this.getPackageManager().getPackageInstaller();
      PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(1);
      int sessionId = packageInstaller.createSession(params);
      session = packageInstaller.openSession(sessionId);
      return session;
   }

   @TargetApi(21)
   private void addApkToInstallSession(String filePath, PackageInstaller.Session session) throws IOException {
      OutputStream packageInSession = session.openWrite(XAPKUtils.getFileName(filePath), 0L, (new File(filePath)).length());

      try {
         InputStream is = new BufferedInputStream(new FileInputStream(filePath));

         try {
            byte[] buffer = new byte[16384];

            int n;
            while((n = ((InputStream)is).read(buffer)) >= 0) {
               packageInSession.write(buffer, 0, n);
            }
         } catch (Throwable var9) {
            try {
               ((InputStream)is).close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         ((InputStream)is).close();
      } catch (Throwable var10) {
         if (packageInSession != null) {
            try {
               packageInSession.close();
            } catch (Throwable var7) {
               var10.addSuppressed(var7);
            }
         }

         throw var10;
      }

      if (packageInSession != null) {
         packageInSession.close();
      }

   }

   @TargetApi(21)
   private void commitSession(PackageInstaller.Session session) {
      Intent intent = new Intent(this, InstallActivity.class);
      intent.setAction(PACKAGE_INSTALLED_ACTION);
      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
      IntentSender statusReceiver = pendingIntent.getIntentSender();
      session.commit(statusReceiver);
   }

   @TargetApi(21)
   private void abandonSession() {
      if (this.mSession != null) {
         this.mSession.abandon();
         this.mSession.close();
      }

   }

   @TargetApi(21)
   protected void onNewIntent(Intent intent) {
      super.onNewIntent(intent);
      Bundle extras = intent.getExtras();
      if (PACKAGE_INSTALLED_ACTION.equals(intent.getAction())) {
         int status = -100;
         String message = "";
         if (extras != null) {
            status = extras.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NDwePC4uPGYVMCR8NSwTICscXGQjSFo=")));
            message = extras.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NDwePC4uPGYVMCR8NSwTICscXGQmGkhgHDBBIwU+Bg==")));
         }

         switch (status) {
            case -1:
               Intent confirmIntent = (Intent)extras.get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZrDlk/KS49KmsIRVRmDB4T")));
               this.startActivity(confirmIntent);
               break;
            case 0:
               Toast.makeText(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxwZXEMRFxVYAB8CAhkFHX4jSFo=")), 0).show();
               this.finish();
               break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
               Toast.makeText(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxwZXEMRFxVYEg8rA1cNPX87EyVVLxNJQFtcKVQFSFo=")), 0).show();
               this.finish();
               Log.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcKWwFJCRgVyQ+LwccCGkjATd4EVRF")) + status + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186Vg==")) + message);
               break;
            default:
               Toast.makeText(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxwZXEMRFxVYEg8rA1cNPX87Ey0dLy1ARDY/Xx8oEz4fPA9XEwAJH1cNMSFVLyEvRDUNIx4SIVMCUgcSXCYrHQY7BzQdUx81WSUZWBgrEyUYAVRF")), 0).show();
               this.finish();
         }
      }

   }

   protected void onDestroy() {
      super.onDestroy();
      if (this.mExecutorService != null && !this.mExecutorService.isShutdown()) {
         this.mExecutorService.shutdown();
      }

      this.abandonSession();
   }
}
