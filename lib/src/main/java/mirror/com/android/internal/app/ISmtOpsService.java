package mirror.com.android.internal.app;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ISmtOpsService {
   public static Class<?> TYPE = RefClass.load(ISmtOpsService.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAPgMTQTsjBBshAxYhExcYNhAG"));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAPgMTQTsjBBshAxYhExcYNhAGSyEEHA0="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
