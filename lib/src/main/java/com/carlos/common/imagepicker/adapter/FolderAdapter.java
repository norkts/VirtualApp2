package com.carlos.common.imagepicker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.carlos.common.imagepicker.entity.Folder;
import com.carlos.common.imagepicker.entity.Image;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import java.io.File;
import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
   private Context mContext;
   private ArrayList<Folder> mFolders;
   private LayoutInflater mInflater;
   private int mSelectItem;
   private OnFolderSelectListener mListener;

   public FolderAdapter(Context context, ArrayList<Folder> folders) {
      this.mContext = context;
      this.mFolders = folders;
      this.mInflater = LayoutInflater.from(context);
   }

   @NonNull
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = this.mInflater.inflate(layout.adapter_folder, parent, false);
      return new ViewHolder(view);
   }

   @SuppressLint({"SetTextI18n"})
   public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
      final Folder folder = (Folder)this.mFolders.get(position);
      ArrayList<Image> images = folder.getImages();
      holder.tvFolderName.setText(folder.getName());
      holder.ivSelect.setVisibility(this.mSelectItem == position ? 0 : 8);
      if (images != null && !images.isEmpty()) {
         holder.tvFolderSize.setText(images.size() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsBOA==")));
         Glide.with(this.mContext).load(new File(((Image)images.get(0)).getPath())).apply((new RequestOptions()).diskCacheStrategy(DiskCacheStrategy.NONE)).into(holder.ivImage);
      } else {
         holder.tvFolderSize.setText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OlsrJBwFSFo=")));
         holder.ivImage.setImageBitmap((Bitmap)null);
      }

      holder.itemView.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            FolderAdapter.this.mSelectItem = holder.getAdapterPosition();
            FolderAdapter.this.notifyDataSetChanged();
            if (FolderAdapter.this.mListener != null) {
               FolderAdapter.this.mListener.OnFolderSelect(folder);
            }

         }
      });
   }

   public int getItemCount() {
      return this.mFolders == null ? 0 : this.mFolders.size();
   }

   public void setOnFolderSelectListener(OnFolderSelectListener listener) {
      this.mListener = listener;
   }

   public interface OnFolderSelectListener {
      void OnFolderSelect(Folder var1);
   }

   static class ViewHolder extends RecyclerView.ViewHolder {
      ImageView ivImage;
      ImageView ivSelect;
      TextView tvFolderName;
      TextView tvFolderSize;

      ViewHolder(View itemView) {
         super(itemView);
         this.ivImage = (ImageView)itemView.findViewById(id.iv_image);
         this.ivSelect = (ImageView)itemView.findViewById(id.iv_select);
         this.tvFolderName = (TextView)itemView.findViewById(id.tv_folder_name);
         this.tvFolderSize = (TextView)itemView.findViewById(id.tv_folder_size);
      }
   }
}
