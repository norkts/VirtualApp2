package com.carlos.science.tab;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.carlos.common.widget.tablayout.SegmentTabLayout;
import com.carlos.common.widget.tablayout.listener.OnTabSelectListener;
import com.carlos.libcommon.StringFog;
import com.carlos.science.FloatBallManager;
import com.carlos.science.FloatBallUtil;
import com.carlos.science.server.ServerController;
import com.kook.common.utils.HVLog;
import com.kook.librelease.R.color;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FloatTab extends FrameLayout implements View.OnClickListener {
   String TAG = "FloatTab";
   private SegmentTabLayout tabTitle;
   private ViewPager tabContext;
   Button featuresMenu;
   private ArrayList<TabContainer> containerArrayList = new ArrayList();
   private int mChildPadding = 5;
   public static final int LEFT_TOP = 1;
   public static final int CENTER_TOP = 2;
   public static final int RIGHT_TOP = 3;
   public static final int LEFT_CENTER = 4;
   public static final int CENTER = 5;
   public static final int RIGHT_CENTER = 6;
   public static final int LEFT_BOTTOM = 7;
   public static final int CENTER_BOTTOM = 8;
   public static final int RIGHT_BOTTOM = 9;
   IBinder iBinder;
   String packageName;
   ServerController serverController;
   private FloatBallManager floatBallManager;
   private WindowManager.LayoutParams mLayoutParams;
   private boolean isAdded = false;
   private int mDuration = 250;
   private int mPosition;
   private int mSize;
   private int mBallSize;
   private boolean mListenBackEvent = true;
   Handler mHandler = new Handler();
   PagerAdapter pagerAdapter = new PagerAdapter() {
      public boolean isViewFromObject(View arg0, Object arg1) {
         return arg0 == arg1;
      }

      public int getCount() {
         return FloatTab.this.containerArrayList.size();
      }

      public void destroyItem(ViewGroup container, int position, Object object) {
         container.removeView(((TabContainer)FloatTab.this.containerArrayList.get(position)).getRootView());
      }

      public int getItemPosition(Object object) {
         return super.getItemPosition(object);
      }

      public CharSequence getPageTitle(int position) {
         return "";
      }

      public Object instantiateItem(ViewGroup container, int position) {
         TabContainer tabContainer = (TabContainer)FloatTab.this.containerArrayList.get(position);
         View rootView = tabContainer.getRootView();
         tabContainer.setFloatTab(FloatTab.this);
         tabContainer.setHandler(FloatTab.this.mHandler);
         container.addView(rootView);
         return rootView;
      }
   };

   public FloatTab(FloatBallManager floatBallManager, Context context, LayoutInflater layoutInflater, ServerController serverController, boolean hideTabView) {
      super(context);
      this.serverController = serverController;
      this.initLayoutParams(context);
      if (hideTabView) {
         layoutInflater.inflate(layout.float_window_single, this, true);
      } else {
         layoutInflater.inflate(layout.float_windows_tab, this, true);
      }

      this.floatBallManager = floatBallManager;
      Log.d(this.TAG, "floattab floatBallManager is " + floatBallManager);
      this.mLayoutParams.height = floatBallManager.mScreenHeight * 2 / 6;
      this.mLayoutParams.width = floatBallManager.mScreenWidth * 2 / 3;
      int halfSize = (int)(Math.sqrt(Math.pow((double)this.mLayoutParams.height, 2.0) + Math.pow((double)this.mLayoutParams.width, 2.0)) / 2.0);
      this.mSize = halfSize * 2;
      this.mBallSize = floatBallManager.getBallSize();
      this.tabContext = (ViewPager)this.findViewById(id.float_tabContext);
      this.featuresMenu = (Button)this.findViewById(id.features_menu);
      this.featuresMenu.setOnClickListener(this);
      this.tabTitle = (SegmentTabLayout)this.findViewById(id.float_tabTitle);
      this.tabTitle.setTextSelectColor(ContextCompat.getColor(context, color.colorAccent));
      this.tabTitle.setTextUnselectColor(ContextCompat.getColor(context, color.light_gray));
      this.tabTitle.setOnTabSelectListener(new OnTabSelectListener() {
         public void onTabSelect(int position) {
            FloatTab.this.tabContext.setCurrentItem(position);
         }

         public void onTabReselect(int position) {
         }
      });
      this.tabContext.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
         public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
         }

         public void onPageSelected(int position) {
            FloatTab.this.tabTitle.setCurrentTab(position);
            int currentItem = FloatTab.this.tabContext.getCurrentItem();
            TabContainer tabContainer = (TabContainer)FloatTab.this.containerArrayList.get(currentItem);
            tabContainer.onCurrentPageSelected(FloatTab.this);
         }

         public void onPageScrollStateChanged(int state) {
         }
      });
      ViewCompat.setElevation(this.tabTitle, 10.0F);
   }

   protected void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      int height = this.getMeasuredHeight();
      int width = this.getMeasuredWidth();
      this.onLocation(height, width);
   }

   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
   }

   private void initLayoutParams(Context context) {
      this.mLayoutParams = FloatBallUtil.getLayoutParams(context, this.mListenBackEvent);
   }

   public void setOnTabSelectListener(OnTabSelectListener onTabSelectListener) {
      this.tabTitle.setOnTabSelectListener(onTabSelectListener);
   }

   public void removeAllItemViews() {
      this.containerArrayList.clear();
   }

   public boolean onTouchEvent(MotionEvent event) {
      int action = event.getAction();
      int ballSize = this.floatBallManager.getBallSize();
      int width = this.floatBallManager.getFloatBall().getWidth();
      int height = this.floatBallManager.getFloatBall().getHeight();
      int x = (int)event.getRawX();
      int y = (int)event.getRawY();
      switch (action) {
         case 1:
         case 3:
         case 4:
            boolean isRectX = this.floatBallManager.floatballX < x && x < this.floatBallManager.floatballX + width;
            boolean var10000;
            if (this.floatBallManager.floatballY < y && y < this.floatBallManager.floatballY + height + this.floatBallManager.getStatusBarHeight()) {
               var10000 = true;
            } else {
               var10000 = false;
            }

            if (!isRectX || !isRectX) {
               this.toggle(this.mDuration);
            }
         case 0:
         case 2:
         default:
            return super.onTouchEvent(event);
      }
   }

   public void onLocation(int width, int height) {
      if (width != 0 || height != 0) {
         WindowManager windowManager = this.floatBallManager.getWindowManager();
         int mScreenWidth = this.floatBallManager.getWindowManager().getDefaultDisplay().getWidth();
         int mScreenHeight = this.floatBallManager.getWindowManager().getDefaultDisplay().getHeight();
         HVLog.d("floatMenu width:" + width + "   height:" + height + "   mScreenWidth:" + mScreenWidth + "  mScreenHeight:" + mScreenHeight + "    windowManager:" + (windowManager != null));
         this.mLayoutParams.x = 0;
         this.mLayoutParams.y = mScreenHeight / 2 - height / 2;
         HVLog.d("mLayoutParams " + this.mLayoutParams.x + "   " + this.mLayoutParams.y);
         if (windowManager != null) {
            windowManager.updateViewLayout(this, this.mLayoutParams);
         }

      }
   }

   public void attachToWindow(WindowManager windowManager) {
      if (!this.isAdded) {
         int mScreenWidth = this.floatBallManager.getWindowManager().getDefaultDisplay().getWidth();
         int mScreenHeight = this.floatBallManager.getWindowManager().getDefaultDisplay().getHeight();
         boolean IS_HORIZONTAL = true;
         if (mScreenHeight > mScreenWidth) {
            IS_HORIZONTAL = true;
         } else {
            IS_HORIZONTAL = false;
         }

         int width = this.getMeasuredWidth();
         int height = this.getMeasuredHeight();
         this.mLayoutParams.x = 0;
         if (IS_HORIZONTAL) {
            this.mLayoutParams.height = mScreenHeight * 2 / 6;
            this.mLayoutParams.y = mScreenHeight / 2 - this.mLayoutParams.height / 2;
            this.mLayoutParams.width = mScreenWidth * 2 / 3;
         } else {
            this.mLayoutParams.height = mScreenHeight * 2 / 3;
            this.mLayoutParams.width = mScreenWidth * 1 / 3;
            this.mLayoutParams.y = mScreenHeight / 2 - this.mLayoutParams.height / 2;
         }

         this.toggle(this.mDuration);
         HVLog.d("mLayoutParams " + this.mLayoutParams.x + "   " + this.mLayoutParams.y + "横竖屏：" + IS_HORIZONTAL + "   mDuration:" + this.mDuration);
         windowManager.addView(this, this.mLayoutParams);
         this.isAdded = true;
      }

   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      int currentItem = this.tabContext.getCurrentItem();
      TabContainer tabContainer = (TabContainer)this.containerArrayList.get(currentItem);
      if (tabContainer != null) {
         tabContainer.onAttachedToWindow(this);
      }

      Log.d(this.TAG, "floatTab onAttachedToWindow " + currentItem);
   }

   public void refreshToWindowsw(WindowManager windowManager) {
      Log.d(this.TAG, " refreshToWindowsw");
      windowManager.updateViewLayout(this, this.mLayoutParams);
   }

   private void toggle(int duration) {
      if (duration > 0) {
         if (this.isAdded) {
            this.floatBallManager.reset();
         }

      }
   }

   public void detachFromWindow(WindowManager windowManager) {
      HVLog.d(this.TAG, "detachFromWindow isAdded:" + this.isAdded);
      if (this.isAdded) {
         this.toggle(0);
         this.setVisibility(8);
         if (this.getContext() instanceof Activity) {
            windowManager.removeViewImmediate(this);
         } else {
            windowManager.removeView(this);
         }

         this.isAdded = false;
      }

   }

   public boolean isAdded() {
      return this.isAdded;
   }

   public void computeLayoutXY(WindowManager.LayoutParams layoutParams, int position) {
      int width = layoutParams.width;
      int height = layoutParams.height;
      int ballCenterX = this.floatBallManager.floatballX;
      int ballCenterY = this.floatBallManager.floatballY;
      int ballSize = this.floatBallManager.getBallSize();
      int layoutX = 0;
      int layoutY = 0;
      switch (position) {
         case 1:
            layoutX = ballCenterX + ballSize + this.getPadding();
            layoutY = ballCenterY + ballSize + this.getPadding();
            break;
         case 2:
            layoutX = ballCenterX + ballSize / 2 - width / 2;
            layoutY = ballCenterY - height - this.getPadding();
            break;
         case 3:
            layoutX = ballCenterX - width - this.getPadding();
            layoutY = ballCenterY + ballSize - this.getPadding();
            break;
         case 4:
            layoutX = ballCenterX + ballSize + this.getPadding();
            layoutY = ballCenterY - height / 2;
            break;
         case 5:
            layoutX = width / 2;
            layoutY = width / 2;
            break;
         case 6:
            layoutX = ballCenterX - width - this.getPadding();
            layoutY = ballCenterY - height / 2;
            break;
         case 7:
            layoutX = ballCenterX + ballSize + this.getPadding();
            layoutY = ballCenterY - height - this.getPadding();
            break;
         case 8:
            layoutX = ballCenterX + ballSize / 2 - width / 2;
            layoutY = ballCenterY + ballSize + this.getPadding();
            break;
         case 9:
            layoutX = ballCenterX - width - this.getPadding();
            layoutY = ballCenterY - height - this.getPadding();
      }

      layoutParams.x = layoutX;
      layoutParams.y = layoutY;
      Log.d(this.TAG, "layoutX:" + layoutX + "    layoutY:" + layoutY + "    ballCenterX:" + ballCenterX + "    ballCenterY:" + ballCenterY);
   }

   private int getPadding() {
      return this.mChildPadding * 2;
   }

   public void setTabData(List<TabChild> tabChildren) {
      if (this.containerArrayList.size() > 0) {
         this.containerArrayList.clear();
      }

      String[] titles = new String[tabChildren.size()];
      int size = tabChildren.size();

      for(int i = 0; i < size; ++i) {
         TabChild tabChild = (TabChild)tabChildren.get(i);
         titles[i] = tabChild.getTitle();
         TabContainer tabContainer = tabChild.getTabContainer();
         tabContainer.initContainer();
         this.containerArrayList.add(tabContainer);
         HVLog.d(this.TAG, "floatTab 显示的tabContainer：" + tabContainer);
      }

      this.tabTitle.setTabData(titles);
      if (this.tabContext.getAdapter() == null) {
         this.tabContext.setAdapter(this.pagerAdapter);
      } else {
         this.pagerAdapter.notifyDataSetChanged();
      }

   }

   public int getTabWidth() {
      return this.mLayoutParams.width;
   }

   public int getTabHeight() {
      return this.mLayoutParams.height;
   }

   public TabContainer getTabContainerByPosition(int position) {
      TabContainer tabContainer = (TabContainer)this.containerArrayList.get(position);
      return tabContainer;
   }

   public int computeMenuLayout(WindowManager.LayoutParams layoutParams) {
      int position = 6;
      int halfBallSize = this.mBallSize / 2;
      int screenWidth = this.floatBallManager.mScreenWidth;
      int screenHeight = this.floatBallManager.mScreenHeight;
      int floatballCenterY = this.floatBallManager.floatballY + halfBallSize;
      int wmX = this.floatBallManager.floatballX;
      int wmY = floatballCenterY;
      if (wmX <= screenWidth / 3) {
         wmX = 0;
         if (wmY <= this.mSize / 2) {
            position = 1;
            wmY = floatballCenterY - halfBallSize;
         } else if (wmY > screenHeight - this.mSize / 2) {
            position = 7;
            wmY = floatballCenterY - this.mSize + halfBallSize;
         } else {
            position = 4;
            wmY = floatballCenterY - this.mSize / 2;
         }
      } else if (wmX >= screenWidth * 2 / 3) {
         wmX = screenWidth - this.mSize;
         if (wmY <= this.mSize / 2) {
            position = 3;
            wmY = floatballCenterY - halfBallSize;
         } else if (wmY > screenHeight - this.mSize / 2) {
            position = 9;
            wmY = floatballCenterY - this.mSize + halfBallSize;
         } else {
            position = 6;
            wmY = floatballCenterY - this.mSize / 2;
         }
      }

      layoutParams.x = wmX;
      layoutParams.y = wmY;
      return position;
   }

   public ArrayList<TabContainer> getTabContainers() {
      return this.containerArrayList;
   }

   public TabContainer getTabContainer(int tabflag) {
      Iterator var2 = this.containerArrayList.iterator();

      TabContainer tabContainer;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         tabContainer = (TabContainer)var2.next();
      } while(tabflag != tabContainer.tabflag);

      return tabContainer;
   }

   public FloatTab setPosition(int position) {
      this.tabContext.setCurrentItem(position);
      return this;
   }

   public FloatBallManager getFloatBallManager() {
      return this.floatBallManager;
   }

   public ServerController getServerController() {
      return this.serverController;
   }

   public IBinder getClientBinder() {
      return this.iBinder;
   }

   public void setClientBinder(IBinder iBinder) {
      this.iBinder = iBinder;
   }

   public String getPackageName() {
      return this.packageName;
   }

   public void setPackageName(String packageName) {
      this.packageName = packageName;
   }

   public Button getFeaturesMenu() {
      return this.featuresMenu;
   }

   public void onClick(View view) {
      if (view == this.featuresMenu) {
         int currentItem = this.tabContext.getCurrentItem();
         TabContainer tabContainer = (TabContainer)this.containerArrayList.get(currentItem);
         Log.d(this.TAG, "当前是在 ：" + tabContainer + "中执行点击");
         tabContainer.onClick(view);
      }

   }
}
