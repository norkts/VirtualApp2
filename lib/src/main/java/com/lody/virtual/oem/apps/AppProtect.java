package com.lody.virtual.oem.apps;

import com.lody.virtual.StringFog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import java.util.ArrayList;
import java.util.List;

public class AppProtect {
   public static void handleForAppProject(String packageName) {
      try {
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRg+DmwjAiFONygZIy42PW8nMFN9DDA/LRg2LQ==")), AppProtect.class.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQFRQRiDiAwOy42OW4FJFFsNzgqLhhSVg==")), Thread.class, new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               StackTraceElement[] ret = (StackTraceElement[])param.getResult();
               List<StackTraceElement> list = new ArrayList();
               int length = ret.length;

               for(int i = 0; i < length; ++i) {
                  StackTraceElement element = ret[i];
                  if (!element.toString().contains(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+CGgFRSVgJA5F"))) && element.toString().contains(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7IxglDmczNDdrESgvJRc6LU4wQSRvJAoALD1XKGoFLDNsAR46Jj5SVg==")))) {
                     list.add(element);
                     StackTraceElement stackTraceElement = new StackTraceElement(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7IxglDmYjAgZqDiQaKgcYXWMVMCBpDjBF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBg+CGgFHitlNAY2KBUiKmozOC9oJzg/IxgAKg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2LGUaOC9mEQZLKRguPW4jASZqNzg9LRhSVg==")), 6963);
                     list.add(stackTraceElement);
                     StackTraceElement stackTraceElement2 = new StackTraceElement(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7IxglDmYjAgZqDiQaKgcYXWMVMCBpDjBF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWgaLANIViMvM18mVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2LGUaOC9mEQZLKRguPW4jASZqNzg9LRhSVg==")), 258);
                     list.add(stackTraceElement2);
                     StackTraceElement stackTraceElement3 = new StackTraceElement(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7IxglDmYjAgZqDiQaKgcYXWMVMCBpDjMtIj5SVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBg+CGgFHitoDjApIy0iM2kjSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2LGUaOC9mEQZLKRguPW4jASZqNzg9LRhSVg==")), 1983);
                     list.add(stackTraceElement3);
                     StackTraceElement stackTraceElement4 = new StackTraceElement(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k1IykYX24jMCxlESg5")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgYKW8FJAZ9JBoNKAgqL24jEis=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBg+CGgFHithMFkyLwg+OQ==")), 106);
                     list.add(stackTraceElement4);
                     StackTraceElement stackTraceElement5 = new StackTraceElement(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k1IykYU28FNAJrDgpF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgAD28FSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OxgAD28FNARONAI7LD0iVg==")), 236);
                     list.add(stackTraceElement5);
                     StackTraceElement stackTraceElement6 = new StackTraceElement(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7IxglDmYjAgZqDiQaKgcYXWMVMCBpDjBF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+CWojSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2LGUaOC9mEQZLKRguPW4jASZqNzg9LRhSVg==")), 8060);
                     list.add(stackTraceElement6);
                     StackTraceElement stackTraceElement7 = new StackTraceElement(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4+LmtSBiR9Dlk9Oj4uPWkVOCtoJC8bJBguCmMaAi8=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLmozQSs=")), "", -2);
                     list.add(stackTraceElement7);
                     StackTraceElement stackTraceElement8 = new StackTraceElement(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmwjMAZrDgobLRgDKmAjNyl9NwodIz42L2sLAgRlHi8zLgc2M28aRTJmJywzJgcMPW8xLDdgHlE/Iz5SVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj0uCA==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ij0uCGwFAiNiDAY2KQg1DmwVQT5oAVRF")), 656);
                     list.add(stackTraceElement8);
                     StackTraceElement stackTraceElement9 = new StackTraceElement(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmwjMAZrDgobLRgDKmAjNyljNxogKQccJ2IFBgVsAVRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+CWojSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ID0YPWowMCtrDlkzLF4YCm4gHjc=")), 976);
                     list.add(stackTraceElement9);
                     break;
                  }
               }

               param.setResult(list.toArray());
            }
         });
      } catch (Throwable var2) {
      }

   }
}
