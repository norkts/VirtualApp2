package com.android.dex.util;

public final class ByteArrayByteInput implements ByteInput {
   private final byte[] bytes;
   private int position;

   public ByteArrayByteInput(byte... bytes) {
      this.bytes = bytes;
   }

   public byte readByte() {
      return this.bytes[this.position++];
   }
}
