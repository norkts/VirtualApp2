package mirror.android.app;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IUriGrantsManager {
   public static Class<?> TYPE = RefClass.load(IUriGrantsManager.class, StringFog.decrypt("EgsWBAoHO10CHwJeIDocGiIAFwsaLD4CARMXDB0="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10CHwJeIDocGiIAFwsaLD4CARMXDB1KIBEHFA=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
