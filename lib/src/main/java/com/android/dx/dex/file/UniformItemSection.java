package com.android.dx.dex.file;

import com.android.dx.rop.cst.Constant;
import com.android.dx.util.AnnotatedOutput;
import java.util.Collection;
import java.util.Iterator;

public abstract class UniformItemSection extends Section {
   public UniformItemSection(String name, DexFile file, int alignment) {
      super(name, file, alignment);
   }

   public final int writeSize() {
      Collection<? extends Item> items = this.items();
      int sz = items.size();
      return sz == 0 ? 0 : sz * ((Item)items.iterator().next()).writeSize();
   }

   public abstract IndexedItem get(Constant var1);

   protected final void prepare0() {
      DexFile file = this.getFile();
      this.orderItems();
      Iterator var2 = this.items().iterator();

      while(var2.hasNext()) {
         Item one = (Item)var2.next();
         one.addContents(file);
      }

   }

   protected final void writeTo0(AnnotatedOutput out) {
      DexFile file = this.getFile();
      int alignment = this.getAlignment();
      Iterator var4 = this.items().iterator();

      while(var4.hasNext()) {
         Item one = (Item)var4.next();
         one.writeTo(file, out);
         out.alignTo(alignment);
      }

   }

   public final int getAbsoluteItemOffset(Item item) {
      IndexedItem ii = (IndexedItem)item;
      int relativeOffset = ii.getIndex() * ii.writeSize();
      return this.getAbsoluteOffset(relativeOffset);
   }

   protected abstract void orderItems();
}
