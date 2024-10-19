package mirror.com.android.internal.telephony;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ITelephonyRegistry {
   public static Class<?> TYPE = RefClass.load(ITelephonyRegistry.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAKxYPCgIYBgEXXSwmEwkLLxsMAQsiDAgHABEADw=="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAKxYPCgIYBgEXXSwmEwkLLxsMAQsiDAgHABEAD0E9KwYB"));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
