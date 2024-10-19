package com.carlos.common.imagepicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.carlos.common.imagepicker.entity.Image;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R.drawable;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import java.util.ArrayList;
import java.util.Iterator;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   private Context mContext;
   private ArrayList<Image> mImages;
   private LayoutInflater mInflater;
   private View.OnClickListener onCameraClickListener = null;
   private static final int ITEM_TYPE_CAMERA = 100;
   private static final int ITEM_TYPE_PHOTO = 101;
   private boolean showCamera;
   private ArrayList<Image> mSelectImages = new ArrayList();
   private OnImageSelectListener mSelectListener;
   private OnItemClickListener mItemClickListener;
   private int mMaxCount;
   private boolean isSingle;

   public int getItemViewType(int position) {
      return this.showCamera && position == 0 ? 100 : 101;
   }

   public ImageAdapter(Context context, int maxCount, boolean isSingle) {
      this.mContext = context;
      this.mInflater = LayoutInflater.from(this.mContext);
      this.mMaxCount = maxCount;
      this.isSingle = isSingle;
   }

   @NonNull
   public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      if (viewType == 100) {
         CameraHolder cameraHolder = new CameraHolder(this.mInflater.inflate(layout.adapter_camera_item, parent, false));
         cameraHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               if (ImageAdapter.this.onCameraClickListener != null) {
                  ImageAdapter.this.onCameraClickListener.onClick(v);
               }

            }
         });
         return cameraHolder;
      } else {
         return new ImageHolder(this.mInflater.inflate(layout.adapter_images_item, parent, false));
      }
   }

   @RequiresApi(
      api = 16
   )
   public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      if (this.getItemViewType(position) == 101) {
         final ImageHolder imageHolder = (ImageHolder)holder;
         final Image image;
         if (this.showCamera) {
            image = (Image)this.mImages.get(position - 1);
            image.setPosition(position - 1);
         } else {
            image = (Image)this.mImages.get(position);
            image.setPosition(position);
         }

         Glide.with(this.mContext).load(image.getPath()).transition((new GenericTransitionOptions()).transition(17432578)).transition((new DrawableTransitionOptions()).crossFade(150)).apply((new RequestOptions()).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(drawable.ic_image).error(drawable.ic_img_load_fail)).thumbnail(0.5F).into(imageHolder.ivImage);
         this.setItemSelect(imageHolder, this.mSelectImages.contains(image));
         imageHolder.ivSelectIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               if (ImageAdapter.this.mSelectImages.contains(image)) {
                  ImageAdapter.this.unSelectImage(image);
                  ImageAdapter.this.setItemSelect(imageHolder, false);
               } else if (ImageAdapter.this.isSingle) {
                  ImageAdapter.this.clearImageSelect();
                  ImageAdapter.this.selectImage(image);
                  ImageAdapter.this.setItemSelect(imageHolder, true);
               } else if (ImageAdapter.this.mMaxCount > 0 && ImageAdapter.this.mSelectImages.size() >= ImageAdapter.this.mMaxCount) {
                  if (ImageAdapter.this.mSelectImages.size() == ImageAdapter.this.mMaxCount) {
                     Toast.makeText(ImageAdapter.this.mContext, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwkBEkZaPVdYEBMyA0AnJUotWgk=")) + ImageAdapter.this.mMaxCount + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsBOA==")), 0).show();
                  }
               } else {
                  ImageAdapter.this.selectImage(image);
                  ImageAdapter.this.setItemSelect(imageHolder, true);
               }

            }
         });
         holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               if (ImageAdapter.this.mItemClickListener != null) {
                  if (ImageAdapter.this.showCamera) {
                     ImageAdapter.this.mItemClickListener.OnItemClick(image, imageHolder.itemView, imageHolder.getAdapterPosition() - 1);
                  } else {
                     ImageAdapter.this.mItemClickListener.OnItemClick(image, imageHolder.itemView, imageHolder.getAdapterPosition());
                  }
               }

            }
         });
      }

   }

   public void setOnCameraClickListener(View.OnClickListener onCameraClickListener) {
      this.onCameraClickListener = onCameraClickListener;
   }

   private void selectImage(Image image) {
      this.mSelectImages.add(image);
      if (this.mSelectListener != null) {
         this.mSelectListener.OnImageSelect(image, true, this.mSelectImages.size());
      }

   }

   private void unSelectImage(Image image) {
      this.mSelectImages.remove(image);
      if (this.mSelectListener != null) {
         this.mSelectListener.OnImageSelect(image, false, this.mSelectImages.size());
      }

   }

   public int getItemCount() {
      if (this.showCamera) {
         return this.mImages == null ? 0 : this.mImages.size() + 1;
      } else {
         return this.mImages == null ? 0 : this.mImages.size();
      }
   }

   public ArrayList<Image> getData() {
      return this.mImages;
   }

   public void refresh(ArrayList<Image> data, boolean showCamera) {
      this.showCamera = showCamera;
      this.mImages = data;
      this.notifyDataSetChanged();
   }

   private void setItemSelect(ImageHolder holder, boolean isSelect) {
      if (isSelect) {
         holder.ivSelectIcon.setImageResource(drawable.ic_image_select);
         holder.ivMasking.setAlpha(0.5F);
      } else {
         holder.ivSelectIcon.setImageResource(drawable.ic_image_un_select);
         holder.ivMasking.setAlpha(0.2F);
      }

   }

   private void clearImageSelect() {
      this.mSelectImages.clear();
      this.notifyDataSetChanged();
   }

   public void setSelectedImages(ArrayList<String> selected) {
      this.mSelectImages.clear();
      if (this.mImages != null && selected != null) {
         Iterator var2 = selected.iterator();

         while(true) {
            while(var2.hasNext()) {
               String path = (String)var2.next();
               if (this.isFull()) {
                  return;
               }

               Iterator var4 = this.mImages.iterator();

               while(var4.hasNext()) {
                  Image image = (Image)var4.next();
                  if (path.equals(image.getPath())) {
                     if (!this.mSelectImages.contains(image)) {
                        this.mSelectImages.add(image);
                     }
                     break;
                  }
               }
            }

            this.notifyDataSetChanged();
            break;
         }
      }

   }

   private boolean isFull() {
      return this.isSingle && this.mSelectImages.size() == 1 || this.mMaxCount > 0 && this.mSelectImages.size() == this.mMaxCount;
   }

   public ArrayList<Image> getSelectImages() {
      return this.mSelectImages;
   }

   public void setOnImageSelectListener(OnImageSelectListener listener) {
      this.mSelectListener = listener;
   }

   public void setOnItemClickListener(OnItemClickListener listener) {
      this.mItemClickListener = listener;
   }

   public interface OnItemClickListener {
      void OnItemClick(Image var1, View var2, int var3);
   }

   public interface OnImageSelectListener {
      void OnImageSelect(Image var1, boolean var2, int var3);
   }

   class CameraHolder extends RecyclerView.ViewHolder {
      ImageView ivCamera;

      CameraHolder(View itemView) {
         super(itemView);
         this.ivCamera = (ImageView)itemView.findViewById(id.iv_camera);
      }
   }

   class ImageHolder extends RecyclerView.ViewHolder {
      ImageView ivImage;
      ImageView ivSelectIcon;
      ImageView ivMasking;

      ImageHolder(View itemView) {
         super(itemView);
         this.ivImage = (ImageView)itemView.findViewById(id.iv_image);
         this.ivSelectIcon = (ImageView)itemView.findViewById(id.iv_select);
         this.ivMasking = (ImageView)itemView.findViewById(id.iv_masking);
      }
   }
}
