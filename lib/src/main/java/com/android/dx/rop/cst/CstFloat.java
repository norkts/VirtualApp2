package com.android.dx.rop.cst;

import com.android.dx.rop.type.Type;
import com.android.dx.util.Hex;

public final class CstFloat extends CstLiteral32 {
   public static final CstFloat VALUE_0 = make(Float.floatToIntBits(0.0F));
   public static final CstFloat VALUE_1 = make(Float.floatToIntBits(1.0F));
   public static final CstFloat VALUE_2 = make(Float.floatToIntBits(2.0F));

   public static CstFloat make(int bits) {
      return new CstFloat(bits);
   }

   private CstFloat(int bits) {
      super(bits);
   }

   public String toString() {
      int bits = this.getIntBits();
      return "float{0x" + Hex.u4(bits) + " / " + Float.intBitsToFloat(bits) + '}';
   }

   public Type getType() {
      return Type.FLOAT;
   }

   public String typeName() {
      return "float";
   }

   public String toHuman() {
      return Float.toString(Float.intBitsToFloat(this.getIntBits()));
   }

   public float getValue() {
      return Float.intBitsToFloat(this.getIntBits());
   }
}
