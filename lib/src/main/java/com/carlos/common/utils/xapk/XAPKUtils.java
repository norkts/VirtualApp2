package com.carlos.common.utils.xapk;

import java.io.File;

public class XAPKUtils {
   public static boolean createOrExistsDir(String dirPath) {
      return createOrExistsDir(getFileByPath(dirPath));
   }

   public static boolean createOrExistsDir(File file) {
      return file != null && file.exists() && file.isDirectory() ? true : file.mkdirs();
   }

   public static File getFileByPath(String filePath) {
      return isSpace(filePath) ? null : new File(filePath);
   }

   public static String getFileName(File file) {
      return file == null ? "" : getFileName(file.getAbsolutePath());
   }

   public static String getFileName(String filePath) {
      if (isSpace(filePath)) {
         return "";
      } else {
         int lastSep = filePath.lastIndexOf(File.separator);
         return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
      }
   }

   public static String getFileNameNoExtension(File file) {
      return file == null ? "" : getFileNameNoExtension(file.getPath());
   }

   public static String getFileNameNoExtension(String filePath) {
      if (isSpace(filePath)) {
         return "";
      } else {
         int lastPoi = filePath.lastIndexOf(46);
         int lastSep = filePath.lastIndexOf(File.separator);
         if (lastSep == -1) {
            return lastPoi == -1 ? filePath : filePath.substring(0, lastPoi);
         } else {
            return lastPoi != -1 && lastSep <= lastPoi ? filePath.substring(lastSep + 1, lastPoi) : filePath.substring(lastSep + 1);
         }
      }
   }

   private static boolean isSpace(String s) {
      if (s == null) {
         return true;
      } else {
         int i = 0;

         for(int len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
               return false;
            }
         }

         return true;
      }
   }
}
