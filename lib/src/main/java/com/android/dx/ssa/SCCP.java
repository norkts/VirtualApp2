package com.android.dx.ssa;

import com.android.dx.rop.code.CstInsn;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.PlainInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.TypedConstant;
import com.android.dx.rop.type.TypeBearer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;

public class SCCP {
   private static final int TOP = 0;
   private static final int CONSTANT = 1;
   private static final int VARYING = 2;
   private final SsaMethod ssaMeth;
   private final int regCount;
   private final int[] latticeValues;
   private final Constant[] latticeConstants;
   private final ArrayList<SsaBasicBlock> cfgWorklist;
   private final ArrayList<SsaBasicBlock> cfgPhiWorklist;
   private final BitSet executableBlocks;
   private final ArrayList<SsaInsn> ssaWorklist;
   private final ArrayList<SsaInsn> varyingWorklist;
   private final ArrayList<SsaInsn> branchWorklist;

   private SCCP(SsaMethod ssaMeth) {
      this.ssaMeth = ssaMeth;
      this.regCount = ssaMeth.getRegCount();
      this.latticeValues = new int[this.regCount];
      this.latticeConstants = new Constant[this.regCount];
      this.cfgWorklist = new ArrayList();
      this.cfgPhiWorklist = new ArrayList();
      this.executableBlocks = new BitSet(ssaMeth.getBlocks().size());
      this.ssaWorklist = new ArrayList();
      this.varyingWorklist = new ArrayList();
      this.branchWorklist = new ArrayList();

      for(int i = 0; i < this.regCount; ++i) {
         this.latticeValues[i] = 0;
         this.latticeConstants[i] = null;
      }

   }

   public static void process(SsaMethod ssaMethod) {
      (new SCCP(ssaMethod)).run();
   }

   private void addBlockToWorklist(SsaBasicBlock ssaBlock) {
      if (!this.executableBlocks.get(ssaBlock.getIndex())) {
         this.cfgWorklist.add(ssaBlock);
         this.executableBlocks.set(ssaBlock.getIndex());
      } else {
         this.cfgPhiWorklist.add(ssaBlock);
      }

   }

   private void addUsersToWorklist(int reg, int latticeValue) {
      Iterator var3;
      SsaInsn insn;
      if (latticeValue == 2) {
         var3 = this.ssaMeth.getUseListForRegister(reg).iterator();

         while(var3.hasNext()) {
            insn = (SsaInsn)var3.next();
            this.varyingWorklist.add(insn);
         }
      } else {
         var3 = this.ssaMeth.getUseListForRegister(reg).iterator();

         while(var3.hasNext()) {
            insn = (SsaInsn)var3.next();
            this.ssaWorklist.add(insn);
         }
      }

   }

   private boolean setLatticeValueTo(int reg, int value, Constant cst) {
      if (value != 1) {
         if (this.latticeValues[reg] != value) {
            this.latticeValues[reg] = value;
            return true;
         } else {
            return false;
         }
      } else if (this.latticeValues[reg] == value && this.latticeConstants[reg].equals(cst)) {
         return false;
      } else {
         this.latticeValues[reg] = value;
         this.latticeConstants[reg] = cst;
         return true;
      }
   }

   private void simulatePhi(PhiInsn insn) {
      int phiResultReg = insn.getResult().getReg();
      if (this.latticeValues[phiResultReg] != 2) {
         RegisterSpecList sources = insn.getSources();
         int phiResultValue = 0;
         Constant phiConstant = null;
         int sourceSize = sources.size();

         for(int i = 0; i < sourceSize; ++i) {
            int predBlockIndex = insn.predBlockIndexForSourcesIndex(i);
            int sourceReg = sources.get(i).getReg();
            int sourceRegValue = this.latticeValues[sourceReg];
            if (this.executableBlocks.get(predBlockIndex)) {
               if (sourceRegValue != 1) {
                  phiResultValue = sourceRegValue;
                  break;
               }

               if (phiConstant == null) {
                  phiConstant = this.latticeConstants[sourceReg];
                  phiResultValue = 1;
               } else if (!this.latticeConstants[sourceReg].equals(phiConstant)) {
                  phiResultValue = 2;
                  break;
               }
            }
         }

         if (this.setLatticeValueTo(phiResultReg, phiResultValue, phiConstant)) {
            this.addUsersToWorklist(phiResultReg, phiResultValue);
         }

      }
   }

