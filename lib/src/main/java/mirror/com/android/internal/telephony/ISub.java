package mirror.com.android.internal.telephony;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ISub {
   public static Class<?> TYPE = RefClass.load(ISub.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAKxYPCgIYBgEXXSwhAwc="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAKxYPCgIYBgEXXSwhAwdKDAcWDQ=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
