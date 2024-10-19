package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;

public class DataLoaderParamsParcel implements Parcelable {
   public int type;
   public String packageName;
   public String className;
   public String arguments;
   public static final Parcelable.Creator<DataLoaderParamsParcel> CREATOR = new Parcelable.Creator<DataLoaderParamsParcel>() {
      public DataLoaderParamsParcel createFromParcel(Parcel _aidl_source) {
         DataLoaderParamsParcel _aidl_out = new DataLoaderParamsParcel();
         _aidl_out.readFromParcel(_aidl_source);
         return _aidl_out;
      }

      public DataLoaderParamsParcel[] newArray(int _aidl_size) {
         return new DataLoaderParamsParcel[_aidl_size];
      }
   };

   public final void writeToParcel(Parcel _aidl_parcel, int _aidl_flag) {
      int _aidl_start_pos = _aidl_parcel.dataPosition();
      _aidl_parcel.writeInt(0);
      _aidl_parcel.writeInt(this.type);
      _aidl_parcel.writeString(this.packageName);
      _aidl_parcel.writeString(this.className);
      _aidl_parcel.writeString(this.arguments);
      int _aidl_end_pos = _aidl_parcel.dataPosition();
      _aidl_parcel.setDataPosition(_aidl_start_pos);
      _aidl_parcel.writeInt(_aidl_end_pos - _aidl_start_pos);
      _aidl_parcel.setDataPosition(_aidl_end_pos);
   }

   public final void readFromParcel(Parcel _aidl_parcel) {
      int _aidl_start_pos = _aidl_parcel.dataPosition();
      int _aidl_parcelable_size = _aidl_parcel.readInt();
      if (_aidl_parcelable_size >= 0) {
         try {
            this.type = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos < _aidl_parcelable_size) {
               this.packageName = _aidl_parcel.readString();
               if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                  return;
               }

               this.className = _aidl_parcel.readString();
               if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                  return;
               }

               this.arguments = _aidl_parcel.readString();
               if (_aidl_parcel.dataPosition() - _aidl_start_pos < _aidl_parcelable_size) {
                  return;
               }

               return;
            }
         } finally {
            _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
         }

      }
   }

   public int describeContents() {
      return 0;
   }
}
