package com.android.dx.cf.attrib;

import com.android.dx.cf.code.LineNumberList;
import com.android.dx.util.MutabilityException;

public final class AttLineNumberTable extends BaseAttribute {
   public static final String ATTRIBUTE_NAME = "LineNumberTable";
   private final LineNumberList lineNumbers;

   public AttLineNumberTable(LineNumberList lineNumbers) {
      super("LineNumberTable");

      try {
         if (lineNumbers.isMutable()) {
            throw new MutabilityException("lineNumbers.isMutable()");
         }
      } catch (NullPointerException var3) {
         throw new NullPointerException("lineNumbers == null");
      }

      this.lineNumbers = lineNumbers;
   }

   public int byteLength() {
      return 8 + 4 * this.lineNumbers.size();
   }

   public LineNumberList getLineNumbers() {
      return this.lineNumbers;
   }
}
