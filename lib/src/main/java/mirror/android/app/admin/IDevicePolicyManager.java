package mirror.android.app.admin;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IDevicePolicyManager {
   public static Class<?> TYPE = RefClass.load(IDevicePolicyManager.class, StringFog.decrypt("EgsWBAoHO10CHwJeCAsDGgtcPyELKRoACiIfBQYNCigTGAQJOgE="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10CHwJeCAsDGgtcPyELKRoACiIfBQYNCigTGAQJOgFHPAYFCw=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
