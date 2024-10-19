package com.android.dx.cf.direct;

import com.android.dx.cf.iface.ParseException;
import com.android.dx.cf.iface.ParseObserver;
import com.android.dx.rop.annotation.Annotation;
import com.android.dx.rop.annotation.AnnotationVisibility;
import com.android.dx.rop.annotation.Annotations;
import com.android.dx.rop.annotation.AnnotationsList;
import com.android.dx.rop.annotation.NameValuePair;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.ConstantPool;
import com.android.dx.rop.cst.CstAnnotation;
import com.android.dx.rop.cst.CstArray;
import com.android.dx.rop.cst.CstBoolean;
import com.android.dx.rop.cst.CstByte;
import com.android.dx.rop.cst.CstChar;
import com.android.dx.rop.cst.CstDouble;
import com.android.dx.rop.cst.CstEnumRef;
import com.android.dx.rop.cst.CstFloat;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstLong;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstShort;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.util.ByteArray;
import com.android.dx.util.Hex;
import java.io.IOException;

public final class AnnotationParser {
   private final DirectClassFile cf;
   private final ConstantPool pool;
   private final ByteArray bytes;
   private final ParseObserver observer;
   private final ByteArray.MyDataInputStream input;
   private int parseCursor;

   public AnnotationParser(DirectClassFile cf, int offset, int length, ParseObserver observer) {
      if (cf == null) {
         throw new NullPointerException("cf == null");
      } else {
         this.cf = cf;
         this.pool = cf.getConstantPool();
         this.observer = observer;
         this.bytes = cf.getBytes().slice(offset, offset + length);
         this.input = this.bytes.makeDataInputStream();
         this.parseCursor = 0;
      }
   }

   public Constant parseValueAttribute() {
      try {
         Constant result = this.parseValue();
         if (this.input.available() != 0) {
            throw new ParseException("extra data in attribute");
         } else {
            return result;
         }
      } catch (IOException var3) {
         IOException ex = var3;
         throw new RuntimeException("shouldn't happen", ex);
      }
   }

   public AnnotationsList parseParameterAttribute(AnnotationVisibility visibility) {
      try {
         AnnotationsList result = this.parseAnnotationsList(visibility);
         if (this.input.available() != 0) {
            throw new ParseException("extra data in attribute");
         } else {
            return result;
         }
      } catch (IOException var4) {
         IOException ex = var4;
         throw new RuntimeException("shouldn't happen", ex);
      }
   }

   public Annotations parseAnnotationAttribute(AnnotationVisibility visibility) {
      try {
         Annotations result = this.parseAnnotations(visibility);
         if (this.input.available() != 0) {
            throw new ParseException("extra data in attribute");
         } else {
            return result;
         }
      } catch (IOException var4) {
         IOException ex = var4;
         throw new RuntimeException("shouldn't happen", ex);
      }
   }

   private AnnotationsList parseAnnotationsList(AnnotationVisibility visibility) throws IOException {
      int count = this.input.readUnsignedByte();
      if (this.observer != null) {
         this.parsed(1, "num_parameters: " + Hex.u1(count));
      }

      AnnotationsList outerList = new AnnotationsList(count);

      for(int i = 0; i < count; ++i) {
         if (this.observer != null) {
            this.parsed(0, "parameter_annotations[" + i + "]:");
            this.changeIndent(1);
         }

         Annotations annotations = this.parseAnnotations(visibility);
         outerList.set(i, annotations);
         if (this.observer != null) {
            this.observer.changeIndent(-1);
         }
      }

      outerList.setImmutable();
      return outerList;
   }

   private Annotations parseAnnotations(AnnotationVisibility visibility) throws IOException {
      int count = this.input.readUnsignedShort();
      if (this.observer != null) {
         this.parsed(2, "num_annotations: " + Hex.u2(count));
      }

      Annotations annotations = new Annotations();

      for(int i = 0; i < count; ++i) {
         if (this.observer != null) {
            this.parsed(0, "annotations[" + i + "]:");
            this.changeIndent(1);
         }

         Annotation annotation = this.parseAnnotation(visibility);
         annotations.add(annotation);
         if (this.observer != null) {
            this.observer.changeIndent(-1);
         }
      }

      annotations.setImmutable();
      return annotations;
   }

   private Annotation parseAnnotation(AnnotationVisibility visibility) throws IOException {
      this.requireLength(4);
      int typeIndex = this.input.readUnsignedShort();
      int numElements = this.input.readUnsignedShort();
      CstString typeString = (CstString)this.pool.get(typeIndex);
      CstType type = new CstType(Type.intern(typeString.getString()));
      if (this.observer != null) {
         this.parsed(2, "type: " + type.toHuman());
         this.parsed(2, "num_elements: " + numElements);
      }

      Annotation annotation = new Annotation(type, visibility);

      for(int i = 0; i < numElements; ++i) {
         if (this.observer != null) {
            this.parsed(0, "elements[" + i + "]:");
            this.changeIndent(1);
         }

         NameValuePair element = this.parseElement();
         annotation.add(element);
         if (this.observer != null) {
            this.changeIndent(-1);
         }
      }

      annotation.setImmutable();
      return annotation;
   }

