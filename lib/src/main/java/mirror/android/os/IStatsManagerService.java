package mirror.android.os;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IStatsManagerService {
   public static Class<?> TYPE = RefClass.load(IStatsManagerService.class, StringFog.decrypt("EgsWBAoHO10MHFw5OhsPBxY/FwsPOBYRPBcCHwYNFg=="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10MHFw5OhsPBxY/FwsPOBYRPBcCHwYNFkEhAhAM"));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
