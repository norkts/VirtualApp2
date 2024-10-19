package com.android.dx.ssa.back;

import com.android.dx.rop.code.CstInsn;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.ssa.BasicRegisterMapper;
import com.android.dx.ssa.NormalSsaInsn;
import com.android.dx.ssa.RegisterMapper;
import com.android.dx.ssa.SsaMethod;
import com.android.dx.util.BitIntSet;
import com.android.dx.util.IntSet;
import java.util.BitSet;

public class FirstFitAllocator extends RegisterAllocator {
   private static final boolean PRESLOT_PARAMS = true;
   private final BitSet mapped;

   public FirstFitAllocator(SsaMethod ssaMeth, InterferenceGraph interference) {
      super(ssaMeth, interference);
      this.mapped = new BitSet(ssaMeth.getRegCount());
   }

   public boolean wantsParamsMovedHigh() {
      return true;
   }

   public RegisterMapper allocateRegisters() {
      int oldRegCount = this.ssaMeth.getRegCount();
      BasicRegisterMapper mapper = new BasicRegisterMapper(oldRegCount);
      int nextNewRegister = this.ssaMeth.getParamWidth();

      for(int i = 0; i < oldRegCount; ++i) {
         if (!this.mapped.get(i)) {
            int maxCategory = this.getCategoryForSsaReg(i);
            IntSet current = new BitIntSet(oldRegCount);
            this.interference.mergeInterferenceSet(i, current);
            boolean isPreslotted = false;
            int newReg;
            if (this.isDefinitionMoveParam(i)) {
               NormalSsaInsn defInsn = (NormalSsaInsn)this.ssaMeth.getDefinitionForRegister(i);
               newReg = this.paramNumberFromMoveParam(defInsn);
               mapper.addMapping(i, newReg, maxCategory);
               isPreslotted = true;
            } else {
               mapper.addMapping(i, nextNewRegister, maxCategory);
               newReg = nextNewRegister;
            }

            for(int j = i + 1; j < oldRegCount; ++j) {
               if (!this.mapped.get(j) && !this.isDefinitionMoveParam(j) && !current.has(j) && (!isPreslotted || maxCategory >= this.getCategoryForSsaReg(j))) {
                  this.interference.mergeInterferenceSet(j, current);
                  maxCategory = Math.max(maxCategory, this.getCategoryForSsaReg(j));
                  mapper.addMapping(j, newReg, maxCategory);
                  this.mapped.set(j);
               }
            }

            this.mapped.set(i);
            if (!isPreslotted) {
               nextNewRegister += maxCategory;
            }
         }
      }

      return mapper;
   }

   private int paramNumberFromMoveParam(NormalSsaInsn ndefInsn) {
      CstInsn origInsn = (CstInsn)ndefInsn.getOriginalRopInsn();
      return ((CstInteger)origInsn.getConstant()).getValue();
   }
}
