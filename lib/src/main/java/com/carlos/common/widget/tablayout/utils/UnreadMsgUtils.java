package com.carlos.common.widget.tablayout.utils;

import android.util.DisplayMetrics;
import android.widget.RelativeLayout;
import com.carlos.common.widget.tablayout.widget.MsgView;
import com.carlos.libcommon.StringFog;

public class UnreadMsgUtils {
   public static void show(MsgView msgView, int num) {
      if (msgView != null) {
         RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)msgView.getLayoutParams();
         DisplayMetrics dm = msgView.getResources().getDisplayMetrics();
         msgView.setVisibility(0);
         if (num <= 0) {
            msgView.setStrokeWidth(0);
            msgView.setText("");
            lp.width = (int)(5.0F * dm.density);
            lp.height = (int)(5.0F * dm.density);
            msgView.setLayoutParams(lp);
         } else {
            lp.height = (int)(18.0F * dm.density);
            if (num > 0 && num < 10) {
               lp.width = (int)(18.0F * dm.density);
               msgView.setText(num + "");
            } else if (num > 9 && num < 100) {
               lp.width = -2;
               msgView.setPadding((int)(6.0F * dm.density), 0, (int)(6.0F * dm.density), 0);
               msgView.setText(num + "");
            } else {
               lp.width = -2;
               msgView.setPadding((int)(6.0F * dm.density), 0, (int)(6.0F * dm.density), 0);
               msgView.setText("99+");
            }

            msgView.setLayoutParams(lp);
         }

      }
   }

   public static void setSize(MsgView rtv, int size) {
      if (rtv != null) {
         RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)rtv.getLayoutParams();
         lp.width = size;
         lp.height = size;
         rtv.setLayoutParams(lp);
      }
   }
}
