package mirror.android.app.servertransaction;

import android.os.IBinder;
import com.lody.virtual.StringFog;
import java.util.List;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;

public class ClientTransaction {
   public static Class<?> TYPE = RefClass.load(ClientTransaction.class, StringFog.decrypt("EgsWBAoHO10CHwJeGgocBQAAAhcPMQACDAYZBgFAMAkbEwsaCwECAQERChsHHAs="));
   public static RefMethod<List<Object>> getTransactionItems;
   public static RefObject<IBinder> mActivityToken;
   public static RefObject<Object> mLifecycleStateRequest;
   public static RefObject<List<Object>> mActivityCallbacks;
}
