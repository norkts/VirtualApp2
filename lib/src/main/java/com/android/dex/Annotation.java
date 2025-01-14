package com.android.dex;

public final class Annotation implements Comparable<Annotation> {
   private final Dex dex;
   private final byte visibility;
   private final EncodedValue encodedAnnotation;

   public Annotation(Dex dex, byte visibility, EncodedValue encodedAnnotation) {
      this.dex = dex;
      this.visibility = visibility;
      this.encodedAnnotation = encodedAnnotation;
   }

   public byte getVisibility() {
      return this.visibility;
   }

   public EncodedValueReader getReader() {
      return new EncodedValueReader(this.encodedAnnotation, 29);
   }

   public int getTypeIndex() {
      EncodedValueReader reader = this.getReader();
      reader.readAnnotation();
      return reader.getAnnotationType();
   }

   public void writeTo(Dex.Section out) {
      out.writeByte(this.visibility);
      this.encodedAnnotation.writeTo(out);
   }

   public int compareTo(Annotation other) {
      return this.encodedAnnotation.compareTo(other.encodedAnnotation);
   }

   public String toString() {
      return this.dex == null ? this.visibility + " " + this.getTypeIndex() : this.visibility + " " + (String)this.dex.typeNames().get(this.getTypeIndex());
   }
}
