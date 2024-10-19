package com.android.dx.command.grep;

import com.android.dex.ClassData;
import com.android.dex.ClassDef;
import com.android.dex.Dex;
import com.android.dex.EncodedValueReader;
import com.android.dex.MethodId;
import com.android.dx.io.CodeReader;
import com.android.dx.io.instructions.DecodedInstruction;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

public final class Grep {
   private final Dex dex;
   private final CodeReader codeReader = new CodeReader();
   private final Set<Integer> stringIds;
   private final PrintWriter out;
   private int count = 0;
   private ClassDef currentClass;
   private ClassData.Method currentMethod;

   public Grep(Dex dex, Pattern pattern, PrintWriter out) {
      this.dex = dex;
      this.out = out;
      this.stringIds = this.getStringIds(dex, pattern);
      this.codeReader.setStringVisitor(new CodeReader.Visitor() {
         public void visit(DecodedInstruction[] all, DecodedInstruction one) {
            Grep.this.encounterString(one.getIndex());
         }
      });
   }

   private void readArray(EncodedValueReader reader) {
      int i = 0;

      for(int size = reader.readArray(); i < size; ++i) {
         switch (reader.peek()) {
            case 23:
               this.encounterString(reader.readString());
               break;
            case 28:
               this.readArray(reader);
         }
      }

   }

   private void encounterString(int index) {
      if (this.stringIds.contains(index)) {
         this.out.println(this.location() + " " + (String)this.dex.strings().get(index));
         ++this.count;
      }

   }

   private String location() {
      String className = (String)this.dex.typeNames().get(this.currentClass.getTypeIndex());
      if (this.currentMethod != null) {
         MethodId methodId = (MethodId)this.dex.methodIds().get(this.currentMethod.getMethodIndex());
         return className + "." + (String)this.dex.strings().get(methodId.getNameIndex());
      } else {
         return className;
      }
   }

   public int grep() {
      Iterator var1 = this.dex.classDefs().iterator();

      while(true) {
         ClassDef classDef;
         do {
            if (!var1.hasNext()) {
               this.currentClass = null;
               this.currentMethod = null;
               return this.count;
            }

            classDef = (ClassDef)var1.next();
            this.currentClass = classDef;
            this.currentMethod = null;
         } while(classDef.getClassDataOffset() == 0);

         ClassData classData = this.dex.readClassData(classDef);
         int staticValuesOffset = classDef.getStaticValuesOffset();
         if (staticValuesOffset != 0) {
            this.readArray(new EncodedValueReader(this.dex.open(staticValuesOffset)));
         }

         ClassData.Method[] var5 = classData.allMethods();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            ClassData.Method method = var5[var7];
            this.currentMethod = method;
            if (method.getCodeOffset() != 0) {
               this.codeReader.visitAll(this.dex.readCode(method).getInstructions());
            }
         }
      }
   }

   private Set<Integer> getStringIds(Dex dex, Pattern pattern) {
      Set<Integer> stringIds = new HashSet();
      int stringIndex = 0;

      for(Iterator var5 = dex.strings().iterator(); var5.hasNext(); ++stringIndex) {
         String s = (String)var5.next();
         if (pattern.matcher(s).find()) {
            stringIds.add(stringIndex);
         }
      }

      return stringIds;
   }
}
