package com.carlos.common.imagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import com.carlos.common.imagepicker.utils.PermissionsUtils;
import com.carlos.libcommon.StringFog;
import java.io.Serializable;
import java.util.ArrayList;

public class PhotoSelector {
   public static final int CROP_RECTANG = 1;
   public static final int CROP_CIRCLE = 2;
   public static final int DEFAULT_MAX_SELECTED_COUNT = 9;
   public static final int DEFAULT_GRID_COLUMN = 3;
   public static final int DEFAULT_REQUEST_CODE = 999;
   public static final int RESULT_CODE = 1000;
   public static final int TAKE_PHOTO_CROP_REQUESTCODE = 1001;
   public static final int TAKE_PHOTO_REQUESTCODE = 1002;
   public static final int CROP_REQUESTCODE = 1003;
   public static final String SELECT_RESULT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uDmgVLAZsJyw/Iy4MCGUzSFo="));
   public static final String EXTRA_MAX_SELECTED_COUNT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+IGYwLCtgHjA5LBcMPmMFAiVvARo/"));
   public static final String EXTRA_GRID_COLUMN = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADmwVEiY="));
   public static final String EXTRA_SHOW_CAMERA = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki5fD2w2Gil9Dl0/Iz0iVg=="));
   public static final String EXTRA_SELECTED_IMAGES = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uDmgVLAZiDgpAKQdXOWkFGgM="));
   public static final String EXTRA_SINGLE = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YCGgzHis="));
   public static final String EXTRA_CROP = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2H2swFiVhEVRF"));
   public static final String EXTRA_CROP_MODE = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0MD28IGiNgJAo/"));
   public static final String EXTRA_MATERIAL_DESIGN = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+LGgaFi99DlFAKBcML2wjEiY="));
   public static final String EXTRA_TOOLBARCOLOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgAD2oLFjdhMig1KhdfKA=="));
   public static final String EXTRA_BOTTOMBARCOLOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4ALGwFGiNlNCAqJy1fCG8KRVo="));
   public static final String EXTRA_STATUSBARCOLOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wKNANlNCAqJy1fCG8KRVo="));
   public static final String EXTRA_POSITION = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAKWUaMC9gJFlF"));
   public static final String EXTRA_ISPREVIEW = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2Am8jND5jDjAt"));
   public static final String IS_CONFIRM = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2H2szGiZiNAYqKghSVg=="));

   public static Uri getCropImageUri(@NonNull Intent intent) {
      return UCrop.getOutput(intent);
   }

   public static PhotoSelectorBuilder builder() {
      return new PhotoSelectorBuilder();
   }

   public static class PhotoSelectorBuilder {
      private Bundle mPickerOptionsBundle = new Bundle();
      private Intent mPickerIntent = new Intent();

      PhotoSelectorBuilder() {
      }

      public void start(@NonNull Activity activity, int requestCode) {
         if (PermissionsUtils.checkReadStoragePermission(activity)) {
            activity.startActivityForResult(this.getIntent(activity), requestCode);
         }

      }

      public Intent getIntent(@NonNull Context context) {
         this.mPickerIntent.setClass(context, ImageSelectorActivity.class);
         this.mPickerIntent.putExtras(this.mPickerOptionsBundle);
         return this.mPickerIntent;
      }

      public PhotoSelectorBuilder setIntentExtras(String extrasKey, Serializable values) {
         this.mPickerIntent.putExtra(extrasKey, values);
         return this;
      }

      public void start(@NonNull Activity activity) {
         this.start(activity, 999);
      }

      public PhotoSelectorBuilder setMaxSelectCount(int maxSelectCount) {
         this.mPickerOptionsBundle.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+IGYwLCtgHjA5LBcMPmMFAiVvARo/")), maxSelectCount);
         return this;
      }

      public PhotoSelectorBuilder setSingle(boolean isSingle) {
         this.mPickerOptionsBundle.putBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YCGgzHis=")), isSingle);
         return this;
      }

      public PhotoSelectorBuilder setGridColumnCount(int columnCount) {
         this.mPickerOptionsBundle.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADmwVEiY=")), columnCount);
         return this;
      }

      public PhotoSelectorBuilder setShowCamera(boolean showCamera) {
         this.mPickerOptionsBundle.putBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki5fD2w2Gil9Dl0/Iz0iVg==")), showCamera);
         return this;
      }

      public PhotoSelectorBuilder setSelected(ArrayList<String> selected) {
         this.mPickerOptionsBundle.putStringArrayList(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uDmgVLAZiDgpAKQdXOWkFGgM=")), selected);
         return this;
      }

      public PhotoSelectorBuilder setToolBarColor(@ColorInt int toolBarColor) {
         this.mPickerOptionsBundle.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgAD2oLFjdhMig1KhdfKA==")), toolBarColor);
         return this;
      }

      public PhotoSelectorBuilder setBottomBarColor(@ColorInt int bottomBarColor) {
         this.mPickerOptionsBundle.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4ALGwFGiNlNCAqJy1fCG8KRVo=")), bottomBarColor);
         return this;
      }

      public PhotoSelectorBuilder setStatusBarColor(@ColorInt int statusBarColor) {
         this.mPickerOptionsBundle.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wKNANlNCAqJy1fCG8KRVo=")), statusBarColor);
         return this;
      }

      public PhotoSelectorBuilder setMaterialDesign(boolean materialDesign) {
         this.mPickerOptionsBundle.putBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+LGgaFi99DlFAKBcML2wjEiY=")), materialDesign);
         return this;
      }

      public PhotoSelectorBuilder setCrop(boolean isCrop) {
         this.mPickerOptionsBundle.putBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2H2swFiVhEVRF")), isCrop);
         return this;
      }

      public PhotoSelectorBuilder setCropMode(int mode) {
         this.mPickerOptionsBundle.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0MD28IGiNgJAo/")), mode);
         return this;
      }
   }
}