   private void simulateBlock(SsaBasicBlock block) {
      Iterator var2 = block.getInsns().iterator();

      while(var2.hasNext()) {
         SsaInsn insn = (SsaInsn)var2.next();
         if (insn instanceof PhiInsn) {
            this.simulatePhi((PhiInsn)insn);
         } else {
            this.simulateStmt(insn);
         }
      }

   }

   private void simulatePhiBlock(SsaBasicBlock block) {
      Iterator var2 = block.getInsns().iterator();

      while(var2.hasNext()) {
         SsaInsn insn = (SsaInsn)var2.next();
         if (!(insn instanceof PhiInsn)) {
            return;
         }

         this.simulatePhi((PhiInsn)insn);
      }

   }

   private static String latticeValName(int latticeVal) {
      switch (latticeVal) {
         case 0:
            return "TOP";
         case 1:
            return "CONSTANT";
         case 2:
            return "VARYING";
         default:
            return "UNKNOWN";
      }
   }

   private void simulateBranch(SsaInsn insn) {
      Rop opcode = insn.getOpcode();
      RegisterSpecList sources = insn.getSources();
      boolean constantBranch = false;
      boolean constantSuccessor = false;
      if (opcode.getBranchingness() == 4) {
         Constant cA = null;
         Constant cB = null;
         RegisterSpec specA = sources.get(0);
         int regA = specA.getReg();
         if (!this.ssaMeth.isRegALocal(specA) && this.latticeValues[regA] == 1) {
            cA = this.latticeConstants[regA];
         }

         int vB;
         if (sources.size() == 2) {
            RegisterSpec specB = sources.get(1);
            vB = specB.getReg();
            if (!this.ssaMeth.isRegALocal(specB) && this.latticeValues[vB] == 1) {
               cB = this.latticeConstants[vB];
            }
         }

         int vA;
         if (cA != null && sources.size() == 1) {
            switch (((TypedConstant)cA).getBasicType()) {
               case 6:
                  constantBranch = true;
                  vA = ((CstInteger)cA).getValue();
                  switch (opcode.getOpcode()) {
                     case 7:
                        constantSuccessor = vA == 0;
                        break;
                     case 8:
                        constantSuccessor = vA != 0;
                        break;
                     case 9:
                        constantSuccessor = vA < 0;
                        break;
                     case 10:
                        constantSuccessor = vA >= 0;
                        break;
                     case 11:
                        constantSuccessor = vA <= 0;
                        break;
                     case 12:
                        constantSuccessor = vA > 0;
                        break;
                     default:
                        throw new RuntimeException("Unexpected op");
                  }
            }
         } else if (cA != null && cB != null) {
            switch (((TypedConstant)cA).getBasicType()) {
               case 6:
                  constantBranch = true;
                  vA = ((CstInteger)cA).getValue();
                  vB = ((CstInteger)cB).getValue();
                  switch (opcode.getOpcode()) {
                     case 7:
                        constantSuccessor = vA == vB;
                        break;
                     case 8:
                        constantSuccessor = vA != vB;
                        break;
                     case 9:
                        constantSuccessor = vA < vB;
                        break;
                     case 10:
                        constantSuccessor = vA >= vB;
                        break;
                     case 11:
                        constantSuccessor = vA <= vB;
                        break;
                     case 12:
                        constantSuccessor = vA > vB;
                        break;
                     default:
                        throw new RuntimeException("Unexpected op");
                  }
            }
         }
      }

      SsaBasicBlock block = insn.getBlock();
      int successorBlock;
      if (constantBranch) {
         if (constantSuccessor) {
            successorBlock = block.getSuccessorList().get(1);
         } else {
            successorBlock = block.getSuccessorList().get(0);
         }

         this.addBlockToWorklist((SsaBasicBlock)this.ssaMeth.getBlocks().get(successorBlock));
         this.branchWorklist.add(insn);
      } else {
         for(successorBlock = 0; successorBlock < block.getSuccessorList().size(); ++successorBlock) {
            successorBlock = block.getSuccessorList().get(successorBlock);
            this.addBlockToWorklist((SsaBasicBlock)this.ssaMeth.getBlocks().get(successorBlock));
         }
      }

   }

