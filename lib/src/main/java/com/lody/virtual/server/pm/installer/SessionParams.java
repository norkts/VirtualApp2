package com.lody.virtual.server.pm.installer;

import android.annotation.TargetApi;
import android.content.pm.PackageInstaller;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;

@TargetApi(21)
public class SessionParams implements Parcelable {
   public static final int MODE_INVALID = -1;
   public static final int MODE_FULL_INSTALL = 1;
   public static final int MODE_INHERIT_EXISTING = 2;
   public int mode = -1;
   public int installFlags;
   public int installLocation = 1;
   public long sizeBytes = -1L;
   public String appPackageName;
   public Bitmap appIcon;
   public String appLabel;
   public long appIconLastModified = -1L;
   public Uri originatingUri;
   public Uri referrerUri;
   public String abiOverride;
   public String volumeUuid;
   public String[] grantedRuntimePermissions;
   public static final Parcelable.Creator<SessionParams> CREATOR = new Parcelable.Creator<SessionParams>() {
      public SessionParams createFromParcel(Parcel source) {
         return new SessionParams(source);
      }

      public SessionParams[] newArray(int size) {
         return new SessionParams[size];
      }
   };

   public SessionParams(int mode) {
      this.mode = mode;
   }

   public PackageInstaller.SessionParams build() {
      PackageInstaller.SessionParams params;
      if (VERSION.SDK_INT >= 23) {
         params = new PackageInstaller.SessionParams(this.mode);
         mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.installFlags.set(params, this.installFlags);
         mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.installLocation.set(params, this.installLocation);
         mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.sizeBytes.set(params, this.sizeBytes);
         mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.appPackageName.set(params, this.appPackageName);
         mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.appIcon.set(params, this.appIcon);
         mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.appLabel.set(params, this.appLabel);
         mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.appIconLastModified.set(params, this.appIconLastModified);
         mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.originatingUri.set(params, this.originatingUri);
         mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.referrerUri.set(params, this.referrerUri);
         mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.abiOverride.set(params, this.abiOverride);
         mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.volumeUuid.set(params, this.volumeUuid);
         mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.grantedRuntimePermissions.set(params, this.grantedRuntimePermissions);
         return params;
      } else {
         params = new PackageInstaller.SessionParams(this.mode);
         mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.installFlags.set(params, this.installFlags);
         mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.installLocation.set(params, this.installLocation);
         mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.sizeBytes.set(params, this.sizeBytes);
         mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.appPackageName.set(params, this.appPackageName);
         mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.appIcon.set(params, this.appIcon);
         mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.appLabel.set(params, this.appLabel);
         mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.appIconLastModified.set(params, this.appIconLastModified);
         mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.originatingUri.set(params, this.originatingUri);
         mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.referrerUri.set(params, this.referrerUri);
         mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.abiOverride.set(params, this.abiOverride);
         return params;
      }
   }

   public static SessionParams create(PackageInstaller.SessionParams sessionParams) {
      SessionParams params;
      if (VERSION.SDK_INT >= 23) {
         params = new SessionParams(mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.mode.get(sessionParams));
         params.installFlags = mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.installFlags.get(sessionParams);
         params.installLocation = mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.installLocation.get(sessionParams);
         params.sizeBytes = mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.sizeBytes.get(sessionParams);
         params.appPackageName = (String)mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.appPackageName.get(sessionParams);
         params.appIcon = (Bitmap)mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.appIcon.get(sessionParams);
         params.appLabel = (String)mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.appLabel.get(sessionParams);
         params.appIconLastModified = mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.appIconLastModified.get(sessionParams);
         params.originatingUri = (Uri)mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.originatingUri.get(sessionParams);
         params.referrerUri = (Uri)mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.referrerUri.get(sessionParams);
         params.abiOverride = (String)mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.abiOverride.get(sessionParams);
         params.volumeUuid = (String)mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.volumeUuid.get(sessionParams);
         params.grantedRuntimePermissions = (String[])mirror.android.content.pm.PackageInstaller.SessionParamsMarshmallow.grantedRuntimePermissions.get(sessionParams);
         return params;
      } else {
         params = new SessionParams(mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.mode.get(sessionParams));
         params.installFlags = mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.installFlags.get(sessionParams);
         params.installLocation = mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.installLocation.get(sessionParams);
         params.sizeBytes = mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.sizeBytes.get(sessionParams);
         params.appPackageName = (String)mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.appPackageName.get(sessionParams);
         params.appIcon = (Bitmap)mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.appIcon.get(sessionParams);
         params.appLabel = (String)mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.appLabel.get(sessionParams);
         params.appIconLastModified = mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.appIconLastModified.get(sessionParams);
         params.originatingUri = (Uri)mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.originatingUri.get(sessionParams);
         params.referrerUri = (Uri)mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.referrerUri.get(sessionParams);
         params.abiOverride = (String)mirror.android.content.pm.PackageInstaller.SessionParamsLOLLIPOP.abiOverride.get(sessionParams);
         return params;
      }
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.mode);
      dest.writeInt(this.installFlags);
      dest.writeInt(this.installLocation);
      dest.writeLong(this.sizeBytes);
      dest.writeString(this.appPackageName);
      dest.writeParcelable(this.appIcon, flags);
      dest.writeString(this.appLabel);
      dest.writeLong(this.appIconLastModified);
      dest.writeParcelable(this.originatingUri, flags);
      dest.writeParcelable(this.referrerUri, flags);
      dest.writeString(this.abiOverride);
      dest.writeString(this.volumeUuid);
      dest.writeStringArray(this.grantedRuntimePermissions);
   }

   protected SessionParams(Parcel in) {
      this.mode = in.readInt();
      this.installFlags = in.readInt();
      this.installLocation = in.readInt();
      this.sizeBytes = in.readLong();
      this.appPackageName = in.readString();
      this.appIcon = (Bitmap)in.readParcelable(Bitmap.class.getClassLoader());
      this.appLabel = in.readString();
      this.appIconLastModified = in.readLong();
      this.originatingUri = (Uri)in.readParcelable(Uri.class.getClassLoader());
      this.referrerUri = (Uri)in.readParcelable(Uri.class.getClassLoader());
      this.abiOverride = in.readString();
      this.volumeUuid = in.readString();
      this.grantedRuntimePermissions = in.createStringArray();
   }
}
