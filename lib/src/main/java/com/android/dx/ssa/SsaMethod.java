package com.android.dx.ssa;

import com.android.dx.rop.code.BasicBlockList;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.PlainInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.util.IntList;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public final class SsaMethod {
   private ArrayList<SsaBasicBlock> blocks;
   private int entryBlockIndex;
   private int exitBlockIndex;
   private int registerCount;
   private int spareRegisterBase;
   private int borrowedSpareRegisters;
   private int maxLabel;
   private final int paramWidth;
   private final boolean isStatic;
   private SsaInsn[] definitionList;
   private ArrayList<SsaInsn>[] useList;
   private List<SsaInsn>[] unmodifiableUseList;
   private boolean backMode;

   public static SsaMethod newFromRopMethod(RopMethod ropMethod, int paramWidth, boolean isStatic) {
      SsaMethod result = new SsaMethod(ropMethod, paramWidth, isStatic);
      result.convertRopToSsaBlocks(ropMethod);
      return result;
   }

   private SsaMethod(RopMethod ropMethod, int paramWidth, boolean isStatic) {
      this.paramWidth = paramWidth;
      this.isStatic = isStatic;
      this.backMode = false;
      this.maxLabel = ropMethod.getBlocks().getMaxLabel();
      this.registerCount = ropMethod.getBlocks().getRegCount();
      this.spareRegisterBase = this.registerCount;
   }

   static BitSet bitSetFromLabelList(BasicBlockList blocks, IntList labelList) {
      BitSet result = new BitSet(blocks.size());
      int i = 0;

      for(int sz = labelList.size(); i < sz; ++i) {
         result.set(blocks.indexOfLabel(labelList.get(i)));
      }

      return result;
   }

   public static IntList indexListFromLabelList(BasicBlockList ropBlocks, IntList labelList) {
      IntList result = new IntList(labelList.size());
      int i = 0;

      for(int sz = labelList.size(); i < sz; ++i) {
         result.add(ropBlocks.indexOfLabel(labelList.get(i)));
      }

      return result;
   }

   private void convertRopToSsaBlocks(RopMethod rmeth) {
      BasicBlockList ropBlocks = rmeth.getBlocks();
      int sz = ropBlocks.size();
      this.blocks = new ArrayList(sz + 2);

      int origEntryBlockIndex;
      SsaBasicBlock entryBlock;
      for(origEntryBlockIndex = 0; origEntryBlockIndex < sz; ++origEntryBlockIndex) {
         entryBlock = SsaBasicBlock.newFromRop(rmeth, origEntryBlockIndex, this);
         this.blocks.add(entryBlock);
      }

      origEntryBlockIndex = rmeth.getBlocks().indexOfLabel(rmeth.getFirstLabel());
      entryBlock = ((SsaBasicBlock)this.blocks.get(origEntryBlockIndex)).insertNewPredecessor();
      this.entryBlockIndex = entryBlock.getIndex();
      this.exitBlockIndex = -1;
   }

   void makeExitBlock() {
      if (this.exitBlockIndex >= 0) {
         throw new RuntimeException("must be called at most once");
      } else {
         this.exitBlockIndex = this.blocks.size();
         SsaBasicBlock exitBlock = new SsaBasicBlock(this.exitBlockIndex, this.maxLabel++, this);
         this.blocks.add(exitBlock);
         Iterator var2 = this.blocks.iterator();

         while(var2.hasNext()) {
            SsaBasicBlock block = (SsaBasicBlock)var2.next();
            block.exitBlockFixup(exitBlock);
         }

         if (exitBlock.getPredecessors().cardinality() == 0) {
            this.blocks.remove(this.exitBlockIndex);
            this.exitBlockIndex = -1;
            --this.maxLabel;
         }

      }
   }

   private static SsaInsn getGoto(SsaBasicBlock block) {
      return new NormalSsaInsn(new PlainInsn(Rops.GOTO, SourcePosition.NO_INFO, (RegisterSpec)null, RegisterSpecList.EMPTY), block);
   }

   public SsaBasicBlock makeNewGotoBlock() {
      int newIndex = this.blocks.size();
      SsaBasicBlock newBlock = new SsaBasicBlock(newIndex, this.maxLabel++, this);
      newBlock.getInsns().add(getGoto(newBlock));
      this.blocks.add(newBlock);
      return newBlock;
   }

   public int getEntryBlockIndex() {
      return this.entryBlockIndex;
   }

   public SsaBasicBlock getEntryBlock() {
      return (SsaBasicBlock)this.blocks.get(this.entryBlockIndex);
   }

   public int getExitBlockIndex() {
      return this.exitBlockIndex;
   }

   public SsaBasicBlock getExitBlock() {
      return this.exitBlockIndex < 0 ? null : (SsaBasicBlock)this.blocks.get(this.exitBlockIndex);
   }

   public int blockIndexToRopLabel(int bi) {
      return bi < 0 ? -1 : ((SsaBasicBlock)this.blocks.get(bi)).getRopLabel();
   }

   public int getRegCount() {
      return this.registerCount;
   }

   public int getParamWidth() {
      return this.paramWidth;
   }

   public boolean isStatic() {
      return this.isStatic;
   }

   public int borrowSpareRegister(int category) {
      int result = this.spareRegisterBase + this.borrowedSpareRegisters;
      this.borrowedSpareRegisters += category;
      this.registerCount = Math.max(this.registerCount, result + category);
      return result;
   }

   public void returnSpareRegisters() {
      this.borrowedSpareRegisters = 0;
   }

   public ArrayList<SsaBasicBlock> getBlocks() {
      return this.blocks;
   }

   public BitSet computeReachability() {
      int size = this.blocks.size();
      BitSet reachableUnvisited = new BitSet(size);
      BitSet reachableVisited = new BitSet(size);
      reachableUnvisited.set(this.getEntryBlock().getIndex());

      int index;
      while((index = reachableUnvisited.nextSetBit(0)) != -1) {
         reachableVisited.set(index);
         reachableUnvisited.or(((SsaBasicBlock)this.blocks.get(index)).getSuccessors());
         reachableUnvisited.andNot(reachableVisited);
      }

      return reachableVisited;
   }

   public void mapRegisters(RegisterMapper mapper) {
      Iterator var2 = this.getBlocks().iterator();

      while(var2.hasNext()) {
         SsaBasicBlock block = (SsaBasicBlock)var2.next();
         Iterator var4 = block.getInsns().iterator();

         while(var4.hasNext()) {
            SsaInsn insn = (SsaInsn)var4.next();
            insn.mapRegisters(mapper);
         }
      }

      this.registerCount = mapper.getNewRegisterCount();
      this.spareRegisterBase = this.registerCount;
   }

   public SsaInsn getDefinitionForRegister(int reg) {
      if (this.backMode) {
         throw new RuntimeException("No def list in back mode");
      } else if (this.definitionList != null) {
         return this.definitionList[reg];
      } else {
         this.definitionList = new SsaInsn[this.getRegCount()];
         this.forEachInsn(new SsaInsn.Visitor() {
            public void visitMoveInsn(NormalSsaInsn insn) {
               SsaMethod.this.definitionList[insn.getResult().getReg()] = insn;
            }

            public void visitPhiInsn(PhiInsn phi) {
               SsaMethod.this.definitionList[phi.getResult().getReg()] = phi;
            }

            public void visitNonMoveInsn(NormalSsaInsn insn) {
               RegisterSpec result = insn.getResult();
               if (result != null) {
                  SsaMethod.this.definitionList[insn.getResult().getReg()] = insn;
               }

            }
         });
         return this.definitionList[reg];
      }
   }

   private void buildUseList() {
      if (this.backMode) {
         throw new RuntimeException("No use list in back mode");
      } else {
         this.useList = new ArrayList[this.registerCount];

         int i;
         for(i = 0; i < this.registerCount; ++i) {
            this.useList[i] = new ArrayList();
         }

         this.forEachInsn(new SsaInsn.Visitor() {
            public void visitMoveInsn(NormalSsaInsn insn) {
               this.addToUses(insn);
            }

            public void visitPhiInsn(PhiInsn phi) {
               this.addToUses(phi);
            }

            public void visitNonMoveInsn(NormalSsaInsn insn) {
               this.addToUses(insn);
            }

            private void addToUses(SsaInsn insn) {
               RegisterSpecList rl = insn.getSources();
               int sz = rl.size();

               for(int i = 0; i < sz; ++i) {
                  SsaMethod.this.useList[rl.get(i).getReg()].add(insn);
               }

            }
         });
         this.unmodifiableUseList = new List[this.registerCount];

         for(i = 0; i < this.registerCount; ++i) {
            this.unmodifiableUseList[i] = Collections.unmodifiableList(this.useList[i]);
         }

      }
   }

   void onSourceChanged(SsaInsn insn, RegisterSpec oldSource, RegisterSpec newSource) {
      if (this.useList != null) {
         int reg;
         if (oldSource != null) {
            reg = oldSource.getReg();
            this.useList[reg].remove(insn);
         }

         reg = newSource.getReg();
         if (this.useList.length <= reg) {
            this.useList = null;
         } else {
            this.useList[reg].add(insn);
         }
      }
   }

   void onSourcesChanged(SsaInsn insn, RegisterSpecList oldSources) {
      if (this.useList != null) {
         if (oldSources != null) {
            this.removeFromUseList(insn, oldSources);
         }

         RegisterSpecList sources = insn.getSources();
         int szNew = sources.size();

         for(int i = 0; i < szNew; ++i) {
            int reg = sources.get(i).getReg();
            this.useList[reg].add(insn);
         }

      }
   }

   private void removeFromUseList(SsaInsn insn, RegisterSpecList oldSources) {
      if (oldSources != null) {
         int szNew = oldSources.size();

         for(int i = 0; i < szNew; ++i) {
            if (!this.useList[oldSources.get(i).getReg()].remove(insn)) {
               throw new RuntimeException("use not found");
            }
         }

      }
   }

   void onInsnAdded(SsaInsn insn) {
      this.onSourcesChanged(insn, (RegisterSpecList)null);
      this.updateOneDefinition(insn, (RegisterSpec)null);
   }

   void onInsnRemoved(SsaInsn insn) {
      if (this.useList != null) {
         this.removeFromUseList(insn, insn.getSources());
      }

      RegisterSpec resultReg = insn.getResult();
      if (this.definitionList != null && resultReg != null) {
         this.definitionList[resultReg.getReg()] = null;
      }

   }

   public void onInsnsChanged() {
      this.definitionList = null;
      this.useList = null;
      this.unmodifiableUseList = null;
   }

   void updateOneDefinition(SsaInsn insn, RegisterSpec oldResult) {
      if (this.definitionList != null) {
         if (oldResult != null) {
            int reg = oldResult.getReg();
            this.definitionList[reg] = null;
         }

         RegisterSpec resultReg = insn.getResult();
         if (resultReg != null) {
            int reg = resultReg.getReg();
            if (this.definitionList[reg] != null) {
               throw new RuntimeException("Duplicate add of insn");
            }

            this.definitionList[resultReg.getReg()] = insn;
         }

      }
   }

   public List<SsaInsn> getUseListForRegister(int reg) {
      if (this.unmodifiableUseList == null) {
         this.buildUseList();
      }

      return this.unmodifiableUseList[reg];
   }

   public ArrayList<SsaInsn>[] getUseListCopy() {
      if (this.useList == null) {
         this.buildUseList();
      }

      ArrayList<SsaInsn>[] useListCopy = new ArrayList[this.registerCount];

      for(int i = 0; i < this.registerCount; ++i) {
         useListCopy[i] = new ArrayList(this.useList[i]);
      }

      return useListCopy;
   }

   public boolean isRegALocal(RegisterSpec spec) {
      SsaInsn defn = this.getDefinitionForRegister(spec.getReg());
      if (defn == null) {
         return false;
      } else if (defn.getLocalAssignment() != null) {
         return true;
      } else {
         Iterator var3 = this.getUseListForRegister(spec.getReg()).iterator();

         Insn insn;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            SsaInsn use = (SsaInsn)var3.next();
            insn = use.getOriginalRopInsn();
         } while(insn == null || insn.getOpcode().getOpcode() != 54);

         return true;
      }
   }

   void setNewRegCount(int newRegCount) {
      this.registerCount = newRegCount;
      this.spareRegisterBase = this.registerCount;
      this.onInsnsChanged();
   }

   public int makeNewSsaReg() {
      int reg = this.registerCount++;
      this.spareRegisterBase = this.registerCount;
      this.onInsnsChanged();
      return reg;
   }

   public void forEachInsn(SsaInsn.Visitor visitor) {
      Iterator var2 = this.blocks.iterator();

      while(var2.hasNext()) {
         SsaBasicBlock block = (SsaBasicBlock)var2.next();
         block.forEachInsn(visitor);
      }

   }

   public void forEachPhiInsn(PhiInsn.Visitor v) {
      Iterator var2 = this.blocks.iterator();

      while(var2.hasNext()) {
         SsaBasicBlock block = (SsaBasicBlock)var2.next();
         block.forEachPhiInsn(v);
      }

   }

   public void forEachBlockDepthFirst(boolean reverse, SsaBasicBlock.Visitor v) {
      BitSet visited = new BitSet(this.blocks.size());
      Stack<SsaBasicBlock> stack = new Stack();
      SsaBasicBlock rootBlock = reverse ? this.getExitBlock() : this.getEntryBlock();
      if (rootBlock != null) {
         stack.add((Object)null);
         stack.add(rootBlock);

         while(true) {
            SsaBasicBlock cur;
            SsaBasicBlock parent;
            do {
               if (stack.size() <= 0) {
                  return;
               }

               cur = (SsaBasicBlock)stack.pop();
               parent = (SsaBasicBlock)stack.pop();
            } while(visited.get(cur.getIndex()));

            BitSet children = reverse ? cur.getPredecessors() : cur.getSuccessors();

            for(int i = children.nextSetBit(0); i >= 0; i = children.nextSetBit(i + 1)) {
               stack.add(cur);
               stack.add((SsaBasicBlock)this.blocks.get(i));
            }

            visited.set(cur.getIndex());
            v.visitBlock(cur, parent);
         }
      }
   }

   public void forEachBlockDepthFirstDom(SsaBasicBlock.Visitor v) {
      BitSet visited = new BitSet(this.getBlocks().size());
      Stack<SsaBasicBlock> stack = new Stack();
      stack.add(this.getEntryBlock());

      while(true) {
         SsaBasicBlock cur;
         ArrayList curDomChildren;
         do {
            if (stack.size() <= 0) {
               return;
            }

            cur = (SsaBasicBlock)stack.pop();
            curDomChildren = cur.getDomChildren();
         } while(visited.get(cur.getIndex()));

         for(int i = curDomChildren.size() - 1; i >= 0; --i) {
            SsaBasicBlock child = (SsaBasicBlock)curDomChildren.get(i);
            stack.add(child);
         }

         visited.set(cur.getIndex());
         v.visitBlock(cur, (SsaBasicBlock)null);
      }
   }

   public void deleteInsns(Set<SsaInsn> deletedInsns) {
      Iterator var2 = deletedInsns.iterator();

      while(true) {
         SsaBasicBlock block;
         ArrayList insns;
         int insnsSz;
         SsaInsn lastInsn;
         do {
            do {
               if (!var2.hasNext()) {
                  return;
               }

               SsaInsn deletedInsn = (SsaInsn)var2.next();
               block = deletedInsn.getBlock();
               insns = block.getInsns();

               for(insnsSz = insns.size() - 1; insnsSz >= 0; --insnsSz) {
                  lastInsn = (SsaInsn)insns.get(insnsSz);
                  if (deletedInsn == lastInsn) {
                     this.onInsnRemoved(lastInsn);
                     insns.remove(insnsSz);
                     break;
                  }
               }

               insnsSz = insns.size();
               lastInsn = insnsSz == 0 ? null : (SsaInsn)insns.get(insnsSz - 1);
            } while(block == this.getExitBlock());
         } while(insnsSz != 0 && lastInsn.getOriginalRopInsn() != null && lastInsn.getOriginalRopInsn().getOpcode().getBranchingness() != 1);

         Insn gotoInsn = new PlainInsn(Rops.GOTO, SourcePosition.NO_INFO, (RegisterSpec)null, RegisterSpecList.EMPTY);
         insns.add(SsaInsn.makeFromRop(gotoInsn, block));
         BitSet succs = block.getSuccessors();

         for(int i = succs.nextSetBit(0); i >= 0; i = succs.nextSetBit(i + 1)) {
            if (i != block.getPrimarySuccessorIndex()) {
               block.removeSuccessor(i);
            }
         }
      }
   }

   public void setBackMode() {
      this.backMode = true;
      this.useList = null;
      this.definitionList = null;
   }
}
