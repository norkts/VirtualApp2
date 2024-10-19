package com.carlos.science.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import com.kook.librelease.R.id;

public class CheckLayout extends LinearLayout {
   Switch aSwitch;
   TextView textview;

   public CheckLayout(Context context, @Nullable AttributeSet attrs) {
      super(context, attrs);
   }

   protected void onFinishInflate() {
      super.onFinishInflate();
      this.textview = (TextView)this.findViewById(id.textview);
      this.aSwitch = (Switch)this.findViewById(id.aSwitch);
   }

   @SuppressLint({"ResourceType"})
   public CheckLayout setText(@IdRes int res) {
      this.textview.setText(res);
      return this;
   }

   public CheckLayout setText(String text) {
      this.textview.setText(text);
      return this;
   }

   public CheckLayout setTextId(int resId) {
      this.textview.setId(resId);
      return this;
   }

   public CheckLayout setSwitchId(int resId) {
      this.aSwitch.setId(resId);
      return this;
   }

   public int getSwitchId() {
      int id = this.aSwitch.getId();
      return id;
   }

   public CheckLayout setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
      this.aSwitch.setOnCheckedChangeListener(listener);
      return this;
   }
}
