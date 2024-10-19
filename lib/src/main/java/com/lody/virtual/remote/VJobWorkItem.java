package com.lody.virtual.remote;

import android.annotation.TargetApi;
import android.app.job.JobWorkItem;
import android.os.Parcel;
import android.os.Parcelable;

@TargetApi(26)
public class VJobWorkItem implements Parcelable {
   private JobWorkItem item;
   public static final Parcelable.Creator<VJobWorkItem> CREATOR = new Parcelable.Creator<VJobWorkItem>() {
      public VJobWorkItem createFromParcel(Parcel source) {
         return new VJobWorkItem(source);
      }

      public VJobWorkItem[] newArray(int size) {
         return new VJobWorkItem[size];
      }
   };

   public JobWorkItem get() {
      return this.item;
   }

   public void set(JobWorkItem item) {
      this.item = item;
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeParcelable(this.item, flags);
   }

   public VJobWorkItem() {
   }

   public VJobWorkItem(JobWorkItem item) {
      this.item = item;
   }

   protected VJobWorkItem(Parcel in) {
      this.item = (JobWorkItem)in.readParcelable(JobWorkItem.class.getClassLoader());
   }
}
