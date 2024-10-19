package com.android.dx.cf.code;

import com.android.dx.cf.attrib.AttCode;
import com.android.dx.cf.attrib.AttLineNumberTable;
import com.android.dx.cf.attrib.AttLocalVariableTable;
import com.android.dx.cf.attrib.AttLocalVariableTypeTable;
import com.android.dx.cf.iface.AttributeList;
import com.android.dx.cf.iface.ClassFile;
import com.android.dx.cf.iface.Method;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Prototype;

public final class ConcreteMethod implements Method {
   private final Method method;
   private final ClassFile classFile;
   private final AttCode attCode;
   private final LineNumberList lineNumbers;
   private final LocalVariableList localVariables;

   public ConcreteMethod(Method method, ClassFile classFile, boolean keepLines, boolean keepLocals) {
      this.method = method;
      this.classFile = classFile;
      AttributeList attribs = method.getAttributes();
      this.attCode = (AttCode)attribs.findFirst("Code");
      AttributeList codeAttribs = this.attCode.getAttributes();
      LineNumberList lnl = LineNumberList.EMPTY;
      if (keepLines) {
         for(AttLineNumberTable lnt = (AttLineNumberTable)codeAttribs.findFirst("LineNumberTable"); lnt != null; lnt = (AttLineNumberTable)codeAttribs.findNext(lnt)) {
            lnl = LineNumberList.concat(lnl, lnt.getLineNumbers());
         }
      }

      this.lineNumbers = lnl;
      LocalVariableList lvl = LocalVariableList.EMPTY;
      if (keepLocals) {
         for(AttLocalVariableTable lvt = (AttLocalVariableTable)codeAttribs.findFirst("LocalVariableTable"); lvt != null; lvt = (AttLocalVariableTable)codeAttribs.findNext(lvt)) {
            lvl = LocalVariableList.concat(lvl, lvt.getLocalVariables());
         }

         LocalVariableList typeList = LocalVariableList.EMPTY;

         for(AttLocalVariableTypeTable lvtt = (AttLocalVariableTypeTable)codeAttribs.findFirst("LocalVariableTypeTable"); lvtt != null; lvtt = (AttLocalVariableTypeTable)codeAttribs.findNext(lvtt)) {
            typeList = LocalVariableList.concat(typeList, lvtt.getLocalVariables());
         }

         if (typeList.size() != 0) {
            lvl = LocalVariableList.mergeDescriptorsAndSignatures(lvl, typeList);
         }
      }

      this.localVariables = lvl;
   }

   public CstString getSourceFile() {
      return this.classFile.getSourceFile();
   }

   public final boolean isDefaultOrStaticInterfaceMethod() {
      return (this.classFile.getAccessFlags() & 512) != 0 && !this.getNat().isClassInit();
   }

   public final boolean isStaticMethod() {
      return (this.getAccessFlags() & 8) != 0;
   }

   public CstNat getNat() {
      return this.method.getNat();
   }

   public CstString getName() {
      return this.method.getName();
   }

   public CstString getDescriptor() {
      return this.method.getDescriptor();
   }

   public int getAccessFlags() {
      return this.method.getAccessFlags();
   }

   public AttributeList getAttributes() {
      return this.method.getAttributes();
   }

   public CstType getDefiningClass() {
      return this.method.getDefiningClass();
   }

   public Prototype getEffectiveDescriptor() {
      return this.method.getEffectiveDescriptor();
   }

   public int getMaxStack() {
      return this.attCode.getMaxStack();
   }

   public int getMaxLocals() {
      return this.attCode.getMaxLocals();
   }

   public BytecodeArray getCode() {
      return this.attCode.getCode();
   }

   public ByteCatchList getCatches() {
      return this.attCode.getCatches();
   }

   public LineNumberList getLineNumbers() {
      return this.lineNumbers;
   }

   public LocalVariableList getLocalVariables() {
      return this.localVariables;
   }

   public SourcePosition makeSourcePosistion(int offset) {
      return new SourcePosition(this.getSourceFile(), offset, this.lineNumbers.pcToLine(offset));
   }
}
