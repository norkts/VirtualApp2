package mirror.android.app;

import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefObject;
import mirror.RefStaticMethod;
import mirror.RefStaticObject;

public class ActivityClient {
   public static Class<?> TYPE = RefClass.load(ActivityClient.class, StringFog.decrypt("EgsWBAoHO10CHwJeKAwaGhMbAhwtMxoGAQY="));
   public static RefStaticMethod<IInterface> getActivityClientController;
   public static RefStaticObject<Object> INTERFACE_SINGLETON;

   public static class ActivityClientControllerSingleton {
      public static Class<?> TYPE = RefClass.load(ActivityClientControllerSingleton.class, StringFog.decrypt("EgsWBAoHO10CHwJeKAwaGhMbAhwtMxoGAQZUKAwaGhMbAhwtMxoGAQYzBgEaAQoeGgAcDBoNCB4VHQAA"));
      public static RefObject<IInterface> mKnownInstance;
   }
}
