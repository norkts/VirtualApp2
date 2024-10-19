package com.android.dx.dex.cf;

import com.android.dx.cf.attrib.AttAnnotationDefault;
import com.android.dx.cf.attrib.AttEnclosingMethod;
import com.android.dx.cf.attrib.AttExceptions;
import com.android.dx.cf.attrib.AttInnerClasses;
import com.android.dx.cf.attrib.AttRuntimeInvisibleAnnotations;
import com.android.dx.cf.attrib.AttRuntimeInvisibleParameterAnnotations;
import com.android.dx.cf.attrib.AttRuntimeVisibleAnnotations;
import com.android.dx.cf.attrib.AttRuntimeVisibleParameterAnnotations;
import com.android.dx.cf.attrib.AttSignature;
import com.android.dx.cf.attrib.AttSourceDebugExtension;
import com.android.dx.cf.attrib.InnerClassList;
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.iface.AttributeList;
import com.android.dx.cf.iface.Method;
import com.android.dx.cf.iface.MethodList;
import com.android.dx.dex.file.AnnotationUtils;
import com.android.dx.rop.annotation.Annotation;
import com.android.dx.rop.annotation.AnnotationVisibility;
import com.android.dx.rop.annotation.Annotations;
import com.android.dx.rop.annotation.AnnotationsList;
import com.android.dx.rop.annotation.NameValuePair;
import com.android.dx.rop.code.AccessFlags;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.Warning;
import java.util.ArrayList;

class AttributeTranslator {
   private AttributeTranslator() {
   }

   public static TypeList getExceptions(Method method) {
      AttributeList attribs = method.getAttributes();
      AttExceptions exceptions = (AttExceptions)attribs.findFirst("Exceptions");
      return (TypeList)(exceptions == null ? StdTypeList.EMPTY : exceptions.getExceptions());
   }

   public static Annotations getAnnotations(AttributeList attribs) {
      Annotations result = getAnnotations0(attribs);
      Annotation signature = getSignature(attribs);
      Annotation sourceDebugExtension = getSourceDebugExtension(attribs);
      if (signature != null) {
         result = Annotations.combine(result, signature);
      }

      if (sourceDebugExtension != null) {
         result = Annotations.combine(result, sourceDebugExtension);
      }

      return result;
   }

   public static Annotations getClassAnnotations(DirectClassFile cf, CfOptions args) {
      CstType thisClass = cf.getThisClass();
      AttributeList attribs = cf.getAttributes();
      Annotations result = getAnnotations(attribs);
      Annotation enclosingMethod = translateEnclosingMethod(attribs);

      try {
         Annotations innerClassAnnotations = translateInnerClasses(thisClass, attribs, enclosingMethod == null);
         if (innerClassAnnotations != null) {
            result = Annotations.combine(result, innerClassAnnotations);
         }
      } catch (Warning var7) {
         Warning warn = var7;
         args.warn.println("warning: " + warn.getMessage());
      }

      if (enclosingMethod != null) {
         result = Annotations.combine(result, enclosingMethod);
      }

      if (AccessFlags.isAnnotation(cf.getAccessFlags())) {
         Annotation annotationDefault = translateAnnotationDefaults(cf);
         if (annotationDefault != null) {
            result = Annotations.combine(result, annotationDefault);
         }
      }

      return result;
   }

   public static Annotations getMethodAnnotations(Method method) {
      Annotations result = getAnnotations(method.getAttributes());
      TypeList exceptions = getExceptions(method);
      if (exceptions.size() != 0) {
         Annotation throwsAnnotation = AnnotationUtils.makeThrows(exceptions);
         result = Annotations.combine(result, throwsAnnotation);
      }

      return result;
   }

   private static Annotations getAnnotations0(AttributeList attribs) {
      AttRuntimeVisibleAnnotations visible = (AttRuntimeVisibleAnnotations)attribs.findFirst("RuntimeVisibleAnnotations");
      AttRuntimeInvisibleAnnotations invisible = (AttRuntimeInvisibleAnnotations)attribs.findFirst("RuntimeInvisibleAnnotations");
      if (visible == null) {
         return invisible == null ? Annotations.EMPTY : invisible.getAnnotations();
      } else {
         return invisible == null ? visible.getAnnotations() : Annotations.combine(visible.getAnnotations(), invisible.getAnnotations());
      }
   }

   private static Annotation getSignature(AttributeList attribs) {
      AttSignature signature = (AttSignature)attribs.findFirst("Signature");
      return signature == null ? null : AnnotationUtils.makeSignature(signature.getSignature());
   }

   private static Annotation getSourceDebugExtension(AttributeList attribs) {
      AttSourceDebugExtension extension = (AttSourceDebugExtension)attribs.findFirst("SourceDebugExtension");
      return extension == null ? null : AnnotationUtils.makeSourceDebugExtension(extension.getSmapString());
   }

