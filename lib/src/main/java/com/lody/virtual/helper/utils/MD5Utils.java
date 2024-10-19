package com.lody.virtual.helper.utils;

import android.text.TextUtils;
import android.util.Base64;
import com.lody.virtual.StringFog;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
   protected static char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   protected static MessageDigest MESSAGE_DIGEST_5 = null;

   public static String getFileMD5String(File file) throws IOException {
      InputStream fis = new FileInputStream(file);
      byte[] buffer = new byte[1024];

      int numRead;
      while((numRead = ((InputStream)fis).read(buffer)) > 0) {
         MESSAGE_DIGEST_5.update(buffer, 0, numRead);
      }

      ((InputStream)fis).close();
      return bufferToHex(MESSAGE_DIGEST_5.digest());
   }

   public static String hashBase64(byte[] bs) {
      ByteArrayInputStream in = new ByteArrayInputStream(bs);
      MessageDigest SHA = null;

      Object var4;
      try {
         SHA = MessageDigest.getInstance(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IixfEXpTJFo=")));
         byte[] buffer = new byte[1024];

         int numRead;
         while((numRead = in.read(buffer)) > 0) {
            SHA.update(buffer, 0, numRead);
         }

         return Base64.encodeToString(SHA.digest(), 0);
      } catch (Exception var14) {
         var4 = null;
      } finally {
         try {
            in.close();
         } catch (IOException var13) {
         }

      }

      return (String)var4;
   }

   public static String getFileMD5String(InputStream in) throws IOException {
      byte[] buffer = new byte[1024];

      int numRead;
      while((numRead = in.read(buffer)) > 0) {
         MESSAGE_DIGEST_5.update(buffer, 0, numRead);
      }

      in.close();
      return bufferToHex(MESSAGE_DIGEST_5.digest());
   }

   private static String bufferToHex(byte[] bytes) {
      return bufferToHex(bytes, 0, bytes.length);
   }

   private static String bufferToHex(byte[] bytes, int m, int n) {
      StringBuffer stringbuffer = new StringBuffer(2 * n);
      int k = m + n;

      for(int l = m; l < k; ++l) {
         appendHexPair(bytes[l], stringbuffer);
      }

      return stringbuffer.toString();
   }

   private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
      char c0 = HEX_DIGITS[(bt & 240) >> 4];
      char c1 = HEX_DIGITS[bt & 15];
      stringbuffer.append(c0);
      stringbuffer.append(c1);
   }

   public static boolean compareFiles(File one, File two) throws IOException {
      if (one.getAbsolutePath().equals(two.getAbsolutePath())) {
         return true;
      } else {
         String md5_1 = getFileMD5String(one);
         String md5_2 = getFileMD5String(two);
         return TextUtils.equals(md5_1, md5_2);
      }
   }

   static {
      try {
         MESSAGE_DIGEST_5 = MessageDigest.getInstance(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwYpIw==")));
      } catch (NoSuchAlgorithmException var1) {
         NoSuchAlgorithmException e = var1;
         e.printStackTrace();
      }

   }
}
