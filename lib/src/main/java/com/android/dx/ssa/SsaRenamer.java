package com.android.dx.ssa;

import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.code.PlainInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.type.Type;
import com.android.dx.util.IntList;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class SsaRenamer implements Runnable {
   private static final boolean DEBUG = false;
   private final SsaMethod ssaMeth;
   private int nextSsaReg;
   private final int ropRegCount;
   private int threshold;
   private final RegisterSpec[][] startsForBlocks;
   private final ArrayList<LocalItem> ssaRegToLocalItems;
   private IntList ssaRegToRopReg;

   public SsaRenamer(SsaMethod ssaMeth) {
      this.ropRegCount = ssaMeth.getRegCount();
      this.ssaMeth = ssaMeth;
      this.nextSsaReg = this.ropRegCount;
      this.threshold = 0;
      this.startsForBlocks = new RegisterSpec[ssaMeth.getBlocks().size()][];
      this.ssaRegToLocalItems = new ArrayList();
      RegisterSpec[] initialRegMapping = new RegisterSpec[this.ropRegCount];

      for(int i = 0; i < this.ropRegCount; ++i) {
         initialRegMapping[i] = RegisterSpec.make(i, Type.VOID);
      }

      this.startsForBlocks[ssaMeth.getEntryBlockIndex()] = initialRegMapping;
   }

   public SsaRenamer(SsaMethod ssaMeth, int thresh) {
      this(ssaMeth);
      this.threshold = thresh;
   }

   public void run() {
      this.ssaMeth.forEachBlockDepthFirstDom(new SsaBasicBlock.Visitor() {
         public void visitBlock(SsaBasicBlock block, SsaBasicBlock unused) {
            (SsaRenamer.this.new BlockRenamer(block)).process();
         }
      });
      this.ssaMeth.setNewRegCount(this.nextSsaReg);
      this.ssaMeth.onInsnsChanged();
   }

   private static RegisterSpec[] dupArray(RegisterSpec[] orig) {
      RegisterSpec[] copy = new RegisterSpec[orig.length];
      System.arraycopy(orig, 0, copy, 0, orig.length);
      return copy;
   }

   private LocalItem getLocalForNewReg(int ssaReg) {
      return ssaReg < this.ssaRegToLocalItems.size() ? (LocalItem)this.ssaRegToLocalItems.get(ssaReg) : null;
   }

   private void setNameForSsaReg(RegisterSpec ssaReg) {
      int reg = ssaReg.getReg();
      LocalItem local = ssaReg.getLocalItem();
      this.ssaRegToLocalItems.ensureCapacity(reg + 1);

      while(this.ssaRegToLocalItems.size() <= reg) {
         this.ssaRegToLocalItems.add((Object)null);
      }

      this.ssaRegToLocalItems.set(reg, local);
   }

   private boolean isBelowThresholdRegister(int ssaReg) {
      return ssaReg < this.threshold;
   }

   private boolean isVersionZeroRegister(int ssaReg) {
      return ssaReg < this.ropRegCount;
   }

   private static boolean equalsHandlesNulls(Object a, Object b) {
      return a == b || a != null && a.equals(b);
   }

   private class BlockRenamer implements SsaInsn.Visitor {
      private final SsaBasicBlock block;
      private final RegisterSpec[] currentMapping;
      private final HashSet<SsaInsn> movesToKeep;
      private final HashMap<SsaInsn, SsaInsn> insnsToReplace;
      private final RenamingMapper mapper;

      BlockRenamer(SsaBasicBlock block) {
         this.block = block;
         this.currentMapping = SsaRenamer.this.startsForBlocks[block.getIndex()];
         this.movesToKeep = new HashSet();
         this.insnsToReplace = new HashMap();
         this.mapper = new RenamingMapper();
         SsaRenamer.this.startsForBlocks[block.getIndex()] = null;
      }

      public void process() {
         this.block.forEachInsn(this);
         this.updateSuccessorPhis();
         ArrayList<SsaInsn> insns = this.block.getInsns();
         int szInsns = insns.size();

         for(int i = szInsns - 1; i >= 0; --i) {
            SsaInsn insn = (SsaInsn)insns.get(i);
            SsaInsn replaceInsn = (SsaInsn)this.insnsToReplace.get(insn);
            if (replaceInsn != null) {
               insns.set(i, replaceInsn);
            } else if (insn.isNormalMoveInsn() && !this.movesToKeep.contains(insn)) {
               insns.remove(i);
            }
         }

         boolean first = true;
         Iterator var8 = this.block.getDomChildren().iterator();

         while(var8.hasNext()) {
            SsaBasicBlock child = (SsaBasicBlock)var8.next();
            if (child != this.block) {
               RegisterSpec[] childStart = first ? this.currentMapping : SsaRenamer.dupArray(this.currentMapping);
               SsaRenamer.this.startsForBlocks[child.getIndex()] = childStart;
               first = false;
            }
         }

      }

      private void addMapping(int ropReg, RegisterSpec ssaReg) {
         int ssaRegNum = ssaReg.getReg();
         LocalItem ssaRegLocal = ssaReg.getLocalItem();
         this.currentMapping[ropReg] = ssaReg;

         int i;
         RegisterSpec cur;
         for(i = this.currentMapping.length - 1; i >= 0; --i) {
            cur = this.currentMapping[i];
            if (ssaRegNum == cur.getReg()) {
               this.currentMapping[i] = ssaReg;
            }
         }

         if (ssaRegLocal != null) {
            SsaRenamer.this.setNameForSsaReg(ssaReg);

            for(i = this.currentMapping.length - 1; i >= 0; --i) {
               cur = this.currentMapping[i];
               if (ssaRegNum != cur.getReg() && ssaRegLocal.equals(cur.getLocalItem())) {
                  this.currentMapping[i] = cur.withLocalItem((LocalItem)null);
               }
            }

         }
      }

      public void visitPhiInsn(PhiInsn phi) {
         this.processResultReg(phi);
      }

      public void visitMoveInsn(NormalSsaInsn insn) {
         RegisterSpec ropResult = insn.getResult();
         int ropResultReg = ropResult.getReg();
         int ropSourceReg = insn.getSources().get(0).getReg();
         insn.mapSourceRegisters(this.mapper);
         int ssaSourceReg = insn.getSources().get(0).getReg();
         LocalItem sourceLocal = this.currentMapping[ropSourceReg].getLocalItem();
         LocalItem resultLocal = ropResult.getLocalItem();
         LocalItem newLocal = resultLocal == null ? sourceLocal : resultLocal;
         LocalItem associatedLocal = SsaRenamer.this.getLocalForNewReg(ssaSourceReg);
         boolean onlyOneAssociatedLocal = associatedLocal == null || newLocal == null || newLocal.equals(associatedLocal);
         RegisterSpec ssaReg = RegisterSpec.makeLocalOptional(ssaSourceReg, ropResult.getType(), newLocal);
         if (!Optimizer.getPreserveLocals() || onlyOneAssociatedLocal && SsaRenamer.equalsHandlesNulls(newLocal, sourceLocal) && SsaRenamer.this.threshold == 0) {
            this.addMapping(ropResultReg, ssaReg);
         } else if (onlyOneAssociatedLocal && sourceLocal == null && SsaRenamer.this.threshold == 0) {
            RegisterSpecList ssaSources = RegisterSpecList.make(RegisterSpec.make(ssaReg.getReg(), ssaReg.getType(), newLocal));
            SsaInsn newInsn = SsaInsn.makeFromRop(new PlainInsn(Rops.opMarkLocal(ssaReg), SourcePosition.NO_INFO, (RegisterSpec)null, ssaSources), this.block);
            this.insnsToReplace.put(insn, newInsn);
            this.addMapping(ropResultReg, ssaReg);
         } else {
            this.processResultReg(insn);
            this.movesToKeep.add(insn);
         }

      }

      public void visitNonMoveInsn(NormalSsaInsn insn) {
         insn.mapSourceRegisters(this.mapper);
         this.processResultReg(insn);
      }

      void processResultReg(SsaInsn insn) {
         RegisterSpec ropResult = insn.getResult();
         if (ropResult != null) {
            int ropReg = ropResult.getReg();
            if (!SsaRenamer.this.isBelowThresholdRegister(ropReg)) {
               insn.changeResultReg(SsaRenamer.this.nextSsaReg);
               this.addMapping(ropReg, insn.getResult());
               SsaRenamer.this.nextSsaReg++;
            }
         }
      }

      private void updateSuccessorPhis() {
         PhiInsn.Visitor visitor = new PhiInsn.Visitor() {
            public void visitPhiInsn(PhiInsn insn) {
               int ropReg = insn.getRopResultReg();
               if (!SsaRenamer.this.isBelowThresholdRegister(ropReg)) {
                  RegisterSpec stackTop = BlockRenamer.this.currentMapping[ropReg];
                  if (!SsaRenamer.this.isVersionZeroRegister(stackTop.getReg())) {
                     insn.addPhiOperand(stackTop, BlockRenamer.this.block);
                  }

               }
            }
         };
         BitSet successors = this.block.getSuccessors();

         for(int i = successors.nextSetBit(0); i >= 0; i = successors.nextSetBit(i + 1)) {
            SsaBasicBlock successor = (SsaBasicBlock)SsaRenamer.this.ssaMeth.getBlocks().get(i);
            successor.forEachPhiInsn(visitor);
         }

      }

      private class RenamingMapper extends RegisterMapper {
         public RenamingMapper() {
         }

         public int getNewRegisterCount() {
            return SsaRenamer.this.nextSsaReg;
         }

         public RegisterSpec map(RegisterSpec registerSpec) {
            if (registerSpec == null) {
               return null;
            } else {
               int reg = registerSpec.getReg();
               return registerSpec.withReg(BlockRenamer.this.currentMapping[reg].getReg());
            }
         }
      }
   }
}
