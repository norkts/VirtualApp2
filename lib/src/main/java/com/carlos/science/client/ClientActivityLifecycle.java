package com.carlos.science.client;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import com.carlos.science.IVirtualController;
import com.kook.controller.server.IServerController;

public interface ClientActivityLifecycle {
   void setIServerController(Application var1, IServerController var2, IVirtualController var3);

   IVirtualController getVirtualControllerImpl();

   IServerController getIServerController();

   Activity getCurrentActivity();

   Handler getHandler();

   void onActivityResumed(Activity var1);

   void onActivityPaused(Activity var1);

   void runOnUiThread(Runnable var1);
}
