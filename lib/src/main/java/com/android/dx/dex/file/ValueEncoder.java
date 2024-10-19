package com.android.dx.dex.file;

import com.android.dex.EncodedValueCodec;
import com.android.dx.rop.annotation.Annotation;
import com.android.dx.rop.annotation.NameValuePair;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstAnnotation;
import com.android.dx.rop.cst.CstArray;
import com.android.dx.rop.cst.CstBoolean;
import com.android.dx.rop.cst.CstByte;
import com.android.dx.rop.cst.CstChar;
import com.android.dx.rop.cst.CstDouble;
import com.android.dx.rop.cst.CstEnumRef;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstFloat;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstKnownNull;
import com.android.dx.rop.cst.CstLiteralBits;
import com.android.dx.rop.cst.CstLong;
import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.rop.cst.CstShort;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.Collection;
import java.util.Iterator;

public final class ValueEncoder {
   private static final int VALUE_BYTE = 0;
   private static final int VALUE_SHORT = 2;
   private static final int VALUE_CHAR = 3;
   private static final int VALUE_INT = 4;
   private static final int VALUE_LONG = 6;
   private static final int VALUE_FLOAT = 16;
   private static final int VALUE_DOUBLE = 17;
   private static final int VALUE_METHOD_TYPE = 21;
   private static final int VALUE_METHOD_HANDLE = 22;
   private static final int VALUE_STRING = 23;
   private static final int VALUE_TYPE = 24;
   private static final int VALUE_FIELD = 25;
   private static final int VALUE_METHOD = 26;
   private static final int VALUE_ENUM = 27;
   private static final int VALUE_ARRAY = 28;
   private static final int VALUE_ANNOTATION = 29;
   private static final int VALUE_NULL = 30;
   private static final int VALUE_BOOLEAN = 31;
   private final DexFile file;
   private final AnnotatedOutput out;

   public ValueEncoder(DexFile file, AnnotatedOutput out) {
      if (file == null) {
         throw new NullPointerException("file == null");
      } else if (out == null) {
         throw new NullPointerException("out == null");
      } else {
         this.file = file;
         this.out = out;
      }
   }

