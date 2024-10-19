package com.android.dx;

import com.android.dx.dex.file.ClassDefItem;
import com.android.dx.rop.annotation.Annotation;
import com.android.dx.rop.annotation.AnnotationVisibility;
import com.android.dx.rop.annotation.Annotations;
import com.android.dx.rop.annotation.NameValuePair;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstEnumRef;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import java.lang.annotation.ElementType;
import java.util.HashMap;
import java.util.Iterator;

public final class AnnotationId<D, V> {
   private final TypeId<D> declaringType;
   private final TypeId<V> type;
   private final ElementType annotatedElement;
   private final HashMap<String, NameValuePair> elements;

   private AnnotationId(TypeId<D> declaringType, TypeId<V> type, ElementType annotatedElement) {
      this.declaringType = declaringType;
      this.type = type;
      this.annotatedElement = annotatedElement;
      this.elements = new HashMap();
   }

   public static <D, V> AnnotationId<D, V> get(TypeId<D> declaringType, TypeId<V> type, ElementType annotatedElement) {
      if (annotatedElement != ElementType.TYPE && annotatedElement != ElementType.METHOD && annotatedElement != ElementType.FIELD && annotatedElement != ElementType.PARAMETER) {
         throw new IllegalArgumentException("element type is not supported to annotate yet.");
      } else {
         return new AnnotationId(declaringType, type, annotatedElement);
      }
   }

   public void set(Element element) {
      if (element == null) {
         throw new NullPointerException("element == null");
      } else {
         CstString pairName = new CstString(element.getName());
         Constant pairValue = AnnotationId.Element.toConstant(element.getValue());
         NameValuePair nameValuePair = new NameValuePair(pairName, pairValue);
         this.elements.put(element.getName(), nameValuePair);
      }
   }

   public void addToMethod(DexMaker dexMaker, MethodId<?, ?> method) {
      if (this.annotatedElement != ElementType.METHOD) {
         throw new IllegalStateException("This annotation is not for method");
      } else if (!method.declaringType.equals(this.declaringType)) {
         throw new IllegalArgumentException("Method" + method + "'s declaring type is inconsistent with" + this);
      } else {
         ClassDefItem classDefItem = dexMaker.getTypeDeclaration(this.declaringType).toClassDefItem();
         if (classDefItem == null) {
            throw new NullPointerException("No class defined item is found");
         } else {
            CstMethodRef cstMethodRef = method.constant;
            if (cstMethodRef == null) {
               throw new NullPointerException("Method reference is NULL");
            } else {
               CstType cstType = CstType.intern(this.type.ropType);
               Annotation annotation = new Annotation(cstType, AnnotationVisibility.RUNTIME);
               Annotations annotations = new Annotations();
               Iterator var8 = this.elements.values().iterator();

               while(var8.hasNext()) {
                  NameValuePair nvp = (NameValuePair)var8.next();
                  annotation.add(nvp);
               }

               annotations.add(annotation);
               classDefItem.addMethodAnnotations(cstMethodRef, annotations, dexMaker.getDexFile());
            }
         }
      }
   }

   public static final class Element {
      private final String name;
      private final Object value;

      public Element(String name, Object value) {
         if (name == null) {
            throw new NullPointerException("name == null");
         } else if (value == null) {
            throw new NullPointerException("value == null");
         } else {
            this.name = name;
            this.value = value;
         }
      }

      public String getName() {
         return this.name;
      }

      public Object getValue() {
         return this.value;
      }

      public String toString() {
         return "[" + this.name + ", " + this.value + "]";
      }

      public int hashCode() {
         return this.name.hashCode() * 31 + this.value.hashCode();
      }

      public boolean equals(Object other) {
         if (!(other instanceof Element)) {
            return false;
         } else {
            Element otherElement = (Element)other;
            return this.name.equals(otherElement.name) && this.value.equals(otherElement.value);
         }
      }

      static Constant toConstant(Object value) {
         Class clazz = value.getClass();
         if (clazz.isEnum()) {
            CstString descriptor = new CstString(TypeId.get(clazz).getName());
            CstString name = new CstString(((Enum)value).name());
            CstNat cstNat = new CstNat(name, descriptor);
            return new CstEnumRef(cstNat);
         } else if (clazz.isArray()) {
            throw new UnsupportedOperationException("Array is not supported yet");
         } else if (value instanceof TypeId) {
            throw new UnsupportedOperationException("TypeId is not supported yet");
         } else {
            return Constants.getConstant(value);
         }
      }
   }
}
