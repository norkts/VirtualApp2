package mirror.android.os.mount;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IMountService {
   public static Class<?> TYPE = RefClass.load(IMountService.class, StringFog.decrypt("EgsWBAoHO10MHFwDHQAcEgIXWCwjMAYNGyEVGxkHEAA="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10MHFwDHQAcEgIXWCwjMAYNGyEVGxkHEABWJREbPQ=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
