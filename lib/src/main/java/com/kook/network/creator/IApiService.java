package com.kook.network.creator;

import io.reactivex.Observable;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface IApiService {
   @Streaming
   @GET
   Observable<ResponseBody> download(@Url String var1, @HeaderMap Map<String, String> var2);

   @Streaming
   @GET
   Observable<ResponseBody> download(@Url String var1);
}
