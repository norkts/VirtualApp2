package com.carlos.science.stebcore;

import java.io.Serializable;

public interface IStepInfo<T extends IStep> extends Serializable {
   long serialVersionUID = 1L;

   long getStepId();

   String getTitle();

   T getStepImpl();

   IStepController getController();
}