   private static Annotation translateEnclosingMethod(AttributeList attribs) {
      AttEnclosingMethod enclosingMethod = (AttEnclosingMethod)attribs.findFirst("EnclosingMethod");
      if (enclosingMethod == null) {
         return null;
      } else {
         CstType enclosingClass = enclosingMethod.getEnclosingClass();
         CstNat nat = enclosingMethod.getMethod();
         return nat == null ? AnnotationUtils.makeEnclosingClass(enclosingClass) : AnnotationUtils.makeEnclosingMethod(new CstMethodRef(enclosingClass, nat));
      }
   }

   private static Annotations translateInnerClasses(CstType thisClass, AttributeList attribs, boolean needEnclosingClass) {
      AttInnerClasses innerClasses = (AttInnerClasses)attribs.findFirst("InnerClasses");
      if (innerClasses == null) {
         return null;
      } else {
         InnerClassList list = innerClasses.getInnerClasses();
         int size = list.size();
         InnerClassList.Item foundThisClass = null;
         ArrayList<Type> membersList = new ArrayList();

         int membersSize;
         CstType outer;
         for(membersSize = 0; membersSize < size; ++membersSize) {
            InnerClassList.Item item = list.get(membersSize);
            outer = item.getInnerClass();
            if (outer.equals(thisClass)) {
               foundThisClass = item;
            } else if (thisClass.equals(item.getOuterClass())) {
               membersList.add(outer.getClassType());
            }
         }

         membersSize = membersList.size();
         if (foundThisClass == null && membersSize == 0) {
            return null;
         } else {
            Annotations result = new Annotations();
            if (foundThisClass != null) {
               result.add(AnnotationUtils.makeInnerClass(foundThisClass.getInnerName(), foundThisClass.getAccessFlags()));
               if (needEnclosingClass) {
                  outer = foundThisClass.getOuterClass();
                  if (outer == null) {
                     throw new Warning("Ignoring InnerClasses attribute for an anonymous inner class\n(" + thisClass.toHuman() + ") that doesn't come with an\nassociated EnclosingMethod attribute. This class was probably produced by a\ncompiler that did not target the modern .class file format. The recommended\nsolution is to recompile the class from source, using an up-to-date compiler\nand without specifying any \"-target\" type options. The consequence of ignoring\nthis warning is that reflective operations on this class will incorrectly\nindicate that it is *not* an inner class.");
                  }

                  result.add(AnnotationUtils.makeEnclosingClass(foundThisClass.getOuterClass()));
               }
            }

            if (membersSize != 0) {
               StdTypeList typeList = new StdTypeList(membersSize);

               for(int i = 0; i < membersSize; ++i) {
                  typeList.set(i, (Type)membersList.get(i));
               }

               typeList.setImmutable();
               result.add(AnnotationUtils.makeMemberClasses(typeList));
            }

            result.setImmutable();
            return result;
         }
      }
   }

   public static AnnotationsList getParameterAnnotations(Method method) {
      AttributeList attribs = method.getAttributes();
      AttRuntimeVisibleParameterAnnotations visible = (AttRuntimeVisibleParameterAnnotations)attribs.findFirst("RuntimeVisibleParameterAnnotations");
      AttRuntimeInvisibleParameterAnnotations invisible = (AttRuntimeInvisibleParameterAnnotations)attribs.findFirst("RuntimeInvisibleParameterAnnotations");
      if (visible == null) {
         return invisible == null ? AnnotationsList.EMPTY : invisible.getParameterAnnotations();
      } else {
         return invisible == null ? visible.getParameterAnnotations() : AnnotationsList.combine(visible.getParameterAnnotations(), invisible.getParameterAnnotations());
      }
   }

   private static Annotation translateAnnotationDefaults(DirectClassFile cf) {
      CstType thisClass = cf.getThisClass();
      MethodList methods = cf.getMethods();
      int sz = methods.size();
      Annotation result = new Annotation(thisClass, AnnotationVisibility.EMBEDDED);
      boolean any = false;

      for(int i = 0; i < sz; ++i) {
         Method one = methods.get(i);
         AttributeList attribs = one.getAttributes();
         AttAnnotationDefault oneDefault = (AttAnnotationDefault)attribs.findFirst("AnnotationDefault");
         if (oneDefault != null) {
            NameValuePair pair = new NameValuePair(one.getNat().getName(), oneDefault.getValue());
            result.add(pair);
            any = true;
         }
      }

      if (!any) {
         return null;
      } else {
         result.setImmutable();
         return AnnotationUtils.makeAnnotationDefault(result);
      }
   }
}
