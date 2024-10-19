package mirror.android.app.job;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IJobScheduler {
   public static Class<?> TYPE = RefClass.load(IJobScheduler.class, StringFog.decrypt("EgsWBAoHO10CHwJeAwAMXSw4GQc9PBsGCwccDB0="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10CHwJeAwAMXSw4GQc9PBsGCwccDB1KIBEHFA=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
