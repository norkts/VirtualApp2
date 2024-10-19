package com.android.dx.cf.attrib;

public final class AttSynthetic extends BaseAttribute {
   public static final String ATTRIBUTE_NAME = "Synthetic";

   public AttSynthetic() {
      super("Synthetic");
   }

   public int byteLength() {
      return 6;
   }
}
