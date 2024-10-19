package com.carlos.science.client;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;
import com.carlos.science.IVirtualController;
import com.kook.common.utils.HVLog;
import com.kook.controller.server.IServerController;

public class ClientServiceConnection extends ForegroundCallbacks implements ServiceConnection {
   String TAG = "ClientServiceConnection";
   Application mApplication;
   IServerController mIServerController;
   IVirtualController mVirtualControllerImpl;
   IBinder iBinder = null;
   private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
      public void binderDied() {
         HVLog.e(ClientServiceConnection.this.TAG, "注册一个死亡代理在这里");
      }
   };
   ForegroundCallbacks.Listener mListener = new ForegroundCallbacks.Listener() {
      public void onBecameForeground(Activity activity) {
         if (ClientServiceConnection.this.mVirtualControllerImpl.getController() != null) {
            if (ClientServiceConnection.this.mVirtualControllerImpl.getController() == null || ClientServiceConnection.this.mVirtualControllerImpl.getController().needShow()) {
               String packageName = activity.getPackageName();
               HVLog.d(ClientServiceConnection.this.TAG, "从后台回到前台需要执行的逻辑 " + ClientServiceConnection.this.getClientIBinder(packageName));

               try {
                  IBinder service = ClientServiceConnection.this.getService(packageName);
                  if (ClientServiceConnection.this.mIServerController != null && service != null) {
                     ClientServiceConnection.this.mIServerController.show(activity.getPackageName(), service);
                  } else {
                     HVLog.e("onBecameForeground mIServerControler is not init");
                  }
               } catch (RemoteException var4) {
                  RemoteException e = var4;
                  HVLog.printThrowable(e);
               }

            }
         }
      }

      public void onBecameBackground() {
         try {
            HVLog.d(ClientServiceConnection.this.TAG, "当前程序切换到后台");
            if (ClientServiceConnection.this.mIServerController != null) {
               ClientServiceConnection.this.mIServerController.hide();
            } else {
               HVLog.d(ClientServiceConnection.this.TAG, "onBecameBackground mIServerControler is not init");
            }
         } catch (RemoteException var2) {
            RemoteException e = var2;
            HVLog.printThrowable(e);
         }

      }
   };

   public ClientServiceConnection(Application application, IVirtualController virtualController) {
      this.mApplication = application;
      this.mVirtualControllerImpl = virtualController;
      this.mApplication.registerActivityLifecycleCallbacks(this);
      this.addListener(this.mListener);
   }

   public IBinder getServerIBinder() {
      return this.iBinder;
   }

   public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
      try {
         this.mIServerController = IServerController.Stub.asInterface(iBinder);
         if (this.mApplication != null) {
            String packageName = this.mApplication.getPackageName();
            IBinder service = this.getService(packageName);
            this.iBinder = iBinder;
            HVLog.i(this.TAG, "这里 连接上了  iBinder:" + iBinder);
            if (service instanceof ClientActivityLifecycle) {
               ClientActivityLifecycle clientActivityLifecycle = (ClientActivityLifecycle)service;
               if (this.mIServerController != null) {
                  clientActivityLifecycle.setIServerController(this.mApplication, this.mIServerController, this.mVirtualControllerImpl);
               }
            }

            if (service != null) {
               service.linkToDeath(this.mDeathRecipient, 0);
               if (this.mVirtualControllerImpl.getController() == null) {
                  return;
               }

               if (this.mVirtualControllerImpl.getController() != null && !this.mVirtualControllerImpl.getController().needShow()) {
                  return;
               }

               this.mIServerController.setClientApplication(packageName, this.getClientIBinder(packageName));
            }
         } else {
            HVLog.d("mApplication is null");
         }
      } catch (RemoteException var6) {
         RemoteException e = var6;
         HVLog.printException(e);
      }

   }

   public IBinder getService(String packageName) {
      IBinder service = ClientControlerServiceCache.getService(packageName);
      return service;
   }

   public void onServiceDisconnected(ComponentName componentName) {
      HVLog.i(this.TAG, "这里 连接中断了");
   }

   public void onActivityResumed(Activity activity) {
      super.onActivityResumed(activity);
      if (this.mVirtualControllerImpl != null) {
         this.mVirtualControllerImpl.controllerActivityResume(activity);
      }

      String packageName = activity.getPackageName();
      IBinder service = this.getClientIBinder(packageName);
      if (service instanceof ClientActivityLifecycle) {
         ClientActivityLifecycle clientActivityLifecycle = (ClientActivityLifecycle)service;
         clientActivityLifecycle.onActivityResumed(activity);
         if (this.mIServerController != null) {
            clientActivityLifecycle.setIServerController(this.mApplication, this.mIServerController, this.mVirtualControllerImpl);
         }
      }

   }

   public void onActivityPaused(Activity activity) {
      super.onActivityPaused(activity);
      String packageName = activity.getPackageName();
      IBinder service = this.getService(packageName);
      if (service instanceof ClientActivityLifecycle) {
         ClientActivityLifecycle clientActivityLifecycle = (ClientActivityLifecycle)service;
         clientActivityLifecycle.onActivityPaused(activity);
      }

   }

   public IBinder getClientIBinder(String packageName) {
      IBinder service = this.getService(packageName);
      return service;
   }
}
