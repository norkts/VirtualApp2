package com.lody.virtual.common;

import com.github.megatronking.stringfog.xor.StringFogImpl;

public final class StringFog {
   private static final StringFogImpl IMPL = new StringFogImpl();

   public static String encrypt(String value) {
      return IMPL.encrypt(value, "kook-bug-fix");
   }

   public static String decrypt(String value) {
      return IMPL.decrypt(value, "kook-bug-fix");
   }

   public static boolean overflow(String value) {
      return IMPL.overflow(value, "kook-bug-fix");
   }
}
