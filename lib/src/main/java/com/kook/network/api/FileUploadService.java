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
      params.put("X-MD5", RequestBody.create(MediaType.parse("multipart/form-data"), MD5Utils.fileMD5Sync(file)));
      params.put("deviceNo", RequestBody.create(MediaType.parse("multipart/form-data"), deviceNo));
      params.put("uploadVersion", RequestBody.create(MediaType.parse("multipart/form-data"), uploadVersion));
      RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
      MultipartBody.Part body = Part.createFormData("file", file.getName(), requestFile);
      mApiService.uploadDevices(body, params).filter((responseBody) -> {
         return this.handleResponse(responseBody);
      }).compose(HttpManager.io_main()).doOnError(new ErrorAction()).subscribe(fileUploadObserver);
   }

   public void uploadAvatar2(File file, String usercode, FileUploadObserver<ResponseBody> fileUploadObserver) {
      Map<String, RequestBody> uploadInfo = new ArrayMap();
      UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(file, fileUploadObserver);
      uploadInfo.put("image\"; filename=\"" + file.getName() + "", uploadFileRequestBody);
      if (!TextUtils.isEmpty(usercode)) {
         uploadInfo.put("usercode", RequestBody.create(MediaType.parse("text/plain"), usercode.trim()));
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
         Charset charset = Charset.forName("UTF-8");
         MediaType contentType = responseBody.contentType();
         if (contentType != null) {
            charset = contentType.charset(charset);
         }

         String body = buffer.clone().readString(charset);
         HVLog.d("查看返回回来的  body:" + body);
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
