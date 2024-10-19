package com.android.dx.cf.code;

import com.android.dx.dex.DexOptions;
import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstInterfaceMethodRef;
import com.android.dx.rop.cst.CstInvokeDynamic;
import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.Type;
import com.android.dx.util.Hex;
import java.util.ArrayList;

public class Simulator {
   private static final String LOCAL_MISMATCH_ERROR = "This is symptomatic of .class transformation tools that ignore local variable information.";
   private final Machine machine;
   private final BytecodeArray code;
   private ConcreteMethod method;
   private final LocalVariableList localVariables;
   private final SimVisitor visitor;
   private final DexOptions dexOptions;

   public Simulator(Machine machine, ConcreteMethod method, DexOptions dexOptions) {
      if (machine == null) {
         throw new NullPointerException("machine == null");
      } else if (method == null) {
         throw new NullPointerException("method == null");
      } else if (dexOptions == null) {
         throw new NullPointerException("dexOptions == null");
      } else {
         this.machine = machine;
         this.code = method.getCode();
         this.method = method;
         this.localVariables = method.getLocalVariables();
         this.visitor = new SimVisitor();
         this.dexOptions = dexOptions;
         if (method.isDefaultOrStaticInterfaceMethod()) {
            this.checkInterfaceMethodDeclaration(method);
         }

      }
   }

   public void simulate(ByteBlock bb, Frame frame) {
      int end = bb.getEnd();
      this.visitor.setFrame(frame);

      try {
         int length;
         for(int off = bb.getStart(); off < end; off += length) {
            length = this.code.parseInstruction(off, this.visitor);
            this.visitor.setPreviousOffset(off);
         }

      } catch (SimException var6) {
         SimException ex = var6;
         frame.annotate(ex);
         throw ex;
      }
   }

   public int simulate(int offset, Frame frame) {
      this.visitor.setFrame(frame);
      return this.code.parseInstruction(offset, this.visitor);
   }

   private static SimException illegalTos() {
      return new SimException("stack mismatch: illegal top-of-stack for opcode");
   }

   private static Type requiredArrayTypeFor(Type impliedType, Type foundArrayType) {
      if (foundArrayType == Type.KNOWN_NULL) {
         return impliedType.isReference() ? Type.KNOWN_NULL : impliedType.getArrayType();
      } else if (impliedType == Type.OBJECT && foundArrayType.isArray() && foundArrayType.getComponentType().isReference()) {
         return foundArrayType;
      } else {
         return impliedType == Type.BYTE && foundArrayType == Type.BOOLEAN_ARRAY ? Type.BOOLEAN_ARRAY : impliedType.getArrayType();
      }
   }

   private void checkConstMethodHandleSupported(Constant cst) throws SimException {
      if (!this.dexOptions.apiIsSupported(28)) {
         this.fail(String.format("invalid constant type %s requires --min-sdk-version >= %d (currently %d)", cst.typeName(), 28, this.dexOptions.minSdkVersion));
      }

   }

   private void checkInvokeDynamicSupported(int opcode) throws SimException {
      if (!this.dexOptions.apiIsSupported(26)) {
         this.fail(String.format("invalid opcode %02x - invokedynamic requires --min-sdk-version >= %d (currently %d)", opcode, 26, this.dexOptions.minSdkVersion));
      }

   }

   private void checkInvokeInterfaceSupported(int opcode, CstMethodRef callee) {
      if (opcode != 185) {
         if (!this.dexOptions.apiIsSupported(24)) {
            boolean softFail = this.dexOptions.allowAllInterfaceMethodInvokes;
            if (opcode == 184) {
               softFail &= this.dexOptions.apiIsSupported(21);
            } else {
               assert opcode == 183 || opcode == 182;
            }

            String invokeKind = opcode == 184 ? "static" : "default";
            String reason;
            if (softFail) {
               reason = String.format("invoking a %s interface method %s.%s strictly requires --min-sdk-version >= %d (experimental at current API level %d)", invokeKind, callee.getDefiningClass().toHuman(), callee.getNat().toHuman(), 24, this.dexOptions.minSdkVersion);
               this.warn(reason);
            } else {
               reason = String.format("invoking a %s interface method %s.%s strictly requires --min-sdk-version >= %d (blocked at current API level %d)", invokeKind, callee.getDefiningClass().toHuman(), callee.getNat().toHuman(), 24, this.dexOptions.minSdkVersion);
               this.fail(reason);
            }

         }
      }
   }

