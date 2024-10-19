package com.android.dx.cf.attrib;

import com.android.dx.rop.cst.Constant;

public final class AttAnnotationDefault extends BaseAttribute {
   public static final String ATTRIBUTE_NAME = "AnnotationDefault";
   private final Constant value;
   private final int byteLength;

   public AttAnnotationDefault(Constant value, int byteLength) {
      super("AnnotationDefault");
      if (value == null) {
         throw new NullPointerException("value == null");
      } else {
         this.value = value;
         this.byteLength = byteLength;
      }
   }

   public int byteLength() {
      return this.byteLength + 6;
   }

   public Constant getValue() {
      return this.value;
   }
}
