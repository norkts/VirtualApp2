package com.carlos.science.server.module.mrfz;

import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import com.carlos.libcommon.StringFog;
import com.carlos.science.FloatBallManager;
import com.carlos.science.tab.FloatTab;
import com.carlos.science.tab.TabContainer;
import com.kook.common.utils.HVLog;
import com.kook.controller.client.mrfz.IMRFZController;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MRFZTab extends TabContainer {
   String TAG = StringFog.decrypt("Pjc0LDEPPQ==");
   public static final int VIEW_ACTION_WD = 1;
   public static final int VIEW_ACTION_SX = 2;
   public static final int VIEW_ACTION_XDL = 3;
   public static final int VIEW_ACTION_GJFW = 4;
   public static final int VIEW_ACTION_SKILL = 5;
   public static final int VIEW_ACTION_TCTG = 6;
   public static final int VIEW_ACTION_SZJS = 7;
   public static final int VIEW_ACTION_ATK = 8;
   public static final int VIEW_ACTION_ATKSEEK = 9;
   public static final int VIEW_ACTION_ATKTEXT = 10;
   public static final int VIEW_ACTION_YXJS = 11;
   public static final int VIEW_ACTION_YXJSSEEK = 12;
   public static final int VIEW_ACTION_YXJSTEXT = 13;
   private Switch wd;
   private Switch sx;
   private Switch xdl;
   private Switch gjfw;
   private Switch skill;
   private Switch tctg;
   private Switch szjs;
   private Switch atk;
   private SeekBar atkSeek;
   private TextView atkText;
   private Switch yxjs;
   private SeekBar yxjsSeek;
   private TextView yxjsText;
   FloatTab floatTab;

   public MRFZTab(LayoutInflater layoutInflater, FloatBallManager floatBallManager, int tabflag) {
      super(layoutInflater, floatBallManager, tabflag);
   }

   protected int getViewId() {
      return layout.mrfz_tab;
   }

   protected void findViews(View root) {
      this.sx = (Switch)root.findViewById(id.switchsx);
      this.gjfw = (Switch)root.findViewById(id.switchgjfw);
      this.szjs = (Switch)root.findViewById(id.switchszjs);
      this.atk = (Switch)root.findViewById(id.switchatk);
      this.atkSeek = (SeekBar)root.findViewById(id.atkSeekBar);
      this.atkText = (TextView)root.findViewById(id.atkValue);
      this.yxjs = (Switch)root.findViewById(id.switchyxjs);
      this.yxjsSeek = (SeekBar)root.findViewById(id.yxjsSeekBar);
      this.yxjsText = (TextView)root.findViewById(id.yxjsValue);
      this.tctg = (Switch)root.findViewById(id.switchtctg);
      this.wd = (Switch)root.findViewById(id.switchwd);
      this.xdl = (Switch)root.findViewById(id.switchxdl);
      this.skill = (Switch)root.findViewById(id.switchskill);
      this.atkSeek.setMax(9999);
      this.atkSeek.setProgress(680);
      this.atkText.setText(StringFog.decrypt("QktF"));
      this.yxjsSeek.setMax(9999);
      this.yxjsSeek.setProgress(1000);
      this.yxjsText.setText(StringFog.decrypt("RktC"));
      this.wd.setChecked(false);
      this.sx.setChecked(false);
      this.xdl.setChecked(false);
      this.skill.setChecked(false);
      this.tctg.setChecked(false);
      this.gjfw.setChecked(false);
      this.szjs.setChecked(false);
      this.atk.setChecked(false);
      this.atkSeek.setProgress(680);
      this.atkText.setText(StringFog.decrypt("QktF"));
      this.yxjs.setChecked(false);
      this.yxjsSeek.setProgress(1000);
      this.yxjsText.setText(StringFog.decrypt("RktC"));
      this.wd.setOnCheckedChangeListener((buttonView, isChecked) -> {
         IMRFZController mrfzController = this.getMRFZController();

         try {
            mrfzController.switchChange(1, isChecked);
         } catch (RemoteException var5) {
            RemoteException e = var5;
            e.printStackTrace();
         }

      });
      this.sx.setOnCheckedChangeListener((buttonView, isChecked) -> {
         IMRFZController mrfzController = this.getMRFZController();

         try {
            mrfzController.switchChange(2, isChecked);
         } catch (RemoteException var5) {
            RemoteException e = var5;
            e.printStackTrace();
         }

      });
      this.szjs.setOnCheckedChangeListener((buttonView, isChecked) -> {
         IMRFZController mrfzController = this.getMRFZController();

         try {
            mrfzController.switchChange(7, isChecked);
         } catch (RemoteException var5) {
            RemoteException e = var5;
            e.printStackTrace();
         }

      });
      this.xdl.setOnCheckedChangeListener((buttonView, isChecked) -> {
         IMRFZController mrfzController = this.getMRFZController();

         try {
            mrfzController.switchChange(3, isChecked);
         } catch (RemoteException var5) {
            RemoteException e = var5;
            e.printStackTrace();
         }

      });
      this.skill.setOnCheckedChangeListener((buttonView, isChecked) -> {
         IMRFZController mrfzController = this.getMRFZController();

         try {
            mrfzController.switchChange(5, isChecked);
         } catch (RemoteException var5) {
            RemoteException e = var5;
            e.printStackTrace();
         }

      });
      this.tctg.setOnCheckedChangeListener((buttonView, isChecked) -> {
         IMRFZController mrfzController = this.getMRFZController();

         try {
            mrfzController.switchChange(6, isChecked);
         } catch (RemoteException var5) {
            RemoteException e = var5;
            e.printStackTrace();
         }

      });
      this.gjfw.setOnCheckedChangeListener((buttonView, isChecked) -> {
         IMRFZController mrfzController = this.getMRFZController();

         try {
            mrfzController.switchChange(4, isChecked);
         } catch (RemoteException var5) {
            RemoteException e = var5;
            e.printStackTrace();
         }

      });
      this.atk.setOnCheckedChangeListener((buttonView, isChecked) -> {
         IMRFZController mrfzController = this.getMRFZController();

         try {
            mrfzController.switchChange(8, isChecked);
         } catch (RemoteException var5) {
            RemoteException e = var5;
            e.printStackTrace();
         }

      });
      this.atkSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            MRFZTab.this.atkText.setText(MRFZTab.this.floatFromat(MRFZTab.this.getAtkByProgress(progress), 2));
         }

         public void onStartTrackingTouch(SeekBar seekBar) {
         }

         public void onStopTrackingTouch(SeekBar seekBar) {
            IMRFZController mrfzController = MRFZTab.this.getMRFZController();

            try {
               mrfzController.setViewValue(9, (float)MRFZTab.this.getAtkByProgress(seekBar.getProgress()));
            } catch (RemoteException var4) {
               RemoteException e = var4;
               e.printStackTrace();
            }

         }
      });
      this.yxjs.setOnCheckedChangeListener((buttonView, isChecked) -> {
         IMRFZController mrfzController = this.getMRFZController();

         try {
            mrfzController.switchChange(11, isChecked);
         } catch (RemoteException var5) {
            RemoteException e = var5;
            e.printStackTrace();
         }

      });
      this.yxjsSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            MRFZTab.this.yxjsText.setText(MRFZTab.this.floatFromat(MRFZTab.this.getGameSpeedByProgress(progress), 2));
         }

         public void onStartTrackingTouch(SeekBar seekBar) {
         }

         public void onStopTrackingTouch(SeekBar seekBar) {
            IMRFZController mrfzController = MRFZTab.this.getMRFZController();

            try {
               mrfzController.setViewValue(12, (float)MRFZTab.this.getGameSpeedByProgress(seekBar.getProgress()));
            } catch (RemoteException var4) {
               RemoteException e = var4;
               e.printStackTrace();
            }

         }
      });
   }

   IMRFZController getMRFZController() {
      IBinder binder = this.floatTab.getClientBinder();
      HVLog.i(this.TAG, StringFog.decrypt("UwIXAig8GSkgABwEGwACHwAAVgcHMRcGHUg=") + binder + StringFog.decrypt("U0VSVgwdHRoNCxcCKAMHBQBITA==") + binder.isBinderAlive());
      return IMRFZController.Stub.asInterface(binder);
   }

   public String floatFromat(Double value, int newScale) {
      return (new BigDecimal(value)).setScale(newScale, RoundingMode.HALF_UP).doubleValue() + "";
   }

   private double getAtkByProgress(int progress) {
      return progress > 4000 ? 549.34469698445 - 0.254726957076848 * (double)progress + 2.99726957076841E-5 * (double)progress * (double)progress : (double)progress * 0.0025;
   }

   private double getGameSpeedByProgress(int progress) {
      return progress > 4000 ? 8.340975058733853E-7 * (double)progress * (double)progress + 0.001659024941266144 * (double)progress + 0.018340140961259594 : (double)progress * 0.005;
   }

   public Object getTabContainerData() {
      return null;
   }

   public void setFloatTab(FloatTab tabLayout) {
      this.floatTab = tabLayout;
   }
}
