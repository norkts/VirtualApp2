package com.carlos.common.imagepicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.carlos.common.imagepicker.adapter.BottomPreviewAdapter;
import com.carlos.common.imagepicker.adapter.PreviewImageAdapter;
import com.carlos.common.imagepicker.entity.Image;
import com.carlos.common.imagepicker.utils.ImageUtil;
import com.carlos.common.imagepicker.utils.StatusBarUtils;
import com.carlos.libcommon.StringFog;
import com.google.android.material.appbar.AppBarLayout;
import com.kook.librelease.R.color;
import com.kook.librelease.R.drawable;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.string;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RvPreviewActivity extends AppCompatActivity {
   private RecyclerView recyclerView;
   private LinearLayoutManager linearLayoutManager;
   private TextView tvConfirm;
   private FrameLayout btnConfirm;
   private TextView tvSelect;
   private RelativeLayout rlBottomBar;
   private AppBarLayout appBarLayout;
   private Toolbar toolbar;
   private static ArrayList<Image> tempImages;
   private static ArrayList<Image> tempSelectImages;
   private ArrayList<Image> mImages;
   private ArrayList<Image> mSelectImages;
   private boolean isShowBar = true;
   private boolean isConfirm = false;
   private boolean isSingle;
   private int mMaxCount;
   private BitmapDrawable mSelectDrawable;
   private BitmapDrawable mUnSelectDrawable;
   private RecyclerView bottomRecycleview;
   private BottomPreviewAdapter bottomPreviewAdapter;
   private View line;
   private boolean isPreview;
   private PreviewImageAdapter previewImageAdapter;

   public static void openActivity(boolean isPreview, Activity activity, ArrayList<Image> images, ArrayList<Image> selectImages, boolean isSingle, int maxSelectCount, int position, @ColorInt int toolBarColor, @ColorInt int bottomBarColor, @ColorInt int statusBarColor) {
      tempImages = images;
      tempSelectImages = selectImages;
      Intent intent = new Intent(activity, RvPreviewActivity.class);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+IGYwLCtgHjA5LBcMPmMFAiVvARo/")), maxSelectCount);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YCGgzHis=")), isSingle);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAKWUaMC9gJFlF")), position);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2Am8jND5jDjAt")), isPreview);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgAD2oLFjdhMig1KhdfKA==")), toolBarColor);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4ALGwFGiNlNCAqJy1fCG8KRVo=")), bottomBarColor);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wKNANlNCAqJy1fCG8KRVo=")), statusBarColor);
      activity.startActivityForResult(intent, 1000);
   }

   @RequiresApi(
      api = 16
   )
   @SuppressLint({"SetTextI18n"})
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.setContentView(layout.activity_rv_preview);
      this.appBarLayout = (AppBarLayout)this.findViewById(id.appbar);
      this.toolbar = (Toolbar)this.findViewById(id.toolbar);
      this.setSupportActionBar(this.toolbar);
      ActionBar actionBar = this.getSupportActionBar();

      assert actionBar != null;

      actionBar.setDisplayHomeAsUpEnabled(true);
      this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            RvPreviewActivity.this.finish();
         }
      });
      this.setStatusBarVisible(true);
      this.mImages = tempImages;
      tempImages = null;
      this.mSelectImages = tempSelectImages;
      tempSelectImages = null;
      Intent intent = this.getIntent();
      this.mMaxCount = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+IGYwLCtgHjA5LBcMPmMFAiVvARo/")), 0);
      this.isSingle = intent.getBooleanExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YCGgzHis=")), false);
      this.isPreview = intent.getBooleanExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2Am8jND5jDjAt")), false);
      Resources resources = this.getResources();
      Bitmap selectBitmap = ImageUtil.getBitmap(this, drawable.ic_image_select);
      this.mSelectDrawable = new BitmapDrawable(resources, selectBitmap);
      this.mSelectDrawable.setBounds(0, 0, selectBitmap.getWidth(), selectBitmap.getHeight());
      Bitmap unSelectBitmap = ImageUtil.getBitmap(this, drawable.ic_image_un_select);
      this.mUnSelectDrawable = new BitmapDrawable(resources, unSelectBitmap);
      this.mUnSelectDrawable.setBounds(0, 0, unSelectBitmap.getWidth(), unSelectBitmap.getHeight());
      int toolBarColor = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgAD2oLFjdhMig1KhdfKA==")), ContextCompat.getColor(this, color.blue));
      int bottomBarColor = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4ALGwFGiNlNCAqJy1fCG8KRVo=")), ContextCompat.getColor(this, color.blue));
      int statusBarColor = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wKNANlNCAqJy1fCG8KRVo=")), ContextCompat.getColor(this, color.blue));
      this.initView();
      StatusBarUtils.setBarColor(this, statusBarColor);
      this.setToolBarColor(toolBarColor);
      this.setBottomBarColor(bottomBarColor);
      this.initListener();
      this.initViewPager();
      this.changeSelect((Image)this.mImages.get(intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAKWUaMC9gJFlF")), 0)));
      this.recyclerView.scrollToPosition(intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAKWUaMC9gJFlF")), 0));
      this.toolbar.setTitle(intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAKWUaMC9gJFlF")), 0) + 1 + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + this.mImages.size());
      if (this.isPreview) {
         this.bottomRecycleview.smoothScrollToPosition(0);
      }

   }

   private void initView() {
      this.recyclerView = (RecyclerView)this.findViewById(id.rv_preview);
      this.tvConfirm = (TextView)this.findViewById(id.tv_confirm);
      this.btnConfirm = (FrameLayout)this.findViewById(id.btn_confirm);
      this.tvSelect = (TextView)this.findViewById(id.tv_select);
      this.rlBottomBar = (RelativeLayout)this.findViewById(id.rl_bottom_bar);
      this.bottomRecycleview = (RecyclerView)this.findViewById(id.bottom_recycleview);
      this.line = this.findViewById(id.line);
      this.bottomRecycleview.setLayoutManager(new LinearLayoutManager(this, 0, false));
      if (this.mSelectImages.size() == 0) {
         this.bottomRecycleview.setVisibility(8);
         this.line.setVisibility(8);
      }

      this.bottomPreviewAdapter = new BottomPreviewAdapter(this, this.mSelectImages);
      this.bottomRecycleview.setAdapter(this.bottomPreviewAdapter);
   }

   private void initListener() {
      this.btnConfirm.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            RvPreviewActivity.this.isConfirm = true;
            RvPreviewActivity.this.finish();
         }
      });
      this.tvSelect.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            RvPreviewActivity.this.clickSelect();
         }
      });
      this.bottomPreviewAdapter.setOnItemClcikLitener(new BottomPreviewAdapter.OnItemClcikLitener() {
         public void OnItemClcik(int position, Image image) {
            if (RvPreviewActivity.this.isPreview) {
               List<Image> imageList = RvPreviewActivity.this.previewImageAdapter.getData();

               for(int i = 0; i < imageList.size(); ++i) {
                  if (((Image)imageList.get(i)).equals(image)) {
                     RvPreviewActivity.this.recyclerView.smoothScrollToPosition(i);
                  }
               }
            } else {
               RvPreviewActivity.this.recyclerView.smoothScrollToPosition(((Image)RvPreviewActivity.this.mSelectImages.get(position)).getPosition());
            }

            RvPreviewActivity.this.bottomPreviewAdapter.notifyDataSetChanged();
         }
      });
   }

   private void initViewPager() {
      this.recyclerView.setLayoutManager(new LinearLayoutManager(this, 0, false));
      this.linearLayoutManager = (LinearLayoutManager)this.recyclerView.getLayoutManager();
      this.previewImageAdapter = new PreviewImageAdapter(this, this.mImages);
      this.recyclerView.setAdapter(this.previewImageAdapter);
      PagerSnapHelper snapHelper = new PagerSnapHelper();
      snapHelper.attachToRecyclerView(this.recyclerView);
      this.previewImageAdapter.setOnItemClcikLitener(new PreviewImageAdapter.OnItemClcikLitener() {
         @RequiresApi(
            api = 16
         )
         public void OnItemClcik(PreviewImageAdapter previewImageAdapter, View iteView, int position) {
            if (RvPreviewActivity.this.isShowBar) {
               RvPreviewActivity.this.hideBar();
            } else {
               RvPreviewActivity.this.showBar();
            }

         }
      });
      this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
         @SuppressLint({"SetTextI18n"})
         public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == 0) {
               int position = RvPreviewActivity.this.linearLayoutManager.findLastVisibleItemPosition();
               ((Image)RvPreviewActivity.this.mImages.get(position)).setPosition(position);
               RvPreviewActivity.this.toolbar.setTitle(position + 1 + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + RvPreviewActivity.this.mImages.size());
               RvPreviewActivity.this.changeSelect((Image)RvPreviewActivity.this.mImages.get(position));
            }

         }
      });
   }

   private void setToolBarColor(@ColorInt int color) {
      this.toolbar.setBackgroundColor(color);
   }

   private void setBottomBarColor(@ColorInt int color) {
      this.rlBottomBar.setBackgroundColor(color);
   }

   @RequiresApi(
      api = 16
   )
   private void setStatusBarVisible(boolean show) {
      if (show) {
         this.getWindow().getDecorView().setSystemUiVisibility(1024);
      } else {
         this.getWindow().getDecorView().setSystemUiVisibility(1028);
      }

   }

   @RequiresApi(
      api = 16
   )
   private void showBar() {
      this.isShowBar = true;
      this.setStatusBarVisible(true);
      this.appBarLayout.postDelayed(new Runnable() {
         public void run() {
            if (RvPreviewActivity.this.appBarLayout != null) {
               ObjectAnimator animator = ObjectAnimator.ofFloat(RvPreviewActivity.this.appBarLayout, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcMP2ogLCR9AQozKi0YGw==")), new float[]{RvPreviewActivity.this.appBarLayout.getTranslationY(), 0.0F}).setDuration(300L);
               animator.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationStart(Animator animation) {
                     super.onAnimationStart(animation);
                     if (RvPreviewActivity.this.appBarLayout != null) {
                        RvPreviewActivity.this.appBarLayout.setVisibility(0);
                     }

                  }
               });
               animator.start();
               ObjectAnimator.ofFloat(RvPreviewActivity.this.rlBottomBar, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcMP2ogLCR9AQozKi0YGw==")), new float[]{RvPreviewActivity.this.rlBottomBar.getTranslationY(), 0.0F}).setDuration(300L).start();
            }

         }
      }, 100L);
   }

   private void hideBar() {
      this.isShowBar = false;
      ObjectAnimator animator = ObjectAnimator.ofFloat(this.appBarLayout, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcMP2ogLCR9AQozKi0YGw==")), new float[]{0.0F, (float)(-this.appBarLayout.getHeight())}).setDuration(300L);
      animator.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (RvPreviewActivity.this.appBarLayout != null) {
               RvPreviewActivity.this.appBarLayout.setVisibility(8);
               RvPreviewActivity.this.appBarLayout.postDelayed(new Runnable() {
                  @RequiresApi(
                     api = 16
                  )
                  public void run() {
                     RvPreviewActivity.this.setStatusBarVisible(false);
                  }
               }, 5L);
            }

         }
      });
      animator.start();
      ObjectAnimator.ofFloat(this.rlBottomBar, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcMP2ogLCR9AQozKi0YGw==")), new float[]{0.0F, (float)this.rlBottomBar.getHeight()}).setDuration(300L).start();
   }

   private void clickSelect() {
      int position = this.linearLayoutManager.findFirstVisibleItemPosition();
      if (this.mImages != null && this.mImages.size() > position) {
         Image image = (Image)this.mImages.get(position);
         if (this.mSelectImages.contains(image)) {
            this.mSelectImages.remove(image);
         } else if (this.isSingle) {
            this.mSelectImages.clear();
            this.mSelectImages.add(image);
         } else if (this.mMaxCount > 0 && this.mSelectImages.size() >= this.mMaxCount) {
            Toast.makeText(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwkBEkZaPVdYEBMyA0AnJUotWgk=")) + this.mMaxCount + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsBOA==")), 0).show();
         } else {
            this.mSelectImages.add(image);
         }

         this.bottomPreviewAdapter.referesh(this.mSelectImages);
         this.bottomPreviewAdapter.notifyDataSetChanged();
         this.changeSelect(image);
      }

      if (this.mSelectImages.size() > 0) {
         this.bottomRecycleview.setVisibility(0);
         this.line.setVisibility(0);
      } else {
         this.bottomRecycleview.setVisibility(8);
         this.line.setVisibility(8);
      }

   }

   private void changeSelect(Image image) {
      this.tvSelect.setCompoundDrawables(this.mSelectImages.contains(image) ? this.mSelectDrawable : this.mUnSelectDrawable, (Drawable)null, (Drawable)null, (Drawable)null);
      this.setSelectImageCount(this.mSelectImages.size());
      Iterator var2 = this.mSelectImages.iterator();

      while(var2.hasNext()) {
         Image image1 = (Image)var2.next();
         image1.setChecked(false);
      }

      image.setChecked(true);
      this.bottomPreviewAdapter.referesh(this.mSelectImages);
      this.bottomPreviewAdapter.notifyDataSetChanged();
      if (this.mSelectImages.contains(image)) {
         this.bottomRecycleview.smoothScrollToPosition(image.getSelectPosition());
      }

   }

   private void setSelectImageCount(int count) {
      if (count == 0) {
         this.btnConfirm.setEnabled(false);
         this.tvConfirm.setText(this.getString(string.confirm));
      } else {
         this.btnConfirm.setEnabled(true);
         if (this.isSingle) {
            this.tvConfirm.setText(this.getString(string.confirm));
         } else if (this.mMaxCount > 0) {
            this.tvConfirm.setText(this.getString(string.confirm_maxcount, new Object[]{count, this.mMaxCount}));
         } else {
            this.tvConfirm.setText(this.getString(string.confirm_count, new Object[]{count}));
         }
      }

   }

   public void finish() {
      Intent intent = new Intent();
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2H2szGiZiNAYqKghSVg==")), this.isConfirm);
      this.setResult(1000, intent);
      super.finish();
   }
}
