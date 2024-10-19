package com.android.dx.dex.file;

import com.android.dx.rop.cst.CstFieldRef;

public final class FieldIdItem extends MemberIdItem {
   public FieldIdItem(CstFieldRef field) {
      super(field);
   }

   public ItemType itemType() {
      return ItemType.TYPE_FIELD_ID_ITEM;
   }

   public void addContents(DexFile file) {
      super.addContents(file);
      TypeIdsSection typeIds = file.getTypeIds();
      typeIds.intern(this.getFieldRef().getType());
   }

   public CstFieldRef getFieldRef() {
      return (CstFieldRef)this.getRef();
   }

   protected int getTypoidIdx(DexFile file) {
      TypeIdsSection typeIds = file.getTypeIds();
      return typeIds.indexOf(this.getFieldRef().getType());
   }

   protected String getTypoidName() {
      return "type_idx";
   }
}
