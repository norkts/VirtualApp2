package com.carlos.common.imagepicker.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class SquareImageView extends AppCompatImageView {
   public SquareImageView(Context context) {
      super(context);
   }

   public SquareImageView(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
   }

   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      super.onMeasure(widthMeasureSpec, widthMeasureSpec);
   }
}
