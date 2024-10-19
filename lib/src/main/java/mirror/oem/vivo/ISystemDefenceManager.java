package mirror.oem.vivo;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ISystemDefenceManager {
   public static Class<?> TYPE = RefClass.load(ISystemDefenceManager.class, StringFog.decrypt("BQwEGUsPLwNNHAsDHQoDFwAUEwsNOl0qPAsDHQoDNwAUEwsNOj4CARMXDB0="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("BQwEGUsPLwNNHAsDHQoDFwAUEwsNOl0qPAsDHQoDNwAUEwsNOj4CARMXDB1KIBEHFA=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
