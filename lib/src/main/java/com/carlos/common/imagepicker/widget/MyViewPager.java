package com.carlos.common.imagepicker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {
   public MyViewPager(Context context) {
      super(context);
   }

   public MyViewPager(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   public boolean onInterceptTouchEvent(MotionEvent ev) {
      try {
         return super.onInterceptTouchEvent(ev);
      } catch (IllegalArgumentException var3) {
         return false;
      }
   }
}
