package com.carlos.common.ui.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.carlos.common.device.DeviceInfo;
import com.carlos.common.ui.activity.base.BaseActivity;
import com.carlos.common.widget.toast.Toasty;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.string;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
   TextView toolbar_title;
   private ImageView imageViewBack;
   private TextView txtVersionCode;
   private TextView txtQQNumber;
   private RelativeLayout relaQQ;
   private TextView activationTv;
   private TextView devicesNo;

   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.setContentView(layout.activity_setting);
      this.initView();
      this.initData();
   }

   public void initView() {
      this.imageViewBack = (ImageView)this.findViewById(id.toolbar_left_menu);
      this.toolbar_title = (TextView)this.findViewById(id.toolbar_title);
      this.txtVersionCode = (TextView)this.findViewById(id.txtVersionCode);
      this.txtQQNumber = (TextView)this.findViewById(id.txtQQNumber);
      this.relaQQ = (RelativeLayout)this.findViewById(id.relaQQ);
      this.activationTv = (TextView)this.findViewById(id.activation_tv);
      this.devicesNo = (TextView)this.findViewById(id.devices_no);
      this.findViewById(id.txtAddFriend).setOnClickListener(this);
      this.findViewById(id.relaFeedBack).setOnClickListener(this);
      this.findViewById(id.relaUpdate).setOnClickListener(this);
      this.findViewById(id.txtShare).setOnClickListener(this);
      this.findViewById(id.relaQuestions).setOnClickListener(this);
      this.imageViewBack.setOnClickListener(this);
      this.relaQQ.setOnClickListener(this);
   }

   public static void copy(String content, Context context) {
      ClipboardManager cmb = (ClipboardManager)context.getSystemService("clipboard");
      cmb.setText(content.trim());
      Toasty.success(context, "编码已复制", 1);
   }

   public void initData() {
      DeviceInfo instance = DeviceInfo.getInstance(this);
      this.toolbar_title.setText("设置");
      this.txtVersionCode.setText("(当前版本" + instance.getVersionName(this) + ")" + instance.getVersionCode());
      this.txtQQNumber.setText("请点击联系客服QQ 后续持续更新,请多多关注");
      this.activationTv.setText("激活状态");
      this.activationTv.setOnClickListener((view) -> {
         this.devicesNo.setText(instance.getDevicesNo());
         copy(instance.getDevicesNo(), this);
      });
   }

   public static String timeMillisToFormat(long timestamp) {
      String time = (new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒")).format(new Date(timestamp));
      return time;
   }

   private FrameLayout.LayoutParams getUnifiedBannerLayoutParams() {
      Point screenSize = new Point();
      this.getWindowManager().getDefaultDisplay().getSize(screenSize);
      return new FrameLayout.LayoutParams(screenSize.x, Math.round((float)screenSize.x / 6.4F));
   }

   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
   }

   public void onClick(View view) {
      if (view.getId() == id.toolbar_left_menu) {
         this.finish();
      } else {
         String updateUrl;
         if (view.getId() == id.txtAddFriend) {
            if (isAppInstall(this.getPackageManager(), "com.tencent.mobileqq")) {
               updateUrl = "mqqwpa://im/chat?chat_type=wpa&uin=82319214";
               this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(updateUrl)));
            } else {
               Toasty.info(this, "请安装QQ客户端").show();
            }
         } else if (view.getId() == id.relaQuestions) {
            Toasty.success(this, "开发中ing").show();
         } else if (view.getId() == id.relaUpdate) {
            if (this.isUpgrade()) {
               if (this.mSoftVersions != null) {
                  updateUrl = this.mSoftVersions.getUpdateUrl();
                  Uri uri = Uri.parse(updateUrl);
                  Intent intent = new Intent("android.intent.action.VIEW", uri);
                  this.startActivity(intent);
               }
            } else {
               Toasty.success(this, this.getString(string.latest_version)).show();
            }
         }
      }

   }

   protected void onResume() {
      super.onResume();
   }

   private void getDownload() {
   }

   protected void onDestroy() {
      super.onDestroy();
   }

   public static boolean isAppInstall(PackageManager pm, String packageName) {
      boolean mBoolean = false;

      try {
         mBoolean = queryPackageInfo(pm, packageName) != null;
      } catch (Exception var4) {
         Exception e = var4;
         e.printStackTrace();
      }

      return mBoolean;
   }

   public static PackageInfo queryPackageInfo(PackageManager pm, String packageName) {
      PackageInfo mPackageInfo = null;
      if (pm != null && packageName.length() > 0) {
         try {
            mPackageInfo = pm.getPackageInfo(packageName.trim(), 0);
         } catch (PackageManager.NameNotFoundException var4) {
            PackageManager.NameNotFoundException e = var4;
            e.printStackTrace();
         }
      }

      return mPackageInfo;
   }
}
