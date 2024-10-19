package mirror.android.app;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IUsageStatsManager {
   public static Class<?> TYPE = RefClass.load(IUsageStatsManager.class, StringFog.decrypt("EgsWBAoHO10CHwJeHBwPFABcPzAdPhQGPAYRHRwjEgsTEQAc"));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10CHwJeHBwPFABcPzAdPhQGPAYRHRwjEgsTEQAceyAXGhA="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
