package mirror.com.android.internal.app;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IBatteryStats {
   public static Class<?> TYPE = RefClass.load(IBatteryStats.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAPgMTQTsyCBsaFhcLJREPKwA="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAPgMTQTsyCBsaFhcLJREPKwBHPAYFCw=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
