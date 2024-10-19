package com.carlos.science.client.hyxd;

import android.app.Activity;
import android.app.Application;
import android.os.Binder;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.carlos.science.IVirtualController;
import com.carlos.science.client.ClientActivityLifecycle;
import com.kook.common.utils.HVLog;
import com.kook.controller.client.hyxd.IHYXDController;
import com.kook.controller.server.IServerController;
import com.lody.virtual.helper.utils.Singleton;
import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HYXDControllerImpl extends IHYXDController.Stub implements ClientActivityLifecycle, Runnable {
   String TAG = "HYXDControllerImpl";
   public static final int VIEW_ACTION_WD = 1;
   private Queue<Unpack> queue = new ConcurrentLinkedQueue();
   Thread threadProcess;
   public boolean isInit = false;
   Activity mCurrentActivity;
   private static final Singleton<HYXDControllerImpl> sService = new Singleton<HYXDControllerImpl>() {
      protected HYXDControllerImpl create() {
         return new HYXDControllerImpl();
      }
   };

   public static HYXDControllerImpl get() {
      return (HYXDControllerImpl)sService.get();
   }

   public void setIServerController(Application application, IServerController serverController, IVirtualController virtualControllerImpl) {
   }

   public IVirtualController getVirtualControllerImpl() {
      return null;
   }

   public IServerController getIServerController() {
      return null;
   }

   public Activity getCurrentActivity() {
      return this.mCurrentActivity;
   }

   public Handler getHandler() {
      return null;
   }

   public void onActivityResumed(Activity activity) {
      this.mCurrentActivity = activity;
   }

   public void onActivityPaused(Activity activity) {
   }

   public void runOnUiThread(Runnable runnable) {
   }

   public boolean memorySRWData(String searchValue, String writeValue, boolean permission) throws RemoteException {
      if (!this.isInit) {
         File file = this.getCurrentActivity().getFilesDir().getParentFile();
         int callingPid = Binder.getCallingPid();
         boolean directory = file.isDirectory();
         Log.d("VA-NATIVE-HYXD", "path:" + file.getAbsolutePath() + "   directory：" + directory + "      pid:" + callingPid);
         HYXDNative.init(callingPid, file.getPath() + "/cacheResult");
         this.threadProcess = new Thread(this);
         this.threadProcess.start();
         this.isInit = true;
      }

      this.addUnpack(new Unpack(searchValue, writeValue, permission));
      return true;
   }

   public boolean memoryTest() throws RemoteException {
      if (!this.isInit) {
         File file = this.getCurrentActivity().getFilesDir().getParentFile();
         int callingPid = Binder.getCallingPid();
         boolean directory = file.isDirectory();
         Log.d("VA-NATIVE-HYXD", "path:" + file.getAbsolutePath() + "   directory：" + directory + "      pid:" + callingPid);
         HYXDNative.init(callingPid, file.getPath() + "/cacheResult");
         this.threadProcess = new Thread(this);
         this.threadProcess.start();
      }

      HYXDNative.memoryTest();
      return false;
   }

   public void addUnpack(Unpack unpack) {
      synchronized(this.queue) {
         this.queue.add(unpack);
         this.queue.notifyAll();
      }
   }

   public void run() {
      while(!Thread.interrupted()) {
         synchronized(this.queue) {
            try {
               if (null == this.queue || this.queue.size() == 0) {
                  this.queue.wait();
               }

               Unpack unpack = (Unpack)this.queue.poll();
               HVLog.d("查看整包数据结果 " + unpack.toString());
               HYXDNative.searchWrite(unpack.searchValue, unpack.writeValue, unpack.permission);
            } catch (Exception var4) {
               Exception e = var4;
               HVLog.printException(e);
            }
         }
      }

   }

   class Unpack {
      String searchValue;
      String writeValue;
      boolean permission;

      public Unpack(String searchValue, String writeValue, boolean permission) {
         this.searchValue = searchValue;
         this.writeValue = writeValue;
         this.permission = permission;
      }

      public String toString() {
         return "Unpack{searchValue=\'" + this.searchValue + '\'' + ", writeValue=\'" + this.writeValue + '\'' + ", permission=" + this.permission + '}';
      }
   }
}
