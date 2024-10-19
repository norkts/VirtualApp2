package com.android.dx.dex.file;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstCallSite;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.ByteArrayAnnotatedOutput;

public final class CallSiteItem extends OffsettedItem {
   private final CstCallSite value;
   private byte[] encodedForm;

   public CallSiteItem(CstCallSite value) {
      super(1, writeSize(value));
      this.value = value;
   }

   private static int writeSize(CstCallSite value) {
      return -1;
   }

   protected void place0(Section addedTo, int offset) {
      ByteArrayAnnotatedOutput out = new ByteArrayAnnotatedOutput();
      ValueEncoder encoder = new ValueEncoder(addedTo.getFile(), out);
      encoder.writeArray(this.value, true);
      this.encodedForm = out.toByteArray();
      this.setWriteSize(this.encodedForm.length);
   }

   public String toHuman() {
      return this.value.toHuman();
   }

   public String toString() {
      return this.value.toString();
   }

   protected void writeTo0(DexFile file, AnnotatedOutput out) {
      if (out.annotates()) {
         out.annotate(0, this.offsetString() + " call site");
         ValueEncoder encoder = new ValueEncoder(file, out);
         encoder.writeArray(this.value, true);
      } else {
         out.write(this.encodedForm);
      }

   }

   public ItemType itemType() {
      return ItemType.TYPE_ENCODED_ARRAY_ITEM;
   }

   public void addContents(DexFile file) {
      ValueEncoder.addContents(file, (Constant)this.value);
   }
}
