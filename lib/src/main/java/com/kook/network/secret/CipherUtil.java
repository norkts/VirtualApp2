package com.kook.network.secret;

import com.kook.network.StringFog;
import java.nio.charset.Charset;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CipherUtil {
   public static final String SECRET_KEY = StringFog.decrypt("IwcFWR1QQUkdXkdIXUJfXA==");
   private static final String ALGORITHM = StringFog.decrypt("Kio8RGghN0h9LSorXj8OD0kLGwA=");

   public static String decrypt(String secretKey, String cipherText) {
      byte[] encrypted = Base64.getDecoder().decode(cipherText);

      try {
         Cipher cipher = Cipher.getInstance(ALGORITHM);
         SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), StringFog.decrypt("Kio8"));
         cipher.init(2, key);
         return new String(cipher.doFinal(encrypted));
      } catch (Exception var5) {
         Exception e = var5;
         throw new RuntimeException(e);
      }
   }

   public static String encrypt(String secretKey, String plainText) {
      try {
         Cipher cipher = Cipher.getInstance(ALGORITHM);
         SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), StringFog.decrypt("Kio8"));
         cipher.init(1, key);
         return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes(Charset.forName(StringFog.decrypt("PjspRhU=")))));
      } catch (Exception var4) {
         Exception e = var4;
         throw new RuntimeException(e);
      }
   }
}
