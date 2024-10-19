package mirror.android.location;

import android.location.Location;
import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefStaticMethod;

public class LocationListener {
   public static Class<?> TYPE = RefClass.load(LocationListener.class, "android.location.LocationListener");
   @MethodParams({IBinder.class})
   public static RefMethod<Void> onCellLocationChanged;
   @MethodParams({Location.class})
   public static RefMethod<Void> onLocationChanged;

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, "android.location.LocationListener$Stub");
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
