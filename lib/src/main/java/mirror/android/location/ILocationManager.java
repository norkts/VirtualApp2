package mirror.android.location;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ILocationManager {
   public static Class<?> TYPE = RefClass.load(ILocationManager.class, StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs7OgoNPgcKABw9CAEPFAAA"));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs7OgoNPgcKABw9CAEPFAAAUjYaKhE="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
