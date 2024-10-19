package com.carlos.common.imagepicker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.carlos.common.imagepicker.callback.CropBoundsChangeListener;
import com.carlos.common.imagepicker.callback.OverlayViewChangeListener;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.styleable;

public class UCropView extends FrameLayout {
   private GestureCropImageView mGestureCropImageView;
   private final OverlayView mViewOverlay;

   public UCropView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public UCropView(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      LayoutInflater.from(context).inflate(layout.ucrop_view, this, true);
      this.mGestureCropImageView = (GestureCropImageView)this.findViewById(id.image_view_crop);
      this.mViewOverlay = (OverlayView)this.findViewById(id.view_overlay);
      TypedArray a = context.obtainStyledAttributes(attrs, styleable.ucrop_UCropView);
      this.mViewOverlay.processStyledAttributes(a);
      this.mGestureCropImageView.processStyledAttributes(a);
      a.recycle();
      this.setListenersToViews();
   }

   private void setListenersToViews() {
      this.mGestureCropImageView.setCropBoundsChangeListener(new CropBoundsChangeListener() {
         public void onCropAspectRatioChanged(float cropRatio) {
            UCropView.this.mViewOverlay.setTargetAspectRatio(cropRatio);
         }
      });
      this.mViewOverlay.setOverlayViewChangeListener(new OverlayViewChangeListener() {
         public void onCropRectUpdated(RectF cropRect) {
            UCropView.this.mGestureCropImageView.setCropRect(cropRect);
         }
      });
   }

   public boolean shouldDelayChildPressedState() {
      return false;
   }

   @NonNull
   public GestureCropImageView getCropImageView() {
      return this.mGestureCropImageView;
   }

   @NonNull
   public OverlayView getOverlayView() {
      return this.mViewOverlay;
   }

   public void resetCropImageView() {
      this.removeView(this.mGestureCropImageView);
      this.mGestureCropImageView = new GestureCropImageView(this.getContext());
      this.setListenersToViews();
      this.mGestureCropImageView.setCropRect(this.getOverlayView().getCropViewRect());
      this.addView(this.mGestureCropImageView, 0);
   }
}
