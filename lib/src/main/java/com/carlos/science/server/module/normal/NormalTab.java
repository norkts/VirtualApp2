package com.carlos.science.server.module.normal;

import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import com.carlos.libcommon.StringFog;
import com.carlos.science.FloatBallManager;
import com.carlos.science.tab.FloatTab;
import com.carlos.science.tab.TabContainer;
import com.kook.common.utils.HVLog;
import com.kook.controller.client.normal.ILearnController;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;

public class NormalTab extends TabContainer {
   String TAG = StringFog.decrypt("PQoAGwQCCxIB");
   FloatTab floatTab;
   Button featuresMenu;
   ILearnController iLearnController;

   public NormalTab(LayoutInflater layoutInflater, FloatBallManager floatBallManager, int tabflag) {
      super(layoutInflater, floatBallManager, tabflag);
   }

   protected int getViewId() {
      return layout.normal_tab;
   }

   protected void findViews(View root) {
      Switch viewById = (Switch)this.findViewById(id.switch_menu);
      viewById.setOnCheckedChangeListener((compoundButton, isChecked) -> {
         if (isChecked) {
            this.floatBallManager.hide();
         }

      });
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

   public void onCurrentPageSelected(FloatTab floatTab) {
   }

   public void onClick(View view) {
      HVLog.d(this.TAG, StringFog.decrypt("UwsdBAgPMycCDVIfBywCGgYZVg==") + (view == this.floatTab.getFeaturesMenu()));
      IBinder clientBinder = this.floatTab.getClientBinder();
      if (clientBinder != null) {
         if (this.iLearnController == null) {
            this.iLearnController = ILearnController.Stub.asInterface(clientBinder);
         }

         try {
            this.iLearnController.debugLearn();
         } catch (RemoteException var4) {
            RemoteException e = var4;
            HVLog.printException(e);
         }

         if (view == this.floatTab.getFeaturesMenu()) {
         }

      }
   }
}
