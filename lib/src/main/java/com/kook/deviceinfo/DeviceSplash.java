package com.kook.deviceinfo;

import android.app.Activity;
import androidx.annotation.RequiresApi;
import com.carlos.common.network.VNetworkManagerService;
import com.kook.common.utils.HVLog;
import com.kook.deviceinfo.impClasses.ExportDetails;
import com.kook.deviceinfo.impClasses.ExportThread;
import com.kook.deviceinfo.persistence.IniFile;
import java.io.File;

public class DeviceSplash {
   @RequiresApi(
      api = 26
   )
   public void attachBaseApplication(Activity context) {
      DeviceApplication.get().startup(context.getApplication());
      ExportThread exportThread = ExportThread.get(context);
      ExportDetails exportDetails = exportThread.getExportDetails();
      IniFile iniFile = exportDetails.getIniFile();
      File file = iniFile.getFile();
      if (file.exists() && file.length() > 10L) {
         HVLog.d("iniFile 存在:");
         VNetworkManagerService networkManagerService = VNetworkManagerService.get();
         networkManagerService.systemReady(context);
         networkManagerService.checkDevicesUpload(file.getAbsolutePath());
      } else {
         HVLog.d("iniFile 开始生成:");
         exportThread.start();
      }

   }
}
