package com.android.dx.ssa;

import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.PlainCstInsn;
import com.android.dx.rop.code.PlainInsn;
import com.android.dx.rop.code.RegOps;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.code.TranslationAdvice;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstLiteralBits;
import com.android.dx.rop.type.TypeBearer;
import java.util.ArrayList;
import java.util.List;

public class LiteralOpUpgrader {
   private final SsaMethod ssaMeth;

   public static void process(SsaMethod ssaMethod) {
      LiteralOpUpgrader dc = new LiteralOpUpgrader(ssaMethod);
      dc.run();
   }

   private LiteralOpUpgrader(SsaMethod ssaMethod) {
      this.ssaMeth = ssaMethod;
   }

   private static boolean isConstIntZeroOrKnownNull(RegisterSpec spec) {
      TypeBearer tb = spec.getTypeBearer();
      if (tb instanceof CstLiteralBits) {
         CstLiteralBits clb = (CstLiteralBits)tb;
         return clb.getLongBits() == 0L;
      } else {
         return false;
      }
   }

   private void run() {
      final TranslationAdvice advice = Optimizer.getAdvice();
      this.ssaMeth.forEachInsn(new SsaInsn.Visitor() {
         public void visitMoveInsn(NormalSsaInsn insn) {
         }

         public void visitPhiInsn(PhiInsn insn) {
         }

         public void visitNonMoveInsn(NormalSsaInsn insn) {
            Insn originalRopInsn = insn.getOriginalRopInsn();
            Rop opcode = originalRopInsn.getOpcode();
            RegisterSpecList sources = insn.getSources();
            if (!LiteralOpUpgrader.this.tryReplacingWithConstant(insn)) {
               if (sources.size() == 2) {
                  if (opcode.getBranchingness() == 4) {
                     if (LiteralOpUpgrader.isConstIntZeroOrKnownNull(sources.get(0))) {
                        LiteralOpUpgrader.this.replacePlainInsn(insn, sources.withoutFirst(), RegOps.flippedIfOpcode(opcode.getOpcode()), (Constant)null);
                     } else if (LiteralOpUpgrader.isConstIntZeroOrKnownNull(sources.get(1))) {
                        LiteralOpUpgrader.this.replacePlainInsn(insn, sources.withoutLast(), opcode.getOpcode(), (Constant)null);
                     }
                  } else if (advice.hasConstantOperation(opcode, sources.get(0), sources.get(1))) {
                     insn.upgradeToLiteral();
                  } else if (opcode.isCommutative() && advice.hasConstantOperation(opcode, sources.get(1), sources.get(0))) {
                     insn.setNewSources(RegisterSpecList.make(sources.get(1), sources.get(0)));
                     insn.upgradeToLiteral();
                  }

               }
            }
         }
      });
   }

   private boolean tryReplacingWithConstant(NormalSsaInsn insn) {
      Insn originalRopInsn = insn.getOriginalRopInsn();
      Rop opcode = originalRopInsn.getOpcode();
      RegisterSpec result = insn.getResult();
      if (result != null && !this.ssaMeth.isRegALocal(result) && opcode.getOpcode() != 5) {
         TypeBearer type = insn.getResult().getTypeBearer();
         if (type.isConstant() && type.getBasicType() == 6) {
            this.replacePlainInsn(insn, RegisterSpecList.EMPTY, 5, (Constant)type);
            if (opcode.getOpcode() == 56) {
               int pred = insn.getBlock().getPredecessors().nextSetBit(0);
               ArrayList<SsaInsn> predInsns = ((SsaBasicBlock)this.ssaMeth.getBlocks().get(pred)).getInsns();
               NormalSsaInsn sourceInsn = (NormalSsaInsn)predInsns.get(predInsns.size() - 1);
               this.replacePlainInsn(sourceInsn, RegisterSpecList.EMPTY, 6, (Constant)null);
            }

            return true;
         }
      }

      return false;
   }

   private void replacePlainInsn(NormalSsaInsn insn, RegisterSpecList newSources, int newOpcode, Constant cst) {
      Insn originalRopInsn = insn.getOriginalRopInsn();
      Rop newRop = Rops.ropFor(newOpcode, insn.getResult(), newSources, cst);
      Object newRopInsn;
      if (cst == null) {
         newRopInsn = new PlainInsn(newRop, originalRopInsn.getPosition(), insn.getResult(), newSources);
      } else {
         newRopInsn = new PlainCstInsn(newRop, originalRopInsn.getPosition(), insn.getResult(), newSources, cst);
      }

      NormalSsaInsn newInsn = new NormalSsaInsn((Insn)newRopInsn, insn.getBlock());
      List<SsaInsn> insns = insn.getBlock().getInsns();
      this.ssaMeth.onInsnRemoved(insn);
      insns.set(insns.lastIndexOf(insn), newInsn);
      this.ssaMeth.onInsnAdded(newInsn);
   }
}
