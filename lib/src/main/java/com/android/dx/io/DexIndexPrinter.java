package com.android.dx.io;

import com.android.dex.ClassDef;
import com.android.dex.Dex;
import com.android.dex.FieldId;
import com.android.dex.MethodId;
import com.android.dex.ProtoId;
import com.android.dex.TableOfContents;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public final class DexIndexPrinter {
   private final Dex dex;
   private final TableOfContents tableOfContents;

   public DexIndexPrinter(File file) throws IOException {
      this.dex = new Dex(file);
      this.tableOfContents = this.dex.getTableOfContents();
   }

   private void printMap() {
      TableOfContents.Section[] var1 = this.tableOfContents.sections;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         TableOfContents.Section section = var1[var3];
         if (section.off != -1) {
            System.out.println("section " + Integer.toHexString(section.type) + " off=" + Integer.toHexString(section.off) + " size=" + Integer.toHexString(section.size) + " byteCount=" + Integer.toHexString(section.byteCount));
         }
      }

   }

   private void printStrings() throws IOException {
      int index = 0;

      for(Iterator var2 = this.dex.strings().iterator(); var2.hasNext(); ++index) {
         String string = (String)var2.next();
         System.out.println("string " + index + ": " + string);
      }

   }

   private void printTypeIds() throws IOException {
      int index = 0;

      for(Iterator var2 = this.dex.typeIds().iterator(); var2.hasNext(); ++index) {
         Integer type = (Integer)var2.next();
         System.out.println("type " + index + ": " + (String)this.dex.strings().get(type));
      }

   }

   private void printProtoIds() throws IOException {
      int index = 0;

      for(Iterator var2 = this.dex.protoIds().iterator(); var2.hasNext(); ++index) {
         ProtoId protoId = (ProtoId)var2.next();
         System.out.println("proto " + index + ": " + protoId);
      }

   }

   private void printFieldIds() throws IOException {
      int index = 0;

      for(Iterator var2 = this.dex.fieldIds().iterator(); var2.hasNext(); ++index) {
         FieldId fieldId = (FieldId)var2.next();
         System.out.println("field " + index + ": " + fieldId);
      }

   }

   private void printMethodIds() throws IOException {
      int index = 0;

      for(Iterator var2 = this.dex.methodIds().iterator(); var2.hasNext(); ++index) {
         MethodId methodId = (MethodId)var2.next();
         System.out.println("methodId " + index + ": " + methodId);
      }

   }

   private void printTypeLists() throws IOException {
      if (this.tableOfContents.typeLists.off == -1) {
         System.out.println("No type lists");
      } else {
         Dex.Section in = this.dex.open(this.tableOfContents.typeLists.off);

         for(int i = 0; i < this.tableOfContents.typeLists.size; ++i) {
            int size = in.readInt();
            System.out.print("Type list i=" + i + ", size=" + size + ", elements=");

            for(int t = 0; t < size; ++t) {
               System.out.print(" " + (String)this.dex.typeNames().get(in.readShort()));
            }

            if (size % 2 == 1) {
               in.readShort();
            }

            System.out.println();
         }

      }
   }

   private void printClassDefs() {
      int index = 0;

      for(Iterator var2 = this.dex.classDefs().iterator(); var2.hasNext(); ++index) {
         ClassDef classDef = (ClassDef)var2.next();
         System.out.println("class def " + index + ": " + classDef);
      }

   }

   public static void main(String[] args) throws IOException {
      DexIndexPrinter indexPrinter = new DexIndexPrinter(new File(args[0]));
      indexPrinter.printMap();
      indexPrinter.printStrings();
      indexPrinter.printTypeIds();
      indexPrinter.printProtoIds();
      indexPrinter.printFieldIds();
      indexPrinter.printMethodIds();
      indexPrinter.printTypeLists();
      indexPrinter.printClassDefs();
   }
}
