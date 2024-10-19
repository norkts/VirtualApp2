package com.android.dx.dex.code.form;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.dex.code.MultiCstInsn;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.util.AnnotatedOutput;

public final class Form4rcc extends InsnFormat {
   public static final InsnFormat THE_ONE = new Form4rcc();

   private Form4rcc() {
   }

   public String insnArgString(DalvInsn insn) {
      return regRangeString(insn.getRegisters()) + ", " + insn.cstString();
   }

   public String insnCommentString(DalvInsn insn, boolean noteIndices) {
      return noteIndices ? insn.cstComment() : "";
   }

   public int codeSize() {
      return 4;
   }

   public boolean isCompatible(DalvInsn insn) {
      if (!(insn instanceof MultiCstInsn)) {
         return false;
      } else {
         MultiCstInsn mci = (MultiCstInsn)insn;
         int methodIdx = mci.getIndex(0);
         int protoIdx = mci.getIndex(1);
         if (unsignedFitsInShort(methodIdx) && unsignedFitsInShort(protoIdx)) {
            Constant methodRef = mci.getConstant(0);
            if (!(methodRef instanceof CstMethodRef)) {
               return false;
            } else {
               Constant protoRef = mci.getConstant(1);
               if (!(protoRef instanceof CstProtoRef)) {
                  return false;
               } else {
                  RegisterSpecList regs = mci.getRegisters();
                  int sz = regs.size();
                  if (sz == 0) {
                     return true;
                  } else {
                     return unsignedFitsInByte(regs.getWordCount()) && unsignedFitsInShort(sz) && unsignedFitsInShort(regs.get(0).getReg()) && isRegListSequential(regs);
                  }
               }
            }
         } else {
            return false;
         }
      }
   }

   public void writeTo(AnnotatedOutput out, DalvInsn insn) {
      MultiCstInsn mci = (MultiCstInsn)insn;
      short regB = (short)mci.getIndex(0);
      short regH = (short)mci.getIndex(1);
      RegisterSpecList regs = insn.getRegisters();
      short regC = 0;
      if (regs.size() > 0) {
         regC = (short)regs.get(0).getReg();
      }

      int regA = regs.getWordCount();
      write(out, opcodeUnit(insn, regA), regB, regC, regH);
   }
}
