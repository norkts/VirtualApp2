package com.android.dx.cf.code;

import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import java.util.ArrayList;

public abstract class BaseMachine implements Machine {
   private final Prototype prototype;
   private TypeBearer[] args;
   private int argCount;
   private Type auxType;
   private int auxInt;
   private Constant auxCst;
   private int auxTarget;
   private SwitchList auxCases;
   private ArrayList<Constant> auxInitValues;
   private int localIndex;
   private boolean localInfo;
   private RegisterSpec localTarget;
   private TypeBearer[] results;
   private int resultCount;

   public BaseMachine(Prototype prototype) {
      if (prototype == null) {
         throw new NullPointerException("prototype == null");
      } else {
         this.prototype = prototype;
         this.args = new TypeBearer[10];
         this.results = new TypeBearer[6];
         this.clearArgs();
      }
   }

   public Prototype getPrototype() {
      return this.prototype;
   }

   public final void clearArgs() {
      this.argCount = 0;
      this.auxType = null;
      this.auxInt = 0;
      this.auxCst = null;
      this.auxTarget = 0;
      this.auxCases = null;
      this.auxInitValues = null;
      this.localIndex = -1;
      this.localInfo = false;
      this.localTarget = null;
      this.resultCount = -1;
   }

   public final void popArgs(Frame frame, int count) {
      ExecutionStack stack = frame.getStack();
      this.clearArgs();
      if (count > this.args.length) {
         this.args = new TypeBearer[count + 10];
      }

      for(int i = count - 1; i >= 0; --i) {
         this.args[i] = stack.pop();
      }

      this.argCount = count;
   }

   public void popArgs(Frame frame, Prototype prototype) {
      StdTypeList types = prototype.getParameterTypes();
      int size = types.size();
      this.popArgs(frame, size);

      for(int i = 0; i < size; ++i) {
         if (!Merger.isPossiblyAssignableFrom(types.getType(i), this.args[i])) {
            throw new SimException("at stack depth " + (size - 1 - i) + ", expected type " + types.getType(i).toHuman() + " but found " + this.args[i].getType().toHuman());
         }
      }

   }

   public final void popArgs(Frame frame, Type type) {
      this.popArgs(frame, 1);
      if (!Merger.isPossiblyAssignableFrom(type, this.args[0])) {
         throw new SimException("expected type " + type.toHuman() + " but found " + this.args[0].getType().toHuman());
      }
   }

   public final void popArgs(Frame frame, Type type1, Type type2) {
      this.popArgs(frame, 2);
      if (!Merger.isPossiblyAssignableFrom(type1, this.args[0])) {
         throw new SimException("expected type " + type1.toHuman() + " but found " + this.args[0].getType().toHuman());
      } else if (!Merger.isPossiblyAssignableFrom(type2, this.args[1])) {
         throw new SimException("expected type " + type2.toHuman() + " but found " + this.args[1].getType().toHuman());
      }
   }

   public final void popArgs(Frame frame, Type type1, Type type2, Type type3) {
      this.popArgs(frame, 3);
      if (!Merger.isPossiblyAssignableFrom(type1, this.args[0])) {
         throw new SimException("expected type " + type1.toHuman() + " but found " + this.args[0].getType().toHuman());
      } else if (!Merger.isPossiblyAssignableFrom(type2, this.args[1])) {
         throw new SimException("expected type " + type2.toHuman() + " but found " + this.args[1].getType().toHuman());
      } else if (!Merger.isPossiblyAssignableFrom(type3, this.args[2])) {
         throw new SimException("expected type " + type3.toHuman() + " but found " + this.args[2].getType().toHuman());
      }
   }

   public final void localArg(Frame frame, int idx) {
      this.clearArgs();
      this.args[0] = frame.getLocals().get(idx);
      this.argCount = 1;
      this.localIndex = idx;
   }

   public final void localInfo(boolean local) {
      this.localInfo = local;
   }

   public final void auxType(Type type) {
      this.auxType = type;
   }

   public final void auxIntArg(int value) {
      this.auxInt = value;
   }

   public final void auxCstArg(Constant cst) {
      if (cst == null) {
         throw new NullPointerException("cst == null");
      } else {
         this.auxCst = cst;
      }
   }

   public final void auxTargetArg(int target) {
      this.auxTarget = target;
   }

