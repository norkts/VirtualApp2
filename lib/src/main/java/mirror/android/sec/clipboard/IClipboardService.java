package mirror.android.sec.clipboard;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IClipboardService {
   public static Class<?> TYPE = RefClass.load(IClipboardService.class, "android.sec.clipboard.IClipboardService");

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, "android.sec.clipboard.IClipboardService$Stub");
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
