package com.android.dx.cf.code;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstInvokeDynamic;
import com.android.dx.rop.cst.CstMemberRef;
import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.util.Bits;
import com.android.dx.util.IntList;
import java.util.ArrayList;

public final class BasicBlocker implements BytecodeArray.Visitor {
   private final ConcreteMethod method;
   private final int[] workSet;
   private final int[] liveSet;
   private final int[] blockSet;
   private final IntList[] targetLists;
   private final ByteCatchList[] catchLists;
   private int previousOffset;

   public static ByteBlockList identifyBlocks(ConcreteMethod method) {
      BasicBlocker bb = new BasicBlocker(method);
      bb.doit();
      return bb.getBlockList();
   }

   private BasicBlocker(ConcreteMethod method) {
      if (method == null) {
         throw new NullPointerException("method == null");
      } else {
         this.method = method;
         int sz = method.getCode().size() + 1;
         this.workSet = Bits.makeBitSet(sz);
         this.liveSet = Bits.makeBitSet(sz);
         this.blockSet = Bits.makeBitSet(sz);
         this.targetLists = new IntList[sz];
         this.catchLists = new ByteCatchList[sz];
         this.previousOffset = -1;
      }
   }

   public void visitInvalid(int opcode, int offset, int length) {
      this.visitCommon(offset, length, true);
   }

   public void visitNoArgs(int opcode, int offset, int length, Type type) {
      switch (opcode) {
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 190:
         case 194:
         case 195:
            this.visitCommon(offset, length, true);
            this.visitThrowing(offset, length, true);
            break;
         case 108:
         case 112:
            this.visitCommon(offset, length, true);
            if (type == Type.INT || type == Type.LONG) {
               this.visitThrowing(offset, length, true);
            }
            break;
         case 172:
         case 177:
            this.visitCommon(offset, length, false);
            this.targetLists[offset] = IntList.EMPTY;
            break;
         case 191:
            this.visitCommon(offset, length, false);
            this.visitThrowing(offset, length, false);
            break;
         default:
            this.visitCommon(offset, length, true);
      }

   }

   public void visitLocal(int opcode, int offset, int length, int idx, Type type, int value) {
      if (opcode == 169) {
         this.visitCommon(offset, length, false);
         this.targetLists[offset] = IntList.EMPTY;
      } else {
         this.visitCommon(offset, length, true);
      }

   }

   public void visitConstant(int opcode, int offset, int length, Constant cst, int value) {
      this.visitCommon(offset, length, true);
      if (cst instanceof CstMemberRef || cst instanceof CstType || cst instanceof CstString || cst instanceof CstInvokeDynamic || cst instanceof CstMethodHandle || cst instanceof CstProtoRef) {
         this.visitThrowing(offset, length, true);
      }

   }

   public void visitBranch(int opcode, int offset, int length, int target) {
      switch (opcode) {
         case 167:
            this.visitCommon(offset, length, false);
            this.targetLists[offset] = IntList.makeImmutable(target);
            break;
         case 168:
            this.addWorkIfNecessary(offset, true);
         default:
            int next = offset + length;
            this.visitCommon(offset, length, true);
            this.addWorkIfNecessary(next, true);
            this.targetLists[offset] = IntList.makeImmutable(next, target);
      }

      this.addWorkIfNecessary(target, true);
   }

   public void visitSwitch(int opcode, int offset, int length, SwitchList cases, int padding) {
      this.visitCommon(offset, length, false);
      this.addWorkIfNecessary(cases.getDefaultTarget(), true);
      int sz = cases.size();

      for(int i = 0; i < sz; ++i) {
         this.addWorkIfNecessary(cases.getTarget(i), true);
      }

      this.targetLists[offset] = cases.getTargets();
   }

   public void visitNewarray(int offset, int length, CstType type, ArrayList<Constant> intVals) {
      this.visitCommon(offset, length, true);
      this.visitThrowing(offset, length, true);
   }

   private ByteBlockList getBlockList() {
      BytecodeArray bytes = this.method.getCode();
      ByteBlock[] bbs = new ByteBlock[bytes.size()];
      int count = 0;
      int at = 0;

      while(true) {
         int next = Bits.findFirst(this.blockSet, at + 1);
         if (next < 0) {
            ByteBlockList result = new ByteBlockList(count);

            for(next = 0; next < count; ++next) {
               result.set(next, bbs[next]);
            }

            return result;
         }

         if (Bits.get(this.liveSet, at)) {
            IntList targets = null;
            int targetsAt = -1;

            for(int i = next - 1; i >= at; --i) {
               targets = this.targetLists[i];
               if (targets != null) {
                  targetsAt = i;
                  break;
               }
            }

            ByteCatchList blockCatches;
            if (targets == null) {
               targets = IntList.makeImmutable(next);
               blockCatches = ByteCatchList.EMPTY;
            } else {
               blockCatches = this.catchLists[targetsAt];
               if (blockCatches == null) {
                  blockCatches = ByteCatchList.EMPTY;
               }
            }

            bbs[count] = new ByteBlock(at, at, next, targets, blockCatches);
            ++count;
         }

         at = next;
      }
   }

   private void doit() {
      BytecodeArray bytes = this.method.getCode();
      ByteCatchList catches = this.method.getCatches();
      int catchSz = catches.size();
      Bits.set(this.workSet, 0);
      Bits.set(this.blockSet, 0);

      while(!Bits.isEmpty(this.workSet)) {
         try {
            bytes.processWorkSet(this.workSet, this);
         } catch (IllegalArgumentException var8) {
            IllegalArgumentException ex = var8;
            throw new SimException("flow of control falls off end of method", ex);
         }

         for(int i = 0; i < catchSz; ++i) {
            ByteCatchList.Item item = catches.get(i);
            int start = item.getStartPc();
            int end = item.getEndPc();
            if (Bits.anyInRange(this.liveSet, start, end)) {
               Bits.set(this.blockSet, start);
               Bits.set(this.blockSet, end);
               this.addWorkIfNecessary(item.getHandlerPc(), true);
            }
         }
      }

   }

   private void addWorkIfNecessary(int offset, boolean blockStart) {
      if (!Bits.get(this.liveSet, offset)) {
         Bits.set(this.workSet, offset);
      }

      if (blockStart) {
         Bits.set(this.blockSet, offset);
      }

   }

   private void visitCommon(int offset, int length, boolean nextIsLive) {
      Bits.set(this.liveSet, offset);
      if (nextIsLive) {
         this.addWorkIfNecessary(offset + length, false);
      } else {
         Bits.set(this.blockSet, offset + length);
      }

   }

   private void visitThrowing(int offset, int length, boolean nextIsLive) {
      int next = offset + length;
      if (nextIsLive) {
         this.addWorkIfNecessary(next, true);
      }

      ByteCatchList catches = this.method.getCatches().listFor(offset);
      this.catchLists[offset] = catches;
      this.targetLists[offset] = catches.toTargetList(nextIsLive ? next : -1);
   }

   public void setPreviousOffset(int offset) {
      this.previousOffset = offset;
   }

   public int getPreviousOffset() {
      return this.previousOffset;
   }
}
