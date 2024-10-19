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
      ClipboardManager cmb = (ClipboardManager)context.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ECW8FFiV9ASww")));
      cmb.setText(content.trim());
      Toasty.success(context, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ByEBAEYBLRFYFT0qAhsNUkctEz4=")), 1);
   }

   public void initData() {
      DeviceInfo instance = DeviceInfo.getInstance(this);
      this.toolbar_title.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlcZJkYGHyY=")));
      this.txtVersionCode.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PFsrOxg7MQkdFj0JXlo7GFUzSFo=")) + instance.getVersionName(this) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PAhSVg==")) + instance.getVersionCode());
      this.txtQQNumber.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlcdLUYHEz9YED0hA0A/AEcWDzFBFR8pAD8BAWkLPyNEEyVTARs7L0EHGx1GAl48HgoFM0QHISZ/Pw86EyYrPBorMSxVPAsVWCU7L1ozSFo=")));
      this.activationTv.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwsdEkYsPTFYNgcuAgkjEw==")));
      this.activationTv.setOnClickListener((view) -> {
         this.devicesNo.setText(instance.getDevicesNo());
         copy(instance.getDevicesNo(), this);
      });
   }

   public static String timeMillisToFormat(long timestamp) {
      String time = (new SimpleDateFormat(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KAcYJ2lWMT8bHF0NAgpYX2kzAC4YKysrOwZdIlQvIChsFg9MXBcYD0EvOQA=")))).format(new Date(timestamp));
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
            if (isAppInstall(this.getPackageManager(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41Dm8jNCpqAQIgKRc+Vg==")))) {
               updateUrl = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwc+L2wwIDd3MBE1KQdWDW4FFjdvVwYqIwg+CmwjFgZlHg02IwdXO3swNAVqIAEvCTotOHsJNyV1N1RF"));
               this.startActivity(new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xIBZmDzxF")), Uri.parse(updateUrl)));
            } else {
               Toasty.info(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlcdLUZaAwlZXy0VOwYgPVVaRy4GXyIuWAwAVg=="))).show();
            }
         } else if (view.getId() == id.relaQuestions) {
            Toasty.success(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsBEkZJB0hYXh83KQcYMw=="))).show();
         } else if (view.getId() == id.relaUpdate) {
            if (this.isUpgrade()) {
               if (this.mSoftVersions != null) {
                  updateUrl = this.mSoftVersions.getUpdateUrl();
                  Uri uri = Uri.parse(updateUrl);
                  Intent intent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xIBZmDzxF")), uri);
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
