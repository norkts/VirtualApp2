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
         if (str.indexOf(":") > 0) {
            String[] arr = str.split(":");
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
      hiddenDialog.add("kr.co.nexon.toy.android.ui.board.NPShowTodayDialog");
      hiddenDialog.add("com.tapjoy.internal.c");
      hiddenDialog.add("com.nxth.wodn:android.app.AlertDialog");
      hiddenDialog.add("com.bluepotiongames.eosm:android.app.AlertDialog");
      hiddenDialog.add("com.bluepotiongames.eosm:android.app.ProgressDialog");
      hiddenDialog.add("com.ncsoft.android.mop.NcCampaignWebViewDialog");
      hiddenDialog.add("com.kakaogame.promotion.view.StartingPromotionFragment");
   }
}
