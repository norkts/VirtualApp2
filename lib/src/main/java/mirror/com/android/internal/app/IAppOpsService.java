package mirror.com.android.internal.app;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IAppOpsService {
   public static Class<?> TYPE = RefClass.load(IAppOpsService.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAPgMTQTsxGR8hAxYhExcYNhAG"));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAPgMTQTsxGR8hAxYhExcYNhAGSyEEHA0="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
