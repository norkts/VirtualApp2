package com.android.dx;

import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;

public enum BinaryOp {
   ADD {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opAdd(types);
      }
   },
   SUBTRACT {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opSub(types);
      }
   },
   MULTIPLY {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opMul(types);
      }
   },
   DIVIDE {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opDiv(types);
      }
   },
   REMAINDER {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opRem(types);
      }
   },
   AND {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opAnd(types);
      }
   },
   OR {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opOr(types);
      }
   },
   XOR {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opXor(types);
      }
   },
   SHIFT_LEFT {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opShl(types);
      }
   },
   SHIFT_RIGHT {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opShr(types);
      }
   },
   UNSIGNED_SHIFT_RIGHT {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opUshr(types);
      }
   };

   private BinaryOp() {
   }

   abstract Rop rop(com.android.dx.rop.type.TypeList var1);

   // $FF: synthetic method
   BinaryOp(Object x2) {
      this();
   }
}
