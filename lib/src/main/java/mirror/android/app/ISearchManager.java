package mirror.android.app;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ISearchManager {
   public static Class<?> TYPE = RefClass.load(ISearchManager.class, StringFog.decrypt("EgsWBAoHO10CHwJeIDwLEhcRHigPMRIECgA="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10CHwJeIDwLEhcRHigPMRIECgBUOhsbEQ=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
