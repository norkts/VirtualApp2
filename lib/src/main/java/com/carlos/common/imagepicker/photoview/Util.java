package com.carlos.common.imagepicker.photoview;

import android.widget.ImageView;
import com.carlos.libcommon.StringFog;

class Util {
   static void checkZoomLevels(float minZoom, float midZoom, float maxZoom) {
      if (minZoom >= midZoom) {
         throw new IllegalArgumentException("Minimum zoom has to be less than Medium zoom. Call setMinimumZoom() with a more appropriate value");
      } else if (midZoom >= maxZoom) {
         throw new IllegalArgumentException("Medium zoom has to be less than Maximum zoom. Call setMaximumZoom() with a more appropriate value");
      }
   }

   static boolean hasDrawable(ImageView imageView) {
      return imageView.getDrawable() != null;
   }

   static boolean isSupportedScaleType(ImageView.ScaleType scaleType) {
      if (scaleType == null) {
         return false;
      } else {
         switch (scaleType) {
            case MATRIX:
               throw new IllegalStateException("Matrix scale type is not supported");
            default:
               return true;
         }
      }
   }

   static int getPointerIndex(int action) {
      return (action & '\uff00') >> 8;
   }
}
