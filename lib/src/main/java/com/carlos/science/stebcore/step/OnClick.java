package com.carlos.science.stebcore.step;

import android.app.Activity;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import com.carlos.libcommon.StringFog;
import com.carlos.science.IVirtualController;
import com.carlos.science.client.core.FindView;
import com.carlos.science.stebcore.StepImpl;
import com.kook.controller.server.IServerController;

public abstract class OnClick extends StepImpl {
   protected String TAG = StringFog.decrypt("PAsxGgwNNA==");
   protected int tryMax = 5;
   protected boolean doubleClick = false;

   public void onActivityResumed(Activity activity) {
   }

   public boolean task() {
      int[] center = this.getLocCenterByView();
      int centerX;
      if (center == null) {
         for(centerX = 0; centerX < this.tryMax; ++centerX) {
            if (this.finishStep) {
               return true;
            }

            this.sleep(400L);
            center = this.getLocCenterByView();
            Log.d(this.TAG, StringFog.decrypt("luP/kMnPusP+h93lHwYLBEWV7OGK4v6E0tw="));
            if (center != null) {
               break;
            }
         }

         if (center == null) {
            Log.d(this.TAG, StringFog.decrypt("m9rrn+LiuezGifvOjOfelP/2AAwLKFOG1eaYxsqI68qWzt8AKh8PQ5T56YvV1o3N74zp0xAGAQYVG0+I+uiU7soAKh8P"));
            return false;
         }
      }

      centerX = center[0];
      int centerY = center[1];
      IVirtualController virtualControllerImpl = this.getVirtualControllerImpl();

      try {
         IServerController iServerControler = this.getIServerControler();
         if (this.doubleClick) {
            iServerControler.virtualClick(centerX, centerY);
            this.sleep(50L);
            iServerControler.virtualClick(centerX, centerY);
         } else {
            iServerControler.virtualClick(centerX, centerY);
         }
      } catch (RemoteException var6) {
         RemoteException e = var6;
         e.printStackTrace();
         Log.e(this.TAG, StringFog.decrypt("IBEXGyYCNhAITxYfPQ4dGEUXDgYLLwcKABxK") + e.toString());
      }

      return true;
   }

   protected abstract View findView();

   public int[] getLocCenterByView() {
      View viewByActivity = this.findView();
      if (viewByActivity == null) {
         return null;
      } else {
         int[] loc = FindView.getLocationOnScreen(viewByActivity);
         int width = viewByActivity.getWidth();
         int height = viewByActivity.getHeight();
         int centerX = loc[0] + width / 2;
         int centerY = loc[1] + height / 2;
         int[] center = new int[]{centerX, centerY};
         return center;
      }
   }
}
