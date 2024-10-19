package com.android.dx.rop.annotation;

import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.MutabilityControl;
import com.android.dx.util.ToHuman;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;

public final class Annotation extends MutabilityControl implements Comparable<Annotation>, ToHuman {
   private final CstType type;
   private final AnnotationVisibility visibility;
   private final TreeMap<CstString, NameValuePair> elements;

   public Annotation(CstType type, AnnotationVisibility visibility) {
      if (type == null) {
         throw new NullPointerException("type == null");
      } else if (visibility == null) {
         throw new NullPointerException("visibility == null");
      } else {
         this.type = type;
         this.visibility = visibility;
         this.elements = new TreeMap();
      }
   }

   public boolean equals(Object other) {
      if (!(other instanceof Annotation)) {
         return false;
      } else {
         Annotation otherAnnotation = (Annotation)other;
         return this.type.equals(otherAnnotation.type) && this.visibility == otherAnnotation.visibility ? this.elements.equals(otherAnnotation.elements) : false;
      }
   }

   public int hashCode() {
      int hash = this.type.hashCode();
      hash = hash * 31 + this.elements.hashCode();
      hash = hash * 31 + this.visibility.hashCode();
      return hash;
   }

   public int compareTo(Annotation other) {
      int result = this.type.compareTo(other.type);
      if (result != 0) {
         return result;
      } else {
         result = this.visibility.compareTo(other.visibility);
         if (result != 0) {
            return result;
         } else {
            Iterator<NameValuePair> thisIter = this.elements.values().iterator();
            Iterator<NameValuePair> otherIter = other.elements.values().iterator();

            while(thisIter.hasNext() && otherIter.hasNext()) {
               NameValuePair thisOne = (NameValuePair)thisIter.next();
               NameValuePair otherOne = (NameValuePair)otherIter.next();
               result = thisOne.compareTo(otherOne);
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
      }
   }

   public String toString() {
      return this.toHuman();
   }

   public String toHuman() {
      StringBuilder sb = new StringBuilder();
      sb.append(this.visibility.toHuman());
      sb.append("-annotation ");
      sb.append(this.type.toHuman());
      sb.append(" {");
      boolean first = true;
      Iterator var3 = this.elements.values().iterator();

      while(var3.hasNext()) {
         NameValuePair pair = (NameValuePair)var3.next();
         if (first) {
            first = false;
         } else {
            sb.append(", ");
         }

         sb.append(pair.getName().toHuman());
         sb.append(": ");
         sb.append(pair.getValue().toHuman());
      }

      sb.append("}");
      return sb.toString();
   }

   public CstType getType() {
      return this.type;
   }

   public AnnotationVisibility getVisibility() {
      return this.visibility;
   }

   public void put(NameValuePair pair) {
      this.throwIfImmutable();
      if (pair == null) {
         throw new NullPointerException("pair == null");
      } else {
         this.elements.put(pair.getName(), pair);
      }
   }

   public void add(NameValuePair pair) {
      this.throwIfImmutable();
      if (pair == null) {
         throw new NullPointerException("pair == null");
      } else {
         CstString name = pair.getName();
         if (this.elements.get(name) != null) {
            throw new IllegalArgumentException("name already added: " + name);
         } else {
            this.elements.put(name, pair);
         }
      }
   }

   public Collection<NameValuePair> getNameValuePairs() {
      return Collections.unmodifiableCollection(this.elements.values());
   }
}
