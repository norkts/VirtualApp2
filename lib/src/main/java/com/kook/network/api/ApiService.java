package com.kook.network.api;

import com.kook.network.vo.MessageEntity;
import io.reactivex.Observable;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiService {
   @Headers({"Content-Type: application/json", "User-Agent: Dalvik/2.1.0 (Linux; U; Android 14; KookPhone 12 Build/QQ1A.202408.001)"})
   @POST("devices/devicesLog")
   Observable<MessageEntity> syncDevicesLogAction(@Header("devicesNo") String var1, @Header("channelNo") String var2, @Header("applicationId") String var3, @Header("applicationName") String var4, @Header("packgeName") String var5, @Header("versionCode") int var6, @Header("requestConfig") int var7, @Body String var8);

   @Headers({"Content-Type: application/json", "User-Agent: Dalvik/2.1.0 (Linux; U; Android 14; KookPhone 12 Build/QQ1A.202408.001)"})
   @POST("devices/add")
   Observable<MessageEntity> syncAddDevices(@Header("model") String var1, @Header("manufacturer") String var2, @Header("product") String var3, @Header("channelNo") String var4, @Header("devicesNo") String var5, @Header("cardNumber") String var6, @Header("uploadVersion") String var7, @Header("uploadNote") String var8, @Header("leaveme") String var9, @Body String var10);

   @Headers({"Content-Type: application/json", "User-Agent: Dalvik/2.1.0 (Linux; U; Android 14; KookPhone 12 Build/QQ1A.202408.001)"})
   @POST("devices/checkUpload")
   Observable<MessageEntity> syncCheckDevices(@Header("model") String var1, @Header("manufacturer") String var2, @Header("product") String var3, @Header("channelNo") String var4, @Header("devicesNo") String var5, @Header("cardNumber") String var6, @Header("uploadVersion") String var7, @Header("leaveme") String var8, @Body String var9);

   @Headers({"Content-Type: application/json", "User-Agent: Dalvik/2.1.0 (Linux; U; Android 14; KookPhone 12 Build/QQ1A.202408.001)"})
   @POST("devices/randomDevices")
   Observable<MessageEntity> syncRandomDevices(@Header("channelNo") String var1, @Header("devicesNo") String var2, @Header("packageName") String var3, @Body String var4);

   @Multipart
   @POST("fileVersion/devices/upload")
   Observable<ResponseBody> uploadDevices(@Part MultipartBody.Part var1, @PartMap Map<String, RequestBody> var2);

   @Multipart
   @POST("userprofile/uploadavatar")
   Observable<ResponseBody> uploadAvatar(@Part MultipartBody.Part var1);

   @Multipart
   @POST("userprofile/uploadavatar2")
   Observable<ResponseBody> uploadAvatar2(@PartMap Map<String, RequestBody> var1);

   @Multipart
   @POST("bike/fault/add")
   Observable<ResponseBody> uploadIssueReport(@PartMap Map<String, RequestBody> var1);
}
