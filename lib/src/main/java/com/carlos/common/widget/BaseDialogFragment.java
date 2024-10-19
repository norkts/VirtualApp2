package com.carlos.common.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.carlos.common.widget.effects.DialogDismissListener;
import com.carlos.common.widget.effects.DialogResultListener;
import com.carlos.libcommon.StringFog;
import com.carlos.science.utils.DensityUtil;
import com.kook.librelease.R.style;

public abstract class BaseDialogFragment extends DialogFragment {
   private int mWidth = -2;
   private int mHeight = -2;
   private int mGravity = 17;
   private int mOffsetX = 0;
   private int mOffsetY = 0;
   private int mAnimation;
   protected DialogResultListener mDialogResultListener;
   protected DialogDismissListener mDialogDismissListener;
   private Context mContext;

   public BaseDialogFragment() {
      this.mAnimation = style.DialogBaseAnimation;
   }

   protected static Bundle getArgumentBundle(Builder b) {
      Bundle bundle = new Bundle();
      bundle.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwUmCWgKMCA=")), b.mWidth);
      bundle.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwZfM2UVPCBmEVRF")), b.mHeight);
      bundle.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwYmKmsaOC9mEQZF")), b.mGravity);
      bundle.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwYAPmggLCtmHxpF")), b.mOffsetX);
      bundle.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwYAPmggLCtmHwZF")), b.mOffsetY);
      bundle.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwY+CGUVEjdmHgY1Kj5SVg==")), b.mAnimation);
      return bundle;
   }

   public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (this.getArguments() != null) {
         this.mWidth = this.getArguments().getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwUmCWgKMCA=")));
         this.mHeight = this.getArguments().getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwZfM2UVPCBmEVRF")));
         this.mOffsetX = this.getArguments().getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwYAPmggLCtmHxpF")));
         this.mOffsetY = this.getArguments().getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwYAPmggLCtmHwZF")));
         this.mAnimation = this.getArguments().getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwY+CGUVEjdmHgY1Kj5SVg==")));
         this.mGravity = this.getArguments().getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwYmKmsaOC9mEQZF")));
      }

   }

   protected abstract View setView(LayoutInflater var1, @Nullable ViewGroup var2, Bundle var3);

   @Nullable
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
      this.setStyle();
      return this.setView(inflater, container, savedInstanceState);
   }

   private void setStyle() {
      Window window = this.getDialog().getWindow();
      this.getDialog().requestWindowFeature(1);
      this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
      window.getDecorView().setPadding(0, 0, 0, 0);
      WindowManager.LayoutParams wlp = window.getAttributes();
      wlp.width = this.mWidth;
      wlp.height = this.mHeight;
      wlp.gravity = this.mGravity;
      wlp.x = DensityUtil.dip2px(this.getDialog().getContext(), (float)this.mOffsetX);
      wlp.y = DensityUtil.dip2px(this.getDialog().getContext(), (float)this.mOffsetY);
      window.setAttributes(wlp);
   }

   public BaseDialogFragment setDialogResultListener(DialogResultListener dialogResultListener) {
      this.mDialogResultListener = dialogResultListener;
      return this;
   }

   public BaseDialogFragment setDialogDismissListener(DialogDismissListener dialogDismissListener) {
      this.mDialogDismissListener = dialogDismissListener;
      return this;
   }

   public void onDestroy() {
      super.onDestroy();
   }

   public void show(FragmentManager manager, String tag) {
      FragmentTransaction transaction = manager.beginTransaction();
      Fragment fragment = manager.findFragmentByTag(tag);
      if (fragment != null) {
         transaction.remove(fragment);
      } else {
         transaction.add(this, tag);
      }

      transaction.commitAllowingStateLoss();
   }

   public void onAttach(Context context) {
      super.onAttach(context);
      this.mContext = context;
   }

   public abstract static class Builder<T extends Builder, D extends BaseDialogFragment> {
      private int mWidth = -2;
      private int mHeight = -2;
      private int mGravity = 17;
      private int mOffsetX = 0;
      private int mOffsetY = 0;
      private int mAnimation;

      public Builder() {
         this.mAnimation = style.DialogBaseAnimation;
      }

      public T setSize(int mWidth, int mHeight) {
         this.mWidth = mWidth;
         this.mHeight = mHeight;
         return this;
      }

      public T setGravity(int mGravity) {
         this.mGravity = mGravity;
         return this;
      }

      public T setOffsetX(int mOffsetX) {
         this.mOffsetX = mOffsetX;
         return this;
      }

      public T setOffsetY(int mOffsetY) {
         this.mOffsetY = mOffsetY;
         return this;
      }

      public T setAnimation(int mAnimation) {
         this.mAnimation = mAnimation;
         return this;
      }

      protected abstract D build();

      protected void clear() {
         this.mWidth = -2;
         this.mHeight = -2;
         this.mGravity = 17;
         this.mOffsetX = 0;
         this.mOffsetY = 0;
      }
   }
}
