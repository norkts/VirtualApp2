package com.android.dx.cf.code;

import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.FixedSizeList;
import com.android.dx.util.IntList;

public final class ByteCatchList extends FixedSizeList {
   public static final ByteCatchList EMPTY = new ByteCatchList(0);

   public ByteCatchList(int count) {
      super(count);
   }

   public int byteLength() {
      return 2 + this.size() * 8;
   }

   public Item get(int n) {
      return (Item)this.get0(n);
   }

   public void set(int n, Item item) {
      if (item == null) {
         throw new NullPointerException("item == null");
      } else {
         this.set0(n, item);
      }
   }

   public void set(int n, int startPc, int endPc, int handlerPc, CstType exceptionClass) {
      this.set0(n, new Item(startPc, endPc, handlerPc, exceptionClass));
   }

   public ByteCatchList listFor(int pc) {
      int sz = this.size();
      Item[] resultArr = new Item[sz];
      int resultSz = 0;

      for(int i = 0; i < sz; ++i) {
         Item one = this.get(i);
         if (one.covers(pc) && typeNotFound(one, resultArr, resultSz)) {
            resultArr[resultSz] = one;
            ++resultSz;
         }
      }

      if (resultSz == 0) {
         return EMPTY;
      } else {
         ByteCatchList result = new ByteCatchList(resultSz);

         for(int i = 0; i < resultSz; ++i) {
            result.set(i, resultArr[i]);
         }

         result.setImmutable();
         return result;
      }
   }

   private static boolean typeNotFound(Item item, Item[] arr, int count) {
      CstType type = item.getExceptionClass();

      for(int i = 0; i < count; ++i) {
         CstType one = arr[i].getExceptionClass();
         if (one == type || one == CstType.OBJECT) {
            return false;
         }
      }

      return true;
   }

   public IntList toTargetList(int noException) {
      if (noException < -1) {
         throw new IllegalArgumentException("noException < -1");
      } else {
         boolean hasDefault = noException >= 0;
         int sz = this.size();
         if (sz == 0) {
            return hasDefault ? IntList.makeImmutable(noException) : IntList.EMPTY;
         } else {
            IntList result = new IntList(sz + (hasDefault ? 1 : 0));

            for(int i = 0; i < sz; ++i) {
               result.add(this.get(i).getHandlerPc());
            }

            if (hasDefault) {
               result.add(noException);
            }

            result.setImmutable();
            return result;
         }
      }
   }

   public TypeList toRopCatchList() {
      int sz = this.size();
      if (sz == 0) {
         return StdTypeList.EMPTY;
      } else {
         StdTypeList result = new StdTypeList(sz);

         for(int i = 0; i < sz; ++i) {
            result.set(i, this.get(i).getExceptionClass().getClassType());
         }

         result.setImmutable();
         return result;
      }
   }

   public static class Item {
      private final int startPc;
      private final int endPc;
      private final int handlerPc;
      private final CstType exceptionClass;

      public Item(int startPc, int endPc, int handlerPc, CstType exceptionClass) {
         if (startPc < 0) {
            throw new IllegalArgumentException("startPc < 0");
         } else if (endPc < startPc) {
            throw new IllegalArgumentException("endPc < startPc");
         } else if (handlerPc < 0) {
            throw new IllegalArgumentException("handlerPc < 0");
         } else {
            this.startPc = startPc;
            this.endPc = endPc;
            this.handlerPc = handlerPc;
            this.exceptionClass = exceptionClass;
         }
      }

      public int getStartPc() {
         return this.startPc;
      }

      public int getEndPc() {
         return this.endPc;
      }

      public int getHandlerPc() {
         return this.handlerPc;
      }

      public CstType getExceptionClass() {
         return this.exceptionClass != null ? this.exceptionClass : CstType.OBJECT;
      }

      public boolean covers(int pc) {
         return pc >= this.startPc && pc < this.endPc;
      }
   }
}
