package com.android.dx.dex.code.form;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.dex.code.MultiCstInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.rop.type.Type;
import com.android.dx.util.AnnotatedOutput;
import java.util.BitSet;

public final class Form45cc extends InsnFormat {
   public static final InsnFormat THE_ONE = new Form45cc();
   private static final int MAX_NUM_OPS = 5;

   private Form45cc() {
   }

   public String insnArgString(DalvInsn insn) {
      RegisterSpecList regs = explicitize(insn.getRegisters());
      return regListString(regs) + ", " + insn.cstString();
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
         if (mci.getNumberOfConstants() != 2) {
            return false;
         } else {
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
                     return wordCount(regs) >= 0;
                  }
               }
            } else {
               return false;
            }
         }
      }
   }

   public BitSet compatibleRegs(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      int sz = regs.size();
      BitSet bits = new BitSet(sz);

      for(int i = 0; i < sz; ++i) {
         RegisterSpec reg = regs.get(i);
         bits.set(i, unsignedFitsInNibble(reg.getReg() + reg.getCategory() - 1));
      }

      return bits;
   }

   public void writeTo(AnnotatedOutput out, DalvInsn insn) {
      MultiCstInsn mci = (MultiCstInsn)insn;
      short regB = (short)mci.getIndex(0);
      short regH = (short)mci.getIndex(1);
      RegisterSpecList regs = explicitize(insn.getRegisters());
      int regA = regs.size();
      int regC = regA > 0 ? regs.get(0).getReg() : 0;
      int regD = regA > 1 ? regs.get(1).getReg() : 0;
      int regE = regA > 2 ? regs.get(2).getReg() : 0;
      int regF = regA > 3 ? regs.get(3).getReg() : 0;
      int regG = regA > 4 ? regs.get(4).getReg() : 0;
      write(out, opcodeUnit(insn, makeByte(regG, regA)), regB, codeUnit(regC, regD, regE, regF), regH);
   }

   private static int wordCount(RegisterSpecList regs) {
      int sz = regs.size();
      if (sz > 5) {
         return -1;
      } else {
         int result = 0;

         for(int i = 0; i < sz; ++i) {
            RegisterSpec one = regs.get(i);
            result += one.getCategory();
            if (!unsignedFitsInNibble(one.getReg() + one.getCategory() - 1)) {
               return -1;
            }
         }

         return result <= 5 ? result : -1;
      }
   }

   private static RegisterSpecList explicitize(RegisterSpecList orig) {
      int wordCount = wordCount(orig);
      int sz = orig.size();
      if (wordCount == sz) {
         return orig;
      } else {
         RegisterSpecList result = new RegisterSpecList(wordCount);
         int wordAt = 0;

         for(int i = 0; i < sz; ++i) {
            RegisterSpec one = orig.get(i);
            result.set(wordAt, one);
            if (one.getCategory() == 2) {
               result.set(wordAt + 1, RegisterSpec.make(one.getReg() + 1, Type.VOID));
               wordAt += 2;
            } else {
               ++wordAt;
            }
         }

         result.setImmutable();
         return result;
      }
   }
}
