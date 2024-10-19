package mirror.android.app;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IWallpaperManager {
   public static Class<?> TYPE = RefClass.load(IWallpaperManager.class, StringFog.decrypt("EgsWBAoHO10CHwJeIDgPHwkCFxULLT4CARMXDB0="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(IUsageStatsManager.Stub.class, StringFog.decrypt("EgsWBAoHO10CHwJeIDgPHwkCFxULLT4CARMXDB1KIBEHFA=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
