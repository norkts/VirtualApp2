package com.carlos.common.imagepicker;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.carlos.common.imagepicker.callback.BitmapCropCallback;
import com.carlos.common.imagepicker.entity.AspectRatio;
import com.carlos.common.imagepicker.util.SelectedStateListDrawable;
import com.carlos.common.imagepicker.view.GestureCropImageView;
import com.carlos.common.imagepicker.view.OverlayView;
import com.carlos.common.imagepicker.view.TransformImageView;
import com.carlos.common.imagepicker.view.UCropView;
import com.carlos.common.imagepicker.view.widget.AspectRatioTextView;
import com.carlos.common.imagepicker.view.widget.HorizontalProgressWheelView;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R;
import com.kook.librelease.R.color;
import com.kook.librelease.R.dimen;
import com.kook.librelease.R.drawable;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.menu;
import com.kook.librelease.R.string;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class UCropActivity extends AppCompatActivity {
   public static final int DEFAULT_COMPRESS_QUALITY = 90;
   public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT;
   public static final int NONE = 0;
   public static final int SCALE = 1;
   public static final int ROTATE = 2;
   public static final int ALL = 3;
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQY2KmowIBF9JwozLD0cLmgjSFo="));
   private static final int TABS_COUNT = 3;
   private static final int SCALE_WIDGET_SENSITIVITY_COEFFICIENT = 15000;
   private static final int ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;
   private String mToolbarTitle;
   private int mToolbarColor;
   private int mStatusBarColor;
   private int mActiveWidgetColor;
   private int mToolbarWidgetColor;
   @ColorInt
   private int mRootViewBackgroundColor;
   @DrawableRes
   private int mToolbarCancelDrawable;
   @DrawableRes
   private int mToolbarCropDrawable;
   private int mLogoColor;
   private boolean mShowBottomControls;
   private boolean mShowLoader = true;
   private UCropView mUCropView;
   private GestureCropImageView mGestureCropImageView;
   private OverlayView mOverlayView;
   private ViewGroup mWrapperStateAspectRatio;
   private ViewGroup mWrapperStateRotate;
   private ViewGroup mWrapperStateScale;
   private ViewGroup mLayoutAspectRatio;
   private ViewGroup mLayoutRotate;
   private ViewGroup mLayoutScale;
   private List<ViewGroup> mCropAspectRatioViews = new ArrayList();
   private TextView mTextViewRotateAngle;
   private TextView mTextViewScalePercent;
   private View mBlockingView;
   private Bitmap.CompressFormat mCompressFormat;
   private int mCompressQuality;
   private int[] mAllowedGestures;
   private TransformImageView.TransformImageListener mImageListener;
   private final View.OnClickListener mStateClickListener;

   public UCropActivity() {
      this.mCompressFormat = DEFAULT_COMPRESS_FORMAT;
      this.mCompressQuality = 90;
      this.mAllowedGestures = new int[]{1, 2, 3};
      this.mImageListener = new TransformImageView.TransformImageListener() {
         public void onRotate(float currentAngle) {
            UCropActivity.this.setAngleText(currentAngle);
         }

         public void onScale(float currentScale) {
            UCropActivity.this.setScaleText(currentScale);
         }

         public void onLoadComplete() {
            UCropActivity.this.mUCropView.animate().alpha(1.0F).setDuration(300L).setInterpolator(new AccelerateInterpolator());
            UCropActivity.this.mBlockingView.setClickable(false);
            UCropActivity.this.mShowLoader = false;
            UCropActivity.this.supportInvalidateOptionsMenu();
         }

         public void onLoadFailure(@NonNull Exception e) {
            UCropActivity.this.setResultError(e);
            UCropActivity.this.finish();
         }
      };
      this.mStateClickListener = new View.OnClickListener() {
         public void onClick(View v) {
            if (!v.isSelected()) {
               UCropActivity.this.setWidgetState(v.getId());
            }

         }
      };
   }

   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.setContentView(layout.ucrop_activity_photobox);
      Intent intent = this.getIntent();
      this.setupViews(intent);
      this.setImageData(intent);
      this.setInitialState();
      this.addBlockingView();
   }

   public boolean onCreateOptionsMenu(Menu menu) {
      this.getMenuInflater().inflate(R.menu.ucrop_menu_activity, menu);
      MenuItem menuItemLoader = menu.findItem(id.menu_loader);
      Drawable menuItemLoaderIcon = menuItemLoader.getIcon();
      if (menuItemLoaderIcon != null) {
         try {
            menuItemLoaderIcon.mutate();
            menuItemLoaderIcon.setColorFilter(this.mToolbarWidgetColor, Mode.SRC_ATOP);
            menuItemLoader.setIcon(menuItemLoaderIcon);
         } catch (IllegalStateException var6) {
            IllegalStateException e = var6;
            Log.i(TAG, String.format(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PQc1OHpSIythJ1RF")), e.getMessage(), this.getString(string.ucrop_mutate_exception_hint)));
         }

         ((Animatable)menuItemLoader.getIcon()).start();
      }

      MenuItem menuItemCrop = menu.findItem(id.menu_crop);
      Drawable menuItemCropIcon = ContextCompat.getDrawable(this, this.mToolbarCropDrawable);
      if (menuItemCropIcon != null) {
         menuItemCropIcon.mutate();
         menuItemCropIcon.setColorFilter(this.mToolbarWidgetColor, Mode.SRC_ATOP);
         menuItemCrop.setIcon(menuItemCropIcon);
      }

      return true;
   }

   public boolean onPrepareOptionsMenu(Menu menu) {
      menu.findItem(id.menu_crop).setVisible(!this.mShowLoader);
      menu.findItem(id.menu_loader).setVisible(this.mShowLoader);
      return super.onPrepareOptionsMenu(menu);
   }

   public boolean onOptionsItemSelected(MenuItem item) {
      if (item.getItemId() == id.menu_crop) {
         this.cropAndSaveImage();
      } else if (item.getItemId() == 16908332) {
         this.onBackPressed();
      }

      return super.onOptionsItemSelected(item);
   }

   protected void onStop() {
      super.onStop();
      if (this.mGestureCropImageView != null) {
         this.mGestureCropImageView.cancelAllAnimations();
      }

   }

   private void setImageData(@NonNull Intent intent) {
      Uri inputUri = (Uri)intent.getParcelableExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcBWAzODBqHwo7Ki5SVg==")));
      Uri outputUri = (Uri)intent.getParcelableExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcA2YFFjNqATAULBg2Vg==")));
      this.processOptions(intent);
      if (inputUri != null && outputUri != null) {
         try {
            this.mGestureCropImageView.setImageUri(inputUri, outputUri);
         } catch (Exception var5) {
            Exception e = var5;
            this.setResultError(e);
            this.finish();
         }
      } else {
         this.setResultError(new NullPointerException(this.getString(string.ucrop_error_input_data_is_absent)));
         this.finish();
      }

   }

   private void processOptions(@NonNull Intent intent) {
      String compressionFormatName = intent.getStringExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2AgQTNlNAo8LAg2KWUxOANvJw4oIBVfKGwKFlo=")));
      Bitmap.CompressFormat compressFormat = null;
      if (!TextUtils.isEmpty(compressionFormatName)) {
         compressFormat = CompressFormat.valueOf(compressionFormatName);
      }

      this.mCompressFormat = compressFormat == null ? DEFAULT_COMPRESS_FORMAT : compressFormat;
      this.mCompressQuality = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2AgQTNlNAo8LAg2KWU2JC9rEQIwIBgAVg==")), 90);
      int[] allowedGestures = intent.getIntArrayExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcHWAaTSpqJAotJwgAD28aNCZoHjBF")));
      if (allowedGestures != null && allowedGestures.length == 3) {
         this.mAllowedGestures = allowedGestures;
      }

      this.mGestureCropImageView.setMaxBitmapSize(intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcAX0FBg9vATAcKC1XUmoKTT8=")), 0));
      this.mGestureCropImageView.setMaxScaleMultiplier(intent.getFloatExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcAX0FBhBpJCQbLywmCWUaMAVvAQIwJAgqVg==")), 10.0F));
      this.mGestureCropImageView.setImageToWrapCropBoundsAnimDuration((long)intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcBWAKPCJuDzAeIAdfKWwbFgNsERozJysmJW8KTVdlJFkoKRgYD2ojSFo=")), 500));
      this.mOverlayView.setFreestyleCropEnabled(intent.getBooleanExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcBGEwLCB9JzAyKT4AH2wzGiQ=")), false));
      this.mOverlayView.setDimmedColor(intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcGmMKQShuDjBAKC02J2wxLANqAQYb")), this.getResources().getColor(color.ucrop_color_default_dimmed)));
      this.mOverlayView.setCircleDimmedLayer(intent.getBooleanExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2MFMCZsHgpIKi4mL2sFMExrHh40Jz5SVg==")), false));
      this.mOverlayView.setShowCropFrame(intent.getBooleanExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcUmMaAjJhJygeLDwECGgFEj8=")), true));
      this.mOverlayView.setCropFrameColor(intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2EwAjNmNygqKS4AH2UjHgNvJ1RF")), this.getResources().getColor(color.ucrop_color_default_crop_frame)));
      this.mOverlayView.setCropFrameStrokeWidth(intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2EwAjNmNygqKS4AUm8aFgNlNyhNJQcMM28VSFo=")), this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_frame_stoke_width)));
      this.mOverlayView.setShowCropGrid(intent.getBooleanExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcUmMaAjJhJygeLDwICGoFMFo=")), true));
      this.mOverlayView.setCropGridRowCount(intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2EwAjNmJygiLztfKW8hLANsERoZ")), 2));
      this.mOverlayView.setCropGridColumnCount(intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2EwAjNmJygiLzwYKWUaNAFqJTA6IAdfMw==")), 2));
      this.mOverlayView.setCropGridColor(intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2EwAjNmJygiLzwYKWUVGiY=")), this.getResources().getColor(color.ucrop_color_default_crop_grid)));
      this.mOverlayView.setCropGridStrokeWidth(intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2EwAjNmJygiLzsYCmwzGj1oHCAwJBgMJw==")), this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_grid_stoke_width)));
      float aspectRatioX = intent.getFloatExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcHWEjOCBpJzARKC0cI2UmRVo=")), 0.0F);
      float aspectRatioY = intent.getFloatExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcHWEjOCBpJzARKC0cI2UmAlo=")), 0.0F);
      int aspectRationSelectedByDefault = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcHWEjOCBpJzARKC0cI2UmLD9qASg2IBc2I2EzBldpJwIoKQgELA==")), 0);
      ArrayList<AspectRatio> aspectRatioList = intent.getParcelableArrayListExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcHWEjOCBpJzARKC0cI2UhGiRsAR46Jj4uVg==")));
      Log.i(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IT4YKmwKNDdgHCAsIxhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQFJARiJDAgJwgqKmkjAgZkNzg/IxgfJEsaGilvATMpKgguKWojSFo=")));
      this.mGestureCropImageView.setTargetAspectRatio(1.0F);
      int maxSizeX = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcAX0FBhBvAQ4uJj5SVg==")), 0);
      int maxSizeY = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcAX0FBhBvAQ4uJi5SVg==")), 0);
      if (maxSizeX > 0 && maxSizeY > 0) {
         this.mGestureCropImageView.setMaxResultImageSizeX(maxSizeX);
         this.mGestureCropImageView.setMaxResultImageSizeY(maxSizeY);
      }

   }

   private void setupViews(@NonNull Intent intent) {
      this.mStatusBarColor = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcUmYaPD9qASwBKC1fH2UjHgNvJ1RF")), ContextCompat.getColor(this, color.ucrop_color_statusbar));
      this.mToolbarColor = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcXWAgAjdpNCQ7IAguKGUgFlo=")), ContextCompat.getColor(this, color.ucrop_color_toolbar));
      this.mActiveWidgetColor = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcXH0jMCplHCweKT4uCGcjAjBoNygZIwcuM28FJDE=")), ContextCompat.getColor(this, color.ucrop_color_widget_active));
      this.mToolbarWidgetColor = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcXH0jMCplHzAeKQgiJmgKFgtlESwyJAgMDGwgQQFqEVRF")), ContextCompat.getColor(this, color.ucrop_color_toolbar_widget));
      this.mToolbarCancelDrawable = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcXH0jMCplHzAeKQgiJmgKFh9rERo2JAdXBWUwOC9uJ1k7LQhSVg==")), drawable.ucrop_ic_cross);
      this.mToolbarCropDrawable = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcXH0jMCplHzAeKQgiJmgKFh9vJwYdLBgqKGogODRvNwZF")), drawable.ucrop_ic_done);
      this.mToolbarTitle = intent.getStringExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcXH0jMCplHzAeKQgiJmgKFgplHiw7JAYMLGsVLFo=")));
      this.mToolbarTitle = this.mToolbarTitle != null ? this.mToolbarTitle : this.getResources().getString(string.ucrop_label_edit_photo);
      this.mLogoColor = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcXH0jMCplHF0eLwguH2UjHgNvJ1RF")), ContextCompat.getColor(this, color.ucrop_color_default_logo));
      this.mShowBottomControls = !intent.getBooleanExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcBmMKFiBhNFk9Iz4uL2AjGgRsDgo6JhguVg==")), false);
      this.mRootViewBackgroundColor = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcXH0jMCplHygeKQccX2oFNCFjJzg2JS0+OWwjFgJpNR46IxgAKg==")), ContextCompat.getColor(this, color.ucrop_color_crop_background));
      this.setupAppBar();
      this.initiateRootViews();
      if (this.mShowBottomControls) {
         ViewGroup photoBox = (ViewGroup)this.findViewById(id.ucrop_photobox);
         View.inflate(this, layout.ucrop_controls, photoBox);
         this.mWrapperStateAspectRatio = (ViewGroup)this.findViewById(id.state_aspect_ratio);
         this.mWrapperStateAspectRatio.setOnClickListener(this.mStateClickListener);
         this.mWrapperStateRotate = (ViewGroup)this.findViewById(id.state_rotate);
         this.mWrapperStateRotate.setOnClickListener(this.mStateClickListener);
         this.mWrapperStateScale = (ViewGroup)this.findViewById(id.state_scale);
         this.mWrapperStateScale.setOnClickListener(this.mStateClickListener);
         this.mLayoutAspectRatio = (ViewGroup)this.findViewById(id.layout_aspect_ratio);
         this.mLayoutRotate = (ViewGroup)this.findViewById(id.layout_rotate_wheel);
         this.mLayoutScale = (ViewGroup)this.findViewById(id.layout_scale_wheel);
         this.setupAspectRatioWidget(intent);
         this.setupRotateWidget();
         this.setupScaleWidget();
         this.setupStatesWrapper();
      }

   }

   private void setupAppBar() {
      this.setStatusBarColor(this.mStatusBarColor);
      Toolbar toolbar = (Toolbar)this.findViewById(id.toolbar);
      toolbar.setBackgroundColor(this.mToolbarColor);
      toolbar.setTitleTextColor(this.mToolbarWidgetColor);
      TextView toolbarTitle = (TextView)toolbar.findViewById(id.toolbar_title);
      toolbarTitle.setTextColor(this.mToolbarWidgetColor);
      toolbarTitle.setText(this.mToolbarTitle);
      Drawable stateButtonDrawable = ContextCompat.getDrawable(this, this.mToolbarCancelDrawable).mutate();
      stateButtonDrawable.setColorFilter(this.mToolbarWidgetColor, Mode.SRC_ATOP);
      toolbar.setNavigationIcon(stateButtonDrawable);
      this.setSupportActionBar(toolbar);
      ActionBar actionBar = this.getSupportActionBar();
      if (actionBar != null) {
         actionBar.setDisplayShowTitleEnabled(false);
      }

   }

   private void initiateRootViews() {
      this.mUCropView = (UCropView)this.findViewById(id.ucrop);
      this.mGestureCropImageView = this.mUCropView.getCropImageView();
      this.mOverlayView = this.mUCropView.getOverlayView();
      this.mGestureCropImageView.setTransformImageListener(this.mImageListener);
      ((ImageView)this.findViewById(id.image_view_logo)).setColorFilter(this.mLogoColor, Mode.SRC_ATOP);
      this.findViewById(id.ucrop_frame).setBackgroundColor(this.mRootViewBackgroundColor);
   }

   private void setupStatesWrapper() {
      ImageView stateScaleImageView = (ImageView)this.findViewById(id.image_view_state_scale);
      ImageView stateRotateImageView = (ImageView)this.findViewById(id.image_view_state_rotate);
      ImageView stateAspectRatioImageView = (ImageView)this.findViewById(id.image_view_state_aspect_ratio);
      stateScaleImageView.setImageDrawable(new SelectedStateListDrawable(stateScaleImageView.getDrawable(), this.mActiveWidgetColor));
      stateRotateImageView.setImageDrawable(new SelectedStateListDrawable(stateRotateImageView.getDrawable(), this.mActiveWidgetColor));
      stateAspectRatioImageView.setImageDrawable(new SelectedStateListDrawable(stateAspectRatioImageView.getDrawable(), this.mActiveWidgetColor));
   }

   @TargetApi(21)
   private void setStatusBarColor(@ColorInt int color) {
      if (VERSION.SDK_INT >= 21) {
         Window window = this.getWindow();
         if (window != null) {
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(color);
         }
      }

   }

   private void setupAspectRatioWidget(@NonNull Intent intent) {
      int aspectRationSelectedByDefault = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcHWEjOCBpJzARKC0cI2UmLD9qASg2IBc2I2EzBldpJwIoKQgELA==")), 0);
      ArrayList<AspectRatio> aspectRatioList = intent.getParcelableArrayListExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcHWEjOCBpJzARKC0cI2UhGiRsAR46Jj4uVg==")));
      if (aspectRatioList == null || aspectRatioList.isEmpty()) {
         aspectRationSelectedByDefault = 0;
         aspectRatioList = new ArrayList();
         aspectRatioList.add(new AspectRatio((String)null, 1.0F, 1.0F));
         aspectRatioList.add(new AspectRatio((String)null, 3.0F, 4.0F));
         aspectRatioList.add(new AspectRatio(this.getString(string.ucrop_label_original).toUpperCase(), 0.0F, 0.0F));
         aspectRatioList.add(new AspectRatio((String)null, 3.0F, 2.0F));
         aspectRatioList.add(new AspectRatio((String)null, 16.0F, 9.0F));
      }

      LinearLayout wrapperAspectRatioList = (LinearLayout)this.findViewById(id.layout_aspect_ratio);
      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -1);
      lp.weight = 1.0F;
      Iterator var8 = aspectRatioList.iterator();

      while(var8.hasNext()) {
         AspectRatio aspectRatio = (AspectRatio)var8.next();
         FrameLayout wrapperAspectRatio = (FrameLayout)this.getLayoutInflater().inflate(layout.ucrop_aspect_ratio, (ViewGroup)null);
         wrapperAspectRatio.setLayoutParams(lp);
         AspectRatioTextView aspectRatioTextView = (AspectRatioTextView)wrapperAspectRatio.getChildAt(0);
         aspectRatioTextView.setActiveColor(this.mActiveWidgetColor);
         aspectRatioTextView.setAspectRatio(aspectRatio);
         wrapperAspectRatioList.addView(wrapperAspectRatio);
         this.mCropAspectRatioViews.add(wrapperAspectRatio);
      }

      ((ViewGroup)this.mCropAspectRatioViews.get(aspectRationSelectedByDefault)).setSelected(true);
      var8 = this.mCropAspectRatioViews.iterator();

      while(var8.hasNext()) {
         ViewGroup cropAspectRatioView = (ViewGroup)var8.next();
         cropAspectRatioView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               UCropActivity.this.mGestureCropImageView.setTargetAspectRatio(((AspectRatioTextView)((ViewGroup)v).getChildAt(0)).getAspectRatio(v.isSelected()));
               UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
               if (!v.isSelected()) {
                  Iterator var2 = UCropActivity.this.mCropAspectRatioViews.iterator();

                  while(var2.hasNext()) {
                     ViewGroup cropAspectRatioView = (ViewGroup)var2.next();
                     cropAspectRatioView.setSelected(cropAspectRatioView == v);
                  }
               }

            }
         });
      }

   }

   private void setupRotateWidget() {
      this.mTextViewRotateAngle = (TextView)this.findViewById(id.text_view_rotate);
      ((HorizontalProgressWheelView)this.findViewById(id.rotate_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
         public void onScroll(float delta, float totalDistance) {
            UCropActivity.this.mGestureCropImageView.postRotate(delta / 42.0F);
         }

         public void onScrollEnd() {
            UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
         }

         public void onScrollStart() {
            UCropActivity.this.mGestureCropImageView.cancelAllAnimations();
         }
      });
      ((HorizontalProgressWheelView)this.findViewById(id.rotate_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
      this.findViewById(id.wrapper_reset_rotate).setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            UCropActivity.this.resetRotation();
         }
      });
      this.findViewById(id.wrapper_rotate_by_angle).setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            UCropActivity.this.rotateByAngle(90);
         }
      });
   }

   private void setupScaleWidget() {
      this.mTextViewScalePercent = (TextView)this.findViewById(id.text_view_scale);
      ((HorizontalProgressWheelView)this.findViewById(id.scale_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
         public void onScroll(float delta, float totalDistance) {
            if (delta > 0.0F) {
               UCropActivity.this.mGestureCropImageView.zoomInImage(UCropActivity.this.mGestureCropImageView.getCurrentScale() + delta * ((UCropActivity.this.mGestureCropImageView.getMaxScale() - UCropActivity.this.mGestureCropImageView.getMinScale()) / 15000.0F));
            } else {
               UCropActivity.this.mGestureCropImageView.zoomOutImage(UCropActivity.this.mGestureCropImageView.getCurrentScale() + delta * ((UCropActivity.this.mGestureCropImageView.getMaxScale() - UCropActivity.this.mGestureCropImageView.getMinScale()) / 15000.0F));
            }

         }

         public void onScrollEnd() {
            UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
         }

         public void onScrollStart() {
            UCropActivity.this.mGestureCropImageView.cancelAllAnimations();
         }
      });
      ((HorizontalProgressWheelView)this.findViewById(id.scale_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
   }

   private void setAngleText(float angle) {
      if (this.mTextViewRotateAngle != null) {
         this.mTextViewRotateAngle.setText(String.format(Locale.getDefault(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PQQbL2gpEwI=")), angle));
      }

   }

   private void setScaleText(float scale) {
      if (this.mTextViewScalePercent != null) {
         this.mTextViewScalePercent.setText(String.format(Locale.getDefault(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PQgpM3gVSFo=")), (int)(scale * 100.0F)));
      }

   }

   private void resetRotation() {
      this.mGestureCropImageView.postRotate(-this.mGestureCropImageView.getCurrentAngle());
      this.mGestureCropImageView.setImageToWrapCropBounds();
   }

   private void rotateByAngle(int angle) {
      this.mGestureCropImageView.postRotate((float)angle);
      this.mGestureCropImageView.setImageToWrapCropBounds();
   }

   private void setInitialState() {
      if (this.mShowBottomControls) {
         if (this.mWrapperStateAspectRatio.getVisibility() == 0) {
            this.setWidgetState(id.state_aspect_ratio);
         } else {
            this.setWidgetState(id.state_scale);
         }
      } else {
         this.setAllowedGestures(0);
      }

   }

   private void setWidgetState(@IdRes int stateViewId) {
      if (this.mShowBottomControls) {
         this.mWrapperStateAspectRatio.setSelected(stateViewId == id.state_aspect_ratio);
         this.mWrapperStateRotate.setSelected(stateViewId == id.state_rotate);
         this.mWrapperStateScale.setSelected(stateViewId == id.state_scale);
         this.mLayoutAspectRatio.setVisibility(stateViewId == id.state_aspect_ratio ? 0 : 8);
         this.mLayoutRotate.setVisibility(stateViewId == id.state_rotate ? 0 : 8);
         this.mLayoutScale.setVisibility(stateViewId == id.state_scale ? 0 : 8);
         if (stateViewId == id.state_scale) {
            this.setAllowedGestures(0);
         } else if (stateViewId == id.state_rotate) {
            this.setAllowedGestures(1);
         } else {
            this.setAllowedGestures(2);
         }

      }
   }

   private void setAllowedGestures(int tab) {
      this.mGestureCropImageView.setScaleEnabled(this.mAllowedGestures[tab] == 3 || this.mAllowedGestures[tab] == 1);
      this.mGestureCropImageView.setRotateEnabled(this.mAllowedGestures[tab] == 3 || this.mAllowedGestures[tab] == 2);
   }

   private void addBlockingView() {
      if (this.mBlockingView == null) {
         this.mBlockingView = new View(this);
         RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
         lp.addRule(3, id.toolbar);
         this.mBlockingView.setLayoutParams(lp);
         this.mBlockingView.setClickable(true);
      }

      ((RelativeLayout)this.findViewById(id.ucrop_photobox)).addView(this.mBlockingView);
   }

   protected void cropAndSaveImage() {
      this.mBlockingView.setClickable(true);
      this.mShowLoader = true;
      this.supportInvalidateOptionsMenu();
      this.mGestureCropImageView.cropAndSaveImage(this.mCompressFormat, this.mCompressQuality, new BitmapCropCallback() {
         public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
            Log.i(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IT4YKmwKNDdgHCAsIxhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKWwVHgZuASwzPT5SVg==")) + resultUri);
            Log.i(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IT4YKmwKNDdgHCAsIxhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4iPm8zNAZvVgJF")) + offsetX + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl86D2gjOANiAQpOPT5SVg==")) + offsetY + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsFAiN9Djg/JC0cPmUzETI=")) + imageWidth + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsFAiN9Djg/IRcMMWkFFgZ+N1RF")) + imageHeight);
            UCropActivity.this.setResultUri(resultUri, UCropActivity.this.mGestureCropImageView.getTargetAspectRatio(), offsetX, offsetY, imageWidth, imageHeight);
            UCropActivity.this.finish();
         }

         public void onCropFailure(@NonNull Throwable t) {
            UCropActivity.this.setResultError(t);
            UCropActivity.this.finish();
         }
      });
   }

   protected void setResultUri(Uri uri, float resultAspectRatio, int offsetX, int offsetY, int imageWidth, int imageHeight) {
      this.setResult(-1, this.getIntent().putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcA2YFFjNqATAULBg2Vg==")), uri).putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2EwAjNhASw5Ly4YCmQzJCBlEQZF")), resultAspectRatio).putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcBWAKPCJuDzwiLz0cLA==")), imageWidth).putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcBWAKPCJuDAYuKi4ILG8VSFo=")), imageHeight).putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcA2IwIDZuATAX")), offsetX).putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcA2IwIDZuATAI")), offsetY));
   }

   protected void setResultError(Throwable throwable) {
      this.setResult(96, (new Intent()).putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcGWEzMCplN1RF")), throwable));
   }

   static {
      DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface GestureTypes {
   }
}
