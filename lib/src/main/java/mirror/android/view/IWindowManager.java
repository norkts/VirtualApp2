package mirror.android.view;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IWindowManager {
   public static Class<?> TYPE = RefClass.load(IWindowManager.class, StringFog.decrypt("EgsWBAoHO10VBhcHRyY5GgsWGRIjPh0CCBcC"));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10VBhcHRyY5GgsWGRIjPh0CCBcCTTwaBgc="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
