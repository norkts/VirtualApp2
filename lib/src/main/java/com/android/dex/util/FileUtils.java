package com.android.dex.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class FileUtils {
   private FileUtils() {
   }

   public static byte[] readFile(String fileName) {
      File file = new File(fileName);
      return readFile(file);
   }

   public static byte[] readFile(File file) {
      if (!file.exists()) {
         throw new RuntimeException(file + ": file not found");
      } else if (!file.isFile()) {
         throw new RuntimeException(file + ": not a file");
      } else if (!file.canRead()) {
         throw new RuntimeException(file + ": file not readable");
      } else {
         long longLength = file.length();
         int length = (int)longLength;
         if ((long)length != longLength) {
            throw new RuntimeException(file + ": file too long");
         } else {
            byte[] result = new byte[length];

            try {
               FileInputStream in = new FileInputStream(file);

               int amt;
               for(int at = 0; length > 0; length -= amt) {
                  amt = in.read(result, at, length);
                  if (amt == -1) {
                     throw new RuntimeException(file + ": unexpected EOF");
                  }

                  at += amt;
               }

               in.close();
               return result;
            } catch (IOException var8) {
               IOException ex = var8;
               throw new RuntimeException(file + ": trouble reading", ex);
            }
         }
      }
   }

   public static boolean hasArchiveSuffix(String fileName) {
      return fileName.endsWith(".zip") || fileName.endsWith(".jar") || fileName.endsWith(".apk");
   }
}