   private Constant simulateMath(SsaInsn insn, int resultType) {
      Insn ropInsn = insn.getOriginalRopInsn();
      int opcode = insn.getOpcode().getOpcode();
      RegisterSpecList sources = insn.getSources();
      int regA = sources.get(0).getReg();
      Constant cA;
      if (this.latticeValues[regA] != 1) {
         cA = null;
      } else {
         cA = this.latticeConstants[regA];
      }

      Constant cB;
      int vR;
      if (sources.size() == 1) {
         CstInsn cstInsn = (CstInsn)ropInsn;
         cB = cstInsn.getConstant();
      } else {
         vR = sources.get(1).getReg();
         if (this.latticeValues[vR] != 1) {
            cB = null;
         } else {
            cB = this.latticeConstants[vR];
         }
      }

      if (cA != null && cB != null) {
         switch (resultType) {
            case 6:
               boolean skip = false;
               int vA = ((CstInteger)cA).getValue();
               int vB = ((CstInteger)cB).getValue();
               switch (opcode) {
                  case 14:
                     vR = vA + vB;
                     break;
                  case 15:
                     if (sources.size() == 1) {
                        vR = vB - vA;
                     } else {
                        vR = vA - vB;
                     }
                     break;
                  case 16:
                     vR = vA * vB;
                     break;
                  case 17:
                     if (vB == 0) {
                        skip = true;
                        vR = 0;
                     } else {
                        vR = vA / vB;
                     }
                     break;
                  case 18:
                     if (vB == 0) {
                        skip = true;
                        vR = 0;
                     } else {
                        vR = vA % vB;
                     }
                     break;
                  case 19:
                  default:
                     throw new RuntimeException("Unexpected op");
                  case 20:
                     vR = vA & vB;
                     break;
                  case 21:
                     vR = vA | vB;
                     break;
                  case 22:
                     vR = vA ^ vB;
                     break;
                  case 23:
                     vR = vA << vB;
                     break;
                  case 24:
                     vR = vA >> vB;
                     break;
                  case 25:
                     vR = vA >>> vB;
               }

               return skip ? null : CstInteger.make(vR);
            default:
               return null;
         }
      } else {
         return null;
      }
   }

   private void simulateStmt(SsaInsn insn) {
      Insn ropInsn = insn.getOriginalRopInsn();
      if (ropInsn.getOpcode().getBranchingness() != 1 || ropInsn.getOpcode().isCallLike()) {
         this.simulateBranch(insn);
      }

      int opcode = insn.getOpcode().getOpcode();
      RegisterSpec result = insn.getResult();
      if (result == null) {
         if (opcode != 17 && opcode != 18) {
            return;
         }

         SsaBasicBlock succ = insn.getBlock().getPrimarySuccessor();
         result = ((SsaInsn)succ.getInsns().get(0)).getResult();
      }

      int resultReg = result.getReg();
      int resultValue = 2;
      Constant resultConstant = null;
      switch (opcode) {
         case 2:
            if (insn.getSources().size() == 1) {
               int sourceReg = insn.getSources().get(0).getReg();
               resultValue = this.latticeValues[sourceReg];
               resultConstant = this.latticeConstants[sourceReg];
            }
         case 3:
         case 4:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 19:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         default:
            break;
         case 5:
            CstInsn cstInsn = (CstInsn)ropInsn;
            resultValue = 1;
            resultConstant = cstInsn.getConstant();
            break;
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
            resultConstant = this.simulateMath(insn, result.getBasicType());
            if (resultConstant != null) {
               resultValue = 1;
            }
            break;
         case 56:
            if (this.latticeValues[resultReg] == 1) {
               resultValue = this.latticeValues[resultReg];
               resultConstant = this.latticeConstants[resultReg];
            }
      }

      if (this.setLatticeValueTo(resultReg, resultValue, resultConstant)) {
         this.addUsersToWorklist(resultReg, resultValue);
      }

   }

