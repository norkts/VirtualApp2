package com.android.dx.ssa;

import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;

public final class NormalSsaInsn extends SsaInsn implements Cloneable {
   private Insn insn;

   NormalSsaInsn(Insn insn, SsaBasicBlock block) {
      super(insn.getResult(), block);
      this.insn = insn;
   }

   public final void mapSourceRegisters(RegisterMapper mapper) {
      RegisterSpecList oldSources = this.insn.getSources();
      RegisterSpecList newSources = mapper.map(oldSources);
      if (newSources != oldSources) {
         this.insn = this.insn.withNewRegisters(this.getResult(), newSources);
         this.getBlock().getParent().onSourcesChanged(this, oldSources);
      }

   }

   public final void changeOneSource(int index, RegisterSpec newSpec) {
      RegisterSpecList origSources = this.insn.getSources();
      int sz = origSources.size();
      RegisterSpecList newSources = new RegisterSpecList(sz);

      for(int i = 0; i < sz; ++i) {
         newSources.set(i, i == index ? newSpec : origSources.get(i));
      }

      newSources.setImmutable();
      RegisterSpec origSpec = origSources.get(index);
      if (origSpec.getReg() != newSpec.getReg()) {
         this.getBlock().getParent().onSourceChanged(this, origSpec, newSpec);
      }

      this.insn = this.insn.withNewRegisters(this.getResult(), newSources);
   }

   public final void setNewSources(RegisterSpecList newSources) {
      RegisterSpecList origSources = this.insn.getSources();
      if (origSources.size() != newSources.size()) {
         throw new RuntimeException("Sources counts don't match");
      } else {
         this.insn = this.insn.withNewRegisters(this.getResult(), newSources);
      }
   }

   public NormalSsaInsn clone() {
      return (NormalSsaInsn)super.clone();
   }

   public RegisterSpecList getSources() {
      return this.insn.getSources();
   }

   public String toHuman() {
      return this.toRopInsn().toHuman();
   }

   public Insn toRopInsn() {
      return this.insn.withNewRegisters(this.getResult(), this.insn.getSources());
   }

   public Rop getOpcode() {
      return this.insn.getOpcode();
   }

   public Insn getOriginalRopInsn() {
      return this.insn;
   }

   public RegisterSpec getLocalAssignment() {
      RegisterSpec assignment;
      if (this.insn.getOpcode().getOpcode() == 54) {
         assignment = this.insn.getSources().get(0);
      } else {
         assignment = this.getResult();
      }

      if (assignment == null) {
         return null;
      } else {
         LocalItem local = assignment.getLocalItem();
         return local == null ? null : assignment;
      }
   }

   public void upgradeToLiteral() {
      RegisterSpecList oldSources = this.insn.getSources();
      this.insn = this.insn.withSourceLiteral();
      this.getBlock().getParent().onSourcesChanged(this, oldSources);
   }

   public boolean isNormalMoveInsn() {
      return this.insn.getOpcode().getOpcode() == 2;
   }

   public boolean isMoveException() {
      return this.insn.getOpcode().getOpcode() == 4;
   }

   public boolean canThrow() {
      return this.insn.canThrow();
   }

   public void accept(SsaInsn.Visitor v) {
      if (this.isNormalMoveInsn()) {
         v.visitMoveInsn(this);
      } else {
         v.visitNonMoveInsn(this);
      }

   }

   public boolean isPhiOrMove() {
      return this.isNormalMoveInsn();
   }

   public boolean hasSideEffect() {
      Rop opcode = this.getOpcode();
      if (opcode.getBranchingness() != 1) {
         return true;
      } else {
         boolean hasLocalSideEffect = Optimizer.getPreserveLocals() && this.getLocalAssignment() != null;
         switch (opcode.getOpcode()) {
            case 2:
            case 5:
            case 55:
               return hasLocalSideEffect;
            default:
               return true;
         }
      }
   }
}
