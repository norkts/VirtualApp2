package com.carlos.common.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import com.carlos.common.ui.activity.base.VActivity;
import com.carlos.common.ui.adapter.bean.DeviceData;
import com.carlos.common.widget.MainFunBtn;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import com.kook.librelease.R;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.menu;
import com.kook.librelease.R.string;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VDeviceManager;
import com.lody.virtual.remote.VDeviceConfig;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DeviceDetailActiivty extends VActivity {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRguLmUVLCtqHiAgLwhSVg=="));
   private int mDeviceID;
   private String mPackageName;
   private String mTitle;
   private int mUserId;
   private int mPosition;
   private VDeviceConfig mDeviceConfig;
   private TelephonyManager mTelephonyManager;
   private WifiManager mWifiManager;
   private EditText edt_androidId;
   private EditText edt_imei;
   private EditText edt_imsi;
   private EditText edt_mac;
   private EditText edt_brand;
   private EditText edt_model;
   private EditText edt_name;
   private EditText edt_device;
   private EditText edt_board;
   private EditText edt_display;
   private EditText edt_id;
   private EditText edt_serial;
   private EditText edt_manufacturer;
   private EditText edt_fingerprint;
   private EditText edt_bluetooth_name;
   MainFunBtn randomData;

   public static void open(Activity context, DeviceData data, int position) {
      Intent intent = new Intent(context, DeviceDetailActiivty.class);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgYLGoFNFo=")), data.name);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhhbPQ==")), data.packageName);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28jSFo=")), data.userId);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAKQ==")), position);
      context.startActivityForResult(intent, 1001);
   }

   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.setResult(0);
      this.setContentView(layout.activity_mock_device);
      Toolbar toolbar = (Toolbar)this.bind(id.top_toolbar);
      this.setSupportActionBar(toolbar);
      this.enableBackHome();
      this.edt_androidId = (EditText)this.findViewById(id.edt_androidId);
      this.edt_imei = (EditText)this.findViewById(id.edt_imei);
      this.edt_imsi = (EditText)this.findViewById(id.edt_imsi);
      this.edt_mac = (EditText)this.findViewById(id.edt_mac);
      this.edt_brand = (EditText)this.findViewById(id.edt_brand);
      this.edt_model = (EditText)this.findViewById(id.edt_model);
      this.edt_name = (EditText)this.findViewById(id.edt_name);
      this.edt_device = (EditText)this.findViewById(id.edt_device);
      this.edt_board = (EditText)this.findViewById(id.edt_board);
      this.edt_display = (EditText)this.findViewById(id.edt_display);
      this.edt_id = (EditText)this.findViewById(id.edt_id);
      this.edt_serial = (EditText)this.findViewById(id.edt_serial);
      this.edt_manufacturer = (EditText)this.findViewById(id.edt_manufacturer);
      this.edt_fingerprint = (EditText)this.findViewById(id.edt_fingerprint);
      this.edt_bluetooth_name = (EditText)this.findViewById(id.edt_bluetooth_name);
      this.mWifiManager = (WifiManager)this.getApplicationContext().getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4YPmUVSFo=")));
      this.mTelephonyManager = (TelephonyManager)this.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhhfD2ojNFo=")));
      if (TextUtils.isEmpty(this.mTitle)) {
         this.mPackageName = this.getIntent().getStringExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhhbPQ==")));
         this.mUserId = this.getIntent().getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28jSFo=")), 0);
         this.mTitle = this.getIntent().getStringExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgYLGoFNFo=")));
      }

      this.mDeviceID = getDeviceId(this.mPackageName, this.mUserId);
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwYqM2wjAiliDAYWDCJYGg==")) + this.mDeviceID + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OGoYNANiASwJKF8IVg==")) + this.mUserId + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsFEkx9DigxLwc6PWcVQSNrClFF")) + this.mPackageName);
      this.setTitle(this.mTitle);
      this.setTitleColor(-1);
      this.mDeviceConfig = VDeviceManager.get().getDeviceConfig(this.mDeviceID);
      this.updateConfig();
      this.randomData = (MainFunBtn)this.findViewById(id.main_fun_btn);
      this.randomData.setTopText(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcEkNNPSZYAzEsAgpYIA==")));
      this.randomData.setOnClickListener((view) -> {
         this.mDeviceConfig = VDeviceConfig.random();
         this.updateConfig();
      });
   }

   protected void onNewIntent(Intent intent) {
      super.onNewIntent(intent);
      this.mPackageName = intent.getStringExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhhbPQ==")));
      this.mUserId = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28jSFo=")), 0);
      this.mTitle = intent.getStringExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgYLGoFNFo=")));
      this.mPosition = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAKQ==")), -1);
   }

   public boolean onCreateOptionsMenu(Menu menu) {
      this.getMenuInflater().inflate(R.menu.menu_device, menu);
      return true;
   }

   private void killApp() {
      if (TextUtils.isEmpty(this.mPackageName)) {
         VirtualCore.get().killAllApps();
      } else {
         VirtualCore.get().killApp(this.mPackageName, this.mUserId);
      }

   }

   public boolean onOptionsItemSelected(MenuItem item) {
      if (item.getItemId() == id.action_save) {
         this.mDeviceConfig.enable = true;
         this.fillConfig();
         this.updateConfig();
         VDeviceManager.get().updateDeviceConfig(this.mDeviceID, this.mDeviceConfig);
         Intent intent = new Intent();
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhhbPQ==")), this.mPackageName);
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28jSFo=")), this.mUserId);
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAKQ==")), this.mPosition);
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKWwVHgY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+LmgVSFo=")));
         this.setResult(-1, intent);
         if (TextUtils.isEmpty(this.mPackageName)) {
            VirtualCore.get().killAllApps();
         } else {
            VirtualCore.get().killApp(this.mPackageName, this.mUserId);
         }

         this.killApp();
         Toast.makeText(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1YdHUZaH1VYAB8CAhkFHQ==")), 0).show();
      } else if (item.getItemId() == id.action_reset) {
         (new AlertDialog.Builder(this)).setMessage(string.dlg_reset_device).setPositiveButton(17039370, (dialog, which) -> {
            this.mDeviceConfig.enable = false;
            this.mDeviceConfig.clear();
            VDeviceManager.get().updateDeviceConfig(this.mDeviceID, this.mDeviceConfig);
            Intent intent = new Intent();
            intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhhbPQ==")), this.mPackageName);
            intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28jSFo=")), this.mUserId);
            intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAKQ==")), this.mPosition);
            intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKWwVHgY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKWgaMFo=")));
            this.setResult(-1, intent);
            this.killApp();
            this.updateConfig();
         }).setNegativeButton(17039360, (dialog, which) -> {
            dialog.dismiss();
         }).setCancelable(false).show();
      }

      return super.onOptionsItemSelected(item);
   }

   private String getValue(EditText text) {
      return text.getText().toString().trim();
   }

   private void setValue(EditText text, String value, String defValue) {
      if (TextUtils.isEmpty(value)) {
         text.setText(defValue);
      } else {
         text.setText(value);
      }
   }

   private void fillConfig() {
      this.mDeviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjsMEWIhMFo=")), this.getValue(this.edt_brand));
      this.mDeviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwYAWWAbHlo=")), this.getValue(this.edt_model));
      this.mDeviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IhUMUmAINBNuEVRF")), this.getValue(this.edt_name));
      this.mDeviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYuAH0bLBU=")), this.getValue(this.edt_device));
      this.mDeviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjwAEWchMFo=")), this.getValue(this.edt_board));
      this.mDeviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYYA2cLHhFvAVRF")), this.getValue(this.edt_display));
      this.mDeviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAYqVg==")), this.getValue(this.edt_id));
      this.mDeviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwY+U2QbOBFlJQpKOzsMAg==")), this.getValue(this.edt_manufacturer));
      this.mDeviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jj4EI2gaMCVgJwo0Ij0iD2kjSFo=")), this.getValue(this.edt_bluetooth_name));
      this.mDeviceConfig.setProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwYU2AxNF9pHywJIjw2Vg==")), this.getValue(this.edt_fingerprint));
      this.mDeviceConfig.serial = this.getValue(this.edt_serial);
      this.mDeviceConfig.deviceId = this.getValue(this.edt_imei);
      this.mDeviceConfig.iccId = this.getValue(this.edt_imsi);
      this.mDeviceConfig.wifiMac = this.getValue(this.edt_mac);
      this.mDeviceConfig.androidId = this.getValue(this.edt_androidId);
   }

   @SuppressLint({"HardwareIds", "MissingPermission"})
   private void updateConfig() {
      this.setValue(this.edt_brand, this.mDeviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjsMEWIhMFo="))), Build.BRAND);
      this.setValue(this.edt_model, this.mDeviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwYAWWAbHlo="))), Build.MODEL);
      this.setValue(this.edt_name, this.mDeviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IhUMUmAINBNuEVRF"))), Build.PRODUCT);
      this.setValue(this.edt_device, this.mDeviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYuAH0bLBU="))), Build.DEVICE);
      this.setValue(this.edt_board, this.mDeviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JjwAEWchMFo="))), Build.BOARD);
      this.setValue(this.edt_display, this.mDeviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRYYA2cLHhFvAVRF"))), Build.DISPLAY);
      this.setValue(this.edt_id, this.mDeviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAYqVg=="))), Build.ID);
      this.setValue(this.edt_manufacturer, this.mDeviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwY+U2QbOBFlJQpKOzsMAg=="))), Build.MANUFACTURER);
      this.setValue(this.edt_fingerprint, this.mDeviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwYU2AxNF9pHywJIjw2Vg=="))), Build.FINGERPRINT + this.mUserId);
      String bluetoothName = Secure.getString(this.getContentResolver(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4EI2gaMCVgJwo0Ji0YOW8jGlo=")));
      this.setValue(this.edt_bluetooth_name, this.mDeviceConfig.getProp(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jj4EI2gaMCVgJwo0Ij0iD2kjSFo="))), bluetoothName);
      this.setValue(this.edt_serial, this.mDeviceConfig.serial, Build.SERIAL);

      try {
         this.setValue(this.edt_imei, this.mDeviceConfig.deviceId, this.mTelephonyManager.getDeviceId());
      } catch (Throwable var4) {
         this.setValue(this.edt_imei, this.mDeviceConfig.deviceId, "");
      }

      try {
         this.setValue(this.edt_imsi, this.mDeviceConfig.iccId, this.mTelephonyManager.getSimSerialNumber());
      } catch (Throwable var3) {
         this.setValue(this.edt_imsi, this.mDeviceConfig.iccId, "");
      }

      this.setValue(this.edt_mac, this.mDeviceConfig.wifiMac, this.getDefaultWifiMac());
      this.setValue(this.edt_androidId, this.mDeviceConfig.androidId, Secure.getString(this.getContentResolver(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iHx4zKBhSVg=="))));
   }

   @SuppressLint({"HardwareIds"})
   private String getDefaultWifiMac() {
      String[] files = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My02J283GilgHiApIylfDmkgASVvJwIsLCk5KX0KFi9lNAo8LAhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My02J283GilgHiApIylfDmkgASVrDiwZOQQAO2IaFjVuASw8")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My02J283GilgHiApIylfDmkgASVvJx4tI18AO2IaFjVuASw8"))};
      String mac = this.mWifiManager.getConnectionInfo().getMacAddress();
      if (TextUtils.isEmpty(mac)) {
         String[] var3 = files;
         int var4 = files.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String file = var3[var5];

            try {
               mac = this.readFileAsString(file);
            } catch (IOException var8) {
               IOException e = var8;
               e.printStackTrace();
            }

            if (!TextUtils.isEmpty(mac)) {
               break;
            }
         }
      }

      return mac;
   }

   private String readFileAsString(String filePath) throws IOException {
      StringBuilder sb = new StringBuilder(1000);
      BufferedReader reader = new BufferedReader(new FileReader(filePath));
      char[] buf = new char[1024];

      int numRead;
      while((numRead = reader.read(buf)) != -1) {
         String readData = String.valueOf(buf, 0, numRead);
         sb.append(readData);
      }

      reader.close();
      return sb.toString().trim();
   }
}
