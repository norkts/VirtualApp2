package mirror.android.app.servertransaction;

import android.os.IBinder;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefMethod;

public class ClientTransactionItem {
   public static Class<?> TYPE = RefClass.load(ClientTransactionItem.class, "android.app.servertransaction.ClientTransactionItem");
   public static RefMethod<IBinder> getActivityToken;
}