   private void checkInterfaceMethodDeclaration(ConcreteMethod declaredMethod) {
      if (!this.dexOptions.apiIsSupported(24)) {
         String reason = String.format("defining a %s interface method requires --min-sdk-version >= %d (currently %d) for interface methods: %s.%s", declaredMethod.isStaticMethod() ? "static" : "default", 24, this.dexOptions.minSdkVersion, declaredMethod.getDefiningClass().toHuman(), declaredMethod.getNat().toHuman());
         this.warn(reason);
      }

   }

   private void checkInvokeSignaturePolymorphic(int opcode) {
      if (!this.dexOptions.apiIsSupported(26)) {
         this.fail(String.format("invoking a signature-polymorphic requires --min-sdk-version >= %d (currently %d)", 26, this.dexOptions.minSdkVersion));
      } else if (opcode != 182) {
         this.fail("Unsupported signature polymorphic invocation (" + ByteOps.opName(opcode) + ")");
      }

   }

   private void fail(String reason) {
      String message = String.format("ERROR in %s.%s: %s", this.method.getDefiningClass().toHuman(), this.method.getNat().toHuman(), reason);
      throw new SimException(message);
   }

   private void warn(String reason) {
      String warning = String.format("WARNING in %s.%s: %s", this.method.getDefiningClass().toHuman(), this.method.getNat().toHuman(), reason);
      this.dexOptions.err.println(warning);
   }

   private class SimVisitor implements BytecodeArray.Visitor {
      private final Machine machine;
      private Frame frame;
      private int previousOffset;

      public SimVisitor() {
         this.machine = Simulator.this.machine;
         this.frame = null;
      }

      public void setFrame(Frame frame) {
         if (frame == null) {
            throw new NullPointerException("frame == null");
         } else {
            this.frame = frame;
         }
      }

      public void visitInvalid(int opcode, int offset, int length) {
         throw new SimException("invalid opcode " + Hex.u1(opcode));
      }

