package com.android.dx.dex.file;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.rop.type.Prototype;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

public final class ProtoIdsSection extends UniformItemSection {
   private final TreeMap<Prototype, ProtoIdItem> protoIds = new TreeMap();

   public ProtoIdsSection(DexFile file) {
      super("proto_ids", file, 4);
   }

   public Collection<? extends Item> items() {
      return this.protoIds.values();
   }

   public IndexedItem get(Constant cst) {
      if (cst == null) {
         throw new NullPointerException("cst == null");
      } else if (!(cst instanceof CstProtoRef)) {
         throw new IllegalArgumentException("cst not instance of CstProtoRef");
      } else {
         this.throwIfNotPrepared();
         CstProtoRef protoRef = (CstProtoRef)cst;
         IndexedItem result = (IndexedItem)this.protoIds.get(protoRef.getPrototype());
         if (result == null) {
            throw new IllegalArgumentException("not found");
         } else {
            return result;
         }
      }
   }

   public void writeHeaderPart(AnnotatedOutput out) {
      this.throwIfNotPrepared();
      int sz = this.protoIds.size();
      int offset = sz == 0 ? 0 : this.getFileOffset();
      if (sz > 65536) {
         throw new UnsupportedOperationException("too many proto ids");
      } else {
         if (out.annotates()) {
            out.annotate(4, "proto_ids_size:  " + Hex.u4(sz));
            out.annotate(4, "proto_ids_off:   " + Hex.u4(offset));
         }

         out.writeInt(sz);
         out.writeInt(offset);
      }
   }

   public synchronized ProtoIdItem intern(Prototype prototype) {
      if (prototype == null) {
         throw new NullPointerException("prototype == null");
      } else {
         this.throwIfPrepared();
         ProtoIdItem result = (ProtoIdItem)this.protoIds.get(prototype);
         if (result == null) {
            result = new ProtoIdItem(prototype);
            this.protoIds.put(prototype, result);
         }

         return result;
      }
   }

   public int indexOf(Prototype prototype) {
      if (prototype == null) {
         throw new NullPointerException("prototype == null");
      } else {
         this.throwIfNotPrepared();
         ProtoIdItem item = (ProtoIdItem)this.protoIds.get(prototype);
         if (item == null) {
            throw new IllegalArgumentException("not found");
         } else {
            return item.getIndex();
         }
      }
   }

   protected void orderItems() {
      int idx = 0;

      for(Iterator var2 = this.items().iterator(); var2.hasNext(); ++idx) {
         Object i = var2.next();
         ((ProtoIdItem)i).setIndex(idx);
      }

   }
}
