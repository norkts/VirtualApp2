package com.android.dx.cf.attrib;

import com.android.dx.cf.code.LocalVariableList;
import com.android.dx.util.MutabilityException;

public abstract class BaseLocalVariables extends BaseAttribute {
   private final LocalVariableList localVariables;

   public BaseLocalVariables(String name, LocalVariableList localVariables) {
      super(name);

      try {
         if (localVariables.isMutable()) {
            throw new MutabilityException("localVariables.isMutable()");
         }
      } catch (NullPointerException var4) {
         throw new NullPointerException("localVariables == null");
      }

      this.localVariables = localVariables;
   }

   public final int byteLength() {
      return 8 + this.localVariables.size() * 10;
   }

   public final LocalVariableList getLocalVariables() {
      return this.localVariables;
   }
}
