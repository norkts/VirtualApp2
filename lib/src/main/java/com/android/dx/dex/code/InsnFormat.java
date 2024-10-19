package com.android.dx.dex.code;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstKnownNull;
import com.android.dx.rop.cst.CstLiteral64;
import com.android.dx.rop.cst.CstLiteralBits;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.BitSet;

public abstract class InsnFormat {
   public static final boolean ALLOW_EXTENDED_OPCODES = true;

   public final String listingString(DalvInsn insn, boolean noteIndices) {
      String op = insn.getOpcode().getName();
      String arg = this.insnArgString(insn);
      String comment = this.insnCommentString(insn, noteIndices);
      StringBuilder sb = new StringBuilder(100);
      sb.append(op);
      if (arg.length() != 0) {
         sb.append(' ');
         sb.append(arg);
      }

      if (comment.length() != 0) {
         sb.append(" // ");
         sb.append(comment);
      }

      return sb.toString();
   }

   public abstract String insnArgString(DalvInsn var1);

   public abstract String insnCommentString(DalvInsn var1, boolean var2);

   public abstract int codeSize();

   public abstract boolean isCompatible(DalvInsn var1);

   public BitSet compatibleRegs(DalvInsn insn) {
      return new BitSet();
   }

   public boolean branchFits(TargetInsn insn) {
      return false;
   }

   public abstract void writeTo(AnnotatedOutput var1, DalvInsn var2);

   protected static String regListString(RegisterSpecList list) {
      int sz = list.size();
      StringBuilder sb = new StringBuilder(sz * 5 + 2);
      sb.append('{');

      for(int i = 0; i < sz; ++i) {
         if (i != 0) {
            sb.append(", ");
         }

         sb.append(list.get(i).regString());
      }

      sb.append('}');
      return sb.toString();
   }

   protected static String regRangeString(RegisterSpecList list) {
      int size = list.size();
      StringBuilder sb = new StringBuilder(30);
      sb.append("{");
      switch (size) {
         case 0:
            break;
         case 1:
            sb.append(list.get(0).regString());
            break;
         default:
            RegisterSpec lastReg = list.get(size - 1);
            if (lastReg.getCategory() == 2) {
               lastReg = lastReg.withOffset(1);
            }

            sb.append(list.get(0).regString());
            sb.append("..");
            sb.append(lastReg.regString());
      }

      sb.append("}");
      return sb.toString();
   }

   protected static String literalBitsString(CstLiteralBits value) {
      StringBuilder sb = new StringBuilder(100);
      sb.append('#');
      if (value instanceof CstKnownNull) {
         sb.append("null");
      } else {
         sb.append(value.typeName());
         sb.append(' ');
         sb.append(value.toHuman());
      }

      return sb.toString();
   }

   protected static String literalBitsComment(CstLiteralBits value, int width) {
      StringBuilder sb = new StringBuilder(20);
      sb.append("#");
      long bits;
      if (value instanceof CstLiteral64) {
         bits = ((CstLiteral64)value).getLongBits();
      } else {
         bits = (long)value.getIntBits();
      }

      switch (width) {
         case 4:
            sb.append(Hex.uNibble((int)bits));
            break;
         case 8:
            sb.append(Hex.u1((int)bits));
            break;
         case 16:
            sb.append(Hex.u2((int)bits));
            break;
         case 32:
            sb.append(Hex.u4((int)bits));
            break;
         case 64:
            sb.append(Hex.u8(bits));
            break;
         default:
            throw new RuntimeException("shouldn't happen");
      }

      return sb.toString();
   }

   protected static String branchString(DalvInsn insn) {
      TargetInsn ti = (TargetInsn)insn;
      int address = ti.getTargetAddress();
      return address == (char)address ? Hex.u2(address) : Hex.u4(address);
   }

   protected static String branchComment(DalvInsn insn) {
      TargetInsn ti = (TargetInsn)insn;
      int offset = ti.getTargetOffset();
      return offset == (short)offset ? Hex.s2(offset) : Hex.s4(offset);
   }

   protected static boolean signedFitsInNibble(int value) {
      return value >= -8 && value <= 7;
   }

   protected static boolean unsignedFitsInNibble(int value) {
      return value == (value & 15);
   }

   protected static boolean signedFitsInByte(int value) {
      return (byte)value == value;
   }

   protected static boolean unsignedFitsInByte(int value) {
      return value == (value & 255);
   }

   protected static boolean signedFitsInShort(int value) {
      return (short)value == value;
   }

