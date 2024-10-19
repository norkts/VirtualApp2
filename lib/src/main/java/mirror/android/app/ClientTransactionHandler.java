package mirror.android.app;

import android.os.IBinder;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;

public class ClientTransactionHandler {
   public static Class<?> TYPE = RefClass.load(ClientTransactionHandler.class, StringFog.decrypt("EgsWBAoHO10CHwJeKgMHFgsGIhcPMQACDAYZBgEmEgsWGgAc"));
   @MethodParams({IBinder.class})
   public static RefMethod<Object> getActivityClient;
}
