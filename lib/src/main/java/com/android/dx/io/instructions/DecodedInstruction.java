package com.android.dx.io.instructions;

import com.android.dex.DexException;
import com.android.dx.io.IndexType;
import com.android.dx.io.OpcodeInfo;
import com.android.dx.io.Opcodes;
import com.android.dx.util.Hex;
import java.io.EOFException;

public abstract class DecodedInstruction {
   private final InstructionCodec format;
   private final int opcode;
   private final int index;
   private final IndexType indexType;
   private final int target;
   private final long literal;

   public static DecodedInstruction decode(CodeInput in) throws EOFException {
      int opcodeUnit = in.read();
      int opcode = Opcodes.extractOpcodeFromUnit(opcodeUnit);
      InstructionCodec format = OpcodeInfo.getFormat(opcode);
      return format.decode(opcodeUnit, in);
   }

   public static DecodedInstruction[] decodeAll(short[] encodedInstructions) {
      int size = encodedInstructions.length;
      DecodedInstruction[] decoded = new DecodedInstruction[size];
      ShortArrayCodeInput in = new ShortArrayCodeInput(encodedInstructions);

      try {
         while(in.hasMore()) {
            decoded[in.cursor()] = decode(in);
         }

         return decoded;
      } catch (EOFException var5) {
         EOFException ex = var5;
         throw new DexException(ex);
      }
   }

   public DecodedInstruction(InstructionCodec format, int opcode, int index, IndexType indexType, int target, long literal) {
      if (format == null) {
         throw new NullPointerException("format == null");
      } else if (!Opcodes.isValidShape(opcode)) {
         throw new IllegalArgumentException("invalid opcode");
      } else {
         this.format = format;
         this.opcode = opcode;
         this.index = index;
         this.indexType = indexType;
         this.target = target;
         this.literal = literal;
      }
   }

   public final InstructionCodec getFormat() {
      return this.format;
   }

   public final int getOpcode() {
      return this.opcode;
   }

   public final short getOpcodeUnit() {
      return (short)this.opcode;
   }

   public final int getIndex() {
      return this.index;
   }

   public final short getIndexUnit() {
      return (short)this.index;
   }

   public final IndexType getIndexType() {
      return this.indexType;
   }

   public final int getTarget() {
      return this.target;
   }

   public final int getTarget(int baseAddress) {
      return this.target - baseAddress;
   }

   public final short getTargetUnit(int baseAddress) {
      int relativeTarget = this.getTarget(baseAddress);
      if (relativeTarget != (short)relativeTarget) {
         throw new DexException("Target out of range: " + Hex.s4(relativeTarget));
      } else {
         return (short)relativeTarget;
      }
   }

   public final int getTargetByte(int baseAddress) {
      int relativeTarget = this.getTarget(baseAddress);
      if (relativeTarget != (byte)relativeTarget) {
         throw new DexException("Target out of range: " + Hex.s4(relativeTarget));
      } else {
         return relativeTarget & 255;
      }
   }

   public final long getLiteral() {
      return this.literal;
   }

   public final int getLiteralInt() {
      if (this.literal != (long)((int)this.literal)) {
         throw new DexException("Literal out of range: " + Hex.u8(this.literal));
      } else {
         return (int)this.literal;
      }
   }

   public final short getLiteralUnit() {
      if (this.literal != (long)((short)((int)this.literal))) {
         throw new DexException("Literal out of range: " + Hex.u8(this.literal));
      } else {
         return (short)((int)this.literal);
      }
   }

   public final int getLiteralByte() {
      if (this.literal != (long)((byte)((int)this.literal))) {
         throw new DexException("Literal out of range: " + Hex.u8(this.literal));
      } else {
         return (int)this.literal & 255;
      }
   }

   public final int getLiteralNibble() {
      if (this.literal >= -8L && this.literal <= 7L) {
         return (int)this.literal & 15;
      } else {
         throw new DexException("Literal out of range: " + Hex.u8(this.literal));
      }
   }

   public abstract int getRegisterCount();

   public int getA() {
      return 0;
   }

   public int getB() {
      return 0;
   }

   public int getC() {
      return 0;
   }

   public int getD() {
      return 0;
   }

   public int getE() {
      return 0;
   }

   public final short getRegisterCountUnit() {
      int registerCount = this.getRegisterCount();
      if ((registerCount & -65536) != 0) {
         throw new DexException("Register count out of range: " + Hex.u8((long)registerCount));
      } else {
         return (short)registerCount;
      }
   }

   public final short getAUnit() {
      int a = this.getA();
      if ((a & -65536) != 0) {
         throw new DexException("Register A out of range: " + Hex.u8((long)a));
      } else {
         return (short)a;
      }
   }

   public final short getAByte() {
      int a = this.getA();
      if ((a & -256) != 0) {
         throw new DexException("Register A out of range: " + Hex.u8((long)a));
      } else {
         return (short)a;
      }
   }

   public final short getANibble() {
      int a = this.getA();
      if ((a & -16) != 0) {
         throw new DexException("Register A out of range: " + Hex.u8((long)a));
      } else {
         return (short)a;
      }
   }

   public final short getBUnit() {
      int b = this.getB();
      if ((b & -65536) != 0) {
         throw new DexException("Register B out of range: " + Hex.u8((long)b));
      } else {
         return (short)b;
      }
   }

   public final short getBByte() {
      int b = this.getB();
      if ((b & -256) != 0) {
         throw new DexException("Register B out of range: " + Hex.u8((long)b));
      } else {
         return (short)b;
      }
   }

   public final short getBNibble() {
      int b = this.getB();
      if ((b & -16) != 0) {
         throw new DexException("Register B out of range: " + Hex.u8((long)b));
      } else {
         return (short)b;
      }
   }

   public final short getCUnit() {
      int c = this.getC();
      if ((c & -65536) != 0) {
         throw new DexException("Register C out of range: " + Hex.u8((long)c));
      } else {
         return (short)c;
      }
   }

   public final short getCByte() {
      int c = this.getC();
      if ((c & -256) != 0) {
         throw new DexException("Register C out of range: " + Hex.u8((long)c));
      } else {
         return (short)c;
      }
   }

   public final short getCNibble() {
      int c = this.getC();
      if ((c & -16) != 0) {
         throw new DexException("Register C out of range: " + Hex.u8((long)c));
      } else {
         return (short)c;
      }
   }

   public final short getDUnit() {
      int d = this.getD();
      if ((d & -65536) != 0) {
         throw new DexException("Register D out of range: " + Hex.u8((long)d));
      } else {
         return (short)d;
      }
   }

   public final short getDByte() {
      int d = this.getD();
      if ((d & -256) != 0) {
         throw new DexException("Register D out of range: " + Hex.u8((long)d));
      } else {
         return (short)d;
      }
   }

   public final short getDNibble() {
      int d = this.getD();
      if ((d & -16) != 0) {
         throw new DexException("Register D out of range: " + Hex.u8((long)d));
      } else {
         return (short)d;
      }
   }

   public final short getENibble() {
      int e = this.getE();
      if ((e & -16) != 0) {
         throw new DexException("Register E out of range: " + Hex.u8((long)e));
      } else {
         return (short)e;
      }
   }

   public final void encode(CodeOutput out) {
      this.format.encode(this, out);
   }

   public abstract DecodedInstruction withIndex(int var1);

   public DecodedInstruction withProtoIndex(int newIndex, int newProtoIndex) {
      throw new IllegalStateException(this.getClass().toString());
   }

   public short getProtoIndex() {
      throw new IllegalStateException(this.getClass().toString());
   }
}