   public void writeConstant(Constant cst) {
      int type = constantToValueType(cst);
      int value;
      long value;
      switch (type) {
         case 0:
         case 2:
         case 4:
         case 6:
            value = ((CstLiteralBits)cst).getLongBits();
            EncodedValueCodec.writeSignedIntegralValue(this.out, type, value);
            break;
         case 1:
         case 5:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 18:
         case 19:
         case 20:
         default:
            throw new RuntimeException("Shouldn't happen");
         case 3:
            value = ((CstLiteralBits)cst).getLongBits();
            EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, value);
            break;
         case 16:
            value = ((CstFloat)cst).getLongBits() << 32;
            EncodedValueCodec.writeRightZeroExtendedValue(this.out, type, value);
            break;
         case 17:
            value = ((CstDouble)cst).getLongBits();
            EncodedValueCodec.writeRightZeroExtendedValue(this.out, type, value);
            break;
         case 21:
            value = this.file.getProtoIds().indexOf(((CstProtoRef)cst).getPrototype());
            EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, (long)value);
            break;
         case 22:
            value = this.file.getMethodHandles().indexOf((CstMethodHandle)cst);
            EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, (long)value);
            break;
         case 23:
            value = this.file.getStringIds().indexOf((CstString)cst);
            EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, (long)value);
            break;
         case 24:
            value = this.file.getTypeIds().indexOf((CstType)cst);
            EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, (long)value);
            break;
         case 25:
            value = this.file.getFieldIds().indexOf((CstFieldRef)cst);
            EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, (long)value);
            break;
         case 26:
            value = this.file.getMethodIds().indexOf((CstMethodRef)cst);
            EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, (long)value);
            break;
         case 27:
            CstFieldRef fieldRef = ((CstEnumRef)cst).getFieldRef();
            int index = this.file.getFieldIds().indexOf(fieldRef);
            EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, (long)index);
            break;
         case 28:
            this.out.writeByte(type);
            this.writeArray((CstArray)cst, false);
            break;
         case 29:
            this.out.writeByte(type);
            this.writeAnnotation(((CstAnnotation)cst).getAnnotation(), false);
            break;
         case 30:
            this.out.writeByte(type);
            break;
         case 31:
            value = ((CstBoolean)cst).getIntBits();
            this.out.writeByte(type | value << 5);
      }

   }

   private static int constantToValueType(Constant cst) {
      if (cst instanceof CstByte) {
         return 0;
      } else if (cst instanceof CstShort) {
         return 2;
      } else if (cst instanceof CstChar) {
         return 3;
      } else if (cst instanceof CstInteger) {
         return 4;
      } else if (cst instanceof CstLong) {
         return 6;
      } else if (cst instanceof CstFloat) {
         return 16;
      } else if (cst instanceof CstDouble) {
         return 17;
      } else if (cst instanceof CstProtoRef) {
         return 21;
      } else if (cst instanceof CstMethodHandle) {
         return 22;
      } else if (cst instanceof CstString) {
         return 23;
      } else if (cst instanceof CstType) {
         return 24;
      } else if (cst instanceof CstFieldRef) {
         return 25;
      } else if (cst instanceof CstMethodRef) {
         return 26;
      } else if (cst instanceof CstEnumRef) {
         return 27;
      } else if (cst instanceof CstArray) {
         return 28;
      } else if (cst instanceof CstAnnotation) {
         return 29;
      } else if (cst instanceof CstKnownNull) {
         return 30;
      } else if (cst instanceof CstBoolean) {
         return 31;
      } else {
         throw new RuntimeException("Shouldn't happen");
      }
   }

   public void writeArray(CstArray array, boolean topLevel) {
      boolean annotates = topLevel && this.out.annotates();
      CstArray.List list = array.getList();
      int size = list.size();
      if (annotates) {
         this.out.annotate("  size: " + Hex.u4(size));
      }

      this.out.writeUleb128(size);

      for(int i = 0; i < size; ++i) {
         Constant cst = list.get(i);
         if (annotates) {
            this.out.annotate("  [" + Integer.toHexString(i) + "] " + constantToHuman(cst));
         }

         this.writeConstant(cst);
      }

      if (annotates) {
         this.out.endAnnotation();
      }

   }

   public void writeAnnotation(Annotation annotation, boolean topLevel) {
      boolean annotates = topLevel && this.out.annotates();
      StringIdsSection stringIds = this.file.getStringIds();
      TypeIdsSection typeIds = this.file.getTypeIds();
      CstType type = annotation.getType();
      int typeIdx = typeIds.indexOf(type);
      if (annotates) {
         this.out.annotate("  type_idx: " + Hex.u4(typeIdx) + " // " + type.toHuman());
      }

      this.out.writeUleb128(typeIds.indexOf(annotation.getType()));
      Collection<NameValuePair> pairs = annotation.getNameValuePairs();
      int size = pairs.size();
      if (annotates) {
         this.out.annotate("  size: " + Hex.u4(size));
      }

      this.out.writeUleb128(size);
      int at = 0;

      Constant value;
      for(Iterator var11 = pairs.iterator(); var11.hasNext(); this.writeConstant(value)) {
         NameValuePair pair = (NameValuePair)var11.next();
         CstString name = pair.getName();
         int nameIdx = stringIds.indexOf(name);
         value = pair.getValue();
         if (annotates) {
            this.out.annotate(0, "  elements[" + at + "]:");
            ++at;
            this.out.annotate("    name_idx: " + Hex.u4(nameIdx) + " // " + name.toHuman());
         }

         this.out.writeUleb128(nameIdx);
         if (annotates) {
            this.out.annotate("    value: " + constantToHuman(value));
         }
      }

      if (annotates) {
         this.out.endAnnotation();
      }

   }

   public static String constantToHuman(Constant cst) {
      int type = constantToValueType(cst);
      if (type == 30) {
         return "null";
      } else {
         StringBuilder sb = new StringBuilder();
         sb.append(cst.typeName());
         sb.append(' ');
         sb.append(cst.toHuman());
         return sb.toString();
      }
   }

   public static void addContents(DexFile file, Annotation annotation) {
      TypeIdsSection typeIds = file.getTypeIds();
      StringIdsSection stringIds = file.getStringIds();
      typeIds.intern(annotation.getType());
      Iterator var4 = annotation.getNameValuePairs().iterator();

      while(var4.hasNext()) {
         NameValuePair pair = (NameValuePair)var4.next();
         stringIds.intern(pair.getName());
         addContents(file, pair.getValue());
      }

   }

   public static void addContents(DexFile file, Constant cst) {
      if (cst instanceof CstAnnotation) {
         addContents(file, ((CstAnnotation)cst).getAnnotation());
      } else if (cst instanceof CstArray) {
         CstArray.List list = ((CstArray)cst).getList();
         int size = list.size();

         for(int i = 0; i < size; ++i) {
            addContents(file, list.get(i));
         }
      } else {
         file.internIfAppropriate(cst);
      }

   }
}
