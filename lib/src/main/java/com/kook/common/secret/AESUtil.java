package com.kook.common.secret;

import com.kook.common.utils.HVLog;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class AESUtil {
   private static final String SECRET_KEY = "sesr1107";

   public static void main(String[] args) {
      String content = "";
      String key = "20171117";
      System.currentTimeMillis();
      System.out.println("加密前：" + content);
      byte[] encrypted = desEncrypt(content.getBytes(), key.getBytes());
      System.out.println("加密后：" + byteToHexString(encrypted));
      byte[] decrypted = desDecrypt(encrypted, key.getBytes());
      System.out.println("解密后：" + new String(decrypted));
   }

   public static String desEncrypt(String content) {
      byte[] desEncrypt = desEncrypt(content.getBytes(), "sesr1107".getBytes());
      return byteToHexString(desEncrypt);
   }

   private static byte[] desEncrypt(byte[] content, byte[] keyBytes) {
      try {
         DESKeySpec keySpec = new DESKeySpec(keyBytes);
         SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
         SecretKey key = keyFactory.generateSecret(keySpec);
         Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
         cipher.init(1, key, new IvParameterSpec(keySpec.getKey()));
         byte[] result = cipher.doFinal(content);
         return result;
      } catch (Exception var7) {
         Exception e = var7;
         System.out.println("exception:" + e.toString());
         return null;
      }
   }

   public static String desDecrypt(String content) {
      HVLog.e("VA-DSD", " content  111  :" + content);
      byte[] bytes = hexStringToByteArray(content);
      byte[] desDecrypt = desDecrypt(bytes, "sesr1107".getBytes());
      if (desDecrypt == null) {
         return null;
      } else {
         String time = new String(desDecrypt);
         return time;
      }
   }

   public static String desDecrypt(byte[] content) {
      byte[] desDecrypt = desDecrypt(content, "sesr1107".getBytes());
      return new String(desDecrypt);
   }

   private static byte[] desDecrypt(byte[] content, byte[] keyBytes) {
      try {
         DESKeySpec keySpec = new DESKeySpec(keyBytes);
         SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
         SecretKey key = keyFactory.generateSecret(keySpec);
         Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
         cipher.init(2, key, new IvParameterSpec(keyBytes));
         byte[] result = cipher.doFinal(content);
         return result;
      } catch (Exception var7) {
         Exception e = var7;
         HVLog.e("VA-DSD", " desDecrypt exception  :" + e.toString());
         return null;
      }
   }

   private static String byteToHexString(byte[] bytes) {
      StringBuffer sb = new StringBuffer(bytes.length);

      for(int i = 0; i < bytes.length; ++i) {
         String sTemp = Integer.toHexString(255 & bytes[i]);
         if (sTemp.length() < 2) {
            sb.append(0);
         }

         sb.append(sTemp);
      }

      return sb.toString();
   }

   public static byte[] hexStringToByteArray(String s) {
      int len = s.length();
      byte[] b = new byte[len / 2];

      for(int i = 0; i < len; i += 2) {
         b[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
      }

      return b;
   }
}
