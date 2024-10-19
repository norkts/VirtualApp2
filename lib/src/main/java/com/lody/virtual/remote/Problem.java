package com.lody.virtual.remote;

import android.os.Parcel;
import android.os.Parcelable;

public class Problem implements Parcelable {
   public static final Parcelable.Creator<Problem> CREATOR = new Parcelable.Creator<Problem>() {
      public Problem createFromParcel(Parcel source) {
         return new Problem(source);
      }

      public Problem[] newArray(int size) {
         return new Problem[size];
      }
   };
   public Throwable e;

   public Problem(Throwable e) {
      this.e = e;
   }

   protected Problem(Parcel in) {
      this.e = (Throwable)in.readSerializable();
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeSerializable(this.e);
   }
}
