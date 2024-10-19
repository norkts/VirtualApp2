package com.carlos.common.imagepicker.util;

import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class SelectedStateListDrawable extends StateListDrawable {
   private int mSelectionColor;

   public SelectedStateListDrawable(Drawable drawable, int selectionColor) {
      this.mSelectionColor = selectionColor;
      this.addState(new int[]{16842913}, drawable);
      this.addState(new int[0], drawable);
   }

   protected boolean onStateChange(int[] states) {
      boolean isStatePressedInArray = false;
      int[] var3 = states;
      int var4 = states.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int state = var3[var5];
         if (state == 16842913) {
            isStatePressedInArray = true;
         }
      }

      if (isStatePressedInArray) {
         super.setColorFilter(this.mSelectionColor, Mode.SRC_ATOP);
      } else {
         super.clearColorFilter();
      }

      return super.onStateChange(states);
   }

   public boolean isStateful() {
      return true;
   }
}
