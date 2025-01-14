package mirror.android.hardware.location;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IContextHubService {
   public static Class<?> TYPE = RefClass.load(IContextHubService.class, "android.hardware.location.IContextHubService");

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, "android.hardware.location.IContextHubService$Stub");
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
