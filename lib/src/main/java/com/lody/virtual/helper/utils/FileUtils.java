package com.lody.virtual.helper.utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Build.VERSION;
import android.system.Os;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class FileUtils {
   public static String getFilenameExt(String filename) {
      int dotPos = filename.lastIndexOf(46);
      return dotPos == -1 ? "" : filename.substring(dotPos + 1);
   }

   public static File changeExt(File f, String targetExt) {
      String outPath = f.getAbsolutePath();
      if (!getFilenameExt(outPath).equals(targetExt)) {
         int dotPos = outPath.lastIndexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz5SVg==")));
         if (dotPos > 0) {
            outPath = outPath.substring(0, dotPos + 1) + targetExt;
         } else {
            outPath = outPath + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz5SVg==")) + targetExt;
         }

         return new File(outPath);
      } else {
         return f;
      }
   }

   public static String readToString(String fileName) throws IOException {
      InputStream is = new FileInputStream(fileName);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      int i;
      while((i = ((InputStream)is).read()) != -1) {
         baos.write(i);
      }

      return baos.toString();
   }

   public static long fileSize(String path) {
      File file = new File(path);
      return !file.exists() ? 0L : file.length();
   }

   public static void chmod(String path, int mode) {
      if (VERSION.SDK_INT >= 21) {
         try {
            Os.chmod(path, mode);
            return;
         } catch (Exception var7) {
         }
      }

      File file = new File(path);
      String cmd = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fDWozMyg="));
      if (file.isDirectory()) {
         cmd = cmd + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl8IDHsFSFo="));
      }

      String cmode = String.format(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PQgAVg==")), mode);

      try {
         Runtime.getRuntime().exec(cmd + cmode + " " + path).waitFor();
      } catch (IOException | InterruptedException var6) {
         Exception e = var6;
         ((Exception)e).printStackTrace();
      }

   }

   public static void createSymlink(String oldPath, String newPath) throws Exception {
      Os.symlink(oldPath, newPath);
   }

   public static boolean ensureDirCreate(File dir) {
      return dir.exists() || dir.mkdirs();
   }

   public static boolean ensureDirCreate(File... dirs) {
      boolean created = true;
      File[] var2 = dirs;
      int var3 = dirs.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         File dir = var2[var4];
         if (!ensureDirCreate(dir)) {
            created = false;
         }
      }

      return created;
   }

   public static boolean isSymlink(File file) throws IOException {
      if (file == null) {
         throw new NullPointerException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JT4YDmhSICNmASggPxcYDWU3TSprDTwbKhgEKA==")));
      } else {
         File canon;
         if (file.getParent() == null) {
            canon = file;
         } else {
            File canonDir = file.getParentFile().getCanonicalFile();
            canon = new File(canonDir, file.getName());
         }

         return !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
      }
   }

   public static void writeParcelToFile(Parcel p, File file) throws IOException {
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(p.marshall());
      fos.close();
   }

   public static byte[] toByteArray(InputStream inStream) throws IOException {
      ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
      byte[] buff = new byte[100];

      int rc;
      while((rc = inStream.read(buff, 0, 100)) > 0) {
         swapStream.write(buff, 0, rc);
      }

      return swapStream.toByteArray();
   }

   public static void copyTo(InputStream is, OutputStream os) throws IOException {
      BufferedOutputStream bos = new BufferedOutputStream(os);
      byte[] buff = new byte[4096];

      int rc;
      while((rc = is.read(buff, 0, buff.length)) > 0) {
         bos.write(buff, 0, rc);
      }

      bos.flush();
   }

   public static void deleteDir(File dir) {
      boolean isDir = dir.isDirectory();
      if (isDir) {
         boolean link = false;

         try {
            link = isSymlink(dir);
         } catch (Exception var8) {
         }

         if (!link) {
            String[] children = dir.list();
            if (children != null) {
               String[] var4 = children;
               int var5 = children.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  String file = var4[var6];
                  deleteDir(new File(dir, file));
               }
            }
         }
      }

      dir.delete();
   }

   public static void deleteDir(String dir) {
      deleteDir(new File(dir));
   }

   public static void writeToFile(InputStream dataIns, File target) throws IOException {
      boolean BUFFER = true;
      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target));

      try {
         byte[] data = new byte[1024];

         int count;
         while((count = dataIns.read(data, 0, 1024)) != -1) {
            bos.write(data, 0, count);
         }

         bos.close();
      } catch (IOException var9) {
         IOException e = var9;
         throw e;
      } finally {
         closeQuietly(bos);
      }
   }

   public static void writeToFile(byte[] data, File target) throws IOException {
      FileOutputStream fo = null;
      ReadableByteChannel src = null;
      FileChannel out = null;

      try {
         src = Channels.newChannel(new ByteArrayInputStream(data));
         fo = new FileOutputStream(target);
         out = fo.getChannel();
         out.transferFrom(src, 0L, (long)data.length);
      } finally {
         if (fo != null) {
            fo.close();
         }

         if (src != null) {
            src.close();
         }

         if (out != null) {
            out.close();
         }

      }

   }

   public static void copyFile(File source, File target) throws IOException {
      if (!source.getCanonicalPath().equals(target.getCanonicalPath())) {
         FileInputStream inputStream = null;
         FileOutputStream outputStream = null;

         try {
            inputStream = new FileInputStream(source);
            outputStream = new FileOutputStream(target);
            FileChannel iChannel = inputStream.getChannel();
            FileChannel oChannel = outputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while(true) {
               buffer.clear();
               int r = iChannel.read(buffer);
               if (r == -1) {
                  return;
               }

               buffer.limit(buffer.position());
               buffer.position(0);
               oChannel.write(buffer);
            }
         } finally {
            closeQuietly(inputStream);
            closeQuietly(outputStream);
         }
      }
   }

   public static void copyFileFromAssets(Context context, String source, File target) throws IOException {
      InputStream inputStream = null;
      FileOutputStream outputStream = null;

      try {
         inputStream = context.getResources().getAssets().open(source);
         outputStream = new FileOutputStream(target);
         byte[] buffer = new byte[1024];

         int count;
         while((count = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, count);
         }
      } finally {
         closeQuietly(inputStream);
         closeQuietly(outputStream);
      }

   }

   public static void linkDir(String sourcePath, String destPath) {
      File source = new File(sourcePath);
      File dest = new File(destPath);
      if (!dest.exists()) {
         dest.mkdirs();
      }

      File[] files = source.listFiles();
      if (!ArrayUtils.isEmpty(files)) {
         File[] var5 = files;
         int var6 = files.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            File file = var5[var7];

            try {
               Runtime.getRuntime().exec(String.format(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgbOHoaLyhIASs8OAgqVg==")), file.getAbsoluteFile(), (new File(dest, file.getName())).getAbsolutePath())).waitFor();
            } catch (Exception var10) {
               Exception e = var10;
               e.printStackTrace();
            }
         }
      }

   }

   public static void closeQuietly(Closeable closeable) {
      if (closeable != null) {
         try {
            closeable.close();
         } catch (Exception var2) {
         }
      }

   }

   public static int peekInt(byte[] bytes, int value, ByteOrder endian) {
      int v2;
      int v0;
      if (endian == ByteOrder.BIG_ENDIAN) {
         v0 = value + 1;
         v2 = v0 + 1;
         v0 = (bytes[v0] & 255) << 16 | (bytes[value] & 255) << 24 | (bytes[v2] & 255) << 8 | bytes[v2 + 1] & 255;
      } else {
         v0 = value + 1;
         v2 = v0 + 1;
         v0 = (bytes[v0] & 255) << 8 | bytes[value] & 255 | (bytes[v2] & 255) << 16 | (bytes[v2 + 1] & 255) << 24;
      }

      return v0;
   }

   private static boolean isValidExtFilenameChar(char c) {
      switch (c) {
         case '\u0000':
         case '/':
            return false;
         default:
            return true;
      }
   }

   public static boolean isValidExtFilename(String name) {
      return name != null && name.equals(buildValidExtFilename(name));
   }

   public static String buildValidExtFilename(String name) {
      if (!TextUtils.isEmpty(name) && !StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz5SVg==")).equals(name) && !StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MzocVg==")).equals(name)) {
         StringBuilder res = new StringBuilder(name.length());

         for(int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);
            if (isValidExtFilenameChar(c)) {
               res.append(c);
            } else {
               res.append('_');
            }
         }

         return res.toString();
      } else {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PBgYCGwjJCRjDg0z"));
      }
   }

   public static boolean isExist(String path) {
      return (new File(path)).exists();
   }

   public static boolean canRead(String path) {
      return (new File(path)).canRead();
   }

   public static String getPathFileName(String file) {
      String fName = file.trim();
      return fName.indexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg=="))) > -1 ? fName.substring(fName.lastIndexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg=="))) + 1) : file;
   }

   public interface FileMode {
      int MODE_ISUID = 2048;
      int MODE_ISGID = 1024;
      int MODE_ISVTX = 512;
      int MODE_IRUSR = 256;
      int MODE_IWUSR = 128;
      int MODE_IXUSR = 64;
      int MODE_IRGRP = 32;
      int MODE_IWGRP = 16;
      int MODE_IXGRP = 8;
      int MODE_IROTH = 4;
      int MODE_IWOTH = 2;
      int MODE_IXOTH = 1;
      int MODE_755 = 493;
   }
}
