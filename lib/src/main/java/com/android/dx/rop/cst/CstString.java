package com.android.dx.rop.cst;

import com.android.dx.rop.type.Type;
import com.android.dx.util.ByteArray;
import com.android.dx.util.Hex;

public final class CstString extends TypedConstant {
   public static final CstString EMPTY_STRING = new CstString("");
   private final String string;
   private final ByteArray bytes;

   public static byte[] stringToUtf8Bytes(String string) {
      int len = string.length();
      byte[] bytes = new byte[len * 3];
      int outAt = 0;

      for(int i = 0; i < len; ++i) {
         char c = string.charAt(i);
         if (c != 0 && c < 128) {
            bytes[outAt] = (byte)c;
            ++outAt;
         } else if (c < 2048) {
            bytes[outAt] = (byte)(c >> 6 & 31 | 192);
            bytes[outAt + 1] = (byte)(c & 63 | 128);
            outAt += 2;
         } else {
            bytes[outAt] = (byte)(c >> 12 & 15 | 224);
            bytes[outAt + 1] = (byte)(c >> 6 & 63 | 128);
            bytes[outAt + 2] = (byte)(c & 63 | 128);
            outAt += 3;
         }
      }

      byte[] result = new byte[outAt];
      System.arraycopy(bytes, 0, result, 0, outAt);
      return result;
   }

   public static String utf8BytesToString(ByteArray bytes) {
      int length = bytes.size();
      char[] chars = new char[length];
      int outAt = 0;

      for(int at = 0; length > 0; ++outAt) {
         int v0 = bytes.getUnsignedByte(at);
         char out;
         int v1;
         int v2;
         switch (v0 >> 4) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
               --length;
               if (v0 == 0) {
                  return throwBadUtf8(v0, at);
               }

               out = (char)v0;
               ++at;
               break;
            case 8:
            case 9:
            case 10:
            case 11:
            default:
               return throwBadUtf8(v0, at);
            case 12:
            case 13:
               length -= 2;
               if (length < 0) {
                  return throwBadUtf8(v0, at);
               }

               v1 = bytes.getUnsignedByte(at + 1);
               if ((v1 & 192) != 128) {
                  return throwBadUtf8(v1, at + 1);
               }

               v2 = (v0 & 31) << 6 | v1 & 63;
               if (v2 != 0 && v2 < 128) {
                  return throwBadUtf8(v1, at + 1);
               }

               out = (char)v2;
               at += 2;
               break;
            case 14:
               length -= 3;
               if (length < 0) {
                  return throwBadUtf8(v0, at);
               }

               v1 = bytes.getUnsignedByte(at + 1);
               if ((v1 & 192) != 128) {
                  return throwBadUtf8(v1, at + 1);
               }

               v2 = bytes.getUnsignedByte(at + 2);
               if ((v1 & 192) != 128) {
                  return throwBadUtf8(v2, at + 2);
               }

               int value = (v0 & 15) << 12 | (v1 & 63) << 6 | v2 & 63;
               if (value < 2048) {
                  return throwBadUtf8(v2, at + 2);
               }

               out = (char)value;
               at += 3;
         }

         chars[outAt] = out;
      }

      return new String(chars, 0, outAt);
   }

   private static String throwBadUtf8(int value, int offset) {
      throw new IllegalArgumentException("bad utf-8 byte " + Hex.u1(value) + " at offset " + Hex.u4(offset));
   }

   public CstString(String string) {
      if (string == null) {
         throw new NullPointerException("string == null");
      } else {
         this.string = string.intern();
         this.bytes = new ByteArray(stringToUtf8Bytes(string));
      }
   }

   public CstString(ByteArray bytes) {
      if (bytes == null) {
         throw new NullPointerException("bytes == null");
      } else {
         this.bytes = bytes;
         this.string = utf8BytesToString(bytes).intern();
      }
   }

   public boolean equals(Object other) {
      return !(other instanceof CstString) ? false : this.string.equals(((CstString)other).string);
   }

   public int hashCode() {
      return this.string.hashCode();
   }

   protected int compareTo0(Constant other) {
      return this.string.compareTo(((CstString)other).string);
   }

   public String toString() {
      return "string{\"" + this.toHuman() + "\"}";
   }

   public String typeName() {
      return "utf8";
   }

   public boolean isCategory2() {
      return false;
   }

   public String toHuman() {
      int len = this.string.length();
      StringBuilder sb = new StringBuilder(len * 3 / 2);

      for(int i = 0; i < len; ++i) {
         char c = this.string.charAt(i);
         if (c >= ' ' && c < 127) {
            if (c == '\'' || c == '"' || c == '\\') {
               sb.append('\\');
            }

            sb.append(c);
         } else if (c > 127) {
            sb.append("\\u");
            sb.append(Character.forDigit(c >> 12, 16));
            sb.append(Character.forDigit(c >> 8 & 15, 16));
            sb.append(Character.forDigit(c >> 4 & 15, 16));
            sb.append(Character.forDigit(c & 15, 16));
         } else {
            switch (c) {
               case '\t':
                  sb.append("\\t");
                  break;
               case '\n':
                  sb.append("\\n");
                  break;
               case '\u000b':
               case '\f':
               default:
                  char nextChar = i < len - 1 ? this.string.charAt(i + 1) : 0;
                  boolean displayZero = nextChar >= '0' && nextChar <= '7';
                  sb.append('\\');

                  for(int shift = 6; shift >= 0; shift -= 3) {
                     char outChar = (char)((c >> shift & 7) + 48);
                     if (outChar != '0' || displayZero) {
                        sb.append(outChar);
                        displayZero = true;
                     }
                  }

                  if (!displayZero) {
                     sb.append('0');
                  }
                  break;
               case '\r':
                  sb.append("\\r");
            }
         }
      }

      return sb.toString();
   }

   public String toQuoted() {
      return '"' + this.toHuman() + '"';
   }

   public String toQuoted(int maxLength) {
      String string = this.toHuman();
      int length = string.length();
      String ellipses;
      if (length <= maxLength - 2) {
         ellipses = "";
      } else {
         string = string.substring(0, maxLength - 5);
         ellipses = "...";
      }

      return '"' + string + ellipses + '"';
   }

   public String getString() {
      return this.string;
   }

   public ByteArray getBytes() {
      return this.bytes;
   }

   public int getUtf8Size() {
      return this.bytes.size();
   }

   public int getUtf16Size() {
      return this.string.length();
   }

   public Type getType() {
      return Type.STRING;
   }
}
