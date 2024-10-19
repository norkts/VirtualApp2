package com.android.dx.ssa;

import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.util.ToHuman;

public abstract class SsaInsn implements ToHuman, Cloneable {
   private final SsaBasicBlock block;
   private RegisterSpec result;

   protected SsaInsn(RegisterSpec result, SsaBasicBlock block) {
      if (block == null) {
         throw new NullPointerException("block == null");
      } else {
         this.block = block;
         this.result = result;
      }
   }

   public static SsaInsn makeFromRop(Insn insn, SsaBasicBlock block) {
      return new NormalSsaInsn(insn, block);
   }

   public SsaInsn clone() {
      try {
         return (SsaInsn)super.clone();
      } catch (CloneNotSupportedException var2) {
         CloneNotSupportedException ex = var2;
         throw new RuntimeException("unexpected", ex);
      }
   }

   public RegisterSpec getResult() {
      return this.result;
   }

   protected void setResult(RegisterSpec result) {
      if (result == null) {
         throw new NullPointerException("result == null");
      } else {
         this.result = result;
      }
   }

   public abstract RegisterSpecList getSources();

   public SsaBasicBlock getBlock() {
      return this.block;
   }

   public boolean isResultReg(int reg) {
      return this.result != null && this.result.getReg() == reg;
   }

   public void changeResultReg(int reg) {
      if (this.result != null) {
         this.result = this.result.withReg(reg);
      }

   }

   public final void setResultLocal(LocalItem local) {
      LocalItem oldItem = this.result.getLocalItem();
      if (local != oldItem && (local == null || !local.equals(this.result.getLocalItem()))) {
         this.result = RegisterSpec.makeLocalOptional(this.result.getReg(), this.result.getType(), local);
      }

   }

   public final void mapRegisters(RegisterMapper mapper) {
      RegisterSpec oldResult = this.result;
      this.result = mapper.map(this.result);
      this.block.getParent().updateOneDefinition(this, oldResult);
      this.mapSourceRegisters(mapper);
   }

   public abstract void mapSourceRegisters(RegisterMapper var1);

   public abstract Rop getOpcode();

   public abstract Insn getOriginalRopInsn();

   public RegisterSpec getLocalAssignment() {
      return this.result != null && this.result.getLocalItem() != null ? this.result : null;
   }

   public boolean isRegASource(int reg) {
      return null != this.getSources().specForRegister(reg);
   }

   public abstract Insn toRopInsn();

   public abstract boolean isPhiOrMove();

   public abstract boolean hasSideEffect();

   public boolean isNormalMoveInsn() {
      return false;
   }

   public boolean isMoveException() {
      return false;
   }

   public abstract boolean canThrow();

   public abstract void accept(Visitor var1);

   public interface Visitor {
      void visitMoveInsn(NormalSsaInsn var1);

      void visitPhiInsn(PhiInsn var1);

      void visitNonMoveInsn(NormalSsaInsn var1);
   }
}
