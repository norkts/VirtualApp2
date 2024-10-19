package com.android.dx.ssa;

import com.android.dx.rop.code.CstInsn;
import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.cst.CstInteger;
import java.util.HashSet;
import java.util.List;

public class MoveParamCombiner {
   private final SsaMethod ssaMeth;

   public static void process(SsaMethod ssaMethod) {
      (new MoveParamCombiner(ssaMethod)).run();
   }

   private MoveParamCombiner(SsaMethod ssaMeth) {
      this.ssaMeth = ssaMeth;
   }

   private void run() {
      final RegisterSpec[] paramSpecs = new RegisterSpec[this.ssaMeth.getParamWidth()];
      final HashSet<SsaInsn> deletedInsns = new HashSet();
      this.ssaMeth.forEachInsn(new SsaInsn.Visitor() {
         public void visitMoveInsn(NormalSsaInsn insn) {
         }

         public void visitPhiInsn(PhiInsn phi) {
         }

         public void visitNonMoveInsn(NormalSsaInsn insn) {
            if (insn.getOpcode().getOpcode() == 3) {
               int param = MoveParamCombiner.this.getParamIndex(insn);
               if (paramSpecs[param] == null) {
                  paramSpecs[param] = insn.getResult();
               } else {
                  final RegisterSpec specA = paramSpecs[param];
                  final RegisterSpec specB = insn.getResult();
                  LocalItem localA = specA.getLocalItem();
                  LocalItem localB = specB.getLocalItem();
                  LocalItem newLocal;
                  if (localA == null) {
                     newLocal = localB;
                  } else if (localB == null) {
                     newLocal = localA;
                  } else {
                     if (!localA.equals(localB)) {
                        return;
                     }

                     newLocal = localA;
                  }

                  MoveParamCombiner.this.ssaMeth.getDefinitionForRegister(specA.getReg()).setResultLocal(newLocal);
                  RegisterMapper mapper = new RegisterMapper() {
                     public int getNewRegisterCount() {
                        return MoveParamCombiner.this.ssaMeth.getRegCount();
                     }

                     public RegisterSpec map(RegisterSpec registerSpec) {
                        return registerSpec.getReg() == specB.getReg() ? specA : registerSpec;
                     }
                  };
                  List<SsaInsn> uses = MoveParamCombiner.this.ssaMeth.getUseListForRegister(specB.getReg());

                  for(int i = uses.size() - 1; i >= 0; --i) {
                     SsaInsn use = (SsaInsn)uses.get(i);
                     use.mapSourceRegisters(mapper);
                  }

                  deletedInsns.add(insn);
               }

            }
         }
      });
      this.ssaMeth.deleteInsns(deletedInsns);
   }

   private int getParamIndex(NormalSsaInsn insn) {
      CstInsn cstInsn = (CstInsn)insn.getOriginalRopInsn();
      int param = ((CstInteger)cstInsn.getConstant()).getValue();
      return param;
   }
}
