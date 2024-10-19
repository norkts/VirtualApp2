package com.android.dx.dex.code.form;

import com.android.dx.dex.code.CstInsn;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.AnnotatedOutput;
import java.util.BitSet;

public final class Form21c extends InsnFormat {
   public static final InsnFormat THE_ONE = new Form21c();

   private Form21c() {
   }

   public String insnArgString(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      return regs.get(0).regString() + ", " + insn.cstString();
   }

   public String insnCommentString(DalvInsn insn, boolean noteIndices) {
      return noteIndices ? insn.cstComment() : "";
   }

   public int codeSize() {
      return 2;
   }

   public boolean isCompatible(DalvInsn insn) {
      if (!(insn instanceof CstInsn)) {
         return false;
      } else {
         RegisterSpecList regs = insn.getRegisters();
         RegisterSpec reg;
         switch (regs.size()) {
            case 1:
               reg = regs.get(0);
               break;
            case 2:
               reg = regs.get(0);
               if (reg.getReg() != regs.get(1).getReg()) {
                  return false;
               }
               break;
            default:
               return false;
         }

         if (!unsignedFitsInByte(reg.getReg())) {
            return false;
         } else {
            CstInsn ci = (CstInsn)insn;
            int cpi = ci.getIndex();
            Constant cst = ci.getConstant();
            if (!unsignedFitsInShort(cpi)) {
               return false;
            } else {
               return cst instanceof CstType || cst instanceof CstFieldRef || cst instanceof CstString || cst instanceof CstMethodHandle || cst instanceof CstProtoRef;
            }
         }
      }
   }

   public BitSet compatibleRegs(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      int sz = regs.size();
      BitSet bits = new BitSet(sz);
      boolean compat = unsignedFitsInByte(regs.get(0).getReg());
      if (sz == 1) {
         bits.set(0, compat);
      } else if (regs.get(0).getReg() == regs.get(1).getReg()) {
         bits.set(0, compat);
         bits.set(1, compat);
      }

      return bits;
   }

   public void writeTo(AnnotatedOutput out, DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      int cpi = ((CstInsn)insn).getIndex();
      write(out, opcodeUnit(insn, regs.get(0).getReg()), (short)cpi);
   }
}
