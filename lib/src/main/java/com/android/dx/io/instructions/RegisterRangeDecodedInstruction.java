package com.android.dx.io.instructions;

import com.android.dx.io.IndexType;

public final class RegisterRangeDecodedInstruction extends DecodedInstruction {
   private final int a;
   private final int registerCount;

   public RegisterRangeDecodedInstruction(InstructionCodec format, int opcode, int index, IndexType indexType, int target, long literal, int a, int registerCount) {
      super(format, opcode, index, indexType, target, literal);
      this.a = a;
      this.registerCount = registerCount;
   }

   public int getRegisterCount() {
      return this.registerCount;
   }

   public int getA() {
      return this.a;
   }

   public DecodedInstruction withIndex(int newIndex) {
      return new RegisterRangeDecodedInstruction(this.getFormat(), this.getOpcode(), newIndex, this.getIndexType(), this.getTarget(), this.getLiteral(), this.a, this.registerCount);
   }
}
