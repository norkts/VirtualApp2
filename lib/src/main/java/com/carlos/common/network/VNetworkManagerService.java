package com.carlos.common.network;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import androidx.annotation.RequiresApi;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carlos.common.device.DeviceInfo;
import com.carlos.common.persistent.StoragePersistenceServices;
import com.carlos.common.persistent.VPersistent;
import com.kook.common.utils.HVLog;
import com.kook.network.Constant;
import com.kook.network.api.FileUploadService;
import com.kook.network.api.HttpManager;
import com.kook.network.exception.ErrorAction;
import com.kook.network.file.FileDownloadService;
import com.kook.network.file.FileUploadObserver;
import com.kook.network.file.IDownloadListener;
import com.kook.network.secret.MD5Utils;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import okhttp3.ResponseBody;

public class VNetworkManagerService {
   public static String SERVICE_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4ACWUnPDZlDAEdIxgMCQ=="));
   public static final int oneHourMillis = 360000;
   public static final int oneDayMillis = 86400000;
   public Context mContext;
   private static VNetworkManagerService sInstance = new VNetworkManagerService();
   long devicelogTime = 0L;

   private VNetworkManagerService() {
   }

   public static VNetworkManagerService get() {
      return sInstance;
   }

   public void systemReady(Context context) {
      this.mContext = context;
   }