   private void run() {
      SsaBasicBlock firstBlock = this.ssaMeth.getEntryBlock();
      this.addBlockToWorklist(firstBlock);

      while(!this.cfgWorklist.isEmpty() || !this.cfgPhiWorklist.isEmpty() || !this.ssaWorklist.isEmpty() || !this.varyingWorklist.isEmpty()) {
         int listSize;
         SsaBasicBlock block;
         while(!this.cfgWorklist.isEmpty()) {
            listSize = this.cfgWorklist.size() - 1;
            block = (SsaBasicBlock)this.cfgWorklist.remove(listSize);
            this.simulateBlock(block);
         }

         while(!this.cfgPhiWorklist.isEmpty()) {
            listSize = this.cfgPhiWorklist.size() - 1;
            block = (SsaBasicBlock)this.cfgPhiWorklist.remove(listSize);
            this.simulatePhiBlock(block);
         }

         SsaInsn insn;
         while(!this.varyingWorklist.isEmpty()) {
            listSize = this.varyingWorklist.size() - 1;
            insn = (SsaInsn)this.varyingWorklist.remove(listSize);
            if (this.executableBlocks.get(insn.getBlock().getIndex())) {
               if (insn instanceof PhiInsn) {
                  this.simulatePhi((PhiInsn)insn);
               } else {
                  this.simulateStmt(insn);
               }
            }
         }

         while(!this.ssaWorklist.isEmpty()) {
            listSize = this.ssaWorklist.size() - 1;
            insn = (SsaInsn)this.ssaWorklist.remove(listSize);
            if (this.executableBlocks.get(insn.getBlock().getIndex())) {
               if (insn instanceof PhiInsn) {
                  this.simulatePhi((PhiInsn)insn);
               } else {
                  this.simulateStmt(insn);
               }
            }
         }
      }

      this.replaceConstants();
      this.replaceBranches();
   }

   private void replaceConstants() {
      for(int reg = 0; reg < this.regCount; ++reg) {
         if (this.latticeValues[reg] == 1 && this.latticeConstants[reg] instanceof TypedConstant) {
            SsaInsn defn = this.ssaMeth.getDefinitionForRegister(reg);
            TypeBearer typeBearer = defn.getResult().getTypeBearer();
            if (!typeBearer.isConstant()) {
               RegisterSpec dest = defn.getResult();
               RegisterSpec newDest = dest.withType((TypedConstant)this.latticeConstants[reg]);
               defn.setResult(newDest);
               Iterator var6 = this.ssaMeth.getUseListForRegister(reg).iterator();

               while(var6.hasNext()) {
                  SsaInsn insn = (SsaInsn)var6.next();
                  if (!insn.isPhiOrMove()) {
                     NormalSsaInsn nInsn = (NormalSsaInsn)insn;
                     RegisterSpecList sources = insn.getSources();
                     int index = sources.indexOfRegister(reg);
                     RegisterSpec spec = sources.get(index);
                     RegisterSpec newSpec = spec.withType((TypedConstant)this.latticeConstants[reg]);
                     nInsn.changeOneSource(index, newSpec);
                  }
               }
            }
         }
      }

   }

   private void replaceBranches() {
      Iterator var1 = this.branchWorklist.iterator();

      while(var1.hasNext()) {
         SsaInsn insn = (SsaInsn)var1.next();
         int oldSuccessor = -1;
         SsaBasicBlock block = insn.getBlock();
         int successorSize = block.getSuccessorList().size();

         for(int i = 0; i < successorSize; ++i) {
            int successorBlock = block.getSuccessorList().get(i);
            if (!this.executableBlocks.get(successorBlock)) {
               oldSuccessor = successorBlock;
            }
         }

         if (successorSize == 2 && oldSuccessor != -1) {
            Insn originalRopInsn = insn.getOriginalRopInsn();
            block.replaceLastInsn(new PlainInsn(Rops.GOTO, originalRopInsn.getPosition(), (RegisterSpec)null, RegisterSpecList.EMPTY));
            block.removeSuccessor(oldSuccessor);
         }
      }

   }
}
