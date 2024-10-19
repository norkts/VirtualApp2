package com.carlos.libcommon;

import com.github.megatronking.stringfog.xor.StringFogImpl;

public final class StringFog {
   private static final StringFogImpl IMPL = new StringFogImpl();

   public static String encrypt(String value) {
      return IMPL.encrypt(value, "serven_scorpion");
   }

   public static String decrypt(String value) {
      return IMPL.decrypt(value, "serven_scorpion");
   }

   public static boolean overflow(String value) {
      return IMPL.overflow(value, "serven_scorpion");
   }
}
