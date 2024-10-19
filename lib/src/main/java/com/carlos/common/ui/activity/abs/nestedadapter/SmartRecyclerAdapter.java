package com.carlos.common.ui.activity.abs.nestedadapter;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class SmartRecyclerAdapter extends RecyclerViewAdapterWrapper {
   public static final int TYPE_HEADER = -1;
   public static final int TYPE_FOOTER = -2;
   private RecyclerView.LayoutManager layoutManager;
   private View headerView;
   private View footerView;

   public SmartRecyclerAdapter(@NonNull RecyclerView.Adapter targetAdapter) {
      super(targetAdapter);
   }

   public void setHeaderView(View view) {
      this.headerView = view;
      this.getWrappedAdapter().notifyDataSetChanged();
   }

   public void removeHeaderView() {
      this.headerView = null;
      this.getWrappedAdapter().notifyDataSetChanged();
   }

   public void setFooterView(View view) {
      this.footerView = view;
      this.getWrappedAdapter().notifyDataSetChanged();
   }

   public void removeFooterView() {
      this.footerView = null;
      this.getWrappedAdapter().notifyDataSetChanged();
   }

   private void setGridHeaderFooter(RecyclerView.LayoutManager layoutManager) {
      if (layoutManager instanceof GridLayoutManager) {
         final GridLayoutManager gridLayoutManager = (GridLayoutManager)layoutManager;
         gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            public int getSpanSize(int position) {
               boolean isShowHeader = position == 0 && SmartRecyclerAdapter.this.hasHeader();
               boolean isShowFooter = position == SmartRecyclerAdapter.this.getItemCount() - 1 && SmartRecyclerAdapter.this.hasFooter();
               return !isShowFooter && !isShowHeader ? 1 : gridLayoutManager.getSpanCount();
            }
         });
      }

   }

   private boolean hasHeader() {
      return this.headerView != null;
   }

   private boolean hasFooter() {
      return this.footerView != null;
   }

   public void onAttachedToRecyclerView(RecyclerView recyclerView) {
      super.onAttachedToRecyclerView(recyclerView);
      this.layoutManager = recyclerView.getLayoutManager();
      this.setGridHeaderFooter(this.layoutManager);
   }

   public int getItemCount() {
      return super.getItemCount() + (this.hasHeader() ? 1 : 0) + (this.hasFooter() ? 1 : 0);
   }

   public int getItemViewType(int position) {
      if (this.hasHeader() && position == 0) {
         return -1;
      } else {
         return this.hasFooter() && position == this.getItemCount() - 1 ? -2 : super.getItemViewType(this.hasHeader() ? position - 1 : position);
      }
   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View itemView = null;
      if (viewType == -1) {
         itemView = this.headerView;
      } else if (viewType == -2) {
         itemView = this.footerView;
      }

      if (itemView != null) {
         if (this.layoutManager instanceof StaggeredGridLayoutManager) {
            ViewGroup.LayoutParams targetParams = itemView.getLayoutParams();
            StaggeredGridLayoutManager.LayoutParams StaggerLayoutParams;
            if (targetParams != null) {
               StaggerLayoutParams = new StaggeredGridLayoutManager.LayoutParams(targetParams.width, targetParams.height);
            } else {
               StaggerLayoutParams = new StaggeredGridLayoutManager.LayoutParams(-1, -2);
            }

            StaggerLayoutParams.setFullSpan(true);
            itemView.setLayoutParams(StaggerLayoutParams);
         }

         return new RecyclerView.ViewHolder(itemView) {
         };
      } else {
         return super.onCreateViewHolder(parent, viewType);
      }
   }

   public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      if (this.getItemViewType(position) != -1 && this.getItemViewType(position) != -2) {
         super.onBindViewHolder(holder, this.hasHeader() ? position - 1 : position);
      }
   }
}
