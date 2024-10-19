package mirror.android.os;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class INetworkManagementService {
   public static Class<?> TYPE = RefClass.load(INetworkManagementService.class, StringFog.decrypt("EgsWBAoHO10MHFw5JwoaBAoAHSgPMRIECh8VBxs9FhcEHwYL"));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10MHFw5JwoaBAoAHSgPMRIECh8VBxs9FhcEHwYLeyAXGhA="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
