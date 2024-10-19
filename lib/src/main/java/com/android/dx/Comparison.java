package com.android.dx;

import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;

public enum Comparison {
   LT {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opIfLt(types);
      }
   },
   LE {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opIfLe(types);
      }
   },
   EQ {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opIfEq(types);
      }
   },
   GE {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opIfGe(types);
      }
   },
   GT {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opIfGt(types);
      }
   },
   NE {
      Rop rop(com.android.dx.rop.type.TypeList types) {
         return Rops.opIfNe(types);
      }
   };

   private Comparison() {
   }

   abstract Rop rop(com.android.dx.rop.type.TypeList var1);

   // $FF: synthetic method
   Comparison(Object x2) {
      this();
   }
}
