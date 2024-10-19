package com.android.dx.rop.cst;

import com.android.dx.rop.type.Type;

public final class CstFieldRef extends CstMemberRef {
   public static CstFieldRef forPrimitiveType(Type primitiveType) {
      return new CstFieldRef(CstType.forBoxedPrimitiveType(primitiveType), CstNat.PRIMITIVE_TYPE_NAT);
   }

   public CstFieldRef(CstType definingClass, CstNat nat) {
      super(definingClass, nat);
   }

   public String typeName() {
      return "field";
   }

   public Type getType() {
      return this.getNat().getFieldType();
   }

   protected int compareTo0(Constant other) {
      int cmp = super.compareTo0(other);
      if (cmp != 0) {
         return cmp;
      } else {
         CstFieldRef otherField = (CstFieldRef)other;
         CstString thisDescriptor = this.getNat().getDescriptor();
         CstString otherDescriptor = otherField.getNat().getDescriptor();
         return thisDescriptor.compareTo(otherDescriptor);
      }
   }
}
