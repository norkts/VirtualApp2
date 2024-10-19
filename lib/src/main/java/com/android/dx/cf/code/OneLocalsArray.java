package com.android.dx.cf.code;

import com.android.dex.util.ExceptionWithContext;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.util.Hex;

public class OneLocalsArray extends LocalsArray {
   private final TypeBearer[] locals;

   public OneLocalsArray(int maxLocals) {
      super(maxLocals != 0);
      this.locals = new TypeBearer[maxLocals];
   }

   public OneLocalsArray copy() {
      OneLocalsArray result = new OneLocalsArray(this.locals.length);
      System.arraycopy(this.locals, 0, result.locals, 0, this.locals.length);
      return result;
   }

   public void annotate(ExceptionWithContext ex) {
      for(int i = 0; i < this.locals.length; ++i) {
         TypeBearer type = this.locals[i];
         String s = type == null ? "<invalid>" : type.toString();
         ex.addContext("locals[" + Hex.u2(i) + "]: " + s);
      }

   }

   public String toHuman() {
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < this.locals.length; ++i) {
         TypeBearer type = this.locals[i];
         String s = type == null ? "<invalid>" : type.toString();
         sb.append("locals[" + Hex.u2(i) + "]: " + s + "\n");
      }

      return sb.toString();
   }

   public void makeInitialized(Type type) {
      int len = this.locals.length;
      if (len != 0) {
         this.throwIfImmutable();
         Type initializedType = type.getInitializedType();

         for(int i = 0; i < len; ++i) {
            if (this.locals[i] == type) {
               this.locals[i] = initializedType;
            }
         }

      }
   }

   public int getMaxLocals() {
      return this.locals.length;
   }

   public void set(int idx, TypeBearer type) {
      this.throwIfImmutable();

      try {
         type = type.getFrameType();
      } catch (NullPointerException var4) {
         throw new NullPointerException("type == null");
      }

      if (idx < 0) {
         throw new IndexOutOfBoundsException("idx < 0");
      } else {
         if (type.getType().isCategory2()) {
            this.locals[idx + 1] = null;
         }

         this.locals[idx] = type;
         if (idx != 0) {
            TypeBearer prev = this.locals[idx - 1];
            if (prev != null && prev.getType().isCategory2()) {
               this.locals[idx - 1] = null;
            }
         }

      }
   }

   public void set(RegisterSpec spec) {
      this.set(spec.getReg(), spec);
   }

   public void invalidate(int idx) {
      this.throwIfImmutable();
      this.locals[idx] = null;
   }

   public TypeBearer getOrNull(int idx) {
      return this.locals[idx];
   }

   public TypeBearer get(int idx) {
      TypeBearer result = this.locals[idx];
      return result == null ? throwSimException(idx, "invalid") : result;
   }

   public TypeBearer getCategory1(int idx) {
      TypeBearer result = this.get(idx);
      Type type = result.getType();
      if (type.isUninitialized()) {
         return throwSimException(idx, "uninitialized instance");
      } else {
         return type.isCategory2() ? throwSimException(idx, "category-2") : result;
      }
   }

   public TypeBearer getCategory2(int idx) {
      TypeBearer result = this.get(idx);
      return result.getType().isCategory1() ? throwSimException(idx, "category-1") : result;
   }

   public LocalsArray merge(LocalsArray other) {
      return (LocalsArray)(other instanceof OneLocalsArray ? this.merge((OneLocalsArray)other) : other.merge(this));
   }

   public OneLocalsArray merge(OneLocalsArray other) {
      try {
         return Merger.mergeLocals(this, other);
      } catch (SimException var3) {
         SimException ex = var3;
         ex.addContext("underlay locals:");
         this.annotate(ex);
         ex.addContext("overlay locals:");
         other.annotate(ex);
         throw ex;
      }
   }

   public LocalsArraySet mergeWithSubroutineCaller(LocalsArray other, int predLabel) {
      LocalsArraySet result = new LocalsArraySet(this.getMaxLocals());
      return result.mergeWithSubroutineCaller(other, predLabel);
   }

   protected OneLocalsArray getPrimary() {
      return this;
   }

   private static TypeBearer throwSimException(int idx, String msg) {
      throw new SimException("local " + Hex.u2(idx) + ": " + msg);
   }
}
