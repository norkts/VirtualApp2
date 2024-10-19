package com.carlos.common.utils;

import com.carlos.libcommon.StringFog;
import java.io.FileInputStream;

public final class SysUtils {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii0YKWQaMC9gEShF"));

   public static String getCurrentProcessName() {
      FileInputStream in = null;

      try {
         try {
            String fn = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My06KmozLyVhJDAoKDlfP28jBiRqARog"));
            in = new FileInputStream(fn);
            byte[] buffer = new byte[256];

            int len;
            int b;
            for(len = 0; (b = in.read()) > 0 && len < buffer.length; buffer[len++] = (byte)b) {
            }

            if (len > 0) {
               String s = new String(buffer, 0, len, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQUqW3pTRVo=")));
               return s;
            }
         } catch (Throwable var10) {
         }

         return null;
      } finally {
         ;
      }
   }
}
