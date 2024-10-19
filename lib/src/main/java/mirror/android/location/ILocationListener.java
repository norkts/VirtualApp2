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
   public static Class<?> TYPE = RefClass.load(ILocationListener.class, "android.location.ILocationListener");
   @MethodParams({Location.class})
   public static RefMethod<Void> onLocationChanged;

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, "android.location.ILocationListener$Stub");
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
