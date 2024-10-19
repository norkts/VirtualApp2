package com.android.dx.dex.file;

import com.android.dx.rop.annotation.Annotations;
import com.android.dx.rop.annotation.AnnotationsList;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public final class AnnotationsDirectoryItem extends OffsettedItem {
   private static final int ALIGNMENT = 4;
   private static final int HEADER_SIZE = 16;
   private static final int ELEMENT_SIZE = 8;
   private AnnotationSetItem classAnnotations = null;
   private ArrayList<FieldAnnotationStruct> fieldAnnotations = null;
   private ArrayList<MethodAnnotationStruct> methodAnnotations = null;
   private ArrayList<ParameterAnnotationStruct> parameterAnnotations = null;

   public AnnotationsDirectoryItem() {
      super(4, -1);
   }

   public ItemType itemType() {
      return ItemType.TYPE_ANNOTATIONS_DIRECTORY_ITEM;
   }

   public boolean isEmpty() {
      return this.classAnnotations == null && this.fieldAnnotations == null && this.methodAnnotations == null && this.parameterAnnotations == null;
   }

   public boolean isInternable() {
      return this.classAnnotations != null && this.fieldAnnotations == null && this.methodAnnotations == null && this.parameterAnnotations == null;
   }

   public int hashCode() {
      return this.classAnnotations == null ? 0 : this.classAnnotations.hashCode();
   }

   public int compareTo0(OffsettedItem other) {
      if (!this.isInternable()) {
         throw new UnsupportedOperationException("uninternable instance");
      } else {
         AnnotationsDirectoryItem otherDirectory = (AnnotationsDirectoryItem)other;
         return this.classAnnotations.compareTo(otherDirectory.classAnnotations);
      }
   }

   public void setClassAnnotations(Annotations annotations, DexFile dexFile) {
      if (annotations == null) {
         throw new NullPointerException("annotations == null");
      } else if (this.classAnnotations != null) {
         throw new UnsupportedOperationException("class annotations already set");
      } else {
         this.classAnnotations = new AnnotationSetItem(annotations, dexFile);
      }
   }

   public void addFieldAnnotations(CstFieldRef field, Annotations annotations, DexFile dexFile) {
      if (this.fieldAnnotations == null) {
         this.fieldAnnotations = new ArrayList();
      }

      this.fieldAnnotations.add(new FieldAnnotationStruct(field, new AnnotationSetItem(annotations, dexFile)));
   }

   public void addMethodAnnotations(CstMethodRef method, Annotations annotations, DexFile dexFile) {
      if (this.methodAnnotations == null) {
         this.methodAnnotations = new ArrayList();
      }

      this.methodAnnotations.add(new MethodAnnotationStruct(method, new AnnotationSetItem(annotations, dexFile)));
   }

   public void addParameterAnnotations(CstMethodRef method, AnnotationsList list, DexFile dexFile) {
      if (this.parameterAnnotations == null) {
         this.parameterAnnotations = new ArrayList();
      }

      this.parameterAnnotations.add(new ParameterAnnotationStruct(method, list, dexFile));
   }

   public Annotations getMethodAnnotations(CstMethodRef method) {
      if (this.methodAnnotations == null) {
         return null;
      } else {
         Iterator var2 = this.methodAnnotations.iterator();

         MethodAnnotationStruct item;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            item = (MethodAnnotationStruct)var2.next();
         } while(!item.getMethod().equals(method));

         return item.getAnnotations();
      }
   }

   public AnnotationsList getParameterAnnotations(CstMethodRef method) {
      if (this.parameterAnnotations == null) {
         return null;
      } else {
         Iterator var2 = this.parameterAnnotations.iterator();

         ParameterAnnotationStruct item;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            item = (ParameterAnnotationStruct)var2.next();
         } while(!item.getMethod().equals(method));

         return item.getAnnotationsList();
      }
   }

   public void addContents(DexFile file) {
      MixedItemSection wordData = file.getWordData();
      if (this.classAnnotations != null) {
         this.classAnnotations = (AnnotationSetItem)wordData.intern(this.classAnnotations);
      }

      Iterator var3;
      if (this.fieldAnnotations != null) {
         var3 = this.fieldAnnotations.iterator();

         while(var3.hasNext()) {
            FieldAnnotationStruct item = (FieldAnnotationStruct)var3.next();
            item.addContents(file);
         }
      }

      if (this.methodAnnotations != null) {
         var3 = this.methodAnnotations.iterator();

         while(var3.hasNext()) {
            MethodAnnotationStruct item = (MethodAnnotationStruct)var3.next();
            item.addContents(file);
         }
      }

      if (this.parameterAnnotations != null) {
         var3 = this.parameterAnnotations.iterator();

         while(var3.hasNext()) {
            ParameterAnnotationStruct item = (ParameterAnnotationStruct)var3.next();
            item.addContents(file);
         }
      }

   }

   public String toHuman() {
      throw new RuntimeException("unsupported");
   }

   protected void place0(Section addedTo, int offset) {
      int elementCount = listSize(this.fieldAnnotations) + listSize(this.methodAnnotations) + listSize(this.parameterAnnotations);
      this.setWriteSize(16 + elementCount * 8);
   }

   protected void writeTo0(DexFile file, AnnotatedOutput out) {
      boolean annotates = out.annotates();
      int classOff = OffsettedItem.getAbsoluteOffsetOr0(this.classAnnotations);
      int fieldsSize = listSize(this.fieldAnnotations);
      int methodsSize = listSize(this.methodAnnotations);
      int parametersSize = listSize(this.parameterAnnotations);
      if (annotates) {
         out.annotate(0, this.offsetString() + " annotations directory");
         out.annotate(4, "  class_annotations_off: " + Hex.u4(classOff));
         out.annotate(4, "  fields_size:           " + Hex.u4(fieldsSize));
         out.annotate(4, "  methods_size:          " + Hex.u4(methodsSize));
         out.annotate(4, "  parameters_size:       " + Hex.u4(parametersSize));
      }

      out.writeInt(classOff);
      out.writeInt(fieldsSize);
      out.writeInt(methodsSize);
      out.writeInt(parametersSize);
      Iterator var8;
      if (fieldsSize != 0) {
         Collections.sort(this.fieldAnnotations);
         if (annotates) {
            out.annotate(0, "  fields:");
         }

         var8 = this.fieldAnnotations.iterator();

         while(var8.hasNext()) {
            FieldAnnotationStruct item = (FieldAnnotationStruct)var8.next();
            item.writeTo(file, out);
         }
      }

      if (methodsSize != 0) {
         Collections.sort(this.methodAnnotations);
         if (annotates) {
            out.annotate(0, "  methods:");
         }

         var8 = this.methodAnnotations.iterator();

         while(var8.hasNext()) {
            MethodAnnotationStruct item = (MethodAnnotationStruct)var8.next();
            item.writeTo(file, out);
         }
      }

      if (parametersSize != 0) {
         Collections.sort(this.parameterAnnotations);
         if (annotates) {
            out.annotate(0, "  parameters:");
         }

         var8 = this.parameterAnnotations.iterator();

         while(var8.hasNext()) {
            ParameterAnnotationStruct item = (ParameterAnnotationStruct)var8.next();
            item.writeTo(file, out);
         }
      }

   }

   private static int listSize(ArrayList<?> list) {
      return list == null ? 0 : list.size();
   }

   void debugPrint(PrintWriter out) {
      if (this.classAnnotations != null) {
         out.println("  class annotations: " + this.classAnnotations);
      }

      Iterator var2;
      if (this.fieldAnnotations != null) {
         out.println("  field annotations:");
         var2 = this.fieldAnnotations.iterator();

         while(var2.hasNext()) {
            FieldAnnotationStruct item = (FieldAnnotationStruct)var2.next();
            out.println("    " + item.toHuman());
         }
      }

      if (this.methodAnnotations != null) {
         out.println("  method annotations:");
         var2 = this.methodAnnotations.iterator();

         while(var2.hasNext()) {
            MethodAnnotationStruct item = (MethodAnnotationStruct)var2.next();
            out.println("    " + item.toHuman());
         }
      }

      if (this.parameterAnnotations != null) {
         out.println("  parameter annotations:");
         var2 = this.parameterAnnotations.iterator();

         while(var2.hasNext()) {
            ParameterAnnotationStruct item = (ParameterAnnotationStruct)var2.next();
            out.println("    " + item.toHuman());
         }
      }

   }
}
