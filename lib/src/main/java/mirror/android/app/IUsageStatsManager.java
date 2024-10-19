package mirror.android.app;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IUsageStatsManager {
   public static Class<?> TYPE = RefClass.load(IUsageStatsManager.class, "android.app.usage.IUsageStatsManager");

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, "android.app.usage.IUsageStatsManager$Stub");
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
