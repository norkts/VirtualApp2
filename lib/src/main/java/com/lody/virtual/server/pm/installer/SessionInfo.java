package com.lody.virtual.server.pm.installer;

import android.content.pm.PackageInstaller;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class SessionInfo implements Parcelable {
   public int sessionId;
   public String installerPackageName;
   public String resolvedBaseCodePath;
   public float progress;
   public boolean sealed;
   public boolean active;
   public int mode;
   public long sizeBytes;
   public String appPackageName;
   public Bitmap appIcon;
   public CharSequence appLabel;
   public static final Parcelable.Creator<SessionInfo> CREATOR = new Parcelable.Creator<SessionInfo>() {
      public SessionInfo createFromParcel(Parcel source) {
         return new SessionInfo(source);
      }

      public SessionInfo[] newArray(int size) {
         return new SessionInfo[size];
      }
   };

   public PackageInstaller.SessionInfo alloc() {
      PackageInstaller.SessionInfo sessionInfo = (PackageInstaller.SessionInfo)mirror.android.content.pm.PackageInstaller.SessionInfo.ctor.newInstance();
      mirror.android.content.pm.PackageInstaller.SessionInfo.sessionId.set(sessionInfo, this.sessionId);
      mirror.android.content.pm.PackageInstaller.SessionInfo.installerPackageName.set(sessionInfo, this.installerPackageName);
      mirror.android.content.pm.PackageInstaller.SessionInfo.resolvedBaseCodePath.set(sessionInfo, this.resolvedBaseCodePath);
      mirror.android.content.pm.PackageInstaller.SessionInfo.progress.set(sessionInfo, this.progress);
      mirror.android.content.pm.PackageInstaller.SessionInfo.sealed.set(sessionInfo, this.sealed);
      mirror.android.content.pm.PackageInstaller.SessionInfo.active.set(sessionInfo, this.active);
      mirror.android.content.pm.PackageInstaller.SessionInfo.mode.set(sessionInfo, this.mode);
      mirror.android.content.pm.PackageInstaller.SessionInfo.sizeBytes.set(sessionInfo, this.sizeBytes);
      mirror.android.content.pm.PackageInstaller.SessionInfo.appPackageName.set(sessionInfo, this.appPackageName);
      mirror.android.content.pm.PackageInstaller.SessionInfo.appIcon.set(sessionInfo, this.appIcon);
      mirror.android.content.pm.PackageInstaller.SessionInfo.appLabel.set(sessionInfo, this.appLabel);
      return sessionInfo;
   }

   public static SessionInfo realloc(PackageInstaller.SessionInfo sessionInfo) {
      SessionInfo info = new SessionInfo();
      info.sessionId = mirror.android.content.pm.PackageInstaller.SessionInfo.sessionId.get(sessionInfo);
      info.installerPackageName = (String)mirror.android.content.pm.PackageInstaller.SessionInfo.installerPackageName.get(sessionInfo);
      info.resolvedBaseCodePath = (String)mirror.android.content.pm.PackageInstaller.SessionInfo.resolvedBaseCodePath.get(sessionInfo);
      info.progress = mirror.android.content.pm.PackageInstaller.SessionInfo.progress.get(sessionInfo);
      info.sealed = mirror.android.content.pm.PackageInstaller.SessionInfo.sealed.get(sessionInfo);
      info.active = mirror.android.content.pm.PackageInstaller.SessionInfo.active.get(sessionInfo);
      info.mode = mirror.android.content.pm.PackageInstaller.SessionInfo.mode.get(sessionInfo);
      info.sizeBytes = mirror.android.content.pm.PackageInstaller.SessionInfo.sizeBytes.get(sessionInfo);
      info.appPackageName = (String)mirror.android.content.pm.PackageInstaller.SessionInfo.appPackageName.get(sessionInfo);
      info.appIcon = (Bitmap)mirror.android.content.pm.PackageInstaller.SessionInfo.appIcon.get(sessionInfo);
      info.appLabel = (CharSequence)mirror.android.content.pm.PackageInstaller.SessionInfo.appLabel.get(sessionInfo);
      return info;
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.sessionId);
      dest.writeString(this.installerPackageName);
      dest.writeString(this.resolvedBaseCodePath);
      dest.writeFloat(this.progress);
      dest.writeByte((byte)(this.sealed ? 1 : 0));
      dest.writeByte((byte)(this.active ? 1 : 0));
      dest.writeInt(this.mode);
      dest.writeLong(this.sizeBytes);
      dest.writeString(this.appPackageName);
      dest.writeParcelable(this.appIcon, flags);
      if (this.appLabel != null) {
         dest.writeString(this.appLabel.toString());
      }

   }

   public SessionInfo() {
   }

   protected SessionInfo(Parcel in) {
      this.sessionId = in.readInt();
      this.installerPackageName = in.readString();
      this.resolvedBaseCodePath = in.readString();
      this.progress = in.readFloat();
      this.sealed = in.readByte() != 0;
      this.active = in.readByte() != 0;
      this.mode = in.readInt();
      this.sizeBytes = in.readLong();
      this.appPackageName = in.readString();
      this.appIcon = (Bitmap)in.readParcelable(Bitmap.class.getClassLoader());
      this.appLabel = in.readString();
   }
}
