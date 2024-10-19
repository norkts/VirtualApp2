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

public class HYXDTab1 extends TabContainer {
   public static final int CHECKLAYOUT_SWICH_ID_0 = 0;
   public static final int CHECKLAYOUT_SWICH_ID_1 = 1;
   public static final int CHECKLAYOUT_SWICH_ID_2 = 2;
   public static final int CHECKLAYOUT_SWICH_ID_3 = 3;
   public static final int CHECKLAYOUT_SWICH_ID_4 = 4;
   public static final int CHECKLAYOUT_SWICH_ID_5 = 5;
   public static final int CHECKLAYOUT_SWICH_ID_6 = 6;
   public static final int CHECKLAYOUT_SWICH_ID_7 = 7;
   FloatTab floatTab;
   Button featuresMenu;
   String TAG = StringFog.decrypt("OzwqMjEPPUI=");
   public boolean isInit = false;

   public HYXDTab1(LayoutInflater layoutInflater, FloatBallManager floatBallManager, int tabflag) {
      super(layoutInflater, floatBallManager, tabflag);
   }

   protected int getViewId() {
      return 0;
   }

   protected void initViews(ViewGroup view) {
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("luD6k9nuuszmis7wSU9OlsHVk+vrtuvRisLx")).setOnCheckedChangeListener(this).setSwitchId(1));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("lsHVk+vruf3Lh//gSU9Olsjik9nXuvbLiNvP")).setOnCheckedChangeListener(this).setSwitchId(2));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("lsHVk+vruf3Lh//gSU9Ol9/IkezHuNrcitDp")).setOnCheckedChangeListener(this).setSwitchId(3));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("lsHVk+vruf3Lh//gSU9Om+TokefXueTDiuL+")).setOnCheckedChangeListener(this).setSwitchId(4));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("m/XPk/neuf3Lh//gSU9Ol9/IkezHutfKiMjP")).setOnCheckedChangeListener(this).setSwitchId(5));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("ltX9k9f1uf3Lh//gSU9OluDak/7QturHh//5")).setOnCheckedChangeListener(this).setSwitchId(6));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("ltr3n+DjtuvRisLxSU9Olsv+kdvgueTDiuL+")).setOnCheckedChangeListener(this).setSwitchId(7));
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
            case 0:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("Q0tLT11WaUVTXUNGWgk="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("XlVcRlFZaUNWVktIUV4I"), 8);
               memorySRWData.append(StringFog.decrypt("Q0tCQlJYb0ZaVkpIWAk="), 32);
               memorySRWData.append(StringFog.decrypt("Q0tLT11WaUVTXUNGWgk="), 40);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 20);
               memorySRWData.append(StringFog.decrypt("XlVcRldeaUBWVktJXlsI"), 48);
               memorySRWData.append(StringFog.decrypt("Q0tCRlJZb0tTX0JAWwk="), 56);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 60);
               memorySRWData.writeValue(StringFog.decrypt("QlM="), StringFog.decrypt("SlxLTl1W"));
               memorySRWData.writeValue(StringFog.decrypt("RVU="), StringFog.decrypt("SlxLTl1W"));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 1:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QktD"), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("Q0tK"), -32);
               memorySRWData.append(StringFog.decrypt("Q0tD"), -16);
               memorySRWData.writeValue(StringFog.decrypt("XlRE"), StringFog.decrypt("SlxLT1xX"));
               this.setMemorySRWdata(memorySRWData);
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("Qlc="), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("Q0tA"), -112);
               memorySRWData.append(StringFog.decrypt("Qg=="), -96);
               memorySRWData.append(StringFog.decrypt("Q0tD"), -64);
               memorySRWData.append(StringFog.decrypt("Q0tCRw=="), -16);
               memorySRWData.append(StringFog.decrypt("Rw=="), 16);
               memorySRWData.writeValue(StringFog.decrypt("XlNG"), StringFog.decrypt("QQ=="));
               this.setMemorySRWdata(memorySRWData);
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("XlVcQg=="), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("QEtH"), 32);
               memorySRWData.append(StringFog.decrypt("Q0tH"), 48);
               memorySRWData.append(StringFog.decrypt("Q0tK"), 64);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 80);
               memorySRWData.writeValue(StringFog.decrypt("S1U="), StringFog.decrypt("SlxLT1xX"));
               this.setMemorySRWdata(memorySRWData);
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("Q0tD"), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 48);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 64);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 80);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 112);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 128);
               memorySRWData.append(StringFog.decrypt("QlRAQEta"), 160);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 192);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 288);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 352);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 384);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 416);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 464);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 480);
               memorySRWData.writeValue(StringFog.decrypt("Qw=="), StringFog.decrypt("SlxLT1xX"));
               memorySRWData.writeValue(StringFog.decrypt("R10="), StringFog.decrypt("SlxLT1xX"));
               memorySRWData.writeValue(StringFog.decrypt("RVE="), StringFog.decrypt("SlxLT1xX"));
               memorySRWData.writeValue(StringFog.decrypt("S1U="), StringFog.decrypt("SlxLT1xX"));
               memorySRWData.writeValue(StringFog.decrypt("QlRA"), StringFog.decrypt("SlxLT1xX"));
               memorySRWData.writeValue(StringFog.decrypt("QldK"), StringFog.decrypt("SlxLT1xX"));
               memorySRWData.writeValue(StringFog.decrypt("QlNC"), StringFog.decrypt("SlxLT1xX"));
               memorySRWData.writeValue(StringFog.decrypt("QlxA"), StringFog.decrypt("SlxLT1xX"));
               memorySRWData.writeValue(StringFog.decrypt("QV1K"), StringFog.decrypt("SlxLT1xX"));
               memorySRWData.writeValue(StringFog.decrypt("QFBA"), StringFog.decrypt("SlxLT1xX"));
               memorySRWData.writeValue(StringFog.decrypt("QF1G"), StringFog.decrypt("SlxLT1xX"));
               memorySRWData.writeValue(StringFog.decrypt("R1RE"), StringFog.decrypt("SlxLT1xX"));
               memorySRWData.writeValue(StringFog.decrypt("R1NG"), StringFog.decrypt("SlxLT1xX"));
               memorySRWData.writeValue(StringFog.decrypt("R11C"), StringFog.decrypt("SlxLT1xX"));
               this.setMemorySRWdata(memorySRWData);
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("Q0tARQ=="), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("XlRcQ1E="), 16);
               memorySRWData.writeValue(StringFog.decrypt("Qw=="), StringFog.decrypt("XlVcRFY="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 2:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QlNKQldZakE="), MemorySRWData.SearchValueType.i32);
               memorySRWData.append(StringFog.decrypt("QlNKQldZakA="), 28);
               memorySRWData.append(StringFog.decrypt("QlNFQVJcbkQ="), MemorySRWData.SearchValueType.i32, 48);
               memorySRWData.append(StringFog.decrypt("QlNFQVJaaEA="), 72);
               memorySRWData.writeValue(StringFog.decrypt("R10="), StringFog.decrypt("Qw=="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 3:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("Q0tCRlZXbUJWV0ZCW1lfSlQU"), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QlVCRlVAbxU="), 16);
               memorySRWData.append(StringFog.decrypt("QlVCRlVAbxU="), 20);
               memorySRWData.append(StringFog.decrypt("QlVCRlVAbxU="), 24);
               memorySRWData.append(StringFog.decrypt("Q0tHEA=="), 32);
               memorySRWData.writeValue(StringFog.decrypt("Qw=="), StringFog.decrypt("S1U="));
               memorySRWData.writeValue(StringFog.decrypt("QFc="), StringFog.decrypt("S1U="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 4:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("Q0tF"), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("Q0tCQw=="), 24);
               memorySRWData.append(StringFog.decrypt("QlVC"), 48);
               memorySRWData.append(StringFog.decrypt("Qg=="), 96);
               memorySRWData.append(StringFog.decrypt("XlQ="), 120);
               memorySRWData.writeValue(StringFog.decrypt("Qx1GRg=="), StringFog.decrypt("Qw=="));
               this.setMemorySRWdata(memorySRWData);
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("Q0tD"), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("R0tLE0hdbUc="), 12);
               memorySRWData.append(StringFog.decrypt("Q0tCRw=="), 48);
               memorySRWData.append(StringFog.decrypt("Qlc="), 64);
               memorySRWData.writeValue(StringFog.decrypt("Qx1C"), StringFog.decrypt("Qg=="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 5:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("Q0tLT11WaUVTXUNGWgk="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("XlVcRlFZaUNWVktIUV4I"), 8);
               memorySRWData.append(StringFog.decrypt("Q0tCQlJYb0ZaVkpIWAk="), 32);
               memorySRWData.append(StringFog.decrypt("Q0tLT11WaUVTXUNGWgk="), 40);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 20);
               memorySRWData.append(StringFog.decrypt("XlVcRldeaUBWVktJXlsI"), 48);
               memorySRWData.append(StringFog.decrypt("Q0tCRlJZb0tTX0JAWwk="), 56);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 60);
               memorySRWData.writeValue(StringFog.decrypt("QlM="), StringFog.decrypt("SlxLTl1W"));
               memorySRWData.writeValue(StringFog.decrypt("RVU="), StringFog.decrypt("SlxLTl1W"));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 6:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("RlRAQA=="), MemorySRWData.SearchValueType.i32);
               memorySRWData.append(StringFog.decrypt("QA=="), 4);
               memorySRWData.append(StringFog.decrypt("QlU="), MemorySRWData.SearchValueType.i32, 8);
               memorySRWData.append(StringFog.decrypt("QlM="), 12);
               memorySRWData.append(StringFog.decrypt("Rw=="), 16);
               memorySRWData.append(StringFog.decrypt("S1U="), 20);
               memorySRWData.append(StringFog.decrypt("Qg=="), MemorySRWData.SearchValueType.i32, 24);
               memorySRWData.append(StringFog.decrypt("QlU="), 28);
               memorySRWData.append(StringFog.decrypt("QlQ="), 40);
               memorySRWData.append(StringFog.decrypt("QFc="), 44);
               memorySRWData.writeValue(StringFog.decrypt("QV0="), StringFog.decrypt("Qw=="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 7:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QlU="), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("Q0tK"), -24);
               memorySRWData.append(StringFog.decrypt("R0tDTlZbZkBUWg=="), MemorySRWData.SearchValueType.f32, 28);
               memorySRWData.writeValue(StringFog.decrypt("QV0="), StringFog.decrypt("SlxL"));
               this.setMemorySRWdata(memorySRWData);
         }

      }
   }

   public void setMemorySRWdata(MemorySRWData memorySRWData) {
      IHYXDController hyxdController = this.getHYXDController();

      try {
         if (memorySRWData != null) {
            hyxdController.memorySRWData(memorySRWData.getSearchValue(), memorySRWData.getWriteValue(), memorySRWData.getAddressPermission());
         } else {
            hyxdController.memoryTest();
         }
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
