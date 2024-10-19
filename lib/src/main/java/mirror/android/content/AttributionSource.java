package mirror.android.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;

public class AttributionSource {
   private static final String TAG = StringFog.decrypt("MhEGBAwMKgcKABwjBhocEAA=");
   public static Class<?> TYPE = RefClass.load(AttributionSource.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSQGAhcHPQYXBh0eOgAbAQYX"));
   public static Class<?> TYPE_COMP = RefClass.load(AttributionSource.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSQGAhcHPQYXBh0eOgAbAQYXUgQX"));
   @MethodParams({Object.class})
   public static RefMethod<Boolean> equals;
   public static RefMethod<String> getAttributionTag;
   public static RefMethod<Object> getNext;
   public static RefMethod<String> getPackageName;
   public static RefMethod<IBinder> getToken;
   @MethodParams({Binder.class})
   public static RefMethod<Parcelable> withToken;
   public static RefObject<Object> mAttributionSourceState;

   public static boolean equals(Object obj, Object obj2) {
      RefMethod<Boolean> method = equals;
      return method == null ? false : (Boolean)method.call(method, obj2);
   }

   public static String getAttributionTag(Object obj) {
      RefMethod<String> method = getAttributionTag;
      return method != null ? (String)method.call(obj) : null;
   }

   public static String getPackageName(Object obj) {
      RefMethod<String> method = getPackageName;
      return method != null ? (String)method.call(obj) : null;
   }

   public static IBinder getToken(Object obj) {
      RefMethod<IBinder> method = getToken;
      return method != null ? (IBinder)method.call(obj) : null;
   }

   public static void mAttributionSourceState(Object obj, Object obj2) {
      RefObject<Object> objRef = mAttributionSourceState;
      if (objRef != null) {
         objRef.set(obj, obj2);
      }

   }

   public static Parcelable newInstance(Object obj) {
      Parcelable withToken2;
      Object mAttributionSourceState2;
      if (obj != null && (withToken2 = withToken(obj, (Binder)null)) != null && (mAttributionSourceState2 = mAttributionSourceState(withToken2)) != null) {
         AttributionSourceState.token(mAttributionSourceState2, getToken(obj));
         Object mAttributionSourceState3 = mAttributionSourceState(obj);
         if (mAttributionSourceState3 != null) {
            AttributionSourceState.next(mAttributionSourceState2, AttributionSourceState.next(mAttributionSourceState3));
         }

         return withToken2;
      } else {
         Log.w(TAG, StringFog.decrypt("HQAFPwsdKxINDBdQGwoKBgYXVgMPNh8GC14CDBsbAQtSGBACMw=="));
         return null;
      }
   }

   public static Parcelable withToken(Object obj, Binder binder) {
      RefMethod<Parcelable> method = withToken;
      return method == null ? null : (Parcelable)method.call(obj, binder);
   }

   public static Object mAttributionSourceState(Object obj) {
      RefObject<Object> objRef = mAttributionSourceState;
      return objRef != null ? objRef.get(obj) : null;
   }

   static {
      if (TYPE_COMP != null) {
      }

      Class<AttributionSource> cls = AttributionSource.class;
      TYPE = RefClass.load(cls, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSQGAhcHPQYXBh0eOgAbAQYX"));
      TYPE_COMP = RefClass.load(cls, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSQGAhcHPQYXBh0eOgAbAQYXUg=="));
   }
}
