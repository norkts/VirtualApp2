package com.android.dx.rop.code;

import com.android.dx.rop.cst.CstString;

public class LocalItem implements Comparable<LocalItem> {
   private final CstString name;
   private final CstString signature;

   public static LocalItem make(CstString name, CstString signature) {
      return name == null && signature == null ? null : new LocalItem(name, signature);
   }

   private LocalItem(CstString name, CstString signature) {
      this.name = name;
      this.signature = signature;
   }

   public boolean equals(Object other) {
      if (!(other instanceof LocalItem)) {
         return false;
      } else {
         LocalItem local = (LocalItem)other;
         return 0 == this.compareTo(local);
      }
   }

   private static int compareHandlesNulls(CstString a, CstString b) {
      if (a == b) {
         return 0;
      } else if (a == null) {
         return -1;
      } else {
         return b == null ? 1 : a.compareTo(b);
      }
   }

   public int compareTo(LocalItem local) {
      int ret = compareHandlesNulls(this.name, local.name);
      if (ret != 0) {
         return ret;
      } else {
         ret = compareHandlesNulls(this.signature, local.signature);
         return ret;
      }
   }

   public int hashCode() {
      return (this.name == null ? 0 : this.name.hashCode()) * 31 + (this.signature == null ? 0 : this.signature.hashCode());
   }

   public String toString() {
      if (this.name != null && this.signature == null) {
         return this.name.toQuoted();
      } else {
         return this.name == null && this.signature == null ? "" : "[" + (this.name == null ? "" : this.name.toQuoted()) + "|" + (this.signature == null ? "" : this.signature.toQuoted());
      }
   }

   public CstString getName() {
      return this.name;
   }

   public CstString getSignature() {
      return this.signature;
   }
}
