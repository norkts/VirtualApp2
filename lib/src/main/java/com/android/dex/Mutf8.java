package com.android.dex;

import com.android.dex.util.ByteInput;
import java.io.UTFDataFormatException;

public final class Mutf8 {
   private Mutf8() {
   }

   public static String decode(ByteInput in, char[] out) throws UTFDataFormatException {
      int s = 0;

      while(true) {
         while(true) {
            while(true) {
               char a = (char)(in.readByte() & 255);
               if (a == 0) {
                  return new String(out, 0, s);
               }

               out[s] = a;
               if (a >= 128) {
                  int b;
                  if ((a & 224) != 192) {
                     if ((a & 240) != 224) {
                        throw new UTFDataFormatException("bad byte");
                     }

                     b = in.readByte() & 255;
                     int c = in.readByte() & 255;
                     if ((b & 192) != 128 || (c & 192) != 128) {
                        throw new UTFDataFormatException("bad second or third byte");
                     }

                     out[s++] = (char)((a & 15) << 12 | (b & 63) << 6 | c & 63);
                  } else {
                     b = in.readByte() & 255;
                     if ((b & 192) != 128) {
                        throw new UTFDataFormatException("bad second byte");
                     }

                     out[s++] = (char)((a & 31) << 6 | b & 63);
                  }
               } else {
                  ++s;
               }
            }
         }
      }
   }

   private static long countBytes(String s, boolean shortLength) throws UTFDataFormatException {
      long result = 0L;
      int length = s.length();

      for(int i = 0; i < length; ++i) {
         char ch = s.charAt(i);
         if (ch != 0 && ch <= 127) {
            ++result;
         } else if (ch <= 2047) {
            result += 2L;
         } else {
            result += 3L;
         }

         if (shortLength && result > 65535L) {
            throw new UTFDataFormatException("String more than 65535 UTF bytes long");
         }
      }

      return result;
   }

   public static void encode(byte[] dst, int offset, String s) {
      int length = s.length();

      for(int i = 0; i < length; ++i) {
         char ch = s.charAt(i);
         if (ch != 0 && ch <= 127) {
            dst[offset++] = (byte)ch;
         } else if (ch <= 2047) {
            dst[offset++] = (byte)(192 | 31 & ch >> 6);
            dst[offset++] = (byte)(128 | 63 & ch);
         } else {
            dst[offset++] = (byte)(224 | 15 & ch >> 12);
            dst[offset++] = (byte)(128 | 63 & ch >> 6);
            dst[offset++] = (byte)(128 | 63 & ch);
         }
      }

   }

   public static byte[] encode(String s) throws UTFDataFormatException {
      int utfCount = (int)countBytes(s, true);
      byte[] result = new byte[utfCount];
      encode(result, 0, s);
      return result;
   }
}
