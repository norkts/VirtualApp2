package com.android.dx.dex.file;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstBaseMethodRef;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.Collection;
import java.util.TreeMap;

public final class MethodIdsSection extends MemberIdsSection {
   private final TreeMap<CstBaseMethodRef, MethodIdItem> methodIds = new TreeMap();

   public MethodIdsSection(DexFile file) {
      super("method_ids", file);
   }

   public Collection<? extends Item> items() {
      return this.methodIds.values();
   }

   public IndexedItem get(Constant cst) {
      if (cst == null) {
         throw new NullPointerException("cst == null");
      } else {
         this.throwIfNotPrepared();
         IndexedItem result = (IndexedItem)this.methodIds.get((CstBaseMethodRef)cst);
         if (result == null) {
            throw new IllegalArgumentException("not found");
         } else {
            return result;
         }
      }
   }

   public void writeHeaderPart(AnnotatedOutput out) {
      this.throwIfNotPrepared();
      int sz = this.methodIds.size();
      int offset = sz == 0 ? 0 : this.getFileOffset();
      if (out.annotates()) {
         out.annotate(4, "method_ids_size: " + Hex.u4(sz));
         out.annotate(4, "method_ids_off:  " + Hex.u4(offset));
      }

      out.writeInt(sz);
      out.writeInt(offset);
   }

   public synchronized MethodIdItem intern(CstBaseMethodRef method) {
      if (method == null) {
         throw new NullPointerException("method == null");
      } else {
         this.throwIfPrepared();
         MethodIdItem result = (MethodIdItem)this.methodIds.get(method);
         if (result == null) {
            result = new MethodIdItem(method);
            this.methodIds.put(method, result);
         }

         return result;
      }
   }

   public int indexOf(CstBaseMethodRef ref) {
      if (ref == null) {
         throw new NullPointerException("ref == null");
      } else {
         this.throwIfNotPrepared();
         MethodIdItem item = (MethodIdItem)this.methodIds.get(ref);
         if (item == null) {
            throw new IllegalArgumentException("not found");
         } else {
            return item.getIndex();
         }
      }
   }
}
