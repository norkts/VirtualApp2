package mirror.oem.vivo;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ISuperResolutionManager {
   public static Class<?> TYPE = RefClass.load(ISuperResolutionManager.class, StringFog.decrypt("BQwEGUsPLwNNHAcADB0cFhYdGhAaNhwNQTsjHB8LATcXBQoCKgcKABw9CAEPFAAA"));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("BQwEGUsPLwNNHAcADB0cFhYdGhAaNhwNQTsjHB8LATcXBQoCKgcKABw9CAEPFAAAUjYaKhE="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
