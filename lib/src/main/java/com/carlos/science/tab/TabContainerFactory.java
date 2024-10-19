package com.carlos.science.tab;

import android.view.LayoutInflater;
import com.carlos.libcommon.StringFog;
import com.carlos.science.ApplicationPluginPkgName;
import com.carlos.science.FloatBallManager;
import com.carlos.science.server.module.hyxd.HYXDTab1;
import com.carlos.science.server.module.mrfz.MRFZTab;
import com.carlos.science.server.module.normal.NormalTab;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TabContainerFactory extends ApplicationPluginPkgName {
   static TabContainerFactory tabContainerFactory = new TabContainerFactory();
   private static Map<String, List<TabChild>> tabChildMap = new HashMap();
   private static List<String> controlerApplication = new ArrayList();

   public void initTag(LayoutInflater layoutInflater, FloatBallManager floatBallManager) {
      List<TabChild> childList = new ArrayList();
      childList.add(new TabChild(StringFog.decrypt("EAofWBELMRAGAQZeBAI="), StringFog.decrypt("m8vMkdjA"), new NormalTab(layoutInflater, floatBallManager, 0)));
      tabChildMap.put(StringFog.decrypt("EAofWBELMRAGAQZeBAI="), childList);
      childList = new ArrayList();
      childList.add(new TabChild(StringFog.decrypt("EAofWA0XLxYRCAAJGQdAEhcZGAwJNwcQ"), StringFog.decrypt("le7hk9T7uvn8h/HN"), new MRFZTab(layoutInflater, floatBallManager, 3)));
      tabChildMap.put(StringFog.decrypt("EAofWA0XLxYRCAAJGQdAEhcZGAwJNwcQ"), childList);
      childList = new ArrayList();
      tabChildMap.put(StringFog.decrypt("EAofWBEPMBECAFwECAAMEgo="), childList);
      childList = new ArrayList();
      childList.add(new TabChild(StringFog.decrypt("EAofWAsLKxYCHBdeARYWF0sXAQQA"), StringFog.decrypt("lu/tnubTbg=="), new HYXDTab1(layoutInflater, floatBallManager, 4)));
      tabChildMap.put(StringFog.decrypt("EAofWAsLKxYCHBdeARYWF0sXAQQA"), childList);
      childList = new ArrayList();
      childList.add(new TabChild(StringFog.decrypt("FwAU"), StringFog.decrypt("lvrIkPnCuvn8h/HN"), new NormalTab(layoutInflater, floatBallManager, 0)));
      tabChildMap.put(StringFog.decrypt("FwAU"), childList);
      Set<String> pkgList = tabChildMap.keySet();
      controlerApplication = new ArrayList(pkgList);
   }

   public static TabContainerFactory getInstance() {
      return tabContainerFactory;
   }

   public List<TabChild> getTabChildListByPackageName(String packageName) {
      List childList;
      if (tabChildMap.containsKey(packageName)) {
         childList = (List)tabChildMap.get(packageName);
      } else {
         childList = (List)tabChildMap.get(StringFog.decrypt("FwAU"));
      }

      return childList;
   }

   public List<String> getControlerApplication() {
      return controlerApplication;
   }
}
