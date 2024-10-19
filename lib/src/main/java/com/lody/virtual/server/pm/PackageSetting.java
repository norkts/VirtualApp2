package com.lody.virtual.server.pm;

import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.compat.ParcelCompat;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.InstalledAppInfo;
import com.lody.virtual.server.pm.parser.PackageParserEx;

public class PackageSetting implements Parcelable {
   public static final int CURRENT_VERSION = 6;
   private static final PackageUserState DEFAULT_USER_STATE = new PackageUserState();
   public int version;
   public String primaryCpuAbi;
   public String secondaryCpuAbi;
   public boolean is64bitPackage;
   public String packageName;
   public String libPath;
   public int appId;
   public boolean dynamic;
   SparseArray<PackageUserState> userState = new SparseArray();
   public int flag;
   public long firstInstallTime;
   public long lastUpdateTime;
   public static final Parcelable.Creator<PackageSetting> CREATOR = new Parcelable.Creator<PackageSetting>() {
      public PackageSetting createFromParcel(Parcel source) {
         return new PackageSetting(6, source);
      }

      public PackageSetting[] newArray(int size) {
         return new PackageSetting[size];
      }
   };

   public PackageSetting() {
      this.version = 6;
   }

   public String getPackagePath() {
      if (this.dynamic) {
         try {
            ApplicationInfo info = VirtualCore.get().getHostPackageManager().getApplicationInfo(this.packageName, 0L);
            return info.publicSourceDir;
         } catch (PackageManager.NameNotFoundException var2) {
            PackageManager.NameNotFoundException e = var2;
            e.printStackTrace();
            return null;
         }
      } else {
         return VirtualCore.get().isExtPackage() ? VEnvironment.getPackageFileExt(this.packageName).getPath() : VEnvironment.getPackageFile(this.packageName).getPath();
      }
   }

   public InstalledAppInfo getAppInfo() {
      return new InstalledAppInfo(this.packageName, this.libPath, this.dynamic, this.flag, this.appId, this.primaryCpuAbi, this.secondaryCpuAbi, this.is64bitPackage, PackageCacheManager.get(this.packageName).xposedModule);
   }

   void removeUser(int userId) {
      this.userState.delete(userId);
   }

   PackageUserState modifyUserState(int userId) {
      PackageUserState state = (PackageUserState)this.userState.get(userId);
      if (state == null) {
         state = new PackageUserState();
         this.userState.put(userId, state);
      }

      return state;
   }

   void setUserState(int userId, boolean launched, boolean hidden, boolean installed) {
      PackageUserState state = this.modifyUserState(userId);
      state.launched = launched;
      state.hidden = hidden;
      state.installed = installed;
   }

   public PackageUserState readUserState(int userId) {
      PackageUserState state = (PackageUserState)this.userState.get(userId);
      return state != null ? state : DEFAULT_USER_STATE;
   }

   public boolean isEnabledAndMatchLPr(ComponentInfo componentInfo, int flags, int userId) {
      return (flags & 512) != 0 ? true : PackageParserEx.isEnabledLPr(componentInfo, flags, userId);
   }

   public boolean isLaunched(int userId) {
      return this.readUserState(userId).launched;
   }

   public boolean isHidden(int userId) {
      return this.readUserState(userId).hidden;
   }

   public boolean isInstalled(int userId) {
      return this.readUserState(userId).installed;
   }

   public void setLaunched(int userId, boolean launched) {
      this.modifyUserState(userId).launched = launched;
   }

   public void setHidden(int userId, boolean hidden) {
      this.modifyUserState(userId).hidden = hidden;
   }

   public void setInstalled(int userId, boolean installed) {
      this.modifyUserState(userId).installed = installed;
   }

   public boolean isRunInExtProcess() {
      boolean is64bitPkg = this.is64bitPackage();
      return !is64bitPkg;
   }

   public boolean is64bitPackage() {
      return this.is64bitPackage;
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.packageName);
      dest.writeString(this.libPath);
      dest.writeInt(this.appId);
      dest.writeString(this.primaryCpuAbi);
      dest.writeString(this.secondaryCpuAbi);
      dest.writeByte((byte)(this.is64bitPackage ? 1 : 0));
      dest.writeByte((byte)(this.dynamic ? 1 : 0));
      dest.writeSparseArray(this.userState);
      dest.writeInt(this.flag);
      dest.writeLong(this.firstInstallTime);
      dest.writeLong(this.lastUpdateTime);
   }

   PackageSetting(int version, Parcel in) {
      this.version = version;
      this.packageName = in.readString();
      this.libPath = in.readString();
      this.appId = in.readInt();
      this.primaryCpuAbi = in.readString();
      this.secondaryCpuAbi = in.readString();
      this.is64bitPackage = in.readByte() != 0;
      this.dynamic = in.readByte() != 0;
      int backupPosition = in.dataPosition();

      try {
         this.userState = in.readSparseArray(PackageUserState.class.getClassLoader());
      } catch (Throwable var6) {
         in.setDataPosition(backupPosition);
         ParcelCompat parcelCompat = new ParcelCompat(in);
         this.userState = parcelCompat.readSparseArray(PackageUserState.class.getClassLoader());
      }

      this.flag = in.readInt();
      this.firstInstallTime = in.readLong();
      this.lastUpdateTime = in.readLong();
   }
}
