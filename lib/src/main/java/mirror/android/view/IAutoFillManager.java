package mirror.android.view;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IAutoFillManager {
   public static Class<?> TYPE = RefClass.load(IAutoFillManager.class, "android.view.autofill.IAutoFillManager");

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, "android.view.autofill.IAutoFillManager$Stub");
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
