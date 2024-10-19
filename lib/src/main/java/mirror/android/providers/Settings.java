package mirror.android.providers;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;
import mirror.RefStaticObject;

public class Settings {
   public static Class<?> TYPE = RefClass.load(Settings.class, android.provider.Settings.class);

   public static class Config {
      public static Class<?> TYPE = RefClass.load(Config.class, StringFog.decrypt("EgsWBAoHO10THR0GAAsLAUshExEaNh0EHFYzBgEIGgI="));
      private static RefMethod<Object> getString;

      public static Object getString(ContentResolver contentResolver, String str) {
         RefMethod<Object> refMethod = getString;
         return refMethod == null ? null : refMethod.call(new Object[]{contentResolver, str});
      }
   }

   public static class System {
      public static Class<?> TYPE = RefClass.load(System.class, android.provider.Settings.System.class);
      public static RefStaticObject<Object> sNameValueCache;
   }

   public static class Secure {
      public static Class<?> TYPE = RefClass.load(Secure.class, android.provider.Settings.Secure.class);
      public static RefStaticObject<Object> sNameValueCache;
   }

   public static class ContentProviderHolder {
      public static Class<?> TYPE = RefClass.load(ContentProviderHolder.class, StringFog.decrypt("EgsWBAoHO10THR0GAAsLAUshExEaNh0EHFYzBgEaFgsGJhcBKRoHCgA4BgMKFhc="));
      public static RefObject<IInterface> mContentProvider;
   }

   public static class NameValueCacheOreo {
      public static Class<?> TYPE = RefClass.load(NameValueCacheOreo.class, StringFog.decrypt("EgsWBAoHO10THR0GAAsLAUshExEaNh0EHFY+CAILJQQeAwAtPhALCg=="));
      public static RefObject<Object> mProviderHolder;
   }

   public static class NameValueCache {
      public static Class<?> TYPE = RefClass.load(NameValueCache.class, StringFog.decrypt("EgsWBAoHO10THR0GAAsLAUshExEaNh0EHFY+CAILJQQeAwAtPhALCg=="));
      public static RefObject<Object> mContentProvider;
   }

   @TargetApi(17)
   public static class Global {
      public static Class<?> TYPE = RefClass.load(Global.class, android.provider.Settings.Global.class);
      public static RefStaticObject<Object> sNameValueCache;
   }
}
