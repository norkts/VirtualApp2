package com.android.dx.dex.file;

import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;

public final class TypeListItem extends OffsettedItem {
   private static final int ALIGNMENT = 4;
   private static final int ELEMENT_SIZE = 2;
   private static final int HEADER_SIZE = 4;
   private final TypeList list;

   public TypeListItem(TypeList list) {
      super(4, list.size() * 2 + 4);
      this.list = list;
   }

   public int hashCode() {
      return StdTypeList.hashContents(this.list);
   }

   public ItemType itemType() {
      return ItemType.TYPE_TYPE_LIST;
   }

   public void addContents(DexFile file) {
      TypeIdsSection typeIds = file.getTypeIds();
      int sz = this.list.size();

      for(int i = 0; i < sz; ++i) {
         typeIds.intern(this.list.getType(i));
      }

   }

   public String toHuman() {
      throw new RuntimeException("unsupported");
   }

   public TypeList getList() {
      return this.list;
   }

   protected void writeTo0(DexFile file, AnnotatedOutput out) {
      TypeIdsSection typeIds = file.getTypeIds();
      int sz = this.list.size();
      int i;
      if (out.annotates()) {
         out.annotate(0, this.offsetString() + " type_list");
         out.annotate(4, "  size: " + Hex.u4(sz));

         for(i = 0; i < sz; ++i) {
            Type one = this.list.getType(i);
            int idx = typeIds.indexOf(one);
            out.annotate(2, "  " + Hex.u2(idx) + " // " + one.toHuman());
         }
      }

      out.writeInt(sz);

      for(i = 0; i < sz; ++i) {
         out.writeShort(typeIds.indexOf(this.list.getType(i)));
      }

   }

   protected int compareTo0(OffsettedItem other) {
      TypeList thisList = this.list;
      TypeList otherList = ((TypeListItem)other).list;
      return StdTypeList.compareContents(thisList, otherList);
   }
}
