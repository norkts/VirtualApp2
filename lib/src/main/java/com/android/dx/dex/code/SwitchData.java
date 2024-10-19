package com.android.dx.dex.code;

import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import com.android.dx.util.IntList;

public final class SwitchData extends VariableSizeInsn {
   private final CodeAddress user;
   private final IntList cases;
   private final CodeAddress[] targets;
   private final boolean packed;

   public SwitchData(SourcePosition position, CodeAddress user, IntList cases, CodeAddress[] targets) {
      super(position, RegisterSpecList.EMPTY);
      if (user == null) {
         throw new NullPointerException("user == null");
      } else if (cases == null) {
         throw new NullPointerException("cases == null");
      } else if (targets == null) {
         throw new NullPointerException("targets == null");
      } else {
         int sz = cases.size();
         if (sz != targets.length) {
            throw new IllegalArgumentException("cases / targets mismatch");
         } else if (sz > 65535) {
            throw new IllegalArgumentException("too many cases");
         } else {
            this.user = user;
            this.cases = cases;
            this.targets = targets;
            this.packed = shouldPack(cases);
         }
      }
   }

   public int codeSize() {
      return this.packed ? (int)packedCodeSize(this.cases) : (int)sparseCodeSize(this.cases);
   }

   public void writeTo(AnnotatedOutput out) {
      int baseAddress = this.user.getAddress();
      int defaultTarget = Dops.PACKED_SWITCH.getFormat().codeSize();
      int sz = this.targets.length;
      int i;
      int relTarget;
      if (this.packed) {
         i = sz == 0 ? 0 : this.cases.get(0);
         relTarget = sz == 0 ? 0 : this.cases.get(sz - 1);
         int outSz = relTarget - i + 1;
         out.writeShort(256);
         out.writeShort(outSz);
         out.writeInt(i);
         int caseAt = 0;

         for(i = 0; i < outSz; ++i) {
            int outCase = i + i;
            int oneCase = this.cases.get(caseAt);
            if (oneCase > outCase) {
               relTarget = defaultTarget;
            } else {
               relTarget = this.targets[caseAt].getAddress() - baseAddress;
               ++caseAt;
            }

            out.writeInt(relTarget);
         }
      } else {
         out.writeShort(512);
         out.writeShort(sz);

         for(i = 0; i < sz; ++i) {
            out.writeInt(this.cases.get(i));
         }

         for(i = 0; i < sz; ++i) {
            relTarget = this.targets[i].getAddress() - baseAddress;
            out.writeInt(relTarget);
         }
      }

   }

   public DalvInsn withRegisters(RegisterSpecList registers) {
      return new SwitchData(this.getPosition(), this.user, this.cases, this.targets);
   }

   public boolean isPacked() {
      return this.packed;
   }

   protected String argString() {
      StringBuilder sb = new StringBuilder(100);
      int sz = this.targets.length;

      for(int i = 0; i < sz; ++i) {
         sb.append("\n    ");
         sb.append(this.cases.get(i));
         sb.append(": ");
         sb.append(this.targets[i]);
      }

      return sb.toString();
   }

   protected String listingString0(boolean noteIndices) {
      int baseAddress = this.user.getAddress();
      StringBuilder sb = new StringBuilder(100);
      int sz = this.targets.length;
      sb.append(this.packed ? "packed" : "sparse");
      sb.append("-switch-payload // for switch @ ");
      sb.append(Hex.u2(baseAddress));

      for(int i = 0; i < sz; ++i) {
         int absTarget = this.targets[i].getAddress();
         int relTarget = absTarget - baseAddress;
         sb.append("\n  ");
         sb.append(this.cases.get(i));
         sb.append(": ");
         sb.append(Hex.u4(absTarget));
         sb.append(" // ");
         sb.append(Hex.s4(relTarget));
      }

      return sb.toString();
   }

   private static long packedCodeSize(IntList cases) {
      int sz = cases.size();
      long low = (long)cases.get(0);
      long high = (long)cases.get(sz - 1);
      long result = (high - low + 1L) * 2L + 4L;
      return result <= 2147483647L ? result : -1L;
   }

   private static long sparseCodeSize(IntList cases) {
      int sz = cases.size();
      return (long)sz * 4L + 2L;
   }

   private static boolean shouldPack(IntList cases) {
      int sz = cases.size();
      if (sz < 2) {
         return true;
      } else {
         long packedSize = packedCodeSize(cases);
         long sparseSize = sparseCodeSize(cases);
         return packedSize >= 0L && packedSize <= sparseSize * 5L / 4L;
      }
   }
}
