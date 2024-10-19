package com.carlos.common.imagepicker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import com.kook.librelease.R.color;
import com.kook.librelease.R.dimen;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.string;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class UCropFragment extends Fragment {
   public static final int DEFAULT_COMPRESS_QUALITY = 90;
   public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT;
   public static final int NONE = 0;
   public static final int SCALE = 1;
   public static final int ROTATE = 2;
   public static final int ALL = 3;
   public static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQY2KmowIAhhNCA9KgcMDmUzSFo="));
   private static final int TABS_COUNT = 3;
   private static final int SCALE_WIDGET_SENSITIVITY_COEFFICIENT = 15000;
   private static final int ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;
   private UCropFragmentCallback callback;
   private int mActiveWidgetColor;
   @ColorInt
   private int mRootViewBackgroundColor;
   private int mLogoColor;
   private boolean mShowBottomControls;
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

   public UCropFragment() {
      this.mCompressFormat = DEFAULT_COMPRESS_FORMAT;
      this.mCompressQuality = 90;
      this.mAllowedGestures = new int[]{1, 2, 3};
      this.mImageListener = new TransformImageView.TransformImageListener() {
         public void onRotate(float currentAngle) {
            UCropFragment.this.setAngleText(currentAngle);
         }

         public void onScale(float currentScale) {
            UCropFragment.this.setScaleText(currentScale);
         }

         public void onLoadComplete() {
            UCropFragment.this.mUCropView.animate().alpha(1.0F).setDuration(300L).setInterpolator(new AccelerateInterpolator());
            UCropFragment.this.mBlockingView.setClickable(false);
            UCropFragment.this.callback.loadingProgress(false);
         }

         public void onLoadFailure(@NonNull Exception e) {
            UCropFragment.this.callback.onCropFinish(UCropFragment.this.getError(e));
         }
      };
      this.mStateClickListener = new View.OnClickListener() {
         public void onClick(View v) {
            if (!v.isSelected()) {
               UCropFragment.this.setWidgetState(v.getId());
            }

         }
      };
   }

   public static UCropFragment newInstance(Bundle uCrop) {
      UCropFragment fragment = new UCropFragment();
      fragment.setArguments(uCrop);
      return fragment;
   }

   public void onAttach(Context context) {
      super.onAttach(context);

      try {
         this.callback = (UCropFragmentCallback)context;
      } catch (ClassCastException var3) {
         throw new ClassCastException(context.toString() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgII28wMyhjDl0sKhcMD2kjMAZ4HCgAKS4ADmozMCRuJFEuKRccH2gFHgJrJzg2JS5SVg==")));
      }
   }

   public void setCallback(UCropFragmentCallback callback) {
      this.callback = callback;
   }

   @Nullable
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View rootView = inflater.inflate(layout.ucrop_fragment_photobox, container, false);
      Bundle args = this.getArguments();
      this.setupViews(rootView, args);
      this.setImageData(args);
      this.setInitialState();
      this.addBlockingView(rootView);
      return rootView;
   }

   public void setupViews(View view, Bundle args) {
      this.mActiveWidgetColor = args.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcXH0jMCplHCweKT4uCGcjAjBoNygZIwcuM28FJDE=")), ContextCompat.getColor(this.getContext(), color.ucrop_color_widget_active));
      this.mLogoColor = args.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcXH0jMCplHF0eLwguH2UjHgNvJ1RF")), ContextCompat.getColor(this.getContext(), color.ucrop_color_default_logo));
      this.mShowBottomControls = !args.getBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcBmMKFiBhNFk9Iz4uL2AjGgRsDgo6JhguVg==")), false);
      this.mRootViewBackgroundColor = args.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcXH0jMCplHygeKQccX2oFNCFjJzg2JS0+OWwjFgJpNR46IxgAKg==")), ContextCompat.getColor(this.getContext(), color.ucrop_color_crop_background));
      this.initiateRootViews(view);
      this.callback.loadingProgress(true);
      if (this.mShowBottomControls) {
         ViewGroup photoBox = (ViewGroup)view.findViewById(id.ucrop_photobox);
         View.inflate(this.getContext(), layout.ucrop_controls, photoBox);
         this.mWrapperStateAspectRatio = (ViewGroup)view.findViewById(id.state_aspect_ratio);
         this.mWrapperStateAspectRatio.setOnClickListener(this.mStateClickListener);
         this.mWrapperStateRotate = (ViewGroup)view.findViewById(id.state_rotate);
         this.mWrapperStateRotate.setOnClickListener(this.mStateClickListener);
         this.mWrapperStateScale = (ViewGroup)view.findViewById(id.state_scale);
         this.mWrapperStateScale.setOnClickListener(this.mStateClickListener);
         this.mLayoutAspectRatio = (ViewGroup)view.findViewById(id.layout_aspect_ratio);
         this.mLayoutRotate = (ViewGroup)view.findViewById(id.layout_rotate_wheel);
         this.mLayoutScale = (ViewGroup)view.findViewById(id.layout_scale_wheel);
         this.setupAspectRatioWidget(args, view);
         this.setupRotateWidget(view);
         this.setupScaleWidget(view);
         this.setupStatesWrapper(view);
      }

   }

   private void setImageData(@NonNull Bundle bundle) {
      Uri inputUri = (Uri)bundle.getParcelable(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcBWAzODBqHwo7Ki5SVg==")));
      Uri outputUri = (Uri)bundle.getParcelable(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcA2YFFjNqATAULBg2Vg==")));
      this.processOptions(bundle);
      if (inputUri != null && outputUri != null) {
         try {
            this.mGestureCropImageView.setImageUri(inputUri, outputUri);
         } catch (Exception var5) {
            Exception e = var5;
            this.callback.onCropFinish(this.getError(e));
         }
      } else {
         this.callback.onCropFinish(this.getError(new NullPointerException(this.getString(string.ucrop_error_input_data_is_absent))));
      }

   }

   private void processOptions(@NonNull Bundle bundle) {
      String compressionFormatName = bundle.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2AgQTNlNAo8LAg2KWUxOANvJw4oIBVfKGwKFlo=")));
      Bitmap.CompressFormat compressFormat = null;
      if (!TextUtils.isEmpty(compressionFormatName)) {
         compressFormat = CompressFormat.valueOf(compressionFormatName);
      }

      this.mCompressFormat = compressFormat == null ? DEFAULT_COMPRESS_FORMAT : compressFormat;
      this.mCompressQuality = bundle.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2AgQTNlNAo8LAg2KWU2JC9rEQIwIBgAVg==")), 90);
      int[] allowedGestures = bundle.getIntArray(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcHWAaTSpqJAotJwgAD28aNCZoHjBF")));
      if (allowedGestures != null && allowedGestures.length == 3) {
         this.mAllowedGestures = allowedGestures;
      }

      this.mGestureCropImageView.setMaxBitmapSize(bundle.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcAX0FBg9vATAcKC1XUmoKTT8=")), 0));
      this.mGestureCropImageView.setMaxScaleMultiplier(bundle.getFloat(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcAX0FBhBpJCQbLywmCWUaMAVvAQIwJAgqVg==")), 10.0F));
      this.mGestureCropImageView.setImageToWrapCropBoundsAnimDuration((long)bundle.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcBWAKPCJuDzAeIAdfKWwbFgNsERozJysmJW8KTVdlJFkoKRgYD2ojSFo=")), 500));
      this.mOverlayView.setFreestyleCropEnabled(bundle.getBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcBGEwLCB9JzAyKT4AH2wzGiQ=")), false));
      this.mOverlayView.setDimmedColor(bundle.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcGmMKQShuDjBAKC02J2wxLANqAQYb")), this.getResources().getColor(color.ucrop_color_default_dimmed)));
      this.mOverlayView.setCircleDimmedLayer(bundle.getBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2MFMCZsHgpIKi4mL2sFMExrHh40Jz5SVg==")), false));
      this.mOverlayView.setShowCropFrame(bundle.getBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcUmMaAjJhJygeLDwECGgFEj8=")), true));
      this.mOverlayView.setCropFrameColor(bundle.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2EwAjNmNygqKS4AH2UjHgNvJ1RF")), this.getResources().getColor(color.ucrop_color_default_crop_frame)));
      this.mOverlayView.setCropFrameStrokeWidth(bundle.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2EwAjNmNygqKS4AUm8aFgNlNyhNJQcMM28VSFo=")), this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_frame_stoke_width)));
      this.mOverlayView.setShowCropGrid(bundle.getBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcUmMaAjJhJygeLDwICGoFMFo=")), true));
      this.mOverlayView.setCropGridRowCount(bundle.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2EwAjNmJygiLztfKW8hLANsERoZ")), 2));
      this.mOverlayView.setCropGridColumnCount(bundle.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2EwAjNmJygiLzwYKWUaNAFqJTA6IAdfMw==")), 2));
      this.mOverlayView.setCropGridColor(bundle.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2EwAjNmJygiLzwYKWUVGiY=")), this.getResources().getColor(color.ucrop_color_default_crop_grid)));
      this.mOverlayView.setCropGridStrokeWidth(bundle.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2EwAjNmJygiLzsYCmwzGj1oHCAwJBgMJw==")), this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_grid_stoke_width)));
      float aspectRatioX = bundle.getFloat(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcHWEjOCBpJzARKC0cI2UmRVo=")), 0.0F);
      float aspectRatioY = bundle.getFloat(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcHWEjOCBpJzARKC0cI2UmAlo=")), 0.0F);
      int aspectRationSelectedByDefault = bundle.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcHWEjOCBpJzARKC0cI2UmLD9qASg2IBc2I2EzBldpJwIoKQgELA==")), 0);
      ArrayList<AspectRatio> aspectRatioList = bundle.getParcelableArrayList(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcHWEjOCBpJzARKC0cI2UhGiRsAR46Jj4uVg==")));
      if (aspectRatioX > 0.0F && aspectRatioY > 0.0F) {
         if (this.mWrapperStateAspectRatio != null) {
            this.mWrapperStateAspectRatio.setVisibility(8);
         }

         this.mGestureCropImageView.setTargetAspectRatio(aspectRatioX / aspectRatioY);
      } else if (aspectRatioList != null && aspectRationSelectedByDefault < aspectRatioList.size()) {
         this.mGestureCropImageView.setTargetAspectRatio(((AspectRatio)aspectRatioList.get(aspectRationSelectedByDefault)).getAspectRatioX() / ((AspectRatio)aspectRatioList.get(aspectRationSelectedByDefault)).getAspectRatioY());
      } else {
         this.mGestureCropImageView.setTargetAspectRatio(0.0F);
      }

      int maxSizeX = bundle.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcAX0FBhBvAQ4uJj5SVg==")), 0);
      int maxSizeY = bundle.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcAX0FBhBvAQ4uJi5SVg==")), 0);
      if (maxSizeX > 0 && maxSizeY > 0) {
         this.mGestureCropImageView.setMaxResultImageSizeX(maxSizeX);
         this.mGestureCropImageView.setMaxResultImageSizeY(maxSizeY);
      }

   }

   private void initiateRootViews(View view) {
      this.mUCropView = (UCropView)view.findViewById(id.ucrop);
      this.mGestureCropImageView = this.mUCropView.getCropImageView();
      this.mOverlayView = this.mUCropView.getOverlayView();
      this.mGestureCropImageView.setTransformImageListener(this.mImageListener);
      ((ImageView)view.findViewById(id.image_view_logo)).setColorFilter(this.mLogoColor, Mode.SRC_ATOP);
      view.findViewById(id.ucrop_frame).setBackgroundColor(this.mRootViewBackgroundColor);
   }

   private void setupStatesWrapper(View view) {
      ImageView stateScaleImageView = (ImageView)view.findViewById(id.image_view_state_scale);
      ImageView stateRotateImageView = (ImageView)view.findViewById(id.image_view_state_rotate);
      ImageView stateAspectRatioImageView = (ImageView)view.findViewById(id.image_view_state_aspect_ratio);
      stateScaleImageView.setImageDrawable(new SelectedStateListDrawable(stateScaleImageView.getDrawable(), this.mActiveWidgetColor));
      stateRotateImageView.setImageDrawable(new SelectedStateListDrawable(stateRotateImageView.getDrawable(), this.mActiveWidgetColor));
      stateAspectRatioImageView.setImageDrawable(new SelectedStateListDrawable(stateAspectRatioImageView.getDrawable(), this.mActiveWidgetColor));
   }

   private void setupAspectRatioWidget(@NonNull Bundle bundle, View view) {
      int aspectRationSelectedByDefault = bundle.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcHWEjOCBpJzARKC0cI2UmLD9qASg2IBc2I2EzBldpJwIoKQgELA==")), 0);
      ArrayList<AspectRatio> aspectRatioList = bundle.getParcelableArrayList(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcHWEjOCBpJzARKC0cI2UhGiRsAR46Jj4uVg==")));
      if (aspectRatioList == null || aspectRatioList.isEmpty()) {
         aspectRationSelectedByDefault = 2;
         aspectRatioList = new ArrayList();
         aspectRatioList.add(new AspectRatio((String)null, 1.0F, 1.0F));
         aspectRatioList.add(new AspectRatio((String)null, 3.0F, 4.0F));
         aspectRatioList.add(new AspectRatio(this.getString(string.ucrop_label_original).toUpperCase(), 0.0F, 0.0F));
         aspectRatioList.add(new AspectRatio((String)null, 3.0F, 2.0F));
         aspectRatioList.add(new AspectRatio((String)null, 16.0F, 9.0F));
      }

      LinearLayout wrapperAspectRatioList = (LinearLayout)view.findViewById(id.layout_aspect_ratio);
      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -1);
      lp.weight = 1.0F;
      Iterator var9 = aspectRatioList.iterator();

      while(var9.hasNext()) {
         AspectRatio aspectRatio = (AspectRatio)var9.next();
         FrameLayout wrapperAspectRatio = (FrameLayout)this.getLayoutInflater().inflate(layout.ucrop_aspect_ratio, (ViewGroup)null);
         wrapperAspectRatio.setLayoutParams(lp);
         AspectRatioTextView aspectRatioTextView = (AspectRatioTextView)wrapperAspectRatio.getChildAt(0);
         aspectRatioTextView.setActiveColor(this.mActiveWidgetColor);
         aspectRatioTextView.setAspectRatio(aspectRatio);
         wrapperAspectRatioList.addView(wrapperAspectRatio);
         this.mCropAspectRatioViews.add(wrapperAspectRatio);
      }

      ((ViewGroup)this.mCropAspectRatioViews.get(aspectRationSelectedByDefault)).setSelected(true);
      var9 = this.mCropAspectRatioViews.iterator();

      while(var9.hasNext()) {
         ViewGroup cropAspectRatioView = (ViewGroup)var9.next();
         cropAspectRatioView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               UCropFragment.this.mGestureCropImageView.setTargetAspectRatio(((AspectRatioTextView)((ViewGroup)v).getChildAt(0)).getAspectRatio(v.isSelected()));
               UCropFragment.this.mGestureCropImageView.setImageToWrapCropBounds();
               if (!v.isSelected()) {
                  Iterator var2 = UCropFragment.this.mCropAspectRatioViews.iterator();

                  while(var2.hasNext()) {
                     ViewGroup cropAspectRatioView = (ViewGroup)var2.next();
                     cropAspectRatioView.setSelected(cropAspectRatioView == v);
                  }
               }

            }
         });
      }

   }

   private void setupRotateWidget(View view) {
      this.mTextViewRotateAngle = (TextView)view.findViewById(id.text_view_rotate);
      ((HorizontalProgressWheelView)view.findViewById(id.rotate_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
         public void onScroll(float delta, float totalDistance) {
            UCropFragment.this.mGestureCropImageView.postRotate(delta / 42.0F);
         }

         public void onScrollEnd() {
            UCropFragment.this.mGestureCropImageView.setImageToWrapCropBounds();
         }

         public void onScrollStart() {
            UCropFragment.this.mGestureCropImageView.cancelAllAnimations();
         }
      });
      ((HorizontalProgressWheelView)view.findViewById(id.rotate_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
      view.findViewById(id.wrapper_reset_rotate).setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            UCropFragment.this.resetRotation();
         }
      });
      view.findViewById(id.wrapper_rotate_by_angle).setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            UCropFragment.this.rotateByAngle(90);
         }
      });
   }

   private void setupScaleWidget(View view) {
      this.mTextViewScalePercent = (TextView)view.findViewById(id.text_view_scale);
      ((HorizontalProgressWheelView)view.findViewById(id.scale_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
         public void onScroll(float delta, float totalDistance) {
            if (delta > 0.0F) {
               UCropFragment.this.mGestureCropImageView.zoomInImage(UCropFragment.this.mGestureCropImageView.getCurrentScale() + delta * ((UCropFragment.this.mGestureCropImageView.getMaxScale() - UCropFragment.this.mGestureCropImageView.getMinScale()) / 15000.0F));
            } else {
               UCropFragment.this.mGestureCropImageView.zoomOutImage(UCropFragment.this.mGestureCropImageView.getCurrentScale() + delta * ((UCropFragment.this.mGestureCropImageView.getMaxScale() - UCropFragment.this.mGestureCropImageView.getMinScale()) / 15000.0F));
            }

         }

         public void onScrollEnd() {
            UCropFragment.this.mGestureCropImageView.setImageToWrapCropBounds();
         }

         public void onScrollStart() {
            UCropFragment.this.mGestureCropImageView.cancelAllAnimations();
         }
      });
      ((HorizontalProgressWheelView)view.findViewById(id.scale_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
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

   private void addBlockingView(View view) {
      if (this.mBlockingView == null) {
         this.mBlockingView = new View(this.getContext());
         RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
         this.mBlockingView.setLayoutParams(lp);
         this.mBlockingView.setClickable(true);
      }

      ((RelativeLayout)view.findViewById(id.ucrop_photobox)).addView(this.mBlockingView);
   }

   public void cropAndSaveImage() {
      this.mBlockingView.setClickable(true);
      this.callback.loadingProgress(true);
      this.mGestureCropImageView.cropAndSaveImage(this.mCompressFormat, this.mCompressQuality, new BitmapCropCallback() {
         public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
            UCropFragment.this.callback.onCropFinish(UCropFragment.this.getResult(resultUri, UCropFragment.this.mGestureCropImageView.getTargetAspectRatio(), offsetX, offsetY, imageWidth, imageHeight));
            UCropFragment.this.callback.loadingProgress(false);
         }

         public void onCropFailure(@NonNull Throwable t) {
            UCropFragment.this.callback.onCropFinish(UCropFragment.this.getError(t));
         }
      });
   }

   protected UCropResult getResult(Uri uri, float resultAspectRatio, int offsetX, int offsetY, int imageWidth, int imageHeight) {
      return new UCropResult(-1, (new Intent()).putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcA2YFFjNqATAULBg2Vg==")), uri).putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcH2EwAjNhASw5Ly4YCmQzJCBlEQZF")), resultAspectRatio).putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcBWAKPCJuDzwiLz0cLA==")), imageWidth).putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcBWAKPCJuDAYuKi4ILG8VSFo=")), imageHeight).putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcA2IwIDZuATAX")), offsetX).putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcA2IwIDZuATAI")), offsetY));
   }

   protected UCropResult getError(Throwable throwable) {
      return new UCropResult(96, (new Intent()).putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcGWEzMCplN1RF")), throwable));
   }

   static {
      DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
   }

   public class UCropResult {
      public int mResultCode;
      public Intent mResultData;

      public UCropResult(int resultCode, Intent data) {
         this.mResultCode = resultCode;
         this.mResultData = data;
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface GestureTypes {
   }
}
