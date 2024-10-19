package mirror.android.net;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ITetheringConnector {
   public static Class<?> TYPE = RefClass.load(ITetheringConnector.class, StringFog.decrypt("EgsWBAoHO10NCgZeIDsLBw0XBAwAODAMARwVChsBAQ=="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10NCgZeIDsLBw0XBAwAODAMARwVChsBAUEhAhAM"));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
