package mirror.android.location;

import android.location.Location;
import android.os.Bundle;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import java.util.HashMap;
import java.util.Map;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;

public class LocationManager {
   public static Class<?> TYPE = RefClass.load(LocationManager.class, StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs+GQYPKxoMAT8RBw4JFhc="));
   public static RefObject<IInterface> mService;
   public static RefObject<Map> mGnssNmeaListeners;
   public static RefObject<Map> mGnssStatusListeners;
   public static RefObject<Map> mGpsNmeaListeners;
   public static RefObject<Map> mGpsStatusListeners;
   public static RefObject<HashMap> mListeners;
   public static RefObject<HashMap> mNmeaListeners;

   public static class LocationListenerTransport {
      public static Class<?> TYPE = RefClass.load(LocationListenerTransport.class, StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs+GQYPKxoMAT8RBw4JFhdWOgoNPgcKABw8ABwaFgsXBDEcPh0QHx0CHQ=="));
      public static RefObject<android.location.LocationListener> mListener;
      @MethodParams({Location.class})
      public static RefMethod<Void> onLocationChanged;
      @MethodParams({String.class})
      public static RefMethod<Void> onProviderDisabled;
      @MethodParams({String.class})
      public static RefMethod<Void> onProviderEnabled;
      @MethodParams({String.class, int.class, Bundle.class})
      public static RefMethod<Void> onStatusChanged;
      public static RefObject<Object> this$0;
   }

   public static class ListenerTransport {
      public static Class<?> TYPE = RefClass.load(ListenerTransport.class, StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs+GQYPKxoMAT8RBw4JFhdWOgwdKxYNCgAkGw4AABUdBBE="));
      public static RefObject<android.location.LocationListener> mListener;
      @MethodParams({Location.class})
      public static RefMethod<Void> onLocationChanged;
      @MethodParams({String.class})
      public static RefMethod<Void> onProviderDisabled;
      @MethodParams({String.class})
      public static RefMethod<Void> onProviderEnabled;
      @MethodParams({String.class, int.class, Bundle.class})
      public static RefMethod<Void> onStatusChanged;
      public static RefObject<Object> this$0;
   }

   public static class GpsStatusListenerTransportVIVO {
      public static Class<?> TYPE = RefClass.load(GpsStatusListenerTransportVIVO.class, StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs+GQYPKxoMAT8RBw4JFhdWMRUdDAcCGwcDJQYdBwAcExc6LRINHAIfGxs="));
      @MethodParams({int.class, int.class, float.class, float.class, float.class, int.class, int.class, int.class, long.class})
      public static RefMethod<Void> onSvStatusChanged;
   }

   public static class GpsStatusListenerTransportSumsungS5 {
      public static Class<?> TYPE = RefClass.load(GpsStatusListenerTransportSumsungS5.class, StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs+GQYPKxoMAT8RBw4JFhdWMRUdDAcCGwcDJQYdBwAcExc6LRINHAIfGxs="));
      @MethodParams({int.class, int.class, float.class, float.class, float.class, int.class, int.class, int.class, int.class})
      public static RefMethod<Void> onSvStatusChanged;
   }

   public static class GpsStatusListenerTransportOPPO_R815T {
      public static Class<?> TYPE = RefClass.load(GpsStatusListenerTransportOPPO_R815T.class, StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs+GQYPKxoMAT8RBw4JFhdWMRUdDAcCGwcDJQYdBwAcExc6LRINHAIfGxs="));
      @MethodParams({int.class, int.class, float.class, float.class, float.class, int.class, int.class, int.class, int.class})
      public static RefMethod<Void> onSvStatusChanged;
   }

   public static class GpsStatusListenerTransport {
      public static Class<?> TYPE = RefClass.load(GpsStatusListenerTransport.class, StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs+GQYPKxoMAT8RBw4JFhdWMRUdDAcCGwcDJQYdBwAcExc6LRINHAIfGxs="));
      public static RefObject<Object> mListener;
      public static RefObject<Object> mNmeaListener;
      @MethodParams({int.class})
      public static RefMethod<Void> onFirstFix;
      public static RefMethod<Void> onGpsStarted;
      @MethodParams({long.class, String.class})
      public static RefMethod<Void> onNmeaReceived;
      @MethodParams({int.class, int.class, float.class, float.class, float.class, int.class, int.class, int.class})
      public static RefMethod<Void> onSvStatusChanged;
      public static RefObject<Object> this$0;
   }

   public static class GnssStatusListenerTransportO {
      public static Class<?> TYPE = RefClass.load(GnssStatusListenerTransportO.class, StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs+GQYPKxoMAT8RBw4JFhdWMQsdLCAXDgYFGiMHABEXGAAcCwECAQEABh0a"));
      @MethodParams({int.class, int.class, float.class, float.class, float.class, float.class})
      public static RefMethod<Void> onSvStatusChanged;
   }

   public static class GnssStatusListenerTransport {
      public static Class<?> TYPE = RefClass.load(GnssStatusListenerTransport.class, StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs+GQYPKxoMAT8RBw4JFhdWMQsdLCAXDgYFGiMHABEXGAAcCwECAQEABh0a"));
      public static RefObject<Object> mGpsListener;
      public static RefObject<Object> mGpsNmeaListener;
      @MethodParams({int.class})
      public static RefMethod<Void> onFirstFix;
      public static RefMethod<Void> onGnssStarted;
      @MethodParams({long.class, String.class})
      public static RefMethod<Void> onNmeaReceived;
      @MethodParams({int.class, int.class, float.class, float.class, float.class})
      public static RefMethod<Void> onSvStatusChanged;
      public static RefObject<Object> this$0;
   }
}
