package com.carlos.common.ui.activity.abs.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseAdapterPlus<T> extends BaseAdapter implements SpinnerAdapter {
   protected Context context;
   private LayoutInflater mLayoutInflater;
   protected final List<T> mItems = new ArrayList();

   public BaseAdapterPlus(Context context) {
      this.context = context;
      this.mLayoutInflater = LayoutInflater.from(context);
   }

   public Context getContext() {
      return this.context;
   }

   public boolean add(T item) {
      return this.add(-1, item, false);
   }

   public boolean add(int pos, T item, boolean onlyone) {
      if (item != null) {
         if (onlyone && this.exist(item)) {
            return false;
         } else {
            if (pos >= 0) {
               this.mItems.add(pos, item);
            } else {
               this.mItems.add(item);
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public T remove(int pos) {
      return this.mItems.remove(pos);
   }

   public List<T> getItems() {
      return this.mItems;
   }

   protected <VW extends View> VW inflate(int resource, ViewGroup root) {
      return (VW)this.mLayoutInflater.inflate(resource, root);
   }

   protected <VW extends View> VW inflate(int resource, ViewGroup root, boolean attachToRoot) {
      return (VW)this.mLayoutInflater.inflate(resource, root, attachToRoot);
   }

   public void clear() {
      this.mItems.clear();
   }

   public void set(Collection<T> items) {
      this.clear();
      this.addAll(items);
   }

   public void addAll(Collection<T> items) {
      if (items != null) {
         this.mItems.addAll(items);
      }

   }

   public int findItem(T item) {
      return this.mItems.indexOf(item);
   }

   public boolean exist(T item) {
      return item == null ? false : this.mItems.contains(item);
   }

   public final int getCount() {
      return this.mItems.size();
   }

   public final T getDataItem(int position) {
      return this.mItems.get(position);
   }

   public final T getItem(int position) {
      return position >= 0 && position < this.getCount() ? this.mItems.get(position) : null;
   }

   public final T getItemById(long id) {
      return this.getItem((int)id);
   }

   public long getItemId(int position) {
      return (long)position;
   }

   public final View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
         convertView = this.createView(position, parent);
      }

      T t = this.getItem(position);
      this.attach(convertView, t, position);
      return convertView;
   }

   public View getDropDownView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
         convertView = this.createView(position, parent);
      }

      T t = this.getItem(position);
      this.attach(convertView, t, position);
      return convertView;
   }

   protected abstract View createView(int var1, ViewGroup var2);

   protected abstract void attach(View var1, T var2, int var3);

   public static class BaseViewHolder {
      protected View view;
      protected Context context;

      public BaseViewHolder(View view) {
         this.view = view;
         this.context = view.getContext();
      }

      protected <T extends View> T $(int id) {
         return this.view.findViewById(id);
      }
   }
}
