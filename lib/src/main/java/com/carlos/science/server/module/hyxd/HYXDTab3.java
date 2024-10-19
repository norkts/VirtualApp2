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

public class HYXDTab3 extends TabContainer {
   String TAG = StringFog.decrypt("PQoAGwQCCxIB");
   public static final int CHECKLAYOUT_SWICH_ID_21 = 21;
   public static final int CHECKLAYOUT_SWICH_ID_22 = 22;
   public static final int CHECKLAYOUT_SWICH_ID_23 = 23;
   public static final int CHECKLAYOUT_SWICH_ID_24 = 24;
   public static final int CHECKLAYOUT_SWICH_ID_25 = 25;
   public static final int CHECKLAYOUT_SWICH_ID_26 = 26;
   public static final int CHECKLAYOUT_SWICH_ID_27 = 27;
   public static final int CHECKLAYOUT_SWICH_ID_28 = 28;
   public static final int CHECKLAYOUT_SWICH_ID_29 = 29;
   public static final int CHECKLAYOUT_SWICH_ID_30 = 30;
   public static final int CHECKLAYOUT_SWICH_ID_31 = 31;
   FloatTab floatTab;
   Button featuresMenu;

   public HYXDTab3(LayoutInflater layoutInflater, FloatBallManager floatBallManager, int tabflag) {
      super(layoutInflater, floatBallManager, tabflag);
   }

   protected int getViewId() {
      return 0;
   }

