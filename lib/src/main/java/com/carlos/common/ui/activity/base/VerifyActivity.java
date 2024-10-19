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
   private static String META_DATA_KEY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii42D28gIC9gJFkPIBUEVg=="));
   BottomSheetDialog bottomSheetDialog;
   BottomSheetLayout bottomSheetLayout;
   private static final String PKG_NAME_ARGUMENT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwYAWWAbHh9lDywTJAVXWmcYBlo="));
   private static final String KEY_PKGNAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JCwuGWY2IAtqIlkRIgUMVg=="));
   private static final String APP_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JgU6AmYxBhFoDDBF"));
   private static final String KEY_USER = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JCwuGWY2NF5qDyxF"));
   protected final int ACTION_REQUEST_CODE_LAUNCH = 1;
   protected ViewOnclick mViewOnclick = new ViewOnclick();
   protected Dialog mDialog;
   AlertDialog.Builder mBuilder;
   String[] whitelist = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4qD2szSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4qD2swRVo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz1XCW8FSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz0MP28jSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4+KGUzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4YKGsVSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz0qIGwFSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4uIGgVSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MzkmIg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4uVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz1XVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz42LA==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz5bMw==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz42M2wKFjdjDlk/Iz5SVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4qOg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz0qP28jSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz06PGgjSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz0lKWkFSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4uKGwVFlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4ID2sjAlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4+ImwzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4+Imw0LFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4AKWUzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4AKWkjSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz1fKGsVSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz42KGUzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4EI2sVSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz5XP28jSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4qDWgzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz06KGwFSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz06KGwKRVo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz1fDm8zSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz1fDm8wRVo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4IKH8zSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4YKGsVSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4YKWozSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4YDWgzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4mCmozSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz0qLGgjSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz0qLGszSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz0qIGgjSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4qLWgzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4MP2wFSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4qDmoFSFo="))};
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
            Toast.makeText(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IhgEM2saLCtLHgY2Iy42OW8zOyhjDlk/LhgcD2MKAil5HyAqKAg+O2sjNwQ=")), 0).show();
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
                  (new androidx.appcompat.app.AlertDialog.Builder(this.getContext())).setTitle(string.permission_boot_notice).setMessage(string.request_external_storage_manager_notice).setCancelable(false).setNegativeButton(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JSwAVg==")), (dialog, which) -> {
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

            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhcMI2ohJAJhHFk1LCklIA==")) + runAppNow);
            if (runAppNow) {
               this.channelLimit = this.getPersistentValueToInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fP2ojBitgHFEzKgccLg==")));
               this.channelStatus = this.getPersistentValueToInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fP2ojBitgHyggLwg2LWoFSFo=")));
               int channelLimitLocal = SPTools.getInt(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fP2ojBitgHFEzKgccLg==")), 0);
               long currentTimeMillisLimit = 0L;
               if (channelLimitLocal == 0) {
                  SPTools.putLong(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0uKm8jNCZmHwozKgcMUmwjOCRqDjBTIxgII2YVSFo=")), System.currentTimeMillis());
               } else {
                  currentTimeMillisLimit = SPTools.getLong(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0uKm8jNCZmHwozKgcMUmwjOCRqDjBTIxgII2YVSFo=")));
               }

               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fP2ojBitgHFEzKgccLmczNCloAQUx")) + channelLimitLocal + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsFLCB9Dlk2KAdbU2wjPC9vV1FF")) + this.channelLimit);
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fP2ojBitgHyggLwg2LWoOIFo=")) + this.channelStatus + "    ");
               if (this.channelLimit <= channelLimitLocal) {
                  Toasty.warning(this.getContext(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxpAH0MXFzNYEBNIAxoZDEQXWhFAXhs+AEABLFlbRgNEK0YdAQsJL0BJIQpGAgsuH1dYJkRaGyBHAiUBBkQ3WA=="))).show();
                  this.finish();
                  return;
               }

               if (this.channelStatus == 0) {
                  Toasty.warning(this.getContext(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxpAH0MXFzNYEBNIAxoZDEQXWhFAXhs+AEABLFlbRgNEK0YdAQsJL0BJIQpGAgsuH1dYJkRaGyBHAiUBBkQ3WA=="))).show();
                  this.finish();
                  return;
               }

               if (!this.checkUpgrade()) {
                  VActivityManager.get().launchApp(userId, packageName);
               }

               SPTools.putInt(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fP2ojBitgHFEzKgccLg==")), channelLimitLocal + 1);
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
      (new AlertDialog.Builder(this)).setTitle(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwodAkYBPTI="))).setMessage(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwoJCkZbBxVZEiUhAhojA0ctBxFAXh8zAAlAGlgXJSpEEAMhAVYZPUEXWgZGAC0RHxpYKUQXOUxHKS0dBwoZXUYWWgpZXzEUAiABBkdNORNAEBMRPC5SVg=="))).setCancelable(false).setNegativeButton(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JSwAVg==")), (dialog, which) -> {
         Intent intent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kpKAg2LmwjMC1sIxosLT0qI2AgRClkDCRTICwIGWEhGgxgHAoRIwYAU30YFg5nJTBBIiwYUmIjSFo=")));
         intent.setData(Uri.parse(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iDQJF")) + this.getPackageName()));
         this.startActivityForResult(intent, 0);
      }).show();
   }

   public void showPermissionDialog() {
      Intent intent = OemPermissionHelper.getPermissionActivityIntent(this);
      (new androidx.appcompat.app.AlertDialog.Builder(this)).setTitle(string.permission_boot_notice).setMessage(string.permission_boot_content).setCancelable(false).setNegativeButton(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JSwAVg==")), (dialog, which) -> {
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
         MessageDigest md = MessageDigest.getInstance(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IixfEX8VSFo=")));
         byte[] publicKey = md.digest(cert);
         StringBuffer hexString = new StringBuffer();

         for(int i = 0; i < publicKey.length; ++i) {
            String appendString = Integer.toHexString(255 & publicKey[i]).toUpperCase(Locale.US);
            if (appendString.length() == 1) {
               hexString.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OhhSVg==")));
            }

            hexString.append(appendString);
            hexString.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OD5SVg==")));
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
      if (this.checkSelfPermission(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsMGWUIFl99HAZXIRYAE2QmMB1kDyhF"))) != 0) {
         lackedPermission.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsMGWUIFl99HAZXIRYAE2QmMB1kDyhF")));
      }

      if (this.checkSelfPermission(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsmU2sLFgpgIgoXOzwAU30xJExmMjBOLiwqAmYmFlo="))) != 0) {
         lackedPermission.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsmU2sLFgpgIgoXOzwAU30xJExmMjBOLiwqAmYmFlo=")));
      }

      if (this.checkSelfPermission(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCw+H2UmLBB9JVlKIiwqGWEhHl5jNThOLQUYHw=="))) != 0) {
         lackedPermission.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCw+H2UmLBB9JVlKIiwqGWEhHl5jNThOLQUYHw==")));
      }

      if (lackedPermission.size() == 0) {
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwkFE0NND0xZEC0dAhw3KEcWIQ9BAAdPABtABF4vWhFFEC0sAQsNBkEyQiBGKR80H1c/DEQtFz5+NFk0KRcuKmojSFo=")));
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
               String assetFileName = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgEI2gzAiZsJyw/KhcMOWoFBSZoDjwi"));
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxwZXEMRFxVYABMAAlcBLA==")) + assetFileName);
               InputStream inputStream = null;
               File dir = VerifyActivity.this.getCacheDir();

               try {
                  inputStream = VerifyActivity.this.getAssets().open(assetFileName);
                  File apkFile = new File(dir, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgEI2gzAiZsJyw/KhcMOWoFBSZoDjwi")));
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
