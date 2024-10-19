package com.android.dx;

import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;

public enum UnaryOp {
   NOT {
      Rop rop(TypeId<?> type) {
         return Rops.opNot(type.ropType);
      }
   },
   NEGATE {
      Rop rop(TypeId<?> type) {
         return Rops.opNeg(type.ropType);
      }
   };

   private UnaryOp() {
   }

   abstract Rop rop(TypeId<?> var1);

   // $FF: synthetic method
   UnaryOp(Object x2) {
      this();
   }
}
