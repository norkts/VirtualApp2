package mirror.com.android.internal.os;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class UserManager {
   public static Class<?> TYPE = RefClass.load(UserManager.class, StringFog.decrypt("EgsWBAoHO10MHFwlGgocPgQcFwILLQ=="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10MHFw5PBwLASgTGAQJOgFHPAYFCw=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
