package com.android.dex;

import com.android.dex.util.ByteInput;

public final class EncodedValueReader {
   public static final int ENCODED_BYTE = 0;
   public static final int ENCODED_SHORT = 2;
   public static final int ENCODED_CHAR = 3;
   public static final int ENCODED_INT = 4;
   public static final int ENCODED_LONG = 6;
   public static final int ENCODED_FLOAT = 16;
   public static final int ENCODED_DOUBLE = 17;
   public static final int ENCODED_METHOD_TYPE = 21;
   public static final int ENCODED_METHOD_HANDLE = 22;
   public static final int ENCODED_STRING = 23;
   public static final int ENCODED_TYPE = 24;
   public static final int ENCODED_FIELD = 25;
   public static final int ENCODED_ENUM = 27;
   public static final int ENCODED_METHOD = 26;
   public static final int ENCODED_ARRAY = 28;
   public static final int ENCODED_ANNOTATION = 29;
   public static final int ENCODED_NULL = 30;
   public static final int ENCODED_BOOLEAN = 31;
   private static final int MUST_READ = -1;
   protected final ByteInput in;
   private int type;
   private int annotationType;
   private int arg;

   public EncodedValueReader(ByteInput in) {
      this.type = -1;
      this.in = in;
   }

   public EncodedValueReader(EncodedValue in) {
      this(in.asByteInput());
   }

   public EncodedValueReader(ByteInput in, int knownType) {
      this.type = -1;
      this.in = in;
      this.type = knownType;
   }

   public EncodedValueReader(EncodedValue in, int knownType) {
      this(in.asByteInput(), knownType);
   }

   public int peek() {
      if (this.type == -1) {
         int argAndType = this.in.readByte() & 255;
         this.type = argAndType & 31;
         this.arg = (argAndType & 224) >> 5;
      }

      return this.type;
   }

   public int readArray() {
      this.checkType(28);
      this.type = -1;
      return Leb128.readUnsignedLeb128(this.in);
   }

   public int readAnnotation() {
      this.checkType(29);
      this.type = -1;
      this.annotationType = Leb128.readUnsignedLeb128(this.in);
      return Leb128.readUnsignedLeb128(this.in);
   }

   public int getAnnotationType() {
      return this.annotationType;
   }

   public int readAnnotationName() {
      return Leb128.readUnsignedLeb128(this.in);
   }

   public byte readByte() {
      this.checkType(0);
      this.type = -1;
      return (byte)EncodedValueCodec.readSignedInt(this.in, this.arg);
   }

   public short readShort() {
      this.checkType(2);
      this.type = -1;
      return (short)EncodedValueCodec.readSignedInt(this.in, this.arg);
   }

   public char readChar() {
      this.checkType(3);
      this.type = -1;
      return (char)EncodedValueCodec.readUnsignedInt(this.in, this.arg, false);
   }

   public int readInt() {
      this.checkType(4);
      this.type = -1;
      return EncodedValueCodec.readSignedInt(this.in, this.arg);
   }

   public long readLong() {
      this.checkType(6);
      this.type = -1;
      return EncodedValueCodec.readSignedLong(this.in, this.arg);
   }

   public float readFloat() {
      this.checkType(16);
      this.type = -1;
      return Float.intBitsToFloat(EncodedValueCodec.readUnsignedInt(this.in, this.arg, true));
   }

   public double readDouble() {
      this.checkType(17);
      this.type = -1;
      return Double.longBitsToDouble(EncodedValueCodec.readUnsignedLong(this.in, this.arg, true));
   }

   public int readMethodType() {
      this.checkType(21);
      this.type = -1;
      return EncodedValueCodec.readUnsignedInt(this.in, this.arg, false);
   }

   public int readMethodHandle() {
      this.checkType(22);
      this.type = -1;
      return EncodedValueCodec.readUnsignedInt(this.in, this.arg, false);
   }

   public int readString() {
      this.checkType(23);
      this.type = -1;
      return EncodedValueCodec.readUnsignedInt(this.in, this.arg, false);
   }

   public int readType() {
      this.checkType(24);
      this.type = -1;
      return EncodedValueCodec.readUnsignedInt(this.in, this.arg, false);
   }

   public int readField() {
      this.checkType(25);
      this.type = -1;
      return EncodedValueCodec.readUnsignedInt(this.in, this.arg, false);
   }

   public int readEnum() {
      this.checkType(27);
      this.type = -1;
      return EncodedValueCodec.readUnsignedInt(this.in, this.arg, false);
   }

   public int readMethod() {
      this.checkType(26);
      this.type = -1;
      return EncodedValueCodec.readUnsignedInt(this.in, this.arg, false);
   }

   public void readNull() {
      this.checkType(30);
      this.type = -1;
   }

   public boolean readBoolean() {
      this.checkType(31);
      this.type = -1;
      return this.arg != 0;
   }

   public void skipValue() {
      int i;
      int size;
      switch (this.peek()) {
         case 0:
            this.readByte();
            break;
         case 1:
         case 5:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 18:
         case 19:
         case 20:
         default:
            throw new DexException("Unexpected type: " + Integer.toHexString(this.type));
         case 2:
            this.readShort();
            break;
         case 3:
            this.readChar();
            break;
         case 4:
            this.readInt();
            break;
         case 6:
            this.readLong();
            break;
         case 16:
            this.readFloat();
            break;
         case 17:
            this.readDouble();
            break;
         case 21:
            this.readMethodType();
            break;
         case 22:
            this.readMethodHandle();
            break;
         case 23:
            this.readString();
            break;
         case 24:
            this.readType();
            break;
         case 25:
            this.readField();
            break;
         case 26:
            this.readMethod();
            break;
         case 27:
            this.readEnum();
            break;
         case 28:
            i = 0;

            for(size = this.readArray(); i < size; ++i) {
               this.skipValue();
            }

            return;
         case 29:
            i = 0;

            for(size = this.readAnnotation(); i < size; ++i) {
               this.readAnnotationName();
               this.skipValue();
            }

            return;
         case 30:
            this.readNull();
            break;
         case 31:
            this.readBoolean();
      }

   }

   private void checkType(int expected) {
      if (this.peek() != expected) {
         throw new IllegalStateException(String.format("Expected %x but was %x", expected, this.peek()));
      }
   }
}
