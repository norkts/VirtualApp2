package com.carlos.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.carlos.libcommon.StringFog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kook.librelease.R.id;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.remote.InstalledAppInfo;
import java.util.Locale;

public class BottomSheetLayout extends LinearLayout {
   Handler mHandler = new Handler();
   public static String PKG_KEY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iDFk7KgcMVg=="));
   public static String PKG_USERID = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGwaLCthNAYw"));
   BottomSheetDialog bottomSheetDialog;
   private EatBeansView loadingView;

   public BottomSheetLayout(Context context, @Nullable AttributeSet attrs) {
      super(context, attrs);
   }

   public LayoutInflater getLayoutInflater() {
      return LayoutInflater.from(this.getContext());
   }

   public void beginShow(String pkg) {
      InstalledAppInfo installedAppInfo = VirtualCore.get().getInstalledAppInfo(pkg, 0);
      ApplicationInfo applicationInfo = installedAppInfo.getApplicationInfo(installedAppInfo.getInstalledUsers()[0]);
      ImageView iconView = (ImageView)this.findViewById(id.app_icon);
      PackageManager pm = this.getContext().getPackageManager();
      CharSequence sequence = applicationInfo.loadLabel(pm);
      String appName = null;
      if (sequence != null) {
         appName = sequence.toString();
      }

      Drawable icon = applicationInfo.loadIcon(pm);
      iconView.setImageDrawable(icon);
      TextView nameView = (TextView)this.findViewById(id.app_name);
      nameView.setText(String.format(Locale.ENGLISH, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oy06M2ojAiZiICc/IykXDn8VSFo=")), appName));
   }

   @SuppressLint({"WrongViewCast"})
   protected void onFinishInflate() {
      super.onFinishInflate();
      this.loadingView = (EatBeansView)this.findViewById(id.loading_anim);
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
   }

   protected void onWindowVisibilityChanged(int visibility) {
      super.onWindowVisibilityChanged(visibility);
      if (visibility != 0) {
         this.loadingView.stopAnim();
      } else {
         this.loadingView.startAnim();
      }

   }
}
