package com.carlos.science;

import com.carlos.libcommon.StringFog;

public enum ServiceConfig {
   CONFIG(StringFog.decrypt("EAofWBUPPBgCCBdeCgMPABYcFwgL"));

   public final String className;

   private ServiceConfig(String className) {
      this.className = className;
   }
}
