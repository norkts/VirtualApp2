package com.android.dx.dex.file;

import com.android.dx.rop.annotation.Annotation;
import com.android.dx.rop.annotation.AnnotationVisibility;
import com.android.dx.rop.annotation.NameValuePair;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstString;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.ByteArrayAnnotatedOutput;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public final class AnnotationItem extends OffsettedItem {
   private static final int VISIBILITY_BUILD = 0;
   private static final int VISIBILITY_RUNTIME = 1;
   private static final int VISIBILITY_SYSTEM = 2;
   private static final int ALIGNMENT = 1;
   private static final TypeIdSorter TYPE_ID_SORTER = new TypeIdSorter();
   private final Annotation annotation;
   private TypeIdItem type;
   private byte[] encodedForm;

   public static void sortByTypeIdIndex(AnnotationItem[] array) {
      Arrays.sort(array, TYPE_ID_SORTER);
   }

   public AnnotationItem(Annotation annotation, DexFile dexFile) {
      super(1, -1);
      if (annotation == null) {
         throw new NullPointerException("annotation == null");
      } else {
         this.annotation = annotation;
         this.type = null;
         this.encodedForm = null;
         this.addContents(dexFile);
      }
   }

   public ItemType itemType() {
      return ItemType.TYPE_ANNOTATION_ITEM;
   }

   public int hashCode() {
      return this.annotation.hashCode();
   }

   protected int compareTo0(OffsettedItem other) {
      AnnotationItem otherAnnotation = (AnnotationItem)other;
      return this.annotation.compareTo(otherAnnotation.annotation);
   }

   public String toHuman() {
      return this.annotation.toHuman();
   }

   public void addContents(DexFile file) {
      this.type = file.getTypeIds().intern(this.annotation.getType());
      ValueEncoder.addContents(file, this.annotation);
   }

   protected void place0(Section addedTo, int offset) {
      ByteArrayAnnotatedOutput out = new ByteArrayAnnotatedOutput();
      ValueEncoder encoder = new ValueEncoder(addedTo.getFile(), out);
      encoder.writeAnnotation(this.annotation, false);
      this.encodedForm = out.toByteArray();
      this.setWriteSize(this.encodedForm.length + 1);
   }

   public void annotateTo(AnnotatedOutput out, String prefix) {
      out.annotate(0, prefix + "visibility: " + this.annotation.getVisibility().toHuman());
      out.annotate(0, prefix + "type: " + this.annotation.getType().toHuman());
      Iterator var3 = this.annotation.getNameValuePairs().iterator();

      while(var3.hasNext()) {
         NameValuePair pair = (NameValuePair)var3.next();
         CstString name = pair.getName();
         Constant value = pair.getValue();
         out.annotate(0, prefix + name.toHuman() + ": " + ValueEncoder.constantToHuman(value));
      }

   }

   protected void writeTo0(DexFile file, AnnotatedOutput out) {
      boolean annotates = out.annotates();
      AnnotationVisibility visibility = this.annotation.getVisibility();
      if (annotates) {
         out.annotate(0, this.offsetString() + " annotation");
         out.annotate(1, "  visibility: VISBILITY_" + visibility);
      }

      switch (visibility) {
         case BUILD:
            out.writeByte(0);
            break;
         case RUNTIME:
            out.writeByte(1);
            break;
         case SYSTEM:
            out.writeByte(2);
            break;
         default:
            throw new RuntimeException("shouldn't happen");
      }

      if (annotates) {
         ValueEncoder encoder = new ValueEncoder(file, out);
         encoder.writeAnnotation(this.annotation, true);
      } else {
         out.write(this.encodedForm);
      }

   }

   private static class TypeIdSorter implements Comparator<AnnotationItem> {
      private TypeIdSorter() {
      }

      public int compare(AnnotationItem item1, AnnotationItem item2) {
         int index1 = item1.type.getIndex();
         int index2 = item2.type.getIndex();
         if (index1 < index2) {
            return -1;
         } else {
            return index1 > index2 ? 1 : 0;
         }
      }

      // $FF: synthetic method
      TypeIdSorter(Object x0) {
         this();
      }
   }
}
