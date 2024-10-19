package com.carlos.common.ui.activity.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Build.VERSION;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.carlos.common.clouddisk.listview.FileItem;
import com.carlos.common.utils.InstallTools;
import com.carlos.common.utils.SPTools;
import com.carlos.common.widget.BottomSheetLayout;
import com.carlos.common.widget.toast.Toasty;
import com.carlos.libcommon.StringFog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kook.common.utils.HVLog;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.string;
import com.kook.librelease.R.style;
import com.lody.virtual.client.core.AppCallback;
import com.lody.virtual.client.core.AppLauncherCallback;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.client.stub.RequestExternalStorageManagerActivity;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.compat.PermissionCompat;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.oem.OemPermissionHelper;
import com.lody.virtual.remote.InstalledAppInfo;
import com.lody.virtual.server.extension.VExtPackageAccessor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class VerifyActivity extends VActivity implements AppLauncherCallback {
   private static String META_DATA_KEY = "ScorpionSDK";
   BottomSheetDialog bottomSheetDialog;
   BottomSheetLayout bottomSheetLayout;
   private static final String PKG_NAME_ARGUMENT = "MODEL_ARGUMENT";
   private static final String KEY_PKGNAME = "KEY_PKGNAME";
   private static final String APP_NAME = "APP_NAME";
   private static final String KEY_USER = "KEY_USER";
   protected final int ACTION_REQUEST_CODE_LAUNCH = 1;
   protected ViewOnclick mViewOnclick = new ViewOnclick();
   protected Dialog mDialog;
   AlertDialog.Builder mBuilder;
   String[] whitelist = new String[]{".doc", ".docx", ".zip", ".rar", ".apk", ".ipa", ".txt", ".exe", ".7z", ".e", ".z", ".ct", ".ke", ".cetrainer", ".db", ".tar", ".pdf", ".w3x", ".epub", ".mobi", ".azw", ".azw3", ".osk", ".osz", ".xpa", ".cpk", ".lua", ".jar", ".dmg", ".ppt", ".pptx", ".xls", ".xlsx", ".mp3", ".ipa", ".iso", ".img", ".gho", ".ttf", ".ttc", ".txf", ".dwg", ".bat", ".dll"};
   private List<String> history = new ArrayList();
   private List<FileItem> currentFile = new ArrayList();

   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      VirtualCore.get().setAppCallback(new AppCallback() {
         public void beforeStartApplication(String packageName, String processName, Context context) {
         }

         public void beforeApplicationCreate(String packageName, String processName, Application application) {
         }

         public void afterApplicationCreate(String packageName, String processName, Application application) {
         }

         public void beforeActivityOnCreate(Activity activity) {
         }

         public void afterActivityOnCreate(Activity activity) {
         }

         public void beforeActivityOnStart(Activity activity) {
         }

         public void afterActivityOnStart(Activity activity) {
         }

         public void beforeActivityOnResume(Activity activity) {
         }

         public void afterActivityOnResume(Activity activity) {
         }

         public void beforeActivityOnStop(Activity activity) {
         }

         public void afterActivityOnStop(Activity activity) {
         }

         public void beforeActivityOnDestroy(Activity activity) {
         }

         public void afterActivityOnDestroy(Activity activity) {
         }
      });
   }

   public void tipsDialog(String content, View.OnClickListener... onclick) {
      if (this.mDialog == null) {
         this.mBuilder = new AlertDialog.Builder(this, style.VACustomTheme);
         View view1 = this.getLayoutInflater().inflate(layout.dialog_tips, (ViewGroup)null);
         this.mBuilder.setView(view1);
         if (!this.isFinishing()) {
            this.mDialog = this.mBuilder.show();
         }

         if (this.mDialog == null) {
            return;
         }

         this.mDialog.setCanceledOnTouchOutside(false);
         TextView textView = (TextView)view1.findViewById(id.tips_content);
         textView.setText(content);
         this.mDialog.setCancelable(false);
         if (onclick != null && onclick.length == 2) {
            view1.findViewById(id.double_btn_layout).setVisibility(0);
            view1.findViewById(id.btn_cancel).setOnClickListener(onclick[0]);
            view1.findViewById(id.btn_ok).setOnClickListener(onclick[0]);
         } else if (onclick != null && onclick.length == 1) {
            view1.findViewById(id.single_btn_layout).setVisibility(0);
            view1.findViewById(id.single_btn).setOnClickListener(onclick[0]);
         }
      } else {
         this.mDialog.show();
      }

   }

   private String getMetaDataFromApp(Context context, String meta) {
      String value = "";

      try {
         ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
         value = appInfo.metaData.getString(meta);
      } catch (PackageManager.NameNotFoundException var5) {
         PackageManager.NameNotFoundException e = var5;
         e.printStackTrace();
      }

      return value;
   }

   protected void checkExtProcessAndlunch(int userId, String packageName, String appName) {
      if (this.bottomSheetLayout == null) {
         this.bottomSheetLayout = (BottomSheetLayout)LayoutInflater.from(this.getContext()).inflate(layout.layout_bottom_sheet, (ViewGroup)null);
      }

      if (this.bottomSheetDialog == null) {
         this.bottomSheetDialog = new BottomSheetDialog(this, style.BottomSheetDialog);
         this.bottomSheetDialog.setContentView(this.bottomSheetLayout);
      }

      this.bottomSheetLayout.beginShow(packageName);
      this.bottomSheetDialog.show();
      this.launchMirrorApp(userId, packageName, appName);
   }

   protected void launchMirrorApp(int userId, String packageName, String appName) {
      if (VirtualCore.get().isRunInExtProcess(packageName)) {
         if (!VirtualCore.get().isExtPackageInstalled()) {
            Toast.makeText(this, "Please install Extension Package.", 0).show();
            return;
         }

         if (!VExtPackageAccessor.hasExtPackageBootPermission()) {
            Toast.makeText(this, string.permission_boot_content, 0).show();
            return;
         }
      }

      try {
         if (userId != -1 && packageName != null) {
            boolean runAppNow = true;
            if (VERSION.SDK_INT >= 23) {
               InstalledAppInfo info = VirtualCore.get().getInstalledAppInfo(packageName, userId);
               ApplicationInfo applicationInfo = info.getApplicationInfo(userId);
               boolean isExt = VirtualCore.get().isRunInExtProcess(info.packageName);
               int runHostTargetSdkVersion = VirtualCore.get().getHostApplicationInfo().targetSdkVersion;
               if (isExt) {
                  try {
                     runHostTargetSdkVersion = this.getPackageManager().getApplicationInfo(VirtualCore.getConfig().getExtPackageName(), 0).targetSdkVersion;
                  } catch (Exception var10) {
                  }

                  if (this.checkExtPackageBootPermission()) {
                     return;
                  }
               }

               if (BuildCompat.isR() && runHostTargetSdkVersion >= 30 && info.getApplicationInfo(0).targetSdkVersion < 30 && (isExt && !VExtPackageAccessor.isExternalStorageManager() || !isExt && !Environment.isExternalStorageManager())) {
                  (new androidx.appcompat.app.AlertDialog.Builder(this.getContext())).setTitle(string.permission_boot_notice).setMessage(string.request_external_storage_manager_notice).setCancelable(false).setNegativeButton("GO", (dialog, which) -> {
                     RequestExternalStorageManagerActivity.request(VirtualCore.get().getContext(), isExt);
                  }).show();
                  return;
               }

               if (PermissionCompat.isCheckPermissionRequired(applicationInfo)) {
                  String[] permissions = VPackageManager.get().getDangerousPermissions(info.packageName);
                  if (!PermissionCompat.checkPermissions(permissions, isExt)) {
                     runAppNow = false;
                     PermissionRequestActivity.requestPermission(this.getActivity(), permissions, appName, userId, packageName, 6);
                  }
               }
            }

            HVLog.d(" runAppNow :" + runAppNow);
            if (runAppNow) {
               this.channelLimit = this.getPersistentValueToInt("channelLimit");
               this.channelStatus = this.getPersistentValueToInt("channelStatus");
               int channelLimitLocal = SPTools.getInt(this, "channelLimit", 0);
               long currentTimeMillisLimit = 0L;
               if (channelLimitLocal == 0) {
                  SPTools.putLong(this, "currentTimeMillisLimit", System.currentTimeMillis());
               } else {
                  currentTimeMillisLimit = SPTools.getLong(this, "currentTimeMillisLimit");
               }

               HVLog.d("channelLimitLocal:" + channelLimitLocal + "    channelLimit:" + this.channelLimit);
               HVLog.d("channelStatus:" + this.channelStatus + "    ");
               if (this.channelLimit <= channelLimitLocal) {
                  Toasty.warning(this.getContext(), "功能受限、请在设置中联系软件作者").show();
                  this.finish();
                  return;
               }

               if (this.channelStatus == 0) {
                  Toasty.warning(this.getContext(), "功能受限、请在设置中联系软件作者").show();
                  this.finish();
                  return;
               }

               if (!this.checkUpgrade()) {
                  VActivityManager.get().launchApp(userId, packageName);
               }

               SPTools.putInt(this, "channelLimit", channelLimitLocal + 1);
               this.finish();
            }
         }
      } catch (Throwable var11) {
         Throwable e = var11;
         e.printStackTrace();
      }

   }

   private boolean checkExtPackageBootPermission() {
      if (VirtualCore.get().isExtPackageInstalled()) {
         if (!VExtPackageAccessor.hasExtPackageBootPermission()) {
            this.showPermissionDialog();
            return true;
         }

         if (BuildCompat.isQ() && !Settings.canDrawOverlays(this.getActivity())) {
            this.showOverlayPermissionDialog();
            return true;
         }
      }

      return false;
   }

   private void showOverlayPermissionDialog() {
      (new AlertDialog.Builder(this)).setTitle("提示").setMessage("您必须向允许的启动活动界面后台授予覆盖权限.").setCancelable(false).setNegativeButton("GO", (dialog, which) -> {
         Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
         intent.setData(Uri.parse("package:" + this.getPackageName()));
         this.startActivityForResult(intent, 0);
      }).show();
   }

   public void showPermissionDialog() {
      Intent intent = OemPermissionHelper.getPermissionActivityIntent(this);
      (new androidx.appcompat.app.AlertDialog.Builder(this)).setTitle(string.permission_boot_notice).setMessage(string.permission_boot_content).setCancelable(false).setNegativeButton("GO", (dialog, which) -> {
         if (intent != null) {
            try {
               this.startActivity(intent);
            } catch (Throwable var5) {
               Throwable e = var5;
               e.printStackTrace();
            }
         }

      }).show();
   }

   public boolean checkVerify() {
      return true;
   }

   public String currentActivity() {
      return this.getLocalClassName();
   }

   public static String getSHA1(Context context) {
      try {
         PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 64);
         byte[] cert = info.signatures[0].toByteArray();
         MessageDigest md = MessageDigest.getInstance("SHA1");
         byte[] publicKey = md.digest(cert);
         StringBuffer hexString = new StringBuffer();

         for(int i = 0; i < publicKey.length; ++i) {
            String appendString = Integer.toHexString(255 & publicKey[i]).toUpperCase(Locale.US);
            if (appendString.length() == 1) {
               hexString.append("0");
            }

            hexString.append(appendString);
            hexString.append(":");
         }

         String result = hexString.toString();
         return result.substring(0, result.length() - 1);
      } catch (PackageManager.NameNotFoundException var8) {
         var8.printStackTrace();
      } catch (NoSuchAlgorithmException var9) {
         NoSuchAlgorithmException e = var9;
         e.printStackTrace();
      }

      return null;
   }

   @TargetApi(23)
   private boolean checkAndRequestPermission() {
      List<String> lackedPermission = new ArrayList();
      if (this.checkSelfPermission("android.permission.READ_PHONE_STATE") != 0) {
         lackedPermission.add("android.permission.READ_PHONE_STATE");
      }

      if (this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
         lackedPermission.add("android.permission.WRITE_EXTERNAL_STORAGE");
      }

      if (this.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0) {
         lackedPermission.add("android.permission.ACCESS_FINE_LOCATION");
      }

      if (lackedPermission.size() == 0) {
         HVLog.d("权限都已经有了，那么直接调用 return");
         return true;
      } else {
         String[] requestPermissions = new String[lackedPermission.size()];
         lackedPermission.toArray(requestPermissions);
         this.requestPermissions(requestPermissions, 1024);
         return false;
      }
   }

   public List<FileItem> getCurrentFile() {
      return this.currentFile;
   }

   public String getCloudDiskDirectory(String directoryName) {
      Iterator var2 = this.currentFile.iterator();

      FileItem fileItem;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         fileItem = (FileItem)var2.next();
      } while(!directoryName.equals(fileItem.getFilename()));

      return fileItem.getId();
   }

   protected void onStop() {
      super.onStop();
   }

   protected void onPause() {
      super.onPause();
   }

   class ViewOnclick implements View.OnClickListener {
      public void onClick(View v) {
         if (v.getId() == id.single_btn) {
            VerifyActivity.this.mDialog.dismiss();
            VerifyActivity.this.finish();
         } else if (v.getId() == id.btn_cancel) {
            VerifyActivity.this.mDialog.dismiss();
         } else if (v.getId() == id.btn_ok) {
            VerifyActivity.this.mDialog.dismiss();

            try {
               String assetFileName = "plugin_release.apk";
               HVLog.d("安装插件" + assetFileName);
               InputStream inputStream = null;
               File dir = VerifyActivity.this.getCacheDir();

               try {
                  inputStream = VerifyActivity.this.getAssets().open(assetFileName);
                  File apkFile = new File(dir, "plugin_release.apk");
                  FileUtils.writeToFile(inputStream, apkFile);
                  InstallTools.install(VerifyActivity.this, apkFile);
               } catch (IOException var6) {
                  IOException e = var6;
                  HVLog.printException(e);
               }
            } catch (Exception var7) {
            }
         }

      }
   }
}
