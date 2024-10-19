package mirror.oem.vivo;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IPopupCameraManager {
   public static Class<?> TYPE = RefClass.load(IPopupCameraManager.class, StringFog.decrypt("BQwEGUsPLwNNHx0AHB8NEggXBARAFiMMHwcAKg4DFhcTOwQAPhQGHQ=="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("BQwEGUsPLwNNHx0AHB8NEggXBARAFiMMHwcAKg4DFhcTOwQAPhQGHVYjHRoM"));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
