package com.android.dx.cf.iface;

import com.android.dx.cf.code.BootstrapMethodsList;
import com.android.dx.rop.cst.ConstantPool;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.TypeList;

public interface ClassFile extends HasAttribute {
   int getMagic();

   int getMinorVersion();

   int getMajorVersion();

   int getAccessFlags();

   CstType getThisClass();

   CstType getSuperclass();

   ConstantPool getConstantPool();

   TypeList getInterfaces();

   FieldList getFields();

   MethodList getMethods();

   AttributeList getAttributes();

   BootstrapMethodsList getBootstrapMethods();

   CstString getSourceFile();
}
