package com.android.dx.dex.file;

import com.android.dex.util.ExceptionWithContext;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public final class MixedItemSection extends Section {
   private static final Comparator<OffsettedItem> TYPE_SORTER = new Comparator<OffsettedItem>() {
      public int compare(OffsettedItem item1, OffsettedItem item2) {
         ItemType type1 = item1.itemType();
         ItemType type2 = item2.itemType();
         return type1.compareTo(type2);
      }
   };
   private final ArrayList<OffsettedItem> items = new ArrayList(100);
   private final HashMap<OffsettedItem, OffsettedItem> interns = new HashMap(100);
   private final SortType sort;
   private int writeSize;

   public MixedItemSection(String name, DexFile file, int alignment, SortType sort) {
      super(name, file, alignment);
      this.sort = sort;
      this.writeSize = -1;
   }

   public Collection<? extends Item> items() {
      return this.items;
   }

   public int writeSize() {
      this.throwIfNotPrepared();
      return this.writeSize;
   }

   public int getAbsoluteItemOffset(Item item) {
      OffsettedItem oi = (OffsettedItem)item;
      return oi.getAbsoluteOffset();
   }

   public int size() {
      return this.items.size();
   }

   public void writeHeaderPart(AnnotatedOutput out) {
      this.throwIfNotPrepared();
      if (this.writeSize == -1) {
         throw new RuntimeException("write size not yet set");
      } else {
         int sz = this.writeSize;
         int offset = sz == 0 ? 0 : this.getFileOffset();
         String name = this.getName();
         if (name == null) {
            name = "<unnamed>";
         }

         int spaceCount = 15 - name.length();
         char[] spaceArr = new char[spaceCount];
         Arrays.fill(spaceArr, ' ');
         String spaces = new String(spaceArr);
         if (out.annotates()) {
            out.annotate(4, name + "_size:" + spaces + Hex.u4(sz));
            out.annotate(4, name + "_off: " + spaces + Hex.u4(offset));
         }

         out.writeInt(sz);
         out.writeInt(offset);
      }
   }

   public void add(OffsettedItem item) {
      this.throwIfPrepared();

      try {
         if (item.getAlignment() > this.getAlignment()) {
            throw new IllegalArgumentException("incompatible item alignment");
         }
      } catch (NullPointerException var3) {
         throw new NullPointerException("item == null");
      }

      this.items.add(item);
   }

   public synchronized <T extends OffsettedItem> T intern(T item) {
      this.throwIfPrepared();
      OffsettedItem result = (OffsettedItem)this.interns.get(item);
      if (result != null) {
         return (T)result;
      } else {
         this.add(item);
         this.interns.put(item, item);
         return item;
      }
   }

   public <T extends OffsettedItem> T get(T item) {
      this.throwIfNotPrepared();
      OffsettedItem result = (OffsettedItem)this.interns.get(item);
      if (result != null) {
         return (T)result;
      } else {
         throw new NoSuchElementException(item.toString());
      }
   }

   public void writeIndexAnnotation(AnnotatedOutput out, ItemType itemType, String intro) {
      this.throwIfNotPrepared();
      TreeMap<String, OffsettedItem> index = new TreeMap();
      Iterator var5 = this.items.iterator();

      String label;
      while(var5.hasNext()) {
         OffsettedItem item = (OffsettedItem)var5.next();
         if (item.itemType() == itemType) {
            label = item.toHuman();
            index.put(label, item);
         }
      }

      if (index.size() != 0) {
         out.annotate(0, intro);
         var5 = index.entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry<String, OffsettedItem> entry = (Map.Entry)var5.next();
            label = (String)entry.getKey();
            OffsettedItem item = (OffsettedItem)entry.getValue();
            out.annotate(0, item.offsetString() + ' ' + label + '\n');
         }

      }
   }

   protected void prepare0() {
      DexFile file = this.getFile();
      int i = 0;

      while(true) {
         int sz = this.items.size();
         if (i >= sz) {
            return;
         }

         while(i < sz) {
            OffsettedItem one = (OffsettedItem)this.items.get(i);
            one.addContents(file);
            ++i;
         }
      }
   }

   public void placeItems() {
      this.throwIfNotPrepared();
      switch (this.sort) {
         case INSTANCE:
            Collections.sort(this.items);
            break;
         case TYPE:
            Collections.sort(this.items, TYPE_SORTER);
      }

      int sz = this.items.size();
      int outAt = 0;

      for(int i = 0; i < sz; ++i) {
         OffsettedItem one = (OffsettedItem)this.items.get(i);

         try {
            int placedAt = one.place(this, outAt);
            if (placedAt < outAt) {
               throw new RuntimeException("bogus place() result for " + one);
            }

            outAt = placedAt + one.writeSize();
         } catch (RuntimeException var6) {
            RuntimeException ex = var6;
            throw ExceptionWithContext.withContext(ex, "...while placing " + one);
         }
      }

      this.writeSize = outAt;
   }

   protected void writeTo0(AnnotatedOutput out) {
      boolean annotates = out.annotates();
      boolean first = true;
      DexFile file = this.getFile();
      int at = 0;

      OffsettedItem one;
      for(Iterator var6 = this.items.iterator(); var6.hasNext(); at += one.writeSize()) {
         one = (OffsettedItem)var6.next();
         if (annotates) {
            if (first) {
               first = false;
            } else {
               out.annotate(0, "\n");
            }
         }

         int alignMask = one.getAlignment() - 1;
         int writeAt = at + alignMask & ~alignMask;
         if (at != writeAt) {
            out.writeZeroes(writeAt - at);
            at = writeAt;
         }

         one.writeTo(file, out);
      }

      if (at != this.writeSize) {
         throw new RuntimeException("output size mismatch");
      }
   }

   static enum SortType {
      NONE,
      TYPE,
      INSTANCE;
   }
}
