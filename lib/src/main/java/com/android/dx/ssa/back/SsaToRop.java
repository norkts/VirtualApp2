package com.android.dx.ssa.back;

import com.android.dx.rop.code.BasicBlock;
import com.android.dx.rop.code.BasicBlockList;
import com.android.dx.rop.code.InsnList;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.rop.code.Rops;
import com.android.dx.ssa.BasicRegisterMapper;
import com.android.dx.ssa.PhiInsn;
import com.android.dx.ssa.RegisterMapper;
import com.android.dx.ssa.SsaBasicBlock;
import com.android.dx.ssa.SsaInsn;
import com.android.dx.ssa.SsaMethod;
import com.android.dx.util.Hex;
import com.android.dx.util.IntList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.Iterator;

public class SsaToRop {
   private static final boolean DEBUG = false;
   private final SsaMethod ssaMeth;
   private final boolean minimizeRegisters;
   private final InterferenceGraph interference;

   public static RopMethod convertToRopMethod(SsaMethod ssaMeth, boolean minimizeRegisters) {
      return (new SsaToRop(ssaMeth, minimizeRegisters)).convert();
   }

   private SsaToRop(SsaMethod ssaMethod, boolean minimizeRegisters) {
      this.minimizeRegisters = minimizeRegisters;
      this.ssaMeth = ssaMethod;
      this.interference = LivenessAnalyzer.constructInterferenceGraph(ssaMethod);
   }

   private RopMethod convert() {
      RegisterAllocator allocator = new FirstFitLocalCombiningAllocator(this.ssaMeth, this.interference, this.minimizeRegisters);
      RegisterMapper mapper = ((RegisterAllocator)allocator).allocateRegisters();
      this.ssaMeth.setBackMode();
      this.ssaMeth.mapRegisters(mapper);
      this.removePhiFunctions();
      if (((RegisterAllocator)allocator).wantsParamsMovedHigh()) {
         this.moveParametersToHighRegisters();
      }

      this.removeEmptyGotos();
      RopMethod ropMethod = new RopMethod(this.convertBasicBlocks(), this.ssaMeth.blockIndexToRopLabel(this.ssaMeth.getEntryBlockIndex()));
      ropMethod = (new IdenticalBlockCombiner(ropMethod)).process();
      return ropMethod;
   }

   private void removeEmptyGotos() {
      final ArrayList<SsaBasicBlock> blocks = this.ssaMeth.getBlocks();
      this.ssaMeth.forEachBlockDepthFirst(false, new SsaBasicBlock.Visitor() {
         public void visitBlock(SsaBasicBlock b, SsaBasicBlock parent) {
            ArrayList<SsaInsn> insns = b.getInsns();
            if (insns.size() == 1 && ((SsaInsn)insns.get(0)).getOpcode() == Rops.GOTO) {
               BitSet preds = (BitSet)b.getPredecessors().clone();

               for(int i = preds.nextSetBit(0); i >= 0; i = preds.nextSetBit(i + 1)) {
                  SsaBasicBlock pb = (SsaBasicBlock)blocks.get(i);
                  pb.replaceSuccessor(b.getIndex(), b.getPrimarySuccessorIndex());
               }
            }

         }
      });
   }

   private void removePhiFunctions() {
      ArrayList<SsaBasicBlock> blocks = this.ssaMeth.getBlocks();
      Iterator var2 = blocks.iterator();

      SsaBasicBlock block;
      while(var2.hasNext()) {
         block = (SsaBasicBlock)var2.next();
         block.forEachPhiInsn(new PhiVisitor(blocks));
         block.removeAllPhiInsns();
      }

      var2 = blocks.iterator();

      while(var2.hasNext()) {
         block = (SsaBasicBlock)var2.next();
         block.scheduleMovesFromPhis();
      }

   }

   private void moveParametersToHighRegisters() {
      int paramWidth = this.ssaMeth.getParamWidth();
      BasicRegisterMapper mapper = new BasicRegisterMapper(this.ssaMeth.getRegCount());
      int regCount = this.ssaMeth.getRegCount();

      for(int i = 0; i < regCount; ++i) {
         if (i < paramWidth) {
            mapper.addMapping(i, regCount - paramWidth + i, 1);
         } else {
            mapper.addMapping(i, i - paramWidth, 1);
         }
      }

      this.ssaMeth.mapRegisters(mapper);
   }

