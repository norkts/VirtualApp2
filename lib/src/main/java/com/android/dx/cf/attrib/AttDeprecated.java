package com.android.dx.cf.attrib;

public final class AttDeprecated extends BaseAttribute {
   public static final String ATTRIBUTE_NAME = "Deprecated";

   public AttDeprecated() {
      super("Deprecated");
   }

   public int byteLength() {
      return 6;
   }
}
