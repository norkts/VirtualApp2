package com.android.dx.ssa.back;

import com.android.dx.rop.code.BasicBlock;
import com.android.dx.rop.code.BasicBlockList;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.util.IntList;
import java.util.BitSet;

public class IdenticalBlockCombiner {
   private final RopMethod ropMethod;
   private final BasicBlockList blocks;
   private final BasicBlockList newBlocks;

   public IdenticalBlockCombiner(RopMethod rm) {
      this.ropMethod = rm;
      this.blocks = this.ropMethod.getBlocks();
      this.newBlocks = this.blocks.getMutableCopy();
   }

   public RopMethod process() {
      int szBlocks = this.blocks.size();
      BitSet toDelete = new BitSet(this.blocks.getMaxLabel());

      int i;
      for(i = 0; i < szBlocks; ++i) {
         BasicBlock b = this.blocks.get(i);
         if (!toDelete.get(b.getLabel())) {
            IntList preds = this.ropMethod.labelToPredecessors(b.getLabel());
            int szPreds = preds.size();

            for(i = 0; i < szPreds; ++i) {
               int iLabel = preds.get(i);
               BasicBlock iBlock = this.blocks.labelToBlock(iLabel);
               if (!toDelete.get(iLabel) && iBlock.getSuccessors().size() <= 1 && iBlock.getFirstInsn().getOpcode().getOpcode() != 55) {
                  IntList toCombine = new IntList();

                  for(int j = i + 1; j < szPreds; ++j) {
                     int jLabel = preds.get(j);
                     BasicBlock jBlock = this.blocks.labelToBlock(jLabel);
                     if (jBlock.getSuccessors().size() == 1 && compareInsns(iBlock, jBlock)) {
                        toCombine.add(jLabel);
                        toDelete.set(jLabel);
                     }
                  }

                  this.combineBlocks(iLabel, toCombine);
               }
            }
         }
      }

      for(i = szBlocks - 1; i >= 0; --i) {
         if (toDelete.get(this.newBlocks.get(i).getLabel())) {
            this.newBlocks.set(i, (BasicBlock)null);
         }
      }

      this.newBlocks.shrinkToFit();
      this.newBlocks.setImmutable();
      return new RopMethod(this.newBlocks, this.ropMethod.getFirstLabel());
   }

   private static boolean compareInsns(BasicBlock a, BasicBlock b) {
      return a.getInsns().contentEquals(b.getInsns());
   }

   private void combineBlocks(int alphaLabel, IntList betaLabels) {
      int szBetas = betaLabels.size();

      for(int i = 0; i < szBetas; ++i) {
         int betaLabel = betaLabels.get(i);
         BasicBlock bb = this.blocks.labelToBlock(betaLabel);
         IntList preds = this.ropMethod.labelToPredecessors(bb.getLabel());
         int szPreds = preds.size();

         for(int j = 0; j < szPreds; ++j) {
            BasicBlock predBlock = this.newBlocks.labelToBlock(preds.get(j));
            this.replaceSucc(predBlock, betaLabel, alphaLabel);
         }
      }

   }

   private void replaceSucc(BasicBlock block, int oldLabel, int newLabel) {
      IntList newSuccessors = block.getSuccessors().mutableCopy();
      newSuccessors.set(newSuccessors.indexOf(oldLabel), newLabel);
      int newPrimarySuccessor = block.getPrimarySuccessor();
      if (newPrimarySuccessor == oldLabel) {
         newPrimarySuccessor = newLabel;
      }

      newSuccessors.setImmutable();
      BasicBlock newBB = new BasicBlock(block.getLabel(), block.getInsns(), newSuccessors, newPrimarySuccessor);
      this.newBlocks.set(this.newBlocks.indexOfLabel(block.getLabel()), newBB);
   }
}
