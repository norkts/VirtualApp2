package com.android.dx.rop.annotation;

import com.android.dx.rop.cst.CstType;
import com.android.dx.util.MutabilityControl;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;

public final class Annotations extends MutabilityControl implements Comparable<Annotations> {
   public static final Annotations EMPTY = new Annotations();
   private final TreeMap<CstType, Annotation> annotations = new TreeMap();

   public static Annotations combine(Annotations a1, Annotations a2) {
      Annotations result = new Annotations();
      result.addAll(a1);
      result.addAll(a2);
      result.setImmutable();
      return result;
   }

   public static Annotations combine(Annotations annotations, Annotation annotation) {
      Annotations result = new Annotations();
      result.addAll(annotations);
      result.add(annotation);
      result.setImmutable();
      return result;
   }

   public int hashCode() {
      return this.annotations.hashCode();
   }

   public boolean equals(Object other) {
      if (!(other instanceof Annotations)) {
         return false;
      } else {
         Annotations otherAnnotations = (Annotations)other;
         return this.annotations.equals(otherAnnotations.annotations);
      }
   }

   public int compareTo(Annotations other) {
      Iterator<Annotation> thisIter = this.annotations.values().iterator();
      Iterator<Annotation> otherIter = other.annotations.values().iterator();

      while(thisIter.hasNext() && otherIter.hasNext()) {
         Annotation thisOne = (Annotation)thisIter.next();
         Annotation otherOne = (Annotation)otherIter.next();
         int result = thisOne.compareTo(otherOne);
         if (result != 0) {
            return result;
         }
      }

      if (thisIter.hasNext()) {
         return 1;
      } else {
         return otherIter.hasNext() ? -1 : 0;
      }
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      boolean first = true;
      sb.append("annotations{");

      Annotation a;
      for(Iterator var3 = this.annotations.values().iterator(); var3.hasNext(); sb.append(a.toHuman())) {
         a = (Annotation)var3.next();
         if (first) {
            first = false;
         } else {
            sb.append(", ");
         }
      }

      sb.append("}");
      return sb.toString();
   }

   public int size() {
      return this.annotations.size();
   }

   public void add(Annotation annotation) {
      this.throwIfImmutable();
      if (annotation == null) {
         throw new NullPointerException("annotation == null");
      } else {
         CstType type = annotation.getType();
         if (this.annotations.containsKey(type)) {
            throw new IllegalArgumentException("duplicate type: " + type.toHuman());
         } else {
            this.annotations.put(type, annotation);
         }
      }
   }

   public void addAll(Annotations toAdd) {
      this.throwIfImmutable();
      if (toAdd == null) {
         throw new NullPointerException("toAdd == null");
      } else {
         Iterator var2 = toAdd.annotations.values().iterator();

         while(var2.hasNext()) {
            Annotation a = (Annotation)var2.next();
            this.add(a);
         }

      }
   }

   public Collection<Annotation> getAnnotations() {
      return Collections.unmodifiableCollection(this.annotations.values());
   }

   static {
      EMPTY.setImmutable();
   }
}
