package com.android.dx.rop.type;

import com.android.dx.util.FixedSizeList;

public final class StdTypeList extends FixedSizeList implements TypeList {
   public static final StdTypeList EMPTY = new StdTypeList(0);
   public static final StdTypeList INT;
   public static final StdTypeList LONG;
   public static final StdTypeList FLOAT;
   public static final StdTypeList DOUBLE;
   public static final StdTypeList OBJECT;
   public static final StdTypeList RETURN_ADDRESS;
   public static final StdTypeList THROWABLE;
   public static final StdTypeList INT_INT;
   public static final StdTypeList LONG_LONG;
   public static final StdTypeList FLOAT_FLOAT;
   public static final StdTypeList DOUBLE_DOUBLE;
   public static final StdTypeList OBJECT_OBJECT;
   public static final StdTypeList INT_OBJECT;
   public static final StdTypeList LONG_OBJECT;
   public static final StdTypeList FLOAT_OBJECT;
   public static final StdTypeList DOUBLE_OBJECT;
   public static final StdTypeList LONG_INT;
   public static final StdTypeList INTARR_INT;
   public static final StdTypeList LONGARR_INT;
   public static final StdTypeList FLOATARR_INT;
   public static final StdTypeList DOUBLEARR_INT;
   public static final StdTypeList OBJECTARR_INT;
   public static final StdTypeList BOOLEANARR_INT;
   public static final StdTypeList BYTEARR_INT;
   public static final StdTypeList CHARARR_INT;
   public static final StdTypeList SHORTARR_INT;
   public static final StdTypeList INT_INTARR_INT;
   public static final StdTypeList LONG_LONGARR_INT;
   public static final StdTypeList FLOAT_FLOATARR_INT;
   public static final StdTypeList DOUBLE_DOUBLEARR_INT;
   public static final StdTypeList OBJECT_OBJECTARR_INT;
   public static final StdTypeList INT_BOOLEANARR_INT;
   public static final StdTypeList INT_BYTEARR_INT;
   public static final StdTypeList INT_CHARARR_INT;
   public static final StdTypeList INT_SHORTARR_INT;

   public static StdTypeList make(Type type) {
      StdTypeList result = new StdTypeList(1);
      result.set(0, type);
      return result;
   }

   public static StdTypeList make(Type type0, Type type1) {
      StdTypeList result = new StdTypeList(2);
      result.set(0, type0);
      result.set(1, type1);
      return result;
   }

   public static StdTypeList make(Type type0, Type type1, Type type2) {
      StdTypeList result = new StdTypeList(3);
      result.set(0, type0);
      result.set(1, type1);
      result.set(2, type2);
      return result;
   }

   public static StdTypeList make(Type type0, Type type1, Type type2, Type type3) {
      StdTypeList result = new StdTypeList(4);
      result.set(0, type0);
      result.set(1, type1);
      result.set(2, type2);
      result.set(3, type3);
      return result;
   }

   public static String toHuman(TypeList list) {
      int size = list.size();
      if (size == 0) {
         return "<empty>";
      } else {
         StringBuilder sb = new StringBuilder(100);

         for(int i = 0; i < size; ++i) {
            if (i != 0) {
               sb.append(", ");
            }

            sb.append(list.getType(i).toHuman());
         }

         return sb.toString();
      }
   }

   public static int hashContents(TypeList list) {
      int size = list.size();
      int hash = 0;

      for(int i = 0; i < size; ++i) {
         hash = hash * 31 + list.getType(i).hashCode();
      }

      return hash;
   }

   public static boolean equalContents(TypeList list1, TypeList list2) {
      int size = list1.size();
      if (list2.size() != size) {
         return false;
      } else {
         for(int i = 0; i < size; ++i) {
            if (!list1.getType(i).equals(list2.getType(i))) {
               return false;
            }
         }

         return true;
      }
   }

   public static int compareContents(TypeList list1, TypeList list2) {
      int size1 = list1.size();
      int size2 = list2.size();
      int size = Math.min(size1, size2);

      for(int i = 0; i < size; ++i) {
         int comparison = list1.getType(i).compareTo(list2.getType(i));
         if (comparison != 0) {
            return comparison;
         }
      }

      if (size1 == size2) {
         return 0;
      } else if (size1 < size2) {
         return -1;
      } else {
         return 1;
      }
   }

