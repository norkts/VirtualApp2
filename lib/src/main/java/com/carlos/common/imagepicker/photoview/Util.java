package com.carlos.common.imagepicker.photoview;

import android.widget.ImageView;
import com.carlos.libcommon.StringFog;

class Util {
   static void checkZoomLevels(float minZoom, float midZoom, float maxZoom) {
      if (minZoom >= midZoom) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwgYCGUVEgVgCiQiKi1fD34zFjdsIzw/LDo6JmIOODduASw8OD0cLGgFATRiESgzJQg2JHkVEgFvASM5PhY2P2oFGShhJDAgIgccDmwjPAVlDFEcLD4HLEkOODJvATAhOD5aJGUFGiZoVjwoJxgiOWwjPCRsJ10ZLQQ6LmsVHgViAVRF")));
      } else if (midZoom >= maxZoom) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwguPGUaNCNLEQI1Ki1WOmwzQQN4HiwcPQgMJ0saTSBlJy8pIz4MO2U3IEhrHlkwJgg2JHkVEgFvASM5PhY2P2oFGShhJDAgIgciImwjPAVlDFEcLD4HLEkOODJvATAhOD5aJGUFGiZoVjwoJxgiOWwjPCRsJ10ZLQQ6LmsVHgViAVRF")));
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
               throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Owg+LG8jAjBLESg5LwdbPX4wBj9sESsrIxc1JGAwAj95ESw+LD1XKWwwMD9oAVRF")));
            default:
               return true;
         }
      }
   }

   static int getPointerIndex(int action) {
      return (action & '\uff00') >> 8;
   }
}
