package mirror.android.app.admin;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IDevicePolicyManager {
   public static Class<?> TYPE = RefClass.load(IDevicePolicyManager.class, "android.app.admin.IDevicePolicyManager");

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, "android.app.admin.IDevicePolicyManager$Stub");
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
