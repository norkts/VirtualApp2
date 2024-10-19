package com.android.dx.rop.code;

public interface TranslationAdvice {
   boolean hasConstantOperation(Rop var1, RegisterSpec var2, RegisterSpec var3);

   boolean requiresSourcesInOrder(Rop var1, RegisterSpecList var2);

   int getMaxOptimalRegisterCount();
}
