package com.carlos.common.widget.tablayout.listener;

import androidx.annotation.DrawableRes;

public interface CustomTabEntity {
   String getTabTitle();

   @DrawableRes
   int getTabSelectedIcon();

   @DrawableRes
   int getTabUnselectedIcon();
}
