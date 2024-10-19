package com.android.dx.dex.file;

import com.android.dx.rop.annotation.Annotation;
import com.android.dx.rop.annotation.Annotations;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.Iterator;

public final class AnnotationSetItem extends OffsettedItem {
   private static final int ALIGNMENT = 4;
   private static final int ENTRY_WRITE_SIZE = 4;
   private final Annotations annotations;
   private final AnnotationItem[] items;

   public AnnotationSetItem(Annotations annotations, DexFile dexFile) {
      super(4, writeSize(annotations));
      this.annotations = annotations;
      this.items = new AnnotationItem[annotations.size()];
      int at = 0;

      for(Iterator var4 = annotations.getAnnotations().iterator(); var4.hasNext(); ++at) {
         Annotation a = (Annotation)var4.next();
         this.items[at] = new AnnotationItem(a, dexFile);
      }

   }

   private static int writeSize(Annotations annotations) {
      try {
         return annotations.size() * 4 + 4;
      } catch (NullPointerException var2) {
         throw new NullPointerException("list == null");
      }
   }

   public Annotations getAnnotations() {
      return this.annotations;
   }

   public int hashCode() {
      return this.annotations.hashCode();
   }

   protected int compareTo0(OffsettedItem other) {
      AnnotationSetItem otherSet = (AnnotationSetItem)other;
      return this.annotations.compareTo(otherSet.annotations);
   }

   public ItemType itemType() {
      return ItemType.TYPE_ANNOTATION_SET_ITEM;
   }

   public String toHuman() {
      return this.annotations.toString();
   }

   public void addContents(DexFile file) {
      MixedItemSection byteData = file.getByteData();
      int size = this.items.length;

      for(int i = 0; i < size; ++i) {
         this.items[i] = (AnnotationItem)byteData.intern(this.items[i]);
      }

   }

   protected void place0(Section addedTo, int offset) {
      AnnotationItem.sortByTypeIdIndex(this.items);
   }

   protected void writeTo0(DexFile file, AnnotatedOutput out) {
      boolean annotates = out.annotates();
      int size = this.items.length;
      if (annotates) {
         out.annotate(0, this.offsetString() + " annotation set");
         out.annotate(4, "  size: " + Hex.u4(size));
      }

      out.writeInt(size);

      for(int i = 0; i < size; ++i) {
         AnnotationItem item = this.items[i];
         int offset = item.getAbsoluteOffset();
         if (annotates) {
            out.annotate(4, "  entries[" + Integer.toHexString(i) + "]: " + Hex.u4(offset));
            this.items[i].annotateTo(out, "    ");
         }

         out.writeInt(offset);
      }

   }
}