      public void visitNoArgs(int opcode, int offset, int length, Type type) {
         Type arrayType;
         int pattern;
         ExecutionStack stack;
         switch (opcode) {
            case 0:
               this.machine.clearArgs();
               break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 97:
            case 98:
            case 99:
            case 101:
            case 102:
            case 103:
            case 105:
            case 106:
            case 107:
            case 109:
            case 110:
            case 111:
            case 113:
            case 114:
            case 115:
            case 117:
            case 118:
            case 119:
            case 121:
            case 123:
            case 125:
            case 127:
            case 129:
            case 131:
            case 132:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 169:
            case 170:
            case 171:
            case 173:
            case 174:
            case 175:
            case 176:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 192:
            case 193:
            default:
               this.visitInvalid(opcode, offset, length);
               return;
            case 46:
               arrayType = this.frame.getStack().peekType(1);
               Type requiredArrayType = Simulator.requiredArrayTypeFor(type, arrayType);
               type = requiredArrayType == Type.KNOWN_NULL ? Type.KNOWN_NULL : requiredArrayType.getComponentType();
               this.machine.popArgs(this.frame, requiredArrayType, Type.INT);
               break;
            case 79:
               stack = this.frame.getStack();
               pattern = type.isCategory1() ? 2 : 3;
               Type foundArrayType = stack.peekType(pattern);
               boolean foundArrayLocal = stack.peekLocal(pattern);
               Type requiredArrayTypex = Simulator.requiredArrayTypeFor(type, foundArrayType);
               if (foundArrayLocal) {
                  type = requiredArrayTypex == Type.KNOWN_NULL ? Type.KNOWN_NULL : requiredArrayTypex.getComponentType();
               }

               this.machine.popArgs(this.frame, requiredArrayTypex, Type.INT, type);
               break;
            case 87:
               arrayType = this.frame.getStack().peekType(0);
               if (arrayType.isCategory2()) {
                  throw Simulator.illegalTos();
               }

               this.machine.popArgs(this.frame, 1);
               break;
            case 88:
            case 92:
               stack = this.frame.getStack();
               if (stack.peekType(0).isCategory2()) {
                  this.machine.popArgs(this.frame, 1);
                  pattern = 17;
               } else {
                  if (!stack.peekType(1).isCategory1()) {
                     throw Simulator.illegalTos();
                  }

                  this.machine.popArgs(this.frame, 2);
                  pattern = 8481;
               }

               if (opcode == 92) {
                  this.machine.auxIntArg(pattern);
               }
               break;
            case 89:
               arrayType = this.frame.getStack().peekType(0);
               if (arrayType.isCategory2()) {
                  throw Simulator.illegalTos();
               }

               this.machine.popArgs(this.frame, 1);
               this.machine.auxIntArg(17);
               break;
            case 90:
               stack = this.frame.getStack();
               if (!stack.peekType(0).isCategory1() || !stack.peekType(1).isCategory1()) {
                  throw Simulator.illegalTos();
               }

               this.machine.popArgs(this.frame, 2);
               this.machine.auxIntArg(530);
               break;
            case 91:
               stack = this.frame.getStack();
               if (stack.peekType(0).isCategory2()) {
                  throw Simulator.illegalTos();
               }

               if (stack.peekType(1).isCategory2()) {
                  this.machine.popArgs(this.frame, 2);
                  this.machine.auxIntArg(530);
               } else {
                  if (!stack.peekType(2).isCategory1()) {
                     throw Simulator.illegalTos();
                  }

                  this.machine.popArgs(this.frame, 3);
                  this.machine.auxIntArg(12819);
               }
               break;
            case 93:
               stack = this.frame.getStack();
               if (stack.peekType(0).isCategory2()) {
                  if (stack.peekType(2).isCategory2()) {
                     throw Simulator.illegalTos();
                  }

                  this.machine.popArgs(this.frame, 2);
                  this.machine.auxIntArg(530);
               } else {
                  if (stack.peekType(1).isCategory2() || stack.peekType(2).isCategory2()) {
                     throw Simulator.illegalTos();
                  }

                  this.machine.popArgs(this.frame, 3);
                  this.machine.auxIntArg(205106);
               }
               break;
            case 94:
               stack = this.frame.getStack();
               if (stack.peekType(0).isCategory2()) {
                  if (stack.peekType(2).isCategory2()) {
                     this.machine.popArgs(this.frame, 2);
                     this.machine.auxIntArg(530);
                  } else {
                     if (!stack.peekType(3).isCategory1()) {
                        throw Simulator.illegalTos();
                     }

                     this.machine.popArgs(this.frame, 3);
                     this.machine.auxIntArg(12819);
                  }
               } else {
                  if (!stack.peekType(1).isCategory1()) {
                     throw Simulator.illegalTos();
                  }

                  if (stack.peekType(2).isCategory2()) {
                     this.machine.popArgs(this.frame, 3);
                     this.machine.auxIntArg(205106);
                  } else {
                     if (!stack.peekType(3).isCategory1()) {
                        throw Simulator.illegalTos();
                     }

                     this.machine.popArgs(this.frame, 4);
                     this.machine.auxIntArg(4399427);
                  }
               }
               break;
            case 95:
               stack = this.frame.getStack();
               if (!stack.peekType(0).isCategory1() || !stack.peekType(1).isCategory1()) {
                  throw Simulator.illegalTos();
               }

               this.machine.popArgs(this.frame, 2);
               this.machine.auxIntArg(18);
               break;
            case 96:
            case 100:
            case 104:
            case 108:
            case 112:
            case 126:
            case 128:
            case 130:
               this.machine.popArgs(this.frame, type, type);
               break;
            case 116:
               this.machine.popArgs(this.frame, type);
               break;
            case 120:
            case 122:
            case 124:
               this.machine.popArgs(this.frame, type, Type.INT);
               break;
            case 133:
            case 134:
            case 135:
            case 145:
            case 146:
            case 147:
               this.machine.popArgs(this.frame, Type.INT);
               break;
            case 136:
            case 137:
            case 138:
               this.machine.popArgs(this.frame, Type.LONG);
               break;
            case 139:
            case 140:
            case 141:
               this.machine.popArgs(this.frame, Type.FLOAT);
               break;
            case 142:
            case 143:
            case 144:
               this.machine.popArgs(this.frame, Type.DOUBLE);
               break;
            case 148:
               this.machine.popArgs(this.frame, Type.LONG, Type.LONG);
               break;
            case 149:
            case 150:
               this.machine.popArgs(this.frame, Type.FLOAT, Type.FLOAT);
               break;
            case 151:
            case 152:
               this.machine.popArgs(this.frame, Type.DOUBLE, Type.DOUBLE);
               break;
            case 172:
               arrayType = type;
               if (type == Type.OBJECT) {
                  arrayType = this.frame.getStack().peekType(0);
               }

               this.machine.popArgs(this.frame, type);
               this.checkReturnType(arrayType);
               break;
            case 177:
               this.machine.clearArgs();
               this.checkReturnType(Type.VOID);
               break;
            case 190:
               arrayType = this.frame.getStack().peekType(0);
               if (!arrayType.isArrayOrKnownNull()) {
                  Simulator.this.fail("type mismatch: expected array type but encountered " + arrayType.toHuman());
               }

               this.machine.popArgs(this.frame, Type.OBJECT);
               break;
            case 191:
            case 194:
            case 195:
               this.machine.popArgs(this.frame, Type.OBJECT);
         }

         this.machine.auxType(type);
         this.machine.run(this.frame, offset, opcode);
      }

