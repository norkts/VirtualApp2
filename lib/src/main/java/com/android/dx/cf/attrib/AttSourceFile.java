package com.android.dx.cf.attrib;

import com.android.dx.rop.cst.CstString;

public final class AttSourceFile extends BaseAttribute {
   public static final String ATTRIBUTE_NAME = "SourceFile";
   private final CstString sourceFile;

   public AttSourceFile(CstString sourceFile) {
      super("SourceFile");
      if (sourceFile == null) {
         throw new NullPointerException("sourceFile == null");
      } else {
         this.sourceFile = sourceFile;
      }
   }

   public int byteLength() {
      return 8;
   }

   public CstString getSourceFile() {
      return this.sourceFile;
   }
}
