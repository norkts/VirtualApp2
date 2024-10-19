package mirror.com.android.internal.app;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ISliceManager {
   public static Class<?> TYPE = RefClass.load(ISliceManager.class, StringFog.decrypt("EgsWBAoHO10CHwJeGgMHEABcPzYCNhAGIhMeCAgLAQ=="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10CHwJeGgMHEABcPzYCNhAGIhMeCAgLAUEhAhAM"));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
