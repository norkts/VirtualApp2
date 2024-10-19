package com.lody.virtual.helper.utils;

import android.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class EncodeUtils {
   private static final Map<String, String> sStringPool = new HashMap();

   public static String decodeBase64(String base64) {
      synchronized(sStringPool) {
         String decode;
         if (!sStringPool.containsKey(base64)) {
            decode = new String(Base64.decode(base64, 0));
            sStringPool.put(base64, decode);
         } else {
            decode = (String)sStringPool.get(base64);
         }

         return decode;
      }
   }
}
