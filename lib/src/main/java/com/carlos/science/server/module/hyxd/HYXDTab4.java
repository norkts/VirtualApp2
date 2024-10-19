package com.carlos.science.server.module.hyxd;

import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import com.carlos.libcommon.StringFog;
import com.carlos.science.FloatBallManager;
import com.carlos.science.client.core.MemorySRWData;
import com.carlos.science.tab.FloatTab;
import com.carlos.science.tab.TabContainer;
import com.kook.common.utils.HVLog;
import com.kook.controller.client.hyxd.IHYXDController;

public class HYXDTab4 extends TabContainer {
   String TAG = StringFog.decrypt("PQoAGwQCCxIB");
   public static final int CHECKLAYOUT_SWICH_ID_41 = 41;
   public static final int CHECKLAYOUT_SWICH_ID_42 = 42;
   public static final int CHECKLAYOUT_SWICH_ID_43 = 43;
   public static final int CHECKLAYOUT_SWICH_ID_44 = 44;
   public static final int CHECKLAYOUT_SWICH_ID_45 = 45;
   public static final int CHECKLAYOUT_SWICH_ID_46 = 46;
   FloatTab floatTab;
   Button featuresMenu;

   public HYXDTab4(LayoutInflater layoutInflater, FloatBallManager floatBallManager, int tabflag) {
      super(layoutInflater, floatBallManager, tabflag);
   }

   protected int getViewId() {
      return 0;
   }

