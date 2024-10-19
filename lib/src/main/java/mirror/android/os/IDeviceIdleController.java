package mirror.android.os;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IDeviceIdleController {
   public static Class<?> TYPE = RefClass.load(IDeviceIdleController.class, StringFog.decrypt("EgsWBAoHO10MHFw5LQoYGgYXPwECOjAMAQYCBgMCFhc="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10MHFw5LQoYGgYXPwECOjAMAQYCBgMCFhdWJREbPQ=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
