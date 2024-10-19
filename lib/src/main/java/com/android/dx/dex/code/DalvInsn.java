package com.android.dx.dex.code;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.ssa.RegisterMapper;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import com.android.dx.util.TwoColumnOutput;
import java.util.BitSet;

public abstract class DalvInsn {
   private int address;
   private final Dop opcode;
   private final SourcePosition position;
   private final RegisterSpecList registers;

   public static SimpleInsn makeMove(SourcePosition position, RegisterSpec dest, RegisterSpec src) {
      boolean category1 = dest.getCategory() == 1;
      boolean reference = dest.getType().isReference();
      int destReg = dest.getReg();
      int srcReg = src.getReg();
      Dop opcode;
      if ((srcReg | destReg) < 16) {
         opcode = reference ? Dops.MOVE_OBJECT : (category1 ? Dops.MOVE : Dops.MOVE_WIDE);
      } else if (destReg < 256) {
         opcode = reference ? Dops.MOVE_OBJECT_FROM16 : (category1 ? Dops.MOVE_FROM16 : Dops.MOVE_WIDE_FROM16);
      } else {
         opcode = reference ? Dops.MOVE_OBJECT_16 : (category1 ? Dops.MOVE_16 : Dops.MOVE_WIDE_16);
      }

      return new SimpleInsn(opcode, position, RegisterSpecList.make(dest, src));
   }

   public DalvInsn(Dop opcode, SourcePosition position, RegisterSpecList registers) {
      if (opcode == null) {
         throw new NullPointerException("opcode == null");
      } else if (position == null) {
         throw new NullPointerException("position == null");
      } else if (registers == null) {
         throw new NullPointerException("registers == null");
      } else {
         this.address = -1;
         this.opcode = opcode;
         this.position = position;
         this.registers = registers;
      }
   }

   public final String toString() {
      StringBuilder sb = new StringBuilder(100);
      sb.append(this.identifierString());
      sb.append(' ');
      sb.append(this.position);
      sb.append(": ");
      sb.append(this.opcode.getName());
      boolean needComma = false;
      if (this.registers.size() != 0) {
         sb.append(this.registers.toHuman(" ", ", ", (String)null));
         needComma = true;
      }

      String extra = this.argString();
      if (extra != null) {
         if (needComma) {
            sb.append(',');
         }

         sb.append(' ');
         sb.append(extra);
      }

      return sb.toString();
   }

   public final boolean hasAddress() {
      return this.address >= 0;
   }

   public final int getAddress() {
      if (this.address < 0) {
         throw new RuntimeException("address not yet known");
      } else {
         return this.address;
      }
   }

   public final Dop getOpcode() {
      return this.opcode;
   }

   public final SourcePosition getPosition() {
      return this.position;
   }

   public final RegisterSpecList getRegisters() {
      return this.registers;
   }

   public final boolean hasResult() {
      return this.opcode.hasResult();
   }

   public final int getMinimumRegisterRequirement(BitSet compatRegs) {
      boolean hasResult = this.hasResult();
      int regSz = this.registers.size();
      int resultRequirement = 0;
      int sourceRequirement = 0;
      if (hasResult && !compatRegs.get(0)) {
         resultRequirement = this.registers.get(0).getCategory();
      }

      for(int i = hasResult ? 1 : 0; i < regSz; ++i) {
         if (!compatRegs.get(i)) {
            sourceRequirement += this.registers.get(i).getCategory();
         }
      }

      return Math.max(sourceRequirement, resultRequirement);
   }

   public DalvInsn getLowRegVersion() {
      RegisterSpecList regs = this.registers.withExpandedRegisters(0, this.hasResult(), (BitSet)null);
      return this.withRegisters(regs);
   }

   public DalvInsn expandedPrefix(BitSet compatRegs) {
      RegisterSpecList regs = this.registers;
      boolean firstBit = compatRegs.get(0);
      if (this.hasResult()) {
         compatRegs.set(0);
      }

      regs = regs.subset(compatRegs);
      if (this.hasResult()) {
         compatRegs.set(0, firstBit);
      }

      return regs.size() == 0 ? null : new HighRegisterPrefix(this.position, regs);
   }

   public DalvInsn expandedSuffix(BitSet compatRegs) {
      if (this.hasResult() && !compatRegs.get(0)) {
         RegisterSpec r = this.registers.get(0);
         return makeMove(this.position, r, r.withReg(0));
      } else {
         return null;
      }
   }

   public DalvInsn expandedVersion(BitSet compatRegs) {
      RegisterSpecList regs = this.registers.withExpandedRegisters(0, this.hasResult(), compatRegs);
      return this.withRegisters(regs);
   }

   public final String identifierString() {
      return this.address != -1 ? String.format("%04x", this.address) : Hex.u4(System.identityHashCode(this));
   }

   public final String listingString(String prefix, int width, boolean noteIndices) {
      String insnPerSe = this.listingString0(noteIndices);
      if (insnPerSe == null) {
         return null;
      } else {
         String addr = prefix + this.identifierString() + ": ";
         int w1 = addr.length();
         int w2 = width == 0 ? insnPerSe.length() : width - w1;
         return TwoColumnOutput.toString(addr, w1, "", insnPerSe, w2);
      }
   }

   public final void setAddress(int address) {
      if (address < 0) {
         throw new IllegalArgumentException("address < 0");
      } else {
         this.address = address;
      }
   }

   public final int getNextAddress() {
      return this.getAddress() + this.codeSize();
   }

   public DalvInsn withMapper(RegisterMapper mapper) {
      return this.withRegisters(mapper.map(this.getRegisters()));
   }

   public abstract int codeSize();

   public abstract void writeTo(AnnotatedOutput var1);

   public abstract DalvInsn withOpcode(Dop var1);

   public abstract DalvInsn withRegisterOffset(int var1);

   public abstract DalvInsn withRegisters(RegisterSpecList var1);

   protected abstract String argString();

   protected abstract String listingString0(boolean var1);

   public String cstString() {
      throw new UnsupportedOperationException("Not supported.");
   }

   public String cstComment() {
      throw new UnsupportedOperationException("Not supported.");
   }
}
