package com.kook.network.api;

import android.text.TextUtils;
import android.util.ArrayMap;
import com.alibaba.fastjson.JSON;
import com.kook.common.utils.HVLog;
import com.kook.network.StringFog;
import com.kook.network.creator.FileUploadCreator;
import com.kook.network.exception.ApiException;
import com.kook.network.exception.ErrorAction;
import com.kook.network.file.FileUploadObserver;
import com.kook.network.file.UploadFileRequestBody;
import com.kook.network.secret.MD5Utils;
import com.kook.network.vo.MessageEntity;
import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody.Part;
import okio.Buffer;
import okio.BufferedSource;

public class FileUploadService {
   private static ApiService mApiService;
   private static final FileUploadService INSTANCE = new FileUploadService();

   public static FileUploadService getInstance(String url) {
      mApiService = (ApiService)FileUploadCreator.getRetrofitClient(url).create(ApiService.class);
      return INSTANCE;
   }

   public void uploadDevices(File file, String deviceNo, String uploadVersion, FileUploadObserver<ResponseBody> fileUploadObserver) {
      Map<String, RequestBody> params = new HashMap();
      params.put(StringFog.decrypt("M0IiLxg="), RequestBody.create(MediaType.parse(StringFog.decrypt("BhoDH0QSFBVZSQ8XGQJCD0wWFA==")), MD5Utils.fileMD5Sync(file)));
      params.put(StringFog.decrypt("DwoZAk4HOwg="), RequestBody.create(MediaType.parse(StringFog.decrypt("BhoDH0QSFBVZSQ8XGQJCD0wWFA==")), deviceNo));
      params.put(StringFog.decrypt("Hh8DBEwGIwJfFQAXBQ=="), RequestBody.create(MediaType.parse(StringFog.decrypt("BhoDH0QSFBVZSQ8XGQJCD0wWFA==")), uploadVersion));
      RequestBody requestFile = RequestBody.create(MediaType.parse(StringFog.decrypt("BhoDH0QSFBVZSQ8XGQJCD0wWFA==")), file);
      MultipartBody.Part body = Part.createFormData(StringFog.decrypt("DQYDDg=="), file.getName(), requestFile);
      mApiService.uploadDevices(body, params).filter((responseBody) -> {
         return this.handleResponse(responseBody);
      }).compose(HttpManager.io_main()).doOnError(new ErrorAction()).subscribe(fileUploadObserver);
   }

   public void uploadAvatar2(File file, String usercode, FileUploadObserver<ResponseBody> fileUploadObserver) {
      Map<String, RequestBody> uploadInfo = new ArrayMap();
      UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(file, fileUploadObserver);
      uploadInfo.put(StringFog.decrypt("AgIODEhATkdLDwUdBQ4CDhBA") + file.getName() + "", uploadFileRequestBody);
      if (!TextUtils.isEmpty(usercode)) {
         uploadInfo.put(StringFog.decrypt("HhwKGU4NEQI="), RequestBody.create(MediaType.parse(StringFog.decrypt("HwoXHwISGQZECA==")), usercode.trim()));
      }

      mApiService.uploadAvatar2(uploadInfo).filter((responseBody) -> {
         return this.handleResponse(responseBody);
      }).compose(HttpManager.io_main()).doOnError(new ErrorAction()).subscribe(fileUploadObserver);
   }

   private boolean handleResponse(ResponseBody responseBody) {
      BufferedSource source = responseBody.source();

      try {
         source.request(Long.MAX_VALUE);
         Buffer buffer = source.buffer();
         Charset charset = Charset.forName(StringFog.decrypt("PjspRhU="));
         MediaType contentType = responseBody.contentType();
         if (contentType != null) {
            charset = contentType.charset(charset);
         }

         String body = buffer.clone().readString(charset);
         HVLog.d(StringFog.decrypt("jfDKjLHpndi5g/LmjvTxjbDHkv2pRkkaBAsWUQ==") + body);
         MessageEntity messageEntity = (MessageEntity)JSON.parseObject(body, MessageEntity.class);
         if (messageEntity.getCode() != 4000) {
            throw new ApiException(messageEntity.getMsg(), String.valueOf(messageEntity.getCode()), messageEntity.getCode());
         } else {
            return true;
         }
      } catch (Exception var8) {
         Exception e = var8;
         e.printStackTrace();
         return false;
      }
   }
}
