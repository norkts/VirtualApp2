package com.android.dx.dex.file;

import com.android.dx.rop.cst.CstBaseMethodRef;

public final class MethodIdItem extends MemberIdItem {
   public MethodIdItem(CstBaseMethodRef method) {
      super(method);
   }

   public ItemType itemType() {
      return ItemType.TYPE_METHOD_ID_ITEM;
   }

   public void addContents(DexFile file) {
      super.addContents(file);
      ProtoIdsSection protoIds = file.getProtoIds();
      protoIds.intern(this.getMethodRef().getPrototype());
   }

   public CstBaseMethodRef getMethodRef() {
      return (CstBaseMethodRef)this.getRef();
   }

   protected int getTypoidIdx(DexFile file) {
      ProtoIdsSection protoIds = file.getProtoIds();
      return protoIds.indexOf(this.getMethodRef().getPrototype());
   }

   protected String getTypoidName() {
      return "proto_idx";
   }
}
