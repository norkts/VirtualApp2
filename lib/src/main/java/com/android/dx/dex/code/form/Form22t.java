package com.android.dx.dex.code.form;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.dex.code.TargetInsn;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.util.AnnotatedOutput;
import java.util.BitSet;

public final class Form22t extends InsnFormat {
   public static final InsnFormat THE_ONE = new Form22t();

   private Form22t() {
   }

   public String insnArgString(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      return regs.get(0).regString() + ", " + regs.get(1).regString() + ", " + branchString(insn);
   }

   public String insnCommentString(DalvInsn insn, boolean noteIndices) {
      return branchComment(insn);
   }

   public int codeSize() {
      return 2;
   }

   public boolean isCompatible(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      if (insn instanceof TargetInsn && regs.size() == 2 && unsignedFitsInNibble(regs.get(0).getReg()) && unsignedFitsInNibble(regs.get(1).getReg())) {
         TargetInsn ti = (TargetInsn)insn;
         return ti.hasTargetOffset() ? this.branchFits(ti) : true;
      } else {
         return false;
      }
   }

   public BitSet compatibleRegs(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      BitSet bits = new BitSet(2);
      bits.set(0, unsignedFitsInNibble(regs.get(0).getReg()));
      bits.set(1, unsignedFitsInNibble(regs.get(1).getReg()));
      return bits;
   }

   public boolean branchFits(TargetInsn insn) {
      int offset = insn.getTargetOffset();
      return offset != 0 && signedFitsInShort(offset);
   }

   public void writeTo(AnnotatedOutput out, DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      int offset = ((TargetInsn)insn).getTargetOffset();
      write(out, opcodeUnit(insn, makeByte(regs.get(0).getReg(), regs.get(1).getReg())), (short)offset);
   }
}
