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
         HVLog.d(" FeaturesStepController getCurrentActivity clientActivityLifecycle is null ");
      }

      return this.clientActivityLifecycle.getCurrentActivity();
   }

   public Handler getHandler() {
      return this.clientActivityLifecycle.getHandler();
   }

   public IBinder getCallBackIBinder() {
      HVLog.d(" FeaturesStepController getCallBackIBinder callBackIBinder:" + this.callBackIBinder);
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
            HVLog.d("StepControllerImpl", "提示：需要在步骤 \'" + currentStep.getTitle() + "\' 执行完成后调用finish()来结束步骤");
         }
      }

   }

   private void sleep(long time) {
      try {
         Thread.sleep(time);
      } catch (InterruptedException var4) {
         InterruptedException e = var4;
         HVLog.i("StepControllerImpl", "exception e:" + e.toString());
      }

   }

   public IStep getCurrentStep() {
      return this.mCurrentStep;
   }

   public void fastForward() {
      HVLog.i("StepControllerImpl", "步骤从这里开始执行");
      IStepInfo<StepImpl> topStep = this.getTopStepInfo();
      this.doTask(topStep);
   }
}
