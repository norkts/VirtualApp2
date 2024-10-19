package com.android.dx.dex.code;

import com.android.dex.DexException;
import com.android.dx.dex.DexOptions;
import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.RegisterSpecSet;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstMemberRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.ssa.BasicRegisterMapper;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;

public final class OutputFinisher {
   private final DexOptions dexOptions;
   private final int unreservedRegCount;
   private ArrayList<DalvInsn> insns;
   private boolean hasAnyPositionInfo;
   private boolean hasAnyLocalInfo;
   private int reservedCount;
   private int reservedParameterCount;
   private final int paramSize;

   public OutputFinisher(DexOptions dexOptions, int initialCapacity, int regCount, int paramSize) {
      this.dexOptions = dexOptions;
      this.unreservedRegCount = regCount;
      this.insns = new ArrayList(initialCapacity);
      this.reservedCount = -1;
      this.hasAnyPositionInfo = false;
      this.hasAnyLocalInfo = false;
      this.paramSize = paramSize;
   }

   public boolean hasAnyPositionInfo() {
      return this.hasAnyPositionInfo;
   }

   public boolean hasAnyLocalInfo() {
      return this.hasAnyLocalInfo;
   }

   private static boolean hasLocalInfo(DalvInsn insn) {
      if (insn instanceof LocalSnapshot) {
         RegisterSpecSet specs = ((LocalSnapshot)insn).getLocals();
         int size = specs.size();

         for(int i = 0; i < size; ++i) {
            if (hasLocalInfo(specs.get(i))) {
               return true;
            }
         }
      } else if (insn instanceof LocalStart) {
         RegisterSpec spec = ((LocalStart)insn).getLocal();
         if (hasLocalInfo(spec)) {
            return true;
         }
      }

      return false;
   }

   private static boolean hasLocalInfo(RegisterSpec spec) {
      return spec != null && spec.getLocalItem().getName() != null;
   }

   public HashSet<Constant> getAllConstants() {
      HashSet<Constant> result = new HashSet(20);
      Iterator var2 = this.insns.iterator();

      while(var2.hasNext()) {
         DalvInsn insn = (DalvInsn)var2.next();
         addConstants(result, insn);
      }

      return result;
   }

   private static void addConstants(HashSet<Constant> result, DalvInsn insn) {
      if (insn instanceof CstInsn) {
         Constant cst = ((CstInsn)insn).getConstant();
         result.add(cst);
      } else {
         int size;
         if (insn instanceof MultiCstInsn) {
            MultiCstInsn m = (MultiCstInsn)insn;

            for(size = 0; size < m.getNumberOfConstants(); ++size) {
               result.add(m.getConstant(size));
            }
         } else if (insn instanceof LocalSnapshot) {
            RegisterSpecSet specs = ((LocalSnapshot)insn).getLocals();
            size = specs.size();

            for(int i = 0; i < size; ++i) {
               addConstants(result, specs.get(i));
            }
         } else if (insn instanceof LocalStart) {
            RegisterSpec spec = ((LocalStart)insn).getLocal();
            addConstants(result, spec);
         }
      }

   }

   private static void addConstants(HashSet<Constant> result, RegisterSpec spec) {
      if (spec != null) {
         LocalItem local = spec.getLocalItem();
         CstString name = local.getName();
         CstString signature = local.getSignature();
         Type type = spec.getType();
         if (type != Type.KNOWN_NULL) {
            result.add(CstType.intern(type));
         } else {
            result.add(CstType.intern(Type.OBJECT));
         }

         if (name != null) {
            result.add(name);
         }

         if (signature != null) {
            result.add(signature);
         }

      }
   }

   public void add(DalvInsn insn) {
      this.insns.add(insn);
      this.updateInfo(insn);
   }

   public void insert(int at, DalvInsn insn) {
      this.insns.add(at, insn);
      this.updateInfo(insn);
   }

   private void updateInfo(DalvInsn insn) {
      if (!this.hasAnyPositionInfo) {
         SourcePosition pos = insn.getPosition();
         if (pos.getLine() >= 0) {
            this.hasAnyPositionInfo = true;
         }
      }

      if (!this.hasAnyLocalInfo && hasLocalInfo(insn)) {
         this.hasAnyLocalInfo = true;
      }

   }

