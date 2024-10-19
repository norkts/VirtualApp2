package com.android.dx.ssa;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.util.IntIterator;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;

public class SsaConverter {
   public static final boolean DEBUG = false;

   public static SsaMethod convertToSsaMethod(RopMethod rmeth, int paramWidth, boolean isStatic) {
      SsaMethod result = SsaMethod.newFromRopMethod(rmeth, paramWidth, isStatic);
      edgeSplit(result);
      LocalVariableInfo localInfo = LocalVariableExtractor.extract(result);
      placePhiFunctions(result, localInfo, 0);
      (new SsaRenamer(result)).run();
      result.makeExitBlock();
      return result;
   }

   public static void updateSsaMethod(SsaMethod ssaMeth, int threshold) {
      LocalVariableInfo localInfo = LocalVariableExtractor.extract(ssaMeth);
      placePhiFunctions(ssaMeth, localInfo, threshold);
      (new SsaRenamer(ssaMeth, threshold)).run();
   }

   public static SsaMethod testEdgeSplit(RopMethod rmeth, int paramWidth, boolean isStatic) {
      SsaMethod result = SsaMethod.newFromRopMethod(rmeth, paramWidth, isStatic);
      edgeSplit(result);
      return result;
   }

   public static SsaMethod testPhiPlacement(RopMethod rmeth, int paramWidth, boolean isStatic) {
      SsaMethod result = SsaMethod.newFromRopMethod(rmeth, paramWidth, isStatic);
      edgeSplit(result);
      LocalVariableInfo localInfo = LocalVariableExtractor.extract(result);
      placePhiFunctions(result, localInfo, 0);
      return result;
   }

   private static void edgeSplit(SsaMethod result) {
      edgeSplitPredecessors(result);
      edgeSplitMoveExceptionsAndResults(result);
      edgeSplitSuccessors(result);
   }

   private static void edgeSplitPredecessors(SsaMethod result) {
      ArrayList<SsaBasicBlock> blocks = result.getBlocks();

      for(int i = blocks.size() - 1; i >= 0; --i) {
         SsaBasicBlock block = (SsaBasicBlock)blocks.get(i);
         if (nodeNeedsUniquePredecessor(block)) {
            block.insertNewPredecessor();
         }
      }

   }

   private static boolean nodeNeedsUniquePredecessor(SsaBasicBlock block) {
      int countPredecessors = block.getPredecessors().cardinality();
      int countSuccessors = block.getSuccessors().cardinality();
      return countPredecessors > 1 && countSuccessors > 1;
   }

   private static void edgeSplitMoveExceptionsAndResults(SsaMethod ssaMeth) {
      ArrayList<SsaBasicBlock> blocks = ssaMeth.getBlocks();

      for(int i = blocks.size() - 1; i >= 0; --i) {
         SsaBasicBlock block = (SsaBasicBlock)blocks.get(i);
         if (!block.isExitBlock() && block.getPredecessors().cardinality() > 1 && ((SsaInsn)block.getInsns().get(0)).isMoveException()) {
            BitSet preds = (BitSet)block.getPredecessors().clone();

            for(int j = preds.nextSetBit(0); j >= 0; j = preds.nextSetBit(j + 1)) {
               SsaBasicBlock predecessor = (SsaBasicBlock)blocks.get(j);
               SsaBasicBlock zNode = predecessor.insertNewSuccessor(block);
               zNode.getInsns().add(0, ((SsaInsn)block.getInsns().get(0)).clone());
            }

            block.getInsns().remove(0);
         }
      }

   }

   private static void edgeSplitSuccessors(SsaMethod result) {
      ArrayList<SsaBasicBlock> blocks = result.getBlocks();

      for(int i = blocks.size() - 1; i >= 0; --i) {
         SsaBasicBlock block = (SsaBasicBlock)blocks.get(i);
         BitSet successors = (BitSet)block.getSuccessors().clone();

         for(int j = successors.nextSetBit(0); j >= 0; j = successors.nextSetBit(j + 1)) {
            SsaBasicBlock succ = (SsaBasicBlock)blocks.get(j);
            if (needsNewSuccessor(block, succ)) {
               block.insertNewSuccessor(succ);
            }
         }
      }

   }

   private static boolean needsNewSuccessor(SsaBasicBlock block, SsaBasicBlock succ) {
      ArrayList<SsaInsn> insns = block.getInsns();
      SsaInsn lastInsn = (SsaInsn)insns.get(insns.size() - 1);
      if (block.getSuccessors().cardinality() > 1 && succ.getPredecessors().cardinality() > 1) {
         return true;
      } else {
         return (lastInsn.getResult() != null || lastInsn.getSources().size() > 0) && succ.getPredecessors().cardinality() > 1;
      }
   }

   private static void placePhiFunctions(SsaMethod ssaMeth, LocalVariableInfo localInfo, int threshold) {
      ArrayList<SsaBasicBlock> ssaBlocks = ssaMeth.getBlocks();
      int blockCount = ssaBlocks.size();
      int regCount = ssaMeth.getRegCount() - threshold;
      DomFront df = new DomFront(ssaMeth);
      DomFront.DomInfo[] domInfos = df.run();
      BitSet[] defsites = new BitSet[regCount];
      BitSet[] phisites = new BitSet[regCount];

      int bi;
      for(bi = 0; bi < regCount; ++bi) {
         defsites[bi] = new BitSet(blockCount);
         phisites[bi] = new BitSet(blockCount);
      }

      bi = 0;

      int reg;
      for(reg = ssaBlocks.size(); bi < reg; ++bi) {
         SsaBasicBlock b = (SsaBasicBlock)ssaBlocks.get(bi);
         Iterator var13 = b.getInsns().iterator();

         while(var13.hasNext()) {
            SsaInsn insn = (SsaInsn)var13.next();
            RegisterSpec rs = insn.getResult();
            if (rs != null && rs.getReg() - threshold >= 0) {
               defsites[rs.getReg() - threshold].set(bi);
            }
         }
      }

      reg = 0;

      for(int s = regCount; reg < s; ++reg) {
         BitSet worklist = (BitSet)defsites[reg].clone();

         int workBlockIndex;
         while(0 <= (workBlockIndex = worklist.nextSetBit(0))) {
            worklist.clear(workBlockIndex);
            IntIterator dfIterator = domInfos[workBlockIndex].dominanceFrontiers.iterator();

            while(dfIterator.hasNext()) {
               int dfBlockIndex = dfIterator.next();
               if (!phisites[reg].get(dfBlockIndex)) {
                  phisites[reg].set(dfBlockIndex);
                  int tReg = reg + threshold;
                  RegisterSpec rs = localInfo.getStarts(dfBlockIndex).get(tReg);
                  if (rs == null) {
                     ((SsaBasicBlock)ssaBlocks.get(dfBlockIndex)).addPhiInsnForReg(tReg);
                  } else {
                     ((SsaBasicBlock)ssaBlocks.get(dfBlockIndex)).addPhiInsnForReg(rs);
                  }

                  if (!defsites[reg].get(dfBlockIndex)) {
                     worklist.set(dfBlockIndex);
                  }
               }
            }
         }
      }

   }
}
