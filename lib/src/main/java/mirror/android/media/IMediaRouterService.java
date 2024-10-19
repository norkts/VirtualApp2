package mirror.android.media;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IMediaRouterService {
   public static Class<?> TYPE = RefClass.load(IMediaRouterService.class, StringFog.decrypt("EgsWBAoHO10OChYZCEEnPgAWHwQ8MAYXCgAjDB0YGgYX"));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10OChYZCEEnPgAWHwQ8MAYXCgAjDB0YGgYXUjYaKhE="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
