package com.android.dx.cf.attrib;

import com.android.dx.cf.code.LocalVariableList;

public final class AttLocalVariableTable extends BaseLocalVariables {
   public static final String ATTRIBUTE_NAME = "LocalVariableTable";

   public AttLocalVariableTable(LocalVariableList localVariables) {
      super("LocalVariableTable", localVariables);
   }
}
