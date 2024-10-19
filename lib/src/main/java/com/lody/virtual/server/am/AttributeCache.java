package com.lody.virtual.server.am;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.SparseArray;
import com.lody.virtual.client.core.VirtualCore;
import java.util.HashMap;
import java.util.WeakHashMap;

public final class AttributeCache {
   private static final AttributeCache sInstance = new AttributeCache();
   private final WeakHashMap<String, Package> mPackages = new WeakHashMap();
   private final Configuration mConfiguration = new Configuration();

   private AttributeCache() {
   }

   public static AttributeCache instance() {
      return sInstance;
   }

   public void removePackage(String packageName) {
      synchronized(this) {
         this.mPackages.remove(packageName);
      }
   }

   public void updateConfiguration(Configuration config) {
      synchronized(this) {
         int changes = this.mConfiguration.updateFrom(config);
         if ((changes & -1073741985) != 0) {
            this.mPackages.clear();
         }

      }
   }

   public Entry get(String packageName, int resId, int[] styleable) {
      synchronized(this) {
         Package pkg = (Package)this.mPackages.get(packageName);
         HashMap<int[], Entry> map = null;
         Entry ent;
         if (pkg != null) {
            map = (HashMap)pkg.mMap.get(resId);
            if (map != null) {
               ent = (Entry)map.get(styleable);
               if (ent != null) {
                  return ent;
               }
            }
         } else {
            Resources res;
            try {
               res = VirtualCore.get().getResources(packageName);
            } catch (Throwable var12) {
               return null;
            }

            pkg = new Package(res);
            this.mPackages.put(packageName, pkg);
         }

         if (map == null) {
            map = new HashMap();
            pkg.mMap.put(resId, map);
         }

         try {
            ent = new Entry(pkg.resources, pkg.resources.newTheme().obtainStyledAttributes(resId, styleable));
            map.put(styleable, ent);
         } catch (Resources.NotFoundException var11) {
            return null;
         }

         return ent;
      }
   }

   public static final class Entry {
      public final Resources resource;
      public final TypedArray array;

      public Entry(Resources res, TypedArray ta) {
         this.resource = res;
         this.array = ta;
      }
   }

   public static final class Package {
      public final Resources resources;
      private final SparseArray<HashMap<int[], Entry>> mMap = new SparseArray();

      public Package(Resources res) {
         this.resources = res;
      }
   }
}
