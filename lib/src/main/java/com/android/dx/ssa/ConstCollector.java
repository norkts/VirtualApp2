package com.android.dx.ssa;

import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.code.PlainCstInsn;
import com.android.dx.rop.code.PlainInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.code.ThrowingCstInsn;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.TypedConstant;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.TypeBearer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class ConstCollector {
   private static final int MAX_COLLECTED_CONSTANTS = 5;
   private static final boolean COLLECT_STRINGS = false;
   private static final boolean COLLECT_ONE_LOCAL = false;
   private final SsaMethod ssaMeth;

   public static void process(SsaMethod ssaMethod) {
      ConstCollector cc = new ConstCollector(ssaMethod);
      cc.run();
   }

   private ConstCollector(SsaMethod ssaMethod) {
      this.ssaMeth = ssaMethod;
   }

   private void run() {
      int regSz = this.ssaMeth.getRegCount();
      ArrayList<TypedConstant> constantList = this.getConstsSortedByCountUse();
      int toCollect = Math.min(constantList.size(), 5);
      SsaBasicBlock start = this.ssaMeth.getEntryBlock();
      HashMap<TypedConstant, RegisterSpec> newRegs = new HashMap(toCollect);

      for(int i = 0; i < toCollect; ++i) {
         TypedConstant cst = (TypedConstant)constantList.get(i);
         RegisterSpec result = RegisterSpec.make(this.ssaMeth.makeNewSsaReg(), cst);
         Rop constRop = Rops.opConst(cst);
         if (constRop.getBranchingness() == 1) {
            start.addInsnToHead(new PlainCstInsn(Rops.opConst(cst), SourcePosition.NO_INFO, result, RegisterSpecList.EMPTY, cst));
         } else {
            SsaBasicBlock entryBlock = this.ssaMeth.getEntryBlock();
            SsaBasicBlock successorBlock = entryBlock.getPrimarySuccessor();
            SsaBasicBlock constBlock = entryBlock.insertNewSuccessor(successorBlock);
            constBlock.replaceLastInsn(new ThrowingCstInsn(constRop, SourcePosition.NO_INFO, RegisterSpecList.EMPTY, StdTypeList.EMPTY, cst));
            SsaBasicBlock resultBlock = constBlock.insertNewSuccessor(successorBlock);
            PlainInsn insn = new PlainInsn(Rops.opMoveResultPseudo(result.getTypeBearer()), SourcePosition.NO_INFO, result, RegisterSpecList.EMPTY);
            resultBlock.addInsnToHead(insn);
         }

         newRegs.put(cst, result);
      }

      this.updateConstUses(newRegs, regSz);
   }

   private ArrayList<TypedConstant> getConstsSortedByCountUse() {
      int regSz = this.ssaMeth.getRegCount();
      final HashMap<TypedConstant, Integer> countUses = new HashMap();
      new HashSet();

      for(int i = 0; i < regSz; ++i) {
         SsaInsn insn = this.ssaMeth.getDefinitionForRegister(i);
         if (insn != null && insn.getOpcode() != null) {
            RegisterSpec result = insn.getResult();
            TypeBearer typeBearer = result.getTypeBearer();
            if (typeBearer.isConstant()) {
               TypedConstant cst = (TypedConstant)typeBearer;
               if (insn.getOpcode().getOpcode() == 56) {
                  int pred = insn.getBlock().getPredecessors().nextSetBit(0);
                  ArrayList<SsaInsn> predInsns = ((SsaBasicBlock)this.ssaMeth.getBlocks().get(pred)).getInsns();
                  insn = (SsaInsn)predInsns.get(predInsns.size() - 1);
               }

               if (insn.canThrow()) {
                  if (cst instanceof CstString) {
                  }
               } else if (!this.ssaMeth.isRegALocal(result)) {
                  Integer has = (Integer)countUses.get(cst);
                  if (has == null) {
                     countUses.put(cst, 1);
                  } else {
                     countUses.put(cst, has + 1);
                  }
               }
            }
         }
      }

      ArrayList<TypedConstant> constantList = new ArrayList();
      Iterator var12 = countUses.entrySet().iterator();

      while(var12.hasNext()) {
         Map.Entry<TypedConstant, Integer> entry = (Map.Entry)var12.next();
         if ((Integer)entry.getValue() > 1) {
            constantList.add((TypedConstant)entry.getKey());
         }
      }

      Collections.sort(constantList, new Comparator<Constant>() {
         public int compare(Constant a, Constant b) {
            int ret = (Integer)countUses.get(b) - (Integer)countUses.get(a);
            if (ret == 0) {
               ret = a.compareTo(b);
            }

            return ret;
         }

         public boolean equals(Object obj) {
            return obj == this;
         }
      });
      return constantList;
   }

   private void fixLocalAssignment(RegisterSpec origReg, RegisterSpec newReg) {
      Iterator var3 = this.ssaMeth.getUseListForRegister(origReg.getReg()).iterator();

      while(var3.hasNext()) {
         SsaInsn use = (SsaInsn)var3.next();
         RegisterSpec localAssignment = use.getLocalAssignment();
         if (localAssignment != null && use.getResult() != null) {
            LocalItem local = localAssignment.getLocalItem();
            use.setResultLocal((LocalItem)null);
            newReg = newReg.withLocalItem(local);
            SsaInsn newInsn = SsaInsn.makeFromRop(new PlainInsn(Rops.opMarkLocal(newReg), SourcePosition.NO_INFO, (RegisterSpec)null, RegisterSpecList.make(newReg)), use.getBlock());
            ArrayList<SsaInsn> insns = use.getBlock().getInsns();
            insns.add(insns.indexOf(use) + 1, newInsn);
         }
      }

   }

   private void updateConstUses(HashMap<TypedConstant, RegisterSpec> newRegs, int origRegCount) {
      new HashSet();
      ArrayList<SsaInsn>[] useList = this.ssaMeth.getUseListCopy();

      label44:
      for(int i = 0; i < origRegCount; ++i) {
         SsaInsn insn = this.ssaMeth.getDefinitionForRegister(i);
         if (insn != null) {
            final RegisterSpec origReg = insn.getResult();
            TypeBearer typeBearer = insn.getResult().getTypeBearer();
            if (typeBearer.isConstant()) {
               TypedConstant cst = (TypedConstant)typeBearer;
               final RegisterSpec newReg = (RegisterSpec)newRegs.get(cst);
               if (newReg != null && !this.ssaMeth.isRegALocal(origReg)) {
                  RegisterMapper mapper = new RegisterMapper() {
                     public int getNewRegisterCount() {
                        return ConstCollector.this.ssaMeth.getRegCount();
                     }

                     public RegisterSpec map(RegisterSpec registerSpec) {
                        return registerSpec.getReg() == origReg.getReg() ? newReg.withLocalItem(registerSpec.getLocalItem()) : registerSpec;
                     }
                  };
                  Iterator var12 = useList[origReg.getReg()].iterator();

                  while(true) {
                     SsaInsn use;
                     do {
                        if (!var12.hasNext()) {
                           continue label44;
                        }

                        use = (SsaInsn)var12.next();
                     } while(use.canThrow() && use.getBlock().getSuccessors().cardinality() > 1);

                     use.mapSourceRegisters(mapper);
                  }
               }
            }
         }
      }

   }
}
