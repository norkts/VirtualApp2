package com.android.dx.dex.code.form;

import com.android.dx.dex.code.CstInsn;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstLiteralBits;
import com.android.dx.util.AnnotatedOutput;
import java.util.BitSet;

public final class Form21s extends InsnFormat {
   public static final InsnFormat THE_ONE = new Form21s();

   private Form21s() {
   }

   public String insnArgString(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      CstLiteralBits value = (CstLiteralBits)((CstInsn)insn).getConstant();
      return regs.get(0).regString() + ", " + literalBitsString(value);
   }

   public String insnCommentString(DalvInsn insn, boolean noteIndices) {
      CstLiteralBits value = (CstLiteralBits)((CstInsn)insn).getConstant();
      return literalBitsComment(value, 16);
   }

   public int codeSize() {
      return 2;
   }

   public boolean isCompatible(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      if (insn instanceof CstInsn && regs.size() == 1 && unsignedFitsInByte(regs.get(0).getReg())) {
         CstInsn ci = (CstInsn)insn;
         Constant cst = ci.getConstant();
         if (!(cst instanceof CstLiteralBits)) {
            return false;
         } else {
            CstLiteralBits cb = (CstLiteralBits)cst;
            return cb.fitsInInt() && signedFitsInShort(cb.getIntBits());
         }
      } else {
         return false;
      }
   }

   public BitSet compatibleRegs(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      BitSet bits = new BitSet(1);
      bits.set(0, unsignedFitsInByte(regs.get(0).getReg()));
      return bits;
   }

   public void writeTo(AnnotatedOutput out, DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      int value = ((CstLiteralBits)((CstInsn)insn).getConstant()).getIntBits();
      write(out, opcodeUnit(insn, regs.get(0).getReg()), (short)value);
   }
}
