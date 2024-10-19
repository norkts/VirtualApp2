package mirror.com.android.internal.os;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IVibratorManagerService {
   public static Class<?> TYPE = RefClass.load(IVibratorManagerService.class, "android.os.IVibratorManagerService");

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, "android.os.IVibratorManagerService$Stub");
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
