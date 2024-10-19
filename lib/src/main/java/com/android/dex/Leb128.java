package com.android.dex;

import com.android.dex.util.ByteInput;
import com.android.dex.util.ByteOutput;

public final class Leb128 {
   private Leb128() {
   }

   public static int unsignedLeb128Size(int value) {
      int remaining = value >> 7;

      int count;
      for(count = 0; remaining != 0; ++count) {
         remaining >>= 7;
      }

      return count + 1;
   }

   public static int readSignedLeb128(ByteInput in) {
      int result = 0;
      int count = 0;
      int signBits = -1;

      int cur;
      do {
         cur = in.readByte() & 255;
         result |= (cur & 127) << count * 7;
         signBits <<= 7;
         ++count;
      } while((cur & 128) == 128 && count < 5);

      if ((cur & 128) == 128) {
         throw new DexException("invalid LEB128 sequence");
      } else {
         if ((signBits >> 1 & result) != 0) {
            result |= signBits;
         }

         return result;
      }
   }

   public static int readUnsignedLeb128(ByteInput in) {
      int result = 0;
      int count = 0;

      int cur;
      do {
         cur = in.readByte() & 255;
         result |= (cur & 127) << count * 7;
         ++count;
      } while((cur & 128) == 128 && count < 5);

      if ((cur & 128) == 128) {
         throw new DexException("invalid LEB128 sequence");
      } else {
         return result;
      }
   }

   public static void writeUnsignedLeb128(ByteOutput out, int value) {
      for(int remaining = value >>> 7; remaining != 0; remaining >>>= 7) {
         out.writeByte((byte)(value & 127 | 128));
         value = remaining;
      }

      out.writeByte((byte)(value & 127));
   }

   public static void writeSignedLeb128(ByteOutput out, int value) {
      int remaining = value >> 7;
      boolean hasMore = true;

      for(int end = (value & Integer.MIN_VALUE) == 0 ? 0 : -1; hasMore; remaining >>= 7) {
         hasMore = remaining != end || (remaining & 1) != (value >> 6 & 1);
         out.writeByte((byte)(value & 127 | (hasMore ? 128 : 0)));
         value = remaining;
      }

   }
}
