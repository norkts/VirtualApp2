package com.carlos.common.ui.adapter.bean;

import android.content.Context;
import com.lody.virtual.client.ipc.VDeviceManager;
import com.lody.virtual.remote.InstalledAppInfo;

public class DeviceData extends SettingsData {
   public DeviceData(Context context, InstalledAppInfo installedAppInfo, int userId) {
      super(context, installedAppInfo, userId);
   }

   public boolean isMocking() {
      return VDeviceManager.get().isEnable(this.userId);
   }
}