   protected void initViews(ViewGroup view) {
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("lPzJk9j7uf3Lh//gjNPuU0WX/PqG3M6K98CV2e4=")).setOnCheckedChangeListener(this).setSwitchId(21));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("ld3KkO3htu79ifzYgeL+U0Wa+uaLxMeK+/OVzds=")).setOnCheckedChangeListener(this).setSwitchId(22));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("ltX9k9f1tu79ifzYgeL+U0WUxtGK5/iLzv6Y3N8=")).setOnCheckedChangeListener(this).setSwitchId(23));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("l97JkOHhuvzMifzYgeL+U0WX5uyIxt2G5dKZ6fA=")).setOnCheckedChangeListener(this).setSwitchId(24));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("l97JkOHhuvzMifzYgeL+U0Wb68eL0/aG5dKZ6fA=")).setOnCheckedChangeListener(this).setSwitchId(25));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("ld3KkO3huN7qi83ejMvjU0WV0d6L1duG3/aV7tQ=")).setOnCheckedChangeListener(this).setSwitchId(26));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("lsHVk+vruvL/iObYjdfDU0WUx9iG4tWK+/OW29Y=")).setOnCheckedChangeListener(this).setSwitchId(27));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("l97JkOHhuvzMifzYgeL+U0WUx9iG4tWExs2Vy/Y=")).setOnCheckedChangeListener(this).setSwitchId(28));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("l97JkOHhuvzMifzYgeL+U0WWzN+J1tqGweiWydM=")).setOnCheckedChangeListener(this).setSwitchId(29));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("l97JkOHhus/jiuLfSU+I+9qU1vSL2tuK9tY=")).setOnCheckedChangeListener(this).setSwitchId(30));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("l97JkOHhufLBitb9SU+I+9qU1vSL2tuK9tY=")).setOnCheckedChangeListener(this).setSwitchId(31));
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
            case 21:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("XlVcQg=="), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("QEtH"), 32);
               memorySRWData.append(StringFog.decrypt("Q0tH"), 48);
               memorySRWData.append(StringFog.decrypt("Q0tK"), 64);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 80);
               memorySRWData.writeValue(StringFog.decrypt("U11C"), StringFog.decrypt("SlxLT1xX"));
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
               break;
            case 22:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QktGRlRcZktXWUZDW1tWFkhGQwM="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("Q0tCEA=="), 4);
               memorySRWData.append(StringFog.decrypt("Q0tCEA=="), 8);
               memorySRWData.append(StringFog.decrypt("Q0tCEA=="), 12);
               memorySRWData.append(StringFog.decrypt("Q0tDQ1Veb0NTX0dJX19aRQM="), 16);
               memorySRWData.writeValue(StringFog.decrypt("Qx1DRg=="), StringFog.decrypt("QEtH"));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 23:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("Q0tB"), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("Q0tH"), 64);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 112);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 128);
               memorySRWData.append(StringFog.decrypt("Q0tD"), 144);
               memorySRWData.append(StringFog.decrypt("Q0tB"), 160);
               memorySRWData.append(StringFog.decrypt("QktC"), 176);
               memorySRWData.append(StringFog.decrypt("Q0tA"), 192);
               memorySRWData.writeValue(StringFog.decrypt("QlFG"), StringFog.decrypt("XlM="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 24:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("Q0tH"), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("Q0tK"), 16);
               memorySRWData.append(StringFog.decrypt("QktH"), 32);
               memorySRWData.append(StringFog.decrypt("QlU="), 48);
               memorySRWData.append(StringFog.decrypt("QVU="), 64);
               memorySRWData.append(StringFog.decrypt("Qg=="), 80);
               memorySRWData.append(StringFog.decrypt("Q0tL"), 96);
               memorySRWData.writeValue(StringFog.decrypt("QFc="), StringFog.decrypt("QVU="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 25:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QQ=="), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("RVU="), 16);
               memorySRWData.append(StringFog.decrypt("QVA="), 32);
               memorySRWData.append(StringFog.decrypt("QlVC"), 48);
               memorySRWData.append(StringFog.decrypt("QlZcTlw="), 64);
               memorySRWData.append(StringFog.decrypt("QUtCRw=="), 80);
               memorySRWData.writeValue(StringFog.decrypt("Qw=="), StringFog.decrypt("QVU="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 26:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QktE"), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("QEtDQg=="), 16);
               memorySRWData.append(StringFog.decrypt("QktHQQ=="), 48);
               memorySRWData.append(StringFog.decrypt("Q0tBT1db"), 64);
               memorySRWData.append(StringFog.decrypt("Q0tA"), 80);
               memorySRWData.writeValue(StringFog.decrypt("Qw=="), StringFog.decrypt("RktF"));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 27:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("Q0tCRlJWbkFW"), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), MemorySRWData.SearchValueType.f32, 4);
               memorySRWData.append(StringFog.decrypt("QVVGQA=="), MemorySRWData.SearchValueType.f64, 8);
               memorySRWData.append(StringFog.decrypt("RktCEA=="), MemorySRWData.SearchValueType.f32, 12);
               this.setMemorySRWdata(memorySRWData);
               break;
            case 28:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("XlVcQ1Zfa0RbV0pBUVxbSlE="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("XlVcQ1Vdb0tQWkpGX1ZcS1QU"), 8);
               memorySRWData.append(StringFog.decrypt("XlVcQ1VcZkJXW0BIXl5eSlEU"), 12);
               memorySRWData.append(StringFog.decrypt("XlVcQ1VcZkJXW0BIXl5eSlEU"), 16);
               memorySRWData.append(StringFog.decrypt("XlVcQ1VcZkJXW0BIXl5eSlEU"), 20);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 24);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 44);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 56);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 60);
               memorySRWData.append(StringFog.decrypt("QlBAQ1xAbUtWXkdGW1oI"), 92);
               memorySRWData.append(StringFog.decrypt("R1RLT1decUMF"), 104);
               memorySRWData.setAddressPermission(true);
               memorySRWData.writeValue(StringFog.decrypt("QVE="), StringFog.decrypt("QlVCRlU="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 29:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QktC"), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("S1BAT0tbbkJUXkpHXAk="), 100);
               memorySRWData.append(StringFog.decrypt("QlVFQFxAakNRVkBJX1dWFQ=="), 108);
               memorySRWData.append(StringFog.decrypt("RktHRF1abkNXXEZHW11XFQ=="), 116);
               memorySRWData.setAddressPermission(true);
               memorySRWData.writeValue(StringFog.decrypt("Slc="), StringFog.decrypt("Sw=="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 30:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QVVEQkteaUBXWERFX11bFQ=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("RlVCRlVAbxU="), 4);
               memorySRWData.append(StringFog.decrypt("XlBCRlVecUMF"), 8);
               memorySRWData.append(StringFog.decrypt("XlBCRlVecUMF"), 12);
               memorySRWData.append(StringFog.decrypt("RlVCRlVAbxU="), 16);
               memorySRWData.append(StringFog.decrypt("XlRCRlVeb11TCQ=="), 24);
               memorySRWData.append(StringFog.decrypt("XlRCRlVeb11TCQ=="), 28);
               memorySRWData.append(StringFog.decrypt("QlVCRlVecUMF"), 32);
               memorySRWData.setAddressPermission(true);
               memorySRWData.writeValue(StringFog.decrypt("QV0="), StringFog.decrypt("Qw=="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 31:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QVVEQkteaUBXWERFX11bFQ=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("RlVCRlVAbxU="), 4);
               memorySRWData.append(StringFog.decrypt("XlBCRlVecUMF"), 8);
               memorySRWData.append(StringFog.decrypt("XlBCRlVecUMF"), 12);
               memorySRWData.append(StringFog.decrypt("RlVCRlVAbxU="), 16);
               memorySRWData.append(StringFog.decrypt("XlRCRlVeb11TCQ=="), 24);
               memorySRWData.append(StringFog.decrypt("Q0tCEA=="), 28);
               memorySRWData.append(StringFog.decrypt("QlVCRlVecUMF"), 32);
               memorySRWData.setAddressPermission(true);
               memorySRWData.writeValue(StringFog.decrypt("QV0="), StringFog.decrypt("XlRCRlVebw=="));
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
