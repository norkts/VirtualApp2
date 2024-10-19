package com.android.dx.dex.code;

import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.util.AnnotatedOutput;

public abstract class FixedSizeInsn extends DalvInsn {
   public FixedSizeInsn(Dop opcode, SourcePosition position, RegisterSpecList registers) {
      super(opcode, position, registers);
   }

   public final int codeSize() {
      return this.getOpcode().getFormat().codeSize();
   }

   public final void writeTo(AnnotatedOutput out) {
      this.getOpcode().getFormat().writeTo(out, this);
   }

   public final DalvInsn withRegisterOffset(int delta) {
      return this.withRegisters(this.getRegisters().withOffset(delta));
   }

   protected final String listingString0(boolean noteIndices) {
      return this.getOpcode().getFormat().listingString(this, noteIndices);
   }
}
