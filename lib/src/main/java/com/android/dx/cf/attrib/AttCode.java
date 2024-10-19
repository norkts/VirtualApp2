package com.android.dx.cf.attrib;

import com.android.dx.cf.code.ByteCatchList;
import com.android.dx.cf.code.BytecodeArray;
import com.android.dx.cf.iface.AttributeList;
import com.android.dx.util.MutabilityException;

public final class AttCode extends BaseAttribute {
   public static final String ATTRIBUTE_NAME = "Code";
   private final int maxStack;
   private final int maxLocals;
   private final BytecodeArray code;
   private final ByteCatchList catches;
   private final AttributeList attributes;

   public AttCode(int maxStack, int maxLocals, BytecodeArray code, ByteCatchList catches, AttributeList attributes) {
      super("Code");
      if (maxStack < 0) {
         throw new IllegalArgumentException("maxStack < 0");
      } else if (maxLocals < 0) {
         throw new IllegalArgumentException("maxLocals < 0");
      } else if (code == null) {
         throw new NullPointerException("code == null");
      } else {
         try {
            if (catches.isMutable()) {
               throw new MutabilityException("catches.isMutable()");
            }
         } catch (NullPointerException var7) {
            throw new NullPointerException("catches == null");
         }

         try {
            if (attributes.isMutable()) {
               throw new MutabilityException("attributes.isMutable()");
            }
         } catch (NullPointerException var8) {
            throw new NullPointerException("attributes == null");
         }

         this.maxStack = maxStack;
         this.maxLocals = maxLocals;
         this.code = code;
         this.catches = catches;
         this.attributes = attributes;
      }
   }

   public int byteLength() {
      return 10 + this.code.byteLength() + this.catches.byteLength() + this.attributes.byteLength();
   }

   public int getMaxStack() {
      return this.maxStack;
   }

   public int getMaxLocals() {
      return this.maxLocals;
   }

   public BytecodeArray getCode() {
      return this.code;
   }

   public ByteCatchList getCatches() {
      return this.catches;
   }

   public AttributeList getAttributes() {
      return this.attributes;
   }
}
