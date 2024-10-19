package com.android.dx.ssa;

import com.android.dx.rop.code.BasicBlock;
import com.android.dx.rop.code.BasicBlockList;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.InsnList;
import com.android.dx.rop.code.PlainInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.util.Hex;
import com.android.dx.util.IntList;
import com.android.dx.util.IntSet;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class SsaBasicBlock {
   public static final Comparator<SsaBasicBlock> LABEL_COMPARATOR = new LabelComparator();
   private final ArrayList<SsaInsn> insns;
   private BitSet predecessors;
   private BitSet successors;
   private IntList successorList;
   private int primarySuccessor = -1;
   private final int ropLabel;
   private final SsaMethod parent;
   private final int index;
   private final ArrayList<SsaBasicBlock> domChildren;
   private int movesFromPhisAtEnd = 0;
   private int movesFromPhisAtBeginning = 0;
   private IntSet liveIn;
   private IntSet liveOut;

   public SsaBasicBlock(int basicBlockIndex, int ropLabel, SsaMethod parent) {
      this.parent = parent;
      this.index = basicBlockIndex;
      this.insns = new ArrayList();
      this.ropLabel = ropLabel;
      this.predecessors = new BitSet(parent.getBlocks().size());
      this.successors = new BitSet(parent.getBlocks().size());
      this.successorList = new IntList();
      this.domChildren = new ArrayList();
   }

   public static SsaBasicBlock newFromRop(RopMethod rmeth, int basicBlockIndex, SsaMethod parent) {
      BasicBlockList ropBlocks = rmeth.getBlocks();
      BasicBlock bb = ropBlocks.get(basicBlockIndex);
      SsaBasicBlock result = new SsaBasicBlock(basicBlockIndex, bb.getLabel(), parent);
      InsnList ropInsns = bb.getInsns();
      result.insns.ensureCapacity(ropInsns.size());
      int primarySuccessor = 0;

      for(int sz = ropInsns.size(); primarySuccessor < sz; ++primarySuccessor) {
         result.insns.add(new NormalSsaInsn(ropInsns.get(primarySuccessor), result));
      }

      result.predecessors = SsaMethod.bitSetFromLabelList(ropBlocks, rmeth.labelToPredecessors(bb.getLabel()));
      result.successors = SsaMethod.bitSetFromLabelList(ropBlocks, bb.getSuccessors());
      result.successorList = SsaMethod.indexListFromLabelList(ropBlocks, bb.getSuccessors());
      if (result.successorList.size() != 0) {
         primarySuccessor = bb.getPrimarySuccessor();
         result.primarySuccessor = primarySuccessor < 0 ? -1 : ropBlocks.indexOfLabel(primarySuccessor);
      }

      return result;
   }

   public void addDomChild(SsaBasicBlock child) {
      this.domChildren.add(child);
   }

   public ArrayList<SsaBasicBlock> getDomChildren() {
      return this.domChildren;
   }

   public void addPhiInsnForReg(int reg) {
      this.insns.add(0, new PhiInsn(reg, this));
   }

   public void addPhiInsnForReg(RegisterSpec resultSpec) {
      this.insns.add(0, new PhiInsn(resultSpec, this));
   }

   public void addInsnToHead(Insn insn) {
      SsaInsn newInsn = SsaInsn.makeFromRop(insn, this);
      this.insns.add(this.getCountPhiInsns(), newInsn);
      this.parent.onInsnAdded(newInsn);
   }

   public void replaceLastInsn(Insn insn) {
      if (insn.getOpcode().getBranchingness() == 1) {
         throw new IllegalArgumentException("last insn must branch");
      } else {
         SsaInsn oldInsn = (SsaInsn)this.insns.get(this.insns.size() - 1);
         SsaInsn newInsn = SsaInsn.makeFromRop(insn, this);
         this.insns.set(this.insns.size() - 1, newInsn);
         this.parent.onInsnRemoved(oldInsn);
         this.parent.onInsnAdded(newInsn);
      }
   }

   public void forEachPhiInsn(PhiInsn.Visitor v) {
      int sz = this.insns.size();

      for(int i = 0; i < sz; ++i) {
         SsaInsn insn = (SsaInsn)this.insns.get(i);
         if (!(insn instanceof PhiInsn)) {
            break;
         }

         v.visitPhiInsn((PhiInsn)insn);
      }

   }

   public void removeAllPhiInsns() {
      this.insns.subList(0, this.getCountPhiInsns()).clear();
   }

   private int getCountPhiInsns() {
      int sz = this.insns.size();

      int countPhiInsns;
      for(countPhiInsns = 0; countPhiInsns < sz; ++countPhiInsns) {
         SsaInsn insn = (SsaInsn)this.insns.get(countPhiInsns);
         if (!(insn instanceof PhiInsn)) {
            break;
         }
      }

      return countPhiInsns;
   }

   public ArrayList<SsaInsn> getInsns() {
      return this.insns;
   }

   public List<SsaInsn> getPhiInsns() {
      return this.insns.subList(0, this.getCountPhiInsns());
   }

   public int getIndex() {
      return this.index;
   }

   public int getRopLabel() {
      return this.ropLabel;
   }

   public String getRopLabelString() {
      return Hex.u2(this.ropLabel);
   }

   public BitSet getPredecessors() {
      return this.predecessors;
   }

   public BitSet getSuccessors() {
      return this.successors;
   }

   public IntList getSuccessorList() {
      return this.successorList;
   }

   public int getPrimarySuccessorIndex() {
      return this.primarySuccessor;
   }

   public int getPrimarySuccessorRopLabel() {
      return this.parent.blockIndexToRopLabel(this.primarySuccessor);
   }

   public SsaBasicBlock getPrimarySuccessor() {
      return this.primarySuccessor < 0 ? null : (SsaBasicBlock)this.parent.getBlocks().get(this.primarySuccessor);
   }

   public IntList getRopLabelSuccessorList() {
      IntList result = new IntList(this.successorList.size());
      int sz = this.successorList.size();

      for(int i = 0; i < sz; ++i) {
         result.add(this.parent.blockIndexToRopLabel(this.successorList.get(i)));
      }

      return result;
   }

   public SsaMethod getParent() {
      return this.parent;
   }

   public SsaBasicBlock insertNewPredecessor() {
      SsaBasicBlock newPred = this.parent.makeNewGotoBlock();
      newPred.predecessors = this.predecessors;
      newPred.successors.set(this.index);
      newPred.successorList.add(this.index);
      newPred.primarySuccessor = this.index;
      this.predecessors = new BitSet(this.parent.getBlocks().size());
      this.predecessors.set(newPred.index);

      for(int i = newPred.predecessors.nextSetBit(0); i >= 0; i = newPred.predecessors.nextSetBit(i + 1)) {
         SsaBasicBlock predBlock = (SsaBasicBlock)this.parent.getBlocks().get(i);
         predBlock.replaceSuccessor(this.index, newPred.index);
      }

      return newPred;
   }

   public SsaBasicBlock insertNewSuccessor(SsaBasicBlock other) {
      SsaBasicBlock newSucc = this.parent.makeNewGotoBlock();
      if (!this.successors.get(other.index)) {
         throw new RuntimeException("Block " + other.getRopLabelString() + " not successor of " + this.getRopLabelString());
      } else {
         newSucc.predecessors.set(this.index);
         newSucc.successors.set(other.index);
         newSucc.successorList.add(other.index);
         newSucc.primarySuccessor = other.index;

         for(int i = this.successorList.size() - 1; i >= 0; --i) {
            if (this.successorList.get(i) == other.index) {
               this.successorList.set(i, newSucc.index);
            }
         }

         if (this.primarySuccessor == other.index) {
            this.primarySuccessor = newSucc.index;
         }

         this.successors.clear(other.index);
         this.successors.set(newSucc.index);
         other.predecessors.set(newSucc.index);
         other.predecessors.set(this.index, this.successors.get(other.index));
         return newSucc;
      }
   }

   public void replaceSuccessor(int oldIndex, int newIndex) {
      if (oldIndex != newIndex) {
         this.successors.set(newIndex);
         if (this.primarySuccessor == oldIndex) {
            this.primarySuccessor = newIndex;
         }

         for(int i = this.successorList.size() - 1; i >= 0; --i) {
            if (this.successorList.get(i) == oldIndex) {
               this.successorList.set(i, newIndex);
            }
         }

         this.successors.clear(oldIndex);
         ((SsaBasicBlock)this.parent.getBlocks().get(newIndex)).predecessors.set(this.index);
         ((SsaBasicBlock)this.parent.getBlocks().get(oldIndex)).predecessors.clear(this.index);
      }
   }

   public void removeSuccessor(int oldIndex) {
      int removeIndex = 0;

      for(int i = this.successorList.size() - 1; i >= 0; --i) {
         if (this.successorList.get(i) == oldIndex) {
            removeIndex = i;
         } else {
            this.primarySuccessor = this.successorList.get(i);
         }
      }

      this.successorList.removeIndex(removeIndex);
      this.successors.clear(oldIndex);
      ((SsaBasicBlock)this.parent.getBlocks().get(oldIndex)).predecessors.clear(this.index);
   }

   public void exitBlockFixup(SsaBasicBlock exitBlock) {
      if (this != exitBlock) {
         if (this.successorList.size() == 0) {
            this.successors.set(exitBlock.index);
            this.successorList.add(exitBlock.index);
            this.primarySuccessor = exitBlock.index;
            exitBlock.predecessors.set(this.index);
         }

      }
   }

   public void addMoveToEnd(RegisterSpec result, RegisterSpec source) {
      if (this.successors.cardinality() > 1) {
         throw new IllegalStateException("Inserting a move to a block with multiple successors");
      } else if (result.getReg() != source.getReg()) {
         NormalSsaInsn lastInsn = (NormalSsaInsn)this.insns.get(this.insns.size() - 1);
         if (lastInsn.getResult() == null && lastInsn.getSources().size() <= 0) {
            RegisterSpecList sources = RegisterSpecList.make(source);
            NormalSsaInsn toAdd = new NormalSsaInsn(new PlainInsn(Rops.opMove(result.getType()), SourcePosition.NO_INFO, result, sources), this);
            this.insns.add(this.insns.size() - 1, toAdd);
            ++this.movesFromPhisAtEnd;
         } else {
            for(int i = this.successors.nextSetBit(0); i >= 0; i = this.successors.nextSetBit(i + 1)) {
               SsaBasicBlock succ = (SsaBasicBlock)this.parent.getBlocks().get(i);
               succ.addMoveToBeginning(result, source);
            }
         }

      }
   }

   public void addMoveToBeginning(RegisterSpec result, RegisterSpec source) {
      if (result.getReg() != source.getReg()) {
         RegisterSpecList sources = RegisterSpecList.make(source);
         NormalSsaInsn toAdd = new NormalSsaInsn(new PlainInsn(Rops.opMove(result.getType()), SourcePosition.NO_INFO, result, sources), this);
         this.insns.add(this.getCountPhiInsns(), toAdd);
         ++this.movesFromPhisAtBeginning;
      }
   }

   private static void setRegsUsed(BitSet regsUsed, RegisterSpec rs) {
      regsUsed.set(rs.getReg());
      if (rs.getCategory() > 1) {
         regsUsed.set(rs.getReg() + 1);
      }

   }

   private static boolean checkRegUsed(BitSet regsUsed, RegisterSpec rs) {
      int reg = rs.getReg();
      int category = rs.getCategory();
      return regsUsed.get(reg) || category == 2 && regsUsed.get(reg + 1);
   }

   private void scheduleUseBeforeAssigned(List<SsaInsn> toSchedule) {
      BitSet regsUsedAsSources = new BitSet(this.parent.getRegCount());
      BitSet regsUsedAsResults = new BitSet(this.parent.getRegCount());
      int sz = toSchedule.size();
      int insertPlace = 0;

      while(insertPlace < sz) {
         int oldInsertPlace = insertPlace;

         int i;
         for(i = insertPlace; i < sz; ++i) {
            setRegsUsed(regsUsedAsSources, ((SsaInsn)toSchedule.get(i)).getSources().get(0));
            setRegsUsed(regsUsedAsResults, ((SsaInsn)toSchedule.get(i)).getResult());
         }

         for(i = insertPlace; i < sz; ++i) {
            SsaInsn insn = (SsaInsn)toSchedule.get(i);
            if (!checkRegUsed(regsUsedAsSources, insn.getResult())) {
               Collections.swap(toSchedule, i, insertPlace++);
            }
         }

         if (oldInsertPlace == insertPlace) {
            SsaInsn insnToSplit = null;

            for(i = insertPlace; i < sz; ++i) {
               SsaInsn insn = (SsaInsn)toSchedule.get(i);
               if (checkRegUsed(regsUsedAsSources, insn.getResult()) && checkRegUsed(regsUsedAsResults, insn.getSources().get(0))) {
                  insnToSplit = insn;
                  Collections.swap(toSchedule, insertPlace, i);
                  break;
               }
            }

            RegisterSpec result = insnToSplit.getResult();
            RegisterSpec tempSpec = result.withReg(this.parent.borrowSpareRegister(result.getCategory()));
            NormalSsaInsn toAdd = new NormalSsaInsn(new PlainInsn(Rops.opMove(result.getType()), SourcePosition.NO_INFO, tempSpec, insnToSplit.getSources()), this);
            toSchedule.add(insertPlace++, toAdd);
            RegisterSpecList newSources = RegisterSpecList.make(tempSpec);
            NormalSsaInsn toReplace = new NormalSsaInsn(new PlainInsn(Rops.opMove(result.getType()), SourcePosition.NO_INFO, result, newSources), this);
            toSchedule.set(insertPlace, toReplace);
            sz = toSchedule.size();
         }

         regsUsedAsSources.clear();
         regsUsedAsResults.clear();
      }

   }

   public void addLiveOut(int regV) {
      if (this.liveOut == null) {
         this.liveOut = SetFactory.makeLivenessSet(this.parent.getRegCount());
      }

      this.liveOut.add(regV);
   }

   public void addLiveIn(int regV) {
      if (this.liveIn == null) {
         this.liveIn = SetFactory.makeLivenessSet(this.parent.getRegCount());
      }

      this.liveIn.add(regV);
   }

   public IntSet getLiveInRegs() {
      if (this.liveIn == null) {
         this.liveIn = SetFactory.makeLivenessSet(this.parent.getRegCount());
      }

      return this.liveIn;
   }

   public IntSet getLiveOutRegs() {
      if (this.liveOut == null) {
         this.liveOut = SetFactory.makeLivenessSet(this.parent.getRegCount());
      }

      return this.liveOut;
   }

   public boolean isExitBlock() {
      return this.index == this.parent.getExitBlockIndex();
   }

   public void scheduleMovesFromPhis() {
      if (this.movesFromPhisAtBeginning > 1) {
         List<SsaInsn> toSchedule = this.insns.subList(0, this.movesFromPhisAtBeginning);
         this.scheduleUseBeforeAssigned(toSchedule);
         SsaInsn firstNonPhiMoveInsn = (SsaInsn)this.insns.get(this.movesFromPhisAtBeginning);
         if (firstNonPhiMoveInsn.isMoveException()) {
            throw new RuntimeException("Unexpected: moves from phis before move-exception");
         }
      }

      if (this.movesFromPhisAtEnd > 1) {
         this.scheduleUseBeforeAssigned(this.insns.subList(this.insns.size() - this.movesFromPhisAtEnd - 1, this.insns.size() - 1));
      }

      this.parent.returnSpareRegisters();
   }

   public void forEachInsn(SsaInsn.Visitor visitor) {
      int len = this.insns.size();

      for(int i = 0; i < len; ++i) {
         ((SsaInsn)this.insns.get(i)).accept(visitor);
      }

   }

   public String toString() {
      return "{" + this.index + ":" + Hex.u2(this.ropLabel) + '}';
   }

   public static final class LabelComparator implements Comparator<SsaBasicBlock> {
      public int compare(SsaBasicBlock b1, SsaBasicBlock b2) {
         int label1 = b1.ropLabel;
         int label2 = b2.ropLabel;
         if (label1 < label2) {
            return -1;
         } else {
            return label1 > label2 ? 1 : 0;
         }
      }
   }

   public interface Visitor {
      void visitBlock(SsaBasicBlock var1, SsaBasicBlock var2);
   }
}
