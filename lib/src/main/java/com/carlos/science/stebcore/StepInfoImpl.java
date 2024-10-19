package com.carlos.science.stebcore;

import android.app.Activity;
import com.kook.controller.server.IServerController;
import java.util.UUID;

public class StepInfoImpl implements IStepInfo<StepImpl> {
   private final long mId = UUID.randomUUID().getLeastSignificantBits();
   private StepImpl mStepImpl;
   private IStepController mController;
   private final String mTitle;

   public StepInfoImpl(StepImpl stepClass, IStepController stepControlller, String title) {
      this.mStepImpl = stepClass;
      this.mController = stepControlller;
      this.mStepImpl.setIStepControl(stepControlller);
      this.mTitle = title;
   }

   public long getStepId() {
      return this.mId;
   }

   public String getTitle() {
      return this.mTitle;
   }

   public StepImpl getStepImpl() {
      Activity currentActivity = this.mController.getCurrentActivity();
      IServerController serverController = this.mController.getIServerController();
      this.mStepImpl.setStepInfo(this);
      return this.mStepImpl;
   }

   public IStepController getController() {
      return this.mController;
   }
}
