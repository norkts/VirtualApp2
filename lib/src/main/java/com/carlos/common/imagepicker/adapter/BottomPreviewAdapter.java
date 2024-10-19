package com.carlos.common.imagepicker.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.carlos.common.imagepicker.entity.Image;
import com.kook.librelease.R.drawable;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import java.util.Iterator;
import java.util.List;

public class BottomPreviewAdapter extends RecyclerView.Adapter<BottomPreviewAdapter.CustomeHolder> {
   private Context context;
   private List<Image> imagesList;
   public OnItemClcikLitener onItemClcikLitener;
   public OnDataChangeFinishListener onDataChangeFinishListener;

   public void setOnItemClcikLitener(OnItemClcikLitener onItemClcikLitener) {
      this.onItemClcikLitener = onItemClcikLitener;
   }

   public void setOnDataChangeFinishListener(OnDataChangeFinishListener onDataChangeFinishListener) {
      this.onDataChangeFinishListener = onDataChangeFinishListener;
   }

   public BottomPreviewAdapter(Context context, List<Image> imagesList) {
      this.context = context;
      this.imagesList = imagesList;
   }

   @NonNull
   public CustomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new CustomeHolder(LayoutInflater.from(this.context).inflate(layout.bootm_preview_item, parent, false));
   }

   @RequiresApi(
      api = 16
   )
   public void onBindViewHolder(@NonNull final CustomeHolder holder, int position) {
      ((Image)this.imagesList.get(position)).setSelectPosition(position);
      Glide.with(this.context).load(((Image)this.imagesList.get(holder.getAdapterPosition())).getPath()).apply((new RequestOptions()).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().override(800, 800)).thumbnail(0.5F).into(holder.imageView);
      holder.imageView.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            Iterator var2 = BottomPreviewAdapter.this.imagesList.iterator();

            while(var2.hasNext()) {
               Image image = (Image)var2.next();
               image.setChecked(false);
            }

            ((Image)BottomPreviewAdapter.this.imagesList.get(holder.getAdapterPosition())).setChecked(true);
            if (BottomPreviewAdapter.this.onItemClcikLitener != null) {
               int a = holder.getAdapterPosition();
               BottomPreviewAdapter.this.onItemClcikLitener.OnItemClcik(holder.getAdapterPosition(), (Image)BottomPreviewAdapter.this.imagesList.get(holder.getAdapterPosition()));
            }

         }
      });
      if (((Image)this.imagesList.get(position)).isChecked()) {
         holder.imageView.setBackground(ContextCompat.getDrawable(this.context, drawable.border));
      } else {
         holder.imageView.setBackground((Drawable)null);
      }

   }

   public int getItemCount() {
      return this.imagesList.size();
   }

   public void referesh(List<Image> newData) {
      this.imagesList = newData;
      this.notifyDataSetChanged();
   }

   class CustomeHolder extends RecyclerView.ViewHolder {
      private ImageView imageView;

      public CustomeHolder(View itemView) {
         super(itemView);
         this.imageView = (ImageView)itemView.findViewById(id.bottom_imageview_item);
      }
   }

   public interface OnDataChangeFinishListener {
      void changeFinish();
   }

   public interface OnItemClcikLitener {
      void OnItemClcik(int var1, Image var2);
   }
}
