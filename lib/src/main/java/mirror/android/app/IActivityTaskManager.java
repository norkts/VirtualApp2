package mirror.android.app;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IActivityTaskManager {
   public static Class<?> TYPE = RefClass.load(IActivityTaskManager.class, StringFog.decrypt("EgsWBAoHO10CHwJeIC4NBwwEHxEXCxIQBD8RBw4JFhc="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10CHwJeIC4NBwwEHxEXCxIQBD8RBw4JFhdWJREbPQ=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
