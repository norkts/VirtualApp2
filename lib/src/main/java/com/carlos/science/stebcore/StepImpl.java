package com.carlos.science.stebcore;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.carlos.science.IVirtualController;
import com.kook.common.utils.HVLog;
import com.kook.controller.server.IServerController;
import org.jdeferred.Promise;

public abstract class StepImpl implements IStep {
   protected static final String TAG = StringFog.decrypt("IBEXBiwDLx8=");
   private IStepInfo<StepImpl> mStepInfo;
   public boolean finishStep = false;
   IStepController mIStepController;
   protected Rect rect = new Rect();

   public Activity getActivity() {
      return this.mIStepController.getCurrentActivity();
   }

   public void setIStepControl(IStepController stepController) {
      this.mIStepController = stepController;
   }

   public abstract void onActivityResumed(Activity var1);

   public IServerController getIServerControler() {
      return this.mIStepController.getIServerController();
   }

   public IVirtualController getVirtualControllerImpl() {
      return this.mIStepController.getVirtualControllerImpl();
   }

   protected Handler getHandler() {
      return this.mIStepController.getHandler();
   }

   public IBinder getCallBackIBinder() {
      Log.d(TAG, StringFog.decrypt("FAAGNQQCMzECDBk5KwYAFwAAVggnDAcGHzEfBxscHAkeExdU") + this.mIStepController);
      return this.mIStepController.getCallBackIBinder();
   }

   public final void doTask() {
      Promise<Void, Throwable, Void> beforeTask = this.beforeTask();
      if (beforeTask != null) {
         beforeTask.done((VOID) -> {
            if (!this.finishStep) {
               boolean task = this.task();
               if (task) {
                  this.afterTask();
               } else {
                  this.doTask();
               }

            }
         });
      } else {
         if (this.finishStep) {
            return;
         }

         boolean task = this.task();
         if (task) {
            this.afterTask();
         } else {
            this.doTask();
         }
      }

   }

   public abstract Promise<Void, Throwable, Void> beforeTask();

   public abstract boolean task();

   public abstract void afterTask();

   public String getTitle() {
      return this.getStepInfo().getTitle();
   }

   public final void finish() {
      if (this.finishStep) {
         Log.i(TAG, StringFog.decrypt("lcjXn8/Kfw==") + this.getClass().getName() + StringFog.decrypt("U0VV") + this.getTitle() + StringFog.decrypt("VEWXwdeJ5PyE1OGW9PCGzOKWzOM="));
      } else {
         Log.i(TAG, StringFog.decrypt("lN7hkPjxus7wivv9j8LLms/WVg==") + this.getClass().getName() + StringFog.decrypt("U0VV") + this.getTitle() + StringFog.decrypt("VA=="));
         if (this.getClass().getName().equals(StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeHgoNGwQGWDYaOgMzAAEZHQYBHSocNQkHPBg="))) {
            HVLog.printInfo();
            Log.i(TAG, StringFog.decrypt("WU9YXE9EdVlJRVhaQ0VEWU9YXE9Ot8zeh8raSYfe8ILm3kWL//WFz/pQQ0VEWU9YXE9EdVlJRVhaQ0VEWU9YXA=="));
         }

         this.getController().finishCurrentStep();
         this.finishStep = true;
      }
   }

   public IStepController getController() {
      IStepInfo stepInfo = this.getStepInfo();
      IStepController controller = stepInfo.getController();
      return controller;
   }

   public IStepInfo getStepInfo() {
      return this.mStepInfo;
   }

   public void sleep(long time) {
      try {
         Thread.sleep(time);
      } catch (InterruptedException var4) {
         InterruptedException e = var4;
         Log.i(TAG, StringFog.decrypt("Fh0RExUaNhwNTxdK") + e.toString());
      }

   }

   public void setStepInfo(IStepInfo stepInfo) {
      this.mStepInfo = stepInfo;
   }
}
