package com.carlos.common.imagepicker.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import com.carlos.common.imagepicker.util.RotationGestureDetector;

public class GestureCropImageView extends CropImageView {
   private static final int DOUBLE_TAP_ZOOM_DURATION = 200;
   private ScaleGestureDetector mScaleDetector;
   private RotationGestureDetector mRotateDetector;
   private GestureDetector mGestureDetector;
   private float mMidPntX;
   private float mMidPntY;
   private boolean mIsRotateEnabled;
   private boolean mIsScaleEnabled;
   private int mDoubleTapScaleSteps;

   public GestureCropImageView(Context context) {
      super(context);
      this.mIsRotateEnabled = true;
      this.mIsScaleEnabled = true;
      this.mDoubleTapScaleSteps = 5;
   }

   public GestureCropImageView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public GestureCropImageView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      this.mIsRotateEnabled = true;
      this.mIsScaleEnabled = true;
      this.mDoubleTapScaleSteps = 5;
   }

   public void setScaleEnabled(boolean scaleEnabled) {
      this.mIsScaleEnabled = scaleEnabled;
   }

   public boolean isScaleEnabled() {
      return this.mIsScaleEnabled;
   }

   public void setRotateEnabled(boolean rotateEnabled) {
      this.mIsRotateEnabled = rotateEnabled;
   }

   public boolean isRotateEnabled() {
      return this.mIsRotateEnabled;
   }

   public void setDoubleTapScaleSteps(int doubleTapScaleSteps) {
      this.mDoubleTapScaleSteps = doubleTapScaleSteps;
   }

   public int getDoubleTapScaleSteps() {
      return this.mDoubleTapScaleSteps;
   }

   public boolean onTouchEvent(MotionEvent event) {
      if ((event.getAction() & 255) == 0) {
         this.cancelAllAnimations();
      }

      if (event.getPointerCount() > 1) {
         this.mMidPntX = (event.getX(0) + event.getX(1)) / 2.0F;
         this.mMidPntY = (event.getY(0) + event.getY(1)) / 2.0F;
      }

      this.mGestureDetector.onTouchEvent(event);
      if (this.mIsScaleEnabled) {
         this.mScaleDetector.onTouchEvent(event);
      }

      if (this.mIsRotateEnabled) {
         this.mRotateDetector.onTouchEvent(event);
      }

      if ((event.getAction() & 255) == 1) {
         this.setImageToWrapCropBounds();
      }

      return true;
   }

   protected void init() {
      super.init();
      this.setupGestureListeners();
   }

   protected float getDoubleTapTargetScale() {
      return this.getCurrentScale() * (float)Math.pow((double)(this.getMaxScale() / this.getMinScale()), (double)(1.0F / (float)this.mDoubleTapScaleSteps));
   }

   private void setupGestureListeners() {
      this.mGestureDetector = new GestureDetector(this.getContext(), new GestureListener(), (Handler)null, true);
      this.mScaleDetector = new ScaleGestureDetector(this.getContext(), new ScaleListener());
      this.mRotateDetector = new RotationGestureDetector(new RotateListener());
   }

   private class RotateListener extends RotationGestureDetector.SimpleOnRotationGestureListener {
      private RotateListener() {
      }

      public boolean onRotation(RotationGestureDetector rotationDetector) {
         GestureCropImageView.this.postRotate(rotationDetector.getAngle(), GestureCropImageView.this.mMidPntX, GestureCropImageView.this.mMidPntY);
         return true;
      }

      // $FF: synthetic method
      RotateListener(Object x1) {
         this();
      }
   }

   private class GestureListener extends GestureDetector.SimpleOnGestureListener {
      private GestureListener() {
      }

      public boolean onDoubleTap(MotionEvent e) {
         GestureCropImageView.this.zoomImageToPosition(GestureCropImageView.this.getDoubleTapTargetScale(), e.getX(), e.getY(), 200L);
         return super.onDoubleTap(e);
      }

      public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
         GestureCropImageView.this.postTranslate(-distanceX, -distanceY);
         return true;
      }

      // $FF: synthetic method
      GestureListener(Object x1) {
         this();
      }
   }

   private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
      private ScaleListener() {
      }

      public boolean onScale(ScaleGestureDetector detector) {
         GestureCropImageView.this.postScale(detector.getScaleFactor(), GestureCropImageView.this.mMidPntX, GestureCropImageView.this.mMidPntY);
         return true;
      }

      // $FF: synthetic method
      ScaleListener(Object x1) {
         this();
      }
   }
}
