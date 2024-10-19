package mirror.android.content.integrity;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IAppIntegrityManager {
   public static Class<?> TYPE = RefClass.load(IAppIntegrityManager.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXQwcAgAJLRoXFlw5KB8eOgsGEwIcNgcaIhMeCAgLAQ=="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXQwcAgAJLRoXFlw5KB8eOgsGEwIcNgcaIhMeCAgLAUEhAhAM"));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
