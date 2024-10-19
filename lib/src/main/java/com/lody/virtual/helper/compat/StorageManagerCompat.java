package com.lody.virtual.helper.compat;

import android.content.Context;
import android.os.storage.StorageManager;
import com.lody.virtual.StringFog;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class StorageManagerCompat {
   private StorageManagerCompat() {
   }

   public static String[] getAllPoints(Context context) {
      StorageManager manager = (StorageManager)context.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qD28jJC1iAVRF")));
      String[] points = null;

      try {
         Method method = manager.getClass().getMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjGiRmDl0/OxciLmwwAlo=")));
         points = (String[])method.invoke(manager);
      } catch (Exception var4) {
         Exception e = var4;
         e.printStackTrace();
      }

      return points;
   }

   public static boolean isMounted(Context context, String point) {
      if (point == null) {
         return false;
      } else {
         StorageManager manager = (StorageManager)context.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qD28jJC1iAVRF")));

         try {
            Method method = manager.getClass().getMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjGiRmDl0/Oy42OWUzGlo=")), String.class);
            String state = (String)method.invoke(manager, point);
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwgAI2ogMCtiEVRF")).equals(state);
         } catch (Exception var5) {
            Exception e = var5;
            e.printStackTrace();
            return false;
         }
      }
   }

   public static ArrayList<String> getMountedPoints(Context context) {
      StorageManager manager = (StorageManager)context.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qD28jJC1iAVRF")));
      ArrayList<String> mountedPoints = new ArrayList();

      try {
         Method getVolumePaths = manager.getClass().getMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjGiRmDl0/OxciLmwwAlo=")));
         String[] points = (String[])getVolumePaths.invoke(manager);
         if (points != null && points.length > 0) {
            Method getVolumeState = manager.getClass().getMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjGiRmDl0/Oy42OWUzGlo=")), String.class);
            String[] var6 = points;
            int var7 = points.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String point = var6[var8];
               String state = (String)getVolumeState.invoke(manager, point);
               if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwgAI2ogMCtiEVRF")).equals(state)) {
                  mountedPoints.add(point);
               }
            }

            return mountedPoints;
         }
      } catch (Exception var11) {
         Exception e = var11;
         e.printStackTrace();
      }

      return null;
   }
}
