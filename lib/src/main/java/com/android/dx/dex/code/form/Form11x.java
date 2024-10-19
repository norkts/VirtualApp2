package com.android.dx.dex.code.form;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.dex.code.SimpleInsn;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.util.AnnotatedOutput;
import java.util.BitSet;

public final class Form11x extends InsnFormat {
   public static final InsnFormat THE_ONE = new Form11x();

   private Form11x() {
   }

   public String insnArgString(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      return regs.get(0).regString();
   }

   public String insnCommentString(DalvInsn insn, boolean noteIndices) {
      return "";
   }

   public int codeSize() {
      return 1;
   }

   public boolean isCompatible(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      return insn instanceof SimpleInsn && regs.size() == 1 && unsignedFitsInByte(regs.get(0).getReg());
   }

   public BitSet compatibleRegs(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      BitSet bits = new BitSet(1);
      bits.set(0, unsignedFitsInByte(regs.get(0).getReg()));
      return bits;
   }

   public void writeTo(AnnotatedOutput out, DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      write(out, opcodeUnit(insn, regs.get(0).getReg()));
   }
}
