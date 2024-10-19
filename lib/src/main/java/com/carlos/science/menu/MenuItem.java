package com.carlos.science.menu;

import android.graphics.drawable.Drawable;

public abstract class MenuItem {
   public Drawable mDrawable;

   public MenuItem(Drawable drawable) {
      this.mDrawable = drawable;
   }

   public abstract void action();
}
