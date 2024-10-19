package com.kook.network.secret;

import android.text.TextUtils;
import com.kook.network.StringFog;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;

public class MD5Utils {
   public static InputStream ungzipInputStream(InputStream input, boolean gzip) throws IOException {
      InputStream is = null;
      if (input != null) {
         if (gzip) {
            InputStream is = new GZIPInputStream(input);
            BufferedInputStream bis = new BufferedInputStream(is);
            bis.mark(2);
            byte[] header = new byte[2];
            int result = bis.read(header);
            bis.reset();
            int ss = header[0] & 255 | (header[1] & 255) << 8;
            if (result != -1 && ss == 35615) {
               is = new GZIPInputStream(bis);
            } else {
               is = bis;
            }
         } else {
            is = new BufferedInputStream(input);
         }
      }

      return (InputStream)is;
   }

   public static String md5Encode(String inStr) {
      MessageDigest md5 = null;
      new StringBuffer();

      try {
         md5 = MessageDigest.getInstance(StringFog.decrypt("Jita"));
         byte[] byteArray = inStr.getBytes(StringFog.decrypt("PjspRhU="));
         byte[] md5Bytes = md5.digest(byteArray);
         String content = binToHex(md5Bytes);
         return content;
      } catch (Exception var6) {
         Exception e = var6;
         System.out.println(e.toString());
         e.printStackTrace();
         return "";
      }
   }

   public static String binToHex(byte[] md) {
      StringBuffer sb = new StringBuffer("");
      int read = false;

      for(int i = 0; i < md.length; ++i) {
         int read = md[i];
         if (read < 0) {
            read += 256;
         }

         if (read < 16) {
            sb.append(StringFog.decrypt("Ww=="));
         }

         sb.append(Integer.toHexString(read));
      }

      return sb.toString();
   }

   public static String fileMD5Sync(String filePath) {
      return TextUtils.isEmpty(filePath) ? "" : fileMD5Sync(new File(filePath));
   }

   public static String fileMD5Sync(File file) {
      if (file != null && file.exists()) {
         FileInputStream fis = null;

         try {
            MessageDigest messageDigest = MessageDigest.getInstance(StringFog.decrypt("Jita"));
            fis = new FileInputStream(file);
            MappedByteBuffer byteBuffer = fis.getChannel().map(MapMode.READ_ONLY, 0L, file.length());
            messageDigest.update(byteBuffer);
            BigInteger bigInt = new BigInteger(1, messageDigest.digest());

            String md5;
            for(md5 = bigInt.toString(16); md5.length() < 32; md5 = StringFog.decrypt("Ww==") + md5) {
            }

            String var6 = md5;
            return var6;
         } catch (IOException | NoSuchAlgorithmException var16) {
            Exception e = var16;
            ((Exception)e).printStackTrace();
            return "";
         } finally {
            if (fis != null) {
               try {
                  fis.close();
               } catch (IOException var15) {
                  IOException e = var15;
                  e.printStackTrace();
               }
            }

         }
      } else {
         return "";
      }
   }
}
