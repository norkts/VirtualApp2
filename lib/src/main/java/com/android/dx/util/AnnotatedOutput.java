package com.android.dx.util;

public interface AnnotatedOutput extends Output {
   boolean annotates();

   boolean isVerbose();

   void annotate(String var1);

   void annotate(int var1, String var2);

   void endAnnotation();

   int getAnnotationWidth();
}
