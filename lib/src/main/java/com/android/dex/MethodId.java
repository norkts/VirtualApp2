package com.android.dex;

import com.android.dex.util.Unsigned;

public final class MethodId implements Comparable<MethodId> {
   private final Dex dex;
   private final int declaringClassIndex;
   private final int protoIndex;
   private final int nameIndex;

   public MethodId(Dex dex, int declaringClassIndex, int protoIndex, int nameIndex) {
      this.dex = dex;
      this.declaringClassIndex = declaringClassIndex;
      this.protoIndex = protoIndex;
      this.nameIndex = nameIndex;
   }

   public int getDeclaringClassIndex() {
      return this.declaringClassIndex;
   }

   public int getProtoIndex() {
      return this.protoIndex;
   }

   public int getNameIndex() {
      return this.nameIndex;
   }

   public int compareTo(MethodId other) {
      if (this.declaringClassIndex != other.declaringClassIndex) {
         return Unsigned.compare(this.declaringClassIndex, other.declaringClassIndex);
      } else {
         return this.nameIndex != other.nameIndex ? Unsigned.compare(this.nameIndex, other.nameIndex) : Unsigned.compare(this.protoIndex, other.protoIndex);
      }
   }

   public void writeTo(Dex.Section out) {
      out.writeUnsignedShort(this.declaringClassIndex);
      out.writeUnsignedShort(this.protoIndex);
      out.writeInt(this.nameIndex);
   }

   public String toString() {
      return this.dex == null ? this.declaringClassIndex + " " + this.protoIndex + " " + this.nameIndex : (String)this.dex.typeNames().get(this.declaringClassIndex) + "." + (String)this.dex.strings().get(this.nameIndex) + this.dex.readTypeList(((ProtoId)this.dex.protoIds().get(this.protoIndex)).getParametersOffset());
   }
}
