package mirror.android.view.accessibility;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IAccessibilityManager {
   public static Class<?> TYPE = RefClass.load(IAccessibilityManager.class, "android.view.accessibility.IAccessibilityManager");

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, "android.view.accessibility.IAccessibilityManager$Stub");
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
