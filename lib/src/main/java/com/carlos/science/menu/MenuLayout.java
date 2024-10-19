package com.carlos.science.menu;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import com.carlos.libcommon.StringFog;
import com.carlos.science.runner.ICarrier;
import com.carlos.science.runner.ScrollRunner;
import com.carlos.science.utils.DensityUtil;

public class MenuLayout extends ViewGroup implements ICarrier {
   String TAG;
   private int mChildSize;
   private int mChildPadding;
   private float mFromDegrees;
   private float mToDegrees;
   private static int MIN_RADIUS;
   private int mRadius;
   private boolean mExpanded;
   private boolean isMoving;
   private int position;
   private int centerX;
   private int centerY;
   private ScrollRunner mRunner;

   public void computeCenterXY(int position) {
      int size = this.getLayoutSize();
      int width = size;
      int height = size;
      switch (position) {
         case 1:
            this.centerX = width / 2 - this.getRadiusAndPadding();
            this.centerY = height / 2 - this.getRadiusAndPadding();
            break;
         case 2:
            this.centerX = width / 2;
            this.centerY = height / 2 - this.getRadiusAndPadding();
            break;
         case 3:
            this.centerX = width / 2 + this.getRadiusAndPadding();
            this.centerY = height / 2 - this.getRadiusAndPadding();
            break;
         case 4:
            this.centerX = width / 2 - this.getRadiusAndPadding();
            this.centerY = height / 2;
            break;
         case 5:
            this.centerX = width / 2;
            this.centerY = width / 2;
            break;
         case 6:
            this.centerX = width / 2 + this.getRadiusAndPadding();
            this.centerY = height / 2;
            break;
         case 7:
            this.centerX = width / 2 - this.getRadiusAndPadding();
            this.centerY = height / 2 + this.getRadiusAndPadding();
            break;
         case 8:
            this.centerX = width / 2;
            this.centerY = height / 2 + this.getRadiusAndPadding();
            break;
         case 9:
            this.centerX = width / 2 + this.getRadiusAndPadding();
            this.centerY = height / 2 + this.getRadiusAndPadding();
      }

   }

   private int getRadiusAndPadding() {
      return this.mRadius + this.mChildPadding * 2;
   }

   public MenuLayout(Context context) {
      this(context, (AttributeSet)null);
   }

   public MenuLayout(Context context, AttributeSet attrs) {
      super(context, attrs);
      this.TAG = StringFog.decrypt("PgAcAykPJhwWGw==");
      this.mChildPadding = 5;
      this.mExpanded = false;
      this.isMoving = false;
      this.position = 1;
      this.centerX = 0;
      this.centerY = 0;
      MIN_RADIUS = DensityUtil.dip2px(context, 65.0F);
      this.mRunner = new ScrollRunner(this);
      this.setChildrenDrawingOrderEnabled(true);
   }

   private static int computeRadius(float arcDegrees, int childCount, int childSize, int childPadding, int minRadius) {
      if (childCount < 2) {
         return minRadius;
      } else {
         float perDegrees = arcDegrees == 360.0F ? arcDegrees / (float)childCount : arcDegrees / (float)(childCount - 1);
         float perHalfDegrees = perDegrees / 2.0F;
         int perSize = childSize + childPadding;
         int radius = (int)((double)(perSize / 2) / Math.sin(Math.toRadians((double)perHalfDegrees)));
         return Math.max(radius, minRadius);
      }
   }

   private static Rect computeChildFrame(int centerX, int centerY, int radius, float degrees, int size) {
      double childCenterX = (double)centerX + (double)radius * Math.cos(Math.toRadians((double)degrees));
      double childCenterY = (double)centerY + (double)radius * Math.sin(Math.toRadians((double)degrees));
      return new Rect((int)(childCenterX - (double)(size / 2)), (int)(childCenterY - (double)(size / 2)), (int)(childCenterX + (double)(size / 2)), (int)(childCenterY + (double)(size / 2)));
   }

   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      int size = this.getLayoutSize();
      this.setMeasuredDimension(size, size);
      int count = this.getChildCount();

