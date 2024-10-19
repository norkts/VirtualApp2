package mirror.android.bluetooth;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IBluetooth {
   public static Class<?> TYPE = RefClass.load(IBluetooth.class, StringFog.decrypt("EgsWBAoHO10BAwcVHQABBw1cPycCKhYXAB0EAQ=="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10BAwcVHQABBw1cPycCKhYXAB0EAUs9BxAQ"));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
