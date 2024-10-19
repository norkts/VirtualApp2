package com.android.dx.dex.code;

import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;

public final class SimpleInsn extends FixedSizeInsn {
   public SimpleInsn(Dop opcode, SourcePosition position, RegisterSpecList registers) {
      super(opcode, position, registers);
   }

   public DalvInsn withOpcode(Dop opcode) {
      return new SimpleInsn(opcode, this.getPosition(), this.getRegisters());
   }

   public DalvInsn withRegisters(RegisterSpecList registers) {
      return new SimpleInsn(this.getOpcode(), this.getPosition(), registers);
   }

   protected String argString() {
      return null;
   }
}