   public final void auxSwitchArg(SwitchList cases) {
      if (cases == null) {
         throw new NullPointerException("cases == null");
      } else {
         this.auxCases = cases;
      }
   }

   public final void auxInitValues(ArrayList<Constant> initValues) {
      this.auxInitValues = initValues;
   }

   public final void localTarget(int idx, Type type, LocalItem local) {
      this.localTarget = RegisterSpec.makeLocalOptional(idx, type, local);
   }

   protected final int argCount() {
      return this.argCount;
   }

   protected final int argWidth() {
      int result = 0;

      for(int i = 0; i < this.argCount; ++i) {
         result += this.args[i].getType().getCategory();
      }

      return result;
   }

   protected final TypeBearer arg(int n) {
      if (n >= this.argCount) {
         throw new IllegalArgumentException("n >= argCount");
      } else {
         try {
            return this.args[n];
         } catch (ArrayIndexOutOfBoundsException var3) {
            throw new IllegalArgumentException("n < 0");
         }
      }
   }

   protected final Type getAuxType() {
      return this.auxType;
   }

   protected final int getAuxInt() {
      return this.auxInt;
   }

   protected final Constant getAuxCst() {
      return this.auxCst;
   }

   protected final int getAuxTarget() {
      return this.auxTarget;
   }

   protected final SwitchList getAuxCases() {
      return this.auxCases;
   }

   protected final ArrayList<Constant> getInitValues() {
      return this.auxInitValues;
   }

   protected final int getLocalIndex() {
      return this.localIndex;
   }

   protected final boolean getLocalInfo() {
      return this.localInfo;
   }

   protected final RegisterSpec getLocalTarget(boolean isMove) {
      if (this.localTarget == null) {
         return null;
      } else if (this.resultCount != 1) {
         throw new SimException("local target with " + (this.resultCount == 0 ? "no" : "multiple") + " results");
      } else {
         TypeBearer result = this.results[0];
         Type resultType = result.getType();
         Type localType = this.localTarget.getType();
         if (resultType == localType) {
            return isMove ? this.localTarget.withType(result) : this.localTarget;
         } else if (!Merger.isPossiblyAssignableFrom(localType, resultType)) {
            throwLocalMismatch(resultType, localType);
            return null;
         } else {
            if (localType == Type.OBJECT) {
               this.localTarget = this.localTarget.withType(result);
            }

            return this.localTarget;
         }
      }
   }

   protected final void clearResult() {
      this.resultCount = 0;
   }

   protected final void setResult(TypeBearer result) {
      if (result == null) {
         throw new NullPointerException("result == null");
      } else {
         this.results[0] = result;
         this.resultCount = 1;
      }
   }

   protected final void addResult(TypeBearer result) {
      if (result == null) {
         throw new NullPointerException("result == null");
      } else {
         this.results[this.resultCount] = result;
         ++this.resultCount;
      }
   }

   protected final int resultCount() {
      if (this.resultCount < 0) {
         throw new SimException("results never set");
      } else {
         return this.resultCount;
      }
   }

   protected final int resultWidth() {
      int width = 0;

      for(int i = 0; i < this.resultCount; ++i) {
         width += this.results[i].getType().getCategory();
      }

      return width;
   }

   protected final TypeBearer result(int n) {
      if (n >= this.resultCount) {
         throw new IllegalArgumentException("n >= resultCount");
      } else {
         try {
            return this.results[n];
         } catch (ArrayIndexOutOfBoundsException var3) {
            throw new IllegalArgumentException("n < 0");
         }
      }
   }

   protected final void storeResults(Frame frame) {
      if (this.resultCount < 0) {
         throw new SimException("results never set");
      } else if (this.resultCount != 0) {
         if (this.localTarget != null) {
            frame.getLocals().set(this.getLocalTarget(false));
         } else {
            ExecutionStack stack = frame.getStack();

            for(int i = 0; i < this.resultCount; ++i) {
               if (this.localInfo) {
                  stack.setLocal();
               }

               stack.push(this.results[i]);
            }
         }

      }
   }

   public static void throwLocalMismatch(TypeBearer found, TypeBearer local) {
      throw new SimException("local variable type mismatch: attempt to set or access a value of type " + found.toHuman() + " using a local variable of type " + local.toHuman() + ". This is symptomatic of .class transformation tools that ignore local variable information.");
   }
}
