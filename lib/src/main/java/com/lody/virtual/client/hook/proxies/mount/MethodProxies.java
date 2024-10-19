package com.lody.virtual.client.hook.proxies.mount;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.storage.StorageVolume;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.VLog;
import java.io.File;
import java.lang.reflect.Method;

class MethodProxies {
   public static File file = null;

   static class Mkdirs extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwhbPGUaFgM="));
      }

      public boolean beforeCall(Object who, Method method, Object... args) {
         MethodParameterUtils.replaceFirstAppPkg(args);
         return super.beforeCall(who, method, args);
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (VERSION.SDK_INT < 19) {
            return super.call(who, method, args);
         } else {
            String path;
            if (args.length == 1) {
               path = (String)args[0];
            } else {
               path = (String)args[1];
            }

            File file = new File(path);
            return !file.exists() && !file.mkdirs() ? -1 : 0;
         }
      }
   }

   static class GetVolumeList extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQjGiRmDl0/IhccL2UzSFo="));
      }

      public boolean beforeCall(Object who, Method method, Object... args) {
         if (args != null && args.length != 0) {
            if (args[0] instanceof Integer) {
               if (BuildCompat.isT()) {
                  args[0] = getRealUserId();
               } else {
                  args[0] = getRealUid();
               }
            }

            MethodParameterUtils.replaceFirstAppPkg(args);
            return super.beforeCall(who, method, args);
         } else {
            return super.beforeCall(who, method, args);
         }
      }

      private boolean checkPackageSdcard(String appPkg) {
         return appPkg.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojBitnHh42Oj0AMWU0RVo="))) || appPkg.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojBitnHh42Oj0MKA==")));
      }

      public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
         if (this.checkPackageSdcard(getAppPkg()) && StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+DW8wNCZiJ1RF")).equals(Build.BRAND) && VERSION.SDK_INT == 29) {
            VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Bz8nL0YWWgBgDh4vKj41OmoFGgRvNx4qLhc1JGgKLD9vHlktJD1fKW4VAj9vMzw2Ji1aJWwwFi5vAS85LBgYLH8nIi4YXhw+XkBdPRQBFjdlNyw5LD4YIEtSPzN5UwsxX1sNGwINMQQaLy1TER8NABw4LUwfPwtNWDYVOhoJGCAZKRgzXT9ZPR8HGC8UKQAgWT9EJ1dJPiIUAAgiRBwoIx0ERzBUBRw0QEBZLRkEATZ+N1RF")) + getAppPkg());
            StorageVolume[] storageVolumes = (StorageVolume[])result;
            if (MethodProxies.file == null) {
            }

            if (MethodProxies.file == null) {
               new NullPointerException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YDmhSIC9hICQ2Ki41Om8aGiRlHjwcIxgcCmIFMFo=")));
            }

            if (!MethodProxies.file.exists()) {
               MethodProxies.file.mkdirs();
            }

            StorageVolume[] var6 = storageVolumes;
            int var7 = storageVolumes.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               StorageVolume storageVolume = var6[var8];
               File directory;
               if (mirror.android.os.storage.StorageVolume.mPath != null) {
                  directory = (File)mirror.android.os.storage.StorageVolume.mPath.get(storageVolume);
                  mirror.android.os.storage.StorageVolume.mPath.set(storageVolume, MethodProxies.file);
               }

               if (mirror.android.os.storage.StorageVolume.mInternalPath != null) {
                  directory = (File)mirror.android.os.storage.StorageVolume.mInternalPath.get(storageVolume);
                  mirror.android.os.storage.StorageVolume.mInternalPath.set(storageVolume, MethodProxies.file);
               }
            }
         }

         return result;
      }
   }
}
