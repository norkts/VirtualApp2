package com.carlos.common.imagepicker.photoview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

class CustomGestureDetector {
   private static final int INVALID_POINTER_ID = -1;
   private int mActivePointerId = -1;
   private int mActivePointerIndex = 0;
   private final ScaleGestureDetector mDetector;
   private VelocityTracker mVelocityTracker;
   private boolean mIsDragging;
   private float mLastTouchX;
   private float mLastTouchY;
   private final float mTouchSlop;
   private final float mMinimumVelocity;
   private OnGestureListener mListener;

   CustomGestureDetector(Context context, OnGestureListener listener) {
      ViewConfiguration configuration = ViewConfiguration.get(context);
      this.mMinimumVelocity = (float)configuration.getScaledMinimumFlingVelocity();
      this.mTouchSlop = (float)configuration.getScaledTouchSlop();
      this.mListener = listener;
      ScaleGestureDetector.OnScaleGestureListener mScaleListener = new ScaleGestureDetector.OnScaleGestureListener() {
         private float lastFocusX;
         private float lastFocusY = 0.0F;

         public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            if (!Float.isNaN(scaleFactor) && !Float.isInfinite(scaleFactor)) {
               if (scaleFactor >= 0.0F) {
                  CustomGestureDetector.this.mListener.onScale(scaleFactor, detector.getFocusX(), detector.getFocusY(), detector.getFocusX() - this.lastFocusX, detector.getFocusY() - this.lastFocusY);
                  this.lastFocusX = detector.getFocusX();
                  this.lastFocusY = detector.getFocusY();
               }

               return true;
            } else {
               return false;
            }
         }

         public boolean onScaleBegin(ScaleGestureDetector detector) {
            this.lastFocusX = detector.getFocusX();
            this.lastFocusY = detector.getFocusY();
            return true;
         }

         public void onScaleEnd(ScaleGestureDetector detector) {
         }
      };
      this.mDetector = new ScaleGestureDetector(context, mScaleListener);
   }

   private float getActiveX(MotionEvent ev) {
      try {
         return ev.getX(this.mActivePointerIndex);
      } catch (Exception var3) {
         return ev.getX();
      }
   }

   private float getActiveY(MotionEvent ev) {
      try {
         return ev.getY(this.mActivePointerIndex);
      } catch (Exception var3) {
         return ev.getY();
      }
   }

   public boolean isScaling() {
      return this.mDetector.isInProgress();
   }

   public boolean isDragging() {
      return this.mIsDragging;
   }

   public boolean onTouchEvent(MotionEvent ev) {
      try {
         this.mDetector.onTouchEvent(ev);
         return this.processTouchEvent(ev);
      } catch (IllegalArgumentException var3) {
         return true;
      }
   }

   private boolean processTouchEvent(MotionEvent ev) {
      int action = ev.getAction();
      switch (action & 255) {
         case 0:
            this.mActivePointerId = ev.getPointerId(0);
            this.mVelocityTracker = VelocityTracker.obtain();
            if (null != this.mVelocityTracker) {
               this.mVelocityTracker.addMovement(ev);
            }

            this.mLastTouchX = this.getActiveX(ev);
            this.mLastTouchY = this.getActiveY(ev);
            this.mIsDragging = false;
            break;
         case 1:
            this.mActivePointerId = -1;
            if (this.mIsDragging && null != this.mVelocityTracker) {
               this.mLastTouchX = this.getActiveX(ev);
               this.mLastTouchY = this.getActiveY(ev);
               this.mVelocityTracker.addMovement(ev);
               this.mVelocityTracker.computeCurrentVelocity(1000);
               float vX = this.mVelocityTracker.getXVelocity();
               float vY = this.mVelocityTracker.getYVelocity();
               if (Math.max(Math.abs(vX), Math.abs(vY)) >= this.mMinimumVelocity) {
                  this.mListener.onFling(this.mLastTouchX, this.mLastTouchY, -vX, -vY);
               }
            }

            if (null != this.mVelocityTracker) {
               this.mVelocityTracker.recycle();
               this.mVelocityTracker = null;
            }
            break;
         case 2:
            float x = this.getActiveX(ev);
            float y = this.getActiveY(ev);
            float dx = x - this.mLastTouchX;
            float dy = y - this.mLastTouchY;
            if (!this.mIsDragging) {
               this.mIsDragging = Math.sqrt((double)(dx * dx + dy * dy)) >= (double)this.mTouchSlop;
            }

            if (this.mIsDragging) {
               this.mListener.onDrag(dx, dy);
               this.mLastTouchX = x;
               this.mLastTouchY = y;
               if (null != this.mVelocityTracker) {
                  this.mVelocityTracker.addMovement(ev);
               }
            }
            break;
         case 3:
            this.mActivePointerId = -1;
            if (null != this.mVelocityTracker) {
               this.mVelocityTracker.recycle();
               this.mVelocityTracker = null;
            }
         case 4:
         case 5:
         default:
            break;
         case 6:
            int pointerIndex = Util.getPointerIndex(ev.getAction());
            int pointerId = ev.getPointerId(pointerIndex);
            if (pointerId == this.mActivePointerId) {
               int newPointerIndex = pointerIndex == 0 ? 1 : 0;
               this.mActivePointerId = ev.getPointerId(newPointerIndex);
               this.mLastTouchX = ev.getX(newPointerIndex);
               this.mLastTouchY = ev.getY(newPointerIndex);
            }
      }

      this.mActivePointerIndex = ev.findPointerIndex(this.mActivePointerId != -1 ? this.mActivePointerId : 0);
      return true;
   }
}
