package mirror.android.view;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IGraphicsStats {
   public static Class<?> TYPE = RefClass.load(IGraphicsStats.class, StringFog.decrypt("EgsWBAoHO10VBhcHRyYpAQQCHgwNLCAXDgYD"));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10VBhcHRyYpAQQCHgwNLCAXDgYDTTwaBgc="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
