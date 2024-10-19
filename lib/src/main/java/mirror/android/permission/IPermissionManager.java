package mirror.android.permission;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IPermissionManager {
   public static Class<?> TYPE = RefClass.load(IPermissionManager.class, StringFog.decrypt("EgsWBAoHO10MHFw5OQocHgwBBQwBMT4CARMXDB0="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10MHFw5OQocHgwBBQwBMT4CARMXDB1KIBEHFA=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