      for(int i = 0; i < count; ++i) {
         this.getChildAt(i).measure(MeasureSpec.makeMeasureSpec(this.mChildSize, 1073741824), MeasureSpec.makeMeasureSpec(this.mChildSize, 1073741824));
      }

   }

   private int getLayoutSize() {
      this.mRadius = computeRadius(Math.abs(this.mToDegrees - this.mFromDegrees), this.getChildCount(), this.mChildSize, this.mChildPadding, MIN_RADIUS);
      int layoutPadding = 10;
      return this.mRadius * 2 + this.mChildSize + this.mChildPadding + layoutPadding * 2;
   }

   protected void onLayout(boolean changed, int l, int t, int r, int b) {
      if (!this.isMoving) {
         this.computeCenterXY(this.position);
//         int radius = false;
         this.layoutItem(0);
      }
   }

   protected int getChildDrawingOrder(int childCount, int i) {
      return !this.isLeft() ? childCount - i - 1 : i;
   }

   private boolean isLeft() {
      int corner = (int)(this.mFromDegrees / 90.0F);
      return corner == 0 || corner == 3;
   }

   private void layoutItem(int radius) {
      int childCount = this.getChildCount();
      float degrees = this.mFromDegrees;
      float arcDegrees = Math.abs(this.mToDegrees - this.mFromDegrees);
      float perDegrees;
      if (childCount == 1) {
         perDegrees = arcDegrees / (float)(childCount + 1);
         degrees += perDegrees;
      } else if (childCount == 2) {
         if (arcDegrees == 90.0F) {
            perDegrees = arcDegrees / (float)(childCount - 1);
         } else {
            perDegrees = arcDegrees / (float)(childCount + 1);
            degrees += perDegrees;
         }
      } else {
         perDegrees = arcDegrees == 360.0F ? arcDegrees / (float)childCount : arcDegrees / (float)(childCount - 1);
      }

      for(int i = 0; i < childCount; ++i) {
         int index = this.getChildDrawingOrder(childCount, i);
         Rect frame = computeChildFrame(this.centerX, this.centerY, radius, degrees, this.mChildSize);
         degrees += perDegrees;
         this.getChildAt(index).layout(frame.left, frame.top, frame.right, frame.bottom);
      }

   }

   public void requestLayout() {
      if (!this.isMoving) {
         super.requestLayout();
      }

   }

   public void switchState(int position, int duration) {
      this.position = position;
      this.mExpanded = !this.mExpanded;
      this.isMoving = true;
      this.mRadius = computeRadius(Math.abs(this.mToDegrees - this.mFromDegrees), this.getChildCount(), this.mChildSize, this.mChildPadding, MIN_RADIUS);
      int start = this.mExpanded ? 0 : this.mRadius;
      int radius = this.mExpanded ? this.mRadius : -this.mRadius;
      this.mRunner.start(start, 0, radius, 0, duration);
   }

   public boolean isMoving() {
      return this.isMoving;
   }

   public void onMove(int lastX, int lastY, int curX, int curY) {
      this.layoutItem(curX);
   }

   public void onDone() {
      Log.v(this.TAG, StringFog.decrypt("HAs2GQsL"));
      this.isMoving = false;
      if (!this.mExpanded) {
         FloatMenu floatMenu = (FloatMenu)this.getParent();
         floatMenu.remove();
      }

   }

   public boolean isExpanded() {
      return this.mExpanded;
   }

   public void setArc(float fromDegrees, float toDegrees, int position) {
      Log.d(this.TAG, StringFog.decrypt("AAAGNxcNf5vN0Zfe84rS1IDI0A=="));
      this.position = position;
      if (this.mFromDegrees != fromDegrees || this.mToDegrees != toDegrees) {
         this.mFromDegrees = fromDegrees;
         this.mToDegrees = toDegrees;
         this.computeCenterXY(position);
         this.requestLayout();
      }
   }

   public void setArc(float fromDegrees, float toDegrees) {
      this.setArc(fromDegrees, toDegrees, this.position);
   }

   public void setChildSize(int size) {
      this.mChildSize = size;
   }

   public int getChildSize() {
      return this.mChildSize;
   }

   public void setExpand(boolean expand) {
      this.mExpanded = expand;
   }
}
