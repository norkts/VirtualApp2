package mirror.android.app.usage;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IStorageStatsManager {
   public static Class<?> TYPE = RefClass.load(IStorageStatsManager.class, "android.app.usage.IStorageStatsManager");

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, "android.app.usage.IStorageStatsManager$Stub");
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
