package com.android.dx.cf.cst;

import com.android.dx.cf.iface.ParseException;
import com.android.dx.cf.iface.ParseObserver;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstDouble;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstFloat;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstInterfaceMethodRef;
import com.android.dx.rop.cst.CstInvokeDynamic;
import com.android.dx.rop.cst.CstLong;
import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.cst.StdConstantPool;
import com.android.dx.rop.type.Type;
import com.android.dx.util.ByteArray;
import com.android.dx.util.Hex;
import java.util.BitSet;

public final class ConstantPoolParser {
   private final ByteArray bytes;
   private final StdConstantPool pool;
   private final int[] offsets;
   private int endOffset;
   private ParseObserver observer;

   public ConstantPoolParser(ByteArray bytes) {
      int size = bytes.getUnsignedShort(8);
      this.bytes = bytes;
      this.pool = new StdConstantPool(size);
      this.offsets = new int[size];
      this.endOffset = -1;
   }

   public void setObserver(ParseObserver observer) {
      this.observer = observer;
   }

   public int getEndOffset() {
      this.parseIfNecessary();
      return this.endOffset;
   }

   public StdConstantPool getPool() {
      this.parseIfNecessary();
      return this.pool;
   }

   private void parseIfNecessary() {
      if (this.endOffset < 0) {
         this.parse();
      }

   }

   private void parse() {
      this.determineOffsets();
      if (this.observer != null) {
         this.observer.parsed(this.bytes, 8, 2, "constant_pool_count: " + Hex.u2(this.offsets.length));
         this.observer.parsed(this.bytes, 10, 0, "\nconstant_pool:");
         this.observer.changeIndent(1);
      }

      BitSet wasUtf8 = new BitSet(this.offsets.length);

      int i;
      for(i = 1; i < this.offsets.length; ++i) {
         int offset = this.offsets[i];
         if (offset != 0 && this.pool.getOrNull(i) == null) {
            this.parse0(i, wasUtf8);
         }
      }

      if (this.observer != null) {
         for(i = 1; i < this.offsets.length; ++i) {
            Constant cst = this.pool.getOrNull(i);
            if (cst != null) {
               int offset = this.offsets[i];
               int nextOffset = this.endOffset;

               for(int j = i + 1; j < this.offsets.length; ++j) {
                  int off = this.offsets[j];
                  if (off != 0) {
                     nextOffset = off;
                     break;
                  }
               }

               String human = wasUtf8.get(i) ? Hex.u2(i) + ": utf8{\"" + cst.toHuman() + "\"}" : Hex.u2(i) + ": " + cst.toString();
               this.observer.parsed(this.bytes, offset, nextOffset - offset, human);
            }
         }

         this.observer.changeIndent(-1);
         this.observer.parsed(this.bytes, this.endOffset, 0, "end constant_pool");
      }

   }

   private void determineOffsets() {
      int at = 10;

      byte lastCategory;
      for(int i = 1; i < this.offsets.length; i += lastCategory) {
         this.offsets[i] = at;
         int tag = this.bytes.getUnsignedByte(at);

         try {
            switch (tag) {
               case 1:
                  lastCategory = 1;
                  at += this.bytes.getUnsignedShort(at + 1) + 3;
                  break;
               case 2:
               case 13:
               case 14:
               case 17:
               default:
                  throw new ParseException("unknown tag byte: " + Hex.u1(tag));
               case 3:
               case 4:
               case 9:
               case 10:
               case 11:
               case 12:
                  lastCategory = 1;
                  at += 5;
                  break;
               case 5:
               case 6:
                  lastCategory = 2;
                  at += 9;
                  break;
               case 7:
               case 8:
                  lastCategory = 1;
                  at += 3;
                  break;
               case 15:
                  lastCategory = 1;
                  at += 4;
                  break;
               case 16:
                  lastCategory = 1;
                  at += 3;
                  break;
               case 18:
                  lastCategory = 1;
                  at += 5;
            }
         } catch (ParseException var6) {
            ParseException ex = var6;
            ex.addContext("...while preparsing cst " + Hex.u2(i) + " at offset " + Hex.u4(at));
            throw ex;
         }
      }

      this.endOffset = at;
   }

