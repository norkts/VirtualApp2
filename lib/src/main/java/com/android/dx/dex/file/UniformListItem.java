package com.android.dx.dex.file;

import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.Iterator;
import java.util.List;

public final class UniformListItem<T extends OffsettedItem> extends OffsettedItem {
   private static final int HEADER_SIZE = 4;
   private final ItemType itemType;
   private final List<T> items;

   public UniformListItem(ItemType itemType, List<T> items) {
      super(getAlignment(items), writeSize(items));
      if (itemType == null) {
         throw new NullPointerException("itemType == null");
      } else {
         this.items = items;
         this.itemType = itemType;
      }
   }

   private static int getAlignment(List<? extends OffsettedItem> items) {
      try {
         return Math.max(4, ((OffsettedItem)items.get(0)).getAlignment());
      } catch (IndexOutOfBoundsException var2) {
         throw new IllegalArgumentException("items.size() == 0");
      } catch (NullPointerException var3) {
         throw new NullPointerException("items == null");
      }
   }

   private static int writeSize(List<? extends OffsettedItem> items) {
      OffsettedItem first = (OffsettedItem)items.get(0);
      return items.size() * first.writeSize() + getAlignment(items);
   }

   public ItemType itemType() {
      return this.itemType;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder(100);
      sb.append(this.getClass().getName());
      sb.append(this.items);
      return sb.toString();
   }

   public void addContents(DexFile file) {
      Iterator var2 = this.items.iterator();

      while(var2.hasNext()) {
         OffsettedItem i = (OffsettedItem)var2.next();
         i.addContents(file);
      }

   }

   public final String toHuman() {
      StringBuilder sb = new StringBuilder(100);
      boolean first = true;
      sb.append("{");

      OffsettedItem i;
      for(Iterator var3 = this.items.iterator(); var3.hasNext(); sb.append(i.toHuman())) {
         i = (OffsettedItem)var3.next();
         if (first) {
            first = false;
         } else {
            sb.append(", ");
         }
      }

      sb.append("}");
      return sb.toString();
   }

   public final List<T> getItems() {
      return this.items;
   }

   protected void place0(Section addedTo, int offset) {
      offset += this.headerSize();
      boolean first = true;
      int theSize = -1;
      int theAlignment = -1;

      OffsettedItem i;
      int size;
      for(Iterator var6 = this.items.iterator(); var6.hasNext(); offset = i.place(addedTo, offset) + size) {
         i = (OffsettedItem)var6.next();
         size = i.writeSize();
         if (first) {
            theSize = size;
            theAlignment = i.getAlignment();
            first = false;
         } else {
            if (size != theSize) {
               throw new UnsupportedOperationException("item size mismatch");
            }

            if (i.getAlignment() != theAlignment) {
               throw new UnsupportedOperationException("item alignment mismatch");
            }
         }
      }

   }

   protected void writeTo0(DexFile file, AnnotatedOutput out) {
      int size = this.items.size();
      if (out.annotates()) {
         out.annotate(0, this.offsetString() + " " + this.typeName());
         out.annotate(4, "  size: " + Hex.u4(size));
      }

      out.writeInt(size);
      Iterator var4 = this.items.iterator();

      while(var4.hasNext()) {
         OffsettedItem i = (OffsettedItem)var4.next();
         i.writeTo(file, out);
      }

   }

   private int headerSize() {
      return this.getAlignment();
   }
}
