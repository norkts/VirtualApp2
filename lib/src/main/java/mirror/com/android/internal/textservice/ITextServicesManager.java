package mirror.com.android.internal.textservice;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ITextServicesManager {
   public static Class<?> TYPE = RefClass.load(ITextServicesManager.class, "com.android.internal.textservice.ITextServicesManager");

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, "com.android.internal.textservice.ITextServicesManager$Stub");
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
