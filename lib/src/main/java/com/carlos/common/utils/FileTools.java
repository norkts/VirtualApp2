package com.carlos.common.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.TextUtils;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;

public class FileTools {
   public static boolean delete(String delFile) {
      File file = new File(delFile);
      if (!file.exists()) {
         return false;
      } else {
         return file.isFile() ? deleteSingleFile(delFile) : deleteDirectory(delFile);
      }
   }

   private static boolean deleteSingleFile(String filePath$Name) {
      File file = new File(filePath$Name);
      if (file.exists() && file.isFile()) {
         return file.delete();
      } else {
         return false;
      }
   }

   private static boolean deleteDirectory(String filePath) {
      if (!filePath.endsWith(File.separator)) {
         filePath = filePath + File.separator;
      }

      File dirFile = new File(filePath);
      if (dirFile.exists() && dirFile.isDirectory()) {
         boolean flag = true;
         File[] files = dirFile.listFiles();
         File[] var4 = files;
         int var5 = files.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File file = var4[var6];
            if (file.isFile()) {
               flag = deleteSingleFile(file.getAbsolutePath());
               if (!flag) {
                  break;
               }
            } else if (file.isDirectory()) {
               flag = deleteDirectory(file.getAbsolutePath());
               if (!flag) {
                  break;
               }
            }
         }

         if (!flag) {
            return false;
         } else {
            return dirFile.delete();
         }
      } else {
         return false;
      }
   }

   public static boolean saveAsFileWriter(String filePath, String content) {
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Bwk/WkYWQj5YXhNTAhtABEctEwJ+N1RF")) + filePath);
      File file = new File(filePath);
      file.deleteOnExit();
      FileWriter fwriter = null;

      try {
         fwriter = new FileWriter(filePath, false);
         fwriter.write(content);
         boolean var16 = true;
         return var16;
      } catch (IOException var14) {
         IOException ex = var14;
         HVLog.printException(ex);
      } finally {
         try {
            fwriter.flush();
            fwriter.close();
         } catch (IOException var13) {
            IOException ex = var13;
            ex.printStackTrace();
         }

      }

      return false;
   }

   public static String saveBitmap(Bitmap bm, String imageFile) {
      String filename = imageFile.substring(imageFile.lastIndexOf(File.separator) + 1);
      String filepath = imageFile.substring(0, imageFile.lastIndexOf(File.separator));
      File path = new File(filepath);
      if (!path.exists()) {
         path.mkdirs();
      }

      File file = new File(filepath, filename);
      if (file.exists()) {
         file.delete();
      }

      FileOutputStream outputStream = null;

      try {
         Object var8;
         try {
            outputStream = new FileOutputStream(file);
            bm.compress(CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
         } catch (FileNotFoundException var14) {
            FileNotFoundException e = var14;
            e.printStackTrace();
            var8 = null;
            return (String)var8;
         } catch (IOException var15) {
            IOException e = var15;
            e.printStackTrace();
            var8 = null;
            return (String)var8;
         } catch (Exception var16) {
            var8 = null;
            return (String)var8;
         }
      } finally {
         ;
      }

      return file.getAbsolutePath();
   }

   public static void saveBitmap(final Context context, Bitmap bm, boolean updateAlum) {
      String fileName = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+LmhSBgJgNDhF"));
      final File file = new File(Environment.getExternalStoragePublicDirectory(""), fileName);
      if (file.exists()) {
         file.delete();
      }

      FileNotFoundException e;
      try {
         FileOutputStream out = new FileOutputStream(file);
         bm.compress(CompressFormat.PNG, 90, out);
         out.flush();
         out.close();
      } catch (FileNotFoundException var7) {
         e = var7;
         e.printStackTrace();
      } catch (IOException var8) {
         var8.printStackTrace();
      }

      if (updateAlum) {
         try {
            Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, (String)null);
            String[] projection = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy4YPA==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy4qP2wFJFo="))};
            Cursor cursor = context.getContentResolver().query(Thumbnails.EXTERNAL_CONTENT_URI, projection, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy4YPHsOHSh0J1RF")), new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OgMLKQ=="))}, (String)null);

            while(cursor.moveToNext()) {
            }

            context.sendBroadcast(new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42QQpmHBoAJQUYH2ALBl9gHAoALysuAmQxRVVkJSQK")), Uri.parse(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YDmhTTCVOJ1RF")) + file.getPath())));
         } catch (FileNotFoundException var9) {
            e = var9;
            e.printStackTrace();
         }

         (new Handler()).postDelayed(new Runnable() {
            public void run() {
               String where = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy4qP2wFJyhgHgYxKAMlOA==")) + file.getAbsolutePath() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PQQMVg=="));
               int i = context.getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, where, (String[])null);
               if (i > 0) {
               }

            }
         }, 15000L);
      }

   }

   public static String readFile(String filePath) {
      String content = "";

      try {
         InputStream instream = new FileInputStream(filePath);
         if (instream != null) {
            InputStreamReader inputreader = new InputStreamReader(instream, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQUqW3pTRVo=")));
            new BufferedReader(inputreader);
            char[] chars = new char[1024];

            for(int len = 0; (len = inputreader.read(chars)) != -1; content = content + new String(chars, 0, len)) {
            }

            ((InputStream)instream).close();
         }
      } catch (FileNotFoundException var7) {
      } catch (IOException var8) {
      }

      return content;
   }

   public static int copyDir(String fromFile, String toFile) {
      File root = new File(fromFile);
      if (!root.exists()) {
         return -1;
      } else {
         File[] currentFiles = root.listFiles();
         File targetDir = new File(toFile);
         if (!targetDir.exists()) {
            targetDir.mkdirs();
         }

         for(int i = 0; i < currentFiles.length; ++i) {
            if (currentFiles[i].isDirectory()) {
               copyDir(currentFiles[i].getPath() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")), toFile + currentFiles[i].getName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
            } else {
               copyFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
            }
         }

         return 0;
      }
   }

   public static String getFileNameByPath(String path, boolean suffix) {
      if (path == null) {
         return "";
      } else {
         int start = path.lastIndexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
         int end = suffix ? path.length() : path.lastIndexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz5SVg==")));
         return start != -1 && end != -1 ? path.substring(start + 1, end) : "";
      }
   }

   public static String getFileDirByPath(String path) {
      if (path == null) {
         return "";
      } else {
         int start = 0;
         int end = path.lastIndexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
         return start != -1 && end != -1 ? path.substring(start, end + 1) : "";
      }
   }

   public static void copyFolder(String oldPath, String newPath) {
      try {
         (new File(newPath)).mkdirs();
         File a = new File(oldPath);
         String[] file = a.list();
         File temp = null;

         for(int i = 0; i < file.length; ++i) {
            if (oldPath.endsWith(File.separator)) {
               temp = new File(oldPath + file[i]);
            } else {
               temp = new File(oldPath + File.separator + file[i]);
            }

            if (temp.isFile()) {
               FileInputStream input = new FileInputStream(temp);
               FileOutputStream output = new FileOutputStream(newPath + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + temp.getName().toString());
               byte[] b = new byte[5120];

               int len;
               while((len = input.read(b)) != -1) {
                  output.write(b, 0, len);
               }

               output.flush();
               output.close();
               input.close();
            }

            if (temp.isDirectory()) {
               copyFolder(oldPath + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + file[i], newPath + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + file[i]);
            }
         }
      } catch (Exception var10) {
      }

   }

   public static void copyFile(String oldPath, String newPath) {
      try {
         int bytesum = 0;
         File oldfile = new File(oldPath);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxwnREZJRj5YEF5KAlcdCkdNGxdBX149PQArBhpSHlo=")) + newPath + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsFGiRiHjwzKhcLDmkgFi9sJCw6MwQXPg==")) + oldfile.exists());
         if (oldfile.exists()) {
            InputStream inStream = new FileInputStream(oldPath);
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1444];

            int byteread;
            while((byteread = ((InputStream)inStream).read(buffer)) != -1) {
               bytesum += byteread;
               System.out.println(bytesum);
               fs.write(buffer, 0, byteread);
            }

            ((InputStream)inStream).close();
         }
      } catch (Exception var9) {
         Exception e = var9;
         HVLog.printException(e);
      }

   }

   public static String FormetFileSize(long fileS) {
      DecimalFormat df = new DecimalFormat(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PiobKH8FSFo=")));
      String fileSizeString = "";
      if (fileS < 1024L) {
         fileSizeString = df.format((double)fileS) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jj5SVg=="));
      } else if (fileS < 1048576L) {
         fileSizeString = df.format((double)fileS / 1024.0) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JC5SVg=="));
      } else if (fileS < 1073741824L) {
         fileSizeString = df.format((double)fileS / 1048576.0) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwhSVg=="));
      } else {
         fileSizeString = df.format((double)fileS / 1.073741824E9) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JS5SVg=="));
      }

      return fileSizeString;
   }

   public static String getFileNameWithParams(String path, int type) {
      if (!TextUtils.isEmpty(path) && type > 0 && type <= 4) {
         int start = path.lastIndexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
         if (start != -1) {
            if (type == 1) {
               return path.substring(start + 1);
            } else if (type == 2) {
               return path.substring(0, start + 1);
            } else if (type == 3) {
               int index = path.lastIndexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz5SVg==")));
               return path.substring(index + 1);
            } else if (type == 4) {
               String substring = path.substring(0, start);
               int indexOf = substring.lastIndexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
               return indexOf != -1 ? substring.substring(indexOf + 1) : "";
            } else {
               return "";
            }
         } else {
            return "";
         }
      } else {
         throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PlsnJBwNMRUGFgsLXDU7Bx87BzQVPy0zEQQ6Vg==")));
      }
   }

   public static void inputstreamtofile(InputStream ins, File file) {
      try {
         if (file.exists()) {
            file.deleteOnExit();
         } else {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
               file.getParentFile().mkdirs();
            }

            file.createNewFile();
         }

         OutputStream os = new FileOutputStream(file);
         byte[] buffer = new byte[8192];

         int bytesRead;
         while((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            ((OutputStream)os).write(buffer, 0, bytesRead);
         }

         ((OutputStream)os).close();
         ins.close();
      } catch (Exception var5) {
      }

   }
}
