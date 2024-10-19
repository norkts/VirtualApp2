package com.android.dx.dex.file;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

public final class StringIdsSection extends UniformItemSection {
   private final TreeMap<CstString, StringIdItem> strings = new TreeMap();

   public StringIdsSection(DexFile file) {
      super("string_ids", file, 4);
   }

   public Collection<? extends Item> items() {
      return this.strings.values();
   }

   public IndexedItem get(Constant cst) {
      if (cst == null) {
         throw new NullPointerException("cst == null");
      } else {
         this.throwIfNotPrepared();
         IndexedItem result = (IndexedItem)this.strings.get((CstString)cst);
         if (result == null) {
            throw new IllegalArgumentException("not found");
         } else {
            return result;
         }
      }
   }

   public void writeHeaderPart(AnnotatedOutput out) {
      this.throwIfNotPrepared();
      int sz = this.strings.size();
      int offset = sz == 0 ? 0 : this.getFileOffset();
      if (out.annotates()) {
         out.annotate(4, "string_ids_size: " + Hex.u4(sz));
         out.annotate(4, "string_ids_off:  " + Hex.u4(offset));
      }

      out.writeInt(sz);
      out.writeInt(offset);
   }

   public StringIdItem intern(String string) {
      return this.intern(new StringIdItem(new CstString(string)));
   }

   public StringIdItem intern(CstString string) {
      return this.intern(new StringIdItem(string));
   }

   public synchronized StringIdItem intern(StringIdItem string) {
      if (string == null) {
         throw new NullPointerException("string == null");
      } else {
         this.throwIfPrepared();
         CstString value = string.getValue();
         StringIdItem already = (StringIdItem)this.strings.get(value);
         if (already != null) {
            return already;
         } else {
            this.strings.put(value, string);
            return string;
         }
      }
   }

   public synchronized void intern(CstNat nat) {
      this.intern(nat.getName());
      this.intern(nat.getDescriptor());
   }

   public int indexOf(CstString string) {
      if (string == null) {
         throw new NullPointerException("string == null");
      } else {
         this.throwIfNotPrepared();
         StringIdItem s = (StringIdItem)this.strings.get(string);
         if (s == null) {
            throw new IllegalArgumentException("not found");
         } else {
            return s.getIndex();
         }
      }
   }

   protected void orderItems() {
      int idx = 0;

      for(Iterator var2 = this.strings.values().iterator(); var2.hasNext(); ++idx) {
         StringIdItem s = (StringIdItem)var2.next();
         s.setIndex(idx);
      }

   }
}
