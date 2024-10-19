package com.android.dx.dex.file;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstMethodHandle;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

public final class MethodHandlesSection extends UniformItemSection {
   private final TreeMap<CstMethodHandle, MethodHandleItem> methodHandles = new TreeMap();

   public MethodHandlesSection(DexFile dexFile) {
      super("method_handles", dexFile, 8);
   }

   public IndexedItem get(Constant cst) {
      if (cst == null) {
         throw new NullPointerException("cst == null");
      } else {
         this.throwIfNotPrepared();
         IndexedItem result = (IndexedItem)this.methodHandles.get((CstMethodHandle)cst);
         if (result == null) {
            throw new IllegalArgumentException("not found");
         } else {
            return result;
         }
      }
   }

   protected void orderItems() {
      int index = 0;
      Iterator var2 = this.methodHandles.values().iterator();

      while(var2.hasNext()) {
         MethodHandleItem item = (MethodHandleItem)var2.next();
         item.setIndex(index++);
      }

   }

   public Collection<? extends Item> items() {
      return this.methodHandles.values();
   }

   public void intern(CstMethodHandle methodHandle) {
      if (methodHandle == null) {
         throw new NullPointerException("methodHandle == null");
      } else {
         this.throwIfPrepared();
         MethodHandleItem result = (MethodHandleItem)this.methodHandles.get(methodHandle);
         if (result == null) {
            result = new MethodHandleItem(methodHandle);
            this.methodHandles.put(methodHandle, result);
         }

      }
   }

   int indexOf(CstMethodHandle cstMethodHandle) {
      return ((MethodHandleItem)this.methodHandles.get(cstMethodHandle)).getIndex();
   }
}