   protected static boolean unsignedFitsInShort(int value) {
      return value == (value & '\uffff');
   }

   protected static boolean isRegListSequential(RegisterSpecList list) {
      int sz = list.size();
      if (sz < 2) {
         return true;
      } else {
         int first = list.get(0).getReg();
         int next = first;

         for(int i = 0; i < sz; ++i) {
            RegisterSpec one = list.get(i);
            if (one.getReg() != next) {
               return false;
            }

            next += one.getCategory();
         }

         return true;
      }
   }

   protected static int argIndex(DalvInsn insn) {
      int arg = ((CstInteger)((CstInsn)insn).getConstant()).getValue();
      if (arg < 0) {
         throw new IllegalArgumentException("bogus insn");
      } else {
         return arg;
      }
   }

   protected static short opcodeUnit(DalvInsn insn, int arg) {
      if ((arg & 255) != arg) {
         throw new IllegalArgumentException("arg out of range 0..255");
      } else {
         int opcode = insn.getOpcode().getOpcode();
         if ((opcode & 255) != opcode) {
            throw new IllegalArgumentException("opcode out of range 0..255");
         } else {
            return (short)(opcode | arg << 8);
         }
      }
   }

   protected static short opcodeUnit(DalvInsn insn) {
      int opcode = insn.getOpcode().getOpcode();
      if (opcode >= 256 && opcode <= 65535) {
         return (short)opcode;
      } else {
         throw new IllegalArgumentException("opcode out of range 0..65535");
      }
   }

   protected static short codeUnit(int low, int high) {
      if ((low & 255) != low) {
         throw new IllegalArgumentException("low out of range 0..255");
      } else if ((high & 255) != high) {
         throw new IllegalArgumentException("high out of range 0..255");
      } else {
         return (short)(low | high << 8);
      }
   }

   protected static short codeUnit(int n0, int n1, int n2, int n3) {
      if ((n0 & 15) != n0) {
         throw new IllegalArgumentException("n0 out of range 0..15");
      } else if ((n1 & 15) != n1) {
         throw new IllegalArgumentException("n1 out of range 0..15");
      } else if ((n2 & 15) != n2) {
         throw new IllegalArgumentException("n2 out of range 0..15");
      } else if ((n3 & 15) != n3) {
         throw new IllegalArgumentException("n3 out of range 0..15");
      } else {
         return (short)(n0 | n1 << 4 | n2 << 8 | n3 << 12);
      }
   }

   protected static int makeByte(int low, int high) {
      if ((low & 15) != low) {
         throw new IllegalArgumentException("low out of range 0..15");
      } else if ((high & 15) != high) {
         throw new IllegalArgumentException("high out of range 0..15");
      } else {
         return low | high << 4;
      }
   }

   protected static void write(AnnotatedOutput out, short c0) {
      out.writeShort(c0);
   }

   protected static void write(AnnotatedOutput out, short c0, short c1) {
      out.writeShort(c0);
      out.writeShort(c1);
   }

   protected static void write(AnnotatedOutput out, short c0, short c1, short c2) {
      out.writeShort(c0);
      out.writeShort(c1);
      out.writeShort(c2);
   }

   protected static void write(AnnotatedOutput out, short c0, short c1, short c2, short c3) {
      out.writeShort(c0);
      out.writeShort(c1);
      out.writeShort(c2);
      out.writeShort(c3);
   }

   protected static void write(AnnotatedOutput out, short c0, short c1, short c2, short c3, short c4) {
      out.writeShort(c0);
      out.writeShort(c1);
      out.writeShort(c2);
      out.writeShort(c3);
      out.writeShort(c4);
   }

   protected static void write(AnnotatedOutput out, short c0, int c1c2) {
      write(out, c0, (short)c1c2, (short)(c1c2 >> 16));
   }

   protected static void write(AnnotatedOutput out, short c0, int c1c2, short c3) {
      write(out, c0, (short)c1c2, (short)(c1c2 >> 16), c3);
   }

   protected static void write(AnnotatedOutput out, short c0, int c1c2, short c3, short c4) {
      write(out, c0, (short)c1c2, (short)(c1c2 >> 16), c3, c4);
   }

   protected static void write(AnnotatedOutput out, short c0, long c1c2c3c4) {
      write(out, c0, (short)((int)c1c2c3c4), (short)((int)(c1c2c3c4 >> 16)), (short)((int)(c1c2c3c4 >> 32)), (short)((int)(c1c2c3c4 >> 48)));
   }
}