   public StdTypeList(int size) {
      super(size);
   }

   public Type getType(int n) {
      return this.get(n);
   }

   public int getWordCount() {
      int sz = this.size();
      int result = 0;

      for(int i = 0; i < sz; ++i) {
         result += this.get(i).getCategory();
      }

      return result;
   }

   public TypeList withAddedType(Type type) {
      int sz = this.size();
      StdTypeList result = new StdTypeList(sz + 1);

      for(int i = 0; i < sz; ++i) {
         result.set0(i, this.get0(i));
      }

      result.set(sz, type);
      result.setImmutable();
      return result;
   }

   public Type get(int n) {
      return (Type)this.get0(n);
   }

   public void set(int n, Type type) {
      this.set0(n, type);
   }

   public StdTypeList withFirst(Type type) {
      int sz = this.size();
      StdTypeList result = new StdTypeList(sz + 1);
      result.set0(0, type);

      for(int i = 0; i < sz; ++i) {
         result.set0(i + 1, this.getOrNull0(i));
      }

      return result;
   }

   static {
      INT = make(Type.INT);
      LONG = make(Type.LONG);
      FLOAT = make(Type.FLOAT);
      DOUBLE = make(Type.DOUBLE);
      OBJECT = make(Type.OBJECT);
      RETURN_ADDRESS = make(Type.RETURN_ADDRESS);
      THROWABLE = make(Type.THROWABLE);
      INT_INT = make(Type.INT, Type.INT);
      LONG_LONG = make(Type.LONG, Type.LONG);
      FLOAT_FLOAT = make(Type.FLOAT, Type.FLOAT);
      DOUBLE_DOUBLE = make(Type.DOUBLE, Type.DOUBLE);
      OBJECT_OBJECT = make(Type.OBJECT, Type.OBJECT);
      INT_OBJECT = make(Type.INT, Type.OBJECT);
      LONG_OBJECT = make(Type.LONG, Type.OBJECT);
      FLOAT_OBJECT = make(Type.FLOAT, Type.OBJECT);
      DOUBLE_OBJECT = make(Type.DOUBLE, Type.OBJECT);
      LONG_INT = make(Type.LONG, Type.INT);
      INTARR_INT = make(Type.INT_ARRAY, Type.INT);
      LONGARR_INT = make(Type.LONG_ARRAY, Type.INT);
      FLOATARR_INT = make(Type.FLOAT_ARRAY, Type.INT);
      DOUBLEARR_INT = make(Type.DOUBLE_ARRAY, Type.INT);
      OBJECTARR_INT = make(Type.OBJECT_ARRAY, Type.INT);
      BOOLEANARR_INT = make(Type.BOOLEAN_ARRAY, Type.INT);
      BYTEARR_INT = make(Type.BYTE_ARRAY, Type.INT);
      CHARARR_INT = make(Type.CHAR_ARRAY, Type.INT);
      SHORTARR_INT = make(Type.SHORT_ARRAY, Type.INT);
      INT_INTARR_INT = make(Type.INT, Type.INT_ARRAY, Type.INT);
      LONG_LONGARR_INT = make(Type.LONG, Type.LONG_ARRAY, Type.INT);
      FLOAT_FLOATARR_INT = make(Type.FLOAT, Type.FLOAT_ARRAY, Type.INT);
      DOUBLE_DOUBLEARR_INT = make(Type.DOUBLE, Type.DOUBLE_ARRAY, Type.INT);
      OBJECT_OBJECTARR_INT = make(Type.OBJECT, Type.OBJECT_ARRAY, Type.INT);
      INT_BOOLEANARR_INT = make(Type.INT, Type.BOOLEAN_ARRAY, Type.INT);
      INT_BYTEARR_INT = make(Type.INT, Type.BYTE_ARRAY, Type.INT);
      INT_CHARARR_INT = make(Type.INT, Type.CHAR_ARRAY, Type.INT);
      INT_SHORTARR_INT = make(Type.INT, Type.SHORT_ARRAY, Type.INT);
   }
}
