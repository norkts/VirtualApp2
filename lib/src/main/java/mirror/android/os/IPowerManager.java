package mirror.android.os;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IPowerManager {
   public static Class<?> TYPE = RefClass.load(IPowerManager.class, StringFog.decrypt("EgsWBAoHO10MHFw5OQAZFhc/FwsPOBYR"));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10MHFw5OQAZFhc/FwsPOBYRSyEEHA0="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
