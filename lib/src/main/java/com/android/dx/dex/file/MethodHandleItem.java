package com.android.dx.dex.file;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstBaseMethodRef;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstInterfaceMethodRef;
import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;

public final class MethodHandleItem extends IndexedItem {
   private final int ITEM_SIZE = 8;
   private final CstMethodHandle methodHandle;

   public MethodHandleItem(CstMethodHandle methodHandle) {
      this.methodHandle = methodHandle;
   }

   public ItemType itemType() {
      return ItemType.TYPE_METHOD_HANDLE_ITEM;
   }

   public int writeSize() {
      return 8;
   }

   public void addContents(DexFile file) {
      MethodHandlesSection methodHandles = file.getMethodHandles();
      methodHandles.intern(this.methodHandle);
   }

   public void writeTo(DexFile file, AnnotatedOutput out) {
      int targetIndex = this.getTargetIndex(file);
      int mhType = this.methodHandle.getMethodHandleType();
      if (out.annotates()) {
         out.annotate(0, this.indexString() + ' ' + this.methodHandle.toString());
         String typeComment = " // " + CstMethodHandle.getMethodHandleTypeName(mhType);
         out.annotate(2, "type:     " + Hex.u2(mhType) + typeComment);
         out.annotate(2, "reserved: " + Hex.u2(0));
         String targetComment = " // " + this.methodHandle.getRef().toString();
         if (this.methodHandle.isAccessor()) {
            out.annotate(2, "fieldId:  " + Hex.u2(targetIndex) + targetComment);
         } else {
            out.annotate(2, "methodId: " + Hex.u2(targetIndex) + targetComment);
         }

         out.annotate(2, "reserved: " + Hex.u2(0));
      }

      out.writeShort(mhType);
      out.writeShort(0);
      out.writeShort(this.getTargetIndex(file));
      out.writeShort(0);
   }

   private int getTargetIndex(DexFile file) {
      Constant ref = this.methodHandle.getRef();
      if (this.methodHandle.isAccessor()) {
         FieldIdsSection fieldIds = file.getFieldIds();
         return fieldIds.indexOf((CstFieldRef)ref);
      } else if (this.methodHandle.isInvocation()) {
         if (ref instanceof CstInterfaceMethodRef) {
            ref = ((CstInterfaceMethodRef)ref).toMethodRef();
         }

         MethodIdsSection methodIds = file.getMethodIds();
         return methodIds.indexOf((CstBaseMethodRef)ref);
      } else {
         throw new IllegalStateException("Unhandled invocation type");
      }
   }
}
