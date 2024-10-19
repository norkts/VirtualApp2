package com.android.dx.dex.code.form;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.dex.code.SimpleInsn;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.util.AnnotatedOutput;
import java.util.BitSet;

public final class Form32x extends InsnFormat {
   public static final InsnFormat THE_ONE = new Form32x();

   private Form32x() {
   }

   public String insnArgString(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      return regs.get(0).regString() + ", " + regs.get(1).regString();
   }

   public String insnCommentString(DalvInsn insn, boolean noteIndices) {
      return "";
   }

   public int codeSize() {
      return 3;
   }

   public boolean isCompatible(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      return insn instanceof SimpleInsn && regs.size() == 2 && unsignedFitsInShort(regs.get(0).getReg()) && unsignedFitsInShort(regs.get(1).getReg());
   }

   public BitSet compatibleRegs(DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      BitSet bits = new BitSet(2);
      bits.set(0, unsignedFitsInShort(regs.get(0).getReg()));
      bits.set(1, unsignedFitsInShort(regs.get(1).getReg()));
      return bits;
   }

   public void writeTo(AnnotatedOutput out, DalvInsn insn) {
      RegisterSpecList regs = insn.getRegisters();
      write(out, opcodeUnit(insn, 0), (short)regs.get(0).getReg(), (short)regs.get(1).getReg());
   }
}
