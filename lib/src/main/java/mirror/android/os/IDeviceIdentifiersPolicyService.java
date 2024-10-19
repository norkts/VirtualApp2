package mirror.android.os;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IDeviceIdentifiersPolicyService {
   public static Class<?> TYPE = RefClass.load(IDeviceIdentifiersPolicyService.class, StringFog.decrypt("EgsWBAoHO10MHFw5LQoYGgYXPwELMQcKCRsVGxw+HAkbFRw9OgEVBhEV"));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10MHFw5LQoYGgYXPwELMQcKCRsVGxw+HAkbFRw9OgEVBhEVTTwaBgc="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
