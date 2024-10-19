package mirror.com.android.internal.telephony;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IMms {
   public static Class<?> TYPE = RefClass.load(IMms.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAKxYPCgIYBgEXXSw/GxY="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAKxYPCgIYBgEXXSw/GxZKDAcWDQ=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
