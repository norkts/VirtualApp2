package com.android.dx.dex.code.form;

import com.android.dx.dex.code.CstInsn;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstCallSiteRef;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.AnnotatedOutput;

public final class Form3rc extends InsnFormat {
   public static final InsnFormat THE_ONE = new Form3rc();

   private Form3rc() {
   }

   public String insnArgString(DalvInsn insn) {
      return regRangeString(insn.getRegisters()) + ", " + insn.cstString();
   }

   public String insnCommentString(DalvInsn insn, boolean noteIndices) {
      return noteIndices ? insn.cstComment() : "";
   }

   public int codeSize() {
      return 3;
   }

   public boolean isCompatible(DalvInsn insn) {
      if (!(insn instanceof CstInsn)) {
         return false;
      } else {
         CstInsn ci = (CstInsn)insn;
         int cpi = ci.getIndex();
         Constant cst = ci.getConstant();
         if (!unsignedFitsInShort(cpi)) {
            return false;
         } else if (!(cst instanceof CstMethodRef) && !(cst instanceof CstType) && !(cst instanceof CstCallSiteRef)) {
            return false;
         } else {
            RegisterSpecList regs = ci.getRegisters();
            int sz = regs.size();
            return regs.size() == 0 || isRegListSequential(regs) && unsignedFitsInShort(regs.get(0).getReg()) && unsignedFitsInByte(regs.getWordCount());
         }
      }
   }

   public void writeTo(AnnotatedOutput out, DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      int cpi = ((CstInsn)insn).getIndex();
      int firstReg = regs.size() == 0 ? 0 : regs.get(0).getReg();
      int count = regs.getWordCount();
      write(out, opcodeUnit(insn, count), (short)cpi, (short)firstReg);
   }
}
