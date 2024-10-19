package com.lody.virtual.helper;

import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.VLog;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ApkLibraryHelper {
   public static final String[] DEFAULT_SUPPORTED_ABI_32BIT = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcMDWgVJCpjAVRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcMDWgVJCpjCl0uPC0iVg=="))};
   public static final String[] DEFAULT_SUPPORTED_ABI_64BIT = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcMDXwkMyNmMxo7"))};
   public static final String[] ABI_32BIT = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcMDWgVJCpjAVRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcMDWgVJCpjCl0uPC0iVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KF5eLg=="))};
   public static final String[] ABI_64BIT = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcMDXwkMyNmMxo7")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KF5eLmY0OwY="))};
   public static final int INSTALL_SUCCEEDED = 1;
   public static final int INSTALL_FAILED_INVALID_APK = -2;
   public static final int INSTALL_FAILED_NO_MATCHING_ABIS = -113;
   public static final int NO_NATIVE_LIBRARIES = -114;
   private ZipFile apkFile;

   public ApkLibraryHelper(File file) {
      try {
         this.apkFile = new ZipFile(file, 1);
      } catch (IOException var3) {
         IOException e = var3;
         e.printStackTrace();
      }

   }

   public int copyNativeBinaries(File sharedLibraryDir, String abi) {
      if (this.apkFile == null) {
         return -2;
      } else {
         Enumeration<? extends ZipEntry> entries = this.apkFile.entries();
         byte[] buf = new byte[8192];

         while(true) {
            ZipEntry entry;
            String name;
            String prefix;
            do {
               do {
                  if (!entries.hasMoreElements()) {
                     return 1;
                  }

                  entry = (ZipEntry)entries.nextElement();
               } while(entry.isDirectory());

               name = entry.getName();
               prefix = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgYOnozSFo=")) + abi + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg=="));
            } while(!name.startsWith(prefix));

            String fileName = name.substring(prefix.length());
            File extractFile = new File(sharedLibraryDir, fileName);
            VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgc6MWIFAiphNCAqLQUAPW8wTStsN1RF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IS0MCWwFAiZiIwU8")) + extractFile);

            try {
               extractFile.createNewFile();
               BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(extractFile));
               InputStream is = this.apkFile.getInputStream(entry);

               int rc;
               while((rc = is.read(buf, 0, 100)) > 0) {
                  os.write(buf, 0, rc);
               }

               os.flush();
               os.close();
            } catch (IOException var13) {
               IOException e = var13;
               throw new RuntimeException(e);
            }
         }
      }
   }

   public int findSupportedAbi(String[] supportedAbis) {
      if (this.apkFile == null) {
         return -2;
      } else {
         int status = -114;
         Enumeration<? extends ZipEntry> entries = this.apkFile.entries();

         while(true) {
            String name;
            do {
               ZipEntry entry;
               do {
                  if (!entries.hasMoreElements()) {
                     return status;
                  }

                  status = -113;
                  entry = (ZipEntry)entries.nextElement();
               } while(entry.isDirectory());

               name = entry.getName();
            } while(!name.startsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgYOnozSFo="))));

            for(int i = 0; i < supportedAbis.length; ++i) {
               String supportAbi = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgYOnozSFo=")) + supportedAbis[i];
               if (name.startsWith(supportAbi)) {
                  return i;
               }
            }
         }
      }
   }
}
