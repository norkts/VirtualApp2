package com.android.dx.util;

public final class HexParser {
   private HexParser() {
   }

   public static byte[] parse(String src) {
      int len = src.length();
      byte[] result = new byte[len / 2];
      int at = 0;
      int outAt = 0;

      boolean quoteMode;
      do {
         if (at >= len) {
            if (outAt < result.length) {
               byte[] newr = new byte[outAt];
               System.arraycopy(result, 0, newr, 0, outAt);
               result = newr;
            }

            return result;
         }

         int nlAt = src.indexOf(10, at);
         if (nlAt < 0) {
            nlAt = len;
         }

         int poundAt = src.indexOf(35, at);
         String line;
         if (poundAt >= 0 && poundAt < nlAt) {
            line = src.substring(at, poundAt);
         } else {
            line = src.substring(at, nlAt);
         }

         at = nlAt + 1;
         int colonAt = line.indexOf(58);
         int lineLen;
         if (colonAt != -1) {
            lineLen = line.indexOf(34);
            if (lineLen == -1 || lineLen >= colonAt) {
               String atStr = line.substring(0, colonAt).trim();
               line = line.substring(colonAt + 1);
               int alleged = Integer.parseInt(atStr, 16);
               if (alleged != outAt) {
                  throw new RuntimeException("bogus offset marker: " + atStr);
               }
            }
         }

         lineLen = line.length();
         int value = -1;
         quoteMode = false;

         for(int i = 0; i < lineLen; ++i) {
            char c = line.charAt(i);
            if (quoteMode) {
               if (c == '"') {
                  quoteMode = false;
               } else {
                  result[outAt] = (byte)c;
                  ++outAt;
               }
            } else if (c > ' ') {
               if (c == '"') {
                  if (value != -1) {
                     throw new RuntimeException("spare digit around offset " + Hex.u4(outAt));
                  }

                  quoteMode = true;
               } else {
                  int digVal = Character.digit(c, 16);
                  if (digVal == -1) {
                     throw new RuntimeException("bogus digit character: \"" + c + "\"");
                  }

                  if (value == -1) {
                     value = digVal;
                  } else {
                     result[outAt] = (byte)(value << 4 | digVal);
                     ++outAt;
                     value = -1;
                  }
               }
            }
         }

         if (value != -1) {
            throw new RuntimeException("spare digit around offset " + Hex.u4(outAt));
         }
      } while(!quoteMode);

      throw new RuntimeException("unterminated quote around offset " + Hex.u4(outAt));
   }
}
