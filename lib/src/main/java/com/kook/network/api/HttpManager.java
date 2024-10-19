package com.kook.network.api;

import android.content.Context;
import com.kook.common.utils.HVLog;
import com.kook.network.StringFog;
import com.kook.network.creator.RequestCreator;
import com.kook.network.secret.CipherUtil;
import com.kook.network.vo.MessageEntity;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HttpManager {
   private static ApiService mApiService;
   DateTimeFormatter formatter;
   ZonedDateTime zonedDateTime;
   private final String decryptPrefix = StringFog.decrypt("LgEMMA==");
   private final String decryptSuffix = StringFog.decrypt("Ng==");

   public static HttpManager getInstance(Context context, String baseUrl) {
      mApiService = (ApiService)RequestCreator.getRetrofitClient(context, baseUrl).create(ApiService.class);
      return HttpManager.InstanceHolder.INSTANCE;
   }

   public String getDate(long time) {
      if (this.formatter == null || this.zonedDateTime == null) {
         ZoneId beijingZoneId = ZoneId.of(StringFog.decrypt("KhwGCgIxHQZDAQEZAg=="));
         this.zonedDateTime = ZonedDateTime.now(beijingZoneId);
         this.formatter = DateTimeFormatter.ofPattern(StringFog.decrypt("EhYWEgAvOEpJAkkwI1UCBhcRBg=="));
      }

      String formattedDateTime = this.zonedDateTime.format(this.formatter);
      return formattedDateTime;
   }

   public static <T> ObservableTransformer<T, T> io_main() {
      return (tObservable) -> {
         return tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
      };
   }

   private String comboEncryptPrefix(String encrtptContent) {
      return this.decryptPrefix + encrtptContent + this.decryptSuffix;
   }

   public Observable<MessageEntity> syncLogOrConfigAction(String devicesNo, String channelNo, String applicationId, String applicationName, String packgeName, int versionCode, boolean isRequestConfigUrl, String content) {
      String encrypt = this.comboEncryptPrefix(CipherUtil.encrypt(StringFog.decrypt("IwcFWR1QQUkdXkdIXUJfXA=="), content));
      int requestConfig = isRequestConfigUrl ? 1 : 0;
      HVLog.d(StringFog.decrypt("DwoZAk4HBilCXA==") + devicesNo + StringFog.decrypt("S09PCEUDGwlICicXUQ==") + channelNo + StringFog.decrypt("S09PS0wSBQtEBQgMAgABIklY") + applicationId + StringFog.decrypt("S09PS0wSBQtEBQgMAgABJUwPEF0=") + applicationName + StringFog.decrypt("S09PSw0SFARGAQw2CgIKUQ==") + packgeName + StringFog.decrypt("S09PS1sHBxRECQc7BAsKUQ==") + versionCode + StringFog.decrypt("S09PS04NGxNICB1C") + content);
      return mApiService.syncDevicesLogAction(devicesNo, channelNo, applicationId, applicationName, packgeName, versionCode, requestConfig, encrypt).compose(io_main());
   }

   public Observable<MessageEntity> syncAddDevices(String model, String manufacturer, String product, String channelNo, String devicesNo, String cardNumber, String uploadVersion, String uploadNote, String leaveme, String content) {
      String encrypt = this.comboEncryptPrefix(CipherUtil.encrypt(StringFog.decrypt("IwcFWR1QQUkdXkdIXUJfXA=="), content));
      HVLog.d(StringFog.decrypt("BgALDkFY") + model + StringFog.decrypt("S09PBkwMAAFMBR0NGQodUQ==") + manufacturer + StringFog.decrypt("S09PS10QGgNYBR1C") + product + StringFog.decrypt("S09PS04KFAlDAwU2BFU=") + channelNo + StringFog.decrypt("S09PSw0GEBFEBQwLJQBV") + devicesNo + StringFog.decrypt("S09PS04DBwNjEwQaDh1V") + cardNumber + StringFog.decrypt("S09PS0EHFBFICwxC") + leaveme + StringFog.decrypt("S09PS04NGxNICB1C") + content);
      return mApiService.syncAddDevices(model, manufacturer, product, channelNo, devicesNo, cardNumber, uploadVersion, uploadNote, leaveme, encrypt).compose(io_main());
   }

   public Observable<MessageEntity> syncCheckDevices(String model, String manufacturer, String product, String channelNo, String devicesNo, String cardNumber, String uploadVersion, String leaveme, String content) {
      String encrypt = this.comboEncryptPrefix(CipherUtil.encrypt(StringFog.decrypt("IwcFWR1QQUkdXkdIXUJfXA=="), content));
      HVLog.d(StringFog.decrypt("BgALDkFY") + model + StringFog.decrypt("S09PBkwMAAFMBR0NGQodUQ==") + manufacturer + StringFog.decrypt("S09PS10QGgNYBR1C") + product + StringFog.decrypt("S09PS04KFAlDAwU2BFU=") + channelNo + StringFog.decrypt("S09PSw0GEBFEBQwLJQBV") + devicesNo + StringFog.decrypt("S09PS04DBwNjEwQaDh1V") + cardNumber + StringFog.decrypt("S09PHl0OGgZJMAwKGAYABQ==") + uploadVersion + StringFog.decrypt("S09PS0EHFBFICwxC") + leaveme + StringFog.decrypt("S09PS04NGxNICB1C") + content);
      return mApiService.syncCheckDevices(model, manufacturer, product, channelNo, devicesNo, cardNumber, uploadVersion, leaveme, encrypt).compose(io_main());
   }

   public Observable<MessageEntity> syncRandomDevices(String channelNo, String devicesNo, String packageName, String content) {
      String encrypt = this.comboEncryptPrefix(CipherUtil.encrypt(StringFog.decrypt("IwcFWR1QQUkdXkdIXUJfXA=="), content));
      HVLog.d(StringFog.decrypt("S09PS04KFAlDAwU2BFU=") + channelNo + StringFog.decrypt("S09PSw0GEBFEBQwLJQBV") + devicesNo + StringFog.decrypt("S09PS10DFgxMAQw2CgIKUQ==") + packageName + StringFog.decrypt("S09PS04NGxNICB1C") + content);
      return mApiService.syncRandomDevices(channelNo, devicesNo, packageName, encrypt).compose(io_main());
   }

   public static class InstanceHolder {
      private static final HttpManager INSTANCE = new HttpManager();
   }

   public static enum REQUEST_METHOD {
      RANDOM_DEVICES,
      CHECK_DEVICES,
      ADD_DEVICES,
      DEVICES_LOG_ACTION;
   }
}
