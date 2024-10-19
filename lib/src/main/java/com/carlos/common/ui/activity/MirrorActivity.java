package com.carlos.common.ui.activity;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.carlos.common.App;
import com.carlos.common.clouddisk.ClouddiskLauncher;
import com.carlos.common.clouddisk.http.HttpWorker;
import com.carlos.common.clouddisk.listview.FileItem;
import com.carlos.common.device.DeviceInfo;
import com.carlos.common.download.DownloadListner;
import com.carlos.common.download.DownloadManager;
import com.carlos.common.network.VNetworkManagerService;
import com.carlos.common.ui.activity.abs.nestedadapter.SmartRecyclerAdapter;
import com.carlos.common.ui.activity.base.VerifyActivity;
import com.carlos.common.ui.adapter.MirrorAdapter;
import com.carlos.common.ui.adapter.bean.DeviceData;
import com.carlos.common.ui.adapter.bean.MirrorData;
import com.carlos.common.ui.adapter.decorations.ItemOffsetDecoration;
import com.carlos.common.ui.parse.MirrorDataParse;
import com.carlos.common.utils.AESUtil;
import com.carlos.common.utils.FileTools;
import com.carlos.common.utils.MD5Utils;
import com.carlos.common.utils.ResponseProgram;
import com.carlos.common.utils.SPTools;
import com.carlos.common.utils.ZipTools;
import com.carlos.common.widget.LabelView;
import com.carlos.common.widget.MirrorDialog;
import com.carlos.common.widget.TextProgressBar;
import com.carlos.common.widget.toast.Toasty;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import com.kook.librelease.R.dimen;
import com.kook.librelease.R.drawable;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.string;
import com.kook.librelease.R.style;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VLocationManager;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.InstalledAppInfo;
import com.lody.virtual.remote.vloc.VLocation;
import java.io.File;
import java.util.Iterator;
import java.util.List;

public class MirrorActivity extends VerifyActivity implements MirrorAdapter.OnAppClickListener {
   private static final String PKG_NAME_ARGUMENT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwYAWWAbHh9lDywTJAVXWmcYBlo="));
   private static final String KEY_PKGNAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JCwuGWY2IAtqIlkRIgUMVg=="));
   private static final String APP_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JgU6AmYxBhFoDDBF"));
   private static final String KEY_USER = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JCwuGWY2NF5qDyxF"));
   RecyclerView mMirrorMenuPanel;
   MirrorAdapter mMirrorAdapter;
   AppCompatTextView startApplication;
   AppCompatImageView appIcon;
   AppCompatTextView appNameView;
   AppCompatTextView appVersion;
   LabelView appUserId;
   LabelView appIsExt;
   List<MirrorData> mirrorDataList;
   String packageName;
   String appName;
   int userId;
   public String mMirrorParseFileName = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwgYKm8jGgRpHiAqIy0LDmUzGjBvEVRF"));
   public static final int MENU_TYPE_WIFI = 0;
   public static final int MENU_TYPE_LOC = 1;
   public static final int MENU_TYPE_PHONE = 2;
   public static final int MENU_TYPE_SHORTCUT = 3;
   public static final int MENU_TYPE_DELETE_CLEAR = 4;
   public static final int MENU_TYPE_BACKUP_RECOVERY = 5;
   public static final int LOCATION_CODE = 12;
   public static final int MOCK_PHONE = 1001;
   public static int MENU_TYPE_BACKUP = 7;
   public static int MENU_TYPE_RECOVERY = 8;
   boolean isNetWork = true;

