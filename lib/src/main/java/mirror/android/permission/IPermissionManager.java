package mirror.android.permission;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IPermissionManager {
   public static Class<?> TYPE = RefClass.load(IPermissionManager.class, "android.os.IPermissionManager");

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, "android.os.IPermissionManager$Stub");
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