   private Constant parse0(int idx, BitSet wasUtf8) {
      Constant cst = this.pool.getOrNull(idx);
      if (cst != null) {
         return cst;
      } else {
         int at = this.offsets[idx];

         try {
            int tag = this.bytes.getUnsignedByte(at);
            int constantIndex;
            int kind;
            CstString descriptor;
            CstType type;
            long bits;
            CstNat nat;
            int descriptorIndex;
            switch (tag) {
               case 1:
                  cst = this.parseUtf8(at);
                  wasUtf8.set(idx);
                  break;
               case 2:
               case 13:
               case 14:
               case 17:
               default:
                  throw new ParseException("unknown tag byte: " + Hex.u1(tag));
               case 3:
                  kind = this.bytes.getInt(at + 1);
                  cst = CstInteger.make(kind);
                  break;
               case 4:
                  kind = this.bytes.getInt(at + 1);
                  cst = CstFloat.make(kind);
                  break;
               case 5:
                  bits = this.bytes.getLong(at + 1);
                  cst = CstLong.make(bits);
                  break;
               case 6:
                  bits = this.bytes.getLong(at + 1);
                  cst = CstDouble.make(bits);
                  break;
               case 7:
                  kind = this.bytes.getUnsignedShort(at + 1);
                  descriptor = (CstString)this.parse0(kind, wasUtf8);
                  cst = new CstType(Type.internClassName(descriptor.getString()));
                  break;
               case 8:
                  kind = this.bytes.getUnsignedShort(at + 1);
                  cst = this.parse0(kind, wasUtf8);
                  break;
               case 9:
                  kind = this.bytes.getUnsignedShort(at + 1);
                  type = (CstType)this.parse0(kind, wasUtf8);
                  descriptorIndex = this.bytes.getUnsignedShort(at + 3);
                  nat = (CstNat)this.parse0(descriptorIndex, wasUtf8);
                  cst = new CstFieldRef(type, nat);
                  break;
               case 10:
                  kind = this.bytes.getUnsignedShort(at + 1);
                  type = (CstType)this.parse0(kind, wasUtf8);
                  descriptorIndex = this.bytes.getUnsignedShort(at + 3);
                  nat = (CstNat)this.parse0(descriptorIndex, wasUtf8);
                  cst = new CstMethodRef(type, nat);
                  break;
               case 11:
                  kind = this.bytes.getUnsignedShort(at + 1);
                  type = (CstType)this.parse0(kind, wasUtf8);
                  descriptorIndex = this.bytes.getUnsignedShort(at + 3);
                  nat = (CstNat)this.parse0(descriptorIndex, wasUtf8);
                  cst = new CstInterfaceMethodRef(type, nat);
                  break;
               case 12:
                  kind = this.bytes.getUnsignedShort(at + 1);
                  descriptor = (CstString)this.parse0(kind, wasUtf8);
                  descriptorIndex = this.bytes.getUnsignedShort(at + 3);
                  CstString descriptor1 = (CstString)this.parse0(descriptorIndex, wasUtf8);
                  cst = new CstNat(descriptor, descriptor1);
                  break;
               case 15:
                  kind = this.bytes.getUnsignedByte(at + 1);
                  constantIndex = this.bytes.getUnsignedShort(at + 2);
                  Object ref;
                  switch (kind) {
                     case 1:
                     case 2:
                     case 3:
                     case 4:
                        ref = (CstFieldRef)this.parse0(constantIndex, wasUtf8);
                        break;
                     case 5:
                     case 8:
                        ref = (CstMethodRef)this.parse0(constantIndex, wasUtf8);
                        break;
                     case 6:
                     case 7:
                        ref = this.parse0(constantIndex, wasUtf8);
                        if (!(ref instanceof CstMethodRef) && !(ref instanceof CstInterfaceMethodRef)) {
                           throw new ParseException("Unsupported ref constant type for MethodHandle " + ref.getClass());
                        }
                        break;
                     case 9:
                        ref = (CstInterfaceMethodRef)this.parse0(constantIndex, wasUtf8);
                        break;
                     default:
                        throw new ParseException("Unsupported MethodHandle kind: " + kind);
                  }

                  int methodHandleType = getMethodHandleTypeForKind(kind);
                  cst = CstMethodHandle.make(methodHandleType, (Constant)ref);
                  break;
               case 16:
                  kind = this.bytes.getUnsignedShort(at + 1);
                  descriptor = (CstString)this.parse0(kind, wasUtf8);
                  cst = CstProtoRef.make(descriptor);
                  break;
               case 18:
                  kind = this.bytes.getUnsignedShort(at + 1);
                  constantIndex = this.bytes.getUnsignedShort(at + 3);
                  CstNat nat1 = (CstNat)this.parse0(constantIndex, wasUtf8);
                  cst = CstInvokeDynamic.make(kind, nat1);
            }
         } catch (ParseException var10) {
            ParseException ex = var10;
            ex.addContext("...while parsing cst " + Hex.u2(idx) + " at offset " + Hex.u4(at));
            throw ex;
         } catch (RuntimeException var11) {
            RuntimeException ex = var11;
            ParseException pe = new ParseException(ex);
            pe.addContext("...while parsing cst " + Hex.u2(idx) + " at offset " + Hex.u4(at));
            throw pe;
         }

         this.pool.set(idx, (Constant)cst);
         return (Constant)cst;
      }
   }

   private CstString parseUtf8(int at) {
      int length = this.bytes.getUnsignedShort(at + 1);
      at += 3;
      ByteArray ubytes = this.bytes.slice(at, at + length);

      try {
         return new CstString(ubytes);
      } catch (IllegalArgumentException var5) {
         IllegalArgumentException ex = var5;
         throw new ParseException(ex);
      }
   }

   private static int getMethodHandleTypeForKind(int kind) {
      switch (kind) {
         case 1:
            return 3;
         case 2:
            return 1;
         case 3:
            return 2;
         case 4:
            return 0;
         case 5:
            return 5;
         case 6:
            return 4;
         case 7:
            return 7;
         case 8:
            return 6;
         case 9:
            return 8;
         default:
            throw new IllegalArgumentException("invalid kind: " + kind);
      }
   }
}
