package com.android.dx.ssa;

import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.util.Hex;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class PhiInsn extends SsaInsn {
   private final int ropResultReg;
   private final ArrayList<Operand> operands = new ArrayList();
   private RegisterSpecList sources;

   public PhiInsn(RegisterSpec resultReg, SsaBasicBlock block) {
      super(resultReg, block);
      this.ropResultReg = resultReg.getReg();
   }

   public PhiInsn(int resultReg, SsaBasicBlock block) {
      super(RegisterSpec.make(resultReg, Type.VOID), block);
      this.ropResultReg = resultReg;
   }

   public PhiInsn clone() {
      throw new UnsupportedOperationException("can't clone phi");
   }

   public void updateSourcesToDefinitions(SsaMethod ssaMeth) {
      Operand o;
      RegisterSpec def;
      for(Iterator var2 = this.operands.iterator(); var2.hasNext(); o.regSpec = o.regSpec.withType(def.getType())) {
         o = (Operand)var2.next();
         def = ssaMeth.getDefinitionForRegister(o.regSpec.getReg()).getResult();
      }

      this.sources = null;
   }

   public void changeResultType(TypeBearer type, LocalItem local) {
      this.setResult(RegisterSpec.makeLocalOptional(this.getResult().getReg(), type, local));
   }

   public int getRopResultReg() {
      return this.ropResultReg;
   }

   public void addPhiOperand(RegisterSpec registerSpec, SsaBasicBlock predBlock) {
      this.operands.add(new Operand(registerSpec, predBlock.getIndex(), predBlock.getRopLabel()));
      this.sources = null;
   }

   public void removePhiRegister(RegisterSpec registerSpec) {
      ArrayList<Operand> operandsToRemove = new ArrayList();
      Iterator var3 = this.operands.iterator();

      while(var3.hasNext()) {
         Operand o = (Operand)var3.next();
         if (o.regSpec.getReg() == registerSpec.getReg()) {
            operandsToRemove.add(o);
         }
      }

      this.operands.removeAll(operandsToRemove);
      this.sources = null;
   }

   public int predBlockIndexForSourcesIndex(int sourcesIndex) {
      return ((Operand)this.operands.get(sourcesIndex)).blockIndex;
   }

   public Rop getOpcode() {
      return null;
   }

   public Insn getOriginalRopInsn() {
      return null;
   }

   public boolean canThrow() {
      return false;
   }

   public RegisterSpecList getSources() {
      if (this.sources != null) {
         return this.sources;
      } else if (this.operands.size() == 0) {
         return RegisterSpecList.EMPTY;
      } else {
         int szSources = this.operands.size();
         this.sources = new RegisterSpecList(szSources);

         for(int i = 0; i < szSources; ++i) {
            Operand o = (Operand)this.operands.get(i);
            this.sources.set(i, o.regSpec);
         }

         this.sources.setImmutable();
         return this.sources;
      }
   }

   public boolean isRegASource(int reg) {
      Iterator var2 = this.operands.iterator();

      Operand o;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         o = (Operand)var2.next();
      } while(o.regSpec.getReg() != reg);

      return true;
   }

   public boolean areAllOperandsEqual() {
      if (this.operands.size() == 0) {
         return true;
      } else {
         int firstReg = ((Operand)this.operands.get(0)).regSpec.getReg();
         Iterator var2 = this.operands.iterator();

         Operand o;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            o = (Operand)var2.next();
         } while(firstReg == o.regSpec.getReg());

         return false;
      }
   }

   public final void mapSourceRegisters(RegisterMapper mapper) {
      Iterator var2 = this.operands.iterator();

      while(var2.hasNext()) {
         Operand o = (Operand)var2.next();
         RegisterSpec old = o.regSpec;
         o.regSpec = mapper.map(old);
         if (old != o.regSpec) {
            this.getBlock().getParent().onSourceChanged(this, old, o.regSpec);
         }
      }

      this.sources = null;
   }

   public Insn toRopInsn() {
      throw new IllegalArgumentException("Cannot convert phi insns to rop form");
   }

   public List<SsaBasicBlock> predBlocksForReg(int reg, SsaMethod ssaMeth) {
      ArrayList<SsaBasicBlock> ret = new ArrayList();
      Iterator var4 = this.operands.iterator();

      while(var4.hasNext()) {
         Operand o = (Operand)var4.next();
         if (o.regSpec.getReg() == reg) {
            ret.add((SsaBasicBlock)ssaMeth.getBlocks().get(o.blockIndex));
         }
      }

      return ret;
   }

   public boolean isPhiOrMove() {
      return true;
   }

   public boolean hasSideEffect() {
      return Optimizer.getPreserveLocals() && this.getLocalAssignment() != null;
   }

   public void accept(SsaInsn.Visitor v) {
      v.visitPhiInsn(this);
   }

   public String toHuman() {
      return this.toHumanWithInline((String)null);
   }

   protected final String toHumanWithInline(String extra) {
      StringBuilder sb = new StringBuilder(80);
      sb.append(SourcePosition.NO_INFO);
      sb.append(": phi");
      if (extra != null) {
         sb.append("(");
         sb.append(extra);
         sb.append(")");
      }

      RegisterSpec result = this.getResult();
      if (result == null) {
         sb.append(" .");
      } else {
         sb.append(" ");
         sb.append(result.toHuman());
      }

      sb.append(" <-");
      int sz = this.getSources().size();
      if (sz == 0) {
         sb.append(" .");
      } else {
         for(int i = 0; i < sz; ++i) {
            sb.append(" ");
            sb.append(this.sources.get(i).toHuman() + "[b=" + Hex.u2(((Operand)this.operands.get(i)).ropLabel) + "]");
         }
      }

      return sb.toString();
   }

   public interface Visitor {
      void visitPhiInsn(PhiInsn var1);
   }

   private static class Operand {
      public RegisterSpec regSpec;
      public final int blockIndex;
      public final int ropLabel;

      public Operand(RegisterSpec regSpec, int blockIndex, int ropLabel) {
         this.regSpec = regSpec;
         this.blockIndex = blockIndex;
         this.ropLabel = ropLabel;
      }
   }
}
