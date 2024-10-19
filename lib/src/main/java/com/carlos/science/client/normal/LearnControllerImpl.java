package com.carlos.science.client.normal;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import com.carlos.libcommon.StringFog;
import com.carlos.science.IVirtualController;
import com.carlos.science.client.ClientActivityLifecycle;
import com.carlos.science.client.FeaturesStepController;
import com.carlos.science.stebcore.IStep;
import com.carlos.science.stebcore.IStepController;
import com.carlos.science.stebcore.StepImpl;
import com.kook.common.utils.HVLog;
import com.kook.controller.client.normal.ILearnController;
import com.kook.controller.server.IServerController;
import com.lody.virtual.helper.utils.Singleton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class LearnControllerImpl extends ILearnController.Stub implements ClientActivityLifecycle {
   String TAG = "LearnControllerImpl";
   Handler mHandler;
   Activity mCurrentActivity;
   IServerController mIServerController;
   Application application;
   IVirtualController mVirtualControllerImpl;
   Observable.OnSubscribe<Activity> onSubscribe;
   ObserverSubscriber mSubscriber = null;
   boolean mLoginStatus = false;
   private static final Singleton<LearnControllerImpl> sService = new Singleton<LearnControllerImpl>() {
      protected LearnControllerImpl create() {
         return new LearnControllerImpl();
      }
   };
   FeaturesStepController mFeaturesStepController;
   IStepController.StepStatusListener stepStatusListener = new IStepController.StepStatusListener() {
      public void onStepFinished(IStep step) {
      }

      public void onAllStepsFinished() {
         IBinder callBackIBinder = LearnControllerImpl.this.mFeaturesStepController.getCallBackIBinder();
      }
   };
   ViewTreeObserver.OnWindowFocusChangeListener onWindowFocusChangeListener = new ViewTreeObserver.OnWindowFocusChangeListener() {
      public void onWindowFocusChanged(boolean focus) {
         HVLog.d(LearnControllerImpl.this.TAG, "onWindowFocusChanged focus:" + focus);
         if (focus) {
            LearnControllerImpl.this.onSubscribe = new Observable.OnSubscribe<Activity>() {
               public void call(Subscriber<? super Activity> subscriber) {
                  if (LearnControllerImpl.this.getCurrentActivity() == null) {
                     subscriber.onCompleted();
                  } else {
                     LearnControllerImpl.this.mSubscriber.onNext(LearnControllerImpl.this.getCurrentActivity());
                     LearnControllerImpl.this.mSubscriber.onCompleted();
                  }
               }
            };
            Observable<Activity> stringObservable = Observable.create(LearnControllerImpl.this.onSubscribe);
            LearnControllerImpl.this.commitObservable(stringObservable);
         }

      }
   };

   public static LearnControllerImpl get() {
      return (LearnControllerImpl)sService.get();
   }

   public FeaturesStepController getFeaturesStepController(IBinder iBinder, boolean hasNeedCallBack) {
      if (this.mFeaturesStepController == null) {
         if (iBinder == null && hasNeedCallBack) {
            throw new NullPointerException("回调 iBinder 不能为null ");
         }

         this.mFeaturesStepController = new FeaturesStepController(this, iBinder);
         this.mFeaturesStepController.setContainerId(1);
      }

      if (hasNeedCallBack) {
         this.mFeaturesStepController.addStepStatusListener(this.stepStatusListener);
      } else {
         this.mFeaturesStepController.addStepStatusListener((IStepController.StepStatusListener)null);
      }

      this.mFeaturesStepController.setCllBackIBinder(iBinder);
      this.mFeaturesStepController.finishAllSteps();
      return this.mFeaturesStepController;
   }

   public void setIServerController(Application application, IServerController serverController, IVirtualController virtualControllerImpl) {
      this.mIServerController = serverController;
      this.application = application;
      this.mVirtualControllerImpl = virtualControllerImpl;
   }

   public IVirtualController getVirtualControllerImpl() {
      return this.mVirtualControllerImpl;
   }

   public IServerController getIServerController() {
      return this.mIServerController;
   }

   public Activity getCurrentActivity() {
      return this.mCurrentActivity;
   }

   public Handler getHandler() {
      return this.mHandler;
   }

   public void onActivityResumed(Activity activity) {
      this.mCurrentActivity = activity;
      Window activityWindow = activity.getWindow();
      if (this.mHandler == null) {
         this.mHandler = new Handler();
      }

      View decorView = activityWindow.getDecorView();
      decorView.getViewTreeObserver().addOnWindowFocusChangeListener(this.onWindowFocusChangeListener);
   }

   public void onActivityPaused(Activity activity) {
      Window activityWindow = activity.getWindow();
      View decorView = activityWindow.getDecorView();
      decorView.getViewTreeObserver().removeOnWindowFocusChangeListener(this.onWindowFocusChangeListener);
   }

   public void runOnUiThread(Runnable runnable) {
      this.mHandler.post(runnable);
   }

   public void debugLearn() throws RemoteException {
   }

   public void readFileByLines(String fileName) {
      File file = new File(fileName);
      BufferedReader reader = null;

      try {
         IOException e;
         try {
            HVLog.i(this.TAG, "以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            e = null;

            String tempString;
            for(int line = 1; (tempString = reader.readLine()) != null; ++line) {
               HVLog.d("tag_so", "line " + line + ": " + tempString);
            }

            reader.close();
         } catch (IOException var14) {
            e = var14;
            e.printStackTrace();
            HVLog.printException(this.TAG, e);
         }
      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (IOException var13) {
               IOException e1 = var13;
               HVLog.printException(this.TAG, e1);
            }
         }

      }

   }

   private void commitObservable(Observable<Activity> observable) {
      observable.subscribeOn(AndroidSchedulers.mainThread());
      observable.observeOn(AndroidSchedulers.mainThread());
      if (this.mSubscriber == null) {
         this.mSubscriber = new ObserverSubscriber();
      }

      observable.subscribe(this.mSubscriber);
   }

   public class ObserverSubscriber extends Subscriber<Activity> {
      public void onCompleted() {
      }

      public void onError(Throwable throwable) {
      }

      public void onNext(Activity activity) {
         if (LearnControllerImpl.this.mFeaturesStepController != null) {
            StepImpl currentStep = (StepImpl)LearnControllerImpl.this.mFeaturesStepController.getCurrentStep();
            if (currentStep != null) {
               currentStep.onActivityResumed(activity);
            }
         }

      }
   }
}
