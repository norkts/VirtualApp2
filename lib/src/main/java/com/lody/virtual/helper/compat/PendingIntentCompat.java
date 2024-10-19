package com.lody.virtual.helper.compat;

import android.app.PendingIntent;
import android.os.IBinder;
import android.os.Parcel;

public class PendingIntentCompat {
   public static PendingIntent readPendingIntent(IBinder intentSender) {
      Parcel parcel = Parcel.obtain();
      parcel.writeStrongBinder(intentSender);
      parcel.setDataPosition(0);

      PendingIntent var2;
      try {
         var2 = PendingIntent.readPendingIntentOrNullFromParcel(parcel);
      } finally {
         parcel.recycle();
      }

      return var2;
   }
}
