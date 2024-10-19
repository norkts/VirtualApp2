package com.android.dx.ssa.back;

import com.android.dx.rop.code.CstInsn;
import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.ssa.InterferenceRegisterMapper;
import com.android.dx.ssa.NormalSsaInsn;
import com.android.dx.ssa.Optimizer;
import com.android.dx.ssa.PhiInsn;
import com.android.dx.ssa.RegisterMapper;
import com.android.dx.ssa.SsaBasicBlock;
import com.android.dx.ssa.SsaInsn;
import com.android.dx.ssa.SsaMethod;
import com.android.dx.util.IntIterator;
import com.android.dx.util.IntSet;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class FirstFitLocalCombiningAllocator extends RegisterAllocator {
   private static final boolean DEBUG = false;
   private final Map<LocalItem, ArrayList<RegisterSpec>> localVariables;
   private final ArrayList<NormalSsaInsn> moveResultPseudoInsns;
   private final ArrayList<NormalSsaInsn> invokeRangeInsns;
   private final ArrayList<PhiInsn> phiInsns;
   private final BitSet ssaRegsMapped;
   private final InterferenceRegisterMapper mapper;
   private final int paramRangeEnd;
   private final BitSet reservedRopRegs;
   private final BitSet usedRopRegs;
   private final boolean minimizeRegisters;

   public FirstFitLocalCombiningAllocator(SsaMethod ssaMeth, InterferenceGraph interference, boolean minimizeRegisters) {
      super(ssaMeth, interference);
      this.ssaRegsMapped = new BitSet(ssaMeth.getRegCount());
      this.mapper = new InterferenceRegisterMapper(interference, ssaMeth.getRegCount());
      this.minimizeRegisters = minimizeRegisters;
      this.paramRangeEnd = ssaMeth.getParamWidth();
      this.reservedRopRegs = new BitSet(this.paramRangeEnd * 2);
      this.reservedRopRegs.set(0, this.paramRangeEnd);
      this.usedRopRegs = new BitSet(this.paramRangeEnd * 2);
      this.localVariables = new TreeMap();
      this.moveResultPseudoInsns = new ArrayList();
      this.invokeRangeInsns = new ArrayList();
      this.phiInsns = new ArrayList();
   }

   public boolean wantsParamsMovedHigh() {
      return true;
   }

   public RegisterMapper allocateRegisters() {
      this.analyzeInstructions();
      this.handleLocalAssociatedParams();
      this.handleUnassociatedParameters();
      this.handleInvokeRangeInsns();
      this.handleLocalAssociatedOther();
      this.handleCheckCastResults();
      this.handlePhiInsns();
      this.handleNormalUnassociated();
      return this.mapper;
   }

   private void printLocalVars() {
      System.out.println("Printing local vars");
      Iterator var1 = this.localVariables.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry<LocalItem, ArrayList<RegisterSpec>> e = (Map.Entry)var1.next();
         StringBuilder regs = new StringBuilder();
         regs.append('{');
         regs.append(' ');
         Iterator var4 = ((ArrayList)e.getValue()).iterator();

         while(var4.hasNext()) {
            RegisterSpec reg = (RegisterSpec)var4.next();
            regs.append('v');
            regs.append(reg.getReg());
            regs.append(' ');
         }

         regs.append('}');
         System.out.printf("Local: %s Registers: %s\n", e.getKey(), regs);
      }

   }

   private void handleLocalAssociatedParams() {
      Iterator var1 = this.localVariables.values().iterator();

      while(var1.hasNext()) {
         ArrayList<RegisterSpec> ssaRegs = (ArrayList)var1.next();
         int sz = ssaRegs.size();
         int paramIndex = -1;
         int paramCategory = 0;

         for(int i = 0; i < sz; ++i) {
            RegisterSpec ssaSpec = (RegisterSpec)ssaRegs.get(i);
            int ssaReg = ssaSpec.getReg();
            paramIndex = this.getParameterIndexForReg(ssaReg);
            if (paramIndex >= 0) {
               paramCategory = ssaSpec.getCategory();
               this.addMapping(ssaSpec, paramIndex);
               break;
            }
         }

         if (paramIndex >= 0) {
            this.tryMapRegs(ssaRegs, paramIndex, paramCategory, true);
         }
      }

   }

   private int getParameterIndexForReg(int ssaReg) {
      SsaInsn defInsn = this.ssaMeth.getDefinitionForRegister(ssaReg);
      if (defInsn == null) {
         return -1;
      } else {
         Rop opcode = defInsn.getOpcode();
         if (opcode != null && opcode.getOpcode() == 3) {
            CstInsn origInsn = (CstInsn)defInsn.getOriginalRopInsn();
            return ((CstInteger)origInsn.getConstant()).getValue();
         } else {
            return -1;
         }
      }
   }

   private void handleLocalAssociatedOther() {
      Iterator var1 = this.localVariables.values().iterator();

      while(var1.hasNext()) {
         ArrayList<RegisterSpec> specs = (ArrayList)var1.next();
         int ropReg = this.paramRangeEnd;
         boolean done = false;

         while(true) {
            int maxCategory = 1;
            int sz = specs.size();

            for(int i = 0; i < sz; ++i) {
               RegisterSpec ssaSpec = (RegisterSpec)specs.get(i);
               int category = ssaSpec.getCategory();
               if (!this.ssaRegsMapped.get(ssaSpec.getReg()) && category > maxCategory) {
                  maxCategory = category;
               }
            }

            ropReg = this.findRopRegForLocal(ropReg, maxCategory);
            if (this.canMapRegs(specs, ropReg)) {
               done = this.tryMapRegs(specs, ropReg, maxCategory, true);
            }

            ++ropReg;
            if (done) {
               break;
            }
         }
      }

   }

   private boolean tryMapRegs(ArrayList<RegisterSpec> specs, int ropReg, int maxAllowedCategory, boolean markReserved) {
      boolean remaining = false;
      Iterator var6 = specs.iterator();

      while(true) {
         RegisterSpec spec;
         do {
            if (!var6.hasNext()) {
               return !remaining;
            }

            spec = (RegisterSpec)var6.next();
         } while(this.ssaRegsMapped.get(spec.getReg()));

         boolean succeeded = this.tryMapReg(spec, ropReg, maxAllowedCategory);
         remaining = !succeeded || remaining;
         if (succeeded && markReserved) {
            this.markReserved(ropReg, spec.getCategory());
         }
      }
   }

   private boolean tryMapReg(RegisterSpec ssaSpec, int ropReg, int maxAllowedCategory) {
      if (ssaSpec.getCategory() <= maxAllowedCategory && !this.ssaRegsMapped.get(ssaSpec.getReg()) && this.canMapReg(ssaSpec, ropReg)) {
         this.addMapping(ssaSpec, ropReg);
         return true;
      } else {
         return false;
      }
   }

   private void markReserved(int ropReg, int category) {
      this.reservedRopRegs.set(ropReg, ropReg + category, true);
   }

   private boolean rangeContainsReserved(int ropRangeStart, int width) {
      for(int i = ropRangeStart; i < ropRangeStart + width; ++i) {
         if (this.reservedRopRegs.get(i)) {
            return true;
         }
      }

      return false;
   }

   private boolean isThisPointerReg(int startReg) {
      return startReg == 0 && !this.ssaMeth.isStatic();
   }

   private Alignment getAlignment(int regCategory) {
      Alignment alignment = FirstFitLocalCombiningAllocator.Alignment.UNSPECIFIED;
      if (regCategory == 2) {
         if (isEven(this.paramRangeEnd)) {
            alignment = FirstFitLocalCombiningAllocator.Alignment.EVEN;
         } else {
            alignment = FirstFitLocalCombiningAllocator.Alignment.ODD;
         }
      }

      return alignment;
   }

   private int findNextUnreservedRopReg(int startReg, int regCategory) {
      return this.findNextUnreservedRopReg(startReg, regCategory, this.getAlignment(regCategory));
   }

   private int findNextUnreservedRopReg(int startReg, int width, Alignment alignment) {
      int reg = alignment.nextClearBit(this.reservedRopRegs, startReg);

      while(true) {
         int i;
         for(i = 1; i < width && !this.reservedRopRegs.get(reg + i); ++i) {
         }

         if (i == width) {
            return reg;
         }

         reg = alignment.nextClearBit(this.reservedRopRegs, reg + i);
      }
   }

   private int findRopRegForLocal(int startReg, int category) {
      Alignment alignment = this.getAlignment(category);
      int reg = alignment.nextClearBit(this.usedRopRegs, startReg);

      while(true) {
         int i;
         for(i = 1; i < category && !this.usedRopRegs.get(reg + i); ++i) {
         }

         if (i == category) {
            return reg;
         }

         reg = alignment.nextClearBit(this.usedRopRegs, reg + i);
      }
   }

   private void handleUnassociatedParameters() {
      int szSsaRegs = this.ssaMeth.getRegCount();

      for(int ssaReg = 0; ssaReg < szSsaRegs; ++ssaReg) {
         if (!this.ssaRegsMapped.get(ssaReg)) {
            int paramIndex = this.getParameterIndexForReg(ssaReg);
            RegisterSpec ssaSpec = this.getDefinitionSpecForSsaReg(ssaReg);
            if (paramIndex >= 0) {
               this.addMapping(ssaSpec, paramIndex);
            }
         }
      }

   }

   private void handleInvokeRangeInsns() {
      Iterator var1 = this.invokeRangeInsns.iterator();

      while(var1.hasNext()) {
         NormalSsaInsn insn = (NormalSsaInsn)var1.next();
         this.adjustAndMapSourceRangeRange(insn);
      }

   }

   private void handleCheckCastResults() {
      Iterator var1 = this.moveResultPseudoInsns.iterator();

      while(true) {
         RegisterSpec moveRegSpec;
         int moveReg;
         SsaInsn checkCastInsn;
         do {
            BitSet predBlocks;
            do {
               if (!var1.hasNext()) {
                  return;
               }

               NormalSsaInsn insn = (NormalSsaInsn)var1.next();
               moveRegSpec = insn.getResult();
               moveReg = moveRegSpec.getReg();
               predBlocks = insn.getBlock().getPredecessors();
            } while(predBlocks.cardinality() != 1);

            SsaBasicBlock predBlock = (SsaBasicBlock)this.ssaMeth.getBlocks().get(predBlocks.nextSetBit(0));
            ArrayList<SsaInsn> insnList = predBlock.getInsns();
            checkCastInsn = (SsaInsn)insnList.get(insnList.size() - 1);
         } while(checkCastInsn.getOpcode().getOpcode() != 43);

         RegisterSpec checkRegSpec = checkCastInsn.getSources().get(0);
         int checkReg = checkRegSpec.getReg();
         int category = checkRegSpec.getCategory();
         boolean moveMapped = this.ssaRegsMapped.get(moveReg);
         boolean checkMapped = this.ssaRegsMapped.get(checkReg);
         int ropReg;
         if (moveMapped & !checkMapped) {
            ropReg = this.mapper.oldToNew(moveReg);
            checkMapped = this.tryMapReg(checkRegSpec, ropReg, category);
         }

         if (checkMapped & !moveMapped) {
            ropReg = this.mapper.oldToNew(checkReg);
            moveMapped = this.tryMapReg(moveRegSpec, ropReg, category);
         }

         if (!moveMapped || !checkMapped) {
            ropReg = this.findNextUnreservedRopReg(this.paramRangeEnd, category);
            ArrayList<RegisterSpec> ssaRegs = new ArrayList(2);
            ssaRegs.add(moveRegSpec);
            ssaRegs.add(checkRegSpec);

            while(!this.tryMapRegs(ssaRegs, ropReg, category, false)) {
               ropReg = this.findNextUnreservedRopReg(ropReg + 1, category);
            }
         }

         boolean hasExceptionHandlers = checkCastInsn.getOriginalRopInsn().getCatches().size() != 0;
         int moveRopReg = this.mapper.oldToNew(moveReg);
         int checkRopReg = this.mapper.oldToNew(checkReg);
         if (moveRopReg != checkRopReg && !hasExceptionHandlers) {
            ((NormalSsaInsn)checkCastInsn).changeOneSource(0, this.insertMoveBefore(checkCastInsn, checkRegSpec));
            this.addMapping(checkCastInsn.getSources().get(0), moveRopReg);
         }
      }
   }

   private void handlePhiInsns() {
      Iterator var1 = this.phiInsns.iterator();

      while(var1.hasNext()) {
         PhiInsn insn = (PhiInsn)var1.next();
         this.processPhiInsn(insn);
      }

   }

   private void handleNormalUnassociated() {
      int szSsaRegs = this.ssaMeth.getRegCount();

      for(int ssaReg = 0; ssaReg < szSsaRegs; ++ssaReg) {
         if (!this.ssaRegsMapped.get(ssaReg)) {
            RegisterSpec ssaSpec = this.getDefinitionSpecForSsaReg(ssaReg);
            if (ssaSpec != null) {
               int category = ssaSpec.getCategory();

               int ropReg;
               for(ropReg = this.findNextUnreservedRopReg(this.paramRangeEnd, category); !this.canMapReg(ssaSpec, ropReg); ropReg = this.findNextUnreservedRopReg(ropReg + 1, category)) {
               }

               this.addMapping(ssaSpec, ropReg);
            }
         }
      }

   }

   private boolean canMapRegs(ArrayList<RegisterSpec> specs, int ropReg) {
      Iterator var3 = specs.iterator();

      RegisterSpec spec;
      do {
         if (!var3.hasNext()) {
            return true;
         }

         spec = (RegisterSpec)var3.next();
      } while(this.ssaRegsMapped.get(spec.getReg()) || this.canMapReg(spec, ropReg));

      return false;
   }

   private boolean canMapReg(RegisterSpec ssaSpec, int ropReg) {
      int category = ssaSpec.getCategory();
      return !this.spansParamRange(ropReg, category) && !this.mapper.interferes(ssaSpec, ropReg);
   }

   private boolean spansParamRange(int ssaReg, int category) {
      return ssaReg < this.paramRangeEnd && ssaReg + category > this.paramRangeEnd;
   }

   private void analyzeInstructions() {
      this.ssaMeth.forEachInsn(new SsaInsn.Visitor() {
         public void visitMoveInsn(NormalSsaInsn insn) {
            this.processInsn(insn);
         }

         public void visitPhiInsn(PhiInsn insn) {
            this.processInsn(insn);
         }

         public void visitNonMoveInsn(NormalSsaInsn insn) {
            this.processInsn(insn);
         }

         private void processInsn(SsaInsn insn) {
            RegisterSpec assignment = insn.getLocalAssignment();
            if (assignment != null) {
               LocalItem local = assignment.getLocalItem();
               ArrayList<RegisterSpec> regList = (ArrayList)FirstFitLocalCombiningAllocator.this.localVariables.get(local);
               if (regList == null) {
                  regList = new ArrayList();
                  FirstFitLocalCombiningAllocator.this.localVariables.put(local, regList);
               }

               regList.add(assignment);
            }

            if (insn instanceof NormalSsaInsn) {
               if (insn.getOpcode().getOpcode() == 56) {
                  FirstFitLocalCombiningAllocator.this.moveResultPseudoInsns.add((NormalSsaInsn)insn);
               } else if (Optimizer.getAdvice().requiresSourcesInOrder(insn.getOriginalRopInsn().getOpcode(), insn.getSources())) {
                  FirstFitLocalCombiningAllocator.this.invokeRangeInsns.add((NormalSsaInsn)insn);
               }
            } else if (insn instanceof PhiInsn) {
               FirstFitLocalCombiningAllocator.this.phiInsns.add((PhiInsn)insn);
            }

         }
      });
   }

   private void addMapping(RegisterSpec ssaSpec, int ropReg) {
      int ssaReg = ssaSpec.getReg();
      if (!this.ssaRegsMapped.get(ssaReg) && this.canMapReg(ssaSpec, ropReg)) {
         int category = ssaSpec.getCategory();
         this.mapper.addMapping(ssaSpec.getReg(), ropReg, category);
         this.ssaRegsMapped.set(ssaReg);
         this.usedRopRegs.set(ropReg, ropReg + category);
      } else {
         throw new RuntimeException("attempt to add invalid register mapping");
      }
   }

   private void adjustAndMapSourceRangeRange(NormalSsaInsn insn) {
      int newRegStart = this.findRangeAndAdjust(insn);
      RegisterSpecList sources = insn.getSources();
      int szSources = sources.size();
      int nextRopReg = newRegStart;

      for(int i = 0; i < szSources; ++i) {
         RegisterSpec source = sources.get(i);
         int sourceReg = source.getReg();
         int category = source.getCategory();
         int curRopReg = nextRopReg;
         nextRopReg += category;
         if (!this.ssaRegsMapped.get(sourceReg)) {
            LocalItem localItem = this.getLocalItemForReg(sourceReg);
            this.addMapping(source, curRopReg);
            if (localItem != null) {
               this.markReserved(curRopReg, category);
               ArrayList<RegisterSpec> similarRegisters = (ArrayList)this.localVariables.get(localItem);
               int szSimilar = similarRegisters.size();

               for(int j = 0; j < szSimilar; ++j) {
                  RegisterSpec similarSpec = (RegisterSpec)similarRegisters.get(j);
                  int similarReg = similarSpec.getReg();
                  if (-1 == sources.indexOfRegister(similarReg)) {
                     this.tryMapReg(similarSpec, curRopReg, category);
                  }
               }
            }
         }
      }

   }

   private int findRangeAndAdjust(NormalSsaInsn insn) {
      RegisterSpecList sources = insn.getSources();
      int szSources = sources.size();
      int[] categoriesForIndex = new int[szSources];
      int rangeLength = 0;

      int maxScore;
      int resultRangeStart;
      for(maxScore = 0; maxScore < szSources; ++maxScore) {
         resultRangeStart = sources.get(maxScore).getCategory();
         categoriesForIndex[maxScore] = resultRangeStart;
         rangeLength += categoriesForIndex[maxScore];
      }

      maxScore = Integer.MIN_VALUE;
      resultRangeStart = -1;
      BitSet resultMovesRequired = null;
      int rangeStartOffset = 0;

      int i;
      for(i = 0; i < szSources; ++i) {
         int ssaCenterReg = sources.get(i).getReg();
         if (i != 0) {
            rangeStartOffset -= categoriesForIndex[i - 1];
         }

         if (this.ssaRegsMapped.get(ssaCenterReg)) {
            int rangeStart = this.mapper.oldToNew(ssaCenterReg) + rangeStartOffset;
            if (rangeStart >= 0 && !this.spansParamRange(rangeStart, rangeLength)) {
               BitSet curMovesRequired = new BitSet(szSources);
               int fitWidth = this.fitPlanForRange(rangeStart, insn, categoriesForIndex, curMovesRequired);
               if (fitWidth >= 0) {
                  int score = fitWidth - curMovesRequired.cardinality();
                  if (score > maxScore) {
                     maxScore = score;
                     resultRangeStart = rangeStart;
                     resultMovesRequired = curMovesRequired;
                  }

                  if (fitWidth == rangeLength) {
                     break;
                  }
               }
            }
         }
      }

      if (resultRangeStart == -1) {
         resultMovesRequired = new BitSet(szSources);
         resultRangeStart = this.findAnyFittingRange(insn, rangeLength, categoriesForIndex, resultMovesRequired);
      }

      for(i = resultMovesRequired.nextSetBit(0); i >= 0; i = resultMovesRequired.nextSetBit(i + 1)) {
         insn.changeOneSource(i, this.insertMoveBefore(insn, sources.get(i)));
      }

      return resultRangeStart;
   }

   private int findAnyFittingRange(NormalSsaInsn insn, int rangeLength, int[] categoriesForIndex, BitSet outMovesRequired) {
      Alignment alignment = FirstFitLocalCombiningAllocator.Alignment.UNSPECIFIED;
      int rangeStart = 0;
      int fitWidth = 0;
      int p64bitsNotAligned = 0;
      int[] var9 = categoriesForIndex;
      int var10 = categoriesForIndex.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         int category = var9[var11];
         if (category == 2) {
            if (isEven(rangeStart)) {
               ++fitWidth;
            } else {
               ++p64bitsNotAligned;
            }

            rangeStart += 2;
         } else {
            ++rangeStart;
         }
      }

      if (p64bitsNotAligned > fitWidth) {
         if (isEven(this.paramRangeEnd)) {
            alignment = FirstFitLocalCombiningAllocator.Alignment.ODD;
         } else {
            alignment = FirstFitLocalCombiningAllocator.Alignment.EVEN;
         }
      } else if (fitWidth > 0) {
         if (isEven(this.paramRangeEnd)) {
            alignment = FirstFitLocalCombiningAllocator.Alignment.EVEN;
         } else {
            alignment = FirstFitLocalCombiningAllocator.Alignment.ODD;
         }
      }

      rangeStart = this.paramRangeEnd;

      while(true) {
         rangeStart = this.findNextUnreservedRopReg(rangeStart, rangeLength, alignment);
         fitWidth = this.fitPlanForRange(rangeStart, insn, categoriesForIndex, outMovesRequired);
         if (fitWidth >= 0) {
            return rangeStart;
         }

         ++rangeStart;
         outMovesRequired.clear();
      }
   }

   private int fitPlanForRange(int ropReg, NormalSsaInsn insn, int[] categoriesForIndex, BitSet outMovesRequired) {
      RegisterSpecList sources = insn.getSources();
      int szSources = sources.size();
      int fitWidth = 0;
      IntSet liveOut = insn.getBlock().getLiveOutRegs();
      RegisterSpecList liveOutSpecs = this.ssaSetToSpecs(liveOut);
      BitSet seen = new BitSet(this.ssaMeth.getRegCount());

      for(int i = 0; i < szSources; ++i) {
         RegisterSpec ssaSpec = sources.get(i);
         int ssaReg = ssaSpec.getReg();
         int category = categoriesForIndex[i];
         if (i != 0) {
            ropReg += categoriesForIndex[i - 1];
         }

         if (this.ssaRegsMapped.get(ssaReg) && this.mapper.oldToNew(ssaReg) == ropReg) {
            fitWidth += category;
         } else {
            if (this.rangeContainsReserved(ropReg, category)) {
               fitWidth = -1;
               break;
            }

            if (!this.ssaRegsMapped.get(ssaReg) && this.canMapReg(ssaSpec, ropReg) && !seen.get(ssaReg)) {
               fitWidth += category;
            } else {
               if (this.mapper.areAnyPinned(liveOutSpecs, ropReg, category) || this.mapper.areAnyPinned(sources, ropReg, category)) {
                  fitWidth = -1;
                  break;
               }

               outMovesRequired.set(i);
            }
         }

         seen.set(ssaReg);
      }

      return fitWidth;
   }

   RegisterSpecList ssaSetToSpecs(IntSet ssaSet) {
      RegisterSpecList result = new RegisterSpecList(ssaSet.elements());
      IntIterator iter = ssaSet.iterator();
      int i = 0;

      while(iter.hasNext()) {
         result.set(i++, this.getDefinitionSpecForSsaReg(iter.next()));
      }

      return result;
   }

   private LocalItem getLocalItemForReg(int ssaReg) {
      Iterator var2 = this.localVariables.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry<LocalItem, ArrayList<RegisterSpec>> entry = (Map.Entry)var2.next();
         Iterator var4 = ((ArrayList)entry.getValue()).iterator();

         while(var4.hasNext()) {
            RegisterSpec spec = (RegisterSpec)var4.next();
            if (spec.getReg() == ssaReg) {
               return (LocalItem)entry.getKey();
            }
         }
      }

      return null;
   }

   private void processPhiInsn(PhiInsn insn) {
      RegisterSpec result = insn.getResult();
      int resultReg = result.getReg();
      int category = result.getCategory();
      RegisterSpecList sources = insn.getSources();
      int sourcesSize = sources.size();
      ArrayList<RegisterSpec> ssaRegs = new ArrayList();
      Multiset mapSet = new Multiset(sourcesSize + 1);
      if (this.ssaRegsMapped.get(resultReg)) {
         mapSet.add(this.mapper.oldToNew(resultReg));
      } else {
         ssaRegs.add(result);
      }

      int mapReg;
      for(mapReg = 0; mapReg < sourcesSize; ++mapReg) {
         RegisterSpec source = sources.get(mapReg);
         SsaInsn def = this.ssaMeth.getDefinitionForRegister(source.getReg());
         RegisterSpec sourceDef = def.getResult();
         int sourceReg = sourceDef.getReg();
         if (this.ssaRegsMapped.get(sourceReg)) {
            mapSet.add(this.mapper.oldToNew(sourceReg));
         } else {
            ssaRegs.add(sourceDef);
         }
      }

      for(mapReg = 0; mapReg < mapSet.getSize(); ++mapReg) {
         int maxReg = mapSet.getAndRemoveHighestCount();
         this.tryMapRegs(ssaRegs, maxReg, category, false);
      }

      for(mapReg = this.findNextUnreservedRopReg(this.paramRangeEnd, category); !this.tryMapRegs(ssaRegs, mapReg, category, false); mapReg = this.findNextUnreservedRopReg(mapReg + 1, category)) {
      }

   }

   private static boolean isEven(int regNumger) {
      return (regNumger & 1) == 0;
   }

   private static class Multiset {
      private final int[] reg;
      private final int[] count;
      private int size;

      public Multiset(int maxSize) {
         this.reg = new int[maxSize];
         this.count = new int[maxSize];
         this.size = 0;
      }

      public void add(int element) {
         for(int i = 0; i < this.size; ++i) {
            if (this.reg[i] == element) {
               int var10002 = this.count[i]++;
               return;
            }
         }

         this.reg[this.size] = element;
         this.count[this.size] = 1;
         ++this.size;
      }

      public int getAndRemoveHighestCount() {
         int maxIndex = -1;
         int maxReg = -1;
         int maxCount = 0;

         for(int i = 0; i < this.size; ++i) {
            if (maxCount < this.count[i]) {
               maxIndex = i;
               maxReg = this.reg[i];
               maxCount = this.count[i];
            }
         }

         this.count[maxIndex] = 0;
         return maxReg;
      }

      public int getSize() {
         return this.size;
      }
   }

   private static enum Alignment {
      EVEN {
         int nextClearBit(BitSet bitSet, int startIdx) {
            int bitNumber;
            for(bitNumber = bitSet.nextClearBit(startIdx); !FirstFitLocalCombiningAllocator.isEven(bitNumber); bitNumber = bitSet.nextClearBit(bitNumber + 1)) {
            }

            return bitNumber;
         }
      },
      ODD {
         int nextClearBit(BitSet bitSet, int startIdx) {
            int bitNumber;
            for(bitNumber = bitSet.nextClearBit(startIdx); FirstFitLocalCombiningAllocator.isEven(bitNumber); bitNumber = bitSet.nextClearBit(bitNumber + 1)) {
            }

            return bitNumber;
         }
      },
      UNSPECIFIED {
         int nextClearBit(BitSet bitSet, int startIdx) {
            return bitSet.nextClearBit(startIdx);
         }
      };

      private Alignment() {
      }

      abstract int nextClearBit(BitSet var1, int var2);

      // $FF: synthetic method
      Alignment(Object x2) {
         this();
      }
   }
}
