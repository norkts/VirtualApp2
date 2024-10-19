package com.android.dx.cf.attrib;

import com.android.dx.rop.annotation.AnnotationsList;

public final class AttRuntimeVisibleParameterAnnotations extends BaseParameterAnnotations {
   public static final String ATTRIBUTE_NAME = "RuntimeVisibleParameterAnnotations";

   public AttRuntimeVisibleParameterAnnotations(AnnotationsList annotations, int byteLength) {
      super("RuntimeVisibleParameterAnnotations", annotations, byteLength);
   }
}
