package com.lody.virtual.remote;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

public class PendingResultData implements Parcelable {
   public static final Parcelable.Creator<PendingResultData> CREATOR = new Parcelable.Creator<PendingResultData>() {
      public PendingResultData createFromParcel(Parcel source) {
         return new PendingResultData(source);
      }

      public PendingResultData[] newArray(int size) {
         return new PendingResultData[size];
      }
   };
   public int mType;
   public boolean mOrderedHint;
   public boolean mInitialStickyHint;
   public IBinder mToken;
   public int mSendingUser;
   public int mFlags;
   public int mResultCode;
   public String mResultData;
   public Bundle mResultExtras;
   public boolean mAbortBroadcast;
   public boolean mFinished;

   public PendingResultData(BroadcastReceiver.PendingResult result) {
      if (mirror.android.content.BroadcastReceiver.PendingResultMNC.ctor != null) {
         this.mType = mirror.android.content.BroadcastReceiver.PendingResultMNC.mType.get(result);
         this.mOrderedHint = mirror.android.content.BroadcastReceiver.PendingResultMNC.mOrderedHint.get(result);
         this.mInitialStickyHint = mirror.android.content.BroadcastReceiver.PendingResultMNC.mInitialStickyHint.get(result);
         this.mToken = (IBinder)mirror.android.content.BroadcastReceiver.PendingResultMNC.mToken.get(result);
         this.mSendingUser = mirror.android.content.BroadcastReceiver.PendingResultMNC.mSendingUser.get(result);
         this.mFlags = mirror.android.content.BroadcastReceiver.PendingResultMNC.mFlags.get(result);
         this.mResultCode = mirror.android.content.BroadcastReceiver.PendingResultMNC.mResultCode.get(result);
         this.mResultData = (String)mirror.android.content.BroadcastReceiver.PendingResultMNC.mResultData.get(result);
         this.mResultExtras = (Bundle)mirror.android.content.BroadcastReceiver.PendingResultMNC.mResultExtras.get(result);
         this.mAbortBroadcast = mirror.android.content.BroadcastReceiver.PendingResultMNC.mAbortBroadcast.get(result);
         this.mFinished = mirror.android.content.BroadcastReceiver.PendingResultMNC.mFinished.get(result);
      } else if (mirror.android.content.BroadcastReceiver.PendingResultJBMR1.ctor != null) {
         this.mType = mirror.android.content.BroadcastReceiver.PendingResultJBMR1.mType.get(result);
         this.mOrderedHint = mirror.android.content.BroadcastReceiver.PendingResultJBMR1.mOrderedHint.get(result);
         this.mInitialStickyHint = mirror.android.content.BroadcastReceiver.PendingResultJBMR1.mInitialStickyHint.get(result);
         this.mToken = (IBinder)mirror.android.content.BroadcastReceiver.PendingResultJBMR1.mToken.get(result);
         this.mSendingUser = mirror.android.content.BroadcastReceiver.PendingResultJBMR1.mSendingUser.get(result);
         this.mResultCode = mirror.android.content.BroadcastReceiver.PendingResultJBMR1.mResultCode.get(result);
         this.mResultData = (String)mirror.android.content.BroadcastReceiver.PendingResultJBMR1.mResultData.get(result);
         this.mResultExtras = (Bundle)mirror.android.content.BroadcastReceiver.PendingResultJBMR1.mResultExtras.get(result);
         this.mAbortBroadcast = mirror.android.content.BroadcastReceiver.PendingResultJBMR1.mAbortBroadcast.get(result);
         this.mFinished = mirror.android.content.BroadcastReceiver.PendingResultJBMR1.mFinished.get(result);
      } else {
         this.mType = mirror.android.content.BroadcastReceiver.PendingResult.mType.get(result);
         this.mOrderedHint = mirror.android.content.BroadcastReceiver.PendingResult.mOrderedHint.get(result);
         this.mInitialStickyHint = mirror.android.content.BroadcastReceiver.PendingResult.mInitialStickyHint.get(result);
         this.mToken = (IBinder)mirror.android.content.BroadcastReceiver.PendingResult.mToken.get(result);
         this.mResultCode = mirror.android.content.BroadcastReceiver.PendingResult.mResultCode.get(result);
         this.mResultData = (String)mirror.android.content.BroadcastReceiver.PendingResult.mResultData.get(result);
         this.mResultExtras = (Bundle)mirror.android.content.BroadcastReceiver.PendingResult.mResultExtras.get(result);
         this.mAbortBroadcast = mirror.android.content.BroadcastReceiver.PendingResult.mAbortBroadcast.get(result);
         this.mFinished = mirror.android.content.BroadcastReceiver.PendingResult.mFinished.get(result);
      }

   }

   protected PendingResultData(Parcel in) {
      this.mType = in.readInt();
      this.mOrderedHint = in.readByte() != 0;
      this.mInitialStickyHint = in.readByte() != 0;
      this.mToken = in.readStrongBinder();
      this.mSendingUser = in.readInt();
      this.mFlags = in.readInt();
      this.mResultCode = in.readInt();
      this.mResultData = in.readString();
      this.mResultExtras = in.readBundle();
      this.mAbortBroadcast = in.readByte() != 0;
      this.mFinished = in.readByte() != 0;
   }

   public BroadcastReceiver.PendingResult build() {
      if (mirror.android.content.BroadcastReceiver.PendingResultMNC.ctor != null) {
         return (BroadcastReceiver.PendingResult)mirror.android.content.BroadcastReceiver.PendingResultMNC.ctor.newInstance(this.mResultCode, this.mResultData, this.mResultExtras, this.mType, this.mOrderedHint, this.mInitialStickyHint, this.mToken, this.mSendingUser, this.mFlags);
      } else {
         return mirror.android.content.BroadcastReceiver.PendingResultJBMR1.ctor != null ? (BroadcastReceiver.PendingResult)mirror.android.content.BroadcastReceiver.PendingResultJBMR1.ctor.newInstance(this.mResultCode, this.mResultData, this.mResultExtras, this.mType, this.mOrderedHint, this.mInitialStickyHint, this.mToken, this.mSendingUser) : (BroadcastReceiver.PendingResult)mirror.android.content.BroadcastReceiver.PendingResult.ctor.newInstance(this.mResultCode, this.mResultData, this.mResultExtras, this.mType, this.mOrderedHint, this.mInitialStickyHint, this.mToken);
      }
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.mType);
      dest.writeByte((byte)(this.mOrderedHint ? 1 : 0));
      dest.writeByte((byte)(this.mInitialStickyHint ? 1 : 0));
      dest.writeStrongBinder(this.mToken);
      dest.writeInt(this.mSendingUser);
      dest.writeInt(this.mFlags);
      dest.writeInt(this.mResultCode);
      dest.writeString(this.mResultData);
      dest.writeBundle(this.mResultExtras);
      dest.writeByte((byte)(this.mAbortBroadcast ? 1 : 0));
      dest.writeByte((byte)(this.mFinished ? 1 : 0));
   }

   public void finish() {
      try {
         this.build().finish();
      } catch (Throwable var2) {
         Throwable e = var2;
         e.printStackTrace();
      }

   }
}
