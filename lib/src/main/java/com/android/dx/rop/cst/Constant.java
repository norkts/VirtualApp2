package com.android.dx.rop.cst;

import com.android.dx.util.ToHuman;

public abstract class Constant implements ToHuman, Comparable<Constant> {
   public abstract boolean isCategory2();

   public abstract String typeName();

   public final int compareTo(Constant other) {
      Class clazz = this.getClass();
      Class otherClazz = other.getClass();
      return clazz != otherClazz ? clazz.getName().compareTo(otherClazz.getName()) : this.compareTo0(other);
   }

   protected abstract int compareTo0(Constant var1);
}