      private void checkReturnType(Type encountered) {
         Type returnType = this.machine.getPrototype().getReturnType();
         if (!Merger.isPossiblyAssignableFrom(returnType, encountered)) {
            Simulator.this.fail("return type mismatch: prototype indicates " + returnType.toHuman() + ", but encountered type " + encountered.toHuman());
         }

      }

      public void visitLocal(int opcode, int offset, int length, int idx, Type type, int value) {
         int localOffset = opcode == 54 ? offset + length : offset;
         LocalVariableList.Item local = Simulator.this.localVariables.pcAndIndexToLocal(localOffset, idx);
         Type localType;
         if (local != null) {
            localType = local.getType();
            if (localType.getBasicFrameType() != type.getBasicFrameType()) {
               local = null;
               localType = type;
            }
         } else {
            localType = type;
         }

         LocalItem item;
         switch (opcode) {
            case 21:
            case 169:
               this.machine.localArg(this.frame, idx);
               this.machine.localInfo(local != null);
               this.machine.auxType(type);
               break;
            case 54:
               item = local == null ? null : local.getLocalItem();
               this.machine.popArgs(this.frame, type);
               this.machine.auxType(type);
               this.machine.localTarget(idx, localType, item);
               break;
            case 132:
               item = local == null ? null : local.getLocalItem();
               this.machine.localArg(this.frame, idx);
               this.machine.localTarget(idx, localType, item);
               this.machine.auxType(type);
               this.machine.auxIntArg(value);
               this.machine.auxCstArg(CstInteger.make(value));
               break;
            default:
               this.visitInvalid(opcode, offset, length);
               return;
         }

         this.machine.run(this.frame, offset, opcode);
      }

