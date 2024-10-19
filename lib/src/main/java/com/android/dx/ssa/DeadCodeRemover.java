package com.android.dx.ssa;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;

public class DeadCodeRemover {
   private final SsaMethod ssaMeth;
   private final int regCount;
   private final BitSet worklist;
   private final ArrayList<SsaInsn>[] useList;

   public static void process(SsaMethod ssaMethod) {
      DeadCodeRemover dc = new DeadCodeRemover(ssaMethod);
      dc.run();
   }

   private DeadCodeRemover(SsaMethod ssaMethod) {
      this.ssaMeth = ssaMethod;
      this.regCount = ssaMethod.getRegCount();
      this.worklist = new BitSet(this.regCount);
      this.useList = this.ssaMeth.getUseListCopy();
   }

   private void run() {
      this.pruneDeadInstructions();
      HashSet<SsaInsn> deletedInsns = new HashSet();
      this.ssaMeth.forEachInsn(new NoSideEffectVisitor(this.worklist));

      while(true) {
         SsaInsn insnS;
         do {
            int regV;
            do {
               if (0 > (regV = this.worklist.nextSetBit(0))) {
                  this.ssaMeth.deleteInsns(deletedInsns);
                  return;
               }

               this.worklist.clear(regV);
            } while(this.useList[regV].size() != 0 && !this.isCircularNoSideEffect(regV, (BitSet)null));

            insnS = this.ssaMeth.getDefinitionForRegister(regV);
         } while(deletedInsns.contains(insnS));

         RegisterSpecList sources = insnS.getSources();
         int sz = sources.size();

         for(int i = 0; i < sz; ++i) {
            RegisterSpec source = sources.get(i);
            this.useList[source.getReg()].remove(insnS);
            if (!hasSideEffect(this.ssaMeth.getDefinitionForRegister(source.getReg()))) {
               this.worklist.set(source.getReg());
            }
         }

         deletedInsns.add(insnS);
      }
   }

   private void pruneDeadInstructions() {
      HashSet<SsaInsn> deletedInsns = new HashSet();
      BitSet reachable = this.ssaMeth.computeReachability();
      ArrayList<SsaBasicBlock> blocks = this.ssaMeth.getBlocks();
      int blockIndex = 0;

      while((blockIndex = reachable.nextClearBit(blockIndex)) < blocks.size()) {
         SsaBasicBlock block = (SsaBasicBlock)blocks.get(blockIndex);
         ++blockIndex;

         for(int i = 0; i < block.getInsns().size(); ++i) {
            SsaInsn insn = (SsaInsn)block.getInsns().get(i);
            RegisterSpecList sources = insn.getSources();
            int sourcesSize = sources.size();
            if (sourcesSize != 0) {
               deletedInsns.add(insn);
            }

            for(int j = 0; j < sourcesSize; ++j) {
               RegisterSpec source = sources.get(j);
               this.useList[source.getReg()].remove(insn);
            }

            RegisterSpec result = insn.getResult();
            if (result != null) {
               Iterator var15 = this.useList[result.getReg()].iterator();

               while(var15.hasNext()) {
                  SsaInsn use = (SsaInsn)var15.next();
                  if (use instanceof PhiInsn) {
                     PhiInsn phiUse = (PhiInsn)use;
                     phiUse.removePhiRegister(result);
                  }
               }
            }
         }
      }

      this.ssaMeth.deleteInsns(deletedInsns);
   }

   private boolean isCircularNoSideEffect(int regV, BitSet set) {
      if (set != null && set.get(regV)) {
         return true;
      } else {
         Iterator var3 = this.useList[regV].iterator();

         SsaInsn use;
         while(var3.hasNext()) {
            use = (SsaInsn)var3.next();
            if (hasSideEffect(use)) {
               return false;
            }
         }

         if (set == null) {
            set = new BitSet(this.regCount);
         }

         set.set(regV);
         var3 = this.useList[regV].iterator();

         RegisterSpec result;
         do {
            if (!var3.hasNext()) {
               return true;
            }

            use = (SsaInsn)var3.next();
            result = use.getResult();
         } while(result != null && this.isCircularNoSideEffect(result.getReg(), set));

         return false;
      }
   }

   private static boolean hasSideEffect(SsaInsn insn) {
      return insn == null ? true : insn.hasSideEffect();
   }

   private static class NoSideEffectVisitor implements SsaInsn.Visitor {
      BitSet noSideEffectRegs;

      public NoSideEffectVisitor(BitSet noSideEffectRegs) {
         this.noSideEffectRegs = noSideEffectRegs;
      }

      public void visitMoveInsn(NormalSsaInsn insn) {
         if (!DeadCodeRemover.hasSideEffect(insn)) {
            this.noSideEffectRegs.set(insn.getResult().getReg());
         }

      }

      public void visitPhiInsn(PhiInsn phi) {
         if (!DeadCodeRemover.hasSideEffect(phi)) {
            this.noSideEffectRegs.set(phi.getResult().getReg());
         }

      }

      public void visitNonMoveInsn(NormalSsaInsn insn) {
         RegisterSpec result = insn.getResult();
         if (!DeadCodeRemover.hasSideEffect(insn) && result != null) {
            this.noSideEffectRegs.set(result.getReg());
         }

      }
   }
}
