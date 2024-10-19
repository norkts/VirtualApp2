package com.android.dx.dex.code;

import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstLiteral32;
import com.android.dx.rop.cst.CstLiteral64;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.ArrayList;

public final class ArrayData extends VariableSizeInsn {
   private final CodeAddress user;
   private final ArrayList<Constant> values;
   private final Constant arrayType;
   private final int elemWidth;
   private final int initLength;

   public ArrayData(SourcePosition position, CodeAddress user, ArrayList<Constant> values, Constant arrayType) {
      super(position, RegisterSpecList.EMPTY);
      if (user == null) {
         throw new NullPointerException("user == null");
      } else if (values == null) {
         throw new NullPointerException("values == null");
      } else {
         int sz = values.size();
         if (sz <= 0) {
            throw new IllegalArgumentException("Illegal number of init values");
         } else {
            this.arrayType = arrayType;
            if (arrayType != CstType.BYTE_ARRAY && arrayType != CstType.BOOLEAN_ARRAY) {
               if (arrayType != CstType.SHORT_ARRAY && arrayType != CstType.CHAR_ARRAY) {
                  if (arrayType != CstType.INT_ARRAY && arrayType != CstType.FLOAT_ARRAY) {
                     if (arrayType != CstType.LONG_ARRAY && arrayType != CstType.DOUBLE_ARRAY) {
                        throw new IllegalArgumentException("Unexpected constant type");
                     }

                     this.elemWidth = 8;
                  } else {
                     this.elemWidth = 4;
                  }
               } else {
                  this.elemWidth = 2;
               }
            } else {
               this.elemWidth = 1;
            }

            this.user = user;
            this.values = values;
            this.initLength = values.size();
         }
      }
   }

   public int codeSize() {
      int sz = this.initLength;
      return 4 + (sz * this.elemWidth + 1) / 2;
   }

   public void writeTo(AnnotatedOutput out) {
      int sz;
      sz = this.values.size();
      out.writeShort(768);
      out.writeShort(this.elemWidth);
      out.writeInt(this.initLength);
      int i;
      Constant cst;
      label48:
      switch (this.elemWidth) {
         case 1:
            i = 0;

            while(true) {
               if (i >= sz) {
                  break label48;
               }

               cst = (Constant)this.values.get(i);
               out.writeByte((byte)((CstLiteral32)cst).getIntBits());
               ++i;
            }
         case 2:
            for(i = 0; i < sz; ++i) {
               cst = (Constant)this.values.get(i);
               out.writeShort((short)((CstLiteral32)cst).getIntBits());
            }
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            break;
         case 4:
            i = 0;

            while(true) {
               if (i >= sz) {
                  break label48;
               }

               cst = (Constant)this.values.get(i);
               out.writeInt(((CstLiteral32)cst).getIntBits());
               ++i;
            }
         case 8:
            for(i = 0; i < sz; ++i) {
               cst = (Constant)this.values.get(i);
               out.writeLong(((CstLiteral64)cst).getLongBits());
            }
      }

      if (this.elemWidth == 1 && sz % 2 != 0) {
         out.writeByte(0);
      }

   }

   public DalvInsn withRegisters(RegisterSpecList registers) {
      return new ArrayData(this.getPosition(), this.user, this.values, this.arrayType);
   }

   protected String argString() {
      StringBuilder sb = new StringBuilder(100);
      int sz = this.values.size();

      for(int i = 0; i < sz; ++i) {
         sb.append("\n    ");
         sb.append(i);
         sb.append(": ");
         sb.append(((Constant)this.values.get(i)).toHuman());
      }

      return sb.toString();
   }

   protected String listingString0(boolean noteIndices) {
      int baseAddress = this.user.getAddress();
      StringBuilder sb = new StringBuilder(100);
      int sz = this.values.size();
      sb.append("fill-array-data-payload // for fill-array-data @ ");
      sb.append(Hex.u2(baseAddress));

      for(int i = 0; i < sz; ++i) {
         sb.append("\n  ");
         sb.append(i);
         sb.append(": ");
         sb.append(((Constant)this.values.get(i)).toHuman());
      }

      return sb.toString();
   }
}
