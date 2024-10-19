package mirror.android.app;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IAlarmManager {
   public static Class<?> TYPE = RefClass.load(IAlarmManager.class, StringFog.decrypt("EgsWBAoHO10CHwJeIC4CEhcfOwQAPhQGHQ=="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10CHwJeIC4CEhcfOwQAPhQGHVYjHRoM"));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
