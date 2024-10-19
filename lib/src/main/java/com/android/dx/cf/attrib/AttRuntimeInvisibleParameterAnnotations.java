package com.android.dx.cf.attrib;

import com.android.dx.rop.annotation.AnnotationsList;

public final class AttRuntimeInvisibleParameterAnnotations extends BaseParameterAnnotations {
   public static final String ATTRIBUTE_NAME = "RuntimeInvisibleParameterAnnotations";

   public AttRuntimeInvisibleParameterAnnotations(AnnotationsList parameterAnnotations, int byteLength) {
      super("RuntimeInvisibleParameterAnnotations", parameterAnnotations, byteLength);
   }
}
