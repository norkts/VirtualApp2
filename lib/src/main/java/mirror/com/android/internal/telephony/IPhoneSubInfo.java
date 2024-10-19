package mirror.com.android.internal.telephony;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IPhoneSubInfo {
   public static Class<?> TYPE = RefClass.load(IPhoneSubInfo.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAKxYPCgIYBgEXXSwiHgoAOiAWDTseDwA="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAKxYPCgIYBgEXXSwiHgoAOiAWDTseDwBKIBEHFA=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
