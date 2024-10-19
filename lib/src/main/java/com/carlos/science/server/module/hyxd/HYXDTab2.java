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

public class HYXDTab2 extends TabContainer {
   public static final int CHECKLAYOUT_SWICH_ID_11 = 11;
   public static final int CHECKLAYOUT_SWICH_ID_12 = 12;
   public static final int CHECKLAYOUT_SWICH_ID_13 = 13;
   public static final int CHECKLAYOUT_SWICH_ID_14 = 14;
   public static final int CHECKLAYOUT_SWICH_ID_15 = 15;
   public static final int CHECKLAYOUT_SWICH_ID_16 = 16;
   public static final int CHECKLAYOUT_SWICH_ID_17 = 17;
   public static final int CHECKLAYOUT_SWICH_ID_18 = 18;
   public static final int CHECKLAYOUT_SWICH_ID_19 = 19;
   String TAG = StringFog.decrypt("PQoAGwQCCxIB");
   FloatTab floatTab;
   Button featuresMenu;

   public HYXDTab2(LayoutInflater layoutInflater, FloatBallManager floatBallManager, int tabflag) {
      super(layoutInflater, floatBallManager, tabflag);
   }

   protected int getViewId() {
      return 0;
   }

   protected void initViews(ViewGroup view) {
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("mvjEk/nUcJbk1ZXk9orc6EVSkNLfuMnBV0FFjubm")).setOnCheckedChangeListener(this).setSwitchId(11));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("mvjEk/nUcJbk1ZXk9orc6EVSn+Xht9TlV0FFjubm")).setOnCheckedChangeListener(this).setSwitchId(12));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("mvjEk/nUcJbk1ZXk9orc6EVSkd/Mt/rRV0ZFjubm")).setOnCheckedChangeListener(this).setSwitchId(13));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("mvjEk/nUcJbk1ZXk9orc6EVSn+Xht9TlV0ZFjubm")).setOnCheckedChangeListener(this).setSwitchId(14));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("mvjEk/nUcJbk1ZXk9orc6EVSn+Xht9TlV0dFjubm")).setOnCheckedChangeListener(this).setSwitchId(15));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("m/XPk/neuvruieTdjtL/U0WUyeWI1+uG4e2Z7uGL9s2U6t+LwfhOiMjSgebc")).setOnCheckedChangeListener(this).setSwitchId(16));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("m/XPk/neuvruieTdjtL/U0Wb1faH/P2G4viV2/SL9s2U6t+LwfhOiMjSgebc")).setOnCheckedChangeListener(this).setSwitchId(17));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("mv7FntvQtu79ifzYgeL+U0VSnuH0ud7GierOjsvUlujDn/zH")).setOnCheckedChangeListener(this).setSwitchId(18));
      view.addView(this.getCheckLayout().setText(StringFog.decrypt("mv7FntvQtu79ifzYgeL+U0WX2dyI1viLwN+Z9tyL/tSb78w=")).setOnCheckedChangeListener(this).setSwitchId(19));
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
            case 11:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("RktHQFdbb0dRVkNFWltaFQ=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QUtAQ1VXaEVbX0JJWFdYFQ=="), 184);
               memorySRWData.append(StringFog.decrypt("QEtFQwM="), 216);
               memorySRWData.append(StringFog.decrypt("QEtFQwM="), 264);
               memorySRWData.append(StringFog.decrypt("Q0tLT1xeb0NTXkBIXltYFQ=="), 456);
               memorySRWData.setAddressPermission(true);
               memorySRWData.writeValue(StringFog.decrypt("R1BE"), StringFog.decrypt("S1U="));
               this.setMemorySRWdata(memorySRWData);
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("RktHQFdbb0dRVkNFWltaFQ=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QEtFQ1Veb0JSVkBAUF1XFQ=="), 16);
               memorySRWData.append(StringFog.decrypt("QEtFQwM="), 216);
               memorySRWData.append(StringFog.decrypt("QEtFQwM="), 264);
               memorySRWData.append(StringFog.decrypt("Q0tLT1xeb0NTXkBIXltYFQ=="), 456);
               memorySRWData.setAddressPermission(true);
               memorySRWData.writeValue(StringFog.decrypt("R1BE"), StringFog.decrypt("S1U="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 12:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QEtFQ1VaZ0tUWkpAUVhcFQ=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QUtAQwM="), 224);
               memorySRWData.append(StringFog.decrypt("QUtCEA=="), 444);
               memorySRWData.setAddressPermission(true);
               memorySRWData.writeValue(StringFog.decrypt("R1FG"), StringFog.decrypt("S1U="));
               this.setMemorySRWdata(memorySRWData);
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("RktHQFdbb0dRVkNFWltaFQ=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QEtFQwM="), 216);
               memorySRWData.append(StringFog.decrypt("QUtCEA=="), 444);
               memorySRWData.setAddressPermission(true);
               memorySRWData.writeValue(StringFog.decrypt("R1FG"), StringFog.decrypt("S1U="));
               this.setMemorySRWdata(memorySRWData);
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QEtFQ1VaZ0tUWkpAUVhcFQ=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QEtFQ1VaZ0pXWEFDXV1XFQ=="), 200);
               memorySRWData.append(StringFog.decrypt("QUtAQwM="), 224);
               memorySRWData.append(StringFog.decrypt("QUtCEA=="), 444);
               memorySRWData.setAddressPermission(true);
               memorySRWData.writeValue(StringFog.decrypt("R1FG"), StringFog.decrypt("S1U="));
               this.setMemorySRWdata(memorySRWData);
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("RktHQFdbb0dRVkNFWltaFQ=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QEtFQwM="), 216);
               memorySRWData.append(StringFog.decrypt("QEtFQwM="), 264);
               memorySRWData.append(StringFog.decrypt("QEtKQVAI"), 196);
               memorySRWData.append(StringFog.decrypt("QUtCEA=="), 444);
               memorySRWData.writeValue(StringFog.decrypt("R1FG"), StringFog.decrypt("S1U="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 13:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("RktHQFdbb0dRVkNFWltaFQ=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QEtFQ1Veb0JVWUpJWl9fFQ=="), 16);
               memorySRWData.append(StringFog.decrypt("QUtAQ1VXaEVbX0JJWFdYFQ=="), 184);
               memorySRWData.append(StringFog.decrypt("RFJBRlxaaUBUWkRIR18I"), 188);
               memorySRWData.append(StringFog.decrypt("QEtKQVBbakdbX0JAWlxYFQ=="), 200);
               memorySRWData.append(StringFog.decrypt("QEtFQ1Veb0ZaWUJEX1tbFQ=="), 208);
               memorySRWData.append(StringFog.decrypt("QEtFQwM="), 216);
               memorySRWData.append(StringFog.decrypt("QUtCR1BYbUYF"), 252);
               memorySRWData.append(StringFog.decrypt("QEtFQwM="), 264);
               memorySRWData.append(StringFog.decrypt("QEtKQVAI"), 296);
               memorySRWData.append(StringFog.decrypt("Q0tLT1xeb0NTXkBIXltYFQ=="), 464);
               memorySRWData.writeValue(StringFog.decrypt("R1NG"), StringFog.decrypt("S1U="));
               this.setMemorySRWdata(memorySRWData);
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("RktHQFdbb0ZUXUBAXVpXFQ=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QEtFQwM="), 224);
               memorySRWData.append(StringFog.decrypt("QEtAQ1Veb0NRXEpEWFdYFQ=="), 232);
               memorySRWData.append(StringFog.decrypt("QUtCR1BYbUYF"), 260);
               memorySRWData.append(StringFog.decrypt("QEtFQwM="), 272);
               memorySRWData.append(StringFog.decrypt("QEtKQVAI"), 304);
               memorySRWData.append(StringFog.decrypt("S1VcRgM="), 452);
               memorySRWData.append(StringFog.decrypt("XlRcRgM="), 456);
               memorySRWData.append(StringFog.decrypt("Q0tLT1xeb0NTXkBIXltYFQ=="), 464);
               memorySRWData.writeValue(StringFog.decrypt("R1NG"), StringFog.decrypt("S1U="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 14:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QEtFQ1Veb0JaX0VDXVdYFQ=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("RktHQFdbb0dRVkNFWltaFQ=="), 8);
               memorySRWData.append(StringFog.decrypt("R0tLRlNWbEtXXUVCWVlaFk5ARwM="), 16);
               memorySRWData.append(StringFog.decrypt("QEtFQwM="), 224);
               memorySRWData.append(StringFog.decrypt("QUtCR1BYbUYF"), 260);
               memorySRWData.append(StringFog.decrypt("QEtFQwM="), 272);
               memorySRWData.append(StringFog.decrypt("QEtKQVAI"), 304);
               memorySRWData.append(StringFog.decrypt("QUtCEA=="), 460);
               memorySRWData.append(StringFog.decrypt("XlRcRgM="), 464);
               memorySRWData.append(StringFog.decrypt("Q0tLT1xeb0NTXkBIXltYFQ=="), 472);
               memorySRWData.writeValue(StringFog.decrypt("R1NC"), StringFog.decrypt("S1U="));
               this.setMemorySRWdata(memorySRWData);
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QlRAQFNAaEYF"), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QVFHTlRAahU="), 16);
               memorySRWData.append(StringFog.decrypt("QUtCEA=="), 36);
               memorySRWData.append(StringFog.decrypt("QEtKQVBeaUVaVkdGW19ZFQ=="), 152);
               memorySRWData.append(StringFog.decrypt("QEtKQVBeaURXWEBEXFhXFQ=="), 456);
               memorySRWData.append(StringFog.decrypt("QEtFQ1Veb0dRVkNFWltaFQ=="), 632);
               memorySRWData.writeValue(StringFog.decrypt("QFM="), StringFog.decrypt("S1U="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 15:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("RF1DQF1aaEVbWERIR18I"), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QEtFQwM="), 236);
               memorySRWData.append(StringFog.decrypt("QUtCEA=="), 432);
               memorySRWData.append(StringFog.decrypt("XlRcRgNO"), 436);
               memorySRWData.append(StringFog.decrypt("Q0tLT1xeb0NTXkBIXltYFUU="), 444);
               memorySRWData.writeValue(StringFog.decrypt("Qx1DFFU="), StringFog.decrypt("S1U="));
               this.setMemorySRWdata(memorySRWData);
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QlRAQFNAaEYF"), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QVFHTlRAahU="), 16);
               memorySRWData.append(StringFog.decrypt("QUtCEEU="), 36);
               memorySRWData.append(StringFog.decrypt("QEtKQVBeaURXWEBEXFhXFUU="), 456);
               memorySRWData.append(StringFog.decrypt("QEtFQ1Veb0dRVkNFWltaFUU="), 632);
               memorySRWData.append(StringFog.decrypt("QEtFQ1Veb0dRVkNFWltaFUU="), 760);
               memorySRWData.append(StringFog.decrypt("QUtAQ1Veb0NUXkdCXFpZFUU="), 968);
               memorySRWData.writeValue(StringFog.decrypt("Qx1AQg=="), StringFog.decrypt("S1U="));
               this.setMemorySRWdata(memorySRWData);
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("R0tCEA=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QlRAQFNAaEYF"), 12);
               memorySRWData.append(StringFog.decrypt("QVFHTlRAahU="), 28);
               memorySRWData.append(StringFog.decrypt("QUtCEA=="), 48);
               memorySRWData.writeValue(StringFog.decrypt("Qx1BRg=="), StringFog.decrypt("S1U="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 16:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("Q0tHEA=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("Q0tFQwM="), 4);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 8);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 12);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 16);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 20);
               memorySRWData.append(StringFog.decrypt("Q0tHEA=="), 24);
               memorySRWData.append(StringFog.decrypt("QktARlVeb0NTW0VGUVxZFQ=="), 52);
               memorySRWData.writeValue(StringFog.decrypt("Qlc="), StringFog.decrypt("RldC"));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 17:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QktCEA=="), MemorySRWData.SearchValueType.f32);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 4);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 8);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 12);
               memorySRWData.append(StringFog.decrypt("QktCEA=="), 16);
               memorySRWData.append(StringFog.decrypt("Q0tKQ1dabERXVkRBUVpdFQ=="), 20);
               memorySRWData.append(StringFog.decrypt("QktBT1xXZkpaWERBXFdfFQ=="), 24);
               memorySRWData.append(StringFog.decrypt("QktDQVxXZkpaW0VFXVhXFQ=="), 52);
               memorySRWData.append(StringFog.decrypt("QVVcRgM="), 80);
               memorySRWData.writeValue(StringFog.decrypt("QlM="), StringFog.decrypt("S10="));
               memorySRWData.writeValue(StringFog.decrypt("QVU="), StringFog.decrypt("S10="));
               memorySRWData.writeValue(StringFog.decrypt("QVE="), StringFog.decrypt("S10="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 18:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QFY="), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("Ql0="), 48);
               memorySRWData.append(StringFog.decrypt("QlA="), 64);
               memorySRWData.append(StringFog.decrypt("QVdC"), 80);
               memorySRWData.append(StringFog.decrypt("QFU="), 96);
               memorySRWData.writeValue(StringFog.decrypt("SlNS"), StringFog.decrypt("QldCRg=="));
               this.setMemorySRWdata(memorySRWData);
               break;
            case 19:
               memorySRWData = MemorySRWData.AddMemorySearch(StringFog.decrypt("QQ=="), MemorySRWData.SearchValueType.f64);
               memorySRWData.append(StringFog.decrypt("Rg=="), 16);
               memorySRWData.append(StringFog.decrypt("QVU="), 32);
               memorySRWData.append(StringFog.decrypt("QlVC"), 48);
               memorySRWData.append(StringFog.decrypt("QlU="), 64);
               memorySRWData.writeValue(StringFog.decrypt("R1E="), StringFog.decrypt("QldCRg=="));
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