   public void reverseBranch(int which, CodeAddress newTarget) {
      int size = this.insns.size();
      int index = size - which - 1;

      TargetInsn targetInsn;
      try {
         targetInsn = (TargetInsn)this.insns.get(index);
      } catch (IndexOutOfBoundsException var7) {
         throw new IllegalArgumentException("too few instructions");
      } catch (ClassCastException var8) {
         throw new IllegalArgumentException("non-reversible instruction");
      }

      this.insns.set(index, targetInsn.withNewTargetAndReversed(newTarget));
   }

   public void assignIndices(DalvCode.AssignIndicesCallback callback) {
      Iterator var2 = this.insns.iterator();

      while(var2.hasNext()) {
         DalvInsn insn = (DalvInsn)var2.next();
         if (insn instanceof CstInsn) {
            assignIndices((CstInsn)insn, callback);
         } else if (insn instanceof MultiCstInsn) {
            assignIndices((MultiCstInsn)insn, callback);
         }
      }

   }

   private static void assignIndices(CstInsn insn, DalvCode.AssignIndicesCallback callback) {
      Constant cst = insn.getConstant();
      int index = callback.getIndex(cst);
      if (index >= 0) {
         insn.setIndex(index);
      }

      if (cst instanceof CstMemberRef) {
         CstMemberRef member = (CstMemberRef)cst;
         CstType definer = member.getDefiningClass();
         index = callback.getIndex(definer);
         if (index >= 0) {
            insn.setClassIndex(index);
         }
      }

   }

   private static void assignIndices(MultiCstInsn insn, DalvCode.AssignIndicesCallback callback) {
      for(int i = 0; i < insn.getNumberOfConstants(); ++i) {
         Constant cst = insn.getConstant(i);
         int index = callback.getIndex(cst);
         insn.setIndex(i, index);
         if (cst instanceof CstMemberRef) {
            CstMemberRef member = (CstMemberRef)cst;
            CstType definer = member.getDefiningClass();
            index = callback.getIndex(definer);
            insn.setClassIndex(index);
         }
      }

   }

   public DalvInsnList finishProcessingAndGetList() {
      if (this.reservedCount >= 0) {
         throw new UnsupportedOperationException("already processed");
      } else {
         Dop[] opcodes = this.makeOpcodesArray();
         this.reserveRegisters(opcodes);
         if (this.dexOptions.ALIGN_64BIT_REGS_IN_OUTPUT_FINISHER) {
            this.align64bits(opcodes);
         }

         this.massageInstructions(opcodes);
         this.assignAddressesAndFixBranches();
         return DalvInsnList.makeImmutable(this.insns, this.reservedCount + this.unreservedRegCount + this.reservedParameterCount);
      }
   }

   private Dop[] makeOpcodesArray() {
      int size = this.insns.size();
      Dop[] result = new Dop[size];

      for(int i = 0; i < size; ++i) {
         DalvInsn insn = (DalvInsn)this.insns.get(i);
         result[i] = insn.getOpcode();
      }

      return result;
   }

   private boolean reserveRegisters(Dop[] opcodes) {
      boolean reservedCountExpanded = false;
      int oldReservedCount = this.reservedCount < 0 ? 0 : this.reservedCount;

      while(true) {
         int newReservedCount = this.calculateReservedCount(opcodes);
         if (oldReservedCount >= newReservedCount) {
            this.reservedCount = oldReservedCount;
            return reservedCountExpanded;
         }

         reservedCountExpanded = true;
         int reservedDifference = newReservedCount - oldReservedCount;
         int size = this.insns.size();

         for(int i = 0; i < size; ++i) {
            DalvInsn insn = (DalvInsn)this.insns.get(i);
            if (!(insn instanceof CodeAddress)) {
               this.insns.set(i, insn.withRegisterOffset(reservedDifference));
            }
         }

         oldReservedCount = newReservedCount;
      }
   }