   private NameValuePair parseElement() throws IOException {
      this.requireLength(5);
      int elementNameIndex = this.input.readUnsignedShort();
      CstString elementName = (CstString)this.pool.get(elementNameIndex);
      if (this.observer != null) {
         this.parsed(2, "element_name: " + elementName.toHuman());
         this.parsed(0, "value: ");
         this.changeIndent(1);
      }

      Constant value = this.parseValue();
      if (this.observer != null) {
         this.changeIndent(-1);
      }

      return new NameValuePair(elementName, value);
   }

   private Constant parseValue() throws IOException {
      int tag = this.input.readUnsignedByte();
      if (this.observer != null) {
         CstString humanTag = new CstString(Character.toString((char)tag));
         this.parsed(1, "tag: " + humanTag.toQuoted());
      }

      int constNameIndex;
      int numValues;
      CstInteger value;
      switch (tag) {
         case 64:
            Annotation annotation = this.parseAnnotation(AnnotationVisibility.EMBEDDED);
            return new CstAnnotation(annotation);
         case 65:
         case 69:
         case 71:
         case 72:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 100:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 109:
         case 110:
         case 111:
         case 112:
         case 113:
         case 114:
         default:
            throw new ParseException("unknown annotation tag: " + Hex.u1(tag));
         case 66:
            value = (CstInteger)this.parseConstant();
            return CstByte.make(value.getValue());
         case 67:
            value = (CstInteger)this.parseConstant();
            constNameIndex = value.getValue();
            return CstChar.make(value.getValue());
         case 68:
            CstDouble value = (CstDouble)this.parseConstant();
            return value;
         case 70:
            CstFloat value = (CstFloat)this.parseConstant();
            return value;
         case 73:
            value = (CstInteger)this.parseConstant();
            return value;
         case 74:
            CstLong value = (CstLong)this.parseConstant();
            return value;
         case 83:
            value = (CstInteger)this.parseConstant();
            return CstShort.make(value.getValue());
         case 90:
            value = (CstInteger)this.parseConstant();
            return CstBoolean.make(value.getValue());
         case 91:
            this.requireLength(2);
            numValues = this.input.readUnsignedShort();
            CstArray.List list = new CstArray.List(numValues);
            if (this.observer != null) {
               this.parsed(2, "num_values: " + numValues);
               this.changeIndent(1);
            }

            for(int i = 0; i < numValues; ++i) {
               if (this.observer != null) {
                  this.changeIndent(-1);
                  this.parsed(0, "element_value[" + i + "]:");
                  this.changeIndent(1);
               }

               list.set(i, this.parseValue());
            }

            if (this.observer != null) {
               this.changeIndent(-1);
            }

            list.setImmutable();
            return new CstArray(list);
         case 99:
            numValues = this.input.readUnsignedShort();
            CstString value = (CstString)this.pool.get(numValues);
            Type type = Type.internReturnType(value.getString());
            if (this.observer != null) {
               this.parsed(2, "class_info: " + type.toHuman());
            }

            return new CstType(type);
         case 101:
            this.requireLength(4);
            numValues = this.input.readUnsignedShort();
            constNameIndex = this.input.readUnsignedShort();
            CstString typeName = (CstString)this.pool.get(numValues);
            CstString constName = (CstString)this.pool.get(constNameIndex);
            if (this.observer != null) {
               this.parsed(2, "type_name: " + typeName.toHuman());
               this.parsed(2, "const_name: " + constName.toHuman());
            }

            return new CstEnumRef(new CstNat(constName, typeName));
         case 115:
            return this.parseConstant();
      }
   }

   private Constant parseConstant() throws IOException {
      int constValueIndex = this.input.readUnsignedShort();
      Constant value = this.pool.get(constValueIndex);
      if (this.observer != null) {
         String human = value instanceof CstString ? ((CstString)value).toQuoted() : value.toHuman();
         this.parsed(2, "constant_value: " + human);
      }

      return value;
   }

   private void requireLength(int requiredLength) throws IOException {
      if (this.input.available() < requiredLength) {
         throw new ParseException("truncated annotation attribute");
      }
   }

   private void parsed(int length, String message) {
      this.observer.parsed(this.bytes, this.parseCursor, length, message);
      this.parseCursor += length;
   }

   private void changeIndent(int indent) {
      this.observer.changeIndent(indent);
   }
}
