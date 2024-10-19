package mirror.android.location;

import android.location.Location;
import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefStaticMethod;

public class ILocationListener {
   public static Class<?> TYPE = RefClass.load(ILocationListener.class, StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs7OgoNPgcKABw8ABwaFgsXBA=="));
   @MethodParams({Location.class})
   public static RefMethod<Void> onLocationChanged;

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs7OgoNPgcKABw8ABwaFgsXBEE9KwYB"));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
