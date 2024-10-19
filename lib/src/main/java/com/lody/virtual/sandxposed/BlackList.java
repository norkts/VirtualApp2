package com.lody.virtual.sandxposed;

import com.lody.virtual.StringFog;
import java.util.HashSet;
import java.util.Set;

public class BlackList {
   private static Set<String> packageList = new HashSet();
   private static Set<String> processList = new HashSet();

   public static boolean canNotInject(String packageName, String processName) {
      return packageList.contains(packageName) || processList.contains(processName);
   }

   static {
      processList.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41Dm8jPzJsHig6IwhSVg==")));
   }
}
