package com.android.dx.dex.code;

import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.cst.Constant;
import com.android.dx.util.Hex;

public final class MultiCstInsn extends FixedSizeInsn {
   private static final int NOT_SET = -1;
   private final Constant[] constants;
   private final int[] index;
   private int classIndex;

   public MultiCstInsn(Dop opcode, SourcePosition position, RegisterSpecList registers, Constant[] constants) {
      super(opcode, position, registers);
      if (constants == null) {
         throw new NullPointerException("constants == null");
      } else {
         this.constants = constants;
         this.index = new int[constants.length];

         for(int i = 0; i < this.index.length; ++i) {
            if (constants[i] == null) {
               throw new NullPointerException("constants[i] == null");
            }

            this.index[i] = -1;
         }

         this.classIndex = -1;
      }
   }

   private MultiCstInsn(Dop opcode, SourcePosition position, RegisterSpecList registers, Constant[] constants, int[] index, int classIndex) {
      super(opcode, position, registers);
      this.constants = constants;
      this.index = index;
      this.classIndex = classIndex;
   }

   public DalvInsn withOpcode(Dop opcode) {
      return new MultiCstInsn(opcode, this.getPosition(), this.getRegisters(), this.constants, this.index, this.classIndex);
   }

   public DalvInsn withRegisters(RegisterSpecList registers) {
      return new MultiCstInsn(this.getOpcode(), this.getPosition(), registers, this.constants, this.index, this.classIndex);
   }

   public int getNumberOfConstants() {
      return this.constants.length;
   }

   public Constant getConstant(int position) {
      return this.constants[position];
   }

   public int getIndex(int position) {
      if (!this.hasIndex(position)) {
         throw new IllegalStateException("index not yet set for constant " + position + " value = " + this.constants[position]);
      } else {
         return this.index[position];
      }
   }

   public boolean hasIndex(int position) {
      return this.index[position] != -1;
   }

   public void setIndex(int position, int index) {
      if (index < 0) {
         throw new IllegalArgumentException("index < 0");
      } else if (this.hasIndex(position)) {
         throw new IllegalStateException("index already set");
      } else {
         this.index[position] = index;
      }
   }

   public int getClassIndex() {
      if (!this.hasClassIndex()) {
         throw new IllegalStateException("class index not yet set");
      } else {
         return this.classIndex;
      }
   }

   public boolean hasClassIndex() {
      return this.classIndex != -1;
   }

   public void setClassIndex(int index) {
      if (index < 0) {
         throw new IllegalArgumentException("index < 0");
      } else if (this.hasClassIndex()) {
         throw new IllegalStateException("class index already set");
      } else {
         this.classIndex = index;
      }
   }

   protected String argString() {
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < this.constants.length; ++i) {
         if (sb.length() > 0) {
            sb.append(", ");
         }

         sb.append(this.constants[i].toHuman());
      }

      return sb.toString();
   }

   public String cstString() {
      return this.argString();
   }

   public String cstComment() {
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < this.constants.length; ++i) {
         if (!this.hasIndex(i)) {
            return "";
         }

         if (i > 0) {
            sb.append(", ");
         }

         sb.append(this.getConstant(i).typeName());
         sb.append('@');
         int currentIndex = this.getIndex(i);
         if (currentIndex < 65536) {
            sb.append(Hex.u2(currentIndex));
         } else {
            sb.append(Hex.u4(currentIndex));
         }
      }

      return sb.toString();
   }
}
