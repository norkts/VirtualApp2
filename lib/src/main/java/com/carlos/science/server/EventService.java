package com.carlos.science.server;

import android.app.Service;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.RequiresApi;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class EventService extends Service implements Runnable {
   String TAG = StringFog.decrypt("NhMXGBE9OgEVBhEV");
   Thread threadProcess;
   private Queue<Event> queue = new ConcurrentLinkedQueue();
   private boolean EVENT_FLAG = false;
   private final int EVENT_TYPE_EMULATOR_TOUCH = 2;
   private final int EVENT_TYPE_EMULATOR_CLICK = 3;
   private final int EVENT_TYPE_EMULATOR_KEY_CODE = 4;
   public static final int CONTROLLER_EVENT_FLAGS = 1107;
   Handler mHandler = new Handler();
   public static final int TOOL_TYPE_UNKNOWN = 0;
   public static final int TOOL_TYPE_FINGER = 1;
   public static final int TOOL_TYPE_STYLUS = 2;
   public static final int TOOL_TYPE_MOUSE = 3;
   public static final int TOOL_TYPE_ERASER = 4;
   boolean loopVirtualTouchEvent = false;
   Method recycleMethod = null;
   Class<?> clazzInputManager = null;
   Object objectInputManager = null;
   Method getInstanceMethod = null;
   Method injectInputEventMethod = null;
   static MotionEvent.PointerProperties properties;
   static MotionEvent.PointerProperties[] pointerProperties;
   static MotionEvent.PointerCoords[] pointerCoords;
   static MotionEvent.PointerCoords pressure;

   @RequiresApi(
      api = 16
   )
   public void onCreate() {
      super.onCreate();
      this.threadProcess = new Thread(this);
      this.threadProcess.start();
   }

   public boolean emulatorClick(int centerX, int centerY) {
      if (!this.isMainThread()) {
         return this.virtualClick(centerX, centerY);
      } else {
         HVLog.i(this.TAG, StringFog.decrypt("mvjskt3VuMnciNr7j/zjl9ju"));
         Event event = new Event(3);
         event.centerx = centerX;
         event.centery = centerY;
         this.addEvent(event);

         while(this.EVENT_FLAG) {
            this.sleep(200L);
         }

         return true;
      }
   }

   public boolean emulatorKey(int key_code) {
      if (!this.isMainThread()) {
         return this.sendKeyEvent(4098, key_code, false);
      } else {
         Event event = new Event(4);
         event.key_code = key_code;
         this.addEvent(event);

         while(this.EVENT_FLAG) {
            this.sleep(200L);
         }

         return true;
      }
   }

   public int[] getLocationOnScreen(View view) {
      int[] location = new int[2];
      view.getLocationOnScreen(location);
      return location;
   }

   protected boolean clickableView(View view) {
      int[] loc = new int[2];
      view.getLocationOnScreen(loc);
      int width = view.getWidth();
      int height = view.getHeight();
      int centerX = loc[0] + width / 2;
      int centerY = loc[1] + height / 2;
      Rect rect = new Rect();
      view.getGlobalVisibleRect(rect);
      if (!rect.contains(centerX, centerY)) {
         centerX = rect.centerX();
         centerY = rect.centerY();
         if (centerX < 0 || centerY < 0) {
            HVLog.e(this.TAG, StringFog.decrypt("U4DP5YDn0gUKCgVQjdfjlvnak9Thusr2h/7zjPTal93fk+rBt9TiQ5Xy0IrpyIDWx43a+g=="));
            return false;
         }
      }

      return this.emulatorClick(centerX, centerY);
   }

   public boolean emulatorTouch(int fromX, int fromY, int toX, int toY, boolean direction) {
      if (!this.isMainThread()) {
         return this.virtualTouch(fromX, fromY, toX, toY, direction);
      } else {
         Event event = new Event(2);
         event.fromX = fromX;
         event.fromY = fromY;
         event.toX = toX;
         event.toY = toY;
         event.direction = direction;
         this.addEvent(event);

         while(this.EVENT_FLAG) {
            this.sleep(200L);
         }

         return true;
      }
   }

   public void run() {
      while(!Thread.interrupted()) {
         synchronized(this.queue) {
            try {
               if (null == this.queue || this.queue.size() == 0) {
                  this.queue.wait();
               }

               Event event = (Event)this.queue.poll();
               if (event.action_type == 3) {
                  this.virtualClick(event.centerx, event.centery);
               } else if (event.action_type == 2) {
                  this.virtualTouch(event.fromX, event.fromY, event.toX, event.toY, event.direction);
               } else if (event.action_type == 4) {
                  this.sendKeyEvent(4098, event.key_code, false);
               }

               this.EVENT_FLAG = false;
            } catch (Exception var4) {
               Exception e = var4;
               HVLog.i(this.TAG, StringFog.decrypt("Fh0RExUaNhwNTxdK") + e.toString());
            }
         }
      }

   }

   public void addEvent(Event event) {
      this.EVENT_FLAG = true;
      synchronized(this.queue) {
         this.queue.add(event);
         this.queue.notifyAll();
      }
   }

   private boolean sendKeyEvent(int inputSource, int keyCode, boolean longpress) {
      long now = SystemClock.uptimeMillis();
      KeyEvent down = new KeyEvent(now, now, 0, keyCode, 0, 0, -1, 0, 0, inputSource);
      this.invokeInjectInputEventMethod(down, 2);
      KeyEvent up;
      if (longpress) {
         up = new KeyEvent(now, now, 0, keyCode, 1, 0, -1, 0, 128, inputSource);
         this.invokeInjectInputEventMethod(up, 2);
      }

      up = new KeyEvent(now, now, 1, keyCode, 0, 0, -1, 0, 0, inputSource);
      this.invokeInjectInputEventMethod(up, 2);
      return true;
   }

   private boolean virtualClick(int centerX, int centerY) {
      long currentTimeMillis = System.currentTimeMillis();
      long downTime = SystemClock.uptimeMillis();
      long eventTime = SystemClock.uptimeMillis();
      MotionEvent motionEvent = makeMotionEvent(downTime, eventTime, 0, (float)centerX, (float)centerY);
      this.invokeInjectInputEventMethod(motionEvent, 1);
      motionEvent = makeMotionEvent(downTime, eventTime + 2L, 1, (float)centerX, (float)centerY);
      this.invokeInjectInputEventMethod(motionEvent, 2);
      HVLog.e(this.TAG, StringFog.decrypt("MAocAhcBMx8GHSEVGxkHEAAwFxYLfwUKHQYFCAMtHwwRHUWG3+SF+MRQUw==") + (System.currentTimeMillis() - currentTimeMillis) + StringFog.decrypt("U0VSVgYLMQcGHSpK") + centerX + StringFog.decrypt("U0VSVgYLMQcGHStK") + centerY + StringFog.decrypt("U0VSVoP28JbzyZfswYvWyILIyYLG1FND") + (Looper.getMainLooper() == Looper.myLooper()));
      return true;
   }

   public boolean isMainThread() {
      boolean isMainLooper = Looper.getMainLooper() == Looper.myLooper();
      HVLog.d(this.TAG, StringFog.decrypt("GhY/FwwAExwMHxcCSQsLFUUAExEbLR1DGwAFDFU=") + isMainLooper);
      return true;
   }

   protected boolean virtualTouch(int fromX, int fromY, int toX, int toY, boolean direction) {
      try {
         int step = 20;
         int y;
         if (direction) {
            y = fromY;
         } else {
            y = toY;
         }

         long downTime = SystemClock.uptimeMillis();
         long eventTime = SystemClock.uptimeMillis();
         MotionEvent motionEvent = null;
         motionEvent = makeMotionEvent(downTime, eventTime, 0, (float)fromX, (float)y);
         this.invokeInjectInputEventMethod(motionEvent, 1);
         int stepCount = Math.abs((fromY - toY) / step);

         for(int i = 0; i < stepCount; ++i) {
            if (direction) {
               y -= step;
            } else {
               y += step;
            }

            eventTime += 20L;
            motionEvent = makeMotionEvent(downTime, eventTime, 2, (float)fromX, (float)y);
            this.invokeInjectInputEventMethod(motionEvent, 2);
         }

         motionEvent = makeMotionEvent(downTime, eventTime, 1, (float)toX, (float)y);
         this.invokeInjectInputEventMethod(motionEvent, 2);
         return true;
      } catch (Exception var15) {
         Exception e = var15;
         HVLog.i(this.TAG, StringFog.decrypt("BQwAAhAPMycMGhEYSQoWEAACAgwBMVMGVQ==") + e.toString());
         return false;
      }
   }

   public void sleep(long time) {
      try {
         Thread.sleep(time);
      } catch (InterruptedException var4) {
         InterruptedException e = var4;
         HVLog.i(this.TAG, StringFog.decrypt("Fh0RExUaNhwNTxdK") + e.toString());
      }

   }

   private void invokeInjectInputEventMethod(InputEvent event, int mode) {
      boolean isException = false;

      try {
         if (this.clazzInputManager == null) {
            this.clazzInputManager = Class.forName(StringFog.decrypt("EgsWBAoHO10LDgAUHg4cFksbGBUbK10qAQIFHSIPHQQVExc="));
         }

         if (this.getInstanceMethod == null) {
            this.getInstanceMethod = this.clazzInputManager.getMethod(StringFog.decrypt("FAAGPwsdKxINDBc="));
            this.objectInputManager = this.getInstanceMethod.invoke(this.clazzInputManager);
         }

         if (this.injectInputEventMethod == null) {
            this.injectInputEventMethod = this.clazzInputManager.getMethod(StringFog.decrypt("GgsYEwYaFh0TGgY1HwoABw=="), InputEvent.class, Integer.TYPE);
         }

         if (this.objectInputManager != null) {
            this.injectInputEventMethod.invoke(this.objectInputManager, event, mode);
            this.recycleMethod = event.getClass().getMethod(StringFog.decrypt("AQARDwYCOg=="));
            this.recycleMethod.invoke(event);
         } else {
            HVLog.e(this.TAG, StringFog.decrypt("HAcYEwYaFh0TGgY9CAEPFAAAVgwdfx0WAx4="));
         }
      } catch (IllegalAccessException var5) {
         IllegalAccessException e = var5;
         isException = true;
         HVLog.e(this.TAG, StringFog.decrypt("UyweGgAJPh8iDBEVGhwrCwYXBhEHMB1D") + e.toString());
      } catch (InvocationTargetException var6) {
         InvocationTargetException e = var6;
         isException = true;
         HVLog.e(this.TAG, StringFog.decrypt("UywcAAoNPgcKABwkCB0JFhE3DgYLLwcKABxQ") + e.toString());
      } catch (NoSuchMethodException var7) {
         NoSuchMethodException e = var7;
         isException = true;
         HVLog.e(this.TAG, StringFog.decrypt("UysdJRANNz4GGxofDSoWEAACAgwBMVM=") + e.toString());
      } catch (ClassNotFoundException var8) {
         ClassNotFoundException e = var8;
         isException = true;
         HVLog.e(this.TAG, StringFog.decrypt("UyYeFxYdERwXKR0FBwsrCwYXBhEHMB1D") + e.toString());
      } catch (SecurityException var9) {
         SecurityException e = var9;
         isException = true;
         HVLog.e(this.TAG, StringFog.decrypt("UzYXFRAcNgcaKgoTDB8aGgocVg==") + e.toString());
      }

      if (isException) {
         HVLog.e(this.TAG, StringFog.decrypt("U4DO9IDW55T565Xy0Ij09wAEEwsaf0k=") + event);
      }

   }

   public static MotionEvent makeMotionEvent(long downTime, long eventTime, int action, float x, float y) {
      int pointerCount = 1;
      if (properties == null) {
         properties = new MotionEvent.PointerProperties();
      }

      properties.id = 1;
      properties.toolType = 1;
      if (pointerProperties == null) {
         pointerProperties = new MotionEvent.PointerProperties[1];
      }

      pointerProperties[0] = properties;
      if (pointerCoords == null) {
         pointerCoords = new MotionEvent.PointerCoords[1];
      }

      if (pressure == null) {
         pressure = new MotionEvent.PointerCoords();
      }

      pressure.x = x;
      pressure.y = y;
      pressure.pressure = 1000.0F;
      pressure.size = 100.0F;
      pointerCoords[0] = pressure;
      int metaState = 0;
      int buttonState = 0;
      float xPrecision = 1.3342593F;
      float yPrecision = 1.3338541F;
      int deviceId = 4;
      int edgeFlags = 1107;
      int source = 4098;
      int flags = 0;
      MotionEvent motionEvent = MotionEvent.obtain(downTime, eventTime, action, pointerCount, pointerProperties, pointerCoords, metaState, buttonState, xPrecision, yPrecision, deviceId, edgeFlags, source, flags);
      motionEvent.setSource(source);
      return motionEvent;
   }

   private static final float lerp(float a, float b, float alpha) {
      return (b - a) * alpha + a;
   }

   protected void sendSwipe(int inputSource, float x1, float y1, float x2, float y2, int duration) {
      if (duration < 0) {
         duration = 300;
      }

      long now = SystemClock.uptimeMillis();
      this.injectMotionEvent(inputSource, 0, now, x1, y1, 1.0F);
      long startTime = now;

      for(long endTime = startTime + (long)duration; now < endTime; now = SystemClock.uptimeMillis()) {
         long elapsedTime = now - startTime;
         float alpha = (float)elapsedTime / (float)duration;
         this.injectMotionEvent(inputSource, 2, now, lerp(x1, x2, alpha), lerp(y1, y2, alpha), 1.0F);
      }

      this.injectMotionEvent(inputSource, 1, now, x2, y2, 0.0F);
   }

   private void injectMotionEvent(int inputSource, int action, long when, float x, float y, float pressure) {
//      float DEFAULT_SIZE = 1.0F;
//      int DEFAULT_META_STATE = false;
//      float DEFAULT_PRECISION_X = 1.0F;
//      float DEFAULT_PRECISION_Y = 1.0F;
//      int DEFAULT_DEVICE_ID = false;
//      int DEFAULT_EDGE_FLAGS = false;
      MotionEvent event = MotionEvent.obtain(when, when, action, x, y, pressure, 1.0F, 0, 1.0F, 1.0F, 0, 0);
      event.setSource(inputSource);
      this.invokeInjectInputEventMethod(event, 2);
   }

   private class Event {
      int key_code;
      int action_type;
      int fromX;
      int fromY;
      int toX;
      int toY;
      int centerx;
      int centery;
      boolean direction;

      public Event(int action_type) {
         this.action_type = action_type;
      }
   }
}