   protected void initViews(ViewGroup view) {
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("lsHVk+vru8vuifzYgeL+U0Wb1fuG/v+G5dKZ6fA=")).setOnCheckedChangeListener(this).setSwitchId(41));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("lsHVk+vru8vuifzYgeL+U0WV0feG0tyF3cmX//g=")).setOnCheckedChangeListener(this).setSwitchId(42));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("l97JkOHhu8vuifzYgeL+U0Wa+uaLxMeK+/OX/d0=")).setOnCheckedChangeListener(this).setSwitchId(43));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("m/XPk/nef1NDi8jKjubHmuTzk/nef5bz45vp7YfR4oHIzIzzwZbz45TA3YrXwILIyY3JzZvq3ZfM64rWyw==")).setOnCheckedChangeListener(this).setSwitchId(44));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("m/XPk/neuf3Lh//gSU9OluvJn/zKuu/8iu7A")).setOnCheckedChangeListener(this).setSwitchId(45));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("m/XPk/neuf3Lh//gSU9OleTQk8Hjuu/8iu7A")).setOnCheckedChangeListener(this).setSwitchId(46));
   }

   IHYXDController getHYXDController() {
      IBinder binder = this.floatTab.getClientBinder();
      HVLog.i(this.TAG, StringFog.decrypt("UwIXAi03BzcgABwEGwACHwAAVgcHMRcGHUg=") + binder + StringFog.decrypt("U0VSVgwdHRoNCxcCKAMHBQBITA==") + binder.isBinderAlive());
      return IHYXDController.Stub.asInterface(binder);
   }

   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      if (isChecked) {
         MemorySRWData memorySRWData = null;
         switch (buttonView.getId()) {
            case 41:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("Q0tD"), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("XlVcRw=="), 16);
               memorySRWData.append(StringFog.decrypt("Q0tE"), 32);
               memorySRWData.append(StringFog.decrypt("Q0tG"), 48);
               memorySRWData.append(StringFog.decrypt("QlU="), 64);
               memorySRWData.append(StringFog.decrypt("Q0tA"), 80);
               memorySRWData.append(StringFog.decrypt("Q0tGRVM="), 96);
               memorySRWData.append(StringFog.decrypt("Q0tFR1I="), 112);
               memorySRWData.append(StringFog.decrypt("QktAQw=="), 128);
               memorySRWData.writeValue(StringFog.decrypt("Qw=="), StringFog.decrypt("QVVC"));
               memorySRWData.writeValue(StringFog.decrypt("QlM="), StringFog.decrypt("QVVC"));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 42:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QktFTlJaZkpaX0ZGWgk="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QUtETlJbOQ=="), 64);
               memorySRWData.append(StringFog.decrypt("QUtERFAI"), 80);
               memorySRWData.append(StringFog.decrypt("QUtHEA=="), 112);
               memorySRWData.append(StringFog.decrypt("QUtBQVAI"), 144);
               memorySRWData.append(StringFog.decrypt("QUtDRFAI"), 208);
               memorySRWData.writeValue(StringFog.decrypt("Qx1HRkU="), StringFog.decrypt("Q0tD"));
               memorySRWData.writeValue(StringFog.decrypt("Qx1FRkU="), StringFog.decrypt("Q0tD"));
               memorySRWData.writeValue(StringFog.decrypt("Qx1LRg=="), StringFog.decrypt("Q0tD"));
               memorySRWData.writeValue(StringFog.decrypt("Qx02RkU="), StringFog.decrypt("Q0tD"));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 43:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QktCEA=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QktGRlRcZktXWUZDW1tWFkhGQwNO"), 12);
               memorySRWData.append(StringFog.decrypt("Q0tDRlVeb0NTX0NEUF9fQQM="), 40);
               memorySRWData.append(StringFog.decrypt("U1U="), 44);
               memorySRWData.append(StringFog.decrypt("Q0U="), 48);
               memorySRWData.append(StringFog.decrypt("Q0U="), 52);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 56);
               memorySRWData.writeValue(StringFog.decrypt("Qx1ATkU="), StringFog.decrypt("Rg=="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 44:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("Q0tERFRYb0paV0dIW1ddSlxFEA=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("Q0tFRlJfb0VbXUpGUVZbRFBAEEU="), -16);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), -8);
               memorySRWData.append(StringFog.decrypt("Q0tCRlxXZkpaVktHXllaS1dHTlcI"), 4);
               memorySRWData.writeValue(StringFog.decrypt("Xl0="), StringFog.decrypt("QA=="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 45:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QktCEA=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("XlZARlVAbxVD"), 4);
               memorySRWData.append(StringFog.decrypt("XldCQktZaEdQXUBFWVZZRAM="), 8);
               memorySRWData.append(StringFog.decrypt("XlZARlVAbxU="), 12);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 20);
               memorySRWData.writeValue(StringFog.decrypt("Rw=="), StringFog.decrypt("Qw=="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 46:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QktCEA=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.setAddressPermission(true);
               memorySRWData.writeValue(StringFog.decrypt("Qw=="), StringFog.decrypt("SlxLTl1W"));
               this.setMemorySRWdata(memorySRWData);
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QktCEEU="), MemorySRWData.SearchValueType.f32);
               memorySRWData.setAddressPermission(true);
               memorySRWData.writeValue(StringFog.decrypt("Qw=="), StringFog.decrypt("SlxLTl1W"));
               this.setMemorySRWdata(memorySRWData);
         }

      }
   }

   public void setMemorySRWdata(MemorySRWData memorySRWData) {
      IHYXDController hyxdController = this.getHYXDController();

      try {
         hyxdController.memorySRWData(memorySRWData.getSearchValue(), memorySRWData.getWriteValue(), memorySRWData.getAddressPermission());
      } catch (RemoteException var4) {
         RemoteException e = var4;
         e.printStackTrace();
         HVLog.printException(e);
      }

   }

   public Object getTabContainerData() {
      return null;
   }

   public void setFloatTab(FloatTab tabLayout) {
      this.floatTab = tabLayout;
      this.featuresMenu = this.floatTab.getFeaturesMenu();
   }

   public void onAttachedToWindow(FloatTab floatTab) {
      if (this.featuresMenu != null) {
         this.featuresMenu.setVisibility(4);
      }

   }
}
