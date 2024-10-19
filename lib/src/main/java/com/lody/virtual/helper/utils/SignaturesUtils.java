package com.lody.virtual.helper.utils;

import android.content.pm.Signature;
import java.util.ArrayList;
import java.util.List;

public class SignaturesUtils {
   public static int compareSignatures(Signature[] s1, Signature[] s2) {
      if (s1 == null) {
         return s2 == null ? 1 : -1;
      } else if (s2 == null) {
         return -2;
      } else if (s1.length != s2.length) {
         return -3;
      } else if (s1.length == 1) {
         return s1[0].equals(s2[0]) ? 0 : -3;
      } else {
         List<Signature> set1 = new ArrayList();
         Signature[] var3 = s1;
         int var4 = s1.length;

         int var5;
         for(var5 = 0; var5 < var4; ++var5) {
            Signature sig = var3[var5];
            set1.add(sig);
         }

         List<Signature> set2 = new ArrayList();
         Signature[] var9 = s2;
         var5 = s2.length;

         for(int var10 = 0; var10 < var5; ++var10) {
            Signature sig = var9[var10];
            set2.add(sig);
         }

         return set1.equals(set2) ? 0 : -3;
      }
   }
}
