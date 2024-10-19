package mirror.android.app.usage;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IStorageStatsManager {
   public static Class<?> TYPE = RefClass.load(IStorageStatsManager.class, StringFog.decrypt("EgsWBAoHO10CHwJeHBwPFABcPzYaMAECCBcjHQ4aACgTGAQJOgE="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10CHwJeHBwPFABcPzYaMAECCBcjHQ4aACgTGAQJOgFHPAYFCw=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
