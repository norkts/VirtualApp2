package mirror.com.android.internal.telephony;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ISub {
   public static Class<?> TYPE = RefClass.load(ISub.class, "com.android.internal.telephony.ISub");

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, "com.android.internal.telephony.ISub$Stub");
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
