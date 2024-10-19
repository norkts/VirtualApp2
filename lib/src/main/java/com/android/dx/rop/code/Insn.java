package com.android.dx.rop.code;

import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.ToHuman;

public abstract class Insn implements ToHuman {
   private final Rop opcode;
   private final SourcePosition position;
   private final RegisterSpec result;
   private final RegisterSpecList sources;

   public Insn(Rop opcode, SourcePosition position, RegisterSpec result, RegisterSpecList sources) {
      if (opcode == null) {
         throw new NullPointerException("opcode == null");
      } else if (position == null) {
         throw new NullPointerException("position == null");
      } else if (sources == null) {
         throw new NullPointerException("sources == null");
      } else {
         this.opcode = opcode;
         this.position = position;
         this.result = result;
         this.sources = sources;
      }
   }

   public final boolean equals(Object other) {
      return this == other;
   }

   public final int hashCode() {
      return System.identityHashCode(this);
   }

   public String toString() {
      return this.toStringWithInline(this.getInlineString());
   }

   public String toHuman() {
      return this.toHumanWithInline(this.getInlineString());
   }

   public String getInlineString() {
      return null;
   }

   public final Rop getOpcode() {
      return this.opcode;
   }

   public final SourcePosition getPosition() {
      return this.position;
   }

   public final RegisterSpec getResult() {
      return this.result;
   }

   public final RegisterSpec getLocalAssignment() {
      RegisterSpec assignment;
      if (this.opcode.getOpcode() == 54) {
         assignment = this.sources.get(0);
      } else {
         assignment = this.result;
      }

      if (assignment == null) {
         return null;
      } else {
         LocalItem localItem = assignment.getLocalItem();
         return localItem == null ? null : assignment;
      }
   }

   public final RegisterSpecList getSources() {
      return this.sources;
   }

   public final boolean canThrow() {
      return this.opcode.canThrow();
   }

   public abstract TypeList getCatches();

   public abstract void accept(Visitor var1);

   public abstract Insn withAddedCatch(Type var1);

   public abstract Insn withRegisterOffset(int var1);

   public Insn withSourceLiteral() {
      return this;
   }

   public Insn copy() {
      return this.withRegisterOffset(0);
   }

   private static boolean equalsHandleNulls(Object a, Object b) {
      return a == b || a != null && a.equals(b);
   }

   public boolean contentEquals(Insn b) {
      return this.opcode == b.getOpcode() && this.position.equals(b.getPosition()) && this.getClass() == b.getClass() && equalsHandleNulls(this.result, b.getResult()) && equalsHandleNulls(this.sources, b.getSources()) && StdTypeList.equalContents(this.getCatches(), b.getCatches());
   }

   public abstract Insn withNewRegisters(RegisterSpec var1, RegisterSpecList var2);

   protected final String toStringWithInline(String extra) {
      StringBuilder sb = new StringBuilder(80);
      sb.append("Insn{");
      sb.append(this.position);
      sb.append(' ');
      sb.append(this.opcode);
      if (extra != null) {
         sb.append(' ');
         sb.append(extra);
      }

      sb.append(" :: ");
      if (this.result != null) {
         sb.append(this.result);
         sb.append(" <- ");
      }

      sb.append(this.sources);
      sb.append('}');
      return sb.toString();
   }

   protected final String toHumanWithInline(String extra) {
      StringBuilder sb = new StringBuilder(80);
      sb.append(this.position);
      sb.append(": ");
      sb.append(this.opcode.getNickname());
      if (extra != null) {
         sb.append("(");
         sb.append(extra);
         sb.append(")");
      }

      if (this.result == null) {
         sb.append(" .");
      } else {
         sb.append(" ");
         sb.append(this.result.toHuman());
      }

      sb.append(" <-");
      int sz = this.sources.size();
      if (sz == 0) {
         sb.append(" .");
      } else {
         for(int i = 0; i < sz; ++i) {
            sb.append(" ");
            sb.append(this.sources.get(i).toHuman());
         }
      }

      return sb.toString();
   }

   public static class BaseVisitor implements Visitor {
      public void visitPlainInsn(PlainInsn insn) {
      }

      public void visitPlainCstInsn(PlainCstInsn insn) {
      }

      public void visitSwitchInsn(SwitchInsn insn) {
      }

      public void visitThrowingCstInsn(ThrowingCstInsn insn) {
      }

      public void visitThrowingInsn(ThrowingInsn insn) {
      }

      public void visitFillArrayDataInsn(FillArrayDataInsn insn) {
      }

      public void visitInvokePolymorphicInsn(InvokePolymorphicInsn insn) {
      }
   }

   public interface Visitor {
      void visitPlainInsn(PlainInsn var1);

      void visitPlainCstInsn(PlainCstInsn var1);

      void visitSwitchInsn(SwitchInsn var1);

      void visitThrowingCstInsn(ThrowingCstInsn var1);

      void visitThrowingInsn(ThrowingInsn var1);

      void visitFillArrayDataInsn(FillArrayDataInsn var1);

      void visitInvokePolymorphicInsn(InvokePolymorphicInsn var1);
   }
}
