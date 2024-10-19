package com.carlos.science.stebcore;

import android.app.Activity;
import android.os.Handler;
import android.os.IBinder;
import com.carlos.science.IVirtualController;
import com.kook.controller.server.IServerController;

public interface IStepController<T extends IStep> {
   void initSteps();

   Activity getCurrentActivity();

   Handler getHandler();

   IBinder getCallBackIBinder();

   IServerController getIServerController();

   IVirtualController getVirtualControllerImpl();

   void doTask(IStepInfo<T> var1);

   boolean hasNext();

   void finishCurrentStep();

   void finishAllSteps();

   boolean hasFinishAllSteps();

   IStep getCurrentStep();

   public interface StepStatusListener {
      void onStepFinished(IStep var1);

      void onAllStepsFinished();
   }
}
