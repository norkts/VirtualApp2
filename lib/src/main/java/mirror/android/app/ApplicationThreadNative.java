package mirror.android.app;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ApplicationThreadNative {
   public static Class<?> TYPE = RefClass.load(ApplicationThreadNative.class, StringFog.decrypt("EgsWBAoHO10CHwJeKB8eHwwRFxEHMB03BwAVCAsgEhEbAAA="));
   @MethodParams({IBinder.class})
   public static RefStaticMethod<IInterface> asInterface;
}
