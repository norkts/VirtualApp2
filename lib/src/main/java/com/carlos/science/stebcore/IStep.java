package com.carlos.science.stebcore;

public interface IStep {
   void finish();

   void doTask();

   IStepController getController();

   IStepInfo getStepInfo();
}
