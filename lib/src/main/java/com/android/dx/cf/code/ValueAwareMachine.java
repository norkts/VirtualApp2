package com.android.dx.cf.code;

import com.android.dx.rop.cst.CstCallSiteRef;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.util.Hex;

public class ValueAwareMachine extends BaseMachine {
   public ValueAwareMachine(Prototype prototype) {
      super(prototype);
   }

   public void run(Frame frame, int offset, int opcode) {
      Type type;
      label41:
      switch (opcode) {
         case 0:
         case 79:
         case 87:
         case 88:
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
         case 169:
         case 171:
         case 172:
         case 177:
         case 179:
         case 181:
         case 191:
         case 194:
         case 195:
         case 198:
         case 199:
            this.clearResult();
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
         case 19:
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
         case 170:
         case 173:
         case 174:
         case 175:
         case 176:
         case 196:
         default:
            throw new RuntimeException("shouldn't happen: " + Hex.u1(opcode));
         case 18:
         case 20:
            this.setResult((TypeBearer)this.getAuxCst());
            break;
         case 21:
         case 54:
            this.setResult(this.arg(0));
            break;
         case 46:
         case 96:
         case 100:
         case 104:
         case 108:
         case 112:
         case 116:
         case 120:
         case 122:
         case 124:
         case 126:
         case 128:
         case 130:
         case 132:
         case 133:
         case 134:
         case 135:
         case 136:
         case 137:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 145:
         case 146:
         case 147:
         case 148:
         case 149:
         case 150:
         case 151:
         case 152:
         case 190:
            this.setResult(this.getAuxType());
            break;
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
            this.clearResult();
            int pattern = this.getAuxInt();

            while(true) {
               if (pattern == 0) {
                  break label41;
               }

               int which = (pattern & 15) - 1;
               this.addResult(this.arg(which));
               pattern >>= 4;
            }
         case 168:
            this.setResult(new ReturnAddress(this.getAuxTarget()));
            break;
         case 178:
         case 180:
         case 182:
         case 184:
         case 185:
            type = ((TypeBearer)this.getAuxCst()).getType();
            if (type == Type.VOID) {
               this.clearResult();
            } else {
               this.setResult(type);
            }
            break;
         case 183:
            type = this.arg(0).getType();
            if (type.isUninitialized()) {
               frame.makeInitialized(type);
            }

            type = ((TypeBearer)this.getAuxCst()).getType();
            if (type == Type.VOID) {
               this.clearResult();
            } else {
               this.setResult(type);
            }
            break;
         case 186:
            type = ((CstCallSiteRef)this.getAuxCst()).getReturnType();
            if (type == Type.VOID) {
               this.clearResult();
            } else {
               this.setResult(type);
            }
            break;
         case 187:
            type = ((CstType)this.getAuxCst()).getClassType();
            this.setResult(type.asUninitialized(offset));
            break;
         case 188:
         case 192:
         case 197:
            type = ((CstType)this.getAuxCst()).getClassType();
            this.setResult(type);
            break;
         case 189:
            type = ((CstType)this.getAuxCst()).getClassType();
            this.setResult(type.getArrayType());
            break;
         case 193:
            this.setResult(Type.INT);
      }

      this.storeResults(frame);
   }
}
