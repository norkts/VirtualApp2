package mirror.android.net.wifi;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IWifiManager {
   public static Class<?> TYPE = RefClass.load(IWifiManager.class, StringFog.decrypt("EgsWBAoHO10NCgZeHgYIGks7IQwINj4CARMXDB0="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10NCgZeHgYIGks7IQwINj4CARMXDB1KIBEHFA=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
