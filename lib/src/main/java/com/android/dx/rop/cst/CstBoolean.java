package com.android.dx.rop.cst;

import com.android.dx.rop.type.Type;

public final class CstBoolean extends CstLiteral32 {
   public static final CstBoolean VALUE_FALSE = new CstBoolean(false);
   public static final CstBoolean VALUE_TRUE = new CstBoolean(true);

   public static CstBoolean make(boolean value) {
      return value ? VALUE_TRUE : VALUE_FALSE;
   }

   public static CstBoolean make(int value) {
      if (value == 0) {
         return VALUE_FALSE;
      } else if (value == 1) {
         return VALUE_TRUE;
      } else {
         throw new IllegalArgumentException("bogus value: " + value);
      }
   }

   private CstBoolean(boolean value) {
      super(value ? 1 : 0);
   }

   public String toString() {
      return this.getValue() ? "boolean{true}" : "boolean{false}";
   }

   public Type getType() {
      return Type.BOOLEAN;
   }

   public String typeName() {
      return "boolean";
   }

   public String toHuman() {
      return this.getValue() ? "true" : "false";
   }

   public boolean getValue() {
      return this.getIntBits() != 0;
   }
}