   public static void launch(Context context, String packageName, int userId) {
      Intent intent = VirtualCore.get().getLaunchIntent(packageName, userId);
      if (intent != null) {
         Intent loadingPageIntent = new Intent(context, MirrorActivity.class);
         loadingPageIntent.putExtra(PKG_NAME_ARGUMENT, packageName);
         loadingPageIntent.addFlags(268435456);
         loadingPageIntent.putExtra(KEY_PKGNAME, intent);
         loadingPageIntent.putExtra(KEY_USER, userId);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iDFk7KgcLIA==")) + packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OGwaLCthMgYwPT5SVg==")) + userId);
         context.startActivity(loadingPageIntent);
      }

   }

   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.setContentView(layout.activity_mirror);
      this.userId = this.getIntent().getIntExtra(KEY_USER, -1);
      this.packageName = this.getIntent().getStringExtra(PKG_NAME_ARGUMENT);
      this.initView();
      this.initData(this.packageName, this.userId);
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cE28jNDdmHjBF")));
   }

   protected boolean isCheckLog() {
      return true;
   }

   private void initData(String pkg, int userId) {
      this.mirrorDataList = MirrorData.mirrorDataList;
      this.mMirrorAdapter.setList(this.mirrorDataList);
      boolean runInExtProcess = VirtualCore.get().isRunInExtProcess(pkg);
      InstalledAppInfo installedAppInfo = VirtualCore.get().getInstalledAppInfo(pkg, 0);
      ApplicationInfo applicationInfo = installedAppInfo.getApplicationInfo(installedAppInfo.getInstalledUsers()[0]);
      PackageManager pm = this.getPackageManager();
      CharSequence sequence = applicationInfo.loadLabel(pm);
      this.appName = sequence.toString();
      Drawable icon = applicationInfo.loadIcon(pm);
      PackageInfo packageInfo = null;

      try {
         packageInfo = pm.getPackageInfo(pkg, 0);
      } catch (PackageManager.NameNotFoundException var11) {
         PackageManager.NameNotFoundException e = var11;
         e.printStackTrace();
      }

      this.appIcon.setImageDrawable(icon);
      this.appIcon.setOnClickListener((view) -> {
         String apkPath = installedAppInfo.getApkPath();
         File externalFilesDir = this.getExternalFilesDir(this.getPackageName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
         HVLog.d(externalFilesDir + applicationInfo.packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4+KGUzSFo=")));
         FileTools.copyFile(apkPath, externalFilesDir + applicationInfo.packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4+KGUzSFo=")));
      });
      this.appIcon.setOnLongClickListener(new View.OnLongClickListener() {
         public boolean onLongClick(View view) {
            VNetworkManagerService networkManagerService = VNetworkManagerService.get();
            networkManagerService.systemReady(MirrorActivity.this.getContext());
            networkManagerService.randomDevices();
            return true;
         }
      });
      this.appNameView.setText(this.appName);
      if (packageInfo != null) {
         this.appVersion.setText(packageInfo.versionName);
      }

      this.getTitleLeftMenuIcon().setOnClickListener((view) -> {
         this.finish();
      });
      if (userId > 0) {
         this.appUserId.setVisibility(0);
         this.appUserId.setText(userId + 1 + "");
      } else {
         this.appUserId.setVisibility(4);
      }

      if (runInExtProcess) {
         this.appIsExt.setVisibility(0);
         this.appIsExt.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQdfLA==")));
      } else {
         this.appIsExt.setVisibility(4);
      }

   }

   private void initView() {
      this.mMirrorMenuPanel = (RecyclerView)this.findViewById(id.menu_panel);
      this.setTitleLeftMenuIcon(drawable.icon_back);
      this.setTitleName(string.title_mirror_manager);
      this.appIcon = (AppCompatImageView)this.findViewById(id.app_icon);
      this.appNameView = (AppCompatTextView)this.findViewById(id.app_name);
      this.appVersion = (AppCompatTextView)this.findViewById(id.app_version);
      this.appUserId = (LabelView)this.findViewById(id.app_user_id);
      this.appIsExt = (LabelView)this.findViewById(id.app_is_ext);
      StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, 1);
      this.mMirrorMenuPanel.setLayoutManager(layoutManager);
      this.mMirrorAdapter = new MirrorAdapter(this, this.packageName, this.userId);
      SmartRecyclerAdapter wrap = new SmartRecyclerAdapter(this.mMirrorAdapter);
      View footer = new View(this);
      footer.setLayoutParams(new StaggeredGridLayoutManager.LayoutParams(-1, ResponseProgram.dpToPx(this, 60)));
      wrap.setFooterView(footer);
      this.mMirrorMenuPanel.setAdapter(wrap);
      this.mMirrorMenuPanel.addItemDecoration(new ItemOffsetDecoration(this, dimen.desktop_divider));
      this.mMirrorAdapter.setAppClickListener(this);
      this.startApplication = (AppCompatTextView)this.findViewById(id.start_application);
      this.startApplication.setOnClickListener((view) -> {
         if (this.isNovatioNecessaria()) {
            Toasty.warning(this.getContext(), this.getString(string.update_latest_version)).show();
         } else {
            this.checkExtProcessAndlunch(this.userId, this.packageName, this.appName);
         }
      });
   }

   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cEWswMC9mNAYgLQYuPWoKGiRvVj8rKS4uDWYKLDZqHCweLz4eKVdNTVo=")) + requestCode + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl86KmgaLAVgEQofKi02PXgVSFo=")) + resultCode + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsFMDdmHiMi")) + data);
      if (requestCode == 12 && resultCode == -1) {
         int position = data.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAKWUaMC9gJFlF")), -1);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAKWUaMC9gJFs1W0QIVg==")) + position);
         this.mMirrorAdapter.notifyItemChanged(position);
      }

      if (requestCode == 1001 && resultCode == -1) {
         this.mMirrorAdapter.notifyDataSetChanged();
      }

      if (resultCode == 1107 && requestCode == 1) {
         String pkg = data.getStringExtra(KEY_PKGNAME);
         String appName = data.getStringExtra(APP_NAME);
         int userId = data.getIntExtra(KEY_USER, -1);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMBF9JwozLD0cLmgnTChDKAcXPQhSVg==")) + pkg + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OGwaLCthMgYwP18HOg==")) + userId);
         this.launchMirrorApp(userId, this.packageName, appName);
      }
   }

   public void onAppClick(int position, MirrorData model, String tag) {
      if (!this.isNetWork) {
         Toasty.warning(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsFA0ZJDw1YK14BAiIBGEcGEw1BExscAAknLE5XAyofLBsoUlsBDBwNOTcYUiUuHFodARpXE1QdUhsXRABcJRozSFo="))).show();
      } else {
         switch (model.getMenuType()) {
            case 0:
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgID2gFNCRONDg/LBVXPW8aGlFuDjwgMwQXPg==")) + model.getMenuType() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsKICVhJAYgKQdfDngVSFo=")) + position + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsKMANhHx43Ki0qCWUFLC5qClFF")) + this.tsp_mockwifi);
               if (this.tsp_mockwifi != 2) {
                  Toasty.warning(this.getContext(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxpAH0MXFzNYEBNIAxoZDEQXWhFAXhs+AEABLFlbRgNEK0YdAQsJL0BJIQpGAgsuH1dYJkRaGyBHAiUBBkQ3WA=="))).show();
                  return;
               }

               this.modifyWifiAddr(position, model);
               break;
            case 1:
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgID2gFNCRONDg/LBVXPW8aGlFuDjwgMwQXPg==")) + model.getMenuType() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsKICVhJAYgKQdfDngVSFo=")) + position + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsKMANhHx4uKQguLmUjQSRlEQYqLRcqI2AgRD0=")) + this.tsp_virtuallocation);
               if (this.tsp_virtuallocation != 2) {
                  Toasty.warning(this.getContext(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxpAH0MXFzNYEBNIAxoZDEQXWhFAXhs+AEABLFlbRgNEK0YdAQsJL0BJIQpGAgsuH1dYJkRaGyBHAiUBBkQ3WA=="))).show();
                  return;
               }

               this.startMockLocation(position, model);
               break;
            case 2:
               if (VERSION.SDK_INT >= 23 && VirtualCore.get().getTargetSdkVersion() >= 23) {
                  ActivityCompat.requestPermissions(this, new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsMGWUIFl99HAZXIRYAE2QmMB1kDyhF"))}, 0);
               }

               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgID2gFNCRONDg/LBVXPW8aGlFuDjwgMwQXPg==")) + model.getMenuType() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsKICVhJAYgKQdfDngVSFo=")) + position + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsKMANhHx43Ki0qCWozFiVlNysx")) + this.tsp_mockphone);
               if (this.tsp_mockphone != 2) {
                  Toasty.warning(this.getContext(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxpAH0MXFzNYEBNIAxoZDEQXWhFAXhs+AEABLFlbRgNEK0YdAQsJL0BJIQpGAgsuH1dYJkRaGyBHAiUBBkQ3WA=="))).show();
                  return;
               }

               this.startMockDevice(position, model);
               break;
            case 3:
               this.createShortcut();
               break;
            case 4:
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRg+PXkjSFo=")) + tag);
               if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjsqU38VSFo=")).equals(tag)) {
                  boolean res = VirtualCore.get().cleanPackageData(this.packageName, this.userId);
                  Toast.makeText(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4EM2sVASh9ASQsPxc2OWUzQCg=")) + (res ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0uOWszNANhIFlF")) : StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4+CWoFNCxON1RF"))), 0).show();
               } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjsqU38jSFo=")).equals(tag)) {
                  (new AlertDialog.Builder(this.getContext())).setTitle(string.tip_delete).setMessage(this.getContext().getString(string.text_delete_app, new Object[]{this.appName})).setPositiveButton(17039379, (dialog, which) -> {
                     VirtualCore.get().uninstallPackageAsUser(this.packageName, this.userId);
                     this.finish();
                  }).setNegativeButton(17039369, (DialogInterface.OnClickListener)null).show();
               }
               break;
            case 5:
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgID2gFNCRONDg/LBVXPW8aGlFuDjwgMwQXPg==")) + model.getMenuType() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsKICVhJAYgKQdfDngVSFo=")) + position + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsLEhVoNTBAJBYcDGEmNBRgDzBNIhU6E2k2LABkJThJJBU1Pg==")) + 5);
               if (this.tsp_backupRecovery != 2) {
                  Toasty.warning(this.getContext(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxpAH0MXFzNYEBNIAxoZDEQXWhFAXhs+AEABLFlbRgNEK0YdAQsJL0BJIQpGAgsuH1dYJkRaGyBHAiUBBkQ3WA=="))).show();
                  return;
               }

               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRg+PXkjSFo=")) + tag);
               ClouddiskLauncher.getInstance().launcherCloud(this);
               String content = "";
               if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjsqU38VSFo=")).equals(tag)) {
                  content = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxwnWkYWQjNYXh8NA1c7WkoGMRd8Cj87JFsrPlRXIVRVUz0UWTYBAR47MVFVCSEtER87IlooIRQYLxsTWwArGR4JGC5UFSY+XxsWPhwRQy4VFRggQApEJxgHECIGXxgtWhxZIhURBAUUAgAxE1YWLBgGPjFVEwQyXQopDkZaJRRYA0YJAiA7A0dJD0hAXhs+AEQ7ElgGQiVEXkYoARkZLEBaBz9GTUYAH0ArDnxXEzIaCR8uEQA/BVUNJQ0VPDFXXgQHXXokIiAbEEItRAkeJxUHGC8dEFBOPFsrOBs2PAVoJxAzXyEkLRoWRFF6CS0rXTY/HlcrMRAcLDE0ER87XBQPIwZ+Py0xXwAjXRRXIQECLA8cUzYBXFpWJUgfJ1RF"));
                  MirrorDialog.getInstance().showBakupAndRecovery(this, content, (view, dialog, textProgressBar) -> {
                     textProgressBar.setVisibility(0);
                     HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxwnWkYWQjNYEgsdOloJDhVWAzMCDQI+IxgiI05XLUgcPA8NXwABXFpWJUgfJ1RF")));
                     this.dataBakupAndRecovery(MENU_TYPE_BACKUP, dialog, textProgressBar);
                  });
               } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjsqU38jSFo=")).equals(tag)) {
                  content = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlYdGkZJAx9YAwssAglADkcGJVdBEjFIAFYFUlgUBxJEEBtKDAs3LUEyWlVGNi0dHglYJUQBB1ZKNgszM1tcD1o7RjZXLAsVEh8NIhs7AzAfPy0vUzYnPRkSLVUeUz1BE1sFJBUFSFo="));
                  MirrorDialog.getInstance().singleRecoveryInputDialog(this, content, (view, editText, dialog, textProgressBar) -> {
                     String code = editText.getText().toString();
                     if (TextUtils.isEmpty(code)) {
                        this.getHandler().post(() -> {
                           Toasty.error(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlYdGkZJAx9YAwssAglADkcULRZBKzECABtcAVlNMQJEXhszARwNPg=="))).show();
                        });
                     } else {
                        try {
                           String desDecrypt = AESUtil.desDecrypt(code);
                           HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguKWAFNClhNwYsLF8IVg==")) + desDecrypt + " ");
                           if (!TextUtils.isEmpty(desDecrypt)) {
                              textProgressBar.setVisibility(0);
                              textProgressBar.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsBEkZaOQtZXz05AgoVDEoGMVVBEx8MAAw3HQ==")));
                              String fileNameByPath = FileTools.getFileNameByPath(desDecrypt, false);
                              HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguKWAFNClhNwYsLF8IVg==")) + desDecrypt + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsJIC5jDlE/Ij0iD2khRT9kETg/IwNXVg==")) + fileNameByPath);
                              if (!TextUtils.isEmpty(fileNameByPath)) {
                                 String[] strings = fileNameByPath.split(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")));
                                 if (strings.length == 4) {
                                    String deviceNo = strings[0];
                                    String pkgName = strings[1];
                                    String userId = strings[2];
                                    String time = strings[3];
                                    String currentDate = ClouddiskLauncher.getCurrentDate(time);
                                    HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguLmUVLCtoNBEi")) + deviceNo + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl86KGUzPAB9Dl0/PT5SVg==")) + pkgName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsKNANiASwJKF8IVg==")) + userId + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsKMC9gDjMi")) + time + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OGswNARhNDA2LBU2OWUzBTI=")) + currentDate);
                                    if (this.packageName.equals(pkgName)) {
                                       textProgressBar.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzNYAwssAglADkctJRU=")));
                                       boolean res = VirtualCore.get().cleanPackageData(this.packageName, this.userId);
                                       Toast.makeText(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4EM2sVASh9ASQsPxc2OWUzQCg=")) + (res ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0uOWszNANhIFlF")) : StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4+CWoFNCxON1RF"))), 0).show();
                                       ClouddiskLauncher.getInstance().launcherCloud(this, currentDate, (fileItemList) -> {
                                          String fileName = FileTools.getFileNameByPath(desDecrypt, true);
                                          Iterator var6 = fileItemList.iterator();

                                          while(var6.hasNext()) {
                                             FileItem fileItem = (FileItem)var6.next();
                                             HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YDmgbBjdgDjMiPxhSVg==")) + fileName + "  " + fileItem.toString());
                                             if (fileName.equals(fileItem.getFilename())) {
                                                HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwoVJkZJRgJYXgcUA1Y7E0cGEwtAXwMoAAlAGlgHLQxEXgc/ASAJXkBaIQZ7DFFF")));
                                                ClouddiskLauncher.getInstance().downFileByCloud(fileItem.getId()).done((downloadLink) -> {
                                                   if (downloadLink != null && !TextUtils.isEmpty(downloadLink.getDlLink())) {
                                                      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl84M1QXIisGNgAwWkACMhwsPyg=")));
                                                      DownloadManager mDownloadManager = DownloadManager.getInstance();
                                                      final File dataUserPackageDirectory = VEnvironment.getDataUserPackageDirectory(this.userId, this.packageName);
                                                      if (!dataUserPackageDirectory.exists()) {
                                                         dataUserPackageDirectory.mkdirs();
                                                      }

                                                      mDownloadManager.add(this.getContext(), downloadLink.getDlLink(), dataUserPackageDirectory.getPath(), fileName, new DownloadListner() {
                                                         public void onFinished() {
                                                            MirrorActivity.this.getHandler().post(() -> {
                                                               Toasty.success(MirrorActivity.this.getContext(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzNYEloOAgkdDH4jSFo=")), 0).show();
                                                               File file = new File(dataUserPackageDirectory.getPath(), fileName);
                                                               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YDmhSIiwYSQA0WxxEPVVJOi4GST8r")) + file.getAbsolutePath() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl86PmUVHitLHgYpPxcMImwgAgZsIz8x")) + file.exists());
                                                               String fileMD5Sync = MD5Utils.fileMD5Sync(file);
                                                               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YDmgbEhZMDygZKj0pIA==")) + fileMD5Sync);
                                                               int i = ZipTools.uncompressZip(file.getAbsolutePath(), dataUserPackageDirectory.getPath(), (zipName) -> {
                                                                  MirrorActivity.this.getHandler().post(() -> {
                                                                     textProgressBar.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlcjOUZJAws=")) + zipName);
                                                                  });
                                                               });
                                                               if (i == 0) {
                                                                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlcjOUZJAwtYEloOAgkdDA==")));
                                                                  MirrorDataParse mirrorDataParse = new MirrorDataParse();
                                                                  mirrorDataParse.parseBackupData(dataUserPackageDirectory.getPath() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + MirrorActivity.this.mMirrorParseFileName);
                                                               }

                                                               dialog.dismiss();
                                                               MirrorActivity.this.mMirrorAdapter.notifyDataSetChanged();
                                                            });
                                                         }

                                                         public void onProgress(float progress) {
                                                            MirrorActivity.this.getHandler().post(() -> {
                                                               textProgressBar.setProgress((int)progress);
                                                            });
                                                         }

                                                         public void onPause() {
                                                            MirrorActivity.this.getHandler().post(() -> {
                                                               Toast.makeText(MirrorActivity.this.getContext(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwlAHEZJIR5YXgcUPwhSVg==")), 0).show();
                                                            });
                                                         }

                                                         public void onCancel() {
                                                            MirrorActivity.this.getHandler().post(() -> {
                                                               dialog.dismiss();
                                                               textProgressBar.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ol8uVg==")));
                                                               textProgressBar.setProgress(0);
                                                               textProgressBar.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzNYEg8rA1cNPQ==")));
                                                               Toast.makeText(MirrorActivity.this.getContext(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzNYFUYQAhwdIkoBRiFBExsTACE/BksFSFo=")), 0).show();
                                                            });
                                                         }
                                                      });
                                                      mDownloadManager.downloadSingle(downloadLink.getDlLink());
                                                   } else {
                                                      this.getHandler().post(() -> {
                                                         dialog.dismiss();
                                                         Toasty.warning(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlYdGkZJAx9YEAdAA0AnJUcsJRRBEloz"))).show();
                                                      });
                                                   }

                                                });
                                                break;
                                             }
                                          }

                                       });
                                    } else {
                                       this.getHandler().post(() -> {
                                          Toasty.warning(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxwnWkYWQjNYAwssAglADkcGEw1ATQsoBUQ3GFgtEytEFUYSASANAXkVSFo=")) + this.appName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PzYrIhkNOVEUEVRF"))).show();
                                       });
                                    }
                                 }
                              }
                           }
                        } catch (Exception var15) {
                           this.getHandler().post(() -> {
                              Toasty.error(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlcdLUMWA15YEAs/AgtAP0cRQiZBNkZKAFcnG1haHwJEAw85AUQjKkEvLR0="))).show();
                           });
                        }

                     }
                  });
               }
         }

      }
   }

   public void dataBakupAndRecovery(int ITEM_TYPE, Dialog dialog, TextProgressBar textProgressBar) {
      DeviceInfo instance = DeviceInfo.getInstance(this);
      String devicesNo = instance.getDevicesNo();
      textProgressBar.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsBEkZaOQtYEg8WAiAjWUcsLVFBNiEZAD8rDlgEQik=")));
      File dataUserPackageDirectory = VEnvironment.getDataUserPackageDirectory(this.userId, this.packageName);
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PlsrIhkNOVEUUzFKWFo7UlVWH1YePy0oX184KRlJHlo=")) + dataUserPackageDirectory.getAbsolutePath() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OEYyRiVYEzk+AhtABEcyJSBDKAcX")) + dataUserPackageDirectory.exists());
      if (!dataUserPackageDirectory.exists()) {
         this.getHandler().post(() -> {
            Toasty.error(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsFA0ZJDw1YFQdLAiANMkdNBwJBAwMbABtcPlgsBz0="))).show();
            dialog.dismiss();
         });
      } else {
         String currentDateFolderId = ClouddiskLauncher.getInstance().getCurrentDateFolderId();
         MirrorDataParse mirrorDataParse = new MirrorDataParse();
         String backupData = mirrorDataParse.getBackupData(this.packageName, this.userId);
         FileTools.saveAsFileWriter(dataUserPackageDirectory.getAbsolutePath() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + this.mMirrorParseFileName, backupData);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRg+LGsYNANiASwCLwcqCW4jEitjER45Lhg2CmAjMAZ7N1RF")) + dataUserPackageDirectory.getAbsolutePath());
         long currentTimeMillis = System.currentTimeMillis();
         String zipFile = this.getSavePath() + devicesNo + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + this.packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + this.userId + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + currentTimeMillis + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz1XCW8FSFo="));
         String desEncrypt = AESUtil.desEncrypt(zipFile);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KD4YKGAjAiRiDQJF")) + zipFile + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsNMQwCUws1XToIVg==")) + desEncrypt);
         String desDecrypt = AESUtil.desDecrypt(desEncrypt);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlcjOUZaBwhLHgo/Iys2PW4KRT9sHi4cEhlXVg==")) + desDecrypt);
         ResponseProgram.defer().when(() -> {
            try {
               if (ITEM_TYPE == MENU_TYPE_BACKUP) {
                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsBEkZaOQtYEFoXAiJYMXgVSFo=")));
                  textProgressBar.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsBEkZaOQtYEFoXAiJYMQ==")));
                  ZipTools.compressZip(dataUserPackageDirectory.getAbsolutePath(), zipFile, (name) -> {
                     this.getHandler().post(() -> {
                        textProgressBar.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsBEkZaOQtYEFoXAiJYMQ==")) + name);
                     });
                  });
                  textProgressBar.setProgress(10);
                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxoZXkYGGy9YEloOAgkdDHgVSFo=")) + zipFile);
                  textProgressBar.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxoZXkYGGy9YEloOAgkdDA==")));
                  int maxlength = 103809024;
                  File file = new File(zipFile);
                  long sizeKb = file.length() / 1024L;
                  long sizeMb = sizeKb / 1024L;
                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Bwk/WkYWQj5YEg89AhwjREUWJVc=")) + sizeKb + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC4LOHsJIANjAQI/IgctIA==")) + sizeMb + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhYIHA==")));
                  if (file.length() >= (long)maxlength) {
                     this.getHandler().post(() -> {
                        MirrorDialog.getInstance().tipsSingleDialog(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxtABkYEPSBYAwssAglADkoGGxVAXxtJOV45DmgIMzdEFUYSASANAQ==")) + sizeMb + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwYLDkYWRg1ZSS0dAlcdXUcGJSg=")), new MirrorDialog.SingleDialogClickListener() {
                           public void onClick(View view, Dialog exitDialog) {
                              exitDialog.dismiss();
                           }
                        });
                     });
                     dialog.dismiss();
                     return;
                  }

                  ClouddiskLauncher.getInstance().updaterCloud(zipFile, currentDateFolderId, new HttpWorker.UpLoadCallbackListener() {
                     long time = 0L;

                     public void onError(int count) {
                        MirrorActivity.this.getHandler().post(() -> {
                           textProgressBar.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcX0YWGyhZST0p")) + count + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BhknGUMRByU=")));
                           HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cWG8gFiVhMCY/XlYkMRUvACscTSIvEQsGIVUEECAZAAQgWgwOIhgHPjBXAict")) + zipFile);
                           FileTools.delete(zipFile);
                           dialog.dismiss();
                           MirrorDialog.getInstance().tipsSingleDialog(MirrorActivity.this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcX0YWGyhZST0p")) + count + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BhknGUMRByVOUx81WSUZHBs7BxdUN1RF")), new MirrorDialog.SingleDialogClickListener() {
                              public void onClick(View view, Dialog exitDialog) {
                                 exitDialog.dismiss();
                              }
                           });
                        });
                     }

                     public void Progress(double progress) {
                        long timeMillis = System.currentTimeMillis();
                        if (timeMillis - this.time > 2000L) {
                           HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IhcMD2gwFithJys8AlcdXUcGJShBX0ZIPy5SVg==")) + progress);
                           this.time = timeMillis;
                        }

                        if (textProgressBar != null) {
                           MirrorActivity.this.getHandler().post(() -> {
                              textProgressBar.setProgress((int)progress);
                           });
                        }

                        if (progress == 100.0) {
                           HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cW2UVBi9hJB08AhkdOkoyFyxBFSFJABtYP1gtGwlEEBNPARs/I0EEJVZGEl4hMT5SVg==")) + zipFile);
                           FileTools.delete(zipFile);
                           MirrorActivity.this.getHandler().post(() -> {
                              dialog.dismiss();
                              MirrorDialog.getInstance().tipsSingleDialog(MirrorActivity.this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlcdLUMRAwJYXl4LOC5SVg==")) + desEncrypt + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PSYjOBRSGCAVKAg9EUAgMwIBECxUAxwZEgldJx0HDCEGFSIvXiIoIFcWBDEZFVg7HiEjJUVbDzJHKx8SBxpcLg==")), new MirrorDialog.SingleDialogClickListener() {
                                 public void onClick(View view, Dialog exitDialog) {
                                    exitDialog.dismiss();
                                    ClipboardManager cmb = (ClipboardManager)MirrorActivity.this.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ECW8FFiV9ASww")));
                                    //FIXME
//                                    cmb.setText(desEncryptx);
                                    Toasty.success(MirrorActivity.this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ByEBAEYBLRFYFT0qAhsNUkctEz4=")), 1).show();
                                 }
                              });
                           });
                        }

                     }

                     public void onFinish(int count) {
                        MirrorActivity.this.getHandler().post(() -> {
                           if (textProgressBar != null) {
                              textProgressBar.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcX0YWGyg=")) + count + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxwZQEYtRkw=")));
                           }

                           HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cW2UVBi9hJB08AhkdOkoyFyxBFSFJABtYP1gtGwlEEBNPARs/I0EEJVZGEl4hMT5SVg==")) + zipFile);
                           FileTools.delete(zipFile);
                           dialog.dismiss();
                        });
                     }
                  });
               }

               if (ITEM_TYPE == MENU_TYPE_RECOVERY) {
                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsBEkZaOQtZXhNPAhkVHQ==")));
                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlYdGkZJAx9YEloOAgkdDHgVSFo=")) + zipFile);
               }
            } catch (Exception var14) {
               Exception e = var14;
               HVLog.printException(e);
            }

         });
      }
   }

   public void startMockDevice(int position, MirrorData model) {
      DeviceData deviceData = new DeviceData(this.getContext(), (InstalledAppInfo)null, this.userId);
      int count = this.userId + 1;
      deviceData.name = this.getString(string.menu_mock_phone) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PBhSVg==")) + this.appName + (this.userId == 0 ? "" : StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MwhSVg==")) + count) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PAhSVg=="));
      deviceData.packageName = this.packageName;
      DeviceDetailActiivty.open(this, deviceData, position);
   }

   public void createShortcut() {
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxpcG0ZbQjJYFRMxAglAI0dNGz9BEgdB")));
      VirtualCore.OnEmitShortcutListener listener = new VirtualCore.OnEmitShortcutListener() {
         public Bitmap getIcon(Bitmap originIcon) {
            return originIcon;
         }

         public String getName(String originName) {
            return originName;
         }
      };
      boolean shortcut = VirtualCore.get().createShortcut(this.userId, this.packageName, listener);
      Toast.makeText(this.getContext(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxpcG0ZbQjI=")) + (shortcut ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwpcAkZJWh8=")) : StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxwnL0MWPSs="))), 0).show();
   }

   public void startMockLocation(int position, MirrorData model) {
      VLocation location = VLocationManager.get().getLocation(this.packageName, this.userId);
      Intent intent = new Intent(this.getContext(), GDChooseLocationActivity.class);
      if (location != null) {
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgHx4oKi0qOWUzLCVlN1RF")), location);
      }

      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k/LRg2KG4gDSZsETgqIz4+IWIFSFo=")), this.packageName);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k/LRg2KG4gDSZvDjAgKS4YIA==")), this.userId);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAKWUaMC9gJFlF")), position);
      this.startActivityForResult(intent, 12);
   }

   public void modifyWifiAddr(int position, MirrorData model) {
      String SSID_KEY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki02CWgIGiFiAQZF")) + this.packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + this.userId;
      String MAC_KEY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+OWYzQStnAVRF")) + this.packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + this.userId;
      SettingConfig.FakeWifiStatus fakeWifiStatus = App.getApp().mConfig.getFakeWifiStatus(this.packageName, this.userId);
      AlertDialog.Builder builder = new AlertDialog.Builder(this, style.VACustomTheme);
      View view1 = this.getLayoutInflater().inflate(layout.dialog_change_wifi, (ViewGroup)null);
      builder.setView(view1);
      Dialog dialog = builder.show();
      ((Dialog)dialog).setCanceledOnTouchOutside(false);
      EditText editText1 = (EditText)view1.findViewById(id.edt_ssid);
      if (fakeWifiStatus != null) {
         editText1.setText(fakeWifiStatus.getSSID());
      } else {
         editText1.setText(SettingConfig.FakeWifiStatus.DEFAULT_SSID);
      }

      EditText editText2 = (EditText)view1.findViewById(id.edt_mac);
      if (fakeWifiStatus != null) {
         editText2.setText(fakeWifiStatus.getMAC());
      } else {
         editText2.setText(SettingConfig.FakeWifiStatus.DEFAULT_MAC);
      }

      ((Dialog)dialog).setCancelable(false);
      view1.findViewById(id.btn_cancel).setOnClickListener((v2) -> {
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwxcP0YtQh9mJAY+KQMmP24jMClrAQJF")));
         dialog.dismiss();
      });
      view1.findViewById(id.btn_ok).setOnClickListener((v2) -> {
         dialog.dismiss();

         try {
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwxcP0YtQh9mJAY+KQMmDWwFSFo=")));
            String ssid = editText1.getText().toString();
            String mac = editText2.getText().toString();
            SPTools.putString(this.getContext(), SSID_KEY, ssid);
            SPTools.putString(this.getContext(), MAC_KEY, mac);
            this.mMirrorAdapter.notifyItemChanged(position);
         } catch (Exception var10) {
            Toast.makeText(this.getContext(), string.input_loc_error, 0).show();
         }

      });
   }
}
