package com.android.dx.dex.code;

import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;

public final class TargetInsn extends FixedSizeInsn {
   private CodeAddress target;

   public TargetInsn(Dop opcode, SourcePosition position, RegisterSpecList registers, CodeAddress target) {
      super(opcode, position, registers);
      if (target == null) {
         throw new NullPointerException("target == null");
      } else {
         this.target = target;
      }
   }

   public DalvInsn withOpcode(Dop opcode) {
      return new TargetInsn(opcode, this.getPosition(), this.getRegisters(), this.target);
   }

   public DalvInsn withRegisters(RegisterSpecList registers) {
      return new TargetInsn(this.getOpcode(), this.getPosition(), registers, this.target);
   }

   public TargetInsn withNewTargetAndReversed(CodeAddress target) {
      Dop opcode = this.getOpcode().getOppositeTest();
      return new TargetInsn(opcode, this.getPosition(), this.getRegisters(), target);
   }

   public CodeAddress getTarget() {
      return this.target;
   }

   public int getTargetAddress() {
      return this.target.getAddress();
   }

   public int getTargetOffset() {
      return this.target.getAddress() - this.getAddress();
   }

   public boolean hasTargetOffset() {
      return this.hasAddress() && this.target.hasAddress();
   }

   protected String argString() {
      return this.target == null ? "????" : this.target.identifierString();
   }
}
