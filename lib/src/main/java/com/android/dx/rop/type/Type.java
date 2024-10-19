package com.android.dx.rop.type;

import com.android.dx.util.Hex;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class Type implements TypeBearer, Comparable<Type> {
   private static final ConcurrentMap<String, Type> internTable = new ConcurrentHashMap(10000, 0.75F);
   public static final int BT_VOID = 0;
   public static final int BT_BOOLEAN = 1;
   public static final int BT_BYTE = 2;
   public static final int BT_CHAR = 3;
   public static final int BT_DOUBLE = 4;
   public static final int BT_FLOAT = 5;
   public static final int BT_INT = 6;
   public static final int BT_LONG = 7;
   public static final int BT_SHORT = 8;
   public static final int BT_OBJECT = 9;
   public static final int BT_ADDR = 10;
   public static final int BT_COUNT = 11;
   public static final Type BOOLEAN = new Type("Z", 1);
   public static final Type BYTE = new Type("B", 2);
   public static final Type CHAR = new Type("C", 3);
   public static final Type DOUBLE = new Type("D", 4);
   public static final Type FLOAT = new Type("F", 5);
   public static final Type INT = new Type("I", 6);
   public static final Type LONG = new Type("J", 7);
   public static final Type SHORT = new Type("S", 8);
   public static final Type VOID = new Type("V", 0);
   public static final Type KNOWN_NULL = new Type("<null>", 9);
   public static final Type RETURN_ADDRESS = new Type("<addr>", 10);
   public static final Type ANNOTATION = new Type("Ljava/lang/annotation/Annotation;", 9);
   public static final Type CLASS = new Type("Ljava/lang/Class;", 9);
   public static final Type CLONEABLE = new Type("Ljava/lang/Cloneable;", 9);
   public static final Type METHOD_HANDLE = new Type("Ljava/lang/invoke/MethodHandle;", 9);
   public static final Type METHOD_TYPE = new Type("Ljava/lang/invoke/MethodType;", 9);
   public static final Type VAR_HANDLE = new Type("Ljava/lang/invoke/VarHandle;", 9);
   public static final Type OBJECT = new Type("Ljava/lang/Object;", 9);
   public static final Type SERIALIZABLE = new Type("Ljava/io/Serializable;", 9);
   public static final Type STRING = new Type("Ljava/lang/String;", 9);
   public static final Type THROWABLE = new Type("Ljava/lang/Throwable;", 9);
   public static final Type BOOLEAN_CLASS = new Type("Ljava/lang/Boolean;", 9);
   public static final Type BYTE_CLASS = new Type("Ljava/lang/Byte;", 9);
   public static final Type CHARACTER_CLASS = new Type("Ljava/lang/Character;", 9);
   public static final Type DOUBLE_CLASS = new Type("Ljava/lang/Double;", 9);
   public static final Type FLOAT_CLASS = new Type("Ljava/lang/Float;", 9);
   public static final Type INTEGER_CLASS = new Type("Ljava/lang/Integer;", 9);
   public static final Type LONG_CLASS = new Type("Ljava/lang/Long;", 9);
   public static final Type SHORT_CLASS = new Type("Ljava/lang/Short;", 9);
   public static final Type VOID_CLASS = new Type("Ljava/lang/Void;", 9);
   public static final Type BOOLEAN_ARRAY;
   public static final Type BYTE_ARRAY;
   public static final Type CHAR_ARRAY;
   public static final Type DOUBLE_ARRAY;
   public static final Type FLOAT_ARRAY;
   public static final Type INT_ARRAY;
   public static final Type LONG_ARRAY;
   public static final Type OBJECT_ARRAY;
   public static final Type SHORT_ARRAY;
   private final String descriptor;
   private final int basicType;
   private final int newAt;
   private String className;
   private Type arrayType;
   private Type componentType;
   private Type initializedType;

   private static void initInterns() {
      putIntern(BOOLEAN);
      putIntern(BYTE);
      putIntern(CHAR);
      putIntern(DOUBLE);
      putIntern(FLOAT);
      putIntern(INT);
      putIntern(LONG);
      putIntern(SHORT);
      putIntern(ANNOTATION);
      putIntern(CLASS);
      putIntern(CLONEABLE);
      putIntern(METHOD_HANDLE);
      putIntern(VAR_HANDLE);
      putIntern(OBJECT);
      putIntern(SERIALIZABLE);
      putIntern(STRING);
      putIntern(THROWABLE);
      putIntern(BOOLEAN_CLASS);
      putIntern(BYTE_CLASS);
      putIntern(CHARACTER_CLASS);
      putIntern(DOUBLE_CLASS);
      putIntern(FLOAT_CLASS);
      putIntern(INTEGER_CLASS);
      putIntern(LONG_CLASS);
      putIntern(SHORT_CLASS);
      putIntern(VOID_CLASS);
      putIntern(BOOLEAN_ARRAY);
      putIntern(BYTE_ARRAY);
      putIntern(CHAR_ARRAY);
      putIntern(DOUBLE_ARRAY);
      putIntern(FLOAT_ARRAY);
      putIntern(INT_ARRAY);
      putIntern(LONG_ARRAY);
      putIntern(OBJECT_ARRAY);
      putIntern(SHORT_ARRAY);
   }

   public static Type intern(String descriptor) {
      Type result = (Type)internTable.get(descriptor);
      if (result != null) {
         return result;
      } else {
         char firstChar;
         try {
            firstChar = descriptor.charAt(0);
         } catch (IndexOutOfBoundsException var7) {
            throw new IllegalArgumentException("descriptor is empty");
         } catch (NullPointerException var8) {
            throw new NullPointerException("descriptor == null");
         }

         if (firstChar == '[') {
            result = intern(descriptor.substring(1));
            return result.getArrayType();
         } else {
            int length = descriptor.length();
            if (firstChar == 'L' && descriptor.charAt(length - 1) == ';') {
               int limit = length - 1;
               int i = 1;

               while(i < limit) {
                  char c = descriptor.charAt(i);
                  switch (c) {
                     case '(':
                     case ')':
                     case '.':
                     case ';':
                     case '[':
                        throw new IllegalArgumentException("bad descriptor: " + descriptor);
                     case '/':
                        if (i == 1 || i == length - 1 || descriptor.charAt(i - 1) == '/') {
                           throw new IllegalArgumentException("bad descriptor: " + descriptor);
                        }
                     default:
                        ++i;
                  }
               }

               result = new Type(descriptor, 9);
               return putIntern(result);
            } else {
               throw new IllegalArgumentException("bad descriptor: " + descriptor);
            }
         }
      }
   }

   public static Type internReturnType(String descriptor) {
      try {
         if (descriptor.equals("V")) {
            return VOID;
         }
      } catch (NullPointerException var2) {
         throw new NullPointerException("descriptor == null");
      }

      return intern(descriptor);
   }

   public static Type internClassName(String name) {
      if (name == null) {
         throw new NullPointerException("name == null");
      } else {
         return name.startsWith("[") ? intern(name) : intern('L' + name + ';');
      }
   }

   private Type(String descriptor, int basicType, int newAt) {
      if (descriptor == null) {
         throw new NullPointerException("descriptor == null");
      } else if (basicType >= 0 && basicType < 11) {
         if (newAt < -1) {
            throw new IllegalArgumentException("newAt < -1");
         } else {
            this.descriptor = descriptor;
            this.basicType = basicType;
            this.newAt = newAt;
            this.arrayType = null;
            this.componentType = null;
            this.initializedType = null;
         }
      } else {
         throw new IllegalArgumentException("bad basicType");
      }
   }

   private Type(String descriptor, int basicType) {
      this(descriptor, basicType, -1);
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else {
         return !(other instanceof Type) ? false : this.descriptor.equals(((Type)other).descriptor);
      }
   }

   public int hashCode() {
      return this.descriptor.hashCode();
   }

   public int compareTo(Type other) {
      return this.descriptor.compareTo(other.descriptor);
   }

   public String toString() {
      return this.descriptor;
   }

   public String toHuman() {
      switch (this.basicType) {
         case 0:
            return "void";
         case 1:
            return "boolean";
         case 2:
            return "byte";
         case 3:
            return "char";
         case 4:
            return "double";
         case 5:
            return "float";
         case 6:
            return "int";
         case 7:
            return "long";
         case 8:
            return "short";
         case 9:
            if (this.isArray()) {
               return this.getComponentType().toHuman() + "[]";
            }

            return this.getClassName().replace("/", ".");
         default:
            return this.descriptor;
      }
   }

   public Type getType() {
      return this;
   }

   public Type getFrameType() {
      switch (this.basicType) {
         case 1:
         case 2:
         case 3:
         case 6:
         case 8:
            return INT;
         case 4:
         case 5:
         case 7:
         default:
            return this;
      }
   }

   public int getBasicType() {
      return this.basicType;
   }

   public int getBasicFrameType() {
      switch (this.basicType) {
         case 1:
         case 2:
         case 3:
         case 6:
         case 8:
            return 6;
         case 4:
         case 5:
         case 7:
         default:
            return this.basicType;
      }
   }

   public boolean isConstant() {
      return false;
   }

   public String getDescriptor() {
      return this.descriptor;
   }

   public String getClassName() {
      if (this.className == null) {
         if (!this.isReference()) {
            throw new IllegalArgumentException("not an object type: " + this.descriptor);
         }

         if (this.descriptor.charAt(0) == '[') {
            this.className = this.descriptor;
         } else {
            this.className = this.descriptor.substring(1, this.descriptor.length() - 1);
         }
      }

      return this.className;
   }

   public int getCategory() {
      switch (this.basicType) {
         case 4:
         case 7:
            return 2;
         default:
            return 1;
      }
   }

   public boolean isCategory1() {
      switch (this.basicType) {
         case 4:
         case 7:
            return false;
         default:
            return true;
      }
   }

   public boolean isCategory2() {
      switch (this.basicType) {
         case 4:
         case 7:
            return true;
         default:
            return false;
      }
   }

   public boolean isIntlike() {
      switch (this.basicType) {
         case 1:
         case 2:
         case 3:
         case 6:
         case 8:
            return true;
         case 4:
         case 5:
         case 7:
         default:
            return false;
      }
   }

   public boolean isPrimitive() {
      switch (this.basicType) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
            return true;
         default:
            return false;
      }
   }

   public boolean isReference() {
      return this.basicType == 9;
   }

   public boolean isArray() {
      return this.descriptor.charAt(0) == '[';
   }

   public boolean isArrayOrKnownNull() {
      return this.isArray() || this.equals(KNOWN_NULL);
   }

   public boolean isUninitialized() {
      return this.newAt >= 0;
   }

   public int getNewAt() {
      return this.newAt;
   }

   public Type getInitializedType() {
      if (this.initializedType == null) {
         throw new IllegalArgumentException("initialized type: " + this.descriptor);
      } else {
         return this.initializedType;
      }
   }

   public Type getArrayType() {
      if (this.arrayType == null) {
         this.arrayType = putIntern(new Type('[' + this.descriptor, 9));
      }

      return this.arrayType;
   }

   public Type getComponentType() {
      if (this.componentType == null) {
         if (this.descriptor.charAt(0) != '[') {
            throw new IllegalArgumentException("not an array type: " + this.descriptor);
         }

         this.componentType = intern(this.descriptor.substring(1));
      }

      return this.componentType;
   }

   public Type asUninitialized(int newAt) {
      if (newAt < 0) {
         throw new IllegalArgumentException("newAt < 0");
      } else if (!this.isReference()) {
         throw new IllegalArgumentException("not a reference type: " + this.descriptor);
      } else if (this.isUninitialized()) {
         throw new IllegalArgumentException("already uninitialized: " + this.descriptor);
      } else {
         String newDesc = 'N' + Hex.u2(newAt) + this.descriptor;
         Type result = new Type(newDesc, 9, newAt);
         result.initializedType = this;
         return putIntern(result);
      }
   }

   private static Type putIntern(Type type) {
      Type result = (Type)internTable.putIfAbsent(type.getDescriptor(), type);
      return result != null ? result : type;
   }

   public static void clearInternTable() {
      internTable.clear();
      initInterns();
   }

   static {
      BOOLEAN_ARRAY = new Type("[" + BOOLEAN.descriptor, 9);
      BYTE_ARRAY = new Type("[" + BYTE.descriptor, 9);
      CHAR_ARRAY = new Type("[" + CHAR.descriptor, 9);
      DOUBLE_ARRAY = new Type("[" + DOUBLE.descriptor, 9);
      FLOAT_ARRAY = new Type("[" + FLOAT.descriptor, 9);
      INT_ARRAY = new Type("[" + INT.descriptor, 9);
      LONG_ARRAY = new Type("[" + LONG.descriptor, 9);
      OBJECT_ARRAY = new Type("[" + OBJECT.descriptor, 9);
      SHORT_ARRAY = new Type("[" + SHORT.descriptor, 9);
      initInterns();
   }
}
