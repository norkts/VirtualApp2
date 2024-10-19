package com.android.dx.command.findusages;

import com.android.dex.ClassData;
import com.android.dex.ClassDef;
import com.android.dex.Dex;
import com.android.dex.FieldId;
import com.android.dex.MethodId;
import com.android.dx.io.CodeReader;
import com.android.dx.io.OpcodeInfo;
import com.android.dx.io.instructions.DecodedInstruction;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public final class FindUsages {
   private final Dex dex;
   private final Set<Integer> methodIds;
   private final Set<Integer> fieldIds;
   private final CodeReader codeReader = new CodeReader();
   private final PrintWriter out;
   private ClassDef currentClass;
   private ClassData.Method currentMethod;

   public FindUsages(final Dex dex, String declaredBy, String memberName, final PrintWriter out) {
      this.dex = dex;
      this.out = out;
      Set<Integer> typeStringIndexes = new HashSet();
      Set<Integer> memberNameIndexes = new HashSet();
      Pattern declaredByPattern = Pattern.compile(declaredBy);
      Pattern memberNamePattern = Pattern.compile(memberName);
      List<String> strings = dex.strings();

      for(int i = 0; i < strings.size(); ++i) {
         String string = (String)strings.get(i);
         if (declaredByPattern.matcher(string).matches()) {
            typeStringIndexes.add(i);
         }

         if (memberNamePattern.matcher(string).matches()) {
            memberNameIndexes.add(i);
         }
      }

      if (!typeStringIndexes.isEmpty() && !memberNameIndexes.isEmpty()) {
         this.methodIds = new HashSet();
         this.fieldIds = new HashSet();
         Iterator var13 = typeStringIndexes.iterator();

         while(var13.hasNext()) {
            int typeStringIndex = (Integer)var13.next();
            int typeIndex = Collections.binarySearch(dex.typeIds(), typeStringIndex);
            if (typeIndex >= 0) {
               this.methodIds.addAll(this.getMethodIds(dex, memberNameIndexes, typeIndex));
               this.fieldIds.addAll(this.getFieldIds(dex, memberNameIndexes, typeIndex));
            }
         }

         this.codeReader.setFieldVisitor(new CodeReader.Visitor() {
            public void visit(DecodedInstruction[] all, DecodedInstruction one) {
               int fieldId = one.getIndex();
               if (FindUsages.this.fieldIds.contains(fieldId)) {
                  out.println(FindUsages.this.location() + ": field reference " + dex.fieldIds().get(fieldId) + " (" + OpcodeInfo.getName(one.getOpcode()) + ")");
               }

            }
         });
         this.codeReader.setMethodVisitor(new CodeReader.Visitor() {
            public void visit(DecodedInstruction[] all, DecodedInstruction one) {
               int methodId = one.getIndex();
               if (FindUsages.this.methodIds.contains(methodId)) {
                  out.println(FindUsages.this.location() + ": method reference " + dex.methodIds().get(methodId) + " (" + OpcodeInfo.getName(one.getOpcode()) + ")");
               }

            }
         });
      } else {
         this.methodIds = this.fieldIds = null;
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

   public void findUsages() {
      if (this.fieldIds != null && this.methodIds != null) {
         Iterator var1 = this.dex.classDefs().iterator();

         while(true) {
            ClassDef classDef;
            do {
               if (!var1.hasNext()) {
                  this.currentClass = null;
                  this.currentMethod = null;
                  return;
               }

               classDef = (ClassDef)var1.next();
               this.currentClass = classDef;
               this.currentMethod = null;
            } while(classDef.getClassDataOffset() == 0);

            ClassData classData = this.dex.readClassData(classDef);
            ClassData.Field[] var4 = classData.allFields();
            int var5 = var4.length;

            int var6;
            int methodIndex;
            for(var6 = 0; var6 < var5; ++var6) {
               ClassData.Field field = var4[var6];
               methodIndex = field.getFieldIndex();
               if (this.fieldIds.contains(methodIndex)) {
                  this.out.println(this.location() + " field declared " + this.dex.fieldIds().get(methodIndex));
               }
            }

            ClassData.Method[] var9 = classData.allMethods();
            var5 = var9.length;

            for(var6 = 0; var6 < var5; ++var6) {
               ClassData.Method method = var9[var6];
               this.currentMethod = method;
               methodIndex = method.getMethodIndex();
               if (this.methodIds.contains(methodIndex)) {
                  this.out.println(this.location() + " method declared " + this.dex.methodIds().get(methodIndex));
               }

               if (method.getCodeOffset() != 0) {
                  this.codeReader.visitAll(this.dex.readCode(method).getInstructions());
               }
            }
         }
      }
   }

   private Set<Integer> getFieldIds(Dex dex, Set<Integer> memberNameIndexes, int declaringType) {
      Set<Integer> fields = new HashSet();
      int fieldIndex = 0;

      for(Iterator var6 = dex.fieldIds().iterator(); var6.hasNext(); ++fieldIndex) {
         FieldId fieldId = (FieldId)var6.next();
         if (memberNameIndexes.contains(fieldId.getNameIndex()) && declaringType == fieldId.getDeclaringClassIndex()) {
            fields.add(fieldIndex);
         }
      }

      return fields;
   }

   private Set<Integer> getMethodIds(Dex dex, Set<Integer> memberNameIndexes, int declaringType) {
      Set<Integer> subtypes = this.findAssignableTypes(dex, declaringType);
      Set<Integer> methods = new HashSet();
      int methodIndex = 0;

      for(Iterator var7 = dex.methodIds().iterator(); var7.hasNext(); ++methodIndex) {
         MethodId method = (MethodId)var7.next();
         if (memberNameIndexes.contains(method.getNameIndex()) && subtypes.contains(method.getDeclaringClassIndex())) {
            methods.add(methodIndex);
         }
      }

      return methods;
   }

   private Set<Integer> findAssignableTypes(Dex dex, int typeIndex) {
      Set<Integer> assignableTypes = new HashSet();
      assignableTypes.add(typeIndex);
      Iterator var4 = dex.classDefs().iterator();

      while(true) {
         while(var4.hasNext()) {
            ClassDef classDef = (ClassDef)var4.next();
            if (assignableTypes.contains(classDef.getSupertypeIndex())) {
               assignableTypes.add(classDef.getTypeIndex());
            } else {
               short[] var6 = classDef.getInterfaces();
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  int implemented = var6[var8];
                  if (assignableTypes.contains(Integer.valueOf(implemented))) {
                     assignableTypes.add(classDef.getTypeIndex());
                     break;
                  }
               }
            }
         }

         return assignableTypes;
      }
   }
}
