package mirror.com.android.internal.telecom;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ITelecomService {
   public static Class<?> TYPE = RefClass.load(ITelecomService.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAKxYPChEfBEEnJwAeEwYBMiAGHQQZCgo="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAKxYPChEfBEEnJwAeEwYBMiAGHQQZCgpKIBEHFA=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
