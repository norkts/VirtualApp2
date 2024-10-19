package com.android.dx.cf.attrib;

import com.android.dx.util.MutabilityException;

public final class AttInnerClasses extends BaseAttribute {
   public static final String ATTRIBUTE_NAME = "InnerClasses";
   private final InnerClassList innerClasses;

   public AttInnerClasses(InnerClassList innerClasses) {
      super("InnerClasses");

      try {
         if (innerClasses.isMutable()) {
            throw new MutabilityException("innerClasses.isMutable()");
         }
      } catch (NullPointerException var3) {
         throw new NullPointerException("innerClasses == null");
      }

      this.innerClasses = innerClasses;
   }

   public int byteLength() {
      return 8 + this.innerClasses.size() * 8;
   }

   public InnerClassList getInnerClasses() {
      return this.innerClasses;
   }
}
