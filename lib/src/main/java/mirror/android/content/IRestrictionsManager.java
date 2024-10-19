package mirror.android.content;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IRestrictionsManager {
   public static Class<?> TYPE = RefClass.load(IRestrictionsManager.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSwgExYaLRoAGxsfBxwjEgsTEQAc"));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSwgExYaLRoAGxsfBxwjEgsTEQAceyAXGhA="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
