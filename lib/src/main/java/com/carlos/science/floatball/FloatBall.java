package com.carlos.science.floatball;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.carlos.libcommon.StringFog;
import com.carlos.science.FloatBallManager;
import com.carlos.science.FloatBallUtil;
import com.carlos.science.LocationService;
import com.carlos.science.ServiceAgency;
import com.carlos.science.exception.AgencyException;
import com.carlos.science.runner.ICarrier;
import com.carlos.science.runner.OnceRunnable;
import com.carlos.science.runner.ScrollRunner;
import com.carlos.science.tab.FloatTab;
import com.carlos.science.utils.MotionVelocityUtil;
import com.carlos.science.utils.Util;
import com.kook.common.utils.HVLog;
import com.kook.controller.client.wechat.IWeChatController;

public class FloatBall extends FrameLayout implements ICarrier {
   String TAG = StringFog.decrypt("NQkdFxEsPh8P");
   private FloatBallManager floatBallManager;
   private ImageView imageView;
   private WindowManager.LayoutParams mLayoutParams;
   private WindowManager windowManager;
   private boolean isFirst = true;
   private boolean isAdded = false;
   private int mTouchSlop;
   private boolean isClick = false;
   private int mDownX;
   private int mDownY;
   private int mLastX;
   private int mLastY;
   private int mSize;
   private ScrollRunner mRunner;
   private int mVelocityX;
   private int mVelocityY;
   private MotionVelocityUtil mVelocity;
   private boolean sleep = false;
   private FloatBallCfg mConfig;
   private boolean mHideHalfLater = true;
   private boolean mLayoutChanged = false;
   private int mSleepX = -1;
   private boolean isLocationServiceEnable;
   private OnceRunnable mSleepRunnable = new OnceRunnable() {
      public void onRun() {
         if (FloatBall.this.mHideHalfLater && !FloatBall.this.sleep && FloatBall.this.isAdded) {
            FloatBall.this.sleep = true;
            FloatBall.this.moveToEdge(false, FloatBall.this.sleep);
            FloatBall.this.mSleepX = FloatBall.this.mLayoutParams.x;
         }

      }
   };

   public FloatBall(Context context, FloatBallManager floatBallManager, FloatBallCfg config) {
      super(context);
      this.floatBallManager = floatBallManager;
      this.mConfig = config;

      try {
         ServiceAgency.getService(LocationService.class);
         this.isLocationServiceEnable = true;
      } catch (AgencyException var5) {
         this.isLocationServiceEnable = false;
      }

      this.init(context);
   }

   private void init(Context context) {
      this.imageView = new ImageView(context);
      Drawable icon = this.mConfig.mIcon;
      this.mSize = this.mConfig.mSize;
      Util.setBackground(this.imageView, icon);
      this.addView(this.imageView, new ViewGroup.LayoutParams(this.mSize, this.mSize));
      this.initLayoutParams(context);
      this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
      this.mRunner = new ScrollRunner(this);
      this.mVelocity = new MotionVelocityUtil(context);
      this.setOnKeyListener(new View.OnKeyListener() {
         public boolean onKey(View view, int i, KeyEvent keyEvent) {
            HVLog.d(FloatBall.this.TAG, StringFog.decrypt("HAs5ExxONkk=") + i + StringFog.decrypt("U0VSVg4LJjYVChwEUw==") + keyEvent.getAction());
            return false;
         }
      });
   }

   private void initLayoutParams(Context context) {
      this.mLayoutParams = FloatBallUtil.getLayoutParams(context);
   }

   protected void onWindowVisibilityChanged(int visibility) {
      super.onWindowVisibilityChanged(visibility);
      if (visibility == 0) {
         this.onConfigurationChanged((Configuration)null);
      }

   }

   public void attachToWindow(WindowManager windowManager) {
      this.windowManager = windowManager;
      if (!this.isAdded) {
         windowManager.addView(this, this.mLayoutParams);
         this.isAdded = true;
      }

   }

   public void detachFromWindow(WindowManager windowManager) {
      this.windowManager = null;
      if (this.isAdded) {
         this.removeSleepRunnable();
         if (this.getContext() instanceof Activity) {
            windowManager.removeViewImmediate(this);
         } else {
            windowManager.removeView(this);
         }

         this.isAdded = false;
         this.sleep = false;
      }

   }

   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      int height = this.getMeasuredHeight();
      int width = this.getMeasuredWidth();
      int curX = this.mLayoutParams.x;
      if (this.sleep && curX != this.mSleepX && !this.mRunner.isRunning()) {
         this.sleep = false;
         this.postSleepRunnable();
      }

      if (this.mRunner.isRunning()) {
         this.mLayoutChanged = false;
      }

