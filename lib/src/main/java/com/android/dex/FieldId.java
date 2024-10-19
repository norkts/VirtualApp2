package com.android.dex;

import com.android.dex.util.Unsigned;

public final class FieldId implements Comparable<FieldId> {
   private final Dex dex;
   private final int declaringClassIndex;
   private final int typeIndex;
   private final int nameIndex;

   public FieldId(Dex dex, int declaringClassIndex, int typeIndex, int nameIndex) {
      this.dex = dex;
      this.declaringClassIndex = declaringClassIndex;
      this.typeIndex = typeIndex;
      this.nameIndex = nameIndex;
   }

   public int getDeclaringClassIndex() {
      return this.declaringClassIndex;
   }

   public int getTypeIndex() {
      return this.typeIndex;
   }

   public int getNameIndex() {
      return this.nameIndex;
   }

   public int compareTo(FieldId other) {
      if (this.declaringClassIndex != other.declaringClassIndex) {
         return Unsigned.compare(this.declaringClassIndex, other.declaringClassIndex);
      } else {
         return this.nameIndex != other.nameIndex ? Unsigned.compare(this.nameIndex, other.nameIndex) : Unsigned.compare(this.typeIndex, other.typeIndex);
      }
   }

   public void writeTo(Dex.Section out) {
      out.writeUnsignedShort(this.declaringClassIndex);
      out.writeUnsignedShort(this.typeIndex);
      out.writeInt(this.nameIndex);
   }

   public String toString() {
      return this.dex == null ? this.declaringClassIndex + " " + this.typeIndex + " " + this.nameIndex : (String)this.dex.typeNames().get(this.typeIndex) + "." + (String)this.dex.strings().get(this.nameIndex);
   }
}
