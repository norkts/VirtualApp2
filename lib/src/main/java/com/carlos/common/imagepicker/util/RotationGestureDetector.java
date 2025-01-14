package com.carlos.common.imagepicker.util;

import android.view.MotionEvent;
import androidx.annotation.NonNull;

public class RotationGestureDetector {
   private static final int INVALID_POINTER_INDEX = -1;
   private float fX;
   private float fY;
   private float sX;
   private float sY;
   private int mPointerIndex1;
   private int mPointerIndex2;
   private float mAngle;
   private boolean mIsFirstTouch;
   private OnRotationGestureListener mListener;

   public RotationGestureDetector(OnRotationGestureListener listener) {
      this.mListener = listener;
      this.mPointerIndex1 = -1;
      this.mPointerIndex2 = -1;
   }

   public float getAngle() {
      return this.mAngle;
   }

   public boolean onTouchEvent(@NonNull MotionEvent event) {
      switch (event.getActionMasked()) {
         case 0:
            this.sX = event.getX();
            this.sY = event.getY();
            this.mPointerIndex1 = event.findPointerIndex(event.getPointerId(0));
            this.mAngle = 0.0F;
            this.mIsFirstTouch = true;
            break;
         case 1:
            this.mPointerIndex1 = -1;
            break;
         case 2:
            if (this.mPointerIndex1 != -1 && this.mPointerIndex2 != -1 && event.getPointerCount() > this.mPointerIndex2) {
               float nsX = event.getX(this.mPointerIndex1);
               float nsY = event.getY(this.mPointerIndex1);
               float nfX = event.getX(this.mPointerIndex2);
               float nfY = event.getY(this.mPointerIndex2);
               if (this.mIsFirstTouch) {
                  this.mAngle = 0.0F;
                  this.mIsFirstTouch = false;
               } else {
                  this.calculateAngleBetweenLines(this.fX, this.fY, this.sX, this.sY, nfX, nfY, nsX, nsY);
               }

               if (this.mListener != null) {
                  this.mListener.onRotation(this);
               }

               this.fX = nfX;
               this.fY = nfY;
               this.sX = nsX;
               this.sY = nsY;
            }
         case 3:
         case 4:
         default:
            break;
         case 5:
            this.fX = event.getX();
            this.fY = event.getY();
            this.mPointerIndex2 = event.findPointerIndex(event.getPointerId(event.getActionIndex()));
            this.mAngle = 0.0F;
            this.mIsFirstTouch = true;
            break;
         case 6:
            this.mPointerIndex2 = -1;
      }

      return true;
   }

   private float calculateAngleBetweenLines(float fx1, float fy1, float fx2, float fy2, float sx1, float sy1, float sx2, float sy2) {
      return this.calculateAngleDelta((float)Math.toDegrees((double)((float)Math.atan2((double)(fy1 - fy2), (double)(fx1 - fx2)))), (float)Math.toDegrees((double)((float)Math.atan2((double)(sy1 - sy2), (double)(sx1 - sx2)))));
   }

   private float calculateAngleDelta(float angleFrom, float angleTo) {
      this.mAngle = angleTo % 360.0F - angleFrom % 360.0F;
      if (this.mAngle < -180.0F) {
         this.mAngle += 360.0F;
      } else if (this.mAngle > 180.0F) {
         this.mAngle -= 360.0F;
      }

      return this.mAngle;
   }

   public interface OnRotationGestureListener {
      boolean onRotation(RotationGestureDetector var1);
   }

   public static class SimpleOnRotationGestureListener implements OnRotationGestureListener {
      public boolean onRotation(RotationGestureDetector rotationDetector) {
         return false;
      }
   }
}
