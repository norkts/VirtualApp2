package com.carlos.common.utils;

import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipTools {
   private static int result = 0;
   private static final String SECRET_KEY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uKW8kJwFPVjhF"));

   public static int compressZip(String src, String dest, ZipCallback zipCallback) {
      ZipOutputStream out = null;
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlcJMUZJAwtYK0YzIy4uP3gVSFo=")) + src + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsKTS9hUx8cRCUJWB1WEz0eLy01UwNXVg==")) + dest);

      try {
         File outFile = new File(dest);
         File fileOrDirectory = new File(src);
         out = new ZipOutputStream(new FileOutputStream(outFile));
         if (fileOrDirectory.isFile()) {
            zipFileOrDirectory(out, fileOrDirectory, "", zipCallback);
         } else {
            File[] entries = fileOrDirectory.listFiles();

            for(int i = 0; i < entries.length; ++i) {
               zipFileOrDirectory(out, entries[i], "", zipCallback);
            }
         }
      } catch (IOException var16) {
         IOException ex = var16;
         HVLog.printException(ex);
      } finally {
         if (out != null) {
            try {
               out.close();
            } catch (IOException var15) {
               IOException ex = var15;
               HVLog.printException(ex);
            }
         }

      }

      return result;
   }

   private static String checkString(String sourceFileName) {
      if (sourceFileName.indexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz5SVg=="))) > 0) {
         sourceFileName = sourceFileName.substring(0, sourceFileName.length() - 4);
         HVLog.i(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fM2szQV5mESwzKj05IH47GygdCRMhQABcOR8oIVIbESweIy1fJWsLOAVqASgfOwdbLEQHBwFFEjlI")) + sourceFileName);
      }

      return sourceFileName;
   }

   public static int uncompressZip(String zipFileName, String outputDirectory, UnZipCallback unZipCallback) {
      ZipFile zipFile = null;

      try {
         zipFile = new ZipFile(zipFileName);
         Enumeration e = zipFile.entries();
         ZipEntry zipEntry = null;
         File dest = new File(outputDirectory);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgqM28wMyh3N1RF")) + dest.exists());
         if (!dest.exists()) {
            dest.mkdirs();
         }

         while(e.hasMoreElements()) {
            zipEntry = (ZipEntry)e.nextElement();
            String entryName = zipEntry.getName();
            InputStream in = null;
            FileOutputStream out = null;
            unZipCallback.callbackName(entryName);
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhguCGwKFj9oNCA3KAMlIA==")) + entryName);

            try {
               File f;
               if (zipEntry.isDirectory()) {
                  String name = zipEntry.getName();
                  name = name.substring(0, name.length() - 1);
                  f = new File(outputDirectory + File.separator + name);
                  f.mkdirs();
               } else {
                  int index = entryName.lastIndexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JxhSVg==")));
                  if (index != -1) {
                     f = new File(outputDirectory + File.separator + entryName.substring(0, index));
                     f.mkdirs();
                  }

                  index = entryName.lastIndexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
                  if (index != -1) {
                     f = new File(outputDirectory + File.separator + entryName.substring(0, index));
                     f.mkdirs();
                  }

                  f = new File(outputDirectory + File.separator + zipEntry.getName());
                  in = zipFile.getInputStream(zipEntry);
                  out = new FileOutputStream(f);
                  byte[] by = new byte[1024];

                  int c;
                  while((c = in.read(by)) != -1) {
                     out.write(by, 0, c);
                  }

                  out.flush();
               }
            } catch (IOException var41) {
               IOException ex = var41;
               ex.printStackTrace();
               HVLog.printException(ex);
               throw new IOException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlcjOUZJAwtYEg8rA1cNPUUWJVc=")) + ex.toString());
            } finally {
               IOException ex;
               if (in != null) {
                  try {
                     in.close();
                  } catch (IOException var40) {
                     ex = var40;
                     HVLog.printException(ex);
                  }
               }

               if (out != null) {
                  try {
                     out.close();
                  } catch (IOException var39) {
                     ex = var39;
                     HVLog.printException(ex);
                  }
               }

            }
         }
      } catch (IOException var43) {
         IOException ex = var43;
         HVLog.printException(ex);
      } finally {
         if (zipFile != null) {
            try {
               zipFile.close();
            } catch (IOException var38) {
               IOException ex = var38;
               HVLog.printException(ex);
            }
         }

      }

      return result;
   }

   private static void zipFileOrDirectory(ZipOutputStream out, File fileOrDirectory, String curPath, ZipCallback zipCallback) throws IOException {
      FileInputStream in = null;

      try {
         int i;
         if (!fileOrDirectory.isDirectory()) {
            byte[] buffer = new byte[4096];
            in = new FileInputStream(fileOrDirectory);
            ZipEntry entry = new ZipEntry(curPath + fileOrDirectory.getName());
            zipCallback.callbackName(fileOrDirectory.getName());
            out.putNextEntry(entry);

            while((i = in.read(buffer)) != -1) {
               out.write(buffer, 0, i);
            }

            out.closeEntry();
         } else {
            File[] entries = fileOrDirectory.listFiles();

            for(i = 0; i < entries.length; ++i) {
               zipFileOrDirectory(out, entries[i], curPath + fileOrDirectory.getName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")), zipCallback);
            }
         }
      } catch (IOException var16) {
         IOException ex = var16;
         HVLog.printException(ex);
      } finally {
         if (in != null) {
            try {
               in.close();
            } catch (IOException var15) {
               IOException ex = var15;
               HVLog.printException(ex);
            }
         }

      }

   }

   public interface UnZipCallback {
      void callbackName(String var1);
   }

   public interface ZipCallback {
      void callbackName(String var1);
   }
}
