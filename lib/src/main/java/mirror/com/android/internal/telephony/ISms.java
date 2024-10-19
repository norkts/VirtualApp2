package mirror.com.android.internal.telephony;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ISms {
   public static Class<?> TYPE = RefClass.load(ISms.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAKxYPCgIYBgEXXSwhGxY="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAKxYPCgIYBgEXXSwhGxZKDAcWDQ=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
