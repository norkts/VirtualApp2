package com.android.dx.cf.iface;

import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;

public interface Member extends HasAttribute {
   CstType getDefiningClass();

   int getAccessFlags();

   CstString getName();

   CstString getDescriptor();

   CstNat getNat();

   AttributeList getAttributes();
}
