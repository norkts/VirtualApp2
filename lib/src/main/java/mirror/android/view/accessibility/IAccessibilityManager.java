package mirror.android.view.accessibility;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IAccessibilityManager {
   public static Class<?> TYPE = RefClass.load(IAccessibilityManager.class, StringFog.decrypt("EgsWBAoHO10VBhcHRw4NEAABBQwMNh8KGwteIC4NEAABBQwMNh8KGws9CAEPFAAA"));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10VBhcHRw4NEAABBQwMNh8KGwteIC4NEAABBQwMNh8KGws9CAEPFAAAUjYaKhE="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
