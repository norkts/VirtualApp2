package com.android.dx.ssa;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecSet;
import com.android.dx.util.IntList;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class LocalVariableExtractor {
   private final SsaMethod method;
   private final ArrayList<SsaBasicBlock> blocks;
   private final LocalVariableInfo resultInfo;
   private final BitSet workSet;

   public static LocalVariableInfo extract(SsaMethod method) {
      LocalVariableExtractor lve = new LocalVariableExtractor(method);
      return lve.doit();
   }

   private LocalVariableExtractor(SsaMethod method) {
      if (method == null) {
         throw new NullPointerException("method == null");
      } else {
         ArrayList<SsaBasicBlock> blocks = method.getBlocks();
         this.method = method;
         this.blocks = blocks;
         this.resultInfo = new LocalVariableInfo(method);
         this.workSet = new BitSet(blocks.size());
      }
   }

   private LocalVariableInfo doit() {
      if (this.method.getRegCount() > 0) {
         for(int bi = this.method.getEntryBlockIndex(); bi >= 0; bi = this.workSet.nextSetBit(0)) {
            this.workSet.clear(bi);
            this.processBlock(bi);
         }
      }

      this.resultInfo.setImmutable();
      return this.resultInfo;
   }

   private void processBlock(int blockIndex) {
      RegisterSpecSet primaryState = this.resultInfo.mutableCopyOfStarts(blockIndex);
      SsaBasicBlock block = (SsaBasicBlock)this.blocks.get(blockIndex);
      List<SsaInsn> insns = block.getInsns();
      int insnSz = insns.size();
      if (blockIndex != this.method.getExitBlockIndex()) {
         SsaInsn lastInsn = (SsaInsn)insns.get(insnSz - 1);
         boolean hasExceptionHandlers = lastInsn.getOriginalRopInsn().getCatches().size() != 0;
         boolean canThrowDuringLastInsn = hasExceptionHandlers && lastInsn.getResult() != null;
         int freezeSecondaryStateAt = insnSz - 1;
         RegisterSpecSet secondaryState = primaryState;

         for(int i = 0; i < insnSz; ++i) {
            if (canThrowDuringLastInsn && i == freezeSecondaryStateAt) {
               primaryState.setImmutable();
               primaryState = primaryState.mutableCopy();
            }

            SsaInsn insn = (SsaInsn)insns.get(i);
            RegisterSpec result = insn.getLocalAssignment();
            if (result == null) {
               result = insn.getResult();
               if (result != null && primaryState.get(result.getReg()) != null) {
                  primaryState.remove(primaryState.get(result.getReg()));
               }
            } else {
               result = result.withSimpleType();
               RegisterSpec already = primaryState.get(result);
               if (!result.equals(already)) {
                  RegisterSpec previous = primaryState.localItemToSpec(result.getLocalItem());
                  if (previous != null && previous.getReg() != result.getReg()) {
                     primaryState.remove(previous);
                  }

                  this.resultInfo.addAssignment(insn, result);
                  primaryState.put(result);
               }
            }
         }

         primaryState.setImmutable();
         IntList successors = block.getSuccessorList();
         int succSz = successors.size();
         int primarySuccessor = block.getPrimarySuccessorIndex();

         for(int i = 0; i < succSz; ++i) {
            int succ = successors.get(i);
            RegisterSpecSet state = succ == primarySuccessor ? primaryState : secondaryState;
            if (this.resultInfo.mergeStarts(succ, state)) {
               this.workSet.set(succ);
            }
         }

      }
   }
}