   private BasicBlockList convertBasicBlocks() {
      ArrayList<SsaBasicBlock> blocks = this.ssaMeth.getBlocks();
      SsaBasicBlock exitBlock = this.ssaMeth.getExitBlock();
      BitSet reachable = this.ssaMeth.computeReachability();
      int ropBlockCount = reachable.cardinality();
      if (exitBlock != null && reachable.get(exitBlock.getIndex())) {
         --ropBlockCount;
      }

      BasicBlockList result = new BasicBlockList(ropBlockCount);
      int ropBlockIndex = 0;
      Iterator var7 = blocks.iterator();

      while(var7.hasNext()) {
         SsaBasicBlock b = (SsaBasicBlock)var7.next();
         if (reachable.get(b.getIndex()) && b != exitBlock) {
            result.set(ropBlockIndex++, this.convertBasicBlock(b));
         }
      }

      if (exitBlock != null && !exitBlock.getInsns().isEmpty()) {
         throw new RuntimeException("Exit block must have no insns when leaving SSA form");
      } else {
         return result;
      }
   }

   private void verifyValidExitPredecessor(SsaBasicBlock b) {
      ArrayList<SsaInsn> insns = b.getInsns();
      SsaInsn lastInsn = (SsaInsn)insns.get(insns.size() - 1);
      Rop opcode = lastInsn.getOpcode();
      if (opcode.getBranchingness() != 2 && opcode != Rops.THROW) {
         throw new RuntimeException("Exit predecessor must end in valid exit statement.");
      }
   }

   private BasicBlock convertBasicBlock(SsaBasicBlock block) {
      IntList successorList = block.getRopLabelSuccessorList();
      int primarySuccessorLabel = block.getPrimarySuccessorRopLabel();
      SsaBasicBlock exitBlock = this.ssaMeth.getExitBlock();
      int exitRopLabel = exitBlock == null ? -1 : exitBlock.getRopLabel();
      if (successorList.contains(exitRopLabel)) {
         if (successorList.size() > 1) {
            throw new RuntimeException("Exit predecessor must have no other successors" + Hex.u2(block.getRopLabel()));
         }

         successorList = IntList.EMPTY;
         primarySuccessorLabel = -1;
         this.verifyValidExitPredecessor(block);
      }

      successorList.setImmutable();
      BasicBlock result = new BasicBlock(block.getRopLabel(), this.convertInsns(block.getInsns()), successorList, primarySuccessorLabel);
      return result;
   }

   private InsnList convertInsns(ArrayList<SsaInsn> ssaInsns) {
      int insnCount = ssaInsns.size();
      InsnList result = new InsnList(insnCount);

      for(int i = 0; i < insnCount; ++i) {
         result.set(i, ((SsaInsn)ssaInsns.get(i)).toRopInsn());
      }

      result.setImmutable();
      return result;
   }

   public int[] getRegistersByFrequency() {
      int regCount = this.ssaMeth.getRegCount();
      Integer[] ret = new Integer[regCount];

      for(int i = 0; i < regCount; ++i) {
         ret[i] = i;
      }

      Arrays.sort(ret, new Comparator<Integer>() {
         public int compare(Integer o1, Integer o2) {
            return SsaToRop.this.ssaMeth.getUseListForRegister(o2).size() - SsaToRop.this.ssaMeth.getUseListForRegister(o1).size();
         }
      });
      int[] result = new int[regCount];

      for(int i = 0; i < regCount; ++i) {
         result[i] = ret[i];
      }

      return result;
   }

   private static class PhiVisitor implements PhiInsn.Visitor {
      private final ArrayList<SsaBasicBlock> blocks;

      public PhiVisitor(ArrayList<SsaBasicBlock> blocks) {
         this.blocks = blocks;
      }

      public void visitPhiInsn(PhiInsn insn) {
         RegisterSpecList sources = insn.getSources();
         RegisterSpec result = insn.getResult();
         int sz = sources.size();

         for(int i = 0; i < sz; ++i) {
            RegisterSpec source = sources.get(i);
            SsaBasicBlock predBlock = (SsaBasicBlock)this.blocks.get(insn.predBlockIndexForSourcesIndex(i));
            predBlock.addMoveToEnd(result, source);
         }

      }
   }
}
