package com.android.dx.cf.attrib;

import com.android.dx.rop.cst.CstString;

public final class AttSourceDebugExtension extends BaseAttribute {
   public static final String ATTRIBUTE_NAME = "SourceDebugExtension";
   private final CstString smapString;

   public AttSourceDebugExtension(CstString smapString) {
      super("SourceDebugExtension");
      if (smapString == null) {
         throw new NullPointerException("smapString == null");
      } else {
         this.smapString = smapString;
      }
   }

   public int byteLength() {
      return 6 + this.smapString.getUtf8Size();
   }

   public CstString getSmapString() {
      return this.smapString;
   }
}