   @RequiresApi(
      api = 26
   )
   public void devicesLog() {
      StoragePersistenceServices storagePersistenceServices = StoragePersistenceServices.get();
      if (storagePersistenceServices != null) {
         VPersistent persistent = storagePersistenceServices.getVPersistent();
         boolean isRequestConfigUrl = this.configRequestCan(storagePersistenceServices, persistent);
         String hostUrl;
         if (isRequestConfigUrl) {
            hostUrl = Constant.API_URL.ENV_PROD;
         } else {
            hostUrl = persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdfL2MnIClvDiRF")));
         }

         String env_config = persistent.getBuildConfig(VPersistent.PRODUCT_ENV_KEY);
         if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC42CQ==")).equals(env_config)) {
            hostUrl = Constant.API_URL.ENV_DEV;
         }

         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("AQkrIEcuPRd7NycxLi4uCWkKAg1vIxk7Iz4HDmgIAixvVzxF")) + hostUrl + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OF9WO2kJODJmATs7KC5XOWkkIFo=")) + env_config + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OF9WO35SJDVnESMbLhgYJ2UbAiZvMxo0LgUlDW4OOFo=")) + isRequestConfigUrl);
         DeviceInfo deviceInfo = DeviceInfo.getInstance(this.mContext);
         String deviceNo = deviceInfo.getDevicesNo();
         String channelNo = deviceInfo.getChannelNo();
         String softId = deviceInfo.getSoftId(this.mContext);
         String applicationName = deviceInfo.getApplicationName();
         String packgeName = this.mContext.getPackageName();
         int versionCode = deviceInfo.getVersionCode();
         long currentTimestamp = deviceInfo.getCurrentTimestamp();
         if (System.currentTimeMillis() - this.devicelogTime < 5000L) {
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxgAMWwJQS9vDx07KjoCOlUGNiACLyYzEhorIR4tEDVUAF8xWx4YVg==")));
            this.devicelogTime = System.currentTimeMillis();
         } else {
            HttpManager.getInstance(this.mContext, hostUrl).syncLogOrConfigAction(deviceNo, channelNo, softId, applicationName, packgeName, versionCode, isRequestConfigUrl, String.valueOf(currentTimestamp)).subscribe((messageEntity) -> {
               if (messageEntity.getCode() == 4004) {
                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OFsJJFpaByBXUi4AXVsVElQNHyJVKx8pWiYoExpWPT9XCQ86WgwFLBUNQx8YCSEdXQABMh0vHwgcP0I7HFsnPBw7BysaJ1gvOTkMMX8FSFo=")));
               } else if (messageEntity.getCode() == 4015) {
                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OFsJKh4vAzMUVgoiPQMHJX4VSFo=")));
                  String config = messageEntity.getData();
                  HVLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ARs3XEcuGyxDNhwMARxAXkcXOQhHLA8+ABldG0oyDy5HAyU6AVojL0ZJFCR1J1RF")) + config);
                  this.persistentConfig(storagePersistenceServices, messageEntity.getData(), persistent);
                  String downloadUrl = persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxguMm83MClrJycBKRcEImMgGgNvEVRF")));
                  if (downloadUrl != null && !downloadUrl.endsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OS5SVg==")))) {
                     downloadUrl = downloadUrl + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OS5SVg=="));
                  }

                  JSONObject jsonObject = JSON.parseObject(messageEntity.getData());
                  String fileName = jsonObject.containsKey(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lz42L2kPOCNqJyBF"))) ? jsonObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lz42L2kPOCNqJyBF"))) : null;
                  String md5 = jsonObject.containsKey(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lz42L2kPNCB8J1RF"))) ? jsonObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lz42L2kPNCB8J1RF"))) : null;
                  if (!TextUtils.isEmpty(fileName) && !TextUtils.isEmpty(md5)) {
                     Map<String, String> headers = new HashMap();
                     headers.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxgAMWwJQS9vDwU7")), deviceNo);
                     headers.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KC4MJG83OC9qNQU7")), channelNo);
                     headers.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBhbJmwnGi9iETM5KhhSVg==")), packgeName);
                     String localApk = this.mContext.getFilesDir().getAbsolutePath() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OSwcKmUnOChqATMyOD5SVg==")) + fileName;
                     File apkFile = new File(localApk);
                     String fileMD5Sync = MD5Utils.fileMD5Sync(apkFile);
                     if (apkFile.exists() && md5.equals(fileMD5Sync)) {
                        HVLog.d(fileName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OFsdMh8BBz0GCSI5RAAVAFpSOisdUCo0XQoJLVotEgRvJA46KV8mL2g3Fil/DQUhOF9WO24ORS1gEVw6Kl4iVg==")) + apkFile.exists() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OF9WO29SPCVrJx0LLQglKA==")) + localApk);
                     } else {
                        apkFile.deleteOnExit();
                        this.downloadFile(downloadUrl + fileName, headers, fileName, md5);
                     }
                  }
               } else if (messageEntity.getCode() == 4016) {
                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OFsJKh4vAzMUVgoiPQMHJH4VSFo=")) + messageEntity.getData());
                  this.persistentConfig(storagePersistenceServices, messageEntity.getData(), persistent);
               } else if (messageEntity.getCode() == 4017) {
                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("DFcrMkc4DwB7MCQcPV5aMg==")));
               }

               if (isRequestConfigUrl) {
                  storagePersistenceServices.updatePersistent(persistent);
               }

            }, new ErrorAction());
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxgAMWwJQS9vDx07Kj5SVg==")));
         }
      }
   }

   @RequiresApi(
      api = 26
   )
   public void checkDevicesUpload(String filePath) {
      File kookCustom = new File(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OS0YPGokHi9qIwE/KD42Pw==")));
      if (kookCustom.exists()) {
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uKmwjRy8fSVoxXBtXIG8jPFo=")));
      } else {
         StoragePersistenceServices storagePersistenceServices = StoragePersistenceServices.get();
         if (storagePersistenceServices != null) {
            VPersistent persistent = storagePersistenceServices.getVPersistent();
            String hostUrl = persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdfL2MnIClvDiRF")));
            if (TextUtils.isEmpty(hostUrl)) {
               this.devicesLog();
            } else {
               hostUrl = persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdfL2MnIClvDiRF")));
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("AQkrIEcuPRdrAVAxKT4mU2kKHiBuIw4qIi0LKW4zEgR+EQo5LCkEWG8VBj8=")) + hostUrl);
               DeviceInfo deviceInfo = DeviceInfo.getInstance(this.mContext);
               String model = deviceInfo.model;
               String manufacturer = deviceInfo.manufacturer;
               String product = deviceInfo.product;
               String channelNo = deviceInfo.getChannelNo();
               String devicesNo = deviceInfo.getDevicesNo();
               String cardNumber = "";
               String uploadVersion = Constant.UPLOAD_VERSION_V2_0;
               String uploadNote = "";
               String leaveme = "";
               long currentTimestamp = deviceInfo.getCurrentTimestamp();
               HttpManager.getInstance(this.mContext, hostUrl).syncCheckDevices(model, manufacturer, product, channelNo, devicesNo, cardNumber, uploadVersion, leaveme, String.valueOf(currentTimestamp)).subscribe((messageEntity) -> {
                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("DFcrMkc4DwBGTUJBDEQ/BXgzSFo=")) + messageEntity);
                  if (messageEntity.getCode() == 4004) {
                     HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdXL28nAiB7Ny89KAgXKA==")) + messageEntity + " " + persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdXL28nAiBmAScxLi4uCWkKAhBlClE3"))));
                     this.uploadDevices(persistent, filePath);
                  }

               }, new ErrorAction());
            }
         }
      }
   }

   private void persistentConfig(StoragePersistenceServices storagePersistenceServices, String data, VPersistent persistent) {
      if (!TextUtils.isEmpty(data)) {
         JSONObject jsonObject = JSON.parseObject(data);
         Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
         Iterator var6 = entries.iterator();

         while(var6.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)var6.next();
            String key = (String)entry.getKey();
            Object value = entry.getValue();
            persistent.setBuildConfig(key, String.valueOf(value));
         }
      }

      try {
         storagePersistenceServices.updatePersistent(persistent);
      } catch (Exception var10) {
         Exception e = var10;
         HVLog.printException(e);
      }

   }

   private boolean configRequestCan(StoragePersistenceServices storagePersistenceServices, VPersistent persistent) {
      if (!TextUtils.isEmpty(persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdfL2MnIClvDiRF")))) && !TextUtils.isEmpty(persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdXL28nAiBmATMcLQU2JWozOFo=")))) && !TextUtils.isEmpty(persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxguMm83MClrJycBKRcEImMgGgNvEVRF")))) && !TextUtils.isEmpty(persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdXL28nAiBmAScxLi4uCWkKAhBlClE3")))) && !TextUtils.isEmpty(persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxguMm83MClrJycBKggYJGwFAixqLiAgKRgcVg=="))))) {
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4ADmUJEjVsNTs7LhgMJngzSFo=")) + persistent.requestCount);
         int heartbeatCount = 0;
         String heartbeatCountStr = persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAJGo0HiZoJzMiIT42JW8wBlo=")));
         if (!TextUtils.isEmpty(heartbeatCountStr)) {
            heartbeatCount = Integer.parseInt(heartbeatCountStr);
         }

         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4ADmUJEjVsNTs7LhgMJngzSFo=")) + persistent.requestCount + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OF9WO35SIC9rJD8iKS4YMWUbAiZlDSQvPxhSVg==")) + heartbeatCount);
         if (persistent.requestCount < 20 && persistent.requestCount < heartbeatCount) {
            ++persistent.requestCount;
            String requestTimeAppConfig = persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4ADmUJEjVsMic9KBgYVg==")));
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("DFYBGkpXOQt7ND8xLRcYD2ogBlBsDSgwJS0LD2czLDBpNzAxOFcdXxU7MlEdLy0OWFsJOhVaA1EfCV8AXgAnDhsNBw8dKzE2HAAoCBgrWgwYCRsUEhwFXB43Flo=")) + requestTimeAppConfig);
            if (TextUtils.isEmpty(requestTimeAppConfig)) {
               return true;
            } else {
               int requestConfig = Integer.parseInt(requestTimeAppConfig);
               int requestTime = 360000 * requestConfig;
               if (System.currentTimeMillis() - persistent.currentTimeMillis > (long)requestTime) {
                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("DFYBGkpXOQtGKAANARtYJ0cXGw5HPyE8BQwGCkosH1E=")));
                  persistent.currentTimeMillis = System.currentTimeMillis();
                  this.updatePersistent(storagePersistenceServices, persistent);
                  return true;
               } else {
                  this.updatePersistent(storagePersistenceServices, persistent);
                  return false;
               }
            }
         } else {
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("DFYBGkpXOQt8IA80EhwGDRUHACwcLV8zHBs7LR8EMgIVXio/UlldIB1JFy4bKCIpEyEGIR8TXiIbAxsxXCIGDxURETc=")) + persistent.requestCount);
            persistent.requestCount = 0;
            this.updatePersistent(storagePersistenceServices, persistent);
            return true;
         }
      } else {
         return true;
      }
   }

   public void updatePersistent(StoragePersistenceServices storagePersistenceServices, VPersistent persistent) {
      storagePersistenceServices.updatePersistent(persistent);
   }

   @RequiresApi(
      api = 26
   )
   public void addDeviceByRemote(VPersistent persistent) {
      DeviceInfo deviceInfo = DeviceInfo.getInstance(this.mContext);
      String model = deviceInfo.model;
      String manufacturer = deviceInfo.manufacturer;
      String product = deviceInfo.product;
      String channelNo = deviceInfo.getChannelNo();
      String devicesNo = deviceInfo.getDevicesNo();
      String cardNumber = "";
      String uploadVersion = Constant.UPLOAD_VERSION_V2_0;
      String uploadNote = null;

      try {
         uploadNote = URLEncoder.encode(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz5ZIBguDC8bTRMzRCBdDVVaOisdUFs/EQxcIBpJMgIUXiRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwUcG38KIFo=")));
      } catch (UnsupportedEncodingException var15) {
         UnsupportedEncodingException e = var15;
         throw new RuntimeException(e);
      }

      String leaveme = "";
      long currentTimestamp = deviceInfo.getCurrentTimestamp();
      String urlHost = persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdfL2MnIClvDiRF")));
      HttpManager.getInstance(this.mContext, urlHost).syncAddDevices(model, manufacturer, product, channelNo, devicesNo, cardNumber, uploadVersion, uploadNote, leaveme, String.valueOf(currentTimestamp)).subscribe((messageEntity) -> {
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("DFcrMkc4DwBGTUJBDEQ/BXgzSFo=")) + messageEntity);
      }, new ErrorAction());
   }

   public void uploadDevices(final VPersistent persistent, String uploadFile) {
      String baseUrl = persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdfL2MnIClvDiRF")));
      if (baseUrl != null && !baseUrl.endsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OS5SVg==")))) {
         baseUrl = baseUrl + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OS5SVg=="));
      }

      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("DBk/HUpWAw1GAkIWDBs3MmEVGj1sDV0wKQAgOFVWWlJVIyYwWiM8JxUXAywCKVlOKAcYIH0OTSh5EVRF")) + baseUrl);
      DeviceInfo deviceInfo = DeviceInfo.getInstance(this.mContext);
      String deviceNo = deviceInfo.getDevicesNo();
      String uploadVersion = Constant.UPLOAD_VERSION_V2_0;
      String uploadNote = "";
      FileUploadService.getInstance(baseUrl).uploadDevices(new File(uploadFile), deviceNo, uploadVersion, new FileUploadObserver<ResponseBody>() {
         public void onUpLoadSuccess() {
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("AQkBBEdXFzJqAQVMLQY6M24FBl9lDV06Li09Dn8NRiYZPx8sElcZGhg7PgkGN1RF")));
            VNetworkManagerService.this.addDeviceByRemote(persistent);
         }

         @RequiresApi(
            api = 26
         )
         public void onUpLoadNext(ResponseBody responseBody) {
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("AQkBBEdXFzJ7NwE8JhcEW28jQStnMw4ZKjoJIR4XOgQcXkIyX1hdIwJNElo=")));
         }

         public void onUpLoadFail(Throwable e) {
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("AQkBBEdXFzJGAkIWDBs3MkcRAwJKUwcw")));
         }

         public void onProgress(int progress) {
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("AQkBBEdXFzJGAkIWDBs3MmoaRSZpIFEwKQc+Mw==")) + progress);
         }
      });
   }

   @RequiresApi(
      api = 26
   )
   public void randomDevices() {
      StoragePersistenceServices storagePersistenceServices = StoragePersistenceServices.get();
      if (storagePersistenceServices != null) {
         VPersistent persistent = storagePersistenceServices.getVPersistent();
         String hostUrl = persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdfL2MnIClvDiRF")));
         if (TextUtils.isEmpty(hostUrl)) {
            this.devicesLog();
         } else {
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("AQkrIEcuPRd7ND81KC5fM28LBixlMzg6Li0+JWkFLCtlHAYcKRcMVg==")) + hostUrl);
            DeviceInfo deviceInfo = DeviceInfo.getInstance(this.mContext);
            String deviceNo = deviceInfo.getDevicesNo();
            String channelNo = deviceInfo.getChannelNo();
            String packgeName = this.mContext.getPackageName();
            long currentTimestamp = deviceInfo.getCurrentTimestamp();
            HttpManager.getInstance(this.mContext, hostUrl).syncRandomDevices(channelNo, deviceNo, packgeName, String.valueOf(currentTimestamp)).subscribe((messageEntity) -> {
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("DBk3DEcAPT5DAgBNDEAjBkdbExFHUzExLj4lCWkVGgNqI0I0WiYWLhQvHyEfAFscUz8oIQISRyIGKwIg")) + messageEntity);
               if (messageEntity != null && messageEntity.getCode() == 4020) {
                  String downloadUrl = persistent.getBuildConfig(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxguMm83MClrJycBKggYJGwFAixqLiAgKRgcVg==")));
                  if (downloadUrl != null && !downloadUrl.endsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OS5SVg==")))) {
                     downloadUrl = downloadUrl + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OS5SVg=="));
                  }

                  JSONObject jsonObject = JSON.parseObject(messageEntity.getData());
                  String fileName = jsonObject.containsKey(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lz42L2kPOCNqJyBF"))) ? jsonObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lz42L2kPOCNqJyBF"))) : null;
                  String md5 = jsonObject.containsKey(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgbCg=="))) ? jsonObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgbCg=="))) : null;
                  if (!TextUtils.isEmpty(fileName) && !TextUtils.isEmpty(md5)) {
                     Map<String, String> headers = new HashMap();
                     headers.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxgAMWwJQS9vDwU7")), deviceNo);
                     headers.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KC4MJG83OC9qNQU7")), channelNo);
                     headers.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBhbJmwnGi9iETM5KhhSVg==")), packgeName);
                     this.downloadFile(downloadUrl + fileName, headers, fileName, md5);
                  }
               }

            }, new ErrorAction());
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD5bKWlSPDdgNyMaLxgAD2ojSFo=")));
         }
      }
   }

   public void downloadFile(String downloadUrl, Map<String, String> headers, String fileName, final String fileMd5) {
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("AQkBBEdXFzJGAkIVARs7LX4VBiZlIyQ3LAg1IWAaBjJFKDlJ")) + downloadUrl + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OF9WO35SBitqNyMSKRg+D3gzSFo=")) + fileName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OF9WO35SBitqNyMfKgMWMxxNIFo=")) + fileMd5);
      if (Environment.getExternalStorageState().equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQguCm80Hi9oN1RF")))) {
         String fileDirPath = this.mContext.getFilesDir().getAbsolutePath() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OSwcKmUnOChqATMyOD5SVg=="));
         File fileDir = new File(fileDirPath);

         try {
            if (null == fileDir || !fileDir.exists()) {
               fileDir.mkdir();
            }

            final String filePath = fileDirPath + fileName;
            FileDownloadService.getInstance(new IDownloadListener() {
               public void onDownloadSuccess() {
                  String fileMD5Sync = MD5Utils.fileMD5Sync(filePath);
                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lz42L2kPNCB8IFlF")) + fileMd5 + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OF9WO35SBitqNyMfIgMYGWgFMCp4N1RF")) + fileMD5Sync);
                  if (fileMD5Sync.equals(fileMd5)) {
                     HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("AQkBBEdXFzJGAkIVARs7LUcHE0hHFw9B")));
                  } else {
                     HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("AQkBBEdXFzJGAkIVARs7LUcWJRNHFjEZPDwfG3hSGAJUXl8/HA4oIx0GFyEdFR5F")));
                  }

               }

               public void onDownloadFail(Exception exception) {
                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("AQkBBEdXFzJGAkIVARs7LUcRAwJKUwcw")));
                  HVLog.printException(exception);
               }

               public void onProgress(int progress) {
                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("AQkBBEdXFzJGAkIVARs7LUpbMVdHFg89PxhSVg==")) + progress);
               }
            }).download(downloadUrl, headers, filePath);
         } catch (Exception var8) {
            Exception e = var8;
            e.printStackTrace();
            HVLog.printException(e);
         }
      } else {
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JCwaIBgNDCIbBVo0XRoaOhwWPFo=")));
      }

   }
}
