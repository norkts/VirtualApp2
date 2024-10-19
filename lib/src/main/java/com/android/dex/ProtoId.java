package com.android.dex;

import com.android.dex.util.Unsigned;

public final class ProtoId implements Comparable<ProtoId> {
   private final Dex dex;
   private final int shortyIndex;
   private final int returnTypeIndex;
   private final int parametersOffset;

   public ProtoId(Dex dex, int shortyIndex, int returnTypeIndex, int parametersOffset) {
      this.dex = dex;
      this.shortyIndex = shortyIndex;
      this.returnTypeIndex = returnTypeIndex;
      this.parametersOffset = parametersOffset;
   }

   public int compareTo(ProtoId other) {
      return this.returnTypeIndex != other.returnTypeIndex ? Unsigned.compare(this.returnTypeIndex, other.returnTypeIndex) : Unsigned.compare(this.parametersOffset, other.parametersOffset);
   }

   public int getShortyIndex() {
      return this.shortyIndex;
   }

   public int getReturnTypeIndex() {
      return this.returnTypeIndex;
   }

   public int getParametersOffset() {
      return this.parametersOffset;
   }

   public void writeTo(Dex.Section out) {
      out.writeInt(this.shortyIndex);
      out.writeInt(this.returnTypeIndex);
      out.writeInt(this.parametersOffset);
   }

   public String toString() {
      return this.dex == null ? this.shortyIndex + " " + this.returnTypeIndex + " " + this.parametersOffset : (String)this.dex.strings().get(this.shortyIndex) + ": " + (String)this.dex.typeNames().get(this.returnTypeIndex) + " " + this.dex.readTypeList(this.parametersOffset);
   }
}