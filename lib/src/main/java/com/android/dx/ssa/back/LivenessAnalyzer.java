package com.android.dx.ssa.back;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.ssa.PhiInsn;
import com.android.dx.ssa.SsaBasicBlock;
import com.android.dx.ssa.SsaInsn;
import com.android.dx.ssa.SsaMethod;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

public class LivenessAnalyzer {
   private final BitSet visitedBlocks;
   private final BitSet liveOutBlocks;
   private final int regV;
   private final SsaMethod ssaMeth;
   private final InterferenceGraph interference;
   private SsaBasicBlock blockN;
   private int statementIndex;
   private NextFunction nextFunction;

   public static InterferenceGraph constructInterferenceGraph(SsaMethod ssaMeth) {
      int szRegs = ssaMeth.getRegCount();
      InterferenceGraph interference = new InterferenceGraph(szRegs);

      for(int i = 0; i < szRegs; ++i) {
         (new LivenessAnalyzer(ssaMeth, i, interference)).run();
      }

      coInterferePhis(ssaMeth, interference);
      return interference;
   }

   private LivenessAnalyzer(SsaMethod ssaMeth, int reg, InterferenceGraph interference) {
      int blocksSz = ssaMeth.getBlocks().size();
      this.ssaMeth = ssaMeth;
      this.regV = reg;
      this.visitedBlocks = new BitSet(blocksSz);
      this.liveOutBlocks = new BitSet(blocksSz);
      this.interference = interference;
   }

   private void handleTailRecursion() {
      while(this.nextFunction != LivenessAnalyzer.NextFunction.DONE) {
         switch (this.nextFunction) {
            case LIVE_IN_AT_STATEMENT:
               this.nextFunction = LivenessAnalyzer.NextFunction.DONE;
               this.liveInAtStatement();
               break;
            case LIVE_OUT_AT_STATEMENT:
               this.nextFunction = LivenessAnalyzer.NextFunction.DONE;
               this.liveOutAtStatement();
               break;
            case LIVE_OUT_AT_BLOCK:
               this.nextFunction = LivenessAnalyzer.NextFunction.DONE;
               this.liveOutAtBlock();
         }
      }

   }

   public void run() {
      List<SsaInsn> useList = this.ssaMeth.getUseListForRegister(this.regV);
      Iterator var2 = useList.iterator();

      while(true) {
         while(var2.hasNext()) {
            SsaInsn insn = (SsaInsn)var2.next();
            this.nextFunction = LivenessAnalyzer.NextFunction.DONE;
            if (insn instanceof PhiInsn) {
               PhiInsn phi = (PhiInsn)insn;
               Iterator var5 = phi.predBlocksForReg(this.regV, this.ssaMeth).iterator();

               while(var5.hasNext()) {
                  SsaBasicBlock pred = (SsaBasicBlock)var5.next();
                  this.blockN = pred;
                  this.nextFunction = LivenessAnalyzer.NextFunction.LIVE_OUT_AT_BLOCK;
                  this.handleTailRecursion();
               }
            } else {
               this.blockN = insn.getBlock();
               this.statementIndex = this.blockN.getInsns().indexOf(insn);
               if (this.statementIndex < 0) {
                  throw new RuntimeException("insn not found in it's own block");
               }

               this.nextFunction = LivenessAnalyzer.NextFunction.LIVE_IN_AT_STATEMENT;
               this.handleTailRecursion();
            }
         }

         int nextLiveOutBlock;
         while((nextLiveOutBlock = this.liveOutBlocks.nextSetBit(0)) >= 0) {
            this.blockN = (SsaBasicBlock)this.ssaMeth.getBlocks().get(nextLiveOutBlock);
            this.liveOutBlocks.clear(nextLiveOutBlock);
            this.nextFunction = LivenessAnalyzer.NextFunction.LIVE_OUT_AT_BLOCK;
            this.handleTailRecursion();
         }

         return;
      }
   }

   private void liveOutAtBlock() {
      if (!this.visitedBlocks.get(this.blockN.getIndex())) {
         this.visitedBlocks.set(this.blockN.getIndex());
         this.blockN.addLiveOut(this.regV);
         ArrayList<SsaInsn> insns = this.blockN.getInsns();
         this.statementIndex = insns.size() - 1;
         this.nextFunction = LivenessAnalyzer.NextFunction.LIVE_OUT_AT_STATEMENT;
      }

   }

   private void liveInAtStatement() {
      if (this.statementIndex == 0) {
         this.blockN.addLiveIn(this.regV);
         BitSet preds = this.blockN.getPredecessors();
         this.liveOutBlocks.or(preds);
      } else {
         --this.statementIndex;
         this.nextFunction = LivenessAnalyzer.NextFunction.LIVE_OUT_AT_STATEMENT;
      }

   }

   private void liveOutAtStatement() {
      SsaInsn statement = (SsaInsn)this.blockN.getInsns().get(this.statementIndex);
      RegisterSpec rs = statement.getResult();
      if (!statement.isResultReg(this.regV)) {
         if (rs != null) {
            this.interference.add(this.regV, rs.getReg());
         }

         this.nextFunction = LivenessAnalyzer.NextFunction.LIVE_IN_AT_STATEMENT;
      }

   }

   private static void coInterferePhis(SsaMethod ssaMeth, InterferenceGraph interference) {
      Iterator var2 = ssaMeth.getBlocks().iterator();

      while(var2.hasNext()) {
         SsaBasicBlock b = (SsaBasicBlock)var2.next();
         List<SsaInsn> phis = b.getPhiInsns();
         int szPhis = phis.size();

         for(int i = 0; i < szPhis; ++i) {
            for(int j = 0; j < szPhis; ++j) {
               if (i != j) {
                  SsaInsn first = (SsaInsn)phis.get(i);
                  SsaInsn second = (SsaInsn)phis.get(j);
                  coInterferePhiRegisters(interference, first.getResult(), second.getSources());
                  coInterferePhiRegisters(interference, second.getResult(), first.getSources());
                  interference.add(first.getResult().getReg(), second.getResult().getReg());
               }
            }
         }
      }

   }

   private static void coInterferePhiRegisters(InterferenceGraph interference, RegisterSpec result, RegisterSpecList sources) {
      int resultReg = result.getReg();

      for(int i = 0; i < sources.size(); ++i) {
         interference.add(resultReg, sources.get(i).getReg());
      }

   }

   private static enum NextFunction {
      LIVE_IN_AT_STATEMENT,
      LIVE_OUT_AT_STATEMENT,
      LIVE_OUT_AT_BLOCK,
      DONE;
   }
}
