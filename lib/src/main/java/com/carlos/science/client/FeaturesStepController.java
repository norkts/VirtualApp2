package com.carlos.science.client;

import android.app.Activity;
import android.os.Handler;
import android.os.IBinder;
import com.carlos.libcommon.StringFog;
import com.carlos.science.IVirtualController;
import com.carlos.science.stebcore.IStep;
import com.carlos.science.stebcore.IStepInfo;
import com.carlos.science.stebcore.StepControllerImpl;
import com.carlos.science.stebcore.StepImpl;
import com.kook.common.utils.HVLog;
import com.kook.controller.server.IServerController;

public class FeaturesStepController extends StepControllerImpl<StepImpl> {
   private int mContainerId = 0;

   public FeaturesStepController(ClientActivityLifecycle clientActivityLifecycle, IBinder iBinder) {
      super(clientActivityLifecycle, iBinder);
   }

   public void setContainerId(int containerId) {
      this.mContainerId = containerId;
   }

   public void initSteps() {
   }

   public Activity getCurrentActivity() {
      if (this.clientActivityLifecycle == null) {
         HVLog.d(StringFog.decrypt("UyMXFxEbLRYQPAYVGSwBHREAGQkCOgFDCBcEKhocAQAcAiQNKxoVBgYJSQwCGgAcAiQNKxoVBgYJJQYIFgYLFQkLfxoQTxwFBQNO"));
      }

      return this.clientActivityLifecycle.getCurrentActivity();
   }

   public Handler getHandler() {
      return this.clientActivityLifecycle.getHandler();
   }

   public IBinder getCallBackIBinder() {
      HVLog.d(StringFog.decrypt("UyMXFxEbLRYQPAYVGSwBHREAGQkCOgFDCBcEKg4CHycTFQ4nHRoNCxcCSQwPHwkwFwYFFjEKARYVG1U=") + this.callBackIBinder);
      return this.callBackIBinder;
   }

   public IServerController getIServerController() {
      return this.clientActivityLifecycle.getIServerController();
   }

   public IVirtualController getVirtualControllerImpl() {
      return this.clientActivityLifecycle.getVirtualControllerImpl();
   }

   public void doTask(IStepInfo iStepInfo) {
      IStepInfo<StepImpl> nextStepInfo = iStepInfo;
      if (iStepInfo != null) {
         StepImpl currentStep = (StepImpl)nextStepInfo.getStepImpl();
         this.mCurrentStep = currentStep;
         currentStep.doTask();
         if (!currentStep.finishStep) {
            HVLog.d(StringFog.decrypt("IBEXBiYBMQcRAB4cDB0nHhUe"), StringFog.decrypt("lerikcHUsM/5hu7wgcnvlvnakMjLttnHT1U=") + currentStep.getTitle() + StringFog.decrypt("VEWU/8KG/v+Gwf6W4f+L4+uaxuaJy9sFBhwZGgdGWoPv04LVzJX+8JTdzIbE1w=="));
         }
      }

   }

   private void sleep(long time) {
      try {
         Thread.sleep(time);
      } catch (InterruptedException var4) {
         InterruptedException e = var4;
         HVLog.i(StringFog.decrypt("IBEXBiYBMQcRAB4cDB0nHhUe"), StringFog.decrypt("Fh0RExUaNhwNTxdK") + e.toString());
      }

   }

   public IStep getCurrentStep() {
      return this.mCurrentStep;
   }

   public void fastForward() {
      HVLog.i(StringFog.decrypt("IBEXBiYBMQcRAB4cDB0nHhUe"), StringFog.decrypt("lcjXn8/Ku8jth83pgOjiltnyk8LlufrEh9P8"));
      IStepInfo<StepImpl> topStep = this.getTopStepInfo();
      this.doTask(topStep);
   }
}