   private int calculateReservedCount(Dop[] opcodes) {
      int size = this.insns.size();
      int newReservedCount = this.reservedCount;

      for(int i = 0; i < size; ++i) {
         DalvInsn insn = (DalvInsn)this.insns.get(i);
         Dop originalOpcode = opcodes[i];
         Dop newOpcode = this.findOpcodeForInsn(insn, originalOpcode);
         if (newOpcode == null) {
            Dop expandedOp = this.findExpandedOpcodeForInsn(insn);
            BitSet compatRegs = expandedOp.getFormat().compatibleRegs(insn);
            int reserve = insn.getMinimumRegisterRequirement(compatRegs);
            if (reserve > newReservedCount) {
               newReservedCount = reserve;
            }
         } else if (originalOpcode == newOpcode) {
            continue;
         }

         opcodes[i] = newOpcode;
      }

      return newReservedCount;
   }

   private Dop findOpcodeForInsn(DalvInsn insn, Dop guess) {
      while(guess != null && (!guess.getFormat().isCompatible(insn) || this.dexOptions.forceJumbo && guess.getOpcode() == 26)) {
         guess = Dops.getNextOrNull(guess, this.dexOptions);
      }

      return guess;
   }

   private Dop findExpandedOpcodeForInsn(DalvInsn insn) {
      Dop result = this.findOpcodeForInsn(insn.getLowRegVersion(), insn.getOpcode());
      if (result == null) {
         throw new DexException("No expanded opcode for " + insn);
      } else {
         return result;
      }
   }

   private void massageInstructions(Dop[] opcodes) {
      if (this.reservedCount == 0) {
         int size = this.insns.size();

         for(int i = 0; i < size; ++i) {
            DalvInsn insn = (DalvInsn)this.insns.get(i);
            Dop originalOpcode = insn.getOpcode();
            Dop currentOpcode = opcodes[i];
            if (originalOpcode != currentOpcode) {
               this.insns.set(i, insn.withOpcode(currentOpcode));
            }
         }
      } else {
         this.insns = this.performExpansion(opcodes);
      }

   }

   private ArrayList<DalvInsn> performExpansion(Dop[] opcodes) {
      int size = this.insns.size();
      ArrayList<DalvInsn> result = new ArrayList(size * 2);
      ArrayList<CodeAddress> closelyBoundAddresses = new ArrayList();

      for(int i = 0; i < size; ++i) {
         DalvInsn insn = (DalvInsn)this.insns.get(i);
         Dop originalOpcode = insn.getOpcode();
         Dop currentOpcode = opcodes[i];
         DalvInsn prefix;
         DalvInsn suffix;
         if (currentOpcode != null) {
            prefix = null;
            suffix = null;
         } else {
            currentOpcode = this.findExpandedOpcodeForInsn(insn);
            BitSet compatRegs = currentOpcode.getFormat().compatibleRegs(insn);
            prefix = insn.expandedPrefix(compatRegs);
            suffix = insn.expandedSuffix(compatRegs);
            insn = insn.expandedVersion(compatRegs);
         }

         if (insn instanceof CodeAddress && ((CodeAddress)insn).getBindsClosely()) {
            closelyBoundAddresses.add((CodeAddress)insn);
         } else {
            if (prefix != null) {
               result.add(prefix);
            }

            if (!(insn instanceof ZeroSizeInsn) && closelyBoundAddresses.size() > 0) {
               Iterator var13 = closelyBoundAddresses.iterator();

               while(var13.hasNext()) {
                  CodeAddress codeAddress = (CodeAddress)var13.next();
                  result.add(codeAddress);
               }

               closelyBoundAddresses.clear();
            }

            if (currentOpcode != originalOpcode) {
               insn = insn.withOpcode(currentOpcode);
            }

            result.add(insn);
            if (suffix != null) {
               result.add(suffix);
            }
         }
      }

      return result;
   }

   private void assignAddressesAndFixBranches() {
      do {
         this.assignAddresses();
      } while(this.fixBranches());

   }

   private void assignAddresses() {
      int address = 0;
      int size = this.insns.size();

      for(int i = 0; i < size; ++i) {
         DalvInsn insn = (DalvInsn)this.insns.get(i);
         insn.setAddress(address);
         address += insn.codeSize();
      }

   }

