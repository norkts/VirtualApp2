package com.carlos.common.imagepicker;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.carlos.common.imagepicker.adapter.FolderAdapter;
import com.carlos.common.imagepicker.adapter.ImageAdapter;
import com.carlos.common.imagepicker.entity.Folder;
import com.carlos.common.imagepicker.entity.Image;
import com.carlos.common.imagepicker.model.ImageModel;
import com.carlos.common.imagepicker.utils.DateUtils;
import com.carlos.common.imagepicker.utils.ImageCaptureManager;
import com.carlos.common.imagepicker.utils.PermissionsUtils;
import com.carlos.common.imagepicker.utils.StatusBarUtils;
import com.carlos.libcommon.StringFog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kook.librelease.R.color;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.string;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class ImageSelectorActivity extends AppCompatActivity {
   private TextView tvTime;
   private TextView tvFolderName;
   private TextView tvConfirm;
   private TextView tvPreview;
   private FrameLayout btnConfirm;
   private FrameLayout btnPreview;
   private RecyclerView rvImage;
   private RecyclerView rvFolder;
   private View masking;
   private ImageAdapter mAdapter;
   private GridLayoutManager mLayoutManager;
   private ImageCaptureManager captureManager;
   private ArrayList<Folder> mFolders;
   private Folder mFolder;
   private boolean isToSettings = false;
   private static final int PERMISSION_REQUEST_CODE = 17;
   private boolean isShowTime;
   private boolean isInitFolder;
   private RelativeLayout rlBottomBar;
   private int toolBarColor;
   private int bottomBarColor;
   private int statusBarColor;
   private int column;
   private boolean isSingle;
   private boolean showCamera;
   private int mMaxCount;
   private ArrayList<String> mSelectedImages;
   private boolean isCrop;
   private int cropMode;
   private Toolbar toolbar;
   private Handler mHideHandler = new Handler();
   private Runnable mHide = new Runnable() {
      public void run() {
         ImageSelectorActivity.this.hideTime();
      }
   };
   private BottomSheetDialog bottomSheetDialog;
   private String filePath;

   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Intent intent = this.getIntent();
      Bundle bundle = intent.getExtras();

      assert bundle != null;

      this.mMaxCount = bundle.getInt("max_selected_count", 9);
      this.column = bundle.getInt("column", 3);
      this.isSingle = bundle.getBoolean("single", false);
      this.cropMode = bundle.getInt("crop_mode", 1);
      this.showCamera = bundle.getBoolean("show_camera", true);
      this.isCrop = bundle.getBoolean("is_crop", false);
      this.mSelectedImages = bundle.getStringArrayList("selected_images");
      this.captureManager = new ImageCaptureManager(this);
      this.toolBarColor = bundle.getInt("toolBarColor", ContextCompat.getColor(this, color.blue));
      this.bottomBarColor = bundle.getInt("bottomBarColor", ContextCompat.getColor(this, color.blue));
      this.statusBarColor = bundle.getInt("statusBarColor", ContextCompat.getColor(this, color.blue));
      boolean materialDesign = bundle.getBoolean("material_design", false);
      if (materialDesign) {
         this.setContentView(layout.activity_image_select);
      } else {
         this.setContentView(layout.activity_image_select2);
      }

      this.initView();
      StatusBarUtils.setColor(this, this.statusBarColor);
      this.setToolBarColor(this.toolBarColor);
      this.setBottomBarColor(this.bottomBarColor);
      this.initListener();
      this.initImageList();
      this.checkPermissionAndLoadImages();
      this.hideFolderList();
      if (this.mSelectedImages != null) {
         this.setSelectImageCount(this.mSelectedImages.size());
      } else {
         this.setSelectImageCount(0);
      }

   }

   private void initView() {
      this.toolbar = (Toolbar)this.findViewById(id.toolbar);
      this.setSupportActionBar(this.toolbar);
      ActionBar actionBar = this.getSupportActionBar();

      assert actionBar != null;

      actionBar.setDisplayHomeAsUpEnabled(true);
      this.rlBottomBar = (RelativeLayout)this.findViewById(id.rl_bottom_bar);
      this.rvImage = (RecyclerView)this.findViewById(id.rv_image);
      this.bottomSheetDialog = new BottomSheetDialog(this);
      View bsdFolderDialogView = this.getLayoutInflater().inflate(layout.bsd_folder_dialog, (ViewGroup)null);
      this.bottomSheetDialog.setContentView(bsdFolderDialogView);
      this.rvFolder = (RecyclerView)bsdFolderDialogView.findViewById(id.rv_folder);
      this.tvConfirm = (TextView)this.findViewById(id.tv_confirm);
      this.tvPreview = (TextView)this.findViewById(id.tv_preview);
      this.btnConfirm = (FrameLayout)this.findViewById(id.btn_confirm);
      this.btnPreview = (FrameLayout)this.findViewById(id.btn_preview);
      this.tvFolderName = (TextView)this.findViewById(id.tv_folder_name);
      this.tvTime = (TextView)this.findViewById(id.tv_time);
      this.masking = this.findViewById(id.masking);
   }

   private void initListener() {
      this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            ImageSelectorActivity.this.finish();
         }
      });
      this.btnPreview.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            ArrayList<Image> images = new ArrayList(ImageSelectorActivity.this.mAdapter.getSelectImages());
            ImageSelectorActivity.this.toPreviewActivity(true, images, 0);
         }
      });
      this.btnConfirm.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            if (ImageSelectorActivity.this.isCrop && ImageSelectorActivity.this.isSingle) {
               ImageSelectorActivity.this.crop(((Image)ImageSelectorActivity.this.mAdapter.getSelectImages().get(0)).getPath(), 69);
            } else {
               ImageSelectorActivity.this.confirm();
            }

         }
      });
      this.findViewById(id.btn_folder).setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            if (ImageSelectorActivity.this.isInitFolder) {
               ImageSelectorActivity.this.openFolder();
            }

         }
      });
      this.masking.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            ImageSelectorActivity.this.closeFolder();
         }
      });
      this.rvImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            ImageSelectorActivity.this.changeTime();
         }

         public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            ImageSelectorActivity.this.changeTime();
         }
      });
   }

   private void crop(@NonNull String imagePath, int requestCode) {
      Uri selectUri = Uri.fromFile(new File(imagePath));
      SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
      long time = System.currentTimeMillis();
      String imageName = timeFormatter.format(new Date(time));
      UCrop uCrop = UCrop.of(this.getIntent(), selectUri, Uri.fromFile(new File(this.getCacheDir(), imageName + ".jpg")));
      UCrop.Options options = new UCrop.Options();
      if (this.cropMode == 2) {
         options.setCircleDimmedLayer(true);
         options.setShowCropGrid(false);
         options.setShowCropFrame(false);
      }

      options.setToolbarColor(this.toolBarColor);
      options.setStatusBarColor(this.statusBarColor);
      options.setActiveWidgetColor(this.bottomBarColor);
      options.setAspectRatioOptions(0);
      options.setCompressionQuality(100);
      uCrop.withOptions(options);
      uCrop.start(this, requestCode);
   }

   private void setToolBarColor(@ColorInt int color) {
      this.toolbar.setBackgroundColor(color);
   }

   private void setBottomBarColor(@ColorInt int color) {
      this.rlBottomBar.setBackgroundColor(color);
   }

   private void initImageList() {
      Configuration configuration = this.getResources().getConfiguration();
      if (configuration.orientation == 1) {
         this.mLayoutManager = new GridLayoutManager(this, this.column);
      } else {
         this.mLayoutManager = new GridLayoutManager(this, 5);
      }

      this.rvImage.setLayoutManager(this.mLayoutManager);
      this.mAdapter = new ImageAdapter(this, this.mMaxCount, this.isSingle);
      this.rvImage.setAdapter(this.mAdapter);
      ((SimpleItemAnimator)this.rvImage.getItemAnimator()).setSupportsChangeAnimations(false);
      if (this.mFolders != null && !this.mFolders.isEmpty()) {
         this.setFolder((Folder)this.mFolders.get(0));
      }

      this.mAdapter.setOnImageSelectListener(new ImageAdapter.OnImageSelectListener() {
         public void OnImageSelect(Image image, boolean isSelect, int selectCount) {
            ImageSelectorActivity.this.setSelectImageCount(selectCount);
         }
      });
      this.mAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
         public void OnItemClick(Image image, View itemView, int position) {
            ImageSelectorActivity.this.toPreviewActivity(false, ImageSelectorActivity.this.mAdapter.getData(), position);
         }
      });
      this.mAdapter.setOnCameraClickListener(new View.OnClickListener() {
         @RequiresApi(
            api = 23
         )
         public void onClick(View v) {
            if (PermissionsUtils.checkCameraPermission(ImageSelectorActivity.this)) {
               if (PermissionsUtils.checkWriteStoragePermission(ImageSelectorActivity.this)) {
                  ImageSelectorActivity.this.openCamera();
               }
            }
         }
      });
   }

   private void openCamera() {
      try {
         Intent intent = this.captureManager.dispatchTakePictureIntent();
         if (this.isCrop && this.isSingle) {
            this.filePath = intent.getStringExtra("photo_path");
            this.startActivityForResult(intent, 1001);
         } else {
            this.startActivityForResult(intent, 1002);
         }
      } catch (IOException var2) {
         IOException e = var2;
         e.printStackTrace();
      } catch (ActivityNotFoundException var3) {
         ActivityNotFoundException e = var3;
         Log.e("PhotoPickerFragment", "No Activity Found to handle Intent", e);
      }

   }

   private void initFolderList() {
      if (this.mFolders != null && !this.mFolders.isEmpty()) {
         this.isInitFolder = true;
         this.rvFolder.setLayoutManager(new LinearLayoutManager(this));
         FolderAdapter adapter = new FolderAdapter(this, this.mFolders);
         adapter.setOnFolderSelectListener(new FolderAdapter.OnFolderSelectListener() {
            public void OnFolderSelect(Folder folder) {
               ImageSelectorActivity.this.setFolder(folder);
               ImageSelectorActivity.this.closeFolder();
            }
         });
         this.rvFolder.setAdapter(adapter);
      }

   }

   private void hideFolderList() {
   }

   private void setFolder(Folder folder) {
      if (folder != null && this.mAdapter != null && !folder.equals(this.mFolder)) {
         this.mFolder = folder;
         this.tvFolderName.setText(folder.getName());
         this.rvImage.scrollToPosition(0);
         this.mAdapter.refresh(folder.getImages(), folder.isUseCamera());
      }

   }

   @SuppressLint({"SetTextI18n"})
   private void setSelectImageCount(int count) {
      if (count == 0) {
         this.btnConfirm.setEnabled(false);
         this.btnPreview.setEnabled(false);
         this.tvConfirm.setText(this.getString(string.confirm));
         this.tvPreview.setText(this.getString(string.preview));
      } else {
         this.btnConfirm.setEnabled(true);
         this.btnPreview.setEnabled(true);
         this.tvPreview.setText(this.getString(string.preview_count, new Object[]{count}));
         if (this.isSingle) {
            this.tvConfirm.setText(this.getString(string.confirm));
         } else if (this.mMaxCount > 0) {
            this.tvConfirm.setText(this.getString(string.confirm_maxcount, new Object[]{count, this.mMaxCount}));
         } else {
            this.tvConfirm.setText(this.getString(string.confirm_count, new Object[]{count}));
         }
      }

   }

   private void openFolder() {
      this.bottomSheetDialog.show();
   }

   private void closeFolder() {
      this.bottomSheetDialog.dismiss();
   }

   private void hideTime() {
      if (this.isShowTime) {
         ObjectAnimator.ofFloat(this.tvTime, "alpha", new float[]{1.0F, 0.0F}).setDuration(300L).start();
         this.isShowTime = false;
      }

   }

   private void showTime() {
      if (!this.isShowTime) {
         ObjectAnimator.ofFloat(this.tvTime, "alpha", new float[]{0.0F, 1.0F}).setDuration(300L).start();
         this.isShowTime = true;
      }

   }

   private void changeTime() {
      int firstVisibleItem = this.getFirstVisibleItem();
      if (firstVisibleItem >= 0 && firstVisibleItem < this.mAdapter.getData().size()) {
         Image image = (Image)this.mAdapter.getData().get(firstVisibleItem);
         String time = DateUtils.getImageTime(image.getTime() * 1000L);
         this.tvTime.setText(time);
         this.showTime();
         this.mHideHandler.removeCallbacks(this.mHide);
         this.mHideHandler.postDelayed(this.mHide, 1500L);
      }

   }

   private int getFirstVisibleItem() {
      return this.mLayoutManager.findFirstVisibleItemPosition();
   }

   private void confirm() {
      if (this.mAdapter != null) {
         ArrayList<Image> selectImages = this.mAdapter.getSelectImages();
         ArrayList<String> images = new ArrayList();
         Iterator var3 = selectImages.iterator();

         while(var3.hasNext()) {
            Image image = (Image)var3.next();
            images.add(image.getPath());
         }

         Intent intent = this.getIntent();
         intent.putStringArrayListExtra("select_result", images);
         this.setResult(-1, intent);
         this.finish();
      }
   }

   private void toPreviewActivity(boolean isPreview, ArrayList<Image> images, int position) {
      if (images != null && !images.isEmpty()) {
         RvPreviewActivity.openActivity(isPreview, this, images, this.mAdapter.getSelectImages(), this.isSingle, this.mMaxCount, position, this.toolBarColor, this.bottomBarColor, this.statusBarColor);
      }

   }

   protected void onStart() {
      super.onStart();
      if (this.isToSettings) {
         this.isToSettings = false;
         this.checkPermissionAndLoadImages();
      }

   }

   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      Log.d("HV-", "ImageSelectorActivity  onActivityResult:");
      Iterator var4;
      Image image;
      switch (requestCode) {
         case 69:
            if (data != null) {
               this.setResult(-1, data);
               this.finish();
            } else {
               this.mAdapter.notifyDataSetChanged();
               this.setSelectImageCount(this.mAdapter.getSelectImages().size());
            }
            break;
         case 1000:
            if (data != null && data.getBooleanExtra("is_confirm", false)) {
               if (this.isSingle && this.isCrop) {
                  this.crop(((Image)this.mAdapter.getSelectImages().get(0)).getPath(), 69);
               } else {
                  this.confirm();
               }
            } else {
               this.mAdapter.notifyDataSetChanged();
               this.setSelectImageCount(this.mAdapter.getSelectImages().size());
            }
            break;
         case 1001:
            this.crop(this.filePath, 1003);
            break;
         case 1002:
            this.loadImageForSDCard();
            this.setSelectImageCount(this.mAdapter.getSelectImages().size());
            this.mSelectedImages = new ArrayList();
            var4 = this.mAdapter.getSelectImages().iterator();

            while(var4.hasNext()) {
               image = (Image)var4.next();
               this.mSelectedImages.add(image.getPath());
            }

            this.mAdapter.setSelectedImages(this.mSelectedImages);
            this.mAdapter.notifyDataSetChanged();
            break;
         case 1003:
            if (data != null) {
               this.setResult(-1, data);
               this.finish();
            } else {
               this.loadImageForSDCard();
               this.setSelectImageCount(this.mAdapter.getSelectImages().size());
               this.mSelectedImages = new ArrayList();
               var4 = this.mAdapter.getSelectImages().iterator();

               while(var4.hasNext()) {
                  image = (Image)var4.next();
                  this.mSelectedImages.add(image.getPath());
               }

               this.mAdapter.setSelectedImages(this.mSelectedImages);
               this.mAdapter.notifyDataSetChanged();
            }
      }

   }

   public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      if (this.mLayoutManager != null && this.mAdapter != null) {
         if (newConfig.orientation == 1) {
            this.mLayoutManager.setSpanCount(3);
         } else if (newConfig.orientation == 2) {
            this.mLayoutManager.setSpanCount(5);
         }

         this.mAdapter.notifyDataSetChanged();
      }

   }

   private void checkPermissionAndLoadImages() {
      if (Environment.getExternalStorageState().equals("mounted")) {
         int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this.getApplication(), "android.permission.WRITE_EXTERNAL_STORAGE");
         if (hasWriteContactsPermission == 0) {
            this.loadImageForSDCard();
         } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 17);
         }

      }
   }

   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      if (requestCode == 17) {
         if (grantResults.length > 0 && grantResults[0] == 0) {
            this.loadImageForSDCard();
         } else {
            this.showExceptionDialog();
         }
      }

   }

   private void showExceptionDialog() {
      (new AlertDialog.Builder(this)).setCancelable(false).setTitle("提示").setMessage("该相册需要赋予访问存储的权限，请到“设置”>“应用”>“权限”中配置权限。").setNegativeButton("取消", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            ImageSelectorActivity.this.finish();
         }
      }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            ImageSelectorActivity.this.startAppSettings();
            ImageSelectorActivity.this.isToSettings = true;
         }
      }).show();
   }

   private void loadImageForSDCard() {
      ImageModel.loadImageForSDCard(this, new ImageModel.DataCallback() {
         public void onSuccess(ArrayList<Folder> folders) {
            ImageSelectorActivity.this.mFolders = folders;
            ImageSelectorActivity.this.runOnUiThread(new Runnable() {
               public void run() {
                  if (ImageSelectorActivity.this.mFolders != null && !ImageSelectorActivity.this.mFolders.isEmpty()) {
                     ImageSelectorActivity.this.initFolderList();
                     ((Folder)ImageSelectorActivity.this.mFolders.get(0)).setUseCamera(ImageSelectorActivity.this.showCamera);
                     ImageSelectorActivity.this.setFolder((Folder)ImageSelectorActivity.this.mFolders.get(0));
                     if (ImageSelectorActivity.this.mSelectedImages != null && ImageSelectorActivity.this.mAdapter != null) {
                        ImageSelectorActivity.this.mAdapter.setSelectedImages(ImageSelectorActivity.this.mSelectedImages);
                        ImageSelectorActivity.this.mSelectedImages = null;
                     }
                  }

               }
            });
         }
      });
   }

   private void startAppSettings() {
      Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
      intent.setData(Uri.parse("package:" + this.getPackageName()));
      this.startActivity(intent);
   }
}
