package com.android.dx;

import com.android.dx.rop.cst.CstBoolean;
import com.android.dx.rop.cst.CstByte;
import com.android.dx.rop.cst.CstChar;
import com.android.dx.rop.cst.CstDouble;
import com.android.dx.rop.cst.CstFloat;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstKnownNull;
import com.android.dx.rop.cst.CstLong;
import com.android.dx.rop.cst.CstShort;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.cst.TypedConstant;

final class Constants {
   private Constants() {
   }

   static TypedConstant getConstant(Object value) {
      if (value == null) {
         return CstKnownNull.THE_ONE;
      } else if (value instanceof Boolean) {
         return CstBoolean.make((Boolean)value);
      } else if (value instanceof Byte) {
         return CstByte.make((Byte)value);
      } else if (value instanceof Character) {
         return CstChar.make((Character)value);
      } else if (value instanceof Double) {
         return CstDouble.make(Double.doubleToLongBits((Double)value));
      } else if (value instanceof Float) {
         return CstFloat.make(Float.floatToIntBits((Float)value));
      } else if (value instanceof Integer) {
         return CstInteger.make((Integer)value);
      } else if (value instanceof Long) {
         return CstLong.make((Long)value);
      } else if (value instanceof Short) {
         return CstShort.make((Short)value);
      } else if (value instanceof String) {
         return new CstString((String)value);
      } else if (value instanceof Class) {
         return new CstType(TypeId.get((Class)value).ropType);
      } else if (value instanceof TypeId) {
         return new CstType(((TypeId)value).ropType);
      } else {
         throw new UnsupportedOperationException("Not a constant: " + value);
      }
   }
}
