package com.android.dx.rop.cst;

import com.android.dx.rop.type.Type;

public final class CstMethodHandle extends TypedConstant {
   public static final int METHOD_HANDLE_TYPE_STATIC_PUT = 0;
   public static final int METHOD_HANDLE_TYPE_STATIC_GET = 1;
   public static final int METHOD_HANDLE_TYPE_INSTANCE_PUT = 2;
   public static final int METHOD_HANDLE_TYPE_INSTANCE_GET = 3;
   public static final int METHOD_HANDLE_TYPE_INVOKE_STATIC = 4;
   public static final int METHOD_HANDLE_TYPE_INVOKE_INSTANCE = 5;
   public static final int METHOD_HANDLE_TYPE_INVOKE_CONSTRUCTOR = 6;
   public static final int METHOD_HANDLE_TYPE_INVOKE_DIRECT = 7;
   public static final int METHOD_HANDLE_TYPE_INVOKE_INTERFACE = 8;
   private static final String[] TYPE_NAMES = new String[]{"static-put", "static-get", "instance-put", "instance-get", "invoke-static", "invoke-instance", "invoke-constructor", "invoke-direct", "invoke-interface"};
   private final int type;
   private final Constant ref;

   public static CstMethodHandle make(int type, Constant ref) {
      if (isAccessor(type)) {
         if (!(ref instanceof CstFieldRef)) {
            throw new IllegalArgumentException("ref has wrong type: " + ref.getClass());
         }
      } else {
         if (!isInvocation(type)) {
            throw new IllegalArgumentException("type is out of range: " + type);
         }

         if (!(ref instanceof CstBaseMethodRef)) {
            throw new IllegalArgumentException("ref has wrong type: " + ref.getClass());
         }
      }

      return new CstMethodHandle(type, ref);
   }

   private CstMethodHandle(int type, Constant ref) {
      this.type = type;
      this.ref = ref;
   }

   public Constant getRef() {
      return this.ref;
   }

   public int getMethodHandleType() {
      return this.type;
   }

   public static boolean isAccessor(int type) {
      switch (type) {
         case 0:
         case 1:
         case 2:
         case 3:
            return true;
         default:
            return false;
      }
   }

   public boolean isAccessor() {
      return isAccessor(this.type);
   }

   public static boolean isInvocation(int type) {
      switch (type) {
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

   public boolean isInvocation() {
      return isInvocation(this.type);
   }

   public static String getMethodHandleTypeName(int type) {
      return TYPE_NAMES[type];
   }

   public boolean isCategory2() {
      return false;
   }

   protected int compareTo0(Constant other) {
      CstMethodHandle otherHandle = (CstMethodHandle)other;
      return this.getMethodHandleType() == otherHandle.getMethodHandleType() ? this.getRef().compareTo(otherHandle.getRef()) : Integer.compare(this.getMethodHandleType(), otherHandle.getMethodHandleType());
   }

   public String toString() {
      return "method-handle{" + this.toHuman() + "}";
   }

   public String typeName() {
      return "method handle";
   }

   public String toHuman() {
      return getMethodHandleTypeName(this.type) + "," + this.ref.toString();
   }

   public Type getType() {
      return Type.METHOD_HANDLE;
   }
}
