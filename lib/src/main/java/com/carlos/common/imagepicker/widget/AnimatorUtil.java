package com.carlos.common.imagepicker.widget;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

public class AnimatorUtil {
   private static LinearOutSlowInInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();
   private static AccelerateInterpolator LINER_INTERPOLATOR = new AccelerateInterpolator();

   public static void scaleShow(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
      view.setVisibility(0);
      ViewCompat.animate(view).scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setDuration(800L).setListener(viewPropertyAnimatorListener).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).start();
   }

   public static void scaleHide(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
      ViewCompat.animate(view).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setDuration(800L).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(viewPropertyAnimatorListener).start();
   }

   public static void translateShow(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
      view.setVisibility(0);
      ViewCompat.animate(view).translationY(0.0F).setDuration(400L).setListener(viewPropertyAnimatorListener).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).start();
   }

   public static void translateHide(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
      view.setVisibility(0);
      ViewCompat.animate(view).translationY(260.0F).setDuration(400L).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(viewPropertyAnimatorListener).start();
   }
}
