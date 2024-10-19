package mirror.android.app;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefStaticMethod;

public class IApplicationThreadOreo {
   public static Class<?> TYPE = RefClass.load(IApplicationThreadOreo.class, StringFog.decrypt("EgsWBAoHO10CHwJeIC4eAwkbFQQaNhwNOxoCDA4K"));
   @MethodReflectParams({"android.os.IBinder", "android.content.pm.ParceledListSlice"})
   public static RefMethod<Void> scheduleServiceArgs;

   public static final class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10CHwJeIC4eAwkbFQQaNhwNOxoCDA4KVzYGAwc="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
