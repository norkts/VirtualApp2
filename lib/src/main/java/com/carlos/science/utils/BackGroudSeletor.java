package com.carlos.science.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import com.carlos.libcommon.StringFog;
import java.io.IOException;
import java.io.InputStream;

public class BackGroudSeletor {
   static int[] PRESSED_ENABLED_STATE_SET = new int[]{16842910, 16842919};
   static int[] ENABLED_STATE_SET = new int[]{16842910};
   static int[] EMPTY_STATE_SET = new int[0];

   private BackGroudSeletor() {
   }

   public static StateListDrawable createBgByImagedrawble(String[] imagename, Context context) {
      StateListDrawable bg = new StateListDrawable();
      Drawable normal = getdrawble(imagename[0], context);
      Drawable pressed = getdrawble(imagename[1], context);
      bg.addState(PRESSED_ENABLED_STATE_SET, pressed);
      bg.addState(ENABLED_STATE_SET, normal);
      bg.addState(EMPTY_STATE_SET, normal);
      return bg;
   }

   public static StateListDrawable createBg(Drawable normal, Drawable pressed) {
      StateListDrawable bg = new StateListDrawable();
      bg.addState(PRESSED_ENABLED_STATE_SET, pressed);
      bg.addState(ENABLED_STATE_SET, normal);
      bg.addState(EMPTY_STATE_SET, normal);
      return bg;
   }

   public static ColorStateList createColorStateList(int normal, int pressed) {
      int[] colors = new int[]{pressed, pressed, normal, pressed, pressed, normal};
      int[][] states = new int[][]{{16842919, 16842910}, {16842910, 16842908}, {16842910}, {16842908}, {16842909}, new int[0]};
      ColorStateList colorList = new ColorStateList(states, colors);
      return colorList;
   }

   public static StateListDrawable createBgByImage9png(String[] imagename, Context context) {
      StateListDrawable bg = new StateListDrawable();
      NinePatchDrawable normal = get9png(imagename[0], context);
      NinePatchDrawable pressed = get9png(imagename[1], context);
      bg.addState(PRESSED_ENABLED_STATE_SET, pressed);
      bg.addState(ENABLED_STATE_SET, normal);
      bg.addState(EMPTY_STATE_SET, normal);
      return bg;
   }

   public static Bitmap getBitmap(String imagename, Context context) {
      Bitmap bitmap = null;

      try {
         String imagePath = "image/" + imagename + ".png";
         bitmap = BitmapFactory.decodeStream(context.getAssets().open(imagePath));
      } catch (IOException var4) {
         IOException e = var4;
         if (null != bitmap) {
            bitmap.recycle();
         }

         e.printStackTrace();
      }

      return bitmap;
   }

   public static Drawable getdrawble(String imagename, Context context) {
      Drawable drawable = null;
      Bitmap bitmap = null;

      try {
         String imagePath = "image/" + imagename + ".png";
         bitmap = BitmapFactory.decodeStream(context.getAssets().open(imagePath));
         BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
         drawable = bitmapDrawable;
      } catch (IOException var6) {
         IOException e = var6;
         if (null != bitmap) {
            bitmap.recycle();
         }

         e.printStackTrace();
      }

      return drawable;
   }

   public static NinePatchDrawable get9png(String imagename, Context context) {
      try {
         Bitmap toast_bitmap = BitmapFactory.decodeStream(context.getAssets().open("image/" + imagename + ".9.png"));
         byte[] temp = toast_bitmap.getNinePatchChunk();
         boolean is_nine = NinePatch.isNinePatchChunk(temp);
         if (is_nine) {
            NinePatchDrawable nine_draw = new NinePatchDrawable(context.getResources(), toast_bitmap, temp, new Rect(), (String)null);
            return nine_draw;
         }
      } catch (IOException var6) {
         IOException e = var6;
         e.printStackTrace();
      }

      return null;
   }

   public static InputStream zipPic(InputStream is) {
      return null;
   }
}
