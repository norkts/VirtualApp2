package com.carlos.common.imagepicker.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carlos.common.imagepicker.entity.Image;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R.drawable;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import java.io.File;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
   private Context mContext;
   private List<Image> mImgList;
   private OnItemClickListener mListener;

   public ImagePagerAdapter(Context context, List<Image> imgList) {
      this.mContext = context;
      this.mImgList = imgList;
   }

   public int getCount() {
      return this.mImgList == null ? 0 : this.mImgList.size();
   }

   public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
      return view == object;
   }

   public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
      container.removeView((View)object);
   }

   public int getItemPosition(@NonNull Object object) {
      return -2;
   }

   @NonNull
   public Object instantiateItem(@NonNull ViewGroup container, final int position) {
      View itemView = LayoutInflater.from(this.mContext).inflate(layout.item_view_pager, container, false);
      ImageView imageView = (ImageView)itemView.findViewById(id.iv_pager);
      String path = ((Image)this.mImgList.get(position)).getPath();
      Uri uri;
      if (path.startsWith("http")) {
         uri = Uri.parse(path);
      } else {
         uri = Uri.fromFile(new File(path));
      }

      final Image image = (Image)this.mImgList.get(position);
      Glide.with(this.mContext).setDefaultRequestOptions((new RequestOptions()).dontTransform().placeholder(drawable.ic_image).error(drawable.ic_img_load_fail).override(800, 1200)).load(uri).into(imageView);
      imageView.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            if (ImagePagerAdapter.this.mListener != null) {
               ImagePagerAdapter.this.mListener.onItemClick(position, image);
            }

         }
      });
      container.addView(itemView);
      return itemView;
   }

   public void setOnItemClickListener(OnItemClickListener l) {
      this.mListener = l;
   }

   public interface OnItemClickListener {
      void onItemClick(int var1, Image var2);
   }
}
