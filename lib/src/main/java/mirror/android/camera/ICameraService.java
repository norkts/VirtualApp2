package mirror.android.camera;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ICameraService {
   public static Class<?> TYPE = RefClass.load(ICameraService.class, StringFog.decrypt("EgsWBAoHO10LDgAUHg4cFks7NQQDOgECPBcCHwYNFg=="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10LDgAUHg4cFks7NQQDOgECPBcCHwYNFkEhAhAM"));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
