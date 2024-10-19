package com.kook.deviceinfo.impClasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.carlos.common.network.VNetworkManagerService;
import com.kook.common.systemutil.SystemManager;
import com.kook.common.utils.HVLog;
import com.kook.deviceinfo.impClassMethods.InputDeviceMethod;
import com.kook.deviceinfo.impClassMethods.SensorListMethod;
import com.kook.deviceinfo.models.InputModel;
import com.kook.deviceinfo.models.SensorListModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExportThread extends Thread {
   public static ExportDetails exportDetails;
   private static Context context;
   private static int color;
   SensorListMethod sensorListMethod;
   InputDeviceMethod inputDeviceMethod;
   ArrayList<InputModel> inputList;
   ArrayList<SensorListModel> sensorList;

   private ExportThread(Context context) {
      ExportThread.context = context;
      this.sensorListMethod = new SensorListMethod(context);
      this.inputDeviceMethod = new InputDeviceMethod(context);
      exportDetails = new ExportDetails(context);
      this.inputList = this.inputDeviceMethod.getInputList();
      HVLog.d(" 输入法数量:" + this.inputList.size());
      this.sensorList = this.sensorListMethod.getSensorList();
      HVLog.d(" 传感器数量:" + this.sensorList.size());
   }

   public static ExportThread get(Context context) {
      ExportThread exportThread = new ExportThread(context);
      return exportThread;
   }

   public ExportDetails getExportDetails() {
      exportDetails.setSensorList(this.sensorList);
      exportDetails.setInputList(this.inputList);
      return exportDetails;
   }

   @SuppressLint({"inflateParams"})
   public void run() {
      super.run();
      exportDetails.device();
      exportDetails.system();
      exportDetails.cpu();
      exportDetails.battery();
      exportDetails.display();
      exportDetails.memory();
      exportDetails.cameraApi21();
      exportDetails.inputDevices();
      exportDetails.codecs();
      exportDetails.deviceFeatures();
      exportDetails.drmDetails();
      exportDetails.systemProperty();
      exportDetails.javaProperty();
      exportDetails.settingsProperty();
      exportDetails.netlink();
      exportDetails.fingerprintFile();
      exportDetails.generalDataInfo();
      exportDetails.systemFileInfo();
      exportDetails.systemAppInfo();
      exportDetails.userAgent();
      exportDetails.systemSensorInfo();
      exportDetails.systemInputInfo();
      ((Activity)context).runOnUiThread(new Runnable() {
         @RequiresApi(
            api = 26
         )
         public void run() {
            BufferedWriter bufferedWriter = null;

            try {
               if (!SystemManager.isSystemSign(ExportThread.context)) {
                  File inifile = ExportThread.exportDetails.getIniFile().save();
                  VNetworkManagerService networkManagerService = VNetworkManagerService.get();
                  networkManagerService.systemReady(ExportThread.context);
                  networkManagerService.checkDevicesUpload(inifile.getAbsolutePath());
               } else {
                  HVLog.d("当前是定制系统，数据不上传");
               }
            } catch (Exception var12) {
               Exception e = var12;
               Toast.makeText(ExportThread.context.getApplicationContext(), "Unable to save...", 0).show();
               e.printStackTrace();
            } finally {
               if (bufferedWriter != null) {
                  try {
                     ((BufferedWriter)bufferedWriter).flush();
                  } catch (IOException var11) {
                     IOException ex = var11;
                     ex.printStackTrace();
                  }
               }

            }

         }
      });
   }

   private boolean isValidActivity() {
      if (context instanceof Activity) {
         Activity activity = (Activity)context;
         return !activity.isDestroyed() && !activity.isFinishing();
      } else {
         return false;
      }
   }
}
