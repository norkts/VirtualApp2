package com.android.dx.dex.code.form;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.dex.code.TargetInsn;
import com.android.dx.util.AnnotatedOutput;

public final class Form10t extends InsnFormat {
   public static final InsnFormat THE_ONE = new Form10t();

   private Form10t() {
   }

   public String insnArgString(DalvInsn insn) {
      return branchString(insn);
   }

   public String insnCommentString(DalvInsn insn, boolean noteIndices) {
      return branchComment(insn);
   }

   public int codeSize() {
      return 1;
   }

   public boolean isCompatible(DalvInsn insn) {
      if (insn instanceof TargetInsn && insn.getRegisters().size() == 0) {
         TargetInsn ti = (TargetInsn)insn;
         return ti.hasTargetOffset() ? this.branchFits(ti) : true;
      } else {
         return false;
      }
   }

   public boolean branchFits(TargetInsn insn) {
      int offset = insn.getTargetOffset();
      return offset != 0 && signedFitsInByte(offset);
   }

   public void writeTo(AnnotatedOutput out, DalvInsn insn) {
      int offset = ((TargetInsn)insn).getTargetOffset();
      write(out, opcodeUnit(insn, offset & 255));
   }
}
