package com.carlos.common.imagepicker.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.carlos.common.imagepicker.entity.AspectRatio;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R.color;
import com.kook.librelease.R.dimen;
import com.kook.librelease.R.styleable;
import java.util.Locale;

public class AspectRatioTextView extends TextView {
   private final Rect mCanvasClipBounds;
   private Paint mDotPaint;
   private int mDotSize;
   private float mAspectRatio;
   private String mAspectRatioTitle;
   private float mAspectRatioX;
   private float mAspectRatioY;

   public AspectRatioTextView(Context context) {
      this(context, (AttributeSet)null);
   }

   public AspectRatioTextView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public AspectRatioTextView(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      this.mCanvasClipBounds = new Rect();
      TypedArray a = context.obtainStyledAttributes(attrs, styleable.ucrop_AspectRatioTextView);
      this.init(a);
   }

   @TargetApi(21)
   public AspectRatioTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super(context, attrs, defStyleAttr, defStyleRes);
      this.mCanvasClipBounds = new Rect();
      TypedArray a = context.obtainStyledAttributes(attrs, styleable.ucrop_AspectRatioTextView);
      this.init(a);
   }

   public void setActiveColor(@ColorInt int activeColor) {
      this.applyActiveColor(activeColor);
      this.invalidate();
   }

   public void setAspectRatio(@NonNull AspectRatio aspectRatio) {
      this.mAspectRatioTitle = aspectRatio.getAspectRatioTitle();
      this.mAspectRatioX = aspectRatio.getAspectRatioX();
      this.mAspectRatioY = aspectRatio.getAspectRatioY();
      if (this.mAspectRatioX != 0.0F && this.mAspectRatioY != 0.0F) {
         this.mAspectRatio = this.mAspectRatioX / this.mAspectRatioY;
      } else {
         this.mAspectRatio = 0.0F;
      }

      this.setTitle();
   }

   public float getAspectRatio(boolean toggleRatio) {
      if (toggleRatio) {
         this.toggleAspectRatio();
         this.setTitle();
      }

      return this.mAspectRatio;
   }

   protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      if (this.isSelected()) {
         canvas.getClipBounds(this.mCanvasClipBounds);
         canvas.drawCircle((float)(this.mCanvasClipBounds.right - this.mCanvasClipBounds.left) / 2.0F, (float)(this.mCanvasClipBounds.bottom - this.mDotSize), (float)(this.mDotSize / 2), this.mDotPaint);
      }

   }

   private void init(@NonNull TypedArray a) {
      this.setGravity(1);
      this.mAspectRatioTitle = a.getString(styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_title);
      this.mAspectRatioX = a.getFloat(styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_x, 0.0F);
      this.mAspectRatioY = a.getFloat(styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_y, 0.0F);
      if (this.mAspectRatioX != 0.0F && this.mAspectRatioY != 0.0F) {
         this.mAspectRatio = this.mAspectRatioX / this.mAspectRatioY;
      } else {
         this.mAspectRatio = 0.0F;
      }

      this.mDotSize = this.getContext().getResources().getDimensionPixelSize(dimen.ucrop_size_dot_scale_text_view);
      this.mDotPaint = new Paint(1);
      this.mDotPaint.setStyle(Style.FILL);
      this.setTitle();
      int activeColor = this.getResources().getColor(color.ucrop_color_widget_active);
      this.applyActiveColor(activeColor);
      a.recycle();
   }

   private void applyActiveColor(@ColorInt int activeColor) {
      if (this.mDotPaint != null) {
         this.mDotPaint.setColor(activeColor);
      }

      ColorStateList textViewColorStateList = new ColorStateList(new int[][]{{16842913}, {0}}, new int[]{activeColor, ContextCompat.getColor(this.getContext(), color.ucrop_color_widget)});
      this.setTextColor(textViewColorStateList);
   }

   private void toggleAspectRatio() {
      if (this.mAspectRatio != 0.0F) {
         float tempRatioW = this.mAspectRatioX;
         this.mAspectRatioX = this.mAspectRatioY;
         this.mAspectRatioY = tempRatioW;
         this.mAspectRatio = this.mAspectRatioX / this.mAspectRatioY;
      }

   }

   private void setTitle() {
      if (!TextUtils.isEmpty(this.mAspectRatioTitle)) {
         this.setText(this.mAspectRatioTitle);
      } else {
         this.setText(String.format(Locale.US, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PQgpIngVMFo=")), (int)this.mAspectRatioX, (int)this.mAspectRatioY));
      }

   }
}
