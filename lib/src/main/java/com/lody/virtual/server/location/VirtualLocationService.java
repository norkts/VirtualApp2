package com.lody.virtual.server.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.lody.virtual.helper.PersistenceLayer;
import com.lody.virtual.helper.collection.SparseArray;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.vloc.VCell;
import com.lody.virtual.remote.vloc.VLocation;
import com.lody.virtual.server.interfaces.IVirtualLocationManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VirtualLocationService extends IVirtualLocationManager.Stub {
   private static final VirtualLocationService sInstance = new VirtualLocationService();
   private final SparseArray<Map<String, VLocConfig>> mLocConfigs = new SparseArray();
   private final VLocConfig mGlobalConfig = new VLocConfig();
   private final PersistenceLayer mPersistenceLayer = new PersistenceLayer(VEnvironment.getVirtualLocationFile()) {
      public int getCurrentVersion() {
         return 1;
      }

      public void writePersistenceData(Parcel p) {
         VirtualLocationService.this.mGlobalConfig.writeToParcel(p, 0);
         p.writeInt(VirtualLocationService.this.mLocConfigs.size());

         for(int i = 0; i < VirtualLocationService.this.mLocConfigs.size(); ++i) {
            int userId = VirtualLocationService.this.mLocConfigs.keyAt(i);
            Map<String, VLocConfig> pkgs = (Map)VirtualLocationService.this.mLocConfigs.valueAt(i);
            p.writeInt(userId);
            p.writeMap(pkgs);
         }

      }

      public void readPersistenceData(Parcel p, int version) {
         VirtualLocationService.this.mGlobalConfig.set(new VLocConfig(p));
         VirtualLocationService.this.mLocConfigs.clear();
         int size = p.readInt();

         while(size-- > 0) {
            int userId = p.readInt();
            Map<String, VLocConfig> pkgs = p.readHashMap(this.getClass().getClassLoader());
            VirtualLocationService.this.mLocConfigs.put(userId, pkgs);
         }

      }
   };

   public static VirtualLocationService get() {
      return sInstance;
   }

   private VirtualLocationService() {
      this.mPersistenceLayer.read();
   }

   public int getMode(int userId, String pkg) {
      synchronized(this.mLocConfigs) {
         VLocConfig config = this.getOrCreateConfig(userId, pkg);
         this.mPersistenceLayer.save();
         return config.mode;
      }
   }

   public void setMode(int userId, String pkg, int mode) {
      synchronized(this.mLocConfigs) {
         this.getOrCreateConfig(userId, pkg).mode = mode;
         this.mPersistenceLayer.save();
      }
   }

   private VLocConfig getOrCreateConfig(int userId, String pkg) {
      Map<String, VLocConfig> pkgs = (Map)this.mLocConfigs.get(userId);
      if (pkgs == null) {
         pkgs = new HashMap();
         this.mLocConfigs.put(userId, pkgs);
      }

      VLocConfig config = (VLocConfig)((Map)pkgs).get(pkg);
      if (config == null) {
         config = new VLocConfig();
         config.mode = 0;
         ((Map)pkgs).put(pkg, config);
      }

      return config;
   }

   public void setCell(int userId, String pkg, VCell cell) {
      this.getOrCreateConfig(userId, pkg).cell = cell;
      this.mPersistenceLayer.save();
   }

   public void setAllCell(int userId, String pkg, List<VCell> cell) {
      this.getOrCreateConfig(userId, pkg).allCell = cell;
      this.mPersistenceLayer.save();
   }

   public void setNeighboringCell(int userId, String pkg, List<VCell> cell) {
      this.getOrCreateConfig(userId, pkg).neighboringCell = cell;
      this.mPersistenceLayer.save();
   }

   public void setGlobalCell(VCell cell) {
      this.mGlobalConfig.cell = cell;
      this.mPersistenceLayer.save();
   }

   public void setGlobalAllCell(List<VCell> cell) {
      this.mGlobalConfig.allCell = cell;
      this.mPersistenceLayer.save();
   }

   public void setGlobalNeighboringCell(List<VCell> cell) {
      this.mGlobalConfig.neighboringCell = cell;
      this.mPersistenceLayer.save();
   }

   public VCell getCell(int userId, String pkg) {
      VLocConfig config = this.getOrCreateConfig(userId, pkg);
      this.mPersistenceLayer.save();
      switch (config.mode) {
         case 0:
         default:
            return null;
         case 1:
            return this.mGlobalConfig.cell;
         case 2:
            return config.cell;
      }
   }

   public List<VCell> getAllCell(int userId, String pkg) {
      VLocConfig config = this.getOrCreateConfig(userId, pkg);
      this.mPersistenceLayer.save();
      switch (config.mode) {
         case 0:
         default:
            return null;
         case 1:
            return this.mGlobalConfig.allCell;
         case 2:
            return config.allCell;
      }
   }

   public List<VCell> getNeighboringCell(int userId, String pkg) {
      VLocConfig config = this.getOrCreateConfig(userId, pkg);
      this.mPersistenceLayer.save();
      switch (config.mode) {
         case 0:
         default:
            return null;
         case 1:
            return this.mGlobalConfig.neighboringCell;
         case 2:
            return config.neighboringCell;
      }
   }

   public void setLocation(int userId, String pkg, VLocation loc) {
      this.getOrCreateConfig(userId, pkg).location = loc;
      this.mPersistenceLayer.save();
   }

   public VLocation getLocation(int userId, String pkg) {
      VLocConfig config = this.getOrCreateConfig(userId, pkg);
      this.mPersistenceLayer.save();
      switch (config.mode) {
         case 0:
         default:
            return null;
         case 1:
            return this.mGlobalConfig.location;
         case 2:
            return config.location;
      }
   }

   public void setGlobalLocation(VLocation loc) {
      this.mGlobalConfig.location = loc;
   }

   public VLocation getGlobalLocation() {
      return this.mGlobalConfig.location;
   }

   private static class VLocConfig implements Parcelable {
      int mode;
      VCell cell;
      List<VCell> allCell;
      List<VCell> neighboringCell;
      VLocation location;
      public static final Parcelable.Creator<VLocConfig> CREATOR = new Parcelable.Creator<VLocConfig>() {
         public VLocConfig createFromParcel(Parcel source) {
            return new VLocConfig(source);
         }

         public VLocConfig[] newArray(int size) {
            return new VLocConfig[size];
         }
      };

      public void set(VLocConfig other) {
         this.mode = other.mode;
         this.cell = other.cell;
         this.allCell = other.allCell;
         this.neighboringCell = other.neighboringCell;
         this.location = other.location;
      }

      VLocConfig() {
      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel dest, int flags) {
         dest.writeInt(this.mode);
         dest.writeParcelable(this.cell, flags);
         dest.writeTypedList(this.allCell);
         dest.writeTypedList(this.neighboringCell);
         dest.writeParcelable(this.location, flags);
      }

      VLocConfig(Parcel in) {
         this.mode = in.readInt();
         this.cell = (VCell)in.readParcelable(VCell.class.getClassLoader());
         this.allCell = in.createTypedArrayList(VCell.CREATOR);
         this.neighboringCell = in.createTypedArrayList(VCell.CREATOR);
         this.location = (VLocation)in.readParcelable(VLocation.class.getClassLoader());
      }
   }
}