      public void visitConstant(int opcode, int offset, int length, Constant cst, int value) {
         Prototype prototypex;
         Type fieldType;
         switch (opcode) {
            case 18:
            case 19:
               if (cst instanceof CstMethodHandle || cst instanceof CstProtoRef) {
                  Simulator.this.checkConstMethodHandleSupported((Constant)cst);
               }

               this.machine.clearArgs();
               break;
            case 179:
               fieldType = ((CstFieldRef)cst).getType();
               this.machine.popArgs(this.frame, fieldType);
               break;
            case 180:
            case 192:
            case 193:
               this.machine.popArgs(this.frame, Type.OBJECT);
               break;
            case 181:
               fieldType = ((CstFieldRef)cst).getType();
               this.machine.popArgs(this.frame, Type.OBJECT, fieldType);
               break;
            case 182:
            case 183:
            case 184:
            case 185:
               if (cst instanceof CstInterfaceMethodRef) {
                  cst = ((CstInterfaceMethodRef)cst).toMethodRef();
                  Simulator.this.checkInvokeInterfaceSupported(opcode, (CstMethodRef)cst);
               }

               if (cst instanceof CstMethodRef) {
                  CstMethodRef methodRef = (CstMethodRef)cst;
                  if (methodRef.isSignaturePolymorphic()) {
                     Simulator.this.checkInvokeSignaturePolymorphic(opcode);
                  }
               }

               boolean staticMethod = opcode == 184;
               prototypex = ((CstMethodRef)cst).getPrototype(staticMethod);
               this.machine.popArgs(this.frame, prototypex);
               break;
            case 186:
               Simulator.this.checkInvokeDynamicSupported(opcode);
               CstInvokeDynamic invokeDynamicRef = (CstInvokeDynamic)cst;
               prototypex = invokeDynamicRef.getPrototype();
               this.machine.popArgs(this.frame, prototypex);
               cst = invokeDynamicRef.addReference();
               break;
            case 189:
               this.machine.popArgs(this.frame, Type.INT);
               break;
            case 197:
               Prototype prototype = Prototype.internInts(Type.VOID, value);
               this.machine.popArgs(this.frame, prototype);
               break;
            default:
               this.machine.clearArgs();
         }

         this.machine.auxIntArg(value);
         this.machine.auxCstArg((Constant)cst);
         this.machine.run(this.frame, offset, opcode);
      }

      public void visitBranch(int opcode, int offset, int length, int target) {
         switch (opcode) {
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
               this.machine.popArgs(this.frame, Type.INT);
               break;
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
               this.machine.popArgs(this.frame, Type.INT, Type.INT);
               break;
            case 165:
            case 166:
               this.machine.popArgs(this.frame, Type.OBJECT, Type.OBJECT);
               break;
            case 167:
            case 168:
            case 200:
            case 201:
               this.machine.clearArgs();
               break;
            case 169:
            case 170:
            case 171:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case 192:
            case 193:
            case 194:
            case 195:
            case 196:
            case 197:
            default:
               this.visitInvalid(opcode, offset, length);
               return;
            case 198:
            case 199:
               this.machine.popArgs(this.frame, Type.OBJECT);
         }

         this.machine.auxTargetArg(target);
         this.machine.run(this.frame, offset, opcode);
      }

      public void visitSwitch(int opcode, int offset, int length, SwitchList cases, int padding) {
         this.machine.popArgs(this.frame, Type.INT);
         this.machine.auxIntArg(padding);
         this.machine.auxSwitchArg(cases);
         this.machine.run(this.frame, offset, opcode);
      }

      public void visitNewarray(int offset, int length, CstType type, ArrayList<Constant> initValues) {
         this.machine.popArgs(this.frame, Type.INT);
         this.machine.auxInitValues(initValues);
         this.machine.auxCstArg(type);
         this.machine.run(this.frame, offset, 188);
      }

      public void setPreviousOffset(int offset) {
         this.previousOffset = offset;
      }

      public int getPreviousOffset() {
         return this.previousOffset;
      }
   }
}
