package com.android.dx.dex.code.form;

import com.android.dx.dex.code.CstInsn;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.AnnotatedOutput;
import java.util.BitSet;

public final class Form22c extends InsnFormat {
   public static final InsnFormat THE_ONE = new Form22c();

   private Form22c() {
   }

   public String insnArgString(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      return regs.get(0).regString() + ", " + regs.get(1).regString() + ", " + insn.cstString();
   }

   public String insnCommentString(DalvInsn insn, boolean noteIndices) {
      return noteIndices ? insn.cstComment() : "";
   }

   public int codeSize() {
      return 2;
   }

   public boolean isCompatible(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      if (insn instanceof CstInsn && regs.size() == 2 && unsignedFitsInNibble(regs.get(0).getReg()) && unsignedFitsInNibble(regs.get(1).getReg())) {
         CstInsn ci = (CstInsn)insn;
         int cpi = ci.getIndex();
         if (!unsignedFitsInShort(cpi)) {
            return false;
         } else {
            Constant cst = ci.getConstant();
            return cst instanceof CstType || cst instanceof CstFieldRef;
         }
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

   public void writeTo(AnnotatedOutput out, DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      int cpi = ((CstInsn)insn).getIndex();
      write(out, opcodeUnit(insn, makeByte(regs.get(0).getReg(), regs.get(1).getReg())), (short)cpi);
   }
}
