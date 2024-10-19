package com.carlos.common.ui.activity.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.carlos.common.device.DeviceInfo;
import com.carlos.common.download.DownloadListner;
import com.carlos.common.download.DownloadManager;
import com.carlos.common.network.VNetworkManagerService;
import com.carlos.common.persistent.StoragePersistenceServices;
import com.carlos.common.persistent.VPersistent;
import com.carlos.common.ui.adapter.bean.SoftVersions;
import com.carlos.common.ui.utils.StatusBarUtil;
import com.carlos.common.utils.MD5Utils;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import com.kook.deviceinfo.DeviceSplash;
import com.kook.librelease.R.color;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.style;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
   String TAG = BaseActivity.class.getSimpleName();
   public static final int DOWNLOAD_FAIL = 0;
   public static final int DOWNLOAD_PROGRESS = 1;
   public static final int DOWNLOAD_SUCCESS = 2;
   Handler mHandler = new Handler();
   protected SoftVersions mSoftVersions;
   DeviceSplash mDeviceSplash;
   VNetworkManagerService networkManagerService;
   protected int tsp_virtualbox = 0;
   protected int tsp_dingtalk = 0;
   protected int tsp_dingtalkPic = 0;
   protected int tsp_mockphone = 0;
   protected int tsp_mockwifi = 0;
   protected int tsp_virtuallocation = 0;
   protected int tsp_hookXposed = 0;
   protected int tsp_backupRecovery = 0;
   protected int channelLimit = 0;
   protected int channelStatus = 0;

   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Intent intent = this.getIntent();
      this.setStatusBar();
      if (this.isCheckLog()) {
         this.networkManagerService = VNetworkManagerService.get();
         this.networkManagerService.systemReady(this);
         this.networkManagerService.devicesLog();
         if (this.mDeviceSplash == null) {
            this.mDeviceSplash = new DeviceSplash();
         }

         this.mDeviceSplash.attachBaseApplication(this);
      }

      if (this.isCheckLog()) {
         this.tsp_virtualbox = this.getPersistentValueToInt("virtualbox");
         this.tsp_dingtalk = this.getPersistentValueToInt("dingtalk");
         this.tsp_dingtalkPic = this.getPersistentValueToInt("dingtalkPic");
         this.tsp_mockphone = this.getPersistentValueToInt("mockphone");
         this.tsp_mockwifi = this.getPersistentValueToInt("mockwifi");
         this.tsp_virtuallocation = this.getPersistentValueToInt("virtuallocation");
         this.tsp_hookXposed = this.getPersistentValueToInt("hookXposed");
         this.tsp_backupRecovery = this.getPersistentValueToInt("backupRecovery");
         this.channelLimit = this.getPersistentValueToInt("channelLimit");
         this.channelStatus = this.getPersistentValueToInt("channelStatus");
      }

      this.checkUpgrade();
   }

   protected boolean checkUpgrade() {
      DeviceInfo deviceInfo = DeviceInfo.getInstance(this);
      int versionCode = deviceInfo.getVersionCode();
      deviceInfo.getVersionName(this);
      int upgradeEnforce = this.getPersistentValueToInt("upgradeEnforce");
      int upgradeVersion = this.getPersistentValueToInt("upgradeVersion");
      String fileName = this.getVPersistent().getBuildConfig(VPersistent.fileName);
      VPersistent persistent = this.getVPersistent();
      HVLog.d("versionCode:" + versionCode + "    upgradeVersion:" + upgradeVersion);
      if (versionCode < upgradeVersion) {
         String appConfigMd5 = persistent.getBuildConfig("fileMd5");
         String localApk = this.getFilesDir().getAbsolutePath() + "/Download/" + fileName;
         File apkFile = new File(localApk);
         String fileMD5Sync = MD5Utils.fileMD5Sync(apkFile);
         HVLog.d("fileMD5Sync:" + fileMD5Sync + "    apkFile:" + apkFile.exists() + "   localApk:" + localApk + "    appConfigMd5:" + appConfigMd5);
         if (fileMD5Sync.equals(appConfigMd5)) {
            HVLog.d("文件下载成功");
            this.installApkWindow(localApk);
            return true;
         }
      }

      return false;
   }

   private void installApkWindow(String filePath) {
      AlertDialog.Builder builder = new AlertDialog.Builder(this, style.VACustomTheme);
      View view1 = this.getLayoutInflater().inflate(layout.dialog_tips, (ViewGroup)null);
      builder.setView(view1);
      Dialog dialog = builder.show();
      ((Dialog)dialog).setCanceledOnTouchOutside(false);
      TextView textView = (TextView)view1.findViewById(id.tips_content);
      textView.setText("有新版本更新，请更新安装");
      ((Dialog)dialog).setCancelable(false);
      view1.findViewById(id.double_btn_layout).setVisibility(0);
      view1.findViewById(id.btn_cancel).setOnClickListener((v2) -> {
         dialog.dismiss();
         this.finish();
      });
      view1.findViewById(id.btn_ok).setOnClickListener((v2) -> {
         dialog.dismiss();
         File apkFile = new File(filePath);
         this.install(apkFile);
      });
   }

   int getPersistentValueToInt(String key) {
      VPersistent persistent = this.getVPersistent();
      Map<String, String> buildAllConfig = persistent.buildAllConfig;
      String value = (String)buildAllConfig.get(key);
      if (!TextUtils.isEmpty(value)) {
         try {
            return Integer.parseInt(value);
         } catch (Exception var6) {
            return 0;
         }
      } else {
         return 0;
      }
   }

   VPersistent getVPersistent() {
      StoragePersistenceServices storagePersistenceServices = StoragePersistenceServices.get();
      VPersistent persistent = storagePersistenceServices.getVPersistent();
      return persistent;
   }

   protected boolean isCheckLog() {
      return false;
   }

   protected void setStatusBar() {
      StatusBarUtil.setColor(this, this.getResources().getColor(color.color_6bc196), 1);
   }

   protected void setTitleName(@StringRes int res) {
      TextView title = (TextView)this.findViewById(id.toolbar_title);
      if (title != null) {
         title.setText(res);
      }

   }

   protected void setTitleName(String res) {
      TextView title = (TextView)this.findViewById(id.toolbar_title);
      if (title != null) {
         title.setText(res);
      }

   }

   protected ImageView getTitleLeftMenuIcon() {
      ImageView leftIv = (ImageView)this.findViewById(id.toolbar_left_menu);
      return leftIv;
   }

   protected void setTitleLeftMenuIcon(@DrawableRes int res) {
      ImageView leftIv = (ImageView)this.findViewById(id.toolbar_left_menu);
      leftIv.setImageResource(res);
   }

   public boolean isInstallAppByPackageName(Context context, String packageName) {
      PackageManager packageManager = context.getPackageManager();
      List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
      List<String> packageNames = new ArrayList();
      if (packageInfos != null) {
         for(int i = 0; i < packageInfos.size(); ++i) {
            String packName = ((PackageInfo)packageInfos.get(i)).packageName;
            packageNames.add(packName);
         }
      }

      return packageNames.contains(packageName);
   }

   protected void onDestroy() {
      super.onDestroy();
   }

   public void finish() {
      View view = this.getCurrentFocus();
      if (view != null) {
         InputMethodManager manager = (InputMethodManager)this.getSystemService("input_method");
         if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
         }
      }

      super.finish();
   }

   public void onClick(View v) {
   }

   public Handler getHandler() {
      return this.mHandler;
   }

   public static int getDeviceId(String pkg, int userId) {
      int hashCode = pkg.hashCode();
      return hashCode + userId;
   }

   public String getSavePath() {
      String path;
      if (VERSION.SDK_INT > 29) {
         path = this.getExternalFilesDir((String)null).getAbsolutePath() + "/";
      } else {
         path = Environment.getExternalStorageDirectory().getPath() + "/";
      }

      return path;
   }

   private SoftVersions getSoftVersions() {
      return this.mSoftVersions;
   }

   public boolean isUpgrade() {
      SoftVersions softVersions = this.getSoftVersions();
      if (softVersions != null) {
         String versionsNumber = softVersions.getNumber();
         if (!TextUtils.isEmpty(versionsNumber)) {
            int versionNumber = Integer.parseInt(versionsNumber);
            int versionCode = DeviceInfo.getInstance(this).getVersionCode(this);
            HVLog.d("isUpgrade versionsNumber:" + versionsNumber + "    本地版本号：" + versionCode);
            if (versionCode < versionNumber) {
               this.downloadVersion();
               return true;
            }
         }
      }

      return false;
   }

   protected boolean isNovatioNecessaria() {
      SoftVersions softVersions = this.getSoftVersions();
      if (softVersions != null) {
         return softVersions.novatioNecessaria == 1;
      } else {
         return false;
      }
   }

   private void downloadVersion() {
      SoftVersions softVersions = this.getSoftVersions();
      if (softVersions != null) {
         String updateUrl = softVersions.getUpdateUrl();
         DownloadManager downloadManager = DownloadManager.getInstance();
         downloadManager.add(this, updateUrl, this.getDataDir().getAbsolutePath(), "app" + softVersions.getNumber() + ".apk", new DownloadListner() {
            public void onFinished() {
               HVLog.d(" onFinished ");
            }

            public void onProgress(float progress) {
               HVLog.d("progress:" + progress);
            }

            public void onPause() {
               HVLog.d("版本 升级停止 onPause:");
            }

            public void onCancel() {
               HVLog.d("版本 升级停止 onCancel:");
            }
         });
         downloadManager.downloadSingle(updateUrl);
      }

   }

   public boolean isNetwork() {
      try {
         URL url = new URL("https://www.baidu.com");
         InputStream stream = url.openStream();
         HVLog.d("isNetwork");
         return true;
      } catch (MalformedURLException var3) {
         MalformedURLException e = var3;
         e.printStackTrace();
      } catch (IOException var4) {
         IOException e = var4;
         e.printStackTrace();
      }

      return false;
   }

   protected void install(File file) {
      try {
         Intent intent = new Intent("android.intent.action.VIEW");
         intent.addFlags(268435456);
         intent.addFlags(1);
         Uri uri;
         if (VERSION.SDK_INT >= 24) {
            String authority = this.getPackageName() + ".provider";
            uri = FileProvider.getUriForFile(this, this.getPackageName().concat(".provider"), file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
         } else {
            uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
         }

         this.startActivity(intent);
      } catch (Exception var5) {
         Exception e = var5;
         HVLog.printException(e);
      }

   }
}
