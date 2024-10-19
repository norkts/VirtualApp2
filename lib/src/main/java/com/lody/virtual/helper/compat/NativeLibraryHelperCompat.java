package com.lody.virtual.helper.compat;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.VLog;
import java.io.File;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import mirror.com.android.internal.content.NativeLibraryHelper;

public class NativeLibraryHelperCompat {
   private static String TAG = NativeLibraryHelperCompat.class.getSimpleName();
   private Object handle;

   public NativeLibraryHelperCompat(File packageFile) {
      try {
         this.handle = NativeLibraryHelper.Handle.create.callWithException(packageFile);
      } catch (Throwable var3) {
         Throwable e = var3;
         VLog.e(TAG, "failed to create NativeLibraryHelper handle.");
         e.printStackTrace();
      }

   }

   public int copyNativeBinaries(File sharedLibraryDir, String abi) {
      try {
         return (Integer)NativeLibraryHelper.copyNativeBinaries.callWithException(this.handle, sharedLibraryDir, abi);
      } catch (Throwable var4) {
         Throwable e = var4;
         VLog.e(TAG, "failed to copyNativeBinaries.");
         e.printStackTrace();
         return -1;
      }
   }

   @TargetApi(21)
   public static boolean is64bitAbi(String abi) {
      return "arm64-v8a".equals(abi) || "mips64".equals(abi);
   }

   public static boolean is32bitAbi(String abi) {
      return "armeabi".equals(abi) || "armeabi-v7a".equals(abi) || "mips".equals(abi);
   }

   public static boolean isHandledAbi(String abi) {
      return is32bitAbi(abi) || is64bitAbi(abi);
   }

   public static String findSupportedAbi(String[] supportAbis, Set<String> abis) {
      String[] var2 = supportAbis;
      int var3 = supportAbis.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String supportAbi = var2[var4];
         if (abis.contains(supportAbi)) {
            return supportAbi;
         }
      }

      return null;
   }

   @TargetApi(21)
   public static boolean contain64bitAbi(Set<String> supportedAbis) {
      Iterator var1 = supportedAbis.iterator();

      String supportedAbi;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         supportedAbi = (String)var1.next();
      } while(!is64bitAbi(supportedAbi));

      return true;
   }

   public static boolean contain32bitAbi(Set<String> abiList) {
      Iterator var1 = abiList.iterator();

      String supportedAbi;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         supportedAbi = (String)var1.next();
      } while(!is32bitAbi(supportedAbi));

      return true;
   }

   public static Set<String> getPackageAbiList(String apk) {
      try {
         ZipFile apkFile = new ZipFile(apk);
         Enumeration<? extends ZipEntry> entries = apkFile.entries();
         Set<String> supportedABIs = new HashSet();

         while(entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry)entries.nextElement();
            String name = entry.getName();
            if (!name.contains("../") && name.startsWith("lib/") && !entry.isDirectory() && name.endsWith(".so")) {
               String supportedAbi = name.substring(name.indexOf("/") + 1, name.lastIndexOf("/"));
               supportedABIs.add(supportedAbi);
            }
         }

         return supportedABIs;
      } catch (Exception var7) {
         Exception e = var7;
         e.printStackTrace();
         return Collections.emptySet();
      }
   }

   @TargetApi(21)
   public static boolean support64bitAbi(Set<String> supportedABIs) {
      if (VERSION.SDK_INT < 21) {
         return false;
      } else {
         String[] cpuABIs = Build.SUPPORTED_64_BIT_ABIS;
         if (!ArrayUtils.isEmpty(cpuABIs) && supportedABIs != null && !supportedABIs.isEmpty()) {
            String[] var2 = cpuABIs;
            int var3 = cpuABIs.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               String cpuABI = var2[var4];
               Iterator var6 = supportedABIs.iterator();

               while(var6.hasNext()) {
                  String supportedABI = (String)var6.next();
                  if (TextUtils.equals(cpuABI, supportedABI)) {
                     return true;
                  }
               }
            }

            return false;
         } else {
            return false;
         }
      }
   }

   public static Set<String> getSupportAbiList(String apk) {
      try {
         ZipFile apkFile = new ZipFile(apk);
         Enumeration<? extends ZipEntry> entries = apkFile.entries();
         Set<String> supportedABIs = new HashSet();

         while(entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry)entries.nextElement();
            String name = entry.getName();
            if (!name.contains("../") && name.startsWith("lib/") && !entry.isDirectory() && name.endsWith(".so")) {
               String supportedAbi = name.substring(name.indexOf("/") + 1, name.lastIndexOf("/"));
               supportedABIs.add(supportedAbi);
            }
         }

         return supportedABIs;
      } catch (Exception var7) {
         Exception e = var7;
         e.printStackTrace();
         return Collections.emptySet();
      }
   }

   public static class SoLib {
      public String ABI;
      public String path;

      public SoLib() {
      }

      public SoLib(String ABI, String path) {
         this.ABI = ABI;
         this.path = path;
      }

      public boolean is64Bit() {
         return this.ABI != null && NativeLibraryHelperCompat.is64bitAbi(this.ABI);
      }
   }
}