   private boolean fixBranches() {
      int size = this.insns.size();
      boolean anyFixed = false;

      for(int i = 0; i < size; ++i) {
         DalvInsn insn = (DalvInsn)this.insns.get(i);
         if (insn instanceof TargetInsn) {
            Dop opcode = insn.getOpcode();
            TargetInsn target = (TargetInsn)insn;
            if (!opcode.getFormat().branchFits(target)) {
               if (opcode.getFamily() == 40) {
                  opcode = this.findOpcodeForInsn(insn, opcode);
                  if (opcode == null) {
                     throw new UnsupportedOperationException("method too long");
                  }

                  this.insns.set(i, insn.withOpcode(opcode));
               } else {
                  CodeAddress newTarget;
                  try {
                     newTarget = (CodeAddress)this.insns.get(i + 1);
                  } catch (IndexOutOfBoundsException var9) {
                     throw new IllegalStateException("unpaired TargetInsn (dangling)");
                  } catch (ClassCastException var10) {
                     throw new IllegalStateException("unpaired TargetInsn");
                  }

                  TargetInsn gotoInsn = new TargetInsn(Dops.GOTO, target.getPosition(), RegisterSpecList.EMPTY, target.getTarget());
                  this.insns.set(i, gotoInsn);
                  this.insns.add(i, target.withNewTargetAndReversed(newTarget));
                  ++size;
                  ++i;
               }

               anyFixed = true;
            }
         }
      }

      return anyFixed;
   }

   private void align64bits(Dop[] opcodes) {
      do {
         int notAligned64bitRegAccess = 0;
         int aligned64bitRegAccess = 0;
         int notAligned64bitParamAccess = 0;
         int aligned64bitParamAccess = 0;
         int lastParameter = this.unreservedRegCount + this.reservedCount + this.reservedParameterCount;
         int firstParameter = lastParameter - this.paramSize;
         Iterator var8 = this.insns.iterator();

         while(var8.hasNext()) {
            DalvInsn insn = (DalvInsn)var8.next();
            RegisterSpecList regs = insn.getRegisters();

            for(int usedRegIdx = 0; usedRegIdx < regs.size(); ++usedRegIdx) {
               RegisterSpec reg = regs.get(usedRegIdx);
               if (reg.isCategory2()) {
                  boolean isParameter = reg.getReg() >= firstParameter;
                  if (reg.isEvenRegister()) {
                     if (isParameter) {
                        ++aligned64bitParamAccess;
                     } else {
                        ++aligned64bitRegAccess;
                     }
                  } else if (isParameter) {
                     ++notAligned64bitParamAccess;
                  } else {
                     ++notAligned64bitRegAccess;
                  }
               }
            }
         }

         if (notAligned64bitParamAccess > aligned64bitParamAccess && notAligned64bitRegAccess > aligned64bitRegAccess) {
            this.addReservedRegisters(1);
         } else if (notAligned64bitParamAccess > aligned64bitParamAccess) {
            this.addReservedParameters(1);
         } else {
            if (notAligned64bitRegAccess <= aligned64bitRegAccess) {
               return;
            }

            this.addReservedRegisters(1);
            if (this.paramSize != 0 && aligned64bitParamAccess > notAligned64bitParamAccess) {
               this.addReservedParameters(1);
            }
         }
      } while(this.reserveRegisters(opcodes));

   }

   private void addReservedParameters(int delta) {
      this.shiftParameters(delta);
      this.reservedParameterCount += delta;
   }

   private void addReservedRegisters(int delta) {
      this.shiftAllRegisters(delta);
      this.reservedCount += delta;
   }

   private void shiftAllRegisters(int delta) {
      int insnSize = this.insns.size();

      for(int i = 0; i < insnSize; ++i) {
         DalvInsn insn = (DalvInsn)this.insns.get(i);
         if (!(insn instanceof CodeAddress)) {
            this.insns.set(i, insn.withRegisterOffset(delta));
         }
      }

   }

   private void shiftParameters(int delta) {
      int insnSize = this.insns.size();
      int lastParameter = this.unreservedRegCount + this.reservedCount + this.reservedParameterCount;
      int firstParameter = lastParameter - this.paramSize;
      BasicRegisterMapper mapper = new BasicRegisterMapper(lastParameter);

      int i;
      for(i = 0; i < lastParameter; ++i) {
         if (i >= firstParameter) {
            mapper.addMapping(i, i + delta, 1);
         } else {
            mapper.addMapping(i, i, 1);
         }
      }

      for(i = 0; i < insnSize; ++i) {
         DalvInsn insn = (DalvInsn)this.insns.get(i);
         if (!(insn instanceof CodeAddress)) {
            this.insns.set(i, insn.withMapper(mapper));
         }
      }

   }
}
