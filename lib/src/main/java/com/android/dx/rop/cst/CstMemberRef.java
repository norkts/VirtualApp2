package com.android.dx.rop.cst;

public abstract class CstMemberRef extends TypedConstant {
   private final CstType definingClass;
   private final CstNat nat;

   CstMemberRef(CstType definingClass, CstNat nat) {
      if (definingClass == null) {
         throw new NullPointerException("definingClass == null");
      } else if (nat == null) {
         throw new NullPointerException("nat == null");
      } else {
         this.definingClass = definingClass;
         this.nat = nat;
      }
   }

   public final boolean equals(Object other) {
      if (other != null && this.getClass() == other.getClass()) {
         CstMemberRef otherRef = (CstMemberRef)other;
         return this.definingClass.equals(otherRef.definingClass) && this.nat.equals(otherRef.nat);
      } else {
         return false;
      }
   }

   public final int hashCode() {
      return this.definingClass.hashCode() * 31 ^ this.nat.hashCode();
   }

   protected int compareTo0(Constant other) {
      CstMemberRef otherMember = (CstMemberRef)other;
      int cmp = this.definingClass.compareTo(otherMember.definingClass);
      if (cmp != 0) {
         return cmp;
      } else {
         CstString thisName = this.nat.getName();
         CstString otherName = otherMember.nat.getName();
         return thisName.compareTo(otherName);
      }
   }

   public final String toString() {
      return this.typeName() + '{' + this.toHuman() + '}';
   }

   public final boolean isCategory2() {
      return false;
   }

   public final String toHuman() {
      return this.definingClass.toHuman() + '.' + this.nat.toHuman();
   }

   public final CstType getDefiningClass() {
      return this.definingClass;
   }

   public final CstNat getNat() {
      return this.nat;
   }
}
