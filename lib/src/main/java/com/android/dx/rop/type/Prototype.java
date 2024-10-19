package com.android.dx.rop.type;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class Prototype implements Comparable<Prototype> {
   private static final ConcurrentMap<String, Prototype> internTable = new ConcurrentHashMap(10000, 0.75F);
   private final String descriptor;
   private final Type returnType;
   private final StdTypeList parameterTypes;
   private StdTypeList parameterFrameTypes;

   public static Prototype intern(String descriptor) {
      if (descriptor == null) {
         throw new NullPointerException("descriptor == null");
      } else {
         Prototype result = (Prototype)internTable.get(descriptor);
         if (result != null) {
            return result;
         } else {
            result = fromDescriptor(descriptor);
            return putIntern(result);
         }
      }
   }

   public static Prototype fromDescriptor(String descriptor) {
      Prototype result = (Prototype)internTable.get(descriptor);
      if (result != null) {
         return result;
      } else {
         Type[] params = makeParameterArray(descriptor);
         int paramCount = 0;
         int at = 1;

         while(true) {
            int startAt = at;
            char c = descriptor.charAt(at);
            int endAt;
            if (c == ')') {
               ++at;
               Type returnType = Type.internReturnType(descriptor.substring(at));
               StdTypeList parameterTypes = new StdTypeList(paramCount);

               for(endAt = 0; endAt < paramCount; ++endAt) {
                  parameterTypes.set(endAt, params[endAt]);
               }

               return new Prototype(descriptor, returnType, parameterTypes);
            }

            while(c == '[') {
               ++at;
               c = descriptor.charAt(at);
            }

            if (c == 'L') {
               endAt = descriptor.indexOf(59, at);
               if (endAt == -1) {
                  throw new IllegalArgumentException("bad descriptor");
               }

               at = endAt + 1;
            } else {
               ++at;
            }

            params[paramCount] = Type.intern(descriptor.substring(startAt, at));
            ++paramCount;
         }
      }
   }

   public static void clearInternTable() {
      internTable.clear();
   }

   private static Type[] makeParameterArray(String descriptor) {
      int length = descriptor.length();
      if (descriptor.charAt(0) != '(') {
         throw new IllegalArgumentException("bad descriptor");
      } else {
         int closeAt = 0;
         int maxParams = 0;

         for(int i = 1; i < length; ++i) {
            char c = descriptor.charAt(i);
            if (c == ')') {
               closeAt = i;
               break;
            }

            if (c >= 'A' && c <= 'Z') {
               ++maxParams;
            }
         }

         if (closeAt != 0 && closeAt != length - 1) {
            if (descriptor.indexOf(41, closeAt + 1) != -1) {
               throw new IllegalArgumentException("bad descriptor");
            } else {
               return new Type[maxParams];
            }
         } else {
            throw new IllegalArgumentException("bad descriptor");
         }
      }
   }

   public static Prototype intern(String descriptor, Type definer, boolean isStatic, boolean isInit) {
      Prototype base = intern(descriptor);
      if (isStatic) {
         return base;
      } else {
         if (isInit) {
            definer = definer.asUninitialized(Integer.MAX_VALUE);
         }

         return base.withFirstParameter(definer);
      }
   }

   public static Prototype internInts(Type returnType, int count) {
      StringBuilder sb = new StringBuilder(100);
      sb.append('(');

      for(int i = 0; i < count; ++i) {
         sb.append('I');
      }

      sb.append(')');
      sb.append(returnType.getDescriptor());
      return intern(sb.toString());
   }

   private Prototype(String descriptor, Type returnType, StdTypeList parameterTypes) {
      if (descriptor == null) {
         throw new NullPointerException("descriptor == null");
      } else if (returnType == null) {
         throw new NullPointerException("returnType == null");
      } else if (parameterTypes == null) {
         throw new NullPointerException("parameterTypes == null");
      } else {
         this.descriptor = descriptor;
         this.returnType = returnType;
         this.parameterTypes = parameterTypes;
         this.parameterFrameTypes = null;
      }
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else {
         return !(other instanceof Prototype) ? false : this.descriptor.equals(((Prototype)other).descriptor);
      }
   }

   public int hashCode() {
      return this.descriptor.hashCode();
   }

   public int compareTo(Prototype other) {
      if (this == other) {
         return 0;
      } else {
         int result = this.returnType.compareTo(other.returnType);
         if (result != 0) {
            return result;
         } else {
            int thisSize = this.parameterTypes.size();
            int otherSize = other.parameterTypes.size();
            int size = Math.min(thisSize, otherSize);

            for(int i = 0; i < size; ++i) {
               Type thisType = this.parameterTypes.get(i);
               Type otherType = other.parameterTypes.get(i);
               result = thisType.compareTo(otherType);
               if (result != 0) {
                  return result;
               }
            }

            if (thisSize < otherSize) {
               return -1;
            } else if (thisSize > otherSize) {
               return 1;
            } else {
               return 0;
            }
         }
      }
   }

   public String toString() {
      return this.descriptor;
   }

   public String getDescriptor() {
      return this.descriptor;
   }

   public Type getReturnType() {
      return this.returnType;
   }

   public StdTypeList getParameterTypes() {
      return this.parameterTypes;
   }

   public StdTypeList getParameterFrameTypes() {
      if (this.parameterFrameTypes == null) {
         int sz = this.parameterTypes.size();
         StdTypeList list = new StdTypeList(sz);
         boolean any = false;

         for(int i = 0; i < sz; ++i) {
            Type one = this.parameterTypes.get(i);
            if (one.isIntlike()) {
               any = true;
               one = Type.INT;
            }

            list.set(i, one);
         }

         this.parameterFrameTypes = any ? list : this.parameterTypes;
      }

      return this.parameterFrameTypes;
   }

   public Prototype withFirstParameter(Type param) {
      String newDesc = "(" + param.getDescriptor() + this.descriptor.substring(1);
      StdTypeList newParams = this.parameterTypes.withFirst(param);
      newParams.setImmutable();
      Prototype result = new Prototype(newDesc, this.returnType, newParams);
      return putIntern(result);
   }

   private static Prototype putIntern(Prototype desc) {
      Prototype result = (Prototype)internTable.putIfAbsent(desc.getDescriptor(), desc);
      return result != null ? result : desc;
   }
}
