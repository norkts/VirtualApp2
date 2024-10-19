package com.android.dx.dex.code.form;

import com.android.dx.dex.code.CstInsn;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstCallSiteRef;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.util.AnnotatedOutput;
import java.util.BitSet;

public final class Form35c extends InsnFormat {
   public static final InsnFormat THE_ONE = new Form35c();
   private static final int MAX_NUM_OPS = 5;

   private Form35c() {
   }

   public String insnArgString(DalvInsn insn) {
      RegisterSpecList regs = explicitize(insn.getRegisters());
      return regListString(regs) + ", " + insn.cstString();
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
         if (!unsignedFitsInShort(cpi)) {
            return false;
         } else {
            Constant cst = ci.getConstant();
            if (!(cst instanceof CstMethodRef) && !(cst instanceof CstType) && !(cst instanceof CstCallSiteRef)) {
               return false;
            } else {
               RegisterSpecList regs = ci.getRegisters();
               return wordCount(regs) >= 0;
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
      int cpi = ((CstInsn)insn).getIndex();
      RegisterSpecList regs = explicitize(insn.getRegisters());
      int sz = regs.size();
      int r0 = sz > 0 ? regs.get(0).getReg() : 0;
      int r1 = sz > 1 ? regs.get(1).getReg() : 0;
      int r2 = sz > 2 ? regs.get(2).getReg() : 0;
      int r3 = sz > 3 ? regs.get(3).getReg() : 0;
      int r4 = sz > 4 ? regs.get(4).getReg() : 0;
      write(out, opcodeUnit(insn, makeByte(r4, sz)), (short)cpi, codeUnit(r0, r1, r2, r3));
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
