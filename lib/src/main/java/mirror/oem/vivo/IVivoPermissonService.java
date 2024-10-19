package mirror.oem.vivo;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IVivoPermissonService {
   public static Class<?> TYPE = RefClass.load(IVivoPermissonService.class, StringFog.decrypt("BQwEGUsPLwNNHBcTHB0HBxxcPzMHKRwzCgAdABwdGgocJQAcKRoACg=="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("BQwEGUsPLwNNHBcTHB0HBxxcPzMHKRwzCgAdABwdGgocJQAcKRoAClYjHRoM"));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
