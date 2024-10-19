package com.lody.virtual.server.vs;

import android.os.Parcel;
import android.os.Parcelable;

public class VSConfig implements Parcelable {
   public boolean enable;
   public String vsPath;
   public static final Parcelable.Creator<VSConfig> CREATOR = new Parcelable.Creator<VSConfig>() {
      public VSConfig createFromParcel(Parcel source) {
         return new VSConfig(source);
      }

      public VSConfig[] newArray(int size) {
         return new VSConfig[size];
      }
   };

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeByte((byte)(this.enable ? 1 : 0));
      dest.writeString(this.vsPath);
   }

   public VSConfig() {
   }

   protected VSConfig(Parcel in) {
      this.enable = in.readByte() != 0;
      this.vsPath = in.readString();
   }
}
