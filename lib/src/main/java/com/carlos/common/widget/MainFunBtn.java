package com.carlos.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.kook.librelease.R.id;

public class MainFunBtn extends RelativeLayout {
   TextView tv_top;
   TextView tv_buttom;

   public MainFunBtn(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   protected void onFinishInflate() {
      super.onFinishInflate();
      this.tv_top = (TextView)this.findViewById(id.tv_top);
      this.tv_buttom = (TextView)this.findViewById(id.tv_buttom);
   }

   public void setTopText(String text) {
      if (this.tv_top != null) {
         this.tv_top.setText(text);
      }

   }

   public void setButtomText(String text) {
      if (this.tv_buttom != null) {
         this.tv_buttom.setText(text);
      }

   }
}
