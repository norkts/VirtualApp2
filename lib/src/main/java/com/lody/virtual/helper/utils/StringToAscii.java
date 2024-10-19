package com.lody.virtual.helper.utils;

import com.lody.virtual.StringFog;

public class StringToAscii {
   private static String toHexUtil(int n) {
      String rt = "";
      switch (n) {
         case 10:
            rt = rt + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JghSVg=="));
            break;
         case 11:
            rt = rt + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jj5SVg=="));
            break;
         case 12:
            rt = rt + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji5SVg=="));
            break;
         case 13:
            rt = rt + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRhSVg=="));
            break;
         case 14:
            rt = rt + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQhSVg=="));
            break;
         case 15:
            rt = rt + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JT5SVg=="));
            break;
         default:
            rt = rt + n;
      }

      return rt;
   }

   public static String toHex(int n) {
      StringBuilder sb = new StringBuilder();
      if (n / 16 == 0) {
         return toHexUtil(n);
      } else {
         String t = toHex(n / 16);
         int nn = n % 16;
         sb.append(t).append(toHexUtil(nn));
         return sb.toString();
      }
   }

   public static String parseAscii(String str) {
      StringBuilder sb = new StringBuilder();
      byte[] bs = str.getBytes();

      for(int i = 0; i < bs.length; ++i) {
         sb.append(toHex(bs[i]));
      }

      return sb.toString();
   }

   public static void main(String[] args) {
      String s = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KBcYIg=="));
      System.out.println(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BlYFDkYtHypYEzkMAiAFW0cvOVJBKwctABtcCFgHAypKK14J")) + parseAscii(s));
   }
}
