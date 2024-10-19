package mirror.com.android.internal.os;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IVibratorService {
   public static Class<?> TYPE = RefClass.load(IVibratorService.class, StringFog.decrypt("EgsWBAoHO10MHFw5PwYMAQQGGRc9OgEVBhEV"));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10MHFw5PwYMAQQGGRc9OgEVBhEVTTwaBgc="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
