package com.lody.virtual.remote.vloc;

import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.VirtualGPSSatalines;
import com.lody.virtual.helper.utils.Reflect;

public class VLocation implements Parcelable {
   public double latitude = 0.0;
   public double longitude = 0.0;
   public double altitude = 0.0;
   public float accuracy = 0.0F;
   public float speed;
   public float bearing;
   public static final Parcelable.Creator<VLocation> CREATOR = new Parcelable.Creator<VLocation>() {
      public VLocation createFromParcel(Parcel source) {
         return new VLocation(source);
      }

      public VLocation[] newArray(int size) {
         return new VLocation[size];
      }
   };

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeDouble(this.latitude);
      dest.writeDouble(this.longitude);
      dest.writeDouble(this.altitude);
      dest.writeFloat(this.accuracy);
      dest.writeFloat(this.speed);
      dest.writeFloat(this.bearing);
   }

   public double getLatitude() {
      return this.latitude;
   }

   public double getLongitude() {
      return this.longitude;
   }

   public VLocation() {
   }

   public VLocation(double latitude, double longitude) {
      this.latitude = latitude;
      this.longitude = longitude;
   }

   public VLocation(Parcel in) {
      this.latitude = in.readDouble();
      this.longitude = in.readDouble();
      this.altitude = in.readDouble();
      this.accuracy = in.readFloat();
      this.speed = in.readFloat();
      this.bearing = in.readFloat();
   }

   public boolean isEmpty() {
      return this.latitude == 0.0 && this.longitude == 0.0;
   }

   public String toString() {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITwED2szJAZjDh42LS1bOWUzLAZvASwgOBhSVg==")) + this.latitude + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186DmozBi1jAQovKBcLJQ==")) + this.longitude + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186P2oKMC9mETAwKARXVg==")) + this.altitude + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186P2szLAVhNCA5LQRXVg==")) + this.accuracy + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186KW8FNCtiVl1F")) + this.speed + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186OmgVJARjDlk9PghSVg==")) + this.bearing + '}';
   }

   public Location toSysLocation() {
      Location location = new Location(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS06KQ==")));
      location.setAccuracy(8.0F);
      Bundle extraBundle = new Bundle();
      location.setBearing(this.bearing);
      Reflect.on((Object)location).call(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLH0aLAhhNB43IgdfP2wITQRlJCQaLgguCA==")), false);
      location.setLatitude(this.latitude);
      location.setLongitude(this.longitude);
      location.setSpeed(this.speed);
      location.setTime(System.currentTimeMillis());
      int svCount = VirtualGPSSatalines.get().getSvCount();
      extraBundle.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+LGgVHiRjAQo/Iy5SVg==")), svCount);
      extraBundle.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+LGgVHiRjAQo/Iy4+OW8wGis=")), svCount);
      location.setExtras(extraBundle);
      if (VERSION.SDK_INT >= 17) {
         try {
            Reflect.on((Object)location).call(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+MWgbLCVgASQoKAg2PQ==")));
         } catch (Exception var5) {
            location.setTime(System.currentTimeMillis());
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
         }
      }

      return location;
   }
}
