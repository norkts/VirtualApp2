package com.carlos.common.utils;

import com.carlos.libcommon.StringFog;
import com.lody.virtual.helper.utils.VLog;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class AESUtil {
   private static final String SECRET_KEY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uKW8kJwFPVjhF"));

   public static void main(String[] args) {
      String content = "";
      String key = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ojk5L3w0JwFPDThF"));
      System.currentTimeMillis();
      System.out.println(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxpAOEZaBwhYEBsNDCJYGg==")) + content);
      byte[] encrypted = desEncrypt(content.getBytes(), key.getBytes());
      System.out.println(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxpAOEZaBwhYEzkMDCJYGg==")) + byteToHexString(encrypted));
      byte[] decrypted = desDecrypt(encrypted, key.getBytes());
      System.out.println(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlcjOUZaBwhYEzkMDCJYGg==")) + new String(decrypted));
   }

   public static String desEncrypt(String content) {
      byte[] desEncrypt = desEncrypt(content.getBytes(), SECRET_KEY.getBytes());
      return byteToHexString(desEncrypt);
   }

   private static byte[] desEncrypt(byte[] content, byte[] keyBytes) {
      try {
         DESKeySpec keySpec = new DESKeySpec(keyBytes);
         SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYuAw==")));
         SecretKey key = keyFactory.generateSecret(keySpec);
         Cipher cipher = Cipher.getInstance(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYuA3oxLBRlIB4CISsqAXUmTTdrESwaLC4mVg==")));
         cipher.init(1, key, new IvParameterSpec(keySpec.getKey()));
         byte[] result = cipher.doFinal(content);
         return result;
      } catch (Exception var7) {
         Exception e = var7;
         System.out.println(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfOWgaIAZjDh42PT5SVg==")) + e.toString());
         return null;
      }
   }

   public static String desDecrypt(String content) {
      VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITw9DWAILBY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg2D2ogMCtgNw08P18hKXonTCh+N1RF")) + content);
      byte[] bytes = hexStringToByteArray(content);
      byte[] desDecrypt = desDecrypt(bytes, SECRET_KEY.getBytes());
      if (desDecrypt == null) {
         return null;
      } else {
         String time = new String(desDecrypt);
         return time;
      }
   }

   public static String desDecrypt(byte[] content) {
      byte[] desDecrypt = desDecrypt(content, SECRET_KEY.getBytes());
      return new String(desDecrypt);
   }

   private static byte[] desDecrypt(byte[] content, byte[] keyBytes) {
      try {
         DESKeySpec keySpec = new DESKeySpec(keyBytes);
         SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYuAw==")));
         SecretKey key = keyFactory.generateSecret(keySpec);
         Cipher cipher = Cipher.getInstance(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYuA3oxLBRlIB4CISsqAXUmTTdrESwaLC4mVg==")));
         cipher.init(2, key, new IvParameterSpec(keyBytes));
         byte[] result = cipher.doFinal(content);
         return result;
      } catch (Exception var7) {
         Exception e = var7;
         VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITw9DWAILBY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgqM28xMCt9JywZIxg1OmkgFilrDjw/IxgAKktTOz0=")) + e.toString());
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
