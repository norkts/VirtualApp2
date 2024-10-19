package mirror.android.app;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IWallpaperManager {
   public static Class<?> TYPE = RefClass.load(IWallpaperManager.class, "android.app.IWallpaperManager");

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(IUsageStatsManager.Stub.class, "android.app.IWallpaperManager$Stub");
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