      if (height != 0 && this.isFirst || this.mLayoutChanged) {
         if (this.isFirst && height != 0) {
            this.location(width, height);
         } else {
            this.moveToEdge(false, this.sleep);
         }

         this.isFirst = false;
         this.mLayoutChanged = false;
      }

   }

   protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
      super.onLayout(changed, left, top, right, bottom);
      this.floatBallManager.floatballX = this.mLayoutParams.x;
      this.floatBallManager.floatballY = this.mLayoutParams.y;
   }

   private void location(int width, int height) {
      FloatBallCfg.Gravity cfgGravity = this.mConfig.mGravity;
      this.mHideHalfLater = this.mConfig.mHideHalfLater;
      int gravity = cfgGravity.getGravity();
      int topLimit = 0;
      int bottomLimit = this.floatBallManager.mScreenHeight - height;
      int statusBarHeight = this.floatBallManager.getStatusBarHeight();
      int x;
      if ((gravity & 3) == 3) {
         x = 0;
      } else {
         x = this.floatBallManager.mScreenWidth - width;
      }

      int y;
      if ((gravity & 48) == 48) {
         y = topLimit;
      } else if ((gravity & 80) == 80) {
         y = this.floatBallManager.mScreenHeight - height - statusBarHeight;
      } else {
         y = this.floatBallManager.mScreenHeight / 2 - height / 2 - statusBarHeight;
      }

      y = this.mConfig.mOffsetY != 0 ? y + this.mConfig.mOffsetY : y;
      if (y < 0) {
         y = topLimit;
      }

      if (y > bottomLimit) {
         y = topLimit;
      }

      if (this.isLocationServiceEnable) {
         LocationService locationService = (LocationService)ServiceAgency.getService(LocationService.class);
         int[] location = locationService.onRestoreLocation();
         if (location.length == 2) {
            int locationX = location[0];
            int locationY = location[1];
            if (locationX != -1 && locationY != -1) {
               this.onLocation(locationX, locationY);
               return;
            }
         }
      }

      this.onLocation(x, y);
   }

   protected void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      this.mLayoutChanged = true;
      this.floatBallManager.onConfigurationChanged(newConfig);
      this.moveToEdge(false, false);
      this.postSleepRunnable();
   }

   public void onLayoutChange() {
      this.mLayoutChanged = true;
      this.requestLayout();
   }

   public boolean onKeyDown(int keyCode, KeyEvent event) {
      HVLog.d(this.TAG, StringFog.decrypt("HAs5ExwqMAQNTxkVECwBFwBI") + keyCode + StringFog.decrypt("U0VSVgAYOh0XVQ==") + event.getKeyCode());
      return super.onKeyDown(keyCode, event);
   }

   public boolean onTouchEvent(MotionEvent event) {
      int action = event.getAction();
      int x = (int)event.getRawX();
      int y = (int)event.getRawY();
      this.mVelocity.acquireVelocityTracker(event);
      switch (action) {
         case 0:
            this.touchDown(x, y);
            break;
         case 1:
         case 3:
            this.touchUp();
            break;
         case 2:
            this.touchMove(x, y);
            break;
         case 4:
            FloatTab floatTab = this.floatBallManager.getFloatTab();
            IBinder binder = floatTab.getClientBinder();

            try {
               if (binder != null) {
                  HVLog.d(this.TAG, StringFog.decrypt("HAsmGRANNzYVChwESQ4NBwwdGF8=") + action + StringFog.decrypt("U0VSVgcHMRcGHUg=") + binder + "    " + binder.getInterfaceDescriptor());
               }
            } catch (RemoteException var10) {
               RemoteException e = var10;
               e.printStackTrace();
            }

            IWeChatController iWeChatController = IWeChatController.Stub.asInterface(floatTab.getClientBinder());

            try {
               int edgeFlags = event.getEdgeFlags();
               if ((edgeFlags & 1107) != 0) {
                  edgeFlags &= -1108;
               } else if (iWeChatController != null) {
                  iWeChatController.onStopOrPause(true);
               }
            } catch (RemoteException var9) {
               RemoteException e = var9;
               HVLog.e(this.TAG, (Object)e);
            }
      }

      return super.onTouchEvent(event);
   }

   private void touchDown(int x, int y) {
      this.mDownX = x;
      this.mDownY = y;
      this.mLastX = this.mDownX;
      this.mLastY = this.mDownY;
      this.isClick = true;
      this.removeSleepRunnable();
   }

   public boolean isClick() {
      return this.isClick;
   }

   private void touchMove(int x, int y) {
      int totalDeltaX = x - this.mDownX;
      int totalDeltaY = y - this.mDownY;
      int deltaX = x - this.mLastX;
      int deltaY = y - this.mLastY;
      if (Math.abs(totalDeltaX) > this.mTouchSlop || Math.abs(totalDeltaY) > this.mTouchSlop) {
         this.isClick = false;
      }

      this.mLastX = x;
      this.mLastY = y;
      if (!this.isClick) {
         this.onMove(deltaX, deltaY);
      }

   }

   private void touchUp() {
      this.mVelocity.computeCurrentVelocity();
      this.mVelocityX = (int)this.mVelocity.getXVelocity();
      this.mVelocityY = (int)this.mVelocity.getYVelocity();
      this.mVelocity.releaseVelocityTracker();
      if (this.sleep) {
         this.wakeUp();
      } else if (this.isClick) {
         this.onClick();
      } else {
         this.moveToEdge(true, false);
      }

      this.mVelocityX = 0;
      this.mVelocityY = 0;
   }

   private void moveToX(boolean smooth, int destX) {
      int statusBarHeight = this.floatBallManager.getStatusBarHeight();
      int screenHeight = this.floatBallManager.mScreenHeight - statusBarHeight;
      int height = this.getHeight();
      int destY = 0;
      if (this.mLayoutParams.y < 0) {
         destY = 0 - this.mLayoutParams.y;
      } else if (this.mLayoutParams.y > screenHeight - height) {
         destY = screenHeight - height - this.mLayoutParams.y;
      }

      if (smooth) {
         int dx = destX - this.mLayoutParams.x;
         int duration = this.getScrollDuration(Math.abs(dx));
         this.mRunner.start(dx, destY, duration);
      } else {
         this.onMove(destX - this.mLayoutParams.x, destY);
         this.postSleepRunnable();
      }

   }

   private void wakeUp() {
      int screenWidth = this.floatBallManager.mScreenWidth;
      int width = this.getWidth();
      int halfWidth = width / 2;
      int centerX = screenWidth / 2 - halfWidth;
      int destX = this.mLayoutParams.x < centerX ? 0 : screenWidth - width;
      this.sleep = false;
      this.moveToX(true, destX);
   }

   private void moveToEdge(boolean smooth, boolean forceSleep) {
      int screenWidth = this.floatBallManager.mScreenWidth;
      int width = this.getWidth();
      int halfWidth = width / 2;
      int centerX = screenWidth / 2 - halfWidth;
      int minVelocity = this.mVelocity.getMinVelocity();
      int destX;
      if (this.mLayoutParams.x < centerX) {
         this.sleep = forceSleep || Math.abs(this.mVelocityX) > minVelocity && this.mVelocityX < 0 || this.mLayoutParams.x < 0;
         destX = this.sleep ? -halfWidth : 0;
      } else {
         this.sleep = forceSleep || Math.abs(this.mVelocityX) > minVelocity && this.mVelocityX > 0 || this.mLayoutParams.x > screenWidth - width;
         destX = this.sleep ? screenWidth - halfWidth : screenWidth - width;
      }

      if (this.sleep) {
         this.mSleepX = destX;
      }

      this.moveToX(smooth, destX);
   }

   private int getScrollDuration(int distance) {
      return (int)(250.0F * (1.0F * (float)distance / 800.0F));
   }

   private void onMove(int deltaX, int deltaY) {
      WindowManager.LayoutParams var10000 = this.mLayoutParams;
      var10000.x += deltaX;
      var10000 = this.mLayoutParams;
      var10000.y += deltaY;
      if (this.windowManager != null) {
         this.windowManager.updateViewLayout(this, this.mLayoutParams);
      }

   }

   public void onLocation(int x, int y) {
      this.mLayoutParams.x = x;
      this.mLayoutParams.y = y;
      if (this.windowManager != null) {
         this.windowManager.updateViewLayout(this, this.mLayoutParams);
      }

   }

   public void onMove(int lastX, int lastY, int curX, int curY) {
      this.onMove(curX - lastX, curY - lastY);
   }

   public void onDone() {
      this.postSleepRunnable();
      if (this.isLocationServiceEnable) {
         LocationService locationService = (LocationService)ServiceAgency.getService(LocationService.class);
         locationService.onLocationChanged(this.mLayoutParams.x, this.mLayoutParams.y);
      }

   }

   private void moveTo(int x, int y) {
      WindowManager.LayoutParams var10000 = this.mLayoutParams;
      var10000.x += x - this.mLayoutParams.x;
      var10000 = this.mLayoutParams;
      var10000.y += y - this.mLayoutParams.y;
      if (this.windowManager != null) {
         this.windowManager.updateViewLayout(this, this.mLayoutParams);
      }

   }

   public int getSize() {
      return this.mSize;
   }

   private void onClick() {
      boolean isTest = false;
      this.floatBallManager.floatballX = this.mLayoutParams.x;
      this.floatBallManager.floatballY = this.mLayoutParams.y;
      if (!isTest) {
         this.floatBallManager.onFloatBallClick();
      } else {
         IWeChatController iWeChatController = IWeChatController.Stub.asInterface(this.floatBallManager.getFloatTab().getClientBinder());
         if (iWeChatController != null) {
            HVLog.e(StringFog.decrypt("NQkdFxE5Nh0HAAUjDB0YGgYXBQ=="), StringFog.decrypt("ldD5nsr7ud7GhtjU"));
         }
      }

   }

   private void removeSleepRunnable() {
      this.mSleepRunnable.removeSelf(this);
   }

   public void postSleepRunnable() {
      if (this.mHideHalfLater && !this.sleep && this.isAdded) {
         this.mSleepRunnable.postDelaySelf(this, 3000);
      }

   }
}
