package com.android.dx.dex.code.form;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.dex.code.TargetInsn;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.util.AnnotatedOutput;
import java.util.BitSet;

public final class Form31t extends InsnFormat {
   public static final InsnFormat THE_ONE = new Form31t();

   private Form31t() {
   }

   public String insnArgString(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      return regs.get(0).regString() + ", " + branchString(insn);
   }

   public String insnCommentString(DalvInsn insn, boolean noteIndices) {
      return branchComment(insn);
   }

   public int codeSize() {
      return 3;
   }

   public boolean isCompatible(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      return insn instanceof TargetInsn && regs.size() == 1 && unsignedFitsInByte(regs.get(0).getReg());
   }

   public BitSet compatibleRegs(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      BitSet bits = new BitSet(1);
      bits.set(0, unsignedFitsInByte(regs.get(0).getReg()));
      return bits;
   }

   public boolean branchFits(TargetInsn insn) {
      return true;
   }

   public void writeTo(AnnotatedOutput out, DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      int offset = ((TargetInsn)insn).getTargetOffset();
      write(out, opcodeUnit(insn, regs.get(0).getReg()), offset);
   }
}
