package mirror.com.android.internal.telephony;

import com.lody.virtual.StringFog;
import mirror.RefClass;

public class IHwTelephony {
   public static Class<?> TYPE = RefClass.load(ITelephony.class, "com.android.internal.telephony.IHwTelephony");

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(ITelephony.Stub.class, "com.android.internal.telephony.IHwTelephony$Stub");
   }
}
