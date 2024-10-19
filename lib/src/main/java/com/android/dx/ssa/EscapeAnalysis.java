package com.android.dx.ssa;

import com.android.dx.rop.code.Exceptions;
import com.android.dx.rop.code.FillArrayDataInsn;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.PlainCstInsn;
import com.android.dx.rop.code.PlainInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.code.ThrowingCstInsn;
import com.android.dx.rop.code.ThrowingInsn;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstLiteralBits;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.cst.TypedConstant;
import com.android.dx.rop.cst.Zeroes;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class EscapeAnalysis {
   private final SsaMethod ssaMeth;
   private final int regCount;
   private final ArrayList<EscapeSet> latticeValues;

   private EscapeAnalysis(SsaMethod ssaMeth) {
      this.ssaMeth = ssaMeth;
      this.regCount = ssaMeth.getRegCount();
      this.latticeValues = new ArrayList();
   }

   private int findSetIndex(RegisterSpec reg) {
      int i;
      for(i = 0; i < this.latticeValues.size(); ++i) {
         EscapeSet e = (EscapeSet)this.latticeValues.get(i);
         if (e.regSet.get(reg.getReg())) {
            return i;
         }
      }

      return i;
   }

   private SsaInsn getInsnForMove(SsaInsn moveInsn) {
      int pred = moveInsn.getBlock().getPredecessors().nextSetBit(0);
      ArrayList<SsaInsn> predInsns = ((SsaBasicBlock)this.ssaMeth.getBlocks().get(pred)).getInsns();
      return (SsaInsn)predInsns.get(predInsns.size() - 1);
   }

   private SsaInsn getMoveForInsn(SsaInsn insn) {
      int succ = insn.getBlock().getSuccessors().nextSetBit(0);
      ArrayList<SsaInsn> succInsns = ((SsaBasicBlock)this.ssaMeth.getBlocks().get(succ)).getInsns();
      return (SsaInsn)succInsns.get(0);
   }

   private void addEdge(EscapeSet parentSet, EscapeSet childSet) {
      if (!childSet.parentSets.contains(parentSet)) {
         childSet.parentSets.add(parentSet);
      }

      if (!parentSet.childSets.contains(childSet)) {
         parentSet.childSets.add(childSet);
      }

   }

   private void replaceNode(EscapeSet newNode, EscapeSet oldNode) {
      Iterator var3 = oldNode.parentSets.iterator();

      EscapeSet e;
      while(var3.hasNext()) {
         e = (EscapeSet)var3.next();
         e.childSets.remove(oldNode);
         e.childSets.add(newNode);
         newNode.parentSets.add(e);
      }

      var3 = oldNode.childSets.iterator();

      while(var3.hasNext()) {
         e = (EscapeSet)var3.next();
         e.parentSets.remove(oldNode);
         e.parentSets.add(newNode);
         newNode.childSets.add(e);
      }

   }

   public static void process(SsaMethod ssaMethod) {
      (new EscapeAnalysis(ssaMethod)).run();
   }

   private void processInsn(SsaInsn insn) {
      int op = insn.getOpcode().getOpcode();
      RegisterSpec result = insn.getResult();
      EscapeSet escSet;
      if (op == 56 && result.getTypeBearer().getBasicType() == 9) {
         escSet = this.processMoveResultPseudoInsn(insn);
         this.processRegister(result, escSet);
      } else if (op == 3 && result.getTypeBearer().getBasicType() == 9) {
         escSet = new EscapeSet(result.getReg(), this.regCount, EscapeAnalysis.EscapeState.NONE);
         this.latticeValues.add(escSet);
         this.processRegister(result, escSet);
      } else if (op == 55 && result.getTypeBearer().getBasicType() == 9) {
         escSet = new EscapeSet(result.getReg(), this.regCount, EscapeAnalysis.EscapeState.NONE);
         this.latticeValues.add(escSet);
         this.processRegister(result, escSet);
      }

   }

   private EscapeSet processMoveResultPseudoInsn(SsaInsn insn) {
      RegisterSpec result = insn.getResult();
      SsaInsn prevSsaInsn = this.getInsnForMove(insn);
      int prevOpcode = prevSsaInsn.getOpcode().getOpcode();
      EscapeSet escSet;
      RegisterSpec prevSource;
      switch (prevOpcode) {
         case 5:
         case 40:
            escSet = new EscapeSet(result.getReg(), this.regCount, EscapeAnalysis.EscapeState.NONE);
            break;
         case 38:
         case 43:
         case 45:
            prevSource = prevSsaInsn.getSources().get(0);
            int setIndex = this.findSetIndex(prevSource);
            if (setIndex != this.latticeValues.size()) {
               escSet = (EscapeSet)this.latticeValues.get(setIndex);
               escSet.regSet.set(result.getReg());
               return escSet;
            }

            if (prevSource.getType() == Type.KNOWN_NULL) {
               escSet = new EscapeSet(result.getReg(), this.regCount, EscapeAnalysis.EscapeState.NONE);
            } else {
               escSet = new EscapeSet(result.getReg(), this.regCount, EscapeAnalysis.EscapeState.GLOBAL);
            }
            break;
         case 41:
         case 42:
            prevSource = prevSsaInsn.getSources().get(0);
            if (prevSource.getTypeBearer().isConstant()) {
               escSet = new EscapeSet(result.getReg(), this.regCount, EscapeAnalysis.EscapeState.NONE);
               escSet.replaceableArray = true;
            } else {
               escSet = new EscapeSet(result.getReg(), this.regCount, EscapeAnalysis.EscapeState.GLOBAL);
            }
            break;
         case 46:
            escSet = new EscapeSet(result.getReg(), this.regCount, EscapeAnalysis.EscapeState.GLOBAL);
            break;
         default:
            return null;
      }

      this.latticeValues.add(escSet);
      return escSet;
   }

   private void processRegister(RegisterSpec result, EscapeSet escSet) {
      ArrayList<RegisterSpec> regWorklist = new ArrayList();
      regWorklist.add(result);

      while(!regWorklist.isEmpty()) {
         int listSize = regWorklist.size() - 1;
         RegisterSpec def = (RegisterSpec)regWorklist.remove(listSize);
         List<SsaInsn> useList = this.ssaMeth.getUseListForRegister(def.getReg());
         Iterator var7 = useList.iterator();

         while(var7.hasNext()) {
            SsaInsn use = (SsaInsn)var7.next();
            Rop useOpcode = use.getOpcode();
            if (useOpcode == null) {
               this.processPhiUse(use, escSet, regWorklist);
            } else {
               this.processUse(def, use, escSet, regWorklist);
            }
         }
      }

   }

   private void processPhiUse(SsaInsn use, EscapeSet escSet, ArrayList<RegisterSpec> regWorklist) {
      int setIndex = this.findSetIndex(use.getResult());
      if (setIndex != this.latticeValues.size()) {
         EscapeSet mergeSet = (EscapeSet)this.latticeValues.get(setIndex);
         if (mergeSet != escSet) {
            escSet.replaceableArray = false;
            escSet.regSet.or(mergeSet.regSet);
            if (escSet.escape.compareTo(mergeSet.escape) < 0) {
               escSet.escape = mergeSet.escape;
            }

            this.replaceNode(escSet, mergeSet);
            this.latticeValues.remove(setIndex);
         }
      } else {
         escSet.regSet.set(use.getResult().getReg());
         regWorklist.add(use.getResult());
      }

   }

   private void processUse(RegisterSpec def, SsaInsn use, EscapeSet escSet, ArrayList<RegisterSpec> regWorklist) {
      int useOpcode = use.getOpcode().getOpcode();
      switch (useOpcode) {
         case 2:
            escSet.regSet.set(use.getResult().getReg());
            regWorklist.add(use.getResult());
         case 3:
         case 4:
         case 5:
         case 6:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 34:
         case 36:
         case 37:
         case 40:
         case 41:
         case 42:
         case 44:
         case 45:
         case 46:
         default:
            break;
         case 7:
         case 8:
         case 43:
            if (escSet.escape.compareTo(EscapeAnalysis.EscapeState.METHOD) < 0) {
               escSet.escape = EscapeAnalysis.EscapeState.METHOD;
            }
            break;
         case 33:
         case 35:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
            escSet.escape = EscapeAnalysis.EscapeState.INTER;
            break;
         case 38:
            RegisterSpec getIndex = use.getSources().get(1);
            if (!getIndex.getTypeBearer().isConstant()) {
               escSet.replaceableArray = false;
            }
            break;
         case 39:
            RegisterSpec putIndex = use.getSources().get(2);
            if (!putIndex.getTypeBearer().isConstant()) {
               escSet.replaceableArray = false;
            }
         case 47:
            RegisterSpec putValue = use.getSources().get(0);
            if (putValue.getTypeBearer().getBasicType() == 9) {
               escSet.replaceableArray = false;
               RegisterSpecList sources = use.getSources();
               int setIndex;
               EscapeSet parentSet;
               if (sources.get(0).getReg() == def.getReg()) {
                  setIndex = this.findSetIndex(sources.get(1));
                  if (setIndex != this.latticeValues.size()) {
                     parentSet = (EscapeSet)this.latticeValues.get(setIndex);
                     this.addEdge(parentSet, escSet);
                     if (escSet.escape.compareTo(parentSet.escape) < 0) {
                        escSet.escape = parentSet.escape;
                     }
                  }
               } else {
                  setIndex = this.findSetIndex(sources.get(0));
                  if (setIndex != this.latticeValues.size()) {
                     parentSet = (EscapeSet)this.latticeValues.get(setIndex);
                     this.addEdge(escSet, parentSet);
                     if (parentSet.escape.compareTo(escSet.escape) < 0) {
                        parentSet.escape = escSet.escape;
                     }
                  }
               }
            }
            break;
         case 48:
            escSet.escape = EscapeAnalysis.EscapeState.GLOBAL;
      }

   }

   private void scalarReplacement() {
      Iterator var1 = this.latticeValues.iterator();

      while(true) {
         EscapeSet escSet;
         do {
            do {
               if (!var1.hasNext()) {
                  return;
               }

               escSet = (EscapeSet)var1.next();
            } while(!escSet.replaceableArray);
         } while(escSet.escape != EscapeAnalysis.EscapeState.NONE);

         int e = escSet.regSet.nextSetBit(0);
         SsaInsn def = this.ssaMeth.getDefinitionForRegister(e);
         SsaInsn prev = this.getInsnForMove(def);
         TypeBearer lengthReg = prev.getSources().get(0).getTypeBearer();
         int length = ((CstLiteralBits)lengthReg).getIntBits();
         ArrayList<RegisterSpec> newRegs = new ArrayList(length);
         HashSet<SsaInsn> deletedInsns = new HashSet();
         this.replaceDef(def, prev, length, newRegs);
         deletedInsns.add(prev);
         deletedInsns.add(def);
         List<SsaInsn> useList = this.ssaMeth.getUseListForRegister(e);
         Iterator var11 = useList.iterator();

         while(var11.hasNext()) {
            SsaInsn use = (SsaInsn)var11.next();
            this.replaceUse(use, prev, newRegs, deletedInsns);
            deletedInsns.add(use);
         }

         this.ssaMeth.deleteInsns(deletedInsns);
         this.ssaMeth.onInsnsChanged();
         SsaConverter.updateSsaMethod(this.ssaMeth, this.regCount);
         this.movePropagate();
      }
   }

   private void replaceDef(SsaInsn def, SsaInsn prev, int length, ArrayList<RegisterSpec> newRegs) {
      Type resultType = def.getResult().getType();

      for(int i = 0; i < length; ++i) {
         Constant newZero = Zeroes.zeroFor(resultType.getComponentType());
         TypedConstant typedZero = (TypedConstant)newZero;
         RegisterSpec newReg = RegisterSpec.make(this.ssaMeth.makeNewSsaReg(), typedZero);
         newRegs.add(newReg);
         this.insertPlainInsnBefore(def, RegisterSpecList.EMPTY, newReg, 5, newZero);
      }

   }

   private void replaceUse(SsaInsn use, SsaInsn prev, ArrayList<RegisterSpec> newRegs, HashSet<SsaInsn> deletedInsns) {
      int length = newRegs.size();
      int index;
      SsaInsn next;
      RegisterSpecList sources;
      RegisterSpec source;
      RegisterSpec result;
      CstLiteralBits indexReg;
      switch (use.getOpcode().getOpcode()) {
         case 34:
            TypeBearer lengthReg = prev.getSources().get(0).getTypeBearer();
            next = this.getMoveForInsn(use);
            this.insertPlainInsnBefore(next, RegisterSpecList.EMPTY, next.getResult(), 5, (Constant)lengthReg);
            deletedInsns.add(next);
            break;
         case 38:
            next = this.getMoveForInsn(use);
            sources = use.getSources();
            indexReg = (CstLiteralBits)sources.get(1).getTypeBearer();
            index = indexReg.getIntBits();
            if (index < length) {
               source = (RegisterSpec)newRegs.get(index);
               result = source.withReg(next.getResult().getReg());
               this.insertPlainInsnBefore(next, RegisterSpecList.make(source), result, 2, (Constant)null);
            } else {
               this.insertExceptionThrow(next, sources.get(1), deletedInsns);
               deletedInsns.add((SsaInsn)next.getBlock().getInsns().get(2));
            }

            deletedInsns.add(next);
            break;
         case 39:
            sources = use.getSources();
            indexReg = (CstLiteralBits)sources.get(2).getTypeBearer();
            index = indexReg.getIntBits();
            if (index < length) {
               source = sources.get(0);
               result = source.withReg(((RegisterSpec)newRegs.get(index)).getReg());
               this.insertPlainInsnBefore(use, RegisterSpecList.make(source), result, 2, (Constant)null);
               newRegs.set(index, result.withSimpleType());
            } else {
               this.insertExceptionThrow(use, sources.get(2), deletedInsns);
            }
         case 54:
         default:
            break;
         case 57:
            Insn ropUse = use.getOriginalRopInsn();
            FillArrayDataInsn fill = (FillArrayDataInsn)ropUse;
            ArrayList<Constant> constList = fill.getInitValues();

            for(int i = 0; i < length; ++i) {
               RegisterSpec newFill = RegisterSpec.make(((RegisterSpec)newRegs.get(i)).getReg(), (TypeBearer)constList.get(i));
               this.insertPlainInsnBefore(use, RegisterSpecList.EMPTY, newFill, 5, (Constant)constList.get(i));
               newRegs.set(i, newFill);
            }
      }

   }

   private void movePropagate() {
      for(int i = 0; i < this.ssaMeth.getRegCount(); ++i) {
         SsaInsn insn = this.ssaMeth.getDefinitionForRegister(i);
         if (insn != null && insn.getOpcode() != null && insn.getOpcode().getOpcode() == 2) {
            ArrayList<SsaInsn>[] useList = this.ssaMeth.getUseListCopy();
            final RegisterSpec source = insn.getSources().get(0);
            final RegisterSpec result = insn.getResult();
            if (source.getReg() >= this.regCount || result.getReg() >= this.regCount) {
               RegisterMapper mapper = new RegisterMapper() {
                  public int getNewRegisterCount() {
                     return EscapeAnalysis.this.ssaMeth.getRegCount();
                  }

                  public RegisterSpec map(RegisterSpec registerSpec) {
                     return registerSpec.getReg() == result.getReg() ? source : registerSpec;
                  }
               };
               Iterator var7 = useList[result.getReg()].iterator();

               while(var7.hasNext()) {
                  SsaInsn use = (SsaInsn)var7.next();
                  use.mapSourceRegisters(mapper);
               }
            }
         }
      }

   }

   private void run() {
      this.ssaMeth.forEachBlockDepthFirstDom(new SsaBasicBlock.Visitor() {
         public void visitBlock(SsaBasicBlock block, SsaBasicBlock unused) {
            block.forEachInsn(new SsaInsn.Visitor() {
               public void visitMoveInsn(NormalSsaInsn insn) {
               }

               public void visitPhiInsn(PhiInsn insn) {
               }

               public void visitNonMoveInsn(NormalSsaInsn insn) {
                  EscapeAnalysis.this.processInsn(insn);
               }
            });
         }
      });
      Iterator var1 = this.latticeValues.iterator();

      while(true) {
         EscapeSet e;
         do {
            if (!var1.hasNext()) {
               this.scalarReplacement();
               return;
            }

            e = (EscapeSet)var1.next();
         } while(e.escape == EscapeAnalysis.EscapeState.NONE);

         Iterator var3 = e.childSets.iterator();

         while(var3.hasNext()) {
            EscapeSet field = (EscapeSet)var3.next();
            if (e.escape.compareTo(field.escape) > 0) {
               field.escape = e.escape;
            }
         }
      }
   }

   private void insertExceptionThrow(SsaInsn insn, RegisterSpec index, HashSet<SsaInsn> deletedInsns) {
      CstType exception = new CstType(Exceptions.TYPE_ArrayIndexOutOfBoundsException);
      this.insertThrowingInsnBefore(insn, RegisterSpecList.EMPTY, (RegisterSpec)null, 40, exception);
      SsaBasicBlock currBlock = insn.getBlock();
      SsaBasicBlock newBlock = currBlock.insertNewSuccessor(currBlock.getPrimarySuccessor());
      SsaInsn newInsn = (SsaInsn)newBlock.getInsns().get(0);
      RegisterSpec newReg = RegisterSpec.make(this.ssaMeth.makeNewSsaReg(), exception);
      this.insertPlainInsnBefore(newInsn, RegisterSpecList.EMPTY, newReg, 56, (Constant)null);
      SsaBasicBlock newBlock2 = newBlock.insertNewSuccessor(newBlock.getPrimarySuccessor());
      SsaInsn newInsn2 = (SsaInsn)newBlock2.getInsns().get(0);
      CstNat newNat = new CstNat(new CstString("<init>"), new CstString("(I)V"));
      CstMethodRef newRef = new CstMethodRef(exception, newNat);
      this.insertThrowingInsnBefore(newInsn2, RegisterSpecList.make(newReg, index), (RegisterSpec)null, 52, newRef);
      deletedInsns.add(newInsn2);
      SsaBasicBlock newBlock3 = newBlock2.insertNewSuccessor(newBlock2.getPrimarySuccessor());
      SsaInsn newInsn3 = (SsaInsn)newBlock3.getInsns().get(0);
      this.insertThrowingInsnBefore(newInsn3, RegisterSpecList.make(newReg), (RegisterSpec)null, 35, (Constant)null);
      newBlock3.replaceSuccessor(newBlock3.getPrimarySuccessorIndex(), this.ssaMeth.getExitBlock().getIndex());
      deletedInsns.add(newInsn3);
   }

   private void insertPlainInsnBefore(SsaInsn insn, RegisterSpecList newSources, RegisterSpec newResult, int newOpcode, Constant cst) {
      Insn originalRopInsn = insn.getOriginalRopInsn();
      Rop newRop;
      if (newOpcode == 56) {
         newRop = Rops.opMoveResultPseudo(newResult.getType());
      } else {
         newRop = Rops.ropFor(newOpcode, newResult, newSources, cst);
      }

      Object newRopInsn;
      if (cst == null) {
         newRopInsn = new PlainInsn(newRop, originalRopInsn.getPosition(), newResult, newSources);
      } else {
         newRopInsn = new PlainCstInsn(newRop, originalRopInsn.getPosition(), newResult, newSources, cst);
      }

      NormalSsaInsn newInsn = new NormalSsaInsn((Insn)newRopInsn, insn.getBlock());
      List<SsaInsn> insns = insn.getBlock().getInsns();
      insns.add(insns.lastIndexOf(insn), newInsn);
      this.ssaMeth.onInsnAdded(newInsn);
   }

   private void insertThrowingInsnBefore(SsaInsn insn, RegisterSpecList newSources, RegisterSpec newResult, int newOpcode, Constant cst) {
      Insn origRopInsn = insn.getOriginalRopInsn();
      Rop newRop = Rops.ropFor(newOpcode, newResult, newSources, cst);
      Object newRopInsn;
      if (cst == null) {
         newRopInsn = new ThrowingInsn(newRop, origRopInsn.getPosition(), newSources, StdTypeList.EMPTY);
      } else {
         newRopInsn = new ThrowingCstInsn(newRop, origRopInsn.getPosition(), newSources, StdTypeList.EMPTY, cst);
      }

      NormalSsaInsn newInsn = new NormalSsaInsn((Insn)newRopInsn, insn.getBlock());
      List<SsaInsn> insns = insn.getBlock().getInsns();
      insns.add(insns.lastIndexOf(insn), newInsn);
      this.ssaMeth.onInsnAdded(newInsn);
   }

   public static enum EscapeState {
      TOP,
      NONE,
      METHOD,
      INTER,
      GLOBAL;
   }

   static class EscapeSet {
      BitSet regSet;
      EscapeState escape;
      ArrayList<EscapeSet> childSets;
      ArrayList<EscapeSet> parentSets;
      boolean replaceableArray;

      EscapeSet(int reg, int size, EscapeState escState) {
         this.regSet = new BitSet(size);
         this.regSet.set(reg);
         this.escape = escState;
         this.childSets = new ArrayList();
         this.parentSets = new ArrayList();
         this.replaceableArray = false;
      }
   }
}
