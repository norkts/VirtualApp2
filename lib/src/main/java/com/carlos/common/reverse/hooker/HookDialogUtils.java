package com.carlos.common.reverse.hooker;

import android.app.Dialog;
import com.carlos.libcommon.StringFog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HookDialogUtils {
   static List<Integer> hiddenDialogCode = new ArrayList(5);
   static List<String> hiddenDialog = new ArrayList();

   public static void addHiddenDialogCode(int code) {
      if (hiddenDialogCode.size() >= 5) {
         hiddenDialogCode.clear();
      }

      hiddenDialogCode.add(code);
   }

   public static boolean isHiddDialog(Dialog dialog) {
      return dialog != null && hiddenDialogCode.contains(dialog.hashCode());
   }

   public static boolean isHiddDialog(String clzName, String packageName) {
      Iterator var2 = hiddenDialog.iterator();

      while(var2.hasNext()) {
         String str = (String)var2.next();
         if (str.indexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OD5SVg=="))) > 0) {
            String[] arr = str.split(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OD5SVg==")));
            if (arr != null && arr.length == 2 && arr[0].equals(packageName) && arr[1].equals(clzName)) {
               return true;
            }
         } else if (str.equals(clzName)) {
            return true;
         }
      }

      return false;
   }

   static {
      hiddenDialog.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC0LCGszBSZgNDAaKi0XDmUzND91NzgbLgcMKWMKESlqDh0dKBguO2wzMwRiIjxBJRcYPmIaRTJuJDAJLAg+DmozPFo=")));
      hiddenDialog.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMDdhHgI1LQMYMW8aBitsNxosLAQcJQ==")));
      hiddenDialog.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojBjBmHh02LC1fPm9TIDdlNyw5LD4YIE4wPDNlVx4AKT4ACG8bMAVrEQI6JC5SVg==")));
      hiddenDialog.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojFiRmDjAsKi42MW8FMC1oAQ4gKTocJ2AjNCh7NCQdLz1fKWoFMwRrHjwdCDsmO24FNCJhNzAoIxgAPQ==")));
      hiddenDialog.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojFiRmDjAsKi42MW8FMC1oAQ4gKTocJ2AjNCh7NCQdLz1fKWoFMwRrHjwdCDwiOWwgICRpJB4cJRgYP2oFGi0=")));
      hiddenDialog.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojBilhJB4+LF4YOW8VBgRlJx4vPC4IKWFTRRNpIiwqKS1XO2oFPARkNyg1KD0ALGomLD1uJyQ6LS5SVg==")));
      hiddenDialog.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojQTdjJCA1KC0iD2knMAJsNwYeLD0qI2AgRClqNBouIwQqUm8VJCZsAR45JCwiOWwgTQFlNzA6IzwiKmsVPCNiDlkg")));
   }
}
