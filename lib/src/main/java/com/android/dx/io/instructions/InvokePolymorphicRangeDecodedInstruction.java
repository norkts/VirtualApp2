package com.android.dx.io.instructions;

import com.android.dx.io.IndexType;

public class InvokePolymorphicRangeDecodedInstruction extends DecodedInstruction {
   private final int c;
   private final int registerCount;
   private final int protoIndex;

   public InvokePolymorphicRangeDecodedInstruction(InstructionCodec format, int opcode, int methodIndex, IndexType indexType, int c, int registerCount, int protoIndex) {
      super(format, opcode, methodIndex, indexType, 0, 0L);
      if (protoIndex != (short)protoIndex) {
         throw new IllegalArgumentException("protoIndex doesn't fit in a short: " + protoIndex);
      } else {
         this.c = c;
         this.registerCount = registerCount;
         this.protoIndex = protoIndex;
      }
   }

   public int getRegisterCount() {
      return this.registerCount;
   }

   public int getC() {
      return this.c;
   }

   public DecodedInstruction withProtoIndex(int newIndex, int newProtoIndex) {
      return new InvokePolymorphicRangeDecodedInstruction(this.getFormat(), this.getOpcode(), newIndex, this.getIndexType(), this.c, this.registerCount, newProtoIndex);
   }

   public DecodedInstruction withIndex(int newIndex) {
      throw new UnsupportedOperationException("use withProtoIndex to update both the method and proto indices for invoke-polymorphic/range");
   }

   public short getProtoIndex() {
      return (short)this.protoIndex;
   }
}
