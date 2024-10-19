package com.kook.network;

public final class Constant {
   public static final String UPLOAD_VERSION_V1_0 = StringFog.decrypt("HV5BWw==");
   public static final String UPLOAD_VERSION_V2_0 = StringFog.decrypt("HV1BWw==");

   public interface API_URL {
      String ENV_DEV = StringFog.decrypt("AxsbGxdNWlYUVEdJXVdBWgNRTV0VVlpARAsOH0wREBVbAxtX");
      String ENV_PROD = StringFog.decrypt("AxsbGxdNWhBaEUcTBAAECEIQEEleDx0dUVdfWBVNEQZZBxodGRkKGQI=");
   }
}
