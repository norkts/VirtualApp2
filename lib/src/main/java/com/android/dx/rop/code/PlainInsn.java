package com.android.dx.rop.code;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.rop.type.TypeList;

public final class PlainInsn extends Insn {
   public PlainInsn(Rop opcode, SourcePosition position, RegisterSpec result, RegisterSpecList sources) {
      super(opcode, position, result, sources);
      switch (opcode.getBranchingness()) {
         case 5:
         case 6:
            throw new IllegalArgumentException("opcode with invalid branchingness: " + opcode.getBranchingness());
         default:
            if (result != null && opcode.getBranchingness() != 1) {
               throw new IllegalArgumentException("can't mix branchingness with result");
            }
      }
   }

   public PlainInsn(Rop opcode, SourcePosition position, RegisterSpec result, RegisterSpec source) {
      this(opcode, position, result, RegisterSpecList.make(source));
   }

   public TypeList getCatches() {
      return StdTypeList.EMPTY;
   }

   public void accept(Insn.Visitor visitor) {
      visitor.visitPlainInsn(this);
   }

   public Insn withAddedCatch(Type type) {
      throw new UnsupportedOperationException("unsupported");
   }

   public Insn withRegisterOffset(int delta) {
      return new PlainInsn(this.getOpcode(), this.getPosition(), this.getResult().withOffset(delta), this.getSources().withOffset(delta));
   }

   public Insn withSourceLiteral() {
      RegisterSpecList sources = this.getSources();
      int szSources = sources.size();
      if (szSources == 0) {
         return this;
      } else {
         TypeBearer lastType = sources.get(szSources - 1).getTypeBearer();
         if (!lastType.isConstant()) {
            TypeBearer firstType = sources.get(0).getTypeBearer();
            if (szSources == 2 && firstType.isConstant()) {
               Constant cst = (Constant)firstType;
               RegisterSpecList newSources = sources.withoutFirst();
               Rop newRop = Rops.ropFor(this.getOpcode().getOpcode(), this.getResult(), newSources, cst);
               return new PlainCstInsn(newRop, this.getPosition(), this.getResult(), newSources, cst);
            } else {
               return this;
            }
         } else {
            Constant cst = (Constant)lastType;
            RegisterSpecList newSources = sources.withoutLast();

            Rop newRop;
            try {
               int opcode = this.getOpcode().getOpcode();
               if (opcode == 15 && cst instanceof CstInteger) {
                  opcode = 14;
                  cst = CstInteger.make(-((CstInteger)cst).getValue());
               }

               newRop = Rops.ropFor(opcode, this.getResult(), newSources, (Constant)cst);
            } catch (IllegalArgumentException var8) {
               return this;
            }

            return new PlainCstInsn(newRop, this.getPosition(), this.getResult(), newSources, (Constant)cst);
         }
      }
   }

   public Insn withNewRegisters(RegisterSpec result, RegisterSpecList sources) {
      return new PlainInsn(this.getOpcode(), this.getPosition(), result, sources);
   }
}
