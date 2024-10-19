package com.carlos.science;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.WindowManager;
import com.carlos.common.utils.SPTools;
import com.carlos.libcommon.StringFog;
import com.carlos.science.floatball.FloatBall;
import com.carlos.science.floatball.FloatBallCfg;
import com.carlos.science.floatball.StatusBarView;
import com.carlos.science.menu.FloatMenu;
import com.carlos.science.menu.FloatMenuCfg;
import com.carlos.science.menu.MenuItem;
import com.carlos.science.server.ServerController;
import com.carlos.science.tab.FloatTab;
import com.carlos.science.tab.TabChild;
import com.kook.common.utils.HVLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FloatBallManager {
   String TAG;
   public int mScreenWidth;
   public int mScreenHeight;
   private OnFloatBallClickListener mFloatballClickListener;
   private WindowManager mWindowManager;
   private Context mContext;
   private FloatBall floatBall;
   private FloatMenu floatMenu;
   private FloatTab floatTab;
   private StatusBarView statusBarView;
   public int floatballX;
   public int floatballY;
   private boolean isShowing;
   private List<MenuItem> menuItems;
   private List<TabChild> tabChilds;
   LayoutInflater mLayoutInflater;

   public FloatBallManager(Context application, ServerController serverController, FloatBallCfg ballCfg) {
      this(application, serverController, ballCfg, (FloatMenuCfg)null);
   }

   public FloatBallManager(Context application, ServerController serverController, FloatBallCfg ballCfg, FloatMenuCfg menuCfg) {
      this.TAG = StringFog.decrypt("NQkdFxEsPh8PIhMeCAgLAQ==");
      this.isShowing = false;
      this.menuItems = new ArrayList();
      this.tabChilds = new ArrayList();
      this.mLayoutInflater = LayoutInflater.from(application);
      this.mContext = application.getApplicationContext();
      FloatBallUtil.inSingleActivity = false;
      this.mWindowManager = (WindowManager)this.mContext.getSystemService(StringFog.decrypt("BAwcEgoZ"));
      this.computeScreenSize();
      this.floatBall = new FloatBall(this.mContext, this, ballCfg);
      this.floatMenu = new FloatMenu(this.mContext, this, menuCfg);
      this.floatTab = new FloatTab(this, this.mContext, this.mLayoutInflater, serverController, false);
      this.statusBarView = new StatusBarView(this.mContext, this);
   }

   public void buildMenu() {
      this.inflateMenuItem();
   }

   public void buildTabChild() {
      this.inflateTabChild();
   }

   public Context getContext() {
      return this.mContext;
   }

   public FloatBallManager addMenuItem(MenuItem item) {
      this.menuItems.add(item);
      return this;
   }

   public WindowManager getWindowManager() {
      return this.mWindowManager;
   }

   public FloatBallManager addTabChild(TabChild tabChild) {
      this.tabChilds.add(tabChild);
      return this;
   }

   public FloatBallManager addTabChilds(List<TabChild> tabChilds) {
      this.tabChilds.clear();
      this.tabChilds.addAll(tabChilds);
      return this;
   }

   public int getMenuItemSize() {
      return this.menuItems != null ? this.menuItems.size() : 0;
   }

   public FloatBallManager setMenu(List<MenuItem> items) {
      this.menuItems = items;
      return this;
   }

   private void inflateMenuItem() {
      this.floatMenu.removeAllItemViews();
      Iterator var1 = this.menuItems.iterator();

      while(var1.hasNext()) {
         MenuItem item = (MenuItem)var1.next();
         this.floatMenu.addItem(item);
      }

   }

   private void inflateTabChild() {
      this.floatTab.removeAllItemViews();
      this.floatTab.setTabData(this.tabChilds);
   }

   public int getBallSize() {
      return this.floatBall.getSize();
   }

   public FloatBall getFloatBall() {
      return this.floatBall;
   }

   public FloatTab getFloatTab() {
      return this.floatTab;
   }

   public void computeScreenSize() {
      if (VERSION.SDK_INT >= 13) {
         Point point = new Point();
         this.mWindowManager.getDefaultDisplay().getSize(point);
         this.mScreenWidth = point.x;
         this.mScreenHeight = point.y;
      } else {
         this.mScreenWidth = this.mWindowManager.getDefaultDisplay().getWidth();
         this.mScreenHeight = this.mWindowManager.getDefaultDisplay().getHeight();
      }

   }

   public int getStatusBarHeight() {
      return this.statusBarView.getStatusBarHeight();
   }

   public void onStatusBarHeightChange() {
      this.floatBall.onLayoutChange();
   }

   public void show() {
      HVLog.d(this.TAG, StringFog.decrypt("AA0dAUUHLCALAAUZBwhOSUU=") + this.isShowing);
      SPTools.getLong(this.getContext(), StringFog.decrypt("BQwCAgwDOg=="));
      if (!this.isShowing) {
         this.isShowing = true;
         this.floatBall.attachToWindow(this.mWindowManager);
         this.floatBall.setVisibility(0);
         this.statusBarView.attachToWindow(this.mWindowManager);
         this.floatTab.detachFromWindow(this.mWindowManager);
      }
   }

   public boolean isShowing() {
      return this.isShowing;
   }

   public void reset() {
      HVLog.d(this.TAG, StringFog.decrypt("AQABExE="));
      this.floatBall.setVisibility(0);
      this.floatBall.postSleepRunnable();
      this.floatBall.attachToWindow(this.mWindowManager);
      this.floatMenu.detachFromWindow(this.mWindowManager);
      this.floatTab.setVisibility(0);
      this.floatTab.detachFromWindow(this.mWindowManager);
   }

   public void onFloatBallClick() {
      if (this.menuItems != null && this.menuItems.size() > 0 || this.tabChilds != null && this.tabChilds.size() > 0) {
         HVLog.d(this.TAG, StringFog.decrypt("NQkdFxEsPh8PIhMeCAgLAUUdGCMCMBIXLRMcBSwCGgYZVgQaKxIAByYfPgYAFwoF"));
         if (!this.floatTab.isAdded()) {
            this.floatTab.attachToWindow(this.mWindowManager);
            this.floatBall.detachFromWindow(this.mWindowManager);
         } else {
            this.floatTab.detachFromWindow(this.mWindowManager);
            this.floatBall.attachToWindow(this.mWindowManager);
         }
      } else {
         HVLog.d(this.TAG, StringFog.decrypt("NQkdFxEsPh8PIhMeCAgLAUUdGCMCMBIXLRMcBSwCGgYZ"));
         if (this.mFloatballClickListener != null) {
            this.mFloatballClickListener.onFloatBallClick();
         }
      }

   }

   public void refreshFloatTab() {
      if (!this.floatTab.isAdded()) {
         this.floatTab.attachToWindow(this.mWindowManager);
      }

      this.floatTab.refreshToWindowsw(this.mWindowManager);
   }

   public void hide() {
      HVLog.d(this.TAG, StringFog.decrypt("GwwWE0VONgAwBx0HAAEJU1g=") + this.isShowing);
      if (this.isShowing) {
         this.isShowing = false;
         this.floatBall.detachFromWindow(this.mWindowManager);
         this.floatMenu.detachFromWindow(this.mWindowManager);
         this.floatTab.detachFromWindow(this.mWindowManager);
         this.statusBarView.detachFromWindow(this.mWindowManager);
      }
   }

   public void onConfigurationChanged(Configuration newConfig) {
      this.computeScreenSize();
      this.reset();
   }

   public void setOnFloatBallClickListener(OnFloatBallClickListener listener) {
      this.mFloatballClickListener = listener;
   }

   public interface OnFloatBallClickListener {
      void onFloatBallClick();
   }
}
