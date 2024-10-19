package com.android.dx.rop.code;

import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.Hex;
import com.android.dx.util.IntList;
import com.android.dx.util.LabeledList;

public final class BasicBlockList extends LabeledList {
   private int regCount;

   public BasicBlockList(int size) {
      super(size);
      this.regCount = -1;
   }

   private BasicBlockList(BasicBlockList old) {
      super(old);
      this.regCount = old.regCount;
   }

   public BasicBlock get(int n) {
      return (BasicBlock)this.get0(n);
   }

   public void set(int n, BasicBlock bb) {
      super.set(n, bb);
      this.regCount = -1;
   }

   public int getRegCount() {
      if (this.regCount == -1) {
         RegCountVisitor visitor = new RegCountVisitor();
         this.forEachInsn(visitor);
         this.regCount = visitor.getRegCount();
      }

      return this.regCount;
   }

   public int getInstructionCount() {
      int sz = this.size();
      int result = 0;

      for(int i = 0; i < sz; ++i) {
         BasicBlock one = (BasicBlock)this.getOrNull0(i);
         if (one != null) {
            result += one.getInsns().size();
         }
      }

      return result;
   }

   public int getEffectiveInstructionCount() {
      int sz = this.size();
      int result = 0;

      for(int i = 0; i < sz; ++i) {
         BasicBlock one = (BasicBlock)this.getOrNull0(i);
         if (one != null) {
            InsnList insns = one.getInsns();
            int insnsSz = insns.size();

            for(int j = 0; j < insnsSz; ++j) {
               Insn insn = insns.get(j);
               if (insn.getOpcode().getOpcode() != 54) {
                  ++result;
               }
            }
         }
      }

      return result;
   }

   public BasicBlock labelToBlock(int label) {
      int idx = this.indexOfLabel(label);
      if (idx < 0) {
         throw new IllegalArgumentException("no such label: " + Hex.u2(label));
      } else {
         return this.get(idx);
      }
   }

   public void forEachInsn(Insn.Visitor visitor) {
      int sz = this.size();

      for(int i = 0; i < sz; ++i) {
         BasicBlock one = this.get(i);
         InsnList insns = one.getInsns();
         insns.forEach(visitor);
      }

   }

   public BasicBlockList withRegisterOffset(int delta) {
      int sz = this.size();
      BasicBlockList result = new BasicBlockList(sz);

      for(int i = 0; i < sz; ++i) {
         BasicBlock one = (BasicBlock)this.get0(i);
         if (one != null) {
            result.set(i, one.withRegisterOffset(delta));
         }
      }

      if (this.isImmutable()) {
         result.setImmutable();
      }

      return result;
   }

   public BasicBlockList getMutableCopy() {
      return new BasicBlockList(this);
   }

   public BasicBlock preferredSuccessorOf(BasicBlock block) {
      int primarySuccessor = block.getPrimarySuccessor();
      IntList successors = block.getSuccessors();
      int succSize = successors.size();
      switch (succSize) {
         case 0:
            return null;
         case 1:
            return this.labelToBlock(successors.get(0));
         default:
            return primarySuccessor != -1 ? this.labelToBlock(primarySuccessor) : this.labelToBlock(successors.get(0));
      }
   }

   public boolean catchesEqual(BasicBlock block1, BasicBlock block2) {
      TypeList catches1 = block1.getExceptionHandlerTypes();
      TypeList catches2 = block2.getExceptionHandlerTypes();
      if (!StdTypeList.equalContents(catches1, catches2)) {
         return false;
      } else {
         IntList succ1 = block1.getSuccessors();
         IntList succ2 = block2.getSuccessors();
         int size = succ1.size();
         int primary1 = block1.getPrimarySuccessor();
         int primary2 = block2.getPrimarySuccessor();
         if ((primary1 == -1 || primary2 == -1) && primary1 != primary2) {
            return false;
         } else {
            for(int i = 0; i < size; ++i) {
               int label1 = succ1.get(i);
               int label2 = succ2.get(i);
               if (label1 == primary1) {
                  if (label2 != primary2) {
                     return false;
                  }
               } else if (label1 != label2) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   private static class RegCountVisitor implements Insn.Visitor {
      private int regCount = 0;

      public RegCountVisitor() {
      }

      public int getRegCount() {
         return this.regCount;
      }

      public void visitPlainInsn(PlainInsn insn) {
         this.visit(insn);
      }

      public void visitPlainCstInsn(PlainCstInsn insn) {
         this.visit(insn);
      }

      public void visitSwitchInsn(SwitchInsn insn) {
         this.visit(insn);
      }

      public void visitThrowingCstInsn(ThrowingCstInsn insn) {
         this.visit(insn);
      }

      public void visitThrowingInsn(ThrowingInsn insn) {
         this.visit(insn);
      }

      public void visitFillArrayDataInsn(FillArrayDataInsn insn) {
         this.visit(insn);
      }

      public void visitInvokePolymorphicInsn(InvokePolymorphicInsn insn) {
         this.visit(insn);
      }

      private void visit(Insn insn) {
         RegisterSpec result = insn.getResult();
         if (result != null) {
            this.processReg(result);
         }

         RegisterSpecList sources = insn.getSources();
         int sz = sources.size();

         for(int i = 0; i < sz; ++i) {
            this.processReg(sources.get(i));
         }

      }

      private void processReg(RegisterSpec spec) {
         int reg = spec.getNextReg();
         if (reg > this.regCount) {
            this.regCount = reg;
         }

      }
   }
}
